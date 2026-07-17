package software.project;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Provides services for managing rental records stored in a text file.
 * This class allows checking vehicle availability, displaying customer
 * rental history, and saving new rental information.
 *
 * @author Jannat Zaidan, Hala Sleibe, Wijdan Risheh
 * @version 1.0
 */

public class RentalFileService {

    private final String rentalsFile;

    /**
     * Creates a RentalFileService object using the default rental file.
     */
    
    public RentalFileService() {
        this("customer_rentals.txt");
    }
    
    /**
     * Creates a RentalFileService object with a specified rental file.
     *
     * @param rentalsFile The name of the rental file to be used.
     */

    public RentalFileService(String rentalsFile) {
        this.rentalsFile = rentalsFile;
    }

    /**
     * Checks whether a vehicle is currently rented.
     * A vehicle is considered rented if its rental end date
     * is today or in the future.
     *
     * @param vehicleId The ID of the vehicle to check.
     * @return true if the vehicle is currently rented, otherwise false.
     */
    
    public boolean isVehicleRented(int vehicleId) {

        try (BufferedReader br =
                     new BufferedReader(new FileReader(rentalsFile))) {

            String line;
            boolean vehicleFound = false;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("VehicleID: ")) {
                    int rentedId =
                            Integer.parseInt(
                                    line.substring(11).trim()
                            );

                    vehicleFound = rentedId == vehicleId;
                }

                if (vehicleFound
                        && line.startsWith("RentalEndDate: ")) {

                    String endDateText =
                            line.substring(15).trim();

                    LocalDate rentalEndDate =
                            LocalDate.parse(endDateText);

                    if (!rentalEndDate.isBefore(LocalDate.now())) {
                        return true;
                    }
                }
            }

        } catch (IOException e) {
            return false;
        }

        return false;
    }
    
    
    /**
     * Checks whether a vehicle is available for the requested rental period.
     * The vehicle is considered unavailable if its existing rental period
     * overlaps with the requested start and end dates.
     *
     * @param vehicleId The ID of the vehicle to check.
     * @param requestedStart The requested rental start date.
     * @param requestedEnd The requested rental end date.
     * @return true if the vehicle is available for the requested period,
     *         otherwise false.
     */
    
    public boolean isVehicleAvailable(
            int vehicleId,
            LocalDate requestedStart,
            LocalDate requestedEnd) {

        try (BufferedReader br =
                     new BufferedReader(new FileReader(rentalsFile))) {

            String line;
            boolean vehicleFound = false;
            LocalDate rentalStart = null;
            LocalDate rentalEnd = null;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("VehicleID: ")) {

                    int rentedId =
                            Integer.parseInt(line.substring(11).trim());

                    vehicleFound = (rentedId == vehicleId);
                    rentalStart = null;
                    rentalEnd = null;
                }

                if (vehicleFound
                        && line.startsWith("RentalStartDate: ")) {

                    rentalStart =
                            LocalDate.parse(line.substring(17).trim());
                }

                if (vehicleFound
                        && line.startsWith("RentalEndDate: ")) {

                    rentalEnd =
                            LocalDate.parse(line.substring(15).trim());
                }

                if (vehicleFound
                        && rentalStart != null
                        && rentalEnd != null) {

                    if (!(requestedEnd.isBefore(rentalStart)
                            || requestedStart.isAfter(rentalEnd))) {

                        return false;
                    }

                    vehicleFound = false;
                }
            }

        } catch (IOException e) {
            return true;
        }

        return true;
    }
    

    /**
     * Displays all rental records for a specific customer.
     *
     * @param customerId The ID of the customer whose rentals will be displayed.
     */
    public void showCustomerRentals(String customerId) {

        System.out.println("\n=== YOUR RENTALS ===");

        boolean hasRental = false;

        try (BufferedReader br =
                     new BufferedReader(new FileReader(rentalsFile))) {

            String line;
            boolean printBlock = false;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("CustomerID: ")) {
                    String id =
                            line.substring(12).trim();

                    if (id.equals(customerId)) {
                        printBlock = true;
                        hasRental = true;
                    } else {
                        printBlock = false;
                    }
                }

                if (printBlock) {
                    System.out.println(line);

                    if (line.startsWith(
                            "---------------------")) {

                        printBlock = false;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "No rentals file found yet."
            );
            return;
        }

        if (!hasRental) {
            System.out.println(
                    "No previous rentals found."
            );
        }
    }
    
    /**
     * Saves a new rental record to the rentals file.
     *
     * @param vehicle An array containing the vehicle information.
     * @param customerId The customer's ID.
     * @param customerName The customer's name.
     * @param customerPhone The customer's phone number.
     * @param customerEmail The customer's email address.
     * @param startDate The rental start date.
     * @param endDate The rental end date.
     * @param rentalDays The total number of rental days.
     * @param totalCost The total rental cost.
     */

    public void saveRentalToFile(
            String[] vehicle,
            String customerId,
            String customerName,
            String customerPhone,
            String customerEmail,
            String startDate,
            String endDate,
            long rentalDays,
            double totalCost) {

        try (FileWriter writer =
                     new FileWriter(rentalsFile, true)) {

            writer.write(
                    "CustomerID: " + customerId + "\n"
            );
            writer.write(
                    "CustomerName: " + customerName + "\n"
            );
            writer.write(
                    "CustomerPhone: " + customerPhone + "\n"
            );
            
            writer.write(
                    "CustomerEmail: " + customerEmail + "\n"
            );

            writer.write(
                    "VehicleID: " + vehicle[0] + "\n"
            );
            writer.write(
                    "VehicleType: " + vehicle[1] + "\n"
            );
            writer.write(
                    "VehicleModel: " + vehicle[2] + "\n"
            );
            writer.write(
                    "PlateNumber: " + vehicle[3] + "\n"
            );
            writer.write(
                    "VehicleColor: " + vehicle[4] + "\n"
            );
            writer.write(
                    "VehicleNumber: " + vehicle[5] + "\n"
            );
            writer.write(
                    "VehicleYear: " + vehicle[6] + "\n"
            );
            writer.write(
                    "PricePerDay: " + vehicle[7] + "\n"
            );

            writer.write(
                    "RentalStartDate: " + startDate + "\n"
            );
            writer.write(
                    "RentalEndDate: " + endDate + "\n"
            );
            writer.write(
                    "RentalDays: " + rentalDays + "\n"
            );
            writer.write(
                    "TotalCost: " + totalCost + "\n"
            );
            writer.write("---------------------\n");

            System.out.println(
                    "Rental saved successfully!"
            );
            Dotenv dotenv = Dotenv.load();

            EmailService emailService =
                    new EmailService(
                            dotenv.get("EMAIL_USERNAME"),
                            dotenv.get("EMAIL_PASSWORD")
                    );

            RentalNotificationProcessor processor =
                    new RentalNotificationProcessor(emailService);

            processor.sendRentalConfirmation(
                    customerEmail,
                    customerName,
                    vehicle[2]
            );

        } catch (IOException e) {
            System.out.println(
                    "Error saving rental!"
            );
        }
    }
 
}