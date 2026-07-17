package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AdminVehicleFileServiceTest {

	@TempDir
	Path tempDirectory;

	private final PrintStream originalOut = System.out;

	private Path vehiclesFile;
	private AdminVehicleFileService service;

	@BeforeEach
	void setUp() throws IOException {
		vehiclesFile = tempDirectory.resolve("AddingVEHICLE_test.txt");

		Files.writeString(vehiclesFile, "");

		service = new AdminVehicleFileService();
	}

	@AfterEach
	void restoreOutput() {
		System.setOut(originalOut);
	}

	@Test
	void generateVehicleIdShouldReturnOneWhenFileIsEmpty() throws IOException {

		/*
		 * generateVehicleID حاليًا يستخدم الملف الحقيقي AddingVEHICLE.txt، لذلك هذا
		 * الاختبار سنضيفه بعد تعديل الخدمة لتستقبل مسار الملف.
		 */

		assertNotNull(service);
	}

	@Test
	void displayVehiclesShouldReturnTrueForExistingType() throws IOException {

		Files.writeString(vehiclesFile,
				"1,Car,Toyota,Corolla,Red,123456,2020,50.0\n" + "2,Bus,Mercedes,Model1,White,654321,2021,80.0\n");

		ByteArrayOutputStream output = captureOutput();

		boolean result = service.displayVehiclesFromFile(vehiclesFile.toString(), "Car");

		assertTrue(result);

		String printed = output.toString();

		assertTrue(printed.contains("ID: 1"));
		assertTrue(printed.contains("Type: Toyota"));
		assertTrue(printed.contains("Model: Corolla"));
		assertTrue(printed.contains("Plate Number: 123456"));
	}

	@Test
	void displayVehiclesShouldReturnFalseForMissingType() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean result = service.displayVehiclesFromFile(vehiclesFile.toString(), "Truck");

		assertFalse(result);
	}

	@Test
	void displayVehiclesShouldReturnFalseWhenFileMissing() {

		Path missingFile = tempDirectory.resolve("missing.txt");

		boolean result = service.displayVehiclesFromFile(missingFile.toString(), "Car");

		assertFalse(result);
	}

	@Test
	void checkVehicleExistsShouldReturnTrueForMatchingVehicle() throws IOException {

		Files.writeString(vehiclesFile,
				"1,Car,Toyota,Corolla,Red,123456,2020,50.0\n" + "2,Bus,Mercedes,Model1,White,654321,2021,80.0\n");

		assertTrue(service.checkVehicleExists(vehiclesFile.toString(), 1, "Car"));
	}

	@Test
	void checkVehicleExistsShouldReturnFalseForWrongId() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		assertFalse(service.checkVehicleExists(vehiclesFile.toString(), 99, "Car"));
	}

	@Test
	void checkVehicleExistsShouldReturnFalseForWrongType() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		assertFalse(service.checkVehicleExists(vehiclesFile.toString(), 1, "Bus"));
	}

	@Test
	void checkVehicleExistsShouldReturnFalseWhenFileMissing() {

		Path missingFile = tempDirectory.resolve("missing.txt");

		assertFalse(service.checkVehicleExists(missingFile.toString(), 1, "Car"));
	}

	@Test
	void deleteVehicleShouldRemoveMatchingVehicle() throws IOException {

		Files.writeString(vehiclesFile,
				"1,Car,Toyota,Corolla,Red,123456,2020,50.0\n" + "2,Bus,Mercedes,Model1,White,654321,2021,80.0\n");

		boolean deleted = service.deleteVehicleFromFile(vehiclesFile.toString(), 1);

		assertTrue(deleted);

		String content = Files.readString(vehiclesFile);

		assertFalse(content.contains("1,Car,Toyota"));

		assertTrue(content.contains("2,Bus,Mercedes"));
	}

	@Test
	void deleteVehicleShouldReturnFalseWhenIdMissing() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean deleted = service.deleteVehicleFromFile(vehiclesFile.toString(), 99);

		assertFalse(deleted);

		String content = Files.readString(vehiclesFile);

		assertTrue(content.contains("1,Car,Toyota"));
	}

	@Test
	void deleteVehicleShouldReturnFalseWhenFileMissing() {

		Path missingFile = tempDirectory.resolve("missing.txt");

		boolean result = service.deleteVehicleFromFile(missingFile.toString(), 1);

		assertFalse(result);
	}

	@Test
	void editVehicleShouldUpdateType() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean edited = service.editVehicleFromFile(vehiclesFile.toString(), 1, 1, "Honda");

		assertTrue(edited);

		String content = Files.readString(vehiclesFile);

		assertTrue(content.contains("1,Car,Honda,Corolla,Red,123456,2020,50.0"));
	}

	@Test
	void editVehicleShouldUpdateModel() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean edited = service.editVehicleFromFile(vehiclesFile.toString(), 1, 2, "Camry");

		assertTrue(edited);

		String content = Files.readString(vehiclesFile);

		assertTrue(content.contains("1,Car,Toyota,Camry,Red,123456,2020,50.0"));
	}

	@Test
	void editVehicleShouldUpdateColor() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean edited = service.editVehicleFromFile(vehiclesFile.toString(), 1, 3, "Blue");

		assertTrue(edited);

		String content = Files.readString(vehiclesFile);

		assertTrue(content.contains("1,Car,Toyota,Corolla,Blue,123456,2020,50.0"));
	}

	@Test
	void editVehicleShouldUpdateYear() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean edited = service.editVehicleFromFile(vehiclesFile.toString(), 1, 4, "2024");

		assertTrue(edited);

		String content = Files.readString(vehiclesFile);

		assertTrue(content.contains("1,Car,Toyota,Corolla,Red,123456,2024,50.0"));
	}

	@Test
	void editVehicleShouldUpdatePrice() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean edited = service.editVehicleFromFile(vehiclesFile.toString(), 1, 5, "90.0");

		assertTrue(edited);

		String content = Files.readString(vehiclesFile);

		assertTrue(content.contains("1,Car,Toyota,Corolla,Red,123456,2020,90.0"));
	}

	@Test
	void editVehicleShouldReturnFalseForInvalidChoice() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean edited = service.editVehicleFromFile(vehiclesFile.toString(), 1, 9, "Anything");

		assertFalse(edited);
	}

	@Test
	void editVehicleShouldReturnFalseWhenIdMissing() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		boolean edited = service.editVehicleFromFile(vehiclesFile.toString(), 99, 2, "Camry");

		assertFalse(edited);
	}

	@Test
	void editVehicleShouldReturnFalseWhenFileMissing() {

		Path missingFile = tempDirectory.resolve("missing.txt");

		boolean edited = service.editVehicleFromFile(missingFile.toString(), 1, 2, "Camry");

		assertFalse(edited);
	}

	private ByteArrayOutputStream captureOutput() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		System.setOut(new PrintStream(output));

		return output;
	}

	@Test
	void generateVehicleIdShouldReturnOneForEmptyFile() throws IOException {

		AdminVehicleFileService service = new AdminVehicleFileService(vehiclesFile.toString());

		assertEquals(1, service.generateVehicleID());
	}

	@Test
	void generateVehicleIdShouldReturnNextId() throws IOException {

		Files.writeString(vehiclesFile,
				"1,Car,Toyota,Corolla,Red,123456,2020,50.0\n" + "2,Bus,Mercedes,Model1,White,654321,2021,80.0\n");

		AdminVehicleFileService service = new AdminVehicleFileService(vehiclesFile.toString());

		assertEquals(3, service.generateVehicleID());
	}

	@Test
	void saveVehicleShouldAppendVehicleToFile() throws IOException {

		AdminVehicleFileService service = new AdminVehicleFileService(vehiclesFile.toString());

		service.saveVehicle("Car", "Toyota", "Corolla", "Red", 2020, "123456", 50.0);

		String content = Files.readString(vehiclesFile);

		assertTrue(content.contains("1,Car,Toyota,Corolla,Red,123456,2020,50.0"));
	}

	@Test
	void plateExistsShouldReturnTrueForExistingPlate() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		AdminVehicleFileService service = new AdminVehicleFileService(vehiclesFile.toString());

		assertTrue(service.plateExists("123456"));
	}

	@Test
	void plateExistsShouldReturnFalseForMissingPlate() throws IOException {

		Files.writeString(vehiclesFile, "1,Car,Toyota,Corolla,Red,123456,2020,50.0\n");

		AdminVehicleFileService service = new AdminVehicleFileService(vehiclesFile.toString());

		assertFalse(service.plateExists("999999"));
	}
}