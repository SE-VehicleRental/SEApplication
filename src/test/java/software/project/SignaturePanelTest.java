package software.project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SignaturePanelTest {

    @Test
    void testDrawSignature() {
        SignaturePanel panel = new SignaturePanel();

        assertTrue(panel.isEmpty());

        panel.setPreviousPoint(10, 20);
        panel.drawSignature(50, 60);

        assertFalse(panel.isEmpty());
    }
}