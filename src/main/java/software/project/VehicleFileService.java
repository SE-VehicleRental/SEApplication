package software.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class VehicleFileService {

    private final String vehiclesFile;
    private final RentalFileService rentalFileService;

    public VehicleFileService() {
        this("AddingVEHICLE.txt", new RentalFileService());
    }

    public VehicleFileService(
            String vehiclesFile,
            RentalFileService rentalFileService) {

        this.vehiclesFile = vehiclesFile;
        this.rentalFileService = rentalFileService;
    }

    public ArrayList<String[]> getAvailableVehiclesByType(
            String vehicleType) {

        ArrayList<String[]> vehicles = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(vehiclesFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 8) {
                    continue;
                }

                if (!data[1].equalsIgnoreCase(vehicleType)) {
                    continue;
                }

                int vehicleId = Integer.parseInt(data[0]);

                if (!rentalFileService.isVehicleRented(vehicleId)) {
                    vehicles.add(data);
                }
            }

        } catch (IOException e) {
            return vehicles;
        }

        return vehicles;
    }

    public String[] findVehicleById(
            ArrayList<String[]> vehicles,
            int vehicleId) {

        for (String[] vehicle : vehicles) {
            if (Integer.parseInt(vehicle[0]) == vehicleId) {
                return vehicle;
            }
        }

        return null;
    }
}