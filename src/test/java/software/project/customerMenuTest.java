package software.project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

class customerMenuTest {

    private customerMenu menu;

    @BeforeEach
    void setUp() {
        menu = new customerMenu();
    }


    @Test
    void parseLicensesShouldReturnAllLicenses() {
        ArrayList<String> result =
                menu.parseLicenses("[Car, Motorcycle, Bus]");

        assertEquals(3, result.size());
        assertEquals("Car", result.get(0));
        assertEquals("Motorcycle", result.get(1));
        assertEquals("Bus", result.get(2));
    }

    @Test
    void parseEmptyLicensesShouldReturnEmptyList() {
        ArrayList<String> result = menu.parseLicenses("[]");

        assertTrue(result.isEmpty());
    }

    @Test
    void parseSingleLicenseShouldReturnOneItem() {
        ArrayList<String> result = menu.parseLicenses("[Truck]");

        assertEquals(1, result.size());
        assertEquals("Truck", result.get(0));
    }

}