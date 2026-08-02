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
             
            }
        };

        menu.admindelete();

        verify(fileService).displayVehiclesFromFile(
                "AddingVEHICLE.txt",
                "Car"
        );
    }
    
    @Test
    void showMenuShouldOpenAddMenuWhenChoiceIsOne() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt()).thenReturn(1);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu).adminadd();

        menu.showmenu();

        verify(menu).adminadd();
    }
    
    @Test
    void showMenuShouldAskAgainAfterInvalidChoice() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt()).thenReturn(9, 2);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu).admindelete();

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        menu.showmenu();

        assertTrue(
                output.toString().contains(
                        "Invalid choice! Please enter a number between 1 and 3."
                )
        );

        verify(menu).admindelete();
    }
    
    @Test
    void adminEditShouldDisplayTrucksForEditing() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt()).thenReturn(3);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu)
                .displayVehiclesForEdit("Truck");

        menu.adminedit();

        verify(menu).displayVehiclesForEdit("Truck");
    }
    
    @Test
    void adminEditShouldAskAgainAfterInvalidChoice() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt()).thenReturn(9, 1);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu)
                .displayVehiclesForEdit("Car");

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        menu.adminedit();

        assertTrue(
                output.toString().contains(
                        "Invalid choice! Please enter a number between 1 and 5."
                )
        );

        verify(menu).displayVehiclesForEdit("Car");
    }
    
    @Test
    void editVehicleShouldUpdateVehicleTypeSuccessfully() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt())
                .thenReturn(1, 2);

        when(inputService.readType())
                .thenReturn("SUV");

        when(fileService.editVehicleFromFile(
                "AddingVEHICLE.txt",
                1,
                1,
                "SUV"
        )).thenReturn(true);

        adminmenu menu =
                new adminmenu(fileService, inputService) {
            @Override
            public void showmenu() {
                
            }
        };

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        menu.editVehicle(1);

        verify(fileService).editVehicleFromFile(
                "AddingVEHICLE.txt",
                1,
                1,
                "SUV"
        );

        assertTrue(
                output.toString().contains(
                        "Vehicle updated successfully."
                )
        );
    }
    
    @Test
    void deleteVehicleShouldDeleteSuccessfully() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(fileService.deleteVehicleFromFile(
                "AddingVEHICLE.txt",
                1
        )).thenReturn(true);

        when(inputService.readInt()).thenReturn(2);

        adminmenu menu =
                new adminmenu(fileService, inputService) {
            @Override
            public void showmenu() {
              
            }
        };

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        menu.deleteVehicle(1);

        verify(fileService).deleteVehicleFromFile(
                "AddingVEHICLE.txt",
                1
        );

        assertTrue(
                output.toString().contains(
                        "Vehicle deleted successfully."
                )
        );
    }
    
    @Test
    void deleteVehicleShouldPrintNotFoundWhenDeletionFails() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(fileService.deleteVehicleFromFile(
                "AddingVEHICLE.txt",
                5
        )).thenReturn(false);

        when(inputService.readInt()).thenReturn(2);

        adminmenu menu =
                new adminmenu(fileService, inputService) {
            @Override
            public void showmenu() {
            
            }
        };

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        menu.deleteVehicle(5);

        assertTrue(
                output.toString().contains(
                        "Vehicle ID not found."
                )
        );

        verify(fileService).deleteVehicleFromFile(
                "AddingVEHICLE.txt",
                5
        );
    }
    
    @Test
    void adminDeleteShouldDisplayBuses() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt()).thenReturn(4);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu).displayVehicles("Bus");

        menu.admindelete();

        verify(menu).displayVehicles("Bus");
    }
   
    @Test
    void adminAddShouldOpenCarAdding() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt())
                .thenReturn(1)
                .thenReturn(2);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu).showmenu();

        doNothing().when(menu).enterVehicleData("Car");

        menu.adminadd();

        verify(menu).enterVehicleData("Car");
    }
    
    
    @Test
    void adminAddShouldOpenMotorcycleAdding() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt())
                .thenReturn(2)
                .thenReturn(2);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu).showmenu();

        doNothing().when(menu).enterVehicleData("Motorcycle");

        menu.adminadd();

        verify(menu).enterVehicleData("Motorcycle");
    }
    
    
    @Test
    void adminAddShouldOpenTruckAdding() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt())
                .thenReturn(3)
                .thenReturn(2);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu).showmenu();

        doNothing().when(menu).enterVehicleData("Truck");

        menu.adminadd();

        verify(menu).enterVehicleData("Truck");
    }
    
    @Test
    void adminAddShouldOpenBusAdding() {

        AdminVehicleFileService fileService =
                mock(AdminVehicleFileService.class);

        AdminInputService inputService =
                mock(AdminInputService.class);

        when(inputService.readInt())
                .thenReturn(4)
                .thenReturn(2);

        adminmenu menu =
                spy(new adminmenu(fileService, inputService));

        doNothing().when(menu).showmenu();

        doNothing().when(menu).enterVehicleData("Bus");

        menu.adminadd();

        verify(menu).enterVehicleData("Bus");
    }
    
   
    
    
}