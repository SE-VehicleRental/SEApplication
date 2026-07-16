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
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CustomerRentalServiceTest {

    @TempDir
    Path tempDirectory;

    private final PrintStream originalOut = System.out;

    private Path vehiclesFile;
    private Path rentalsFile;

    private CustomerRentalService service;

    private AtomicBoolean confirmationOpened;
    private AtomicReference<Double> capturedTotalCost;
    private AtomicReference<Long> capturedRentalDays;

    @BeforeEach
    void setUp() throws IOException {

        vehiclesFile =
                tempDirectory.resolve("AddingVEHICLE.txt");

        rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        Files.writeString(rentalsFile, "");

        RentalFileService rentalFileService =
                new RentalFileService(
                        rentalsFile.toString()
                );

        VehicleFileService vehicleFileService =
                new VehicleFileService(
                        vehiclesFile.toString(),
                        rentalFileService
                );

        VehicleBrowsingService browsingService =
                new VehicleBrowsingService(
                        vehicleFileService
                );

        confirmationOpened =
                new AtomicBoolean(false);

        capturedTotalCost =
                new AtomicReference<>();

        capturedRentalDays =
                new AtomicReference<>();

        service = new CustomerRentalService(
                new RentalCalculator(),
                rentalFileService,
                vehicleFileService,
                new LicenseService(),
                browsingService,
                (
                        vehicle,
                        customerId,
                        customerName,
                        customerPhone,
                        startDate,
                        endDate,
                        rentalDays,
                        totalCost) -> {

                    confirmationOpened.set(true);
                    capturedTotalCost.set(totalCost);
                    capturedRentalDays.set(rentalDays);
                }
        );
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    void readIntShouldReturnValidNumber() {

        Scanner input =
                scannerFor("5");

        assertEquals(
                5,
                service.readInt(input)
        );
    }

    @Test
    void readIntShouldRejectNonNumericInput() {

        Scanner input = scannerFor(
                "abc",
                "three",
                "3"
        );

        ByteArrayOutputStream output =
                captureOutput();

        int result =
                service.readInt(input);

        assertEquals(3, result);

        assertTrue(
                output.toString().contains(
                        "Invalid input! Please enter a number."
                )
        );
    }

    @Test
    void noAvailableVehiclesShouldReturnToMain()
            throws IOException {

        Files.writeString(
                vehiclesFile,
                "1,Bus,Mercedes,ABC-123,White,V100,2024,100.0\n"
        );

        ArrayList<String> licenses =
                new ArrayList<>();

        licenses.add("Car");

        AtomicBoolean backCalled =
                new AtomicBoolean(false);

        ByteArrayOutputStream output =
                captureOutput();

        service.rentVehicle(
                scannerFor(),
                licenses,
                "1234567",
                "Hala",
                "0591234567",
                () -> backCalled.set(true)
        );

        assertTrue(backCalled.get());
        assertFalse(confirmationOpened.get());

        assertTrue(
                output.toString().contains(
                        "No available vehicles for this license."
                )
        );
    }

    @Test
    void validRentalShouldCalculateCorrectTotal()
            throws IOException {

        Files.writeString(
                vehiclesFile,
                "1,Car,BMW,ABC-123,Black,V100,2024,100.0\n"
        );

        ArrayList<String> licenses =
                new ArrayList<>();

        licenses.add("Car");

        Scanner input = scannerFor(
                "1",
                "2026-07-10",
                "2026-07-12"
        );

        AtomicBoolean backCalled =
                new AtomicBoolean(false);

        service.rentVehicle(
                input,
                licenses,
                "1234567",
                "Hala",
                "0591234567",
                () -> backCalled.set(true)
        );

        assertTrue(confirmationOpened.get());
        assertFalse(backCalled.get());

        assertEquals(
                3L,
                capturedRentalDays.get()
        );

        assertEquals(
                300.0,
                capturedTotalCost.get(),
                0.001
        );
    }

    @Test
    void invalidVehicleIdShouldAskAgain()
            throws IOException {

        Files.writeString(
                vehiclesFile,
                "1,Car,BMW,ABC-123,Black,V100,2024,50.0\n"
        );

        ArrayList<String> licenses =
                new ArrayList<>();

        licenses.add("Car");

        Scanner input = scannerFor(
                "99",
                "1",
                "2026-07-10",
                "2026-07-11"
        );

        ByteArrayOutputStream output =
                captureOutput();

        service.rentVehicle(
                input,
                licenses,
                "1234567",
                "Hala",
                "0591234567",
                () -> {
                }
        );

        assertTrue(confirmationOpened.get());

        assertTrue(
                output.toString().contains(
                        "Invalid Vehicle ID!"
                )
        );

        assertEquals(
                100.0,
                capturedTotalCost.get(),
                0.001
        );
    }

    @Test
    void invalidDateFormatShouldAskAgain()
            throws IOException {

        Files.writeString(
                vehiclesFile,
                "1,Car,BMW,ABC-123,Black,V100,2024,75.0\n"
        );

        ArrayList<String> licenses =
                new ArrayList<>();

        licenses.add("Car");

        Scanner input = scannerFor(
                "1",
                "invalid-date",
                "2026-07-10",
                "2026-07-12"
        );

        ByteArrayOutputStream output =
                captureOutput();

        service.rentVehicle(
                input,
                licenses,
                "1234567",
                "Hala",
                "0591234567",
                () -> {
                }
        );

        assertTrue(confirmationOpened.get());

        assertTrue(
                output.toString().contains(
                        "Invalid date!"
                )
        );

        assertEquals(
                225.0,
                capturedTotalCost.get(),
                0.001
        );
    }

    @Test
    void endDateBeforeStartDateShouldAskAgain()
            throws IOException {

        Files.writeString(
                vehiclesFile,
                "1,Car,BMW,ABC-123,Black,V100,2024,80.0\n"
        );

        ArrayList<String> licenses =
                new ArrayList<>();

        licenses.add("Car");

        Scanner input = scannerFor(
                "1",
                "2026-07-12",
                "2026-07-10",
                "2026-07-10",
                "2026-07-12"
        );

        ByteArrayOutputStream output =
                captureOutput();

        service.rentVehicle(
                input,
                licenses,
                "1234567",
                "Hala",
                "0591234567",
                () -> {
                }
        );

        assertTrue(confirmationOpened.get());

        assertTrue(
                output.toString().contains(
                        "Invalid date!"
                )
        );

        assertEquals(
                240.0,
                capturedTotalCost.get(),
                0.001
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