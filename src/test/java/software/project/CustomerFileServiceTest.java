package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CustomerFileServiceTest {

    @TempDir
    Path tempDirectory;

    @Test
    void saveCustomerShouldWriteCustomerData() throws IOException {
        Path file = tempDirectory.resolve("customers.txt");

        CustomerFileService service =
                new CustomerFileService(file.toString());

        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("Car");
        licenses.add("Bus");

        CustomerData customer = new CustomerData(
                "1234567",
                "Hala",
                "hala@email.com",
                "0591234567",
                1,
                licenses
        );

        service.saveCustomer(customer);

        String content = Files.readString(file);

        assertTrue(content.contains("ID: 1234567"));
        assertTrue(content.contains("Name: Hala"));
        assertTrue(content.contains("Email: hala@email.com"));
        assertTrue(content.contains("Phone: 0591234567"));
        assertTrue(content.contains("Payment: Cash"));
        assertTrue(content.contains("License: [Car, Bus]"));
    }

    @Test
    void saveCustomerShouldWriteVisaPayment() throws IOException {
        Path file = tempDirectory.resolve("customers.txt");

        CustomerFileService service =
                new CustomerFileService(file.toString());

        CustomerData customer = new CustomerData(
                "7654321",
                "Ali",
                "ali@email.com",
                "0561234567",
                2,
                new ArrayList<>()
        );

        service.saveCustomer(customer);

        String content = Files.readString(file);

        assertTrue(content.contains("Payment: Visa"));
    }

    @Test
    void existingIdShouldNotBeUnique() throws IOException {
        Path file = tempDirectory.resolve("customers.txt");

        Files.writeString(
                file,
                "ID: 1234567\n"
                        + "Name: Hala\n"
                        + "----------------------\n"
        );

        CustomerFileService service =
                new CustomerFileService(file.toString());

        assertFalse(service.isIdUnique("1234567"));
    }

    @Test
    void newIdShouldBeUnique() throws IOException {
        Path file = tempDirectory.resolve("customers.txt");

        Files.writeString(
                file,
                "ID: 1234567\n"
                        + "Name: Hala\n"
                        + "----------------------\n"
        );

        CustomerFileService service =
                new CustomerFileService(file.toString());

        assertTrue(service.isIdUnique("7654321"));
    }

    @Test
    void missingFileShouldTreatIdAsUnique() {
        Path file = tempDirectory.resolve("missing.txt");

        CustomerFileService service =
                new CustomerFileService(file.toString());

        assertTrue(service.isIdUnique("1234567"));
    }

    @Test
    void getCustomerByIdShouldReturnCustomer() throws IOException {
        Path file = tempDirectory.resolve("customers.txt");

        Files.writeString(
                file,
                "ID: 1234567\n"
                        + "Name: Hala\n"
                        + "Email: hala@email.com\n"
                        + "Phone: 0591234567\n"
                        + "Payment: Cash\n"
                        + "License: [Car, Bus]\n"
                        + "----------------------\n"
        );

        CustomerFileService service =
                new CustomerFileService(file.toString());

        CustomerData customer =
                service.getCustomerById("1234567");

        assertNotNull(customer);
        assertEquals("Hala", customer.getName());
        assertEquals("hala@email.com", customer.getEmail());
        assertEquals("0591234567", customer.getPhone());
        assertEquals(1, customer.getPayment());
        assertEquals(2, customer.getLicenses().size());
    }

    @Test
    void getCustomerByIdShouldReadVisaPayment() throws IOException {
        Path file = tempDirectory.resolve("customers.txt");

        Files.writeString(
                file,
                "ID: 7654321\n"
                        + "Name: Ali\n"
                        + "Email: ali@email.com\n"
                        + "Phone: 0561234567\n"
                        + "Payment: Visa\n"
                        + "License: [Truck]\n"
                        + "----------------------\n"
        );

        CustomerFileService service =
                new CustomerFileService(file.toString());

        CustomerData customer =
                service.getCustomerById("7654321");

        assertNotNull(customer);
        assertEquals(2, customer.getPayment());
        assertEquals("Truck", customer.getLicenses().get(0));
    }

    @Test
    void unknownCustomerShouldReturnNull() throws IOException {
        Path file = tempDirectory.resolve("customers.txt");

        Files.writeString(
                file,
                "ID: 1234567\n"
                        + "Name: Hala\n"
                        + "----------------------\n"
        );

        CustomerFileService service =
                new CustomerFileService(file.toString());

        assertNull(service.getCustomerById("9999999"));
    }

    @Test
    void parseLicensesShouldReturnValues() {
        CustomerFileService service =
                new CustomerFileService("unused.txt");

        ArrayList<String> result =
                service.parseLicenses("[Car, Motorcycle, Bus]");

        assertEquals(3, result.size());
        assertEquals("Car", result.get(0));
        assertEquals("Motorcycle", result.get(1));
        assertEquals("Bus", result.get(2));
    }

    @Test
    void parseEmptyLicensesShouldReturnEmptyList() {
        CustomerFileService service =
                new CustomerFileService("unused.txt");

        assertTrue(service.parseLicenses("[]").isEmpty());
    }

    @Test
    void updateCustomerShouldReplaceExistingCustomer()
            throws IOException {

        Path file = tempDirectory.resolve("customers.txt");

        Files.writeString(
                file,
                "ID: 1234567\n"
                        + "Name: Hala\n"
                        + "Email: old@email.com\n"
                        + "Phone: 0591234567\n"
                        + "Payment: Cash\n"
                        + "License: [Car]\n"
                        + "----------------------\n"
        );

        CustomerFileService service =
                new CustomerFileService(file.toString());

        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("Bus");

        CustomerData updated = new CustomerData(
                "1234567",
                "Hala",
                "new@email.com",
                "0599999999",
                2,
                licenses
        );

        service.updateCustomer(updated);

        CustomerData result =
                service.getCustomerById("1234567");

        assertNotNull(result);
        assertEquals("new@email.com", result.getEmail());
        assertEquals("0599999999", result.getPhone());
        assertEquals(2, result.getPayment());
        assertEquals("Bus", result.getLicenses().get(0));
    }

    @Test
    void updateCustomerShouldAddCustomerWhenNotFound()
            throws IOException {

        Path file = tempDirectory.resolve("customers.txt");

        Files.writeString(file, "");

        CustomerFileService service =
                new CustomerFileService(file.toString());

        CustomerData customer = new CustomerData(
                "1234567",
                "Hala",
                "hala@email.com",
                "0591234567",
                1,
                new ArrayList<>()
        );

        service.updateCustomer(customer);

        assertNotNull(service.getCustomerById("1234567"));
    }
}