package software.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.github.cdimascio.dotenv.Dotenv;


public class RentalReminderChecker {

	private static final Logger LOGGER =
	        Logger.getLogger(RentalReminderChecker.class.getName());
	private static final String RENTALS_FILE = "customer_rentals.txt";


    public void checkAllRentals() {


     


        	try (BufferedReader reader =
        	         new BufferedReader(
        	        		 new FileReader(RENTALS_FILE))) {


            String line;


            String name = "";
            String email = "";
            String endDate = "";



            while((line = reader.readLine()) != null) {



                if(line.startsWith("CustomerName:")) {

                    name =
                    line.replace("CustomerName:", "")
                        .trim();

                }



                else if(line.startsWith("CustomerEmail:")) {


                    email =
                    line.replace("CustomerEmail:", "")
                         .trim();

                }



                else if(line.startsWith("RentalEndDate:")) {


                    endDate =
                    line.replace("RentalEndDate:", "")
                         .trim();

                }
                
                
                else if(line.startsWith("---------------------")) {
                    sendReminder(
                            email,
                            name,
                            endDate
                    );

                    name="";
                    email="";
                    endDate="";
                }
            }


        	}catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to check rental reminders.", e);
        	}

    }

    
    


    private void sendReminder(
            String email,
            String name,
            String endDate){



        Dotenv dotenv =
                Dotenv.load();



        EmailService emailService =
                new EmailService(
                        dotenv.get("EMAIL_USERNAME"),
                        dotenv.get("EMAIL_PASSWORD")
                );



        RentalNotificationProcessor processor =
                new RentalNotificationProcessor(
                        emailService
                );



        processor.checkRentalReminder(
                email,
                name,
                endDate
        );

    }


}