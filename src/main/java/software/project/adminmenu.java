package software.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class adminmenu {
	Scanner input = new Scanner(System.in);

	private final AdminVehicleFileService fileService;
	private final AdminInputService inputService;

	public adminmenu() {
		this(new AdminVehicleFileService(), new AdminInputService());
	}

	adminmenu(AdminVehicleFileService fileService, AdminInputService inputService) {

		this.fileService = fileService;
		this.inputService = inputService;
	}

	public void showmenu() {
		while (true) {
			System.out.println("What do you want to do? \n 1-Add \n 2-Delete \n 3-Edit \n 4-Back");
			int choice = inputService.readInt();

			switch (choice) {

			case 1:
				adminadd();
				return;

			case 2:
				admindelete();
				return;

			case 3:
				adminedit();
				return;

			case 4:
				Manager m = new Manager();
				m.start();
				return;

			default:
				System.out.println("Invalid choice! Please enter a number between 1 and 3.");
			}
		}
	}

	public void adminadd() {
		while (true) {
			System.out.println("ADD VEHICLE");
			System.out.println("1-Car");
			System.out.println("2-Motorcycle");
			System.out.println("3-Truck");
			System.out.println("4-Bus");
			System.out.println("5-Back");

			int choice = inputService.readInt();
			switch (choice) {

			case 1:
				enterVehicleData("Car");
				break;

			case 2:
				enterVehicleData("Motorcycle");
				break;

			case 3:
				enterVehicleData("Truck");
				break;

			case 4:
				enterVehicleData("Bus");
				break;

			case 5:
				showmenu();
				return;

			default:
				System.out.println("Invalid choice");
			}

			while (true) {

				System.out.println("\nDo you want to add another vehicle?");
				System.out.println("1- Yes");
				System.out.println("2- No (Back)");

				int again = inputService.readInt();

				if (again == 1) {
					break;
				}

				else if (again == 2) {
					showmenu();
					return;
				}

				else {
					System.out.println("Invalid choice! Please enter 1 or 2.");
				}
			}
		}
	}

	public void enterVehicleData(String vehicleType) {

		System.out.println("=== Add " + vehicleType + " ===");

		String type = inputService.readType();
		String model = inputService.readModel();
		int year = inputService.readYear();
		String color = inputService.readColor();
		String plateNumber = inputService.readPlateNumber(fileService);
		double price = inputService.readPrice();

		fileService.saveVehicle(vehicleType, type, model, color, year, plateNumber, price);

		System.out.println(vehicleType + " added successfully!");
	}

	public void admindelete() {

		System.out.println("DELETE VEHICLE");
		System.out.println("1-Car");
		System.out.println("2-Motorcycle");
		System.out.println("3-Truck");
		System.out.println("4-Bus");
		System.out.println("5-Back");

		int choice = inputService.readInt();

		String vehicleType = "";

		switch (choice) {
		case 1:
			vehicleType = "Car";
			break;

		case 2:
			vehicleType = "Motorcycle";
			break;

		case 3:
			vehicleType = "Truck";
			break;

		case 4:
			vehicleType = "Bus";
			break;

		case 5:
			showmenu();
			return;

		default:
			System.out.println("Invalid choice.");
			return;
		}

		displayVehicles(vehicleType);

	}

	public void adminedit() {

		while (true) {

			System.out.println("EDIT VEHICLE");
			System.out.println("1-Car");
			System.out.println("2-Motorcycle");
			System.out.println("3-Truck");
			System.out.println("4-Bus");
			System.out.println("5-Back");

			int choice = inputService.readInt();

			String vehicleType = "";

			switch (choice) {

			case 1:
				vehicleType = "Car";
				break;

			case 2:
				vehicleType = "Motorcycle";
				break;

			case 3:
				vehicleType = "Truck";
				break;

			case 4:
				vehicleType = "Bus";
				break;

			case 5:
				showmenu();
				return;

			default:
				System.out.println("Invalid choice! Please enter a number between 1 and 5.");
				continue;
			}

			displayVehiclesForEdit(vehicleType);
			return;
		}
	}

	public void displayVehiclesForEdit(String vehicleType) {

		try {

			BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

			String line;
			boolean found = false;

			while ((line = br.readLine()) != null) {

				String[] data = line.split(",");

				if (data[1].equalsIgnoreCase(vehicleType)) {

					found = true;

					System.out.println("----------------------------------");
					System.out.println("ID: " + data[0]);
					System.out.println("Type: " + data[2]);
					System.out.println("Model: " + data[3]);
					System.out.println("Color: " + data[4]);
					System.out.println("Plate Number: " + data[5]);
					System.out.println("Year: " + data[6]);
					System.out.println("Price: " + data[7]);
					System.out.println("----------------------------------");

				}

			}

			br.close();

			if (!found) {

				System.out.println("No " + vehicleType + " found.");

				System.out.println("\nDo you want to choose another vehicle type?");
				System.out.println("1- Yes");
				System.out.println("2- No (Back)");

				int again = inputService.readInt();

				if (again == 1) {
					adminedit();
				} else {
					showmenu();
				}

				return;
			}

			int id;

			while (true) {

				System.out.print("Enter Vehicle ID to edit: ");
				id = inputService.readInt();

				boolean exists = false;

				BufferedReader check = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

				String line2;

				while ((line2 = check.readLine()) != null) {

					String[] data = line2.split(",");

					if (Integer.parseInt(data[0]) == id && data[1].equalsIgnoreCase(vehicleType)) {

						exists = true;
						break;
					}
				}

				check.close();

				if (exists) {
					break;
				}

				System.out.println("Vehicle ID not found. Please enter a valid ID.");

			}

			editVehicle(id);

		} catch (IOException e) {

			System.out.println("Error reading file.");

		}

	}

	public void editVehicle(int id) {

		System.out.println("\nWhat do you want to edit?");
		System.out.println("1-Type");
		System.out.println("2-Model");
		System.out.println("3-Color");
		System.out.println("4-Year");
		System.out.println("5-Price");

		int choice;

		while (true) {

			choice = inputService.readInt();

			if (choice >= 1 && choice <= 5) {
				break;
			}

			System.out.println("Invalid choice! Please enter a number between 1 and 5.");
		}

		String newValue = "";

		switch (choice) {

		case 1:
			newValue = inputService.readType();
			break;

		case 2:
			newValue = inputService.readModel();
			break;

		case 3:
			newValue = inputService.readColor();
			break;

		case 4:
			newValue = String.valueOf(inputService.readYear());
			break;

		case 5:
			newValue = String.valueOf(inputService.readPrice());
			break;
		}

		boolean edited = fileService.editVehicleFromFile("AddingVEHICLE.txt", id, choice, newValue);

		if (edited)
			System.out.println("Vehicle updated successfully.");
		else
			System.out.println("Vehicle ID not found.");

		System.out.println("\nDo you want to edit another vehicle?");
		System.out.println("1- Yes");
		System.out.println("2- No (Back)");

		while (true) {

			int again = inputService.readInt();

			if (again == 1) {
				adminedit();
				return;
			}

			else if (again == 2) {
				showmenu();
				return;
			}

			else {
				System.out.println("Invalid choice! Please enter 1 or 2.");
			}
		}
	}

	public void displayVehicles(String vehicleType) {

		boolean found = fileService.displayVehiclesFromFile("AddingVEHICLE.txt", vehicleType);

		if (!found) {

			System.out.println("No " + vehicleType + " found.");

			System.out.println("\nDo you want to choose another vehicle type?");
			System.out.println("1- Yes");
			System.out.println("2- No (Back)");

			int again = inputService.readInt();

			if (again == 1) {
				admindelete();
			} else {
				showmenu();
			}

			return;
		}

		int id;

		while (true) {

			System.out.print("Enter Vehicle ID to delete: ");
			id = inputService.readInt();

			boolean exists = fileService.checkVehicleExists("AddingVEHICLE.txt", id, vehicleType);

			if (exists) {
				break;
			}

			System.out.println("Vehicle ID not found. Please enter a valid ID.");

		}

		deleteVehicle(id);
	}

	public void deleteVehicle(int id) {

		boolean deleted = fileService.deleteVehicleFromFile("AddingVEHICLE.txt", id);

		if (deleted)
			System.out.println("Vehicle deleted successfully.");
		else
			System.out.println("Vehicle ID not found.");

		System.out.println("\nDo you want to delete another vehicle?");
		System.out.println("1- Yes");
		System.out.println("2- No (Back)");

		while (true) {

			int again = inputService.readInt();

			if (again == 1) {
				admindelete();
				return;
			}

			else if (again == 2) {
				showmenu();
				return;
			}

			else {
				System.out.println("Invalid choice! Please enter 1 or 2.");
			}
		}
	}
}