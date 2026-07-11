package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class LicenseServiceTest {

    private final LicenseService service = new LicenseService();

    @Test
    void getAllLicensesShouldReturnAllFourTypes() {
        ArrayList<String> result = service.getAllLicenses();

        assertEquals(4, result.size());
        assertEquals("Car", result.get(0));
        assertEquals("Motorcycle", result.get(1));
        assertEquals("Truck", result.get(2));
        assertEquals("Bus", result.get(3));
    }

    @Test
    void chooseLicensesShouldReturnOneLicense() {
        Scanner input = scannerFor(
                "1",
                "no"
        );

        ArrayList<String> result =
                service.chooseLicenses(input);

        assertEquals(1, result.size());
        assertEquals("Car", result.get(0));
    }

    @Test
    void chooseLicensesShouldReturnMultipleLicenses() {
        Scanner input = scannerFor(
                "1",
                "yes",
                "1",
                "no"
        );

        ArrayList<String> result =
                service.chooseLicenses(input);

        assertEquals(2, result.size());
        assertEquals("Car", result.get(0));
        assertEquals("Motorcycle", result.get(1));
    }

    @Test
    void chooseLicensesShouldAllowSelectingAllLicenses() {
        Scanner input = scannerFor(
                "1",
                "yes",
                "1",
                "yes",
                "1",
                "yes",
                "1"
        );

        ArrayList<String> result =
                service.chooseLicenses(input);

        assertEquals(4, result.size());
        assertEquals("Car", result.get(0));
        assertEquals("Motorcycle", result.get(1));
        assertEquals("Truck", result.get(2));
        assertEquals("Bus", result.get(3));
    }

    @Test
    void chooseLicensesShouldRejectInvalidLicenseChoice() {
        Scanner input = scannerFor(
                "9",
                "1",
                "no"
        );

        ArrayList<String> result =
                service.chooseLicenses(input);

        assertEquals(1, result.size());
        assertEquals("Car", result.get(0));
    }

    @Test
    void chooseLicensesShouldRejectInvalidYesNoAnswer() {
        Scanner input = scannerFor(
                "1",
                "maybe",
                "no"
        );

        ArrayList<String> result =
                service.chooseLicenses(input);

        assertEquals(1, result.size());
        assertEquals("Car", result.get(0));
    }

    @Test
    void chooseOneLicenseShouldReturnOnlyLicenseWithoutInput() {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("Truck");

        Scanner input = scannerFor();

        String result =
                service.chooseOneLicense(input, licenses);

        assertEquals("Truck", result);
    }

    @Test
    void chooseOneLicenseShouldReturnSelectedLicense() {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("Car");
        licenses.add("Bus");

        Scanner input = scannerFor("2");

        String result =
                service.chooseOneLicense(input, licenses);

        assertEquals("Bus", result);
    }

    @Test
    void chooseOneLicenseShouldRejectInvalidChoice() {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("Car");
        licenses.add("Motorcycle");

        Scanner input = scannerFor(
                "5",
                "2"
        );

        String result =
                service.chooseOneLicense(input, licenses);

        assertEquals("Motorcycle", result);
    }

    @Test
    void chooseOneLicenseShouldRejectEmptyList() {
        ArrayList<String> licenses = new ArrayList<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.chooseOneLicense(
                        scannerFor(),
                        licenses
                )
        );
    }

    @Test
    void chooseOneLicenseShouldRejectNullList() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.chooseOneLicense(
                        scannerFor(),
                        null
                )
        );
    }

    private Scanner scannerFor(String... values) {
        String inputText = String.join("\n", values);

        if (!inputText.isEmpty()) {
            inputText += "\n";
        }

        return new Scanner(
                new ByteArrayInputStream(
                        inputText.getBytes()
                )
        );
    }
}