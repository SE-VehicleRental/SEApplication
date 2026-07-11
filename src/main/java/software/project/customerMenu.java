package software.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class customerMenu {

	Scanner input = new Scanner(System.in);
	private final CustomerValidator validator = new CustomerValidator();
	private final RentalCalculator rentalCalculator = new RentalCalculator();
	private final CustomerFileService customerFileService = new CustomerFileService();
	private final RentalFileService rentalFileService = new RentalFileService();
	private final VehicleFileService vehicleFileService = new VehicleFileService();
	private final LicenseService licenseService = new LicenseService();

	public void showMenu() {

		System.out.println("Customer System");
		System.out.println("1- View vehicles without login");
		System.out.println("2- Login / Register");

		int firstChoice;

		while (true) {
			System.out.print("Choose option: ");
			firstChoice = input.nextInt();

			if (firstChoice == 1 || firstChoice == 2) {
				break;
			} else {
				System.out.println("Invalid input!");
			}
		}
		if (firstChoice == 1) {
			showVehiclesWithoutLogin();
			return;
		}

		System.out.println("\n1- New Customer");
		System.out.println("2- Existing Customer");

		int choice;

		while (true) {
			System.out.print("Choose option: ");
			choice = input.nextInt();

			if (choice == 1 || choice == 2) {
				break;
			} else {
				System.out.println("Invalid input!");
			}
		}

		if (choice == 2) {
			handleExistingCustomer();
			return;
		}

		String name;
		while (true) {
			System.out.print("Enter name (letters only): ");
			name = input.next();

			if (validator.isValidName(name)) {
				break;
			} else {
				System.out.println("Invalid name! letters only.");
			}
		}

		String id;
		while (true) {
			System.out.print("Enter ID (7 digits only): ");
			id = input.next();

			if (!validator.isValidId(id)) {
				System.out.println("Invalid id! 7 digits only.");
				continue;
			}

			if (customerFileService.isIdUnique(id)) {
				break;
			} else {
				System.out.println("ID already exists! Try another one.");
			}
		}

		String email;
		while (true) {
			System.out.print("Enter email (example: abc@email.com): ");
			email = input.next();

			if (validator.isValidEmail(email)) {
				break;
			} else {
				System.out.println("Invalid email format!");
			}
		}

		String phone;
		while (true) {
			System.out.print("Enter 10-digit phone number: ");
			phone = input.next();

			if (validator.isValidPhone(phone)) {
				break;
			} else {
				System.out.println("Invalid! phone must be exactly 10 digits.");
			}
		}

		int payment;
		while (true) {
			System.out.println("Payment:");
			System.out.println("1- Cash");
			System.out.println("2- Visa");

			payment = input.nextInt();

			if (validator.isValidPayment(payment)) {
				break;
			} else {
				System.out.println("Invalid choice!");
			}
		}

		ArrayList<String> selected = licenseService.chooseLicenses(input);

		CustomerData newCustomer = new CustomerData(id, name, email, phone, payment, selected);

		customerFileService.saveCustomer(newCustomer);
		System.out.println("Data saved successfully!");

		String rent;
		while (true) {
			System.out.print("Do you want to rent a vehicle? (yes/no): ");
			rent = input.next();

			if (rent.equalsIgnoreCase("yes") || rent.equalsIgnoreCase("no")) {
				break;
			} else {
				System.out.println("Invalid input! Please enter yes or no.");
			}
		}

		if (rent.equalsIgnoreCase("yes")) {
			rentVehicle(selected, id, name, phone);
		} else {
			Manager m = new Manager();
			m.start();
		}
	}

	private void rentVehicle(ArrayList<String> licenses, String customerId, String customerName, String customerPhone) {

		String chosenLicense = licenseService.chooseOneLicense(input, licenses);

		System.out.println("\nAvailable " + chosenLicense + " vehicles:\n");

		ArrayList<String[]> vehicles = vehicleFileService.getAvailableVehiclesByType(chosenLicense);

		if (vehicles.isEmpty()) {
			System.out.println("No available vehicles for this license.");

			Manager manager = new Manager();
			manager.start();
			return;
		}

		displayVehicles(vehicles);

		while (true) {
			System.out.print("Enter Vehicle ID to rent: ");
			int vehicleId = input.nextInt();

			String[] vehicle = vehicleFileService.findVehicleById(vehicles, vehicleId);

			if (vehicle == null) {
				System.out.println("Invalid Vehicle ID! Please choose one " + "of the available vehicles.");
				continue;
			}

			double pricePerDay = Double.parseDouble(vehicle[7]);

			LocalDate startDate;
			LocalDate endDate;

			while (true) {
				try {
					System.out.print("Enter rental start date (yyyy-mm-dd): ");
					startDate = LocalDate.parse(input.next());

					System.out.print("Enter rental end date (yyyy-mm-dd): ");
					endDate = LocalDate.parse(input.next());

					long days = rentalCalculator.calculateRentalDays(startDate, endDate);

					double totalCost = rentalCalculator.calculateTotalCost(days, pricePerDay);

					System.out.println("\n=== RENTAL DETAILS ===");
					System.out.println("Vehicle: " + vehicle[2] + " - Plate: " + vehicle[3]);
					System.out.println("Price per day: " + pricePerDay);
					System.out.println("Rental period: " + days + " day(s)");
					System.out.println("Total cost: " + totalCost);

					createPromissoryNote(vehicle, customerId, customerName, customerPhone, startDate.toString(),
							endDate.toString(), days, totalCost);

					return;

				} catch (IllegalArgumentException e) {
					System.out.println("Invalid date! " + e.getMessage());
				}
			}
		}
	}

	private void displayVehicles(ArrayList<String[]> vehicles) {

		for (String[] vehicle : vehicles) {
			System.out.println("ID: " + vehicle[0]);
			System.out.println("Type: " + vehicle[1]);
			System.out.println("Model: " + vehicle[2]);
			System.out.println("Plate Number: " + vehicle[3]);
			System.out.println("Color: " + vehicle[4]);
			System.out.println("Year: " + vehicle[6]);
			System.out.println("Price per day: " + vehicle[7]);
			System.out.println("-------------------------");
		}
	}

	private void handleExistingCustomer() {
		System.out.print("Enter your ID: ");
		String id = input.next();

		CustomerData customer = customerFileService.getCustomerById(id);

		if (customer == null) {
			System.out.println("Customer not found!");
			Manager m = new Manager();
			m.start();
			return;
		}

		while (true) {
			System.out.println("\n=== CUSTOMER INFO ===");
			System.out.println("ID: " + customer.getId());
			System.out.println("Name: " + customer.getName());
			System.out.println("Email: " + customer.getEmail());
			System.out.println("Phone: " + customer.getPhone());
			System.out.println("Payment: " + (customer.getPayment() == 1 ? "Cash" : "Visa"));
			System.out.println("Licenses: " + customer.getLicenses());

			rentalFileService.showCustomerRentals(customer.getId());

			System.out.println("\n1- Rent a vehicle");
			System.out.println("2- Edit my information");
			System.out.println("3- Back to main menu");
			System.out.print("Choose option: ");

			int choice = input.nextInt();

			if (choice == 1) {
				rentVehicle(customer.getLicenses(), customer.getId(), customer.getName(), customer.getPhone());
				return;

			} else if (choice == 2) {
				editCustomerInfo(customer);

			} else if (choice == 3) {
				Manager m = new Manager();
				m.start();
				return;

			} else {
				System.out.println("Invalid choice!");
			}
		}
	}

	private void editCustomerInfo(CustomerData customer) {

		while (true) {
			System.out.println("\n=== EDIT CUSTOMER INFO ===");
			System.out.println("1- Edit Email");
			System.out.println("2- Edit Phone");
			System.out.println("3- Edit Payment Method");
			System.out.println("4- Edit Licenses");
			System.out.println("5- Save changes and back");
			System.out.print("Choose option: ");

			int choice = input.nextInt();

			switch (choice) {

			case 1:
				while (true) {
					System.out.print("Enter new email: ");
					String newEmail = input.next();

					if (validator.isValidEmail(newEmail)) {
						customer.setEmail(newEmail);
						System.out.println("Email updated successfully!");
						break;
					} else {
						System.out.println("Invalid email format!");
					}
				}
				break;

			case 2:
				while (true) {
					System.out.print("Enter new phone number (10 digits): ");
					String newPhone = input.next();

					if (validator.isValidPhone(newPhone)) {
						customer.setPhone(newPhone);
						System.out.println("Phone updated successfully!");
						break;
					} else {
						System.out.println("Invalid phone number!");
					}
				}
				break;

			case 3:
				while (true) {
					System.out.println("Choose payment method:");
					System.out.println("1- Cash");
					System.out.println("2- Visa");

					int payment = input.nextInt();

					if (validator.isValidPayment(payment)) {
						customer.setPayment(payment);
						System.out.println("Payment updated successfully!");
						break;
					} else {
						System.out.println("Invalid choice!");
					}
				}
				break;

			case 4:
				customer.setLicenses(licenseService.chooseLicenses(input));
				System.out.println("Licenses updated successfully!");
				break;

			case 5:
				customerFileService.updateCustomer(customer);
				System.out.println("Customer information updated successfully!");
				return;

			default:
				System.out.println("Invalid choice!");
			}
		}
	}
	
	private void showVehiclesWithoutLogin() {

		while (true) {
			System.out.println("\nChoose vehicle type:");
			System.out.println("1- Car");
			System.out.println("2- Motorcycle");
			System.out.println("3- Truck");
			System.out.println("4- Bus");
			System.out.println("5- Back");

			int choice = input.nextInt();
			String selectedType = "";

			switch (choice) {
			case 1:
				selectedType = "Car";
				break;
			case 2:
				selectedType = "Motorcycle";
				break;
			case 3:
				selectedType = "Truck";
				break;
			case 4:
				selectedType = "Bus";
				break;
			case 5:
				Manager m = new Manager();
				m.start();
				return;
			default:
				System.out.println("Invalid choice!");
				continue;
			}

			displayAvailableVehiclesByType(selectedType);

			String again;
			while (true) {
				System.out.print("\nDo you want to view another type? (yes/no): ");
				again = input.next();

				if (again.equalsIgnoreCase("yes") || again.equalsIgnoreCase("no")) {
					break;
				} else {
					System.out.println("Invalid input!");
				}
			}

			if (again.equalsIgnoreCase("no")) {
				Manager m = new Manager();
				m.start();
				return;
			}
		}
	}

	private void displayAvailableVehiclesByType(String vehicleType) {

		System.out.println("\nAvailable " + vehicleType + " vehicles:\n");

		ArrayList<String[]> vehicles = vehicleFileService.getAvailableVehiclesByType(vehicleType);

		if (vehicles.isEmpty()) {
			System.out.println("No available " + vehicleType + " vehicles found.");
			return;
		}

		displayVehicles(vehicles);
	}

	private void createPromissoryNote(String[] v, String customerId, String customerName, String customerPhone,
			String startDate, String endDate, long rentalDays, double totalCost) {

		PromissoryNoteForm form = new PromissoryNoteForm(customerName, customerId, customerPhone, v[1], v[2], v[3],
				v[7], startDate, endDate, String.valueOf(totalCost), () -> rentalFileService.saveRentalToFile(v,
						customerId, customerName, customerPhone, startDate, endDate, rentalDays, totalCost));

		form.setVisible(true);
	}

}