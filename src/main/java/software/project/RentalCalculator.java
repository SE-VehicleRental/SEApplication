package software.project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalCalculator {

    public long calculateRentalDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date"
            );
        }

        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}