package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CustomerRegistrationServiceTest {

    @TempDir
    Path tempDirectory;

    private CustomerRegistrationService service;
    private CustomerFileService customerFileService;
    private Path customersFile;

    @BeforeEach
    void setUp() {

        customersFile =
                tempDirectory.resolve("customers.txt");

        customerFileService =
                new CustomerFileService(
                        customersFile.toString()
                );

        service = new CustomerRegistrationService(
                new CustomerValidator(),
                customerFileService,
                new LicenseService()
        );
    }

    @Test
    void registerCustomerShouldCreateAndSaveCustomer()
            throws IOException {

        Scanner input = scannerFor(
                "Hala",
                "1234567",
                "hala@email.com",
                "0591234567",
                "1",
                "1",
                "no"
        );

        CustomerData customer =
                service.registerCustomer(input);

        assertNotNull(customer);
        assertEquals("Hala", customer.getName());
        assertEquals("1234567", customer.getId());
        assertEquals(
                "hala@email.com",
                customer.getEmail()
        );
        assertEquals(
                "0591234567",
                customer.getPhone()
        );
        assertEquals(1, customer.getPayment());

        assertEquals(1, customer.getLicenses().size());
        assertEquals(
                "Car",
                customer.getLicenses().get(0)
        );

        String fileContent =
                Files.readString(customersFile);

        assertTrue(
                fileContent.contains("ID: 1234567")
        );
        assertTrue(
                fileContent.contains("Name: Hala")
        );
        assertTrue(
                fileContent.contains(
                        "Email: hala@email.com"
                )
        );
        assertTrue(
                fileContent.contains("Payment: Cash")
        );
        assertTrue(
                fileContent.contains("License: [Car]")
        );
    }

    @Test
    void registerCustomerShouldSaveVisaAndMultipleLicenses()
            throws IOException {

        Scanner input = scannerFor(
                "Ali",
                "7654321",
                "ali@email.com",
                "0561234567",
                "2",
                "1",
                "yes",
                "1",
                "no"
        );

        CustomerData customer =
                service.registerCustomer(input);

        assertEquals(2, customer.getPayment());
        assertEquals(2, customer.getLicenses().size());
        assertEquals(
                "Car",
                customer.getLicenses().get(0)
        );
        assertEquals(
                "Motorcycle",
                customer.getLicenses().get(1)
        );

        String content =
                Files.readString(customersFile);

        assertTrue(content.contains("Payment: Visa"));
        assertTrue(
                content.contains(
                        "License: [Car, Motorcycle]"
                )
        );
    }

    @Test
    void readValidNameShouldRejectInvalidNames() {

        Scanner input = scannerFor(
                "Hala123",
                "",
                "Hala"
        );

        String result =
                service.readValidName(input);

        assertEquals("Hala", result);
    }

    @Test
    void readValidIdShouldRejectInvalidId() {

        Scanner input = scannerFor(
                "123",
                "abcdefg",
                "1234567"
        );

        String result =
                service.readUniqueValidId(input);

        assertEquals("1234567", result);
    }

    @Test
    void readValidIdShouldRejectExistingId()
            throws IOException {

        Files.writeString(
                customersFile,
                "ID: 1234567\n"
                        + "Name: Existing\n"
                        + "----------------------\n"
        );

        Scanner input = scannerFor(
                "1234567",
                "7654321"
        );

        String result =
                service.readUniqueValidId(input);

        assertEquals("7654321", result);
    }

    @Test
    void readValidEmailShouldRejectInvalidEmail() {

        Scanner input = scannerFor(
                "halaemail.com",
                "hala@email",
                "hala@email.com"
        );

        String result =
                service.readValidEmail(input);

        assertEquals("hala@email.com", result);
    }

    @Test
    void readValidPhoneShouldRejectInvalidPhone() {

        Scanner input = scannerFor(
                "059123",
                "05912abc67",
                "0591234567"
        );

        String result =
                service.readValidPhone(input);

        assertEquals("0591234567", result);
    }

    @Test
    void readValidPaymentShouldAcceptCash() {

        Scanner input = scannerFor("1");

        int result =
                service.readValidPayment(input);

        assertEquals(1, result);
    }

    @Test
    void readValidPaymentShouldAcceptVisa() {

        Scanner input = scannerFor("2");

        int result =
                service.readValidPayment(input);

        assertEquals(2, result);
    }

    @Test
    void readValidPaymentShouldRejectInvalidChoice() {

        Scanner input = scannerFor(
                "3",
                "0",
                "1"
        );

        int result =
                service.readValidPayment(input);

        assertEquals(1, result);
    }

    @Test
    void readIntShouldRejectNonNumericInput() {

        Scanner input = scannerFor(
                "abc",
                "five",
                "5"
        );

        int result =
                service.readInt(input);

        assertEquals(5, result);
    }

    private Scanner scannerFor(String... values) {

        String text = String.join("\n", values);

        if (!text.isEmpty()) {
            text += "\n";
        }

        return new Scanner(
                new ByteArrayInputStream(
                        text.getBytes()
                )
        );
    }
}