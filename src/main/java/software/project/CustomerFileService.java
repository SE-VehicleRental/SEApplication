package software.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomerFileService {

    private final String customersFile;

    public CustomerFileService() {
        this("customers.txt");
    }

    public CustomerFileService(String customersFile) {
        this.customersFile = customersFile;
    }

    public boolean isIdUnique(String id) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(customersFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID: ")) {
                    String existingId = line.substring(4).trim();

                    if (existingId.equals(id)) {
                        return false;
                    }
                }
            }

        } catch (IOException e) {
            return true;
        }

        return true;
    }

    public void saveCustomer(CustomerData customer) {
        try (FileWriter writer =
                     new FileWriter(customersFile, true)) {

            writer.write("ID: " + customer.getId() + "\n");
            writer.write("Name: " + customer.getName() + "\n");
            writer.write("Email: " + customer.getEmail() + "\n");
            writer.write("Phone: " + customer.getPhone() + "\n");
            writer.write(
                    "Payment: "
                            + (customer.getPayment() == 1 ? "Cash" : "Visa")
                            + "\n"
            );
            writer.write(
                    "License: " + customer.getLicenses() + "\n"
            );
            writer.write("----------------------\n");

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error saving customer file",
                    e
            );
        }
    }

    public CustomerData getCustomerById(String targetId) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(customersFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("ID: ")) {
                    continue;
                }

                String id = line.substring(4).trim();

                if (!id.equals(targetId)) {
                    continue;
                }

                CustomerData customer = new CustomerData();
                customer.setId(id);

                while ((line = reader.readLine()) != null
                        && !line.startsWith("----------------------")) {

                    if (line.startsWith("Name: ")) {
                        customer.setName(line.substring(6).trim());

                    } else if (line.startsWith("Email: ")) {
                        customer.setEmail(line.substring(7).trim());

                    } else if (line.startsWith("Phone: ")) {
                        customer.setPhone(line.substring(7).trim());

                    } else if (line.startsWith("Payment: ")) {
                        String payment =
                                line.substring(9).trim();

                        customer.setPayment(
                                payment.equalsIgnoreCase("Cash") ? 1 : 2
                        );

                    } else if (line.startsWith("License: ")) {
                        customer.setLicenses(
                                parseLicenses(
                                        line.substring(9).trim()
                                )
                        );
                    }
                }

                return customer;
            }

        } catch (IOException e) {
            return null;
        }

        return null;
    }

    public void updateCustomer(CustomerData updatedCustomer) {
        ArrayList<CustomerData> customers =
                readAllCustomers();

        boolean replaced = false;

        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId()
                    .equals(updatedCustomer.getId())) {

                customers.set(i, updatedCustomer);
                replaced = true;
                break;
            }
        }

        if (!replaced) {
            customers.add(updatedCustomer);
        }

        writeAllCustomers(customers);
    }

    public ArrayList<String> parseLicenses(String licensesText) {
        ArrayList<String> licenses = new ArrayList<>();

        String cleaned =
                licensesText.replace("[", "")
                        .replace("]", "")
                        .trim();

        if (cleaned.isEmpty()) {
            return licenses;
        }

        String[] parts = cleaned.split(",");

        for (String part : parts) {
            licenses.add(part.trim());
        }

        return licenses;
    }

    private ArrayList<CustomerData> readAllCustomers() {
        ArrayList<CustomerData> customers =
                new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(customersFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("ID: ")) {
                    continue;
                }

                CustomerData customer = new CustomerData();
                customer.setId(line.substring(4).trim());

                while ((line = reader.readLine()) != null
                        && !line.startsWith("----------------------")) {

                    if (line.startsWith("Name: ")) {
                        customer.setName(line.substring(6).trim());

                    } else if (line.startsWith("Email: ")) {
                        customer.setEmail(line.substring(7).trim());

                    } else if (line.startsWith("Phone: ")) {
                        customer.setPhone(line.substring(7).trim());

                    } else if (line.startsWith("Payment: ")) {
                        String payment =
                                line.substring(9).trim();

                        customer.setPayment(
                                payment.equalsIgnoreCase("Cash") ? 1 : 2
                        );

                    } else if (line.startsWith("License: ")) {
                        customer.setLicenses(
                                parseLicenses(
                                        line.substring(9).trim()
                                )
                        );
                    }
                }

                customers.add(customer);
            }

        } catch (IOException e) {
            return customers;
        }

        return customers;
    }

    private void writeAllCustomers(
            ArrayList<CustomerData> customers) {

        try (FileWriter writer =
                     new FileWriter(customersFile, false)) {

            for (CustomerData customer : customers) {
                writer.write(
                        "ID: " + customer.getId() + "\n"
                );
                writer.write(
                        "Name: " + customer.getName() + "\n"
                );
                writer.write(
                        "Email: " + customer.getEmail() + "\n"
                );
                writer.write(
                        "Phone: " + customer.getPhone() + "\n"
                );
                writer.write(
                        "Payment: "
                                + (customer.getPayment() == 1
                                ? "Cash"
                                : "Visa")
                                + "\n"
                );
                writer.write(
                        "License: "
                                + customer.getLicenses()
                                + "\n"
                );
                writer.write("----------------------\n");
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error updating customer file",
                    e
            );
        }
    }
}