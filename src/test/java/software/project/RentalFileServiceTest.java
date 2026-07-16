package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RentalFileServiceTest {

	@TempDir
	Path tempDirectory;

	private final PrintStream originalOut = System.out;

	@AfterEach
	void restoreOutput() {
		System.setOut(originalOut);
	}

	@Test
	void saveRentalToFileShouldWriteAllRentalData() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		RentalFileService service = new RentalFileService(file.toString());

		String[] vehicle = { "10", "Car", "Toyota", "123-ABC", "White", "V100", "2024", "50.0" };

		service.saveRentalToFile(vehicle, "1234567", "Hala", "0591234567", "hala@gmail.com", "2026-07-11", "2026-07-13",
				3, 150.0);

		String content = Files.readString(file);

		assertTrue(content.contains("CustomerID: 1234567"));
		assertTrue(content.contains("CustomerName: Hala"));
		assertTrue(content.contains("CustomerPhone: 0591234567"));
		assertTrue(content.contains("CustomerEmail: hala@gmail.com"));

		assertTrue(content.contains("VehicleID: 10"));
		assertTrue(content.contains("VehicleType: Car"));
		assertTrue(content.contains("VehicleModel: Toyota"));
		assertTrue(content.contains("PlateNumber: 123-ABC"));
		assertTrue(content.contains("VehicleColor: White"));
		assertTrue(content.contains("VehicleNumber: V100"));
		assertTrue(content.contains("VehicleYear: 2024"));
		assertTrue(content.contains("PricePerDay: 50.0"));

		assertTrue(content.contains("RentalStartDate: 2026-07-11"));
		assertTrue(content.contains("RentalEndDate: 2026-07-13"));
		assertTrue(content.contains("RentalDays: 3"));
		assertTrue(content.contains("TotalCost: 150.0"));
	}

	@Test
	void vehicleWithFutureEndDateShouldBeRented() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		String futureDate = LocalDate.now().plusDays(5).toString();

		Files.writeString(file, "VehicleID: 10\n" + "RentalEndDate: " + futureDate + "\n" + "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertTrue(service.isVehicleRented(10));
	}

	@Test
	void vehicleEndingTodayShouldBeRented() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file,
				"VehicleID: 10\n" + "RentalEndDate: " + LocalDate.now() + "\n" + "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertTrue(service.isVehicleRented(10));
	}

	@Test
	void vehicleWithPastEndDateShouldNotBeRented() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		String pastDate = LocalDate.now().minusDays(1).toString();

		Files.writeString(file, "VehicleID: 10\n" + "RentalEndDate: " + pastDate + "\n" + "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertFalse(service.isVehicleRented(10));
	}

	@Test
	void differentVehicleShouldNotBeRented() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		String futureDate = LocalDate.now().plusDays(5).toString();

		Files.writeString(file, "VehicleID: 20\n" + "RentalEndDate: " + futureDate + "\n" + "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertFalse(service.isVehicleRented(10));
	}

	@Test
	void missingRentalsFileShouldReturnFalse() {

		Path file = tempDirectory.resolve("missing.txt");

		RentalFileService service = new RentalFileService(file.toString());

		assertFalse(service.isVehicleRented(10));
	}

	@Test
	void showCustomerRentalsShouldPrintMatchingRental() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file,
				"CustomerID: 1234567\n" + "CustomerName: Hala\n" + "VehicleID: 10\n" + "RentalEndDate: 2026-07-13\n"
						+ "---------------------\n" + "CustomerID: 7654321\n" + "CustomerName: Ali\n"
						+ "VehicleID: 20\n" + "RentalEndDate: 2026-07-14\n" + "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		System.setOut(new PrintStream(output));

		service.showCustomerRentals("1234567");

		String printed = output.toString();

		assertTrue(printed.contains("CustomerID: 1234567"));
		assertTrue(printed.contains("CustomerName: Hala"));
		assertTrue(printed.contains("VehicleID: 10"));

		assertFalse(printed.contains("CustomerName: Ali"));
		assertFalse(printed.contains("VehicleID: 20"));
	}

	@Test
	void showCustomerRentalsShouldPrintNoPreviousRentals() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file, "CustomerID: 7654321\n" + "CustomerName: Ali\n" + "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		System.setOut(new PrintStream(output));

		service.showCustomerRentals("1234567");

		assertTrue(output.toString().contains("No previous rentals found."));
	}

	@Test
	void showCustomerRentalsShouldHandleMissingFile() {

		Path file = tempDirectory.resolve("missing.txt");

		RentalFileService service = new RentalFileService(file.toString());

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		System.setOut(new PrintStream(output));

		service.showCustomerRentals("1234567");

		assertTrue(output.toString().contains("No rentals file found yet."));
	}

	@Test
	void vehicleShouldBeUnavailableWhenRequestedPeriodOverlaps() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file, "VehicleID: 10\n" + "RentalStartDate: 2026-07-10\n" + "RentalEndDate: 2026-07-15\n"
				+ "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		boolean available = service.isVehicleAvailable(10, LocalDate.of(2026, 7, 12), LocalDate.of(2026, 7, 18));

		assertFalse(available);
	}

	@Test
	void vehicleShouldBeUnavailableWhenRequestedPeriodIsInsideExistingRental() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file, "VehicleID: 10\n" + "RentalStartDate: 2026-07-10\n" + "RentalEndDate: 2026-07-20\n"
				+ "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertFalse(service.isVehicleAvailable(10, LocalDate.of(2026, 7, 12), LocalDate.of(2026, 7, 15)));
	}

	@Test
	void vehicleShouldBeAvailableWhenRequestedPeriodIsBeforeExistingRental() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file, "VehicleID: 10\n" + "RentalStartDate: 2026-07-10\n" + "RentalEndDate: 2026-07-15\n"
				+ "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertTrue(service.isVehicleAvailable(10, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 9)));
	}

	@Test
	void vehicleShouldBeAvailableWhenRequestedPeriodIsAfterExistingRental() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file, "VehicleID: 10\n" + "RentalStartDate: 2026-07-10\n" + "RentalEndDate: 2026-07-15\n"
				+ "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertTrue(service.isVehicleAvailable(10, LocalDate.of(2026, 7, 16), LocalDate.of(2026, 7, 20)));
	}

	@Test
	void differentVehicleShouldBeAvailableForRequestedPeriod() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file, "VehicleID: 20\n" + "RentalStartDate: 2026-07-10\n" + "RentalEndDate: 2026-07-20\n"
				+ "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertTrue(service.isVehicleAvailable(10, LocalDate.of(2026, 7, 12), LocalDate.of(2026, 7, 15)));
	}

	@Test
	void missingFileShouldConsiderVehicleAvailable() {

		Path file = tempDirectory.resolve("missing-rentals.txt");

		RentalFileService service = new RentalFileService(file.toString());

		assertTrue(service.isVehicleAvailable(10, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 15)));
	}

	@Test
	void rentalStartingOnExistingEndDateShouldBeUnavailable() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file, "VehicleID: 10\n" + "RentalStartDate: 2026-07-10\n" + "RentalEndDate: 2026-07-15\n"
				+ "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertFalse(service.isVehicleAvailable(10, LocalDate.of(2026, 7, 15), LocalDate.of(2026, 7, 18)));
	}

	@Test
	void rentalEndingOnExistingStartDateShouldBeUnavailable() throws IOException {

		Path file = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(file, "VehicleID: 10\n" + "RentalStartDate: 2026-07-10\n" + "RentalEndDate: 2026-07-15\n"
				+ "---------------------\n");

		RentalFileService service = new RentalFileService(file.toString());

		assertFalse(service.isVehicleAvailable(10, LocalDate.of(2026, 7, 5), LocalDate.of(2026, 7, 10)));
	}
}