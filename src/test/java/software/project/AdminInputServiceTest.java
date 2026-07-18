package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class AdminInputServiceTest {

	@Test
	void readIntShouldReturnValidNumber() {
		AdminInputService service = serviceFor("5");

		assertEquals(5, service.readInt());
	}

	@Test
	void readIntShouldRejectInvalidThenReturnValidNumber() {
		AdminInputService service = serviceFor("abc\n5");

		assertEquals(5, service.readInt());
	}

	@Test
	void readYearShouldReturnValidYear() {
		AdminInputService service = serviceFor("2024");

		assertEquals(2024, service.readYear());
	}

	@Test
	void readYearShouldRejectInvalidThenReturnValidYear() {
		AdminInputService service = serviceFor("1800\n2022");

		assertEquals(2022, service.readYear());
	}

	@Test
	void readYearShouldRejectNonNumericInput() {
		AdminInputService service = serviceFor("abc\n2022");

		assertEquals(2022, service.readYear());
	}

	@Test
	void readPriceShouldReturnValidPrice() {
		AdminInputService service = serviceFor("150");

		assertEquals(150.0, service.readPrice(), 0.001);
	}

	@Test
	void readPriceShouldRejectNegativeThenReturnValidPrice() {
		AdminInputService service = serviceFor("-20\n100");

		assertEquals(100.0, service.readPrice(), 0.001);
	}

	@Test
	void readPriceShouldRejectNonNumericInput() {
		AdminInputService service = serviceFor("abc\n100");

		assertEquals(100.0, service.readPrice(), 0.001);
	}

	@Test
	void readColorShouldReturnValidColor() {
		AdminInputService service = serviceFor("Blue");

		assertEquals("Blue", service.readColor());
	}

	@Test
	void readColorShouldRejectInvalidThenReturnValidColor() {
		AdminInputService service = serviceFor("123\nBlue");

		assertEquals("Blue", service.readColor());
	}

	@Test
	void readModelShouldReturnValidModel() {
		AdminInputService service = serviceFor("Kia2024");

		assertEquals("Kia2024", service.readModel());
	}

	@Test
	void readModelShouldRejectInvalidThenReturnValidModel() {
		AdminInputService service = serviceFor("Kia-2024\nKia2024");

		assertEquals("Kia2024", service.readModel());
	}

	@Test
	void readTypeShouldReturnValidType() {
		AdminInputService service = serviceFor("Car");

		assertEquals("Car", service.readType());
	}

	@Test
	void readTypeShouldRejectInvalidThenReturnValidType() {
		AdminInputService service = serviceFor("123\nTruck");

		assertEquals("Truck", service.readType());
	}

	@Test
	void validPlateNumberShouldReturnTrue() {
		AdminInputService service = serviceFor("");

		assertTrue(service.isValidPlateNumber("123456"));
	}

	@Test
	void invalidPlateNumberShouldReturnFalse() {
		AdminInputService service = serviceFor("");

		assertFalse(service.isValidPlateNumber("12345"));
		assertFalse(service.isValidPlateNumber("abcdef"));
		assertFalse(service.isValidPlateNumber(null));
	}

	private AdminInputService serviceFor(String inputText) {
		Scanner scanner = new Scanner(new ByteArrayInputStream(inputText.getBytes()));

		return new AdminInputService(scanner);
	}

	@Test
	void readPlateNumberShouldReturnValidPlate() {

		AdminVehicleFileService fileService = new AdminVehicleFileService() {
			@Override
			public boolean plateExists(String plateNumber) {
				return false;
			}
		};

		AdminInputService service = serviceFor("123456");

		assertEquals("123456", service.readPlateNumber(fileService));
	}

	@Test
	void readPlateNumberShouldRejectInvalidFormatThenAcceptValidPlate() {

		AdminVehicleFileService fileService = new AdminVehicleFileService() {
			@Override
			public boolean plateExists(String plateNumber) {
				return false;
			}
		};

		AdminInputService service = serviceFor("123\n" + "abcdef\n" + "123456");

		assertEquals("123456", service.readPlateNumber(fileService));
	}

	@Test
	void readPlateNumberShouldRejectExistingPlateThenAcceptNewPlate() {

		AdminVehicleFileService fileService = new AdminVehicleFileService() {
			@Override
			public boolean plateExists(String plateNumber) {
				return plateNumber.equals("123456");
			}
		};

		AdminInputService service = serviceFor("123456\n" + "654321");

		assertEquals("654321", service.readPlateNumber(fileService));
	}

	@Test
	void readPlateNumberShouldRejectInvalidAndExistingThenAcceptValidPlate() {

		AdminVehicleFileService fileService = new AdminVehicleFileService() {
			@Override
			public boolean plateExists(String plateNumber) {
				return plateNumber.equals("111111");
			}
		};

		AdminInputService service = serviceFor("12ab56\n" + "111111\n" + "222222");

		assertEquals("222222", service.readPlateNumber(fileService));
	}
}