package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CustomerProfileServiceTest {

    @TempDir
    Path tempDirectory;

    private final PrintStream originalOut = System.out;

    private Path customersFile;
    private Path rentalsFile;

    private CustomerFileService customerFileService;
    private CustomerProfileService service;

    @BeforeEach
    void setUp() throws IOException {

        customersFile =
                tempDirectory.resolve("customers.txt");

        rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        Files.writeString(customersFile, "");
        Files.writeString(rentalsFile, "");

        customerFileService =
                new CustomerFileService(
                        customersFile.toString()
                );

        RentalFileService rentalFileService =
                new RentalFileService(
                        rentalsFile.toString()
                );

        service = new CustomerProfileService(
                new CustomerValidator(),
                customerFileService,
                rentalFileService,
                new LicenseService()
        );
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    void displayCustomerInfoShouldPrintAllCustomerData() {

        CustomerData customer =
                createCustomer();

        ByteArrayOutputStream output =
                captureOutput();

        service.displayCustomerInfo(customer);

        String printed = output.toString();

        assertTrue(printed.contains("ID: 1234567"));
        assertTrue(printed.contains("Name: Hala"));
        assertTrue(
                printed.contains("Email: hala@email.com")
        );
        assertTrue(
                printed.contains("Phone: 0591234567")
        );
        assertTrue(printed.contains("Payment: Cash"));
        assertTrue(
                printed.contains("Licenses: [Car]")
        );
    }

    @Test
    void updateEmailShouldUpdateValidEmail() {

        CustomerData customer =
                createCustomer();

        Scanner input =
                scannerFor("new@email.com");

        service.updateEmail(input, customer);

        assertEquals(
                "new@email.com",
                customer.getEmail()
        );
    }

    @Test
    void updateEmailShouldRejectInvalidEmail() {

        CustomerData customer =
                createCustomer();

        Scanner input = scannerFor(
                "invalidEmail",
                "new@email.com"
        );

        ByteArrayOutputStream output =
                captureOutput();

        service.updateEmail(input, customer);

        assertEquals(
                "new@email.com",
                customer.getEmail()
        );

        assertTrue(
                output.toString().contains(
                        "Invalid email format!"
                )
        );
    }

    @Test
    void updatePhoneShouldUpdateValidPhone() {

        CustomerData customer =
                createCustomer();

        Scanner input =
                scannerFor("0561234567");

        service.updatePhone(input, customer);

        assertEquals(
                "0561234567",
                customer.getPhone()
        );
    }

    @Test
    void updatePhoneShouldRejectInvalidPhone() {

        CustomerData customer =
                createCustomer();

        Scanner input = scannerFor(
                "123",
                "0561234567"
        );

        ByteArrayOutputStream output =
                captureOutput();

        service.updatePhone(input, customer);

        assertEquals(
                "0561234567",
                customer.getPhone()
        );

        assertTrue(
                output.toString().contains(
                        "Invalid phone number!"
                )
        );
    }

    @Test
    void updatePaymentShouldSetCash() {

        CustomerData customer =
                createCustomer();

        Scanner input = scannerFor("1");

        service.updatePayment(input, customer);

        assertEquals(1, customer.getPayment());
    }

    @Test
    void updatePaymentShouldSetVisa() {

        CustomerData customer =
                createCustomer();

        Scanner input = scannerFor("2");

        service.updatePayment(input, customer);

        assertEquals(2, customer.getPayment());
    }

    @Test
    void updatePaymentShouldRejectInvalidChoice() {

        CustomerData customer =
                createCustomer();

        Scanner input = scannerFor(
                "9",
                "2"
        );

        ByteArrayOutputStream output =
                captureOutput();

        service.updatePayment(input, customer);

        assertEquals(2, customer.getPayment());

        assertTrue(
                output.toString().contains(
                        "Invalid choice!"
                )
        );
    }

    @Test
    void readIntShouldRejectNonNumericInput() {

        Scanner input = scannerFor(
                "abc",
                "5"
        );

        ByteArrayOutputStream output =
                captureOutput();

        int result = service.readInt(input);

        assertEquals(5, result);

        assertTrue(
                output.toString().contains(
                        "Invalid input! Please enter a number."
                )
        );
    }

    @Test
    void editCustomerInfoShouldUpdateEmailAndSave()
            throws IOException {

        CustomerData customer =
                createCustomer();

        customerFileService.saveCustomer(customer);

        Scanner input = scannerFor(
                "1",
                "updated@email.com",
                "5"
        );

        service.editCustomerInfo(
                input,
                customer
        );

        CustomerData saved =
                customerFileService.getCustomerById(
                        "1234567"
                );

        assertNotNull(saved);
        assertEquals(
                "updated@email.com",
                saved.getEmail()
        );
    }

    @Test
    void editCustomerInfoShouldUpdatePhoneAndSave()
            throws IOException {

        CustomerData customer =
                createCustomer();

        customerFileService.saveCustomer(customer);

        Scanner input = scannerFor(
                "2",
                "0569999999",
                "5"
        );

        service.editCustomerInfo(
                input,
                customer
        );

        CustomerData saved =
                customerFileService.getCustomerById(
                        "1234567"
                );

        assertNotNull(saved);
        assertEquals(
                "0569999999",
                saved.getPhone()
        );
    }

    @Test
    void editCustomerInfoShouldUpdatePaymentAndSave()
            throws IOException {

        CustomerData customer =
                createCustomer();

        customerFileService.saveCustomer(customer);

        Scanner input = scannerFor(
                "3",
                "2",
                "5"
        );

        service.editCustomerInfo(
                input,
                customer
        );

        CustomerData saved =
                customerFileService.getCustomerById(
                        "1234567"
                );

        assertNotNull(saved);
        assertEquals(
                2,
                saved.getPayment()
        );
    }

    @Test
    void editCustomerInfoShouldUpdateLicensesAndSave()
            throws IOException {

        CustomerData customer =
                createCustomer();

        customerFileService.saveCustomer(customer);

        Scanner input = scannerFor(
                "4",
                "2",
                "no",
                "5"
        );

        service.editCustomerInfo(
                input,
                customer
        );

        CustomerData saved =
                customerFileService.getCustomerById(
                        "1234567"
                );

        assertNotNull(saved);
        assertEquals(
                1,
                saved.getLicenses().size()
        );
        assertEquals(
                "Motorcycle",
                saved.getLicenses().get(0)
        );
    }

    @Test
    void editCustomerInfoShouldRejectInvalidMenuChoice() {

        CustomerData customer =
                createCustomer();

        Scanner input = scannerFor(
                "9",
                "5"
        );

        ByteArrayOutputStream output =
                captureOutput();

        service.editCustomerInfo(
                input,
                customer
        );

        assertTrue(
                output.toString().contains(
                        "Invalid choice!"
                )
        );
    }

    @Test
    void handleExistingCustomerShouldGoBackWhenCustomerNotFound() {

        Scanner input =
                scannerFor("9999999");

        AtomicBoolean backCalled =
                new AtomicBoolean(false);

        AtomicBoolean rentalCalled =
                new AtomicBoolean(false);

        ByteArrayOutputStream output =
                captureOutput();

        service.handleExistingCustomer(
                input,
                (licenses, id, name, phone,email) ->
                        rentalCalled.set(true),
                () -> backCalled.set(true)
        );

        assertTrue(backCalled.get());
        assertFalse(rentalCalled.get());

        assertTrue(
                output.toString().contains(
                        "Customer not found!"
                )
        );
    }

    @Test
    void handleExistingCustomerShouldStartRental()
            throws IOException {

        CustomerData customer =
                createCustomer();

        customerFileService.saveCustomer(customer);

        Scanner input = scannerFor(
                "1234567",
                "1"
        );

        AtomicBoolean rentalCalled =
                new AtomicBoolean(false);

        AtomicBoolean backCalled =
                new AtomicBoolean(false);

        service.handleExistingCustomer(
                input,
                (licenses, id, name, phone,email) -> {
                    rentalCalled.set(true);

                    assertEquals(
                            "1234567",
                            id
                    );
                    assertEquals(
                            "Hala",
                            name
                    );
                    assertEquals(
                            "0591234567",
                            phone
                    );
                    assertEquals(
                            "Car",
                            licenses.get(0)
                    );
                },
                () -> backCalled.set(true)
        );

        assertTrue(rentalCalled.get());
        assertFalse(backCalled.get());
    }

    @Test
    void handleExistingCustomerShouldGoBackToMain()
            throws IOException {

        CustomerData customer =
                createCustomer();

        customerFileService.saveCustomer(customer);

        Scanner input = scannerFor(
                "1234567",
                "3"
        );

        AtomicBoolean backCalled =
                new AtomicBoolean(false);

        service.handleExistingCustomer(
                input,
                (licenses, id, name, phone,email) -> {
                },
                () -> backCalled.set(true)
        );

        assertTrue(backCalled.get());
    }

    @Test
    void handleExistingCustomerShouldRejectInvalidChoiceThenGoBack()
            throws IOException {

        CustomerData customer =
                createCustomer();

        customerFileService.saveCustomer(customer);

        Scanner input = scannerFor(
                "1234567",
                "9",
                "3"
        );

        AtomicBoolean backCalled =
                new AtomicBoolean(false);

        ByteArrayOutputStream output =
                captureOutput();

        service.handleExistingCustomer(
                input,
                (licenses, id, name, phone,email) -> {
                },
                () -> backCalled.set(true)
        );

        assertTrue(backCalled.get());

        assertTrue(
                output.toString().contains(
                        "Invalid choice!"
                )
        );
    }

    private CustomerData createCustomer() {

        ArrayList<String> licenses =
                new ArrayList<>();

        licenses.add("Car");

        return new CustomerData(
                "1234567",
                "Hala",
                "hala@email.com",
                "0591234567",
                1,
                licenses
        );
    }

    private Scanner scannerFor(
            String... values) {

        String text =
                String.join("\n", values);

        if (!text.isEmpty()) {
            text += "\n";
        }

        return new Scanner(
                new ByteArrayInputStream(
                        text.getBytes()
                )
        );
    }

    private ByteArrayOutputStream captureOutput() {

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(
                new PrintStream(output)
        );

        return output;
    }
    
    
}