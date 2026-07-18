package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeekendPricingStrategyTest {

    private WeekendPricingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new WeekendPricingStrategy();
    }

    @Test
    void fridayAndSaturdayShouldHaveWeekendIncrease() {
        LocalDate startDate = LocalDate.of(2026, 7, 9);
        LocalDate endDate = LocalDate.of(2026, 7, 12);

        double totalCost =
                strategy.calculateTotalCost(
                        startDate,
                        endDate,
                        100.0
                );

        assertEquals(
                440.0,
                totalCost,
                0.001
        );
    }

    @Test
    void weekdaysShouldUseNormalPrice() {
        LocalDate startDate = LocalDate.of(2026, 7, 6);
        LocalDate endDate = LocalDate.of(2026, 7, 8);

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
    void singleFridayShouldApplyWeekendIncrease() {
        LocalDate friday = LocalDate.of(2026, 7, 10);

        double totalCost =
                strategy.calculateTotalCost(
                        friday,
                        friday,
                        100.0
                );

        assertEquals(
                120.0,
                totalCost,
                0.001
        );
    }

    @Test
    void singleSaturdayShouldApplyWeekendIncrease() {
        LocalDate saturday = LocalDate.of(2026, 7, 11);

        double totalCost =
                strategy.calculateTotalCost(
                        saturday,
                        saturday,
                        100.0
                );

        assertEquals(
                120.0,
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