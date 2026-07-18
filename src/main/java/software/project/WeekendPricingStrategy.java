package software.project;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekendPricingStrategy
        implements RentalPricingStrategy {

    private static final double WEEKEND_MULTIPLIER = 1.20;

    @Override
    public double calculateTotalCost(
            LocalDate startDate,
            LocalDate endDate,
            double pricePerDay) {

        double totalCost = 0;

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {

            if (currentDate.getDayOfWeek() == DayOfWeek.FRIDAY
                    || currentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {

                totalCost +=
                        pricePerDay * WEEKEND_MULTIPLIER;

            } else {

                totalCost += pricePerDay;
            }

            currentDate = currentDate.plusDays(1);
        }

        return totalCost;
    }
}