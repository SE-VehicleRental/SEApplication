package software.project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class RentalNotificationProcessor {

    private final EmailService emailService;


    public RentalNotificationProcessor(EmailService emailService) {
        this.emailService = emailService;
    }


    public void sendRentalConfirmation(
            String email,
            String customerName,
            String vehicle) {


        String subject = "Vehicle Rental Confirmation";


        String body =
                "Dear " + customerName + ",\n\n" +
                "Your rental has been confirmed.\n" +
                "Vehicle: " + vehicle + "\n\n" +
                "Thank you.";


        emailService.sendEmail(
                email,
                subject,
                body
        );
    }




    public void checkRentalReminder(
            String email,
            String customerName,
            String endDate) {
   
        LocalDate today = LocalDate.now();
        LocalDate rentalEndDate =
                LocalDate.parse(endDate);

        long daysLeft =
                ChronoUnit.DAYS.between(
                        today,
                        rentalEndDate
                );


        if(daysLeft == 2) {
            String subject =
                    "Rental Ending Soon";

            String body =
                    "Dear " + customerName + ",\n\n" +
                    "Your rental period will end after 2 days.\n" +
                    "Please prepare to return the vehicle.\n\n" +
                    "Thank you.";


            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

        }

        else if(daysLeft == 0) {


            String subject =
                    "Vehicle Return Reminder";


            String body =
                    "Dear " + customerName + ",\n\n" +
                    "Today is the last day of your rental.\n" +
                    "Please return the vehicle today.\n\n" +
                    "Thank you.";

            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

        }

    }

}