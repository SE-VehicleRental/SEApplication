package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StandardPricingStrategyTest {

    private StandardPricingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new StandardPricingStrategy();
    }

    @Test
    void threeRentalDaysShouldCalculateCorrectTotal() {
        LocalDate startDate = LocalDate.of(2026, 7, 10);
        LocalDate endDate = LocalDate.of(2026, 7, 12);

        double totalCost =
                strategy.calculateTotalCost(
                        startDate,
                        endDate,
                        100.0
                );

        assertEquals(
                300.0,
                totalCost,
                0.001
        );
    }

    @Test
    void sameStartAndEndDateShouldCountAsOneDay() {
        LocalDate date = LocalDate.of(2026, 7, 10);

        double totalCost =
                strategy.calculateTotalCost(
                        date,
                        date,
                        75.5
                );

        assertEquals(
                75.5,
                totalCost,
                0.001
        );
    }

    @Test
    void zeroPriceShouldReturnZero() {
        LocalDate startDate = LocalDate.of(2026, 7, 10);
        LocalDate endDate = LocalDate.of(2026, 7, 12);

        double totalCost =
                strategy.calculateTotalCost(
                        startDate,
                        endDate,
                        0.0
                );

        assertEquals(
                0.0,
                totalCost,
                0.001
        );
    }
}