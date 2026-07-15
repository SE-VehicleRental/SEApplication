package software.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LicenseService {

    public ArrayList<String> getAllLicenses() {
        return new ArrayList<>(
                List.of("Car", "Motorcycle", "Truck", "Bus")
        );
    }

    public ArrayList<String> chooseLicenses(Scanner input) {

        ArrayList<String> available = getAllLicenses();
        ArrayList<String> selected = new ArrayList<>();

        while (true) {

            System.out.println("License types:");

            for (int i = 0; i < available.size(); i++) {
                System.out.println((i + 1) + "- " + available.get(i));
            }

            int choice;

            while (true) {

                String value = input.nextLine().trim();

                try {

                    choice = Integer.parseInt(value);

                    if (choice >= 1 && choice <= available.size()) {
                        break;
                    }

                    System.out.println("Invalid license!");

                } catch (NumberFormatException e) {

                    System.out.println("Invalid input! Please enter a number.");
                }
            }

            String picked = available.remove(choice - 1);
            selected.add(picked);

            if (available.isEmpty()) {
                break;
            }

            String answer;

            while (true) {

                System.out.print("Do you have another license? (yes/no): ");

                answer = input.nextLine().trim();

                if (answer.equalsIgnoreCase("yes")
                        || answer.equalsIgnoreCase("no")) {
                    break;
                }

                System.out.println("Invalid input!");
            }

            if (answer.equalsIgnoreCase("no")) {
                break;
            }
        }

        return selected;
    }

    public String chooseOneLicense(
            Scanner input,
            ArrayList<String> licenses) {

        if (licenses == null || licenses.isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer must have at least one license"
            );
        }

        if (licenses.size() == 1) {
            return licenses.get(0);
        }

        System.out.println("Choose license type:");

        for (int i = 0; i < licenses.size(); i++) {
            System.out.println(
                    (i + 1) + "- " + licenses.get(i)
            );
        }

        while (true) {

            String value = input.nextLine().trim();

            try {

                int choice = Integer.parseInt(value);

                if (choice >= 1 && choice <= licenses.size()) {
                    return licenses.get(choice - 1);
                }

                System.out.println("Invalid choice! Try again.");

            } catch (NumberFormatException e) {

                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
}