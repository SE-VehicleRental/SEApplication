package software.project;

import java.util.ArrayList;
import java.util.Scanner;

public class VehicleBrowsingService {

    private final VehicleFileService vehicleFileService;

    public VehicleBrowsingService() {
        this(new VehicleFileService());
    }

    VehicleBrowsingService(
            VehicleFileService vehicleFileService) {

        this.vehicleFileService = vehicleFileService;
    }

    public void showVehiclesWithoutLogin(
            Scanner input,
            Runnable backToMainAction) {

        while (true) {
            System.out.println("\nChoose vehicle type:");
            System.out.println("1- Car");
            System.out.println("2- Motorcycle");
            System.out.println("3- Truck");
            System.out.println("4- Bus");
            System.out.println("5- Back");

            int choice = readInt(input);

            if (choice == 5) {
                backToMainAction.run();
                return;
            }

            String selectedType =
                    getVehicleTypeFromChoice(choice);

            if (selectedType == null) {
                System.out.println("Invalid choice!");
                continue;
            }

            displayAvailableVehiclesByType(selectedType);

            String again = readYesOrNo(
                    input,
                    "\nDo you want to view another type? (yes/no): "
            );

            if (again.equalsIgnoreCase("no")) {
                backToMainAction.run();
                return;
            }
        }
    }

    public void displayAvailableVehiclesByType(
            String vehicleType) {

        System.out.println(
                "\nAvailable "
                        + vehicleType
                        + " vehicles:\n"
        );

        ArrayList<String[]> vehicles =
                vehicleFileService
                        .getAvailableVehiclesByType(vehicleType);

        if (vehicles.isEmpty()) {
            System.out.println(
                    "No available "
                            + vehicleType
                            + " vehicles found."
            );
            return;
        }

        displayVehicles(vehicles);
    }

    public void displayVehicles(
            ArrayList<String[]> vehicles) {

        for (String[] vehicle : vehicles) {
            System.out.println("ID: " + vehicle[0]);
            System.out.println("Type: " + vehicle[1]);
            System.out.println("Model: " + vehicle[2]);
            System.out.println(
                    "Plate Number: " + vehicle[3]
            );
            System.out.println("Color: " + vehicle[4]);
            System.out.println("Year: " + vehicle[6]);
            System.out.println(
                    "Price per day: " + vehicle[7]
            );
            System.out.println("-------------------------");
        }
    }

    String getVehicleTypeFromChoice(int choice) {

        switch (choice) {
        case 1:
            return "Car";

        case 2:
            return "Motorcycle";

        case 3:
            return "Truck";

        case 4:
            return "Bus";

        default:
            return null;
        }
    }

    int readInt(Scanner input) {

        while (true) {
            String value = input.nextLine().trim();

            try {
                return Integer.parseInt(value);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input! Please enter a number."
                );
            }
        }
    }

    String readYesOrNo(
            Scanner input,
            String message) {

        while (true) {
            System.out.print(message);

            String answer =
                    input.nextLine().trim();

            if (answer.equalsIgnoreCase("yes")
                    || answer.equalsIgnoreCase("no")) {

                return answer;
            }

            System.out.println("Invalid input!");
        }
    }
}