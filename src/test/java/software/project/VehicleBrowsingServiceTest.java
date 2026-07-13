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

class VehicleBrowsingServiceTest {

	@TempDir
	Path tempDirectory;

	private final PrintStream originalOut = System.out;

	private Path vehiclesFile;
	private Path rentalsFile;
	private VehicleBrowsingService service;

	@BeforeEach
	void setUp() throws IOException {
		vehiclesFile = tempDirectory.resolve("AddingVEHICLE.txt");

		rentalsFile = tempDirectory.resolve("customer_rentals.txt");

		Files.writeString(rentalsFile, "");

		RentalFileService rentalFileService = new RentalFileService(rentalsFile.toString());

		VehicleFileService vehicleFileService = new VehicleFileService(vehiclesFile.toString(), rentalFileService);

		service = new VehicleBrowsingService(vehicleFileService);
	}

	@AfterEach
	void restoreOutput() {
		System.setOut(originalOut);
	}

	@Test
	void getVehicleTypeFromChoiceShouldReturnCorrectTypes() {
		assertEquals("Car", service.getVehicleTypeFromChoice(1));

		assertEquals("Motorcycle", service.getVehicleTypeFromChoice(2));

		assertEquals("Truck", service.getVehicleTypeFromChoice(3));

		assertEquals("Bus", service.getVehicleTypeFromChoice(4));
	}

	@Test
	void getVehicleTypeFromChoiceShouldReturnNullForInvalidChoice() {
		assertNull(service.getVehicleTypeFromChoice(0));
		assertNull(service.getVehicleTypeFromChoice(5));
	}

	@Test
	void readIntShouldReturnValidNumber() {
		Scanner input = scannerFor("3");

		assertEquals(3, service.readInt(input));
	}

	@Test
	void readIntShouldRejectNonNumericInput() {
		Scanner input = scannerFor("abc", "two", "2");

		ByteArrayOutputStream output = captureOutput();

		int result = service.readInt(input);

		assertEquals(2, result);
		assertTrue(output.toString().contains("Invalid input! Please enter a number."));
	}

	@Test
	void readYesOrNoShouldAcceptYes() {
		Scanner input = scannerFor("yes");

		String result = service.readYesOrNo(input, "Question: ");

		assertEquals("yes", result);
	}

	@Test
	void readYesOrNoShouldAcceptNoIgnoringCase() {
		Scanner input = scannerFor("NO");

		String result = service.readYesOrNo(input, "Question: ");

		assertEquals("NO", result);
	}

	@Test
	void readYesOrNoShouldRejectInvalidAnswer() {
		Scanner input = scannerFor("maybe", "no");

		ByteArrayOutputStream output = captureOutput();

		String result = service.readYesOrNo(input, "Question: ");

		assertEquals("no", result);
		assertTrue(output.toString().contains("Invalid input!"));
	}

	@Test
	void displayVehiclesShouldPrintVehicleDetails() {
		ArrayList<String[]> vehicles = new ArrayList<>();

		vehicles.add(new String[] { "1", "Car", "BMW", "ABC-123", "Black", "V100", "2024", "100.0" });

		ByteArrayOutputStream output = captureOutput();

		service.displayVehicles(vehicles);

		String printed = output.toString();

		assertTrue(printed.contains("ID: 1"));
		assertTrue(printed.contains("Type: Car"));
		assertTrue(printed.contains("Model: BMW"));
		assertTrue(printed.contains("Plate Number: ABC-123"));
		assertTrue(printed.contains("Color: Black"));
		assertTrue(printed.contains("Year: 2024"));
		assertTrue(printed.contains("Price per day: 100.0"));
	}

	@Test
	void displayAvailableVehiclesByTypeShouldPrintAvailableVehicles() throws IOException {

		Files.writeString(vehiclesFile,
				"1,Car,BMW,ABC-123,Black,V100,2024,100.0\n" + "2,Bus,Mercedes,XYZ-555,White,V200,2022,150.0\n");

		ByteArrayOutputStream output = captureOutput();

		service.displayAvailableVehiclesByType("Car");

		String printed = output.toString();

		assertTrue(printed.contains("Available Car vehicles"));
		assertTrue(printed.contains("ID: 1"));
		assertTrue(printed.contains("Model: BMW"));
		assertFalse(printed.contains("Mercedes"));
	}

	@Test
	void displayAvailableVehiclesByTypeShouldPrintMessageWhenEmpty() throws IOException {

		Files.writeString(vehiclesFile, "1,Bus,Mercedes,XYZ-555,White,V200,2022,150.0\n");

		ByteArrayOutputStream output = captureOutput();

		service.displayAvailableVehiclesByType("Car");

		assertTrue(output.toString().contains("No available Car vehicles found."));
	}

	@Test
	void showVehiclesWithoutLoginShouldReturnToMainWhenBackSelected() {
		Scanner input = scannerFor("5");

		AtomicBoolean backCalled = new AtomicBoolean(false);

		service.showVehiclesWithoutLogin(input, () -> backCalled.set(true));

		assertTrue(backCalled.get());
	}

	@Test
	void showVehiclesWithoutLoginShouldRejectInvalidChoiceThenGoBack() {
		Scanner input = scannerFor("9", "5");

		AtomicBoolean backCalled = new AtomicBoolean(false);

		ByteArrayOutputStream output = captureOutput();

		service.showVehiclesWithoutLogin(input, () -> backCalled.set(true));

		assertTrue(backCalled.get());
		assertTrue(output.toString().contains("Invalid choice!"));
	}

	@Test
	void showVehiclesWithoutLoginShouldDisplayTypeThenReturnOnNo() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,BMW,ABC-123,Black,V100,2024,100.0\n");

		Scanner input = scannerFor("1", "no");

		AtomicBoolean backCalled = new AtomicBoolean(false);

		ByteArrayOutputStream output = captureOutput();

		service.showVehiclesWithoutLogin(input, () -> backCalled.set(true));

		assertTrue(backCalled.get());

		String printed = output.toString();

		assertTrue(printed.contains("Available Car vehicles"));
		assertTrue(printed.contains("Model: BMW"));
	}

	@Test
	void showVehiclesWithoutLoginShouldAllowViewingAnotherType() throws IOException {

		Files.writeString(vehiclesFile,
				"1,Car,BMW,ABC-123,Black,V100,2024,100.0\n" + "2,Bus,Mercedes,XYZ-555,White,V200,2022,150.0\n");

		Scanner input = scannerFor("1", "yes", "4", "no");

		AtomicBoolean backCalled = new AtomicBoolean(false);

		ByteArrayOutputStream output = captureOutput();

		service.showVehiclesWithoutLogin(input, () -> backCalled.set(true));

		assertTrue(backCalled.get());

		String printed = output.toString();

		assertTrue(printed.contains("Available Car vehicles"));
		assertTrue(printed.contains("Available Bus vehicles"));
		assertTrue(printed.contains("BMW"));
		assertTrue(printed.contains("Mercedes"));
	}

	private Scanner scannerFor(String... values) {
		String text = String.join("\n", values);

		if (!text.isEmpty()) {
			text += "\n";
		}

		return new Scanner(new ByteArrayInputStream(text.getBytes()));
	}

	private ByteArrayOutputStream captureOutput() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		System.setOut(new PrintStream(output));

		return output;
	}
}