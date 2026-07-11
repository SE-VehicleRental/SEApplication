package software.project;

import java.io.File;
import java.util.Scanner;

public class Manager {

    Scanner input = new Scanner(System.in);

    private File adminFile;

    public Manager() {
        this.adminFile = new File("admin.txt");
    }

    public Manager(File adminFile) {
        this.adminFile = adminFile;
    }

    public void start() {
        System.out.println("please choose your role, \n 1- Admin \n 2- Customer");

        int choice = input.nextInt();

        if (choice == 1) {
            adminLogin();
        } else if (choice == 2) {
            customerMenu cm = new customerMenu();
            cm.showMenu();
        } else {
            System.out.println("Invalid choice");
            start();
        }
    }

    public boolean checkLogin(String username, String password) {

        try (Scanner reader = new Scanner(adminFile)) {

            while (reader.hasNextLine()) {

                String line = reader.nextLine();
                String[] data = line.split(",");

                if (data.length < 2) {
                    continue;
                }

                String fileUser = data[0];
                String filePass = data[1];

                if (username.equals(fileUser)
                        && password.equals(filePass)) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "Error reading file: " + e.getMessage()
            );
        }

        return false;
    }

    private void adminLogin() {

        System.out.print("Enter username: ");
        String username = input.next();

        System.out.print("Enter password: ");
        String password = input.next();

        if (checkLogin(username, password)) {
            System.out.println("Login successful");

            adminmenu menu = new adminmenu();
            menu.showmenu();

        } else {
            System.out.println("Wrong username or password");
            start();
        }
    }
}