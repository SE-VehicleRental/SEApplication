package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ManagerTest {

    @TempDir
    Path tempDirectory;

    @Test
    void checkLoginShouldReturnTrueWhenCredentialsAreCorrect()
            throws IOException {

        Path adminFile = tempDirectory.resolve("admin.txt");

        Files.writeString(
                adminFile,
                "admin,1234\nmanager,abcd"
        );

        Manager manager = new Manager(adminFile.toFile());

        boolean result = manager.checkLogin("admin", "1234");

        assertTrue(result);
    }

    @Test
    void checkLoginShouldReturnFalseWhenPasswordIsWrong()
            throws IOException {

        Path adminFile = tempDirectory.resolve("admin.txt");

        Files.writeString(
                adminFile,
                "admin,1234"
        );

        Manager manager = new Manager(adminFile.toFile());

        boolean result = manager.checkLogin("admin", "wrongPassword");

        assertFalse(result);
    }

    @Test
    void checkLoginShouldReturnFalseWhenUsernameDoesNotExist()
            throws IOException {

        Path adminFile = tempDirectory.resolve("admin.txt");

        Files.writeString(
                adminFile,
                "admin,1234"
        );

        Manager manager = new Manager(adminFile.toFile());

        boolean result =
                manager.checkLogin("unknownUser", "1234");

        assertFalse(result);
    }

    @Test
    void checkLoginShouldFindUserOnSecondLine()
            throws IOException {

        Path adminFile = tempDirectory.resolve("admin.txt");

        Files.writeString(
                adminFile,
                "admin,1234\nhala,5678"
        );

        Manager manager = new Manager(adminFile.toFile());

        boolean result = manager.checkLogin("hala", "5678");

        assertTrue(result);
    }

    @Test
    void checkLoginShouldReturnFalseWhenFileDoesNotExist() {

        Path missingFile =
                tempDirectory.resolve("missing-admin.txt");

        Manager manager = new Manager(missingFile.toFile());

        boolean result =
                manager.checkLogin("admin", "1234");

        assertFalse(result);
    }

    @Test
    void checkLoginShouldIgnoreInvalidFileLine()
            throws IOException {

        Path adminFile = tempDirectory.resolve("admin.txt");

        Files.writeString(
                adminFile,
                "invalidLine\nadmin,1234"
        );

        Manager manager = new Manager(adminFile.toFile());

        boolean result = manager.checkLogin("admin", "1234");

        assertTrue(result);
    }
}