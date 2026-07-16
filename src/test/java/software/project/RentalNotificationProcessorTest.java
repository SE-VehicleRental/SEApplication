package software.project;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RentalNotificationProcessorTest {
    @Test
    public void testSendRentalConfirmation() {
        EmailService emailService = mock(EmailService.class);
        RentalNotificationProcessor processor =
                new RentalNotificationProcessor(emailService);

        processor.sendRentalConfirmation(

                "test@test.com",
                "Ali",
                "Toyota Corolla"

        );

        verify(emailService).sendEmail(
                eq("test@test.com"),
                eq("Vehicle Rental Confirmation"),
                anyString()

        );

    }
    
    
    @Test
    public void testReminderTwoDaysBefore() {
        EmailService emailService = mock(EmailService.class);
        RentalNotificationProcessor processor =
                new RentalNotificationProcessor(emailService);

        processor.checkRentalReminder(
                "test@test.com",
                "Ali",
                java.time.LocalDate.now().plusDays(2).toString()
        );

        verify(emailService).sendEmail(
                eq("test@test.com"),
                eq("Rental Ending Soon"),
                anyString()
        );
    }

    
    @Test
    public void testReminderOnEndDate() {

        EmailService emailService = mock(EmailService.class);

        RentalNotificationProcessor processor =
                new RentalNotificationProcessor(emailService);

        processor.checkRentalReminder(
                "test@test.com",
                "Ali",
                java.time.LocalDate.now().toString()
        );

        verify(emailService).sendEmail(
                eq("test@test.com"),
                eq("Vehicle Return Reminder"),
                anyString()
        );
    }
    
    
    @Test
    public void testNoReminder() {

        EmailService emailService = mock(EmailService.class);

        RentalNotificationProcessor processor =
                new RentalNotificationProcessor(emailService);

        processor.checkRentalReminder(
                "test@test.com",
                "Ali",
                java.time.LocalDate.now().plusDays(5).toString()
        );

        verify(emailService, never()).sendEmail(
                anyString(),
                anyString(),
                anyString()
        );
    }
    
    
    
}