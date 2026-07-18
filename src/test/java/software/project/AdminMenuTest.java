package software.project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class AdminMenuTest {

    private final PrintStream originalOut = System.out;

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    void enterVehicleDataShouldReadAndSaveVehicle() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readType())
                .thenReturn("Toyota");

        when(inputService.readModel())
                .thenReturn("Corolla");

        when(inputService.readYear())
                .thenReturn(2024);

        when(inputService.readColor())
                .thenReturn("White");

        when(inputService.readPlateNumber(fileService))
                .thenReturn("ABC-123");

        when(inputService.readPrice())
                .thenReturn(50.0);

        adminmenu menu =
                new adminmenu(fileService, inputService);

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        menu.enterVehicleData("Car");

        verify(fileService).saveVehicle(
                "Car",
                "Toyota",
                "Corolla",
                "White",
                2024,
                "ABC-123",
                50.0
        );

        assertTrue(
                output.toString().contains(
                        "Car added successfully!"
                )
        );
    }
    
    @Test
    void adminDeleteShouldRejectInvalidChoice() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt())
                .thenReturn(99);

        adminmenu menu =
                new adminmenu(fileService, inputService);

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        menu.admindelete();

        assertTrue(
                output.toString().contains("Invalid choice.")
        );

        verifyNoInteractions(fileService);
    }
    
    @Test
    void adminDeleteShouldDisplayCars() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt())
                .thenReturn(1, 2);

        when(fileService.displayVehiclesFromFile(
                "AddingVEHICLE.txt",
                "Car"
        )).thenReturn(false);

        adminmenu menu =
                new adminmenu(fileService, inputService) {
            @Override
            public void showmenu() {
                // Prevent real menu from opening.
            }
        };

        menu.admindelete();

        verify(fileService).displayVehiclesFromFile(
                "AddingVEHICLE.txt",
                "Car"
        );
    }
}