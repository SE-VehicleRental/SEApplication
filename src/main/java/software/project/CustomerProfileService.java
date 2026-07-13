package software.project;

import java.util.Scanner;

public class CustomerProfileService {

    private final CustomerValidator validator;
    private final CustomerFileService customerFileService;
    private final RentalFileService rentalFileService;
    private final LicenseService licenseService;

    public CustomerProfileService() {
        this(
                new CustomerValidator(),
                new CustomerFileService(),
                new RentalFileService(),
                new LicenseService()
        );
    }

    CustomerProfileService(
            CustomerValidator validator,
            CustomerFileService customerFileService,
            RentalFileService rentalFileService,
            LicenseService licenseService) {

        this.validator = validator;
        this.customerFileService = customerFileService;
        this.rentalFileService = rentalFileService;
        this.licenseService = licenseService;
    }

    public void handleExistingCustomer(
            Scanner input,
            CustomerRentalAction rentalAction,
            Runnable backToMainAction) {

        System.out.print("Enter your ID: ");
        String id = input.nextLine().trim();

        CustomerData customer =
                customerFileService.getCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found!");
            backToMainAction.run();
            return;
        }

        while (true) {
            displayCustomerInfo(customer);

            rentalFileService.showCustomerRentals(
                    customer.getId()
            );

            System.out.println("\n1- Rent a vehicle");
            System.out.println("2- Edit my information");
            System.out.println("3- Back to main menu");
            System.out.print("Choose option: ");

            int choice = readInt(input);

            if (choice == 1) {
                rentalAction.rent(
                        customer.getLicenses(),
                        customer.getId(),
                        customer.getName(),
                        customer.getPhone()
                );
                return;

            } else if (choice == 2) {
                editCustomerInfo(input, customer);

            } else if (choice == 3) {
                backToMainAction.run();
                return;

            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    public void editCustomerInfo(
            Scanner input,
            CustomerData customer) {

        while (true) {
            System.out.println("\n=== EDIT CUSTOMER INFO ===");
            System.out.println("1- Edit Email");
            System.out.println("2- Edit Phone");
            System.out.println("3- Edit Payment Method");
            System.out.println("4- Edit Licenses");
            System.out.println("5- Save changes and back");
            System.out.print("Choose option: ");

            int choice = readInt(input);

            switch (choice) {

            case 1:
                updateEmail(input, customer);
                break;

            case 2:
                updatePhone(input, customer);
                break;

            case 3:
                updatePayment(input, customer);
                break;

            case 4:
                customer.setLicenses(
                        licenseService.chooseLicenses(input)
                );
                System.out.println(
                        "Licenses updated successfully!"
                );
                break;

            case 5:
                customerFileService.updateCustomer(customer);
                System.out.println(
                        "Customer information updated successfully!"
                );
                return;

            default:
                System.out.println("Invalid choice!");
            }
        }
    }

    void displayCustomerInfo(CustomerData customer) {
        System.out.println("\n=== CUSTOMER INFO ===");
        System.out.println("ID: " + customer.getId());
        System.out.println("Name: " + customer.getName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhone());
        System.out.println(
                "Payment: "
                        + (customer.getPayment() == 1
                        ? "Cash"
                        : "Visa")
        );
        System.out.println(
                "Licenses: " + customer.getLicenses()
        );
    }

    void updateEmail(
            Scanner input,
            CustomerData customer) {

        while (true) {
            System.out.print("Enter new email: ");
            String newEmail = input.nextLine().trim();

            if (validator.isValidEmail(newEmail)) {
                customer.setEmail(newEmail);
                System.out.println(
                        "Email updated successfully!"
                );
                return;
            }

            System.out.println("Invalid email format!");
        }
    }

    void updatePhone(
            Scanner input,
            CustomerData customer) {

        while (true) {
            System.out.print(
                    "Enter new phone number (10 digits): "
            );

            String newPhone = input.nextLine().trim();

            if (validator.isValidPhone(newPhone)) {
                customer.setPhone(newPhone);
                System.out.println(
                        "Phone updated successfully!"
                );
                return;
            }

            System.out.println("Invalid phone number!");
        }
    }

    void updatePayment(
            Scanner input,
            CustomerData customer) {

        while (true) {
            System.out.println("Choose payment method:");
            System.out.println("1- Cash");
            System.out.println("2- Visa");

            int payment = readInt(input);

            if (validator.isValidPayment(payment)) {
                customer.setPayment(payment);
                System.out.println(
                        "Payment updated successfully!"
                );
                return;
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

    @FunctionalInterface
    public interface CustomerRentalAction {

        void rent(
                java.util.ArrayList<String> licenses,
                String customerId,
                String customerName,
                String customerPhone
        );
    }
}