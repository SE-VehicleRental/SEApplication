package software.project;

import java.util.ArrayList;

public class CustomerData {

    private String id;
    private String name;
    private String email;
    private String phone;
    private int payment;
    private ArrayList<String> licenses = new ArrayList<>();

    public CustomerData() {
    }

    public CustomerData(
            String id,
            String name,
            String email,
            String phone,
            int payment,
            ArrayList<String> licenses) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.payment = payment;
        this.licenses = licenses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public ArrayList<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(ArrayList<String> licenses) {
        this.licenses = licenses;
    }
}