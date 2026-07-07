package software.project;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class customerMenu {
	
	private static class CustomerData {
	    String id;
	    String name;
	    String email;
	    String phone;
	    int payment; 
	    ArrayList<String> licenses = new ArrayList<>();
	}
	
    Scanner input = new Scanner(System.in);
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

            if (name.matches("[a-zA-Z]+")) {
                break;
            } else {
                System.out.println("Invalid name! letters only.");
            }
        }

        String id;
        while (true) {
            System.out.print("Enter ID (7 digits only): ");
            id = input.next();

            if (!id.matches("\\d{7}")) {
                System.out.println("Invalid id! 7 digits only.");
                continue;
            }

            if (isIdUnique(id)) {
                break;
            } else {
                System.out.println("ID already exists! Try another one.");
            }
        }

        String email;
        while (true) {
            System.out.print("Enter email (example: abc@email.com): ");
            email = input.next();

            if (email.matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,}")) {
                break;
            } else {
                System.out.println("Invalid email format!");
            }
        }

        String phone;
        while (true) {
            System.out.print("Enter 10-digit phone number: ");
            phone = input.next();

            if (phone.matches("\\d{10}")) {
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

            if (payment == 1 || payment == 2) {
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }

        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("Car");
        licenses.add("Motorcycle");
        licenses.add("Truck");
        licenses.add("Bus");

        ArrayList<String> selected = new ArrayList<>();

        while (true) {
            System.out.println("License types:");

            for (int i = 0; i < licenses.size(); i++) {
                System.out.println((i + 1) + "- " + licenses.get(i));
            }

            int choice1 = input.nextInt();
            if (choice1 < 1 || choice1 > licenses.size()) {
                System.out.println("Invalid license!");
                continue;
            }

            String picked = licenses.get(choice1 - 1);
            selected.add(picked);
            licenses.remove(choice1 - 1);

            if (licenses.isEmpty()) {
                break;
            }

            String answer;
            while (true) {
                System.out.print("Do you have another license? (yes/no): ");
                answer = input.next();

                if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
                    break;
                }

                System.out.println("Invalid input! Try again.");
            }

            if (answer.equalsIgnoreCase("no")) {
                break;
            }
        }

        saveToFile(name, id, email, phone, payment, selected);
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
    
    private boolean isIdUnique(String id) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("customers.txt"));

            String line;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("ID: ")) {
                    String existingId = line.substring(4).trim();

                    if (existingId.equals(id)) {
                        br.close();
                        return false;
                    }
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Error reading file!");
        }

        return true;
    }
    
    
    private void rentVehicle(ArrayList<String> licenses, String customerId, String customerName, String customerPhone) {

        String chosenLicense;

        if (licenses.size() == 1) {
            chosenLicense = licenses.get(0);
        } else {
            System.out.println("Choose license type:");

            for (int i = 0; i < licenses.size(); i++) {
                System.out.println((i + 1) + "- " + licenses.get(i));
            }

            int choice;
            while (true) {
                choice = input.nextInt();

                if (choice >= 1 && choice <= licenses.size()) {
                    break;
                } else {
                    System.out.println("Invalid choice! Try again.");
                }
            }

            chosenLicense = licenses.get(choice - 1);
        }

        System.out.println("\nAvailable " + chosenLicense + " vehicles:\n");

        try {
            BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));
            String line;
            ArrayList<String[]> vehicles = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 8) {
                    continue;
                }

                if (data[1].equalsIgnoreCase(chosenLicense)) {
                    int vehicleId = Integer.parseInt(data[0]);

                    if (!isVehicleRented(vehicleId)) {
                        vehicles.add(data);

                        System.out.println("ID: " + data[0]);
                        System.out.println("Type: " + data[1]);
                        System.out.println("Model: " + data[2]);
                        System.out.println("Plate Number: " + data[5]);
                        System.out.println("Color: " + data[4]);
                
                        System.out.println("Year: " + data[6]);
                        System.out.println("Price per day: " + data[7]);
                        System.out.println("-------------------------");
                    }
                }
            }

            br.close();

            if (vehicles.isEmpty()) {
                System.out.println("No available vehicles for this license.");
                Manager m = new Manager();
                m.start();
                return;
            }

            while (true) {
                System.out.print("Enter Vehicle ID to rent: ");
                int id = input.nextInt();

                boolean found = false;

                for (String[] v : vehicles) {
                    if (Integer.parseInt(v[0]) == id) {
                        found = true;

                        double pricePerDay = Double.parseDouble(v[7]);
                        LocalDate startDate;
                        LocalDate endDate;

                        while (true) {
                            try {
                                System.out.print("Enter rental start date (yyyy-mm-dd): ");
                                startDate = LocalDate.parse(input.next());

                                System.out.print("Enter rental end date (yyyy-mm-dd): ");
                                endDate = LocalDate.parse(input.next());

                                if (endDate.isBefore(startDate)) {
                                    System.out.println("End date cannot be before start date!");
                                    continue;
                                }

                                break;
                            } catch (Exception e) {
                                System.out.println("Invalid date format! Please use yyyy-mm-dd");
                            }
                        }

                        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                        double totalCost = days * pricePerDay;

                        System.out.println("\n=== RENTAL DETAILS ===");
                        System.out.println("Vehicle: " + v[2] + " - Plate: " + v[3]);
                        System.out.println("Price per day: " + pricePerDay);
                        System.out.println("Rental period: " + days + " day(s)");
                        System.out.println("Total cost: " + totalCost);

                        createPromissoryNote(v, customerId, customerName, customerPhone,
                                startDate.toString(), endDate.toString(), days, totalCost);
                     return;
                    }
                }

                if (!found) {
                    System.out.println("Invalid Vehicle ID! Please choose one of the available vehicles.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading vehicles file.");
        }
        
    }
    
    private void saveRental(String[] v, String customerId, String customerName,
            String customerPhone, String startDate, String endDate,
            long rentalDays, double totalCost) {

        try {
            FileWriter fw = new FileWriter("customer_rentals.txt", true);

            fw.write("CustomerID: " + customerId + "\n");
            fw.write("CustomerName: " + customerName + "\n");
            fw.write("CustomerPhone: " + customerPhone + "\n");

            fw.write("VehicleID: " + v[0] + "\n");
            fw.write("VehicleType: " + v[1] + "\n");
            fw.write("VehicleModel: " + v[2] + "\n");
            fw.write("PlateNumber: " + v[3] + "\n");
            fw.write("VehicleColor: " + v[4] + "\n");
            fw.write("VehicleNumber: " + v[5] + "\n");
            fw.write("VehicleYear: " + v[6] + "\n");
            fw.write("PricePerDay: " + v[7] + "\n");

            fw.write("RentalStartDate: " + startDate + "\n");
            fw.write("RentalEndDate: " + endDate + "\n");
            fw.write("RentalDays: " + rentalDays + "\n");
            fw.write("TotalCost: " + totalCost + "\n");
            fw.write("---------------------\n");

            fw.close();

            System.out.println("Rental saved successfully!");

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
                    String.valueOf(totalCost), null 
            );

            form.setVisible(true);

        } catch (IOException e) {
            System.out.println("Error saving rental!");
        }
    }
    
  
    
    
    private void handleExistingCustomer() {
        System.out.print("Enter your ID: ");
        String id = input.next();

        CustomerData customer = getCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found!");
            Manager m = new Manager();
            m.start();
            return;
        }

        while (true) {
            System.out.println("\n=== CUSTOMER INFO ===");
            System.out.println("ID: " + customer.id);
            System.out.println("Name: " + customer.name);
            System.out.println("Email: " + customer.email);
            System.out.println("Phone: " + customer.phone);
            System.out.println("Payment: " + (customer.payment == 1 ? "Cash" : "Visa"));
            System.out.println("Licenses: " + customer.licenses);

            showCustomerRentals(customer.id);

            System.out.println("\n1- Rent a vehicle");
            System.out.println("2- Edit my information");
            System.out.println("3- Back to main menu");
            System.out.print("Choose option: ");

            int choice = input.nextInt();

            if (choice == 1) {
                rentVehicle(customer.licenses, customer.id, customer.name, customer.phone);
                return;
            } 
            else if (choice == 2) {
                editCustomerInfo(customer);
            } 
            else if (choice == 3) {
                Manager m = new Manager();
                m.start();
                return;
            } 
            else {
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

                        if (newEmail.matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,}")) {
                            customer.email = newEmail;
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

                        if (newPhone.matches("\\d{10}")) {
                            customer.phone = newPhone;
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

                        if (payment == 1 || payment == 2) {
                            customer.payment = payment;
                            System.out.println("Payment updated successfully!");
                            break;
                        } else {
                            System.out.println("Invalid choice!");
                        }
                    }
                    break;

                case 4:
                    customer.licenses = chooseLicenses();
                    System.out.println("Licenses updated successfully!");
                    break;

                case 5:
                    updateCustomerInFile(customer);
                    System.out.println("Customer information updated successfully!");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    
    private ArrayList<String> chooseLicenses() {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("Car");
        licenses.add("Motorcycle");
        licenses.add("Truck");
        licenses.add("Bus");

        ArrayList<String> selected = new ArrayList<>();

        while (true) {
            System.out.println("License types:");

            for (int i = 0; i < licenses.size(); i++) {
                System.out.println((i + 1) + "- " + licenses.get(i));
            }

            int choice = input.nextInt();

            if (choice < 1 || choice > licenses.size()) {
                System.out.println("Invalid license!");
                continue;
            }

            String picked = licenses.get(choice - 1);
            selected.add(picked);
            licenses.remove(choice - 1);

            if (licenses.isEmpty()) {
                break;
            }

            String answer;
            while (true) {
                System.out.print("Do you have another license? (yes/no): ");
                answer = input.next();

                if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
                    break;
                } else {
                    System.out.println("Invalid input!");
                }
            }

            if (answer.equalsIgnoreCase("no")) {
                break;
            }
        }

        return selected;
    }
    
    
    
    private void updateCustomerInFile(CustomerData updatedCustomer) {
        ArrayList<CustomerData> customers = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("customers.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("ID: ")) {
                    CustomerData customer = new CustomerData();
                    customer.id = line.substring(4).trim();

                    while ((line = br.readLine()) != null && !line.startsWith("----------------------")) {

                        if (line.startsWith("Name: ")) {
                            customer.name = line.substring(6).trim();

                        } else if (line.startsWith("Email: ")) {
                            customer.email = line.substring(7).trim();

                        } else if (line.startsWith("Phone: ")) {
                            customer.phone = line.substring(7).trim();

                        } else if (line.startsWith("Payment: ")) {
                            String paymentText = line.substring(9).trim();
                            if (paymentText.equalsIgnoreCase("Cash")) {
                                customer.payment = 1;
                            } else {
                                customer.payment = 2;
                            }

                        } else if (line.startsWith("License: ")) {
                            String licensesText = line.substring(9).trim();
                            customer.licenses = parseLicenses(licensesText);
                        }
                    }

                    if (customer.id.equals(updatedCustomer.id)) {
                        customers.add(updatedCustomer);
                    } else {
                        customers.add(customer);
                    }
                }
            }

            br.close();

            FileWriter writer = new FileWriter("customers.txt", false);

            for (CustomerData c : customers) {
                writer.write("ID: " + c.id + "\n");
                writer.write("Name: " + c.name + "\n");
                writer.write("Email: " + c.email + "\n");
                writer.write("Phone: " + c.phone + "\n");
                writer.write("Payment: " + (c.payment == 1 ? "Cash" : "Visa") + "\n");
                writer.write("License: " + c.licenses + "\n");
                writer.write("----------------------\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error updating customer file!");
        }
    }
    

    
    private CustomerData getCustomerById(String targetId) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("customers.txt"));
            String line;

            CustomerData customer = null;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("ID: ")) {
                    String id = line.substring(4).trim();

                    if (id.equals(targetId)) {
                        customer = new CustomerData();
                        customer.id = id;

                        while ((line = br.readLine()) != null && !line.startsWith("----------------------")) {

                            if (line.startsWith("Name: ")) {
                                customer.name = line.substring(6).trim();

                            } else if (line.startsWith("Email: ")) {
                                customer.email = line.substring(7).trim();

                            } else if (line.startsWith("Phone: ")) {
                                customer.phone = line.substring(7).trim();

                            } else if (line.startsWith("Payment: ")) {
                                String paymentText = line.substring(9).trim();
                                if (paymentText.equalsIgnoreCase("Cash")) {
                                    customer.payment = 1;
                                } else {
                                    customer.payment = 2;
                                }

                            } else if (line.startsWith("License: ")) {
                                String licensesText = line.substring(9).trim();
                                customer.licenses = parseLicenses(licensesText);
                            }
                        }

                        br.close();
                        return customer;
                    }
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Error reading customer file!");
        }

        return null;
    }
    
    private ArrayList<String> parseLicenses(String licensesText) {
        ArrayList<String> licenses = new ArrayList<>();

        licensesText = licensesText.replace("[", "").replace("]", "").trim();

        if (licensesText.isEmpty()) {
            return licenses;
        }

        String[] parts = licensesText.split(",");

        for (String part : parts) {
            licenses.add(part.trim());
        }

        return licenses;
    }
    
    private void showCustomerRentals(String customerId) {
        System.out.println("\n=== YOUR RENTALS ===");

        boolean hasRental = false;

        try {
            BufferedReader br = new BufferedReader(new FileReader("customer_rentals.txt"));
            String line;
            boolean printBlock = false;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("CustomerID: ")) {
                    String id = line.substring(12).trim();

                    if (id.equals(customerId)) {
                        printBlock = true;
                        hasRental = true;
                    } else {
                        printBlock = false;
                    }
                }

                if (printBlock) {
                    System.out.println(line);

                    if (line.startsWith("---------------------")) {
                        printBlock = false;
                    }
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("No rentals file found yet.");
            return;
        }

        if (!hasRental) {
            System.out.println("No previous rentals found.");
        }
    }
    
    
    private boolean isVehicleRented(int vehicleId) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("customer_rentals.txt"));
            String line;

            boolean vehicleFound = false;
            LocalDate rentalEndDate = null;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("VehicleID: ")) {
                    int rentedId = Integer.parseInt(line.substring(11).trim());

                    if (rentedId == vehicleId) {
                        vehicleFound = true;
                        rentalEndDate = null;
                    } else {
                        vehicleFound = false;
                    }
                }

                if (vehicleFound && line.startsWith("RentalEndDate: ")) {
                    String endDateText = line.substring(15).trim();
                    rentalEndDate = LocalDate.parse(endDateText);

                    if (!rentalEndDate.isBefore(LocalDate.now())) {
                        br.close();
                        return true;
                    }
                }
            }

            br.close();

        } catch (IOException e) {
            return false;
        }

        return false;
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

        boolean found = false;

        try {
            BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 7) {
                    continue;
                }


                if (data[1].equalsIgnoreCase(vehicleType)) {
                    int vehicleId = Integer.parseInt(data[0]);

                    if (!isVehicleRented(vehicleId)) {
                        found = true;

                        System.out.println("ID: " + data[0]);
                        System.out.println("Type: " + data[1]);
                        System.out.println("Model: " + data[2]);
                        System.out.println("Plate Number: " + data[3]);
                        System.out.println("Color: " + data[4]);
                        System.out.println("Year: " + data[6]);
                        System.out.println("Price per day: " + data[7]);
                        System.out.println("-------------------------");
                    }
                }
            }

            br.close();

            if (!found) {
                System.out.println("No available " + vehicleType + " vehicles found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading vehicles file.");
        }
    }
    
    
    
    private void createPromissoryNote(String[] v, String customerId, String customerName,
            String customerPhone, String startDate, String endDate,
            long rentalDays, double totalCost) {

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
                () -> saveRentalToFile(v, customerId, customerName, customerPhone,
                        startDate, endDate, rentalDays, totalCost)
        );

        form.setVisible(true);
    }

    private void saveRentalToFile(String[] v, String customerId, String customerName,
            String customerPhone, String startDate, String endDate,
            long rentalDays, double totalCost) {

        try {
            FileWriter fw = new FileWriter("customer_rentals.txt", true);

            fw.write("CustomerID: " + customerId + "\n");
            fw.write("CustomerName: " + customerName + "\n");
            fw.write("CustomerPhone: " + customerPhone + "\n");

            fw.write("VehicleID: " + v[0] + "\n");
            fw.write("VehicleType: " + v[1] + "\n");
            fw.write("VehicleModel: " + v[2] + "\n");
            fw.write("PlateNumber: " + v[3] + "\n");
            fw.write("VehicleColor: " + v[4] + "\n");
            fw.write("VehicleNumber: " + v[5] + "\n");
            fw.write("VehicleYear: " + v[6] + "\n");
            fw.write("PricePerDay: " + v[7] + "\n");

            fw.write("RentalStartDate: " + startDate + "\n");
            fw.write("RentalEndDate: " + endDate + "\n");
            fw.write("RentalDays: " + rentalDays + "\n");
            fw.write("TotalCost: " + totalCost + "\n");
            fw.write("---------------------\n");

            fw.close();

            System.out.println("Rental saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving rental!");
        }
    }
    
    
    
    
    
    public void saveToFile(String name, String id, String email, String phone, int payment, ArrayList<String> licenses) {
        try {
            FileWriter writer = new FileWriter("customers.txt", true);

            writer.write("ID: " + id + "\n");
            writer.write("Name: " + name + "\n");
            writer.write("Email: " + email + "\n");
            writer.write("Phone: " + phone + "\n");
            writer.write("Payment: " + (payment == 1 ? "Cash" : "Visa") + "\n");
            writer.write("License: " + licenses + "\n");
            writer.write("----------------------\n");

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving file!");
        }
    }
}