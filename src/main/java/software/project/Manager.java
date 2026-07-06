package software.project;
import java.io.File;
import java.util.Scanner;

public class Manager {
	Scanner input = new Scanner(System.in);
	
    public void start() {
        System.out.println("please choose your role, \n 1- Admin \n 2- Customer  ");
        int choice = input.nextInt();
        
        if (choice == 1) {
            adminLogin();
        } 
        else if (choice == 2) {
        	customerMenu cm = new customerMenu();
            cm.showMenu();
        } 
        else {
            System.out.println("Invalid choice");
            start();
}}

	
	public boolean checkLogin(String username, String password) {

	    try {
	        File file = new File("admin.txt");
	        Scanner reader = new Scanner(file);

	        while (reader.hasNextLine()) {

	            String line = reader.nextLine();
	            String[] data = line.split(",");

	            String fileUser = data[0];
	            String filePass = data[1];

	            if (username.equals(fileUser) && password.equals(filePass)) {
	                reader.close();
	                return true;
	            }
	        }

	        reader.close();

	    } catch (Exception e) {
	        System.out.println("Error reading file: " + e.getMessage());
	    }

	    return false;
	}

	private void adminLogin() {
		// TODO Auto-generated method stub
		System.out.print("Enter username: ");
        String username = input.next();

        System.out.print("Enter password: ");
        String password = input.next();
        if (checkLogin(username, password)) {
            System.out.println("Login successful");
            adminmenu menu = new adminmenu();
            menu.showmenu();
        } 
        else {
            System.out.println("Wrong username or password");
            start();
        }
	}



}
