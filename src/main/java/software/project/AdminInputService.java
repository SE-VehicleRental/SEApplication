package software.project;

import java.util.Scanner;

public class AdminInputService {

    private final Scanner input;

    public AdminInputService() {
        this(new Scanner(System.in));
    }

    AdminInputService(Scanner input) {
        this.input = input;
    }

    public int readInt() {

        while (true) {

            if (input.hasNextInt()) {
                return input.nextInt();
            }

            System.out.println(
                    "Invalid input! Please enter a number."
            );
            input.next();
        }
    }

    public String readType() {

        while (true) {
            System.out.print("Enter type: ");
            String type = input.next();

            if (type.matches("[A-Za-z ]+")) {
                return type;
            }

            System.out.println(
                    "Invalid type! Please enter letters only."
            );
        }
    }

    public String readModel() {

        while (true) {
            System.out.print("Enter model: ");
            String model = input.next();

            if (model.matches("[A-Za-z0-9]+")) {
                return model;
            }

            System.out.println("Invalid model!");
        }
    }

    public String readColor() {

        while (true) {
            System.out.print("Enter color: ");
            String color = input.next();

            if (color.matches("[A-Za-z]+")) {
                return color;
            }

            System.out.println(
                    "Invalid color! Please enter letters only."
            );
        }
    }

    public int readYear() {

        while (true) {
            System.out.print("Enter year: ");

            if (input.hasNextInt()) {
                int year = input.nextInt();

                if (year >= 1950 && year <= 2026) {
                    return year;
                }

                System.out.println("Invalid year!");

            } else {
                System.out.println(
                        "Year must be a number."
                );
                input.next();
            }
        }
    }

    public double readPrice() {

        while (true) {
            System.out.print("Enter price per day: ");

            if (input.hasNextDouble()) {
                double price = input.nextDouble();

                if (price > 0) {
                    return price;
                }

                System.out.println(
                        "Price must be greater than zero."
                );

            } else {
                System.out.println(
                        "Price must be a number."
                );
                input.next();
            }
        }
    }

    public String readPlateNumber(
            AdminVehicleFileService fileService) {

        while (true) {
            System.out.print(
                    "Enter plate number (6 digits): "
            );

            String plate = input.next();

            if (!isValidPlateNumber(plate)) {
                System.out.println(
                        "Invalid plate number! "
                                + "Must be exactly 6 digits."
                );
                continue;
            }

            if (fileService.plateExists(plate)) {
                System.out.println(
                        "Plate number already exists! "
                                + "Try another one."
                );
                continue;
            }

            return plate;
        }
    }

    public boolean isValidPlateNumber(
            String plate) {

        return plate != null
                && plate.matches("\\d{6}");
    }
}