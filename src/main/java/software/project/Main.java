package software.project;
public class Main {

	public static void main(String[] args) {
		System.out.println("Welcome to Nablus company for rent vehicles, " );
		
		RentalReminderChecker checker =
	            new RentalReminderChecker();
	    checker.checkAllRentals();

		 Manager manager = new Manager();
	        manager.start();
	}
}
 