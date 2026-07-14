package software.project;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


public class LibraryFineProcessorTest {

    @Test
    void testStudentFineEmailIsSent() {
       
        EmailService mockEmailService = mock(EmailService.class);
        
        FineCalculator calculator = new FineCalculator();

        LibraryFineProcessor processor = new LibraryFineProcessor(mockEmailService, calculator);

        FineCalculationStrategy studentStrategy = new StudentFineStrategy();

       
        processor.processFine("hayasam@najah.edu", studentStrategy, 5);

       
        verify(mockEmailService, times(1))
                .sendEmail(eq("hayasam@najah.edu"), eq("Library Fine Notice"), contains("Your fine is"));
    }

   
}
