package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ManagerTest {

    @TempDir
    Path tempDirectory;

    private final PrintStream originalOut = System.out;

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    void correctAdminLoginShouldOpenAdminMenu()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\n"
        );

        AtomicBoolean adminMenuOpened =
                new AtomicBoolean(false);

        AtomicBoolean customerMenuOpened =
                new AtomicBoolean(false);

        Manager manager = new Manager(
                adminFile,
                scannerFor(
                        "1",
                        "admin",
                        "1234"
                ),
                () -> adminMenuOpened.set(true),
                () -> customerMenuOpened.set(true)
        );

        manager.start();

        assertTrue(adminMenuOpened.get());
        assertFalse(customerMenuOpened.get());
    }

    @Test
    void customerChoiceShouldOpenCustomerMenu()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\n"
        );

        AtomicBoolean adminMenuOpened =
                new AtomicBoolean(false);

        AtomicBoolean customerMenuOpened =
                new AtomicBoolean(false);

        Manager manager = new Manager(
                adminFile,
                scannerFor("2"),
                () -> adminMenuOpened.set(true),
                () -> customerMenuOpened.set(true)
        );

        manager.start();

        assertFalse(adminMenuOpened.get());
        assertTrue(customerMenuOpened.get());
    }

    @Test
    void wrongAdminLoginShouldReturnToRoleSelection()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\n"
        );

        AtomicBoolean adminMenuOpened =
                new AtomicBoolean(false);

        AtomicBoolean customerMenuOpened =
                new AtomicBoolean(false);

        Manager manager = new Manager(
                adminFile,
                scannerFor(
                        "1",
                        "admin",
                        "wrong",
                        "2"
                ),
                () -> adminMenuOpened.set(true),
                () -> customerMenuOpened.set(true)
        );

        manager.start();

        assertFalse(adminMenuOpened.get());
        assertTrue(customerMenuOpened.get());
    }

    @Test
    void invalidRoleShouldAskAgain()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\n"
        );

        AtomicBoolean customerMenuOpened =
                new AtomicBoolean(false);

        ByteArrayOutputStream output =
                captureOutput();

        Manager manager = new Manager(
                adminFile,
                scannerFor(
                        "9",
                        "2"
                ),
                () -> {
                },
                () -> customerMenuOpened.set(true)
        );

        manager.start();

        assertTrue(customerMenuOpened.get());
        assertTrue(
                output.toString().contains("Invalid choice")
        );
    }

    @Test
    void nonNumericChoiceShouldAskForNumberAgain()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\n"
        );

        AtomicBoolean customerMenuOpened =
                new AtomicBoolean(false);

        ByteArrayOutputStream output =
                captureOutput();

        Manager manager = new Manager(
                adminFile,
                scannerFor(
                        "abc",
                        "2"
                ),
                () -> {
                },
                () -> customerMenuOpened.set(true)
        );

        manager.start();

        assertTrue(customerMenuOpened.get());

        assertTrue(
                output.toString().contains(
                        "Invalid input! Please enter a number."
                )
        );
    }

    @Test
    void checkLoginShouldReturnTrueForCorrectCredentials()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\nmanager,abcd\n"
        );

        Manager manager = new Manager(adminFile);

        assertTrue(
                manager.checkLogin("admin", "1234")
        );
    }

    @Test
    void checkLoginShouldFindUserOnLaterLine()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\nhala,5678\n"
        );

        Manager manager = new Manager(adminFile);

        assertTrue(
                manager.checkLogin("hala", "5678")
        );
    }

    @Test
    void wrongPasswordShouldReturnFalse()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\n"
        );

        Manager manager = new Manager(adminFile);

        assertFalse(
                manager.checkLogin("admin", "wrong")
        );
    }

    @Test
    void unknownUsernameShouldReturnFalse()
            throws IOException {

        File adminFile = createAdminFile(
                "admin,1234\n"
        );

        Manager manager = new Manager(adminFile);

        assertFalse(
                manager.checkLogin("unknown", "1234")
        );
    }

    @Test
    void malformedLineShouldBeIgnored()
            throws IOException {

        File adminFile = createAdminFile(
                "invalidLine\nadmin,1234\n"
        );

        Manager manager = new Manager(adminFile);

        assertTrue(
                manager.checkLogin("admin", "1234")
        );
    }

    @Test
    void missingAdminFileShouldReturnFalse() {

        File missingFile =
                tempDirectory.resolve("missing-admin.txt")
                        .toFile();

        Manager manager = new Manager(missingFile);

        assertFalse(
                manager.checkLogin("admin", "1234")
        );
    }

    @Test
    void defaultConstructorShouldCreateManager() {
        assertNotNull(new Manager());
    }

    private File createAdminFile(String content)
            throws IOException {

        Path file =
                tempDirectory.resolve("admin.txt");

        Files.writeString(file, content);

        return file.toFile();
    }

    private Scanner scannerFor(String... values) {

        String text = String.join("\n", values);

        if (!text.isEmpty()) {
            text += "\n";
        }

        return new Scanner(
                new ByteArrayInputStream(
                        text.getBytes()
                )
        );
    }

    private ByteArrayOutputStream captureOutput() {

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        return output;
    }
}