package software.project;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;


public class Adminenutest {
	
	@Test
	void testReadInt() {
	    System.setIn(new ByteArrayInputStream("5".getBytes()));
	    adminmenu admin = new adminmenu();
	    int result = admin.readInt();
	    assertEquals(5, result);
	}
	
	
	 @Test
	    void testGenerateVehicleID() {
	        adminmenu admin = new adminmenu();
	        int id = admin.generateVehicleID();
	        assertTrue(id > 0);
	    }
	 
	 
	 @Test
	 void testSaveVehicle(){
	     adminmenu admin = new adminmenu();
	     admin.saveVehicle(
	             "Car",
	             "Honda",
	             "Civic",
	             "Black",
	             2022,
	             "123456",
	             100
	     );
	     assertTrue(true);
	 }
	 
	 
	 
	 
	 
	 @Test
	 void testPlateExists(){
	     adminmenu admin = new adminmenu();
	     boolean result = admin.plateExists("123456");
	     assertTrue(result);
	 }
	 
	 
	 @Test
	 void testValidPlateNumber(){
	     adminmenu admin = new adminmenu();
	     assertTrue(admin.isValidPlateNumber("123456"));
	 }
	 
	 @Test
	 void testReadYear() {
	     System.setIn(new ByteArrayInputStream("2024".getBytes()));
	     adminmenu admin = new adminmenu();
	     int year = admin.readYear();
	     assertEquals(2024, year);
	 }


	 @Test
	 void testReadYearInvalidThenValid() {
	     System.setIn(new ByteArrayInputStream("1800\n2022".getBytes()));
	     adminmenu admin = new adminmenu();
	     int year = admin.readYear();
	     assertEquals(2022, year);
	 }
	 
	 
	 @Test
	 void testReadPrice() {
	     System.setIn(new ByteArrayInputStream("150".getBytes()));
	     adminmenu admin = new adminmenu();
	     double price = admin.readPrice();
	     assertEquals(150, price);
	 }
	 
	 
	 
	 @Test
	 void testReadPriceInvalidThenValid() {
	     System.setIn(new ByteArrayInputStream("-20\n100".getBytes()));
	     adminmenu admin = new adminmenu();
	     double price = admin.readPrice();
	     assertEquals(100, price);
	 }
	 
	 @Test
	 void testVehicleTypeNotFound() {
	     System.setIn(new ByteArrayInputStream("2\n".getBytes()));
	     ByteArrayOutputStream output = new ByteArrayOutputStream();
	     System.setOut(new PrintStream(output));
	     adminmenu admin = new adminmenu();
		
	 }
	 
	 
	 @Test
	    void testReadColorValid() {
	        System.setIn(new ByteArrayInputStream("Blue\n".getBytes()));
	        adminmenu admin = new adminmenu();
	        String result = admin.readColor();
	        assertEquals("Blue", result);
	    }
	 
	
	 @Test
	    void testReadColorInvalidThenValid() {
	        System.setIn(new ByteArrayInputStream("123\nBlue\n".getBytes()));
	        adminmenu admin = new adminmenu();
	        String result = admin.readColor();
	        assertEquals("Blue", result);
	    }
	 
	 @Test
	    void testReadModelValid() {
	        System.setIn(new ByteArrayInputStream("Kia2024\n".getBytes()));
	        adminmenu admin = new adminmenu();
	        String result = admin.readModel();
	        assertEquals("Kia2024", result);
	    }
	 
	 
	 
	 @Test
	    void testReadModelInvalidThenValid() {
	        System.setIn(new ByteArrayInputStream("Kia-2024\nKia2024\n".getBytes()));
	        adminmenu admin = new adminmenu();
	        String result = admin.readModel();
	        assertEquals("Kia2024", result);
	    }
	 
	 
	 @Test
	    void testReadTypeValid() {
	        System.setIn(new ByteArrayInputStream("Car\n".getBytes()));
	        adminmenu admin = new adminmenu();
	        String result = admin.readType();
	        assertEquals("Car", result);
	    }
	 
	 
	 
	 @Test
	    void testReadTypeInvalidThenValid() {
	        System.setIn(new ByteArrayInputStream("123\nTruck\n".getBytes()));
	        adminmenu admin = new adminmenu();
	        String result = admin.readType();
	        assertEquals("Truck", result);
	    }
	 
	 

	 
	 
	 @Test
	 void testReadPlateNumberAlreadyExists() throws IOException {

	     FileWriter fw = new FileWriter("AddingVEHICLE.txt");
	     fw.write("1,Car,Toyota,Corolla,Red,123456,2020,50\n");
	     fw.close();
	     System.setIn(
	         new ByteArrayInputStream(
	             "123456\n222222\n".getBytes()
	         )
	     );

	     adminmenu admin = new adminmenu();
	     String result = admin.readPlateNumber();
	     assertEquals("222222", result);
	 }
	 
	 
	 @Test
	 void testCheckLoginSuccess() {
	     Manager manager = new Manager(new File("admin.txt"));
	     assertTrue(manager.checkLogin("admin", "1234"));
	 }
	 
	 @Test
	 void testCheckLoginWrongUser() {
	     Manager manager = new Manager(new File("admin.txt"));
	     assertFalse(manager.checkLogin("wrong", "1234"));
	 }
	 
	 
	 @Test
	 void testCheckLoginWrongPassword() {
	     Manager manager = new Manager(new File("admin.txt"));
	     assertFalse(manager.checkLogin("admin", "0000"));
	 }

	 
	 
	 @Test
	    void testDeleteExistingVehicle() throws IOException {
	        FileWriter fw = new FileWriter("AddingVEHICLE_test.txt");
	        fw.write("1,Toyota\n");
	        fw.write("2,Honda\n");
	        fw.close();
	        adminmenu admin = new adminmenu();
	        boolean result = admin.deleteVehicleFromFile("AddingVEHICLE_test.txt", 1);
	        assertTrue(result);
	        BufferedReader br = new BufferedReader(

	          new FileReader("AddingVEHICLE_test.txt"));
	        String line = br.readLine();
	        assertEquals("2,Honda", line);
	        br.close();
	    }
	 
	
	 @Test
	 void testDisplayNonExistingVehicleType() throws IOException {
	     adminmenu admin = new adminmenu();
	     boolean result =
	             admin.displayVehiclesFromFile(
	                     "AddingVEHICLE_test.txt",
	                     "Truck"
	             );
	     assertFalse(result);
	 }
	 
	 
	 @Test
	 void testEditVehicleModel() throws IOException {

	     FileWriter fw = new FileWriter("AddingVEHICLE_test.txt");
	     fw.write("1,Car,Toyota,Corolla,Red,2020,50\n");
	     fw.write("2,Bike,Honda,Civic,Black,2021,40\n");
	     fw.close();
	     adminmenu admin = new adminmenu();
	     boolean result = admin.editVehicleFromFile(
	             "AddingVEHICLE_test.txt",
	             1,
	             2,
	             "Camry"
	     );
	     assertTrue(result);
	     BufferedReader br = new BufferedReader(
	             new FileReader("AddingVEHICLE_test.txt")
	     );
	     String line = br.readLine();
	     assertEquals(
	         "1,Car,Toyota,Camry,Red,2020,50",
	         line
	     );
	     br.close();
	 }
	 
	 
}