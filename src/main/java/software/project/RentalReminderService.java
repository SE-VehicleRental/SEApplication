package software.project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalReminderService {


    private EmailService emailService;


    public RentalReminderService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void test() {
        System.out.println("Working");
    }

    public void checkRental(
            String email,
            String customerName,
            String endDate) {


        LocalDate today = LocalDate.now();

        LocalDate rentalEndDate =
                LocalDate.parse(endDate);



        long daysLeft =
                ChronoUnit.DAYS.between(today, rentalEndDate);




        if(daysLeft == 2) {
            String subject =
                    "Rental Reminder";

            String body =
                    "Dear " + customerName +
                    "\n\nYour rental period will end after 2 days."
                    + "\nPlease prepare to return the vehicle."
                    + "\n\nThank you";


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
                    "Dear " + customerName +
                    "\n\nToday is the last day of your rental."
                    + "\nPlease return the vehicle today."
                    + "\n\nThank you";


            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

        }


    }

}