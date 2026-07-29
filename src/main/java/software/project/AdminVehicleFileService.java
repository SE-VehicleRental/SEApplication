package software.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminVehicleFileService {

	private final String vehiclesFile;

	public AdminVehicleFileService() {
		this("AddingVEHICLE.txt");
	}

	public AdminVehicleFileService(String vehiclesFile) {
		this.vehiclesFile = vehiclesFile;
	}

	public int generateVehicleID() {

		int lastID = 0;

		try (
			BufferedReader br = new BufferedReader(new FileReader(vehiclesFile))){

			String line;

			while ((line = br.readLine()) != null) {

				if (line.trim().isEmpty())
					continue;

				String[] data = line.split(",");

				lastID = Integer.parseInt(data[0]);
			}

			

		} catch (IOException e) {
			e.printStackTrace();

		}

		return lastID + 1;
	}

	public void saveVehicle(String vehicleType, String type, String model, String color, int year, String plateNumber,
			double price) {

		try {
			int id = generateVehicleID();
			FileWriter fw = new FileWriter(vehiclesFile, true);
			PrintWriter pw = new PrintWriter(fw);

			pw.println(id + "," + vehicleType + "," + type + "," + model + "," + color + "," + plateNumber + "," + year
					+ "," + price);
			pw.close();
		} catch (IOException e) {
			System.out.println("Error saving file: " + e.getMessage());
		}
	}

	public boolean plateExists(String plateNumber) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(vehiclesFile));

			String line;

			while ((line = br.readLine()) != null) {

				String[] data = line.split(",");

				if (data.length > 5 && data[5].equals(plateNumber)) {
					br.close();
					return true;
				}
			}

			

		} catch (IOException e) {
			e.printStackTrace();

		}

		return false;
	}

	public boolean displayVehiclesFromFile(String fileName, String vehicleType) {

		boolean found = false;

		try {

			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String line;

			while ((line = br.readLine()) != null) {

				String[] data = line.split(",");

				if (data[1].equalsIgnoreCase(vehicleType)) {

					found = true;

					System.out.println("----------------------------------");
					System.out.println("ID: " + data[0]);
					System.out.println("Type: " + data[2]);
					System.out.println("Model: " + data[3]);
					System.out.println("Color: " + data[4]);
					System.out.println("Plate Number: " + data[5]);
					System.out.println("Year: " + data[6]);
					System.out.println("Price: " + data[7]);
					System.out.println("----------------------------------");
				}
			}

			br.close();

			return found;

		} catch (IOException e) {

			return false;
		}
	}

	public boolean deleteVehicleFromFile(String fileName, int id) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));

			
				PrintWriter pw = new PrintWriter(new FileWriter("temp.txt"))) {

			String line;
			boolean deleted = false;

			while ((line = br.readLine()) != null) {

				String[] data = line.split(",");

				if (Integer.parseInt(data[0]) != id) {

					pw.println(line);

				} else {

					deleted = true;

				}

			}

			 return deleted;


		} catch (IOException e) {
			 e.printStackTrace();}

			return false;
		
	}

	public boolean editVehicleFromFile(String fileName, int id, int choice, String newValue) {

		 try (BufferedReader br = new BufferedReader(new FileReader(fileName));
		         PrintWriter pw = new PrintWriter(new FileWriter("temp.txt"))) {

			String line;
			boolean edited = false;

			while ((line = br.readLine()) != null) {

				String[] data = line.split(",");

				if (Integer.parseInt(data[0]) == id) {

					switch (choice) {

					case 1:
						data[2] = newValue;
						break;

					case 2:
						data[3] = newValue;
						break;

					case 3:
						data[4] = newValue;
						break;

					case 4:
						data[6] = newValue;
						break;

					case 5:
						data[7] = newValue;
						break;

					default:
						
						return false;
					}

					edited = true;
				}

				pw.println(String.join(",", data));
			}


			File oldFile = new File(fileName);
			File newFile = new File("temp.txt");

			oldFile.delete();
			newFile.renameTo(oldFile);

			return edited;

		} catch (IOException e) {

			return false;
		}
	}

	public boolean checkVehicleExists(String fileName, int id, String vehicleType) {

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

			String line;

			while ((line = reader.readLine()) != null) {

				String[] data = line.split(",");

				if (data.length < 2) {
					continue;
				}

				if (Integer.parseInt(data[0]) == id && data[1].equalsIgnoreCase(vehicleType)) {

					return true;
				}
			}

		} catch (IOException | NumberFormatException e) {
			return false;
		}

		return false;
	}
}
