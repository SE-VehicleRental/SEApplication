package software.project;

import java.time.LocalDate;

public interface RentalPricingStrategy {

    double calculateTotalCost(
            LocalDate startDate,
            LocalDate endDate,
            double pricePerDay
    );
}