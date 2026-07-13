package software.project;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

public class PromissoryNoteFormTest {
	
	 @Test
	    void testCreateForm() {
	        assertDoesNotThrow(() -> {
	            new PromissoryNoteForm(
	                    "Wijdan",
	                    "123456789",
	                    "0599999999",
	                    "Car",
	                    "Toyota",
	                    "123456",
	                    "100",
	                    "2026-07-13",
	                    "2026-07-15",
	                    "200",
	                    null
	            );
	        });
	    }
	 

	    @Test
	    void testRunnable() {
	        AtomicBoolean executed = new AtomicBoolean(false);
	        Runnable r = () -> executed.set(true);
	        r.run();
	        assertTrue(executed.get());

	    }
}
