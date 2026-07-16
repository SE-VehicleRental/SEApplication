package software.project;

import java.util.Scanner;

public class customerMenu {
	Scanner input = new Scanner(System.in);
	private final CustomerRegistrationService registrationService = new CustomerRegistrationService();
	private final VehicleBrowsingService vehicleBrowsingService = new VehicleBrowsingService();
	private final CustomerProfileService profileService = new CustomerProfileService();
	private final CustomerRentalService rentalService = new CustomerRentalService();

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
			profileService.handleExistingCustomer(input,
					(licenses, customerId, customerName, customerPhone, customerEmail) -> rentalService.rentVehicle(
							input, licenses, customerId, customerName, customerPhone, customerEmail, () -> {
								Manager manager = new Manager();
								manager.start();
							}),
					() -> {
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
			rentalService.rentVehicle(input, newCustomer.getLicenses(), newCustomer.getId(), newCustomer.getName(),
					newCustomer.getPhone(), newCustomer.getEmail(), () -> {
						Manager manager = new Manager();
						manager.start();
					});
		} else {
			Manager m = new Manager();
			m.start();
		}
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
