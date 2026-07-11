package software.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerValidatorTest {

    private CustomerValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CustomerValidator();
    }

    @Test
    void validNameShouldReturnTrue() {
        assertTrue(validator.isValidName("Hala"));
    }

    @Test
    void nameWithNumbersShouldReturnFalse() {
        assertFalse(validator.isValidName("Hala123"));
    }

    @Test
    void emptyNameShouldReturnFalse() {
        assertFalse(validator.isValidName(""));
    }

    @Test
    void nullNameShouldReturnFalse() {
        assertFalse(validator.isValidName(null));
    }

    @Test
    void validIdShouldReturnTrue() {
        assertTrue(validator.isValidId("1234567"));
    }

    @Test
    void invalidIdShouldReturnFalse() {
        assertFalse(validator.isValidId("12345"));
        assertFalse(validator.isValidId("1234abc"));
        assertFalse(validator.isValidId(null));
    }

    @Test
    void validEmailShouldReturnTrue() {
        assertTrue(validator.isValidEmail("hala@email.com"));
    }

    @Test
    void invalidEmailShouldReturnFalse() {
        assertFalse(validator.isValidEmail("halaemail.com"));
        assertFalse(validator.isValidEmail("hala@email"));
        assertFalse(validator.isValidEmail(null));
    }

    @Test
    void validPhoneShouldReturnTrue() {
        assertTrue(validator.isValidPhone("0591234567"));
    }

    @Test
    void invalidPhoneShouldReturnFalse() {
        assertFalse(validator.isValidPhone("059123"));
        assertFalse(validator.isValidPhone("05912abc67"));
        assertFalse(validator.isValidPhone(null));
    }

    @Test
    void validPaymentsShouldReturnTrue() {
        assertTrue(validator.isValidPayment(1));
        assertTrue(validator.isValidPayment(2));
    }

    @Test
    void invalidPaymentShouldReturnFalse() {
        assertFalse(validator.isValidPayment(0));
        assertFalse(validator.isValidPayment(3));
    }
}