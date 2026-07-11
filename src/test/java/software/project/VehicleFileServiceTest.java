package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class VehicleFileServiceTest {

    @TempDir
    Path tempDirectory;

    @Test
    void getAvailableVehiclesByTypeShouldReturnMatchingAvailableVehicles()
            throws IOException {

        Path vehiclesFile =
                tempDirectory.resolve("AddingVEHICLE.txt");

        Path rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        Files.writeString(
                vehiclesFile,
                "1,Car,Toyota,ABC-123,White,V100,2024,50.0\n"
                        + "2,Car,Honda,DEF-456,Black,V200,2023,60.0\n"
                        + "3,Truck,Volvo,GHI-789,Blue,V300,2022,100.0\n"
        );

        Files.writeString(rentalsFile, "");

        RentalFileService rentalService =
                new RentalFileService(rentalsFile.toString());

        VehicleFileService vehicleService =
                new VehicleFileService(
                        vehiclesFile.toString(),
                        rentalService
                );

        ArrayList<String[]> result =
                vehicleService.getAvailableVehiclesByType("Car");

        assertEquals(2, result.size());
        assertEquals("1", result.get(0)[0]);
        assertEquals("Toyota", result.get(0)[2]);
        assertEquals("2", result.get(1)[0]);
        assertEquals("Honda", result.get(1)[2]);
    }

    @Test
    void getAvailableVehiclesByTypeShouldIgnoreDifferentTypes()
            throws IOException {

        Path vehiclesFile =
                tempDirectory.resolve("AddingVEHICLE.txt");

        Path rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        Files.writeString(
                vehiclesFile,
                "1,Car,Toyota,ABC-123,White,V100,2024,50.0\n"
                        + "2,Truck,Volvo,DEF-456,Blue,V200,2023,100.0\n"
        );

        Files.writeString(rentalsFile, "");

        VehicleFileService service =
                new VehicleFileService(
                        vehiclesFile.toString(),
                        new RentalFileService(rentalsFile.toString())
                );

        ArrayList<String[]> result =
                service.getAvailableVehiclesByType("Truck");

        assertEquals(1, result.size());
        assertEquals("Truck", result.get(0)[1]);
        assertEquals("Volvo", result.get(0)[2]);
    }

    @Test
    void getAvailableVehiclesByTypeShouldIgnoreRentedVehicle()
            throws IOException {

        Path vehiclesFile =
                tempDirectory.resolve("AddingVEHICLE.txt");

        Path rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        Files.writeString(
                vehiclesFile,
                "1,Car,Toyota,ABC-123,White,V100,2024,50.0\n"
                        + "2,Car,Honda,DEF-456,Black,V200,2023,60.0\n"
        );

        Files.writeString(
                rentalsFile,
                "VehicleID: 1\n"
                        + "RentalEndDate: "
                        + LocalDate.now().plusDays(5)
                        + "\n"
                        + "---------------------\n"
        );

        VehicleFileService service =
                new VehicleFileService(
                        vehiclesFile.toString(),
                        new RentalFileService(rentalsFile.toString())
                );

        ArrayList<String[]> result =
                service.getAvailableVehiclesByType("Car");

        assertEquals(1, result.size());
        assertEquals("2", result.get(0)[0]);
        assertEquals("Honda", result.get(0)[2]);
    }

    @Test
    void getAvailableVehiclesByTypeShouldIncludeExpiredRental()
            throws IOException {

        Path vehiclesFile =
                tempDirectory.resolve("AddingVEHICLE.txt");

        Path rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        Files.writeString(
                vehiclesFile,
                "1,Car,Toyota,ABC-123,White,V100,2024,50.0\n"
        );

        Files.writeString(
                rentalsFile,
                "VehicleID: 1\n"
                        + "RentalEndDate: "
                        + LocalDate.now().minusDays(1)
                        + "\n"
                        + "---------------------\n"
        );

        VehicleFileService service =
                new VehicleFileService(
                        vehiclesFile.toString(),
                        new RentalFileService(rentalsFile.toString())
                );

        ArrayList<String[]> result =
                service.getAvailableVehiclesByType("Car");

        assertEquals(1, result.size());
        assertEquals("1", result.get(0)[0]);
    }

    @Test
    void getAvailableVehiclesByTypeShouldIgnoreMalformedLines()
            throws IOException {

        Path vehiclesFile =
                tempDirectory.resolve("AddingVEHICLE.txt");

        Path rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        Files.writeString(
                vehiclesFile,
                "invalid,line\n"
                        + "1,Car,Toyota,ABC-123,White,V100,2024,50.0\n"
        );

        Files.writeString(rentalsFile, "");

        VehicleFileService service =
                new VehicleFileService(
                        vehiclesFile.toString(),
                        new RentalFileService(rentalsFile.toString())
                );

        ArrayList<String[]> result =
                service.getAvailableVehiclesByType("Car");

        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0)[2]);
    }

    @Test
    void getAvailableVehiclesByTypeShouldReturnEmptyListWhenFileMissing() {

        Path missingVehiclesFile =
                tempDirectory.resolve("missing-vehicles.txt");

        Path rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        VehicleFileService service =
                new VehicleFileService(
                        missingVehiclesFile.toString(),
                        new RentalFileService(rentalsFile.toString())
                );

        ArrayList<String[]> result =
                service.getAvailableVehiclesByType("Car");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findVehicleByIdShouldReturnMatchingVehicle() {

        VehicleFileService service =
                new VehicleFileService();

        ArrayList<String[]> vehicles = new ArrayList<>();

        vehicles.add(new String[]{
                "1", "Car", "Toyota", "ABC-123",
                "White", "V100", "2024", "50.0"
        });

        vehicles.add(new String[]{
                "2", "Car", "Honda", "DEF-456",
                "Black", "V200", "2023", "60.0"
        });

        String[] result =
                service.findVehicleById(vehicles, 2);

        assertNotNull(result);
        assertEquals("2", result[0]);
        assertEquals("Honda", result[2]);
    }

    @Test
    void findVehicleByIdShouldReturnNullWhenVehicleNotFound() {

        VehicleFileService service =
                new VehicleFileService();

        ArrayList<String[]> vehicles = new ArrayList<>();

        vehicles.add(new String[]{
                "1", "Car", "Toyota", "ABC-123",
                "White", "V100", "2024", "50.0"
        });

        String[] result =
                service.findVehicleById(vehicles, 99);

        assertNull(result);
    }

    @Test
    void vehicleTypeComparisonShouldIgnoreCase()
            throws IOException {

        Path vehiclesFile =
                tempDirectory.resolve("AddingVEHICLE.txt");

        Path rentalsFile =
                tempDirectory.resolve("customer_rentals.txt");

        Files.writeString(
                vehiclesFile,
                "1,Car,Toyota,ABC-123,White,V100,2024,50.0\n"
        );

        Files.writeString(rentalsFile, "");

        VehicleFileService service =
                new VehicleFileService(
                        vehiclesFile.toString(),
                        new RentalFileService(rentalsFile.toString())
                );

        ArrayList<String[]> result =
                service.getAvailableVehiclesByType("car");

        assertEquals(1, result.size());
    }
}