package software.project;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class adminmenu {
	Scanner input = new Scanner(System.in);
	
	public void showmenu() {
		System.out.println("What do you want to do? \n 1-Add \n 2-Delete \n 3-Edit \n");                                                                 
		     int choice = input.nextInt();                                                    
		                                                                               
		     if (choice == 1) {                                                               
		               adminadd();                                                        
		     }                                                                                
		     else if (choice == 2) {                                                          
		               admindelete();                                                      
		     }  
		     else if (choice == 3) {                                                          
                 
		     }  
		     
		     else {                                                                           
		         System.out.println("Invalid choice");                                        
        }

}

	private void adminadd() {
	    while (true) {
	        System.out.println("ADD VEHICLE");
	        System.out.println("1-Car");
	        System.out.println("2-Motorcycle");
	        System.out.println("3-Truck");
	        System.out.println("4-Bus");
	        System.out.println("5-Back");

	        int choice = input.nextInt();
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

	            default:
	                System.out.println("Invalid choice");
	        }

	        System.out.println("\nDo you want to add another vehicle?");
	        System.out.println("1- Yes");
	        System.out.println("2- No (Back)");

	        int again = input.nextInt();

	        if (again == 2) {
	            return; 
	        }
	    }
	}
	
	private void enterVehicleData(String vehicleType) {

	    System.out.println("=== Add " + vehicleType + " ===");
	    System.out.print("Enter type: ");
	    
		String type = input.next();

	    System.out.print("Enter model: ");
	    String model = input.next();

	    System.out.print("Enter color: ");
	    String color = input.next();

	    System.out.print("Enter year: ");
	    int year = input.nextInt();

	    System.out.print("Enter price per day: ");
	    double price = input.nextDouble();

	    saveVehicle(vehicleType, type, model, color, year, price);
	    System.out.println(vehicleType + " added successfully!");
	}

	private void saveVehicle(String vehicleType, String type, String model,
	        String color, int year, double price) {

	    try {
	        FileWriter fw = new FileWriter("AddingVEHICLE.txt", true);
	        PrintWriter pw = new PrintWriter(fw);

	        pw.println(vehicleType + "," + type + "," + model + "," +
	                color + "," + year + "," + price);
	        pw.close();
	    } catch (IOException e) {
	        System.out.println("Error saving file: " + e.getMessage());
	    }
	}
	
	
	private void admindelete() {
	
		
	}
}
