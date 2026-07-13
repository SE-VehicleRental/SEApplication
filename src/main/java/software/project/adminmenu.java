package software.project;
import java.util.Scanner;
import java.io.*;

public class adminmenu {
	Scanner input = new Scanner(System.in);
	
	public  int readInt() {

	    while (true) {

	        if (input.hasNextInt()) {
	            return input.nextInt();
	        } else {
	            System.out.println("Invalid input! Please enter a number.");
	            input.next();
	        }

	    }
	}
	
	public  String readType() {
	    while (true) {
	        System.out.print("Enter type: ");
	        String type = input.next();

	        if (type.matches("[A-Za-z ]+")) {
	            return type;
	        }

	        System.out.println("Invalid type! Please enter letters only.");
	    }
	}
	
	
	
	public  String readModel() {
	    while (true) {
	        System.out.print("Enter model: ");
	        String model = input.next();

	        if (model.matches("[A-Za-z0-9]+")) {
	            return model;
	        }
	        System.out.println("Invalid model!");
	    }
	}
	
	
	
	public  String readColor() {
	    while (true) {

	        System.out.print("Enter color: ");
	        String color = input.next();

	        if (color.matches("[A-Za-z]+")) {
	            return color;
	        }
	        System.out.println("Invalid color! Please enter letters only.");
	    }
	}
	
	
	
	public  int readYear() {

	    while (true) {

	        System.out.print("Enter year: ");

	        if (input.hasNextInt()) {

	            int year = input.nextInt();

	            if (year >= 1950 && year <= 2026) {
	                return year;
	            }

	            System.out.println("Invalid year!");

	        } else {

	            System.out.println("Year must be a number.");
	            input.next();

	        }

	    }
	}
	
	public  double readPrice() {

	    while (true) {

	        System.out.print("Enter price per day: ");

	        if (input.hasNextDouble()) {

	            double price = input.nextDouble();

	            if (price > 0) {
	                return price;
	            }

	            System.out.println("Price must be greater than zero.");

	        } else {

	            System.out.println("Price must be a number.");
	            input.next();

	        }

	    }
	}
	
	
	public  void showmenu() {
		while (true) {
		System.out.println("What do you want to do? \n 1-Add \n 2-Delete \n 3-Edit \n 4-Back");                                                                 
		int choice = readInt();                                                  
		                                                                               
		switch (choice) {

        case 1:
            adminadd();
            return;

        case 2:
            admindelete();
            return;

        case 3:
            adminedit();
            return;
            
        case 4:
        	Manager m=new Manager();
        	m.start();
            return;
            

        default:
            System.out.println("Invalid choice! Please enter a number between 1 and 3.");
    }                                        
        }
	}

	

	public  void adminadd() {
	    while (true) {
	        System.out.println("ADD VEHICLE");
	        System.out.println("1-Car");
	        System.out.println("2-Motorcycle");
	        System.out.println("3-Truck");
	        System.out.println("4-Bus");
	        System.out.println("5-Back");

	        int choice = readInt();
	        switch (choice) {

	            case 1:
	                enterVehicleData("Car");
	                break;

	            case 2:
	                enterVehicleData("Motorcycle");
	                break;

	            case 3:
	                enterVehicleData("Truck");
	                break;

	            case 4:
	                enterVehicleData("Bus");
	                break;

	            case 5:
	               showmenu();
	               return;

	            default:
	                System.out.println("Invalid choice");
	        }



	        while (true) {

	            System.out.println("\nDo you want to add another vehicle?");
	            System.out.println("1- Yes");
	            System.out.println("2- No (Back)");

	            int again = readInt();

	            if (again == 1) {
	                break;
	            }

	            else if (again == 2) {
	                showmenu();
	                return;
	            }

	            else {
	                System.out.println("Invalid choice! Please enter 1 or 2.");
	            }
	        } 
	        }
	    }
	
	
	public  void enterVehicleData(String vehicleType) {

		System.out.println("=== Add " + vehicleType + " ===");

		String type = readType();
		String model = readModel();
		int year = readYear();
		String color = readColor();
		String plateNumber = readPlateNumber();
		double price = readPrice();

		saveVehicle(vehicleType, type, model, color, year, plateNumber, price);

		System.out.println(vehicleType + " added successfully!");
	}
	
	
	public  int generateVehicleID() {

	    int lastID = 0;

	    try {
	        BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

	        String line;

	        while ((line = br.readLine()) != null) {

	            if (line.trim().isEmpty())
	                continue;

	            String[] data = line.split(",");

	            lastID = Integer.parseInt(data[0]);
	        }

	        br.close();

	    } catch (IOException e) {

	    }

	    return lastID + 1;
	}
	

	public  void saveVehicle(String vehicleType, String type, String model,
	        String color, int year, String plateNumber, double price) {

	    try {
	    	 int id = generateVehicleID();
	        FileWriter fw = new FileWriter("AddingVEHICLE.txt", true);
	        PrintWriter pw = new PrintWriter(fw);

	        pw.println(id + "," + vehicleType + "," + type + "," +
	                model + "," + color + "," + plateNumber + "," + year + "," + price);
	        pw.close();
	    } catch (IOException e) {
	        System.out.println("Error saving file: " + e.getMessage());
	    }
	}
	
	
	public  void admindelete() {

	    System.out.println("DELETE VEHICLE");
	    System.out.println("1-Car");
	    System.out.println("2-Motorcycle");
	    System.out.println("3-Truck");
	    System.out.println("4-Bus");
	    System.out.println("5-Back");

	    int choice = readInt();

	    String vehicleType = "";

	    switch (choice) {
	        case 1:
	            vehicleType = "Car";
	            break;

	        case 2:
	            vehicleType = "Motorcycle";
	            break;

	        case 3:
	            vehicleType = "Truck";
	            break;

	        case 4:
	            vehicleType = "Bus";
	            break;

	        case 5:
	            showmenu();
	            return;

	        default:
	            System.out.println("Invalid choice.");
	            return;
	    }

	    displayVehicles(vehicleType);

	}
	
	public  void adminedit() {

	    while (true) {

	        System.out.println("EDIT VEHICLE");
	        System.out.println("1-Car");
	        System.out.println("2-Motorcycle");
	        System.out.println("3-Truck");
	        System.out.println("4-Bus");
	        System.out.println("5-Back");

	        int choice = readInt();

	        String vehicleType = "";

	        switch (choice) {

	            case 1:
	                vehicleType = "Car";
	                break;

	            case 2:
	                vehicleType = "Motorcycle";
	                break;

	            case 3:
	                vehicleType = "Truck";
	                break;

	            case 4:
	                vehicleType = "Bus";
	                break;

	            case 5:
	                showmenu();
	                return;

	            default:
	                System.out.println("Invalid choice! Please enter a number between 1 and 5.");
	                continue;
	        }

	        displayVehiclesForEdit(vehicleType);
	        return;
	    }
	}
	
	public  void displayVehiclesForEdit(String vehicleType) {

	    try {

	        BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

	        String line;
	        boolean found = false;

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

	        if (!found) {

	            System.out.println("No " + vehicleType + " found.");

	            System.out.println("\nDo you want to choose another vehicle type?");
	            System.out.println("1- Yes");
	            System.out.println("2- No (Back)");

	            int again = readInt();

	            if (again == 1) {
	                adminedit();
	            } else {
	                showmenu();
	            }

	            return;
	        }

	        int id;

	        while (true) {

	            System.out.print("Enter Vehicle ID to edit: ");
	            id = readInt();

	            boolean exists = false;

	            BufferedReader check = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

	            String line2;

	            while ((line2 = check.readLine()) != null) {

	                String[] data = line2.split(",");

	                if (Integer.parseInt(data[0]) == id &&
	                    data[1].equalsIgnoreCase(vehicleType)) {

	                    exists = true;
	                    break;
	                }
	            }

	            check.close();

	            if (exists) {
	                break;
	            }

	            System.out.println("Vehicle ID not found. Please enter a valid ID.");

	        }

	        editVehicle(id);

	    } catch (IOException e) {

	        System.out.println("Error reading file.");

	    }

	}
	public  void editVehicle(int id) {

		System.out.println("\nWhat do you want to edit?");
		System.out.println("1-Type");
		System.out.println("2-Model");
		System.out.println("3-Color");
		System.out.println("4-Year");
		System.out.println("5-Price");

		int choice;

		while (true) {

		    choice = readInt();

		    if (choice >= 1 && choice <= 5) {
		        break;
		    }

		    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
		}

		String newValue = "";

		switch (choice) {

		case 1:
		    newValue = readType();
		    break;

		case 2:
		    newValue = readModel();
		    break;

		case 3:
		    newValue = readColor();
		    break;

		case 4:
		    newValue = String.valueOf(readYear());
		    break;

		case 5:
		    newValue = String.valueOf(readPrice());
		    break;

		}

	    try {

	        BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

	        FileWriter fw = new FileWriter("temp.txt");
	        PrintWriter pw = new PrintWriter(fw);

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
	                        data[5] = newValue;
	                        break;

	                    case 5:
	                        data[6] = newValue;
	                        break;

	                    default:
	                        System.out.println("Invalid choice.");
	                        br.close();
	                        pw.close();
	                        return;
	                }

	                edited = true;
	            }

	            pw.println(String.join(",", data));

	        }

	        br.close();
	        pw.close();

	        File oldFile = new File("AddingVEHICLE.txt");
	        File newFile = new File("temp.txt");

	        oldFile.delete();
	        newFile.renameTo(oldFile);

	        if (edited)
	            System.out.println("Vehicle updated successfully.");
	        else
	            System.out.println("Vehicle ID not found.");

	        System.out.println("\nDo you want to edit another vehicle?");
	        System.out.println("1- Yes");
	        System.out.println("2- No (Back)");

	        int again;

	        while (true) {

	            again = readInt();

	            if (again == 1) {
	                adminedit();
	                return;
	            }

	            else if (again == 2) {
	                showmenu();
	                return;
	            }

	            else {
	                System.out.println("Invalid choice! Please enter 1 or 2.");
	            }

	        }

	    } catch (IOException e) {

	        System.out.println("Error editing vehicle.");

	    }

	}
	
	
	public  void displayVehicles(String vehicleType) {

	    try {

	        BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

	        String line;
	        boolean found = false;

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

	        if (!found) {

	            System.out.println("No " + vehicleType + " found.");

	            System.out.println("\nDo you want to choose another vehicle type?");
	            System.out.println("1- Yes");
	            System.out.println("2- No (Back)");

	            int again = readInt();

	            if (again == 1) {
	            	admindelete();
	            } else {
	                showmenu();
	            }

	            return;
	        }

	        int id;

	        while (true) {

	            System.out.print("Enter Vehicle ID to delete: ");
	            id = readInt();

	            boolean exists = false;

	            BufferedReader check = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

	            String line2;

	            while ((line2 = check.readLine()) != null) {

	                String[] data = line2.split(",");

	                if (Integer.parseInt(data[0]) == id &&
	                    data[1].equalsIgnoreCase(vehicleType)) {

	                    exists = true;
	                    break;
	                }
	            }

	            check.close();

	            if (exists) {
	                break;
	            }

	            System.out.println("Vehicle ID not found. Please enter a valid ID.");

	        }

	        deleteVehicle(id);

	    } catch (IOException e) {

	        System.out.println("Error reading file.");

	    }

	}
	
	public  void deleteVehicle(int id) {

	    try {

	        BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

	        FileWriter fw = new FileWriter("temp.txt");

	        PrintWriter pw = new PrintWriter(fw);

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

	        br.close();
	        pw.close();

	        File oldFile = new File("AddingVEHICLE.txt");
	        File newFile = new File("temp.txt");

	        oldFile.delete();
	        newFile.renameTo(oldFile);

	        if (deleted)
	            System.out.println("Vehicle deleted successfully.");
	        else
	            System.out.println("Vehicle ID not found.");

	        System.out.println("\nDo you want to delete another vehicle?");
	        System.out.println("1- Yes");
	        System.out.println("2- No (Back)");

	        while (true) {

	            int again = readInt();

	            if (again == 1) {
	                admindelete();
	                return;
	            }

	            else if (again == 2) {
	                showmenu();
	                return;
	            }

	            else {
	                System.out.println("Invalid choice! Please enter 1 or 2.");
	            }

	        }

	    } catch (IOException e) {

	        System.out.println("Error deleting vehicle.");

	    }

	}
	
	public String readPlateNumber() {
	    while (true) {
	        System.out.print("Enter plate number (6 digits): ");
	        String plate = input.next();

	        if (!plate.matches("\\d{6}")) {
	            System.out.println("Invalid plate number! Must be exactly 6 digits.");
	            continue;
	        }
	        if (plateExists(plate)) {
	            System.out.println("Plate number already exists! Try another one.");
	            continue;
	        }
	        return plate;
	    }
	}
	
	
	public boolean isValidPlateNumber(String plate) {
	    return plate.matches("\\d{6}");
	}
	
	
	public boolean plateExists(String plateNumber) {

	    try {
	        BufferedReader br = new BufferedReader(new FileReader("AddingVEHICLE.txt"));

	        String line;

	        while ((line = br.readLine()) != null) {

	            String[] data = line.split(",");

	            if (data.length > 5 && data[5].equals(plateNumber)) {
	                br.close();
	                return true;
	            }
	        }

	        br.close();

	    } catch (IOException e) {
	        System.out.println("Error checking plate number.");
	    }

	    return false;
	}
	
}
