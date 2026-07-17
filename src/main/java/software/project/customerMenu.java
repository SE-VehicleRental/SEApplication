package software.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class customerMenu {
	Scanner input = new Scanner(System.in);
	private final RentalCalculator rentalCalculator = new RentalCalculator();
	private final RentalFileService rentalFileService = new RentalFileService();
	private final VehicleFileService vehicleFileService = new VehicleFileService();
	private final LicenseService licenseService = new LicenseService();
	private final CustomerRegistrationService registrationService = new CustomerRegistrationService();
	private final VehicleBrowsingService vehicleBrowsingService = new VehicleBrowsingService();
	private final CustomerProfileService profileService = new CustomerProfileService();

	public void showMenu() {
		System.out.println("Customer System");
		System.out.println("1- View vehicles without login");
		System.out.println("2- Login / Register");
		int firstChoice;
		while (true) {
			System.out.print("Choose option: ");
			firstChoice = readInt();
			if (firstChoice == 1 || firstChoice == 2) {
				break;
			} else {
				System.out.println("Invalid input!");
			}
		}
		if (firstChoice == 1) {
			vehicleBrowsingService.showVehiclesWithoutLogin(input, () -> {
				Manager manager = new Manager();
				manager.start();
			});
			return;
		}
		System.out.println("\n1- New Customer");
		System.out.println("2- Existing Customer");
		int choice;
		while (true) {
			System.out.print("Choose option: ");
			choice = readInt();
			if (choice == 1 || choice == 2) {
				break;
			} else {
				System.out.println("Invalid input!");
			}
		}
		if (choice == 2) {
			profileService.handleExistingCustomer(input, this::rentVehicle, () -> {
				Manager manager = new Manager();
				manager.start();
			});
			return;
		}
		CustomerData newCustomer = registrationService.registerCustomer(input);
		System.out.println("Data saved successfully!");
		String rent;
		while (true) {
			System.out.print("Do you want to rent a vehicle? (yes/no): ");
			rent = input.nextLine().trim();
			if (rent.equalsIgnoreCase("yes") || rent.equalsIgnoreCase("no")) {
				break;
			} else {
				System.out.println("Invalid input! Please enter yes or no.");
			}
		}
		if (rent.equalsIgnoreCase("yes")) {
			rentVehicle(newCustomer.getLicenses(), newCustomer.getId(), newCustomer.getName(), newCustomer.getPhone(),newCustomer.getEmail());
		} else {
			Manager m = new Manager();
			m.start();
		}
	}

	private void rentVehicle(ArrayList<String> licenses, String customerId, String customerName, String customerPhone,String customerEmail) {
		String chosenLicense = licenseService.chooseOneLicense(input, licenses);
		LocalDate startDate;
		LocalDate endDate;

		while (true) {

		    try {

		        System.out.print("Enter rental start date (yyyy-mm-dd): ");
		        startDate = LocalDate.parse(input.nextLine().trim());

		        System.out.print("Enter rental end date (yyyy-mm-dd): ");
		        endDate = LocalDate.parse(input.nextLine().trim());

		        rentalCalculator.calculateRentalDays(startDate, endDate);

		        break;

		    } catch (IllegalArgumentException e) {

		        System.out.println("Invalid date! " + e.getMessage());
		    }
		}

		System.out.println("\nAvailable " + chosenLicense + " vehicles:\n");

		ArrayList<String[]> vehicles =
		        vehicleFileService.getAvailableVehiclesByType(
		                chosenLicense,
		                startDate,
		                endDate);

		if (vehicles.isEmpty()) {

		    System.out.println(
		            "No vehicles are available during the selected period."
		    );

		    Manager manager = new Manager();
		    manager.start();
		    return;
		}
		vehicleBrowsingService.displayVehicles(vehicles);
		while (true) {
			System.out.print("Enter Vehicle ID to rent: ");
			int vehicleId = readInt();
			String[] vehicle = vehicleFileService.findVehicleById(vehicles, vehicleId);
			if (vehicle == null) {
				System.out.println("Invalid Vehicle ID! Please choose one " + "of the available vehicles.");
				continue;
			}
			double pricePerDay = Double.parseDouble(vehicle[7]);
			long days = rentalCalculator.calculateRentalDays(startDate, endDate);

			double totalCost =
			        rentalCalculator.calculateTotalCost(days, pricePerDay);

			System.out.println("\n=== RENTAL DETAILS ===");
			System.out.println("Vehicle: " + vehicle[2] + " - Plate: " + vehicle[3]);
			System.out.println("Price per day: " + pricePerDay);
			System.out.println("Rental period: " + days + " day(s)");
			System.out.println("Total cost: " + totalCost);

			createPromissoryNote(
			        vehicle,
			        customerId,
			        customerName,
			        customerPhone,
			        customerEmail,
			        startDate.toString(),
			        endDate.toString(),
			        days,
			        totalCost);

			return;
		}
	}
	
	

	private void createPromissoryNote(String[] v, String customerId, String customerName, String customerPhone,String customerEmail,
			String startDate, String endDate, long rentalDays, double totalCost) {
		PromissoryNoteForm form = new PromissoryNoteForm(
		        customerName,
		        customerId,
		        customerPhone,
		        v[1],
		        v[2],
		        v[3],
		        v[7],
		        startDate,
		        endDate,
		        String.valueOf(totalCost),
		        () -> {
		            rentalFileService.saveRentalToFile(
		                    v,
		                    customerId,
		                    customerName,
		                    customerPhone,
		                    customerEmail,
		                    startDate,
		                    endDate,
		                    rentalDays,
		                    totalCost);

		            Manager manager = new Manager();
		            manager.start();
		        });
		form.setVisible(true);
	}
	
	
	

	private int readInt() {
		while (true) {
			String value = input.nextLine().trim();
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input! Please enter a number.");
			}
		}
	}
}
