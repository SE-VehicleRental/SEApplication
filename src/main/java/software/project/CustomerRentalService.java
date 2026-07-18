package software.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerRentalService {

	private final RentalCalculator rentalCalculator;
	private final RentalFileService rentalFileService;
	private final VehicleFileService vehicleFileService;
	private final LicenseService licenseService;
	private final VehicleBrowsingService vehicleBrowsingService;
	private final RentalConfirmationAction confirmationAction;

public CustomerRentalService() {
	this(
			new RentalCalculator(),
			new RentalFileService(),
			new VehicleFileService(),
			new LicenseService(),
			new VehicleBrowsingService(),
			(
					vehicle,
					customerId,
					customerName,
					customerPhone,
					customerEmail,
					startDate,
					endDate,
					rentalDays,
					totalCost,
					backToMainAction) -> {

				PromissoryNoteForm form =
						new PromissoryNoteForm(
								customerName,
								customerId,
								customerPhone,
								vehicle[1],
								vehicle[2],
								vehicle[3],
								vehicle[7],
								startDate,
								endDate,
								String.valueOf(totalCost),
								() -> {
									new RentalFileService()
											.saveRentalToFile(
													vehicle,
													customerId,
													customerName,
													customerPhone,
													customerEmail,
													startDate,
													endDate,
													rentalDays,
													totalCost
											);

									backToMainAction.run();
								}
						);

				form.setVisible(true);
			}
	);
}
	CustomerRentalService(
			RentalCalculator rentalCalculator,
			RentalFileService rentalFileService,
			VehicleFileService vehicleFileService,
			LicenseService licenseService,
			VehicleBrowsingService vehicleBrowsingService,
			RentalConfirmationAction confirmationAction) {

		this.rentalCalculator = rentalCalculator;
		this.rentalFileService = rentalFileService;
		this.vehicleFileService = vehicleFileService;
		this.licenseService = licenseService;
		this.vehicleBrowsingService = vehicleBrowsingService;
		this.confirmationAction = confirmationAction;
	}

	public void rentVehicle(
			Scanner input,
			ArrayList<String> licenses,
			String customerId,
			String customerName,
			String customerPhone,
			String customerEmail,
			Runnable backToMainAction) {

		String chosenLicense =
				licenseService.chooseOneLicense(input, licenses);

		LocalDate startDate;
		LocalDate endDate;
		long rentalDays;

		while (true) {
			try {
				System.out.print(
						"Enter rental start date (yyyy-mm-dd): "
				);

				startDate =
						LocalDate.parse(input.nextLine().trim());

				System.out.print(
						"Enter rental end date (yyyy-mm-dd): "
				);

				endDate =
						LocalDate.parse(input.nextLine().trim());

				rentalDays =
						rentalCalculator.calculateRentalDays(
								startDate,
								endDate
						);

				break;

			} catch (RuntimeException e) {
				System.out.println("Invalid date! " + e.getMessage());
			}
		}

		System.out.println(
				"\nAvailable "
						+ chosenLicense
						+ " vehicles:\n"
		);

		ArrayList<String[]> vehicles =
				vehicleFileService.getAvailableVehiclesByType(
						chosenLicense,
						startDate,
						endDate
				);

		if (vehicles.isEmpty()) {
			System.out.println(
					"No vehicles are available during "
							+ "the selected period."
			);

			backToMainAction.run();
			return;
		}

		vehicleBrowsingService.displayVehicles(vehicles);

		while (true) {
			System.out.print(
					"Enter Vehicle ID to rent: "
			);

			int vehicleId = readInt(input);

			String[] vehicle =
					vehicleFileService.findVehicleById(
							vehicles,
							vehicleId
					);

			if (vehicle == null) {
				System.out.println(
						"Invalid Vehicle ID! Please choose one "
								+ "of the available vehicles."
				);
				continue;
			}

			double pricePerDay =
					Double.parseDouble(vehicle[7]);

			double totalCost =
					rentalCalculator.calculateTotalCost(
							rentalDays,
							pricePerDay
					);

			System.out.println("\n=== RENTAL DETAILS ===");

			System.out.println(
					"Vehicle: "
							+ vehicle[2]
							+ " - Plate: "
							+ vehicle[3]
			);

			System.out.println(
					"Price per day: " + pricePerDay
			);

			System.out.println(
					"Rental period: "
							+ rentalDays
							+ " day(s)"
			);

			System.out.println(
					"Total cost: " + totalCost
			);

			createPromissoryNote(
					vehicle,
					customerId,
					customerName,
					customerPhone,
					customerEmail,
					startDate.toString(),
					endDate.toString(),
					rentalDays,
					totalCost,
					backToMainAction
			);

			return;
		}
	}

private void createPromissoryNote(
		String[] vehicle,
		String customerId,
		String customerName,
		String customerPhone,
		String customerEmail,
		String startDate,
		String endDate,
		long rentalDays,
		double totalCost,
		Runnable backToMainAction) {

	confirmationAction.show(
			vehicle,
			customerId,
			customerName,
			customerPhone,
			customerEmail,
			startDate,
			endDate,
			rentalDays,
			totalCost,
			backToMainAction
	);
}
	int readInt(Scanner input) {

		while (true) {
			String value =
					input.nextLine().trim();

			try {
				return Integer.parseInt(value);

			} catch (NumberFormatException e) {
				System.out.println(
						"Invalid input! Please enter a number."
				);
			}
		}
	}

@FunctionalInterface
interface RentalConfirmationAction {

	void show(
			String[] vehicle,
			String customerId,
			String customerName,
			String customerPhone,
			String customerEmail,
			String startDate,
			String endDate,
			long rentalDays,
			double totalCost,
			Runnable backToMainAction
	);
}}