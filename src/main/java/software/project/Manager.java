package software.project;

import java.io.File;
import java.util.Scanner;

public class Manager {

    private final Scanner input;
    private final File adminFile;

    private final Runnable adminMenuAction;
    private final Runnable customerMenuAction;

    public Manager() {
        this(
                new File("admin.txt"),
                new Scanner(System.in),
                () -> {
                    adminmenu menu = new adminmenu();
                    menu.showmenu();
                },
                () -> {
                    customerMenu menu = new customerMenu();
                    menu.showMenu();
                }
        );
    }

    public Manager(File adminFile) {
        this(
                adminFile,
                new Scanner(System.in),
                () -> {
                    adminmenu menu = new adminmenu();
                    menu.showmenu();
                },
                () -> {
                    customerMenu menu = new customerMenu();
                    menu.showMenu();
                }
        );
    }

    Manager(
            File adminFile,
            Scanner input,
            Runnable adminMenuAction,
            Runnable customerMenuAction) {

        this.adminFile = adminFile;
        this.input = input;
        this.adminMenuAction = adminMenuAction;
        this.customerMenuAction = customerMenuAction;
    }

    public void start() {

        while (true) {
            System.out.println(
                    "please choose your role, "
                            + "\n 1- Admin "
                            + "\n 2- Customer"
            );

            int choice = readInt();

            if (choice == 1) {

                boolean loginSucceeded = adminLogin();

                if (loginSucceeded) {
                    return;
                }

            } else if (choice == 2) {

                customerMenuAction.run();
                return;

            } else {
                System.out.println("Invalid choice");
            }
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

    private boolean adminLogin() {

        System.out.print("Enter username: ");
        String username = input.nextLine().trim();

        System.out.print("Enter password: ");
        String password = input.nextLine().trim();

        if (checkLogin(username, password)) {
            System.out.println("Login successful");

            adminMenuAction.run();
            return true;
        }

        System.out.println("Wrong username or password");
        return false;
    }

    private int readInt() {

        while (true) {
            try {
                return Integer.parseInt(
                        input.nextLine().trim()
                );

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input! Please enter a number."
                );
            }
        }
    }
}