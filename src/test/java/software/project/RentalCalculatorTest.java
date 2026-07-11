package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RentalCalculatorTest {

    private RentalCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new RentalCalculator();
    }

    @Test
    void sameStartAndEndDateShouldCountAsOneDay() {
        LocalDate date = LocalDate.of(2026, 7, 11);

        assertEquals(
                1,
                calculator.calculateRentalDays(date, date)
        );
    }

    @Test
    void rentalDaysShouldIncludeBothDates() {
        LocalDate start = LocalDate.of(2026, 7, 10);
        LocalDate end = LocalDate.of(2026, 7, 12);

        assertEquals(
                3,
                calculator.calculateRentalDays(start, end)
        );
    }

    @Test
    void endDateBeforeStartDateShouldThrowException() {
        LocalDate start = LocalDate.of(2026, 7, 12);
        LocalDate end = LocalDate.of(2026, 7, 10);

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateRentalDays(start, end)
        );
    }

    @Test
    void nullStartDateShouldThrowException() {
        LocalDate end = LocalDate.of(2026, 7, 10);

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateRentalDays(null, end)
        );
    }

    @Test
    void nullEndDateShouldThrowException() {
        LocalDate start = LocalDate.of(2026, 7, 10);

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateRentalDays(start, null)
        );
    }

    @Test
    void totalCostShouldBeCalculatedCorrectly() {
        assertEquals(
                150.0,
                calculator.calculateTotalCost(3, 50.0),
                0.001
        );
    }

    @Test
    void decimalPriceShouldBeCalculatedCorrectly() {
        assertEquals(
                75.5,
                calculator.calculateTotalCost(1, 75.5),
                0.001
        );
    }

    @Test
    void zeroRentalDaysShouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateTotalCost(0, 50.0)
        );
    }

    @Test
    void negativeRentalDaysShouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateTotalCost(-1, 50.0)
        );
    }

    @Test
    void negativePriceShouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateTotalCost(3, -50.0)
        );
    }

    @Test
    void zeroPriceShouldReturnZero() {
        assertEquals(
                0.0,
                calculator.calculateTotalCost(3, 0.0),
                0.001
        );
    }
}