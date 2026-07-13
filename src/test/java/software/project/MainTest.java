package software.project;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class MainTest {
	

    @Test
    void testMainPrintsWelcomeMessage() {

        System.setIn(new ByteArrayInputStream("2\n".getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        try {
            Main.main(new String[]{});
        } catch (Exception e) {
        }

        assertTrue(
            output.toString().contains(
                "Welcome to Nablus company for rent vehicles"
            )
        );
    }

}
