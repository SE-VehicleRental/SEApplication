package software.project;

public class CustomerValidator {

    public boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z]+");
    }

    public boolean isValidId(String id) {
        return id != null && id.matches("\\d{7}");
    }

    public boolean isValidEmail(String email) {
        return email != null
                && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public boolean isValidPayment(int payment) {
        return payment == 1 || payment == 2;
    }
}