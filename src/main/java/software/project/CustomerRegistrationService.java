package software.project;

import java.util.ArrayList;
import java.util.Scanner;

public class CustomerRegistrationService {

    private final CustomerValidator validator;
    private final CustomerFileService customerFileService;
    private final LicenseService licenseService;

    public CustomerRegistrationService() {
        this(
                new CustomerValidator(),
                new CustomerFileService(),
                new LicenseService()
        );
    }

    CustomerRegistrationService(
            CustomerValidator validator,
            CustomerFileService customerFileService,
            LicenseService licenseService) {

        this.validator = validator;
        this.customerFileService = customerFileService;
        this.licenseService = licenseService;
    }

    public CustomerData registerCustomer(Scanner input) {

        String name = readValidName(input);
        String id = readUniqueValidId(input);
        String email = readValidEmail(input);
        String phone = readValidPhone(input);
        int payment = readValidPayment(input);

        ArrayList<String> licenses =
                licenseService.chooseLicenses(input);

        CustomerData customer = new CustomerData(
                id,
                name,
                email,
                phone,
                payment,
                licenses
        );

        customerFileService.saveCustomer(customer);

        return customer;
    }

    String readValidName(Scanner input) {

        while (true) {
            System.out.print("Enter name (letters only): ");
            String name = input.nextLine().trim();

            if (validator.isValidName(name)) {
                return name;
            }

            System.out.println("Invalid name! letters only.");
        }
    }

    String readUniqueValidId(Scanner input) {

        while (true) {
            System.out.print("Enter ID (7 digits only): ");
            String id = input.nextLine().trim();

            if (!validator.isValidId(id)) {
                System.out.println("Invalid id! 7 digits only.");
                continue;
            }

            if (customerFileService.isIdUnique(id)) {
                return id;
            }

            System.out.println("ID already exists! Try another one.");
        }
    }

    String readValidEmail(Scanner input) {

        while (true) {
            System.out.print(
                    "Enter email (example: abc@email.com): "
            );

            String email = input.nextLine().trim();

            if (validator.isValidEmail(email)) {
                return email;
            }

            System.out.println("Invalid email format!");
        }
    }

    String readValidPhone(Scanner input) {

        while (true) {
            System.out.print(
                    "Enter 10-digit phone number: "
            );

            String phone = input.nextLine().trim();

            if (validator.isValidPhone(phone)) {
                return phone;
            }

            System.out.println(
                    "Invalid! phone must be exactly 10 digits."
            );
        }
    }

    int readValidPayment(Scanner input) {

        while (true) {
            System.out.println("Payment:");
            System.out.println("1- Cash");
            System.out.println("2- Visa");

            int payment = readInt(input);

            if (validator.isValidPayment(payment)) {
                return payment;
            }

            System.out.println("Invalid choice!");
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
}