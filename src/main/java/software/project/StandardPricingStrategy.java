package software.project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StandardPricingStrategy
        implements RentalPricingStrategy {

    @Override
    public double calculateTotalCost(
            LocalDate startDate,
            LocalDate endDate,
            double pricePerDay) {

        long rentalDays =
                ChronoUnit.DAYS.between(
                        startDate,
                        endDate
                ) + 1;

        return rentalDays * pricePerDay;
    }
}