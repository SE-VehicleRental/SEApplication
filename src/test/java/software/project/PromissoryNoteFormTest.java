package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PromissoryNoteFormTest {

   
    @Test
    void testRunnable() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Runnable r = () -> executed.set(true);
        r.run();

        assertTrue(executed.get());
    }
    
    @Test
    void testBuildPromissoryTextContainsCustomerName() {

        String text = PromissoryNoteForm.buildPromissoryText(
                "Wijdan",
                "123456789",
                "0599999999",
                "Car",
                "Toyota",
                "Corolla",
                "100",
                "2026-07-13",
                "2026-07-15",
                "200"
        );

        assertTrue(text.contains("Wijdan"));
    }
    
    
    @Test
    void testBuildPromissoryTextContainsVehicleType() {

        String text = PromissoryNoteForm.buildPromissoryText(
                "Wijdan",
                "123456789",
                "0599999999",
                "Car",
                "Toyota",
                "Corolla",
                "100",
                "2026-07-13",
                "2026-07-15",
                "200"
        );

        assertTrue(text.contains("Vehicle Type : Car"));
    }
    
    
    
    @Test
    void testBuildPromissoryTextContainsPricePerDay() {

        String text = PromissoryNoteForm.buildPromissoryText(
                "Wijdan",
                "123456789",
                "0599999999",
                "Car",
                "Toyota",
                "Corolla",
                "100",
                "2026-07-13",
                "2026-07-15",
                "200"
        );

        assertTrue(text.contains("Rental Price Per Day : 100 ₪"));
    }
    
    @Test
    void testGuaranteeAmountInText() {

        String text = PromissoryNoteForm.buildPromissoryText(
                "Wijdan",
                "123456789",
                "0599999999",
                "Car",
                "Toyota",
                "Corolla",
                "100",
                "2026-07-13",
                "2026-07-15",
                "200"
        );

        assertTrue(text.contains("Guarantee Amount : 5000 JD"));
    }
    
    
}