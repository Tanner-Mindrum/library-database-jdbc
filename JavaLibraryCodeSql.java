/**
 * @author steve
 */
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JavaLibraryCodeSql { // JDBC driver name and database URL
   static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
   static final String DB_URL = "jdbc:derby://localhost:1527/virtualLibrary";

   //  Database credentials
   static final String USER = "books";
   static final String PASS = "books123";
   
   //main menu list
   private final static String LIST = ("Select One of the following options (insert a number):\n1. "
	+ "List all writing groups\n2. List all the data for a specific writing group"
	+ "\n3. List all Publishers\n4. List all the data for a specific publisher"
	+ "\n5. List all book titles\n6. List all the data for a specific book"
	+ "\n7. Insert a new book \n8. Insert a new publisher and update all books "
	+ "published by one publisher to be published by the new pubisher \n9. Remove a book \n10. Done");
   
   // Global scanner for user input
   static Scanner inputs = new Scanner(System.in);

    /**
    * will return a number that is in the list
    * @return a number between 1 and 8 else return 0
    */
    public static int MenuTest() {
	String userInput;
	try {
            System.out.println(LIST + "\n");
            System.out.println("Enter an option: ");
            userInput = inputs.nextLine();
	}
		 
	//catch exception if input isn't an int
	catch(InputMismatchException | NumberFormatException exception) {
            System.out.println("That is not a valid input, please input a number.");
            return 0;
	}
		 
	//changes user input to an int and checks if it is between numbers 1 and 10
	int userInputAsInt = Integer.parseInt(userInput);
		 
	if(userInputAsInt > 0 && userInputAsInt <= 10) {
            return userInputAsInt;
	}
	
	System.out.println("This is not a number between 1 and 10");
        return 0;
    }
	
    /**
    * List of options presented from the menu that the user selected
    */
    public static void MenuOptionSelected(int Option, Connection conn, Statement stmt) {

        // Option 1: List all writing groups
	if (Option == 1) {
            
            try{
            // Open a database connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            // Execute a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT groupName, headWriter, yearFormed, subject FROM WritingGroup");
            
            // Extract data from result set
            int groupCount = 1; // Counter used for numbered display (e.g. group #1, #2...)
            while(rs.next()){

                // Retrieve data by column name and display values
                System.out.println("Writing group #" + groupCount + ": " + rs.getString("groupName"));
                groupCount++;
            }
            System.out.println(); // Print new line to add white space before next menu print
            
            // Close environment
            rs.close();
            stmt.close();
            conn.close();
            
            }
            catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
            }//end try
	}
        
        // Option 2: List all the data for a specific writing group
	else if (Option == 2) {
            
            // Get a writing group from the user
            System.out.println("Enter a writing group: ");
            String userInput = inputs.nextLine();
            
            try{
            // Open a database connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Prepared statements
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM Books NATURAL JOIN WritingGroup"
                    + " NATURAL JOIN Publisher WHERE groupName = ?");
            myStmt.setString(1, userInput);
                          
            // Execute queries
            ResultSet rs = myStmt.executeQuery();

            boolean initialDisplay = true;
            int bookCount = 0;
            while(rs.next()){
                
                // Extract and display data from result set
                if (initialDisplay) {
                    System.out.println("\nInformation regarding \"" + userInput + "\":");
                    System.out.println("Head writer: " + rs.getString("headWriter"));
                    System.out.println("Year formed: " + rs.getString("yearFormed"));
                    System.out.println("Subject of focus: " + rs.getString("subject") + "\n");
                    System.out.println("Books & publishers associated with " + userInput + ": ");
                }
                
                //Retrieve data by column name and display values
                System.out.println("Book #" + bookCount+1 + ": " + rs.getString("bookTitle") + ", "
                        + rs.getString("numberOfPages") + " pages. @" + rs.getString("yearPublished") + " "
                        + rs.getString("publisherName") + " (" + rs.getString("publisherAddress") + ", "
                        + rs.getString("publisherPhone") + ", " + rs.getString("publisherEmail") + ")");

                initialDisplay = false;
                bookCount++;
            }
            if (bookCount == 0) {
                System.out.println("\nThat writing group does not exist.");
            }
            System.out.println();
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
            }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
            }//end try
            	
	}
        
        
        //List all Publishers -----------------------------------------------------------
	else if (Option == 3) {
            
            try{
                
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            
            String sql;
            sql = "SELECT publisherName FROM Publisher";
            ResultSet rs = stmt.executeQuery(sql);
            
            //STEP 5: Extract data from result set
            int pubCount = 1;
            while(rs.next()){
                //Retrieve by column name
                String pubName  = rs.getString("publisherName");
                //String headWriter = rs.getString("headWriter");
                //String year = rs.getString("yearFormed");
                //String subject = rs.getString("subject");

                //Display values
                System.out.println("Publisher #" + pubCount + ": " + pubName);
                //System.out.print(", Head Writer: " + headWriter);
                //System.out.print(", Year Made: " + year);
                //System.out.println(", Subject: " + subject);
                pubCount++;
            }
            System.out.println();
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
            }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
            }//end try
			
	}
        
        
        //List all the data for a specific publisher--------------------------------------
	else if (Option == 4) {
			
	}
        
        
        //List all book titles---------------------------------------------------------------
	else if (Option == 5) {
			
	}
        
        
        //List all the data for a specific room-----------------------------------------------
	else if (Option == 6) {
			
	}
        
        //Insert a new book--------------------------------------------------------------------
        else if (Option == 7){
        }
        
        
        //Insert a new publisher and update all books published by one publisher
        //to be published by the new pubisher-----------------------------------------------------
        else if (Option == 8){
        }
        
        
        //Remove a book----------------------------------------------------------------------------
	else {
			
	}
    }

    
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      
      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      
      boolean inMainMenu = true;
      int MenuSelection = 0;
		 
      // this creates a main menu that is to be used by the user
      while (inMainMenu) {
	boolean validInput = false;
	while(!validInput) {
            MenuSelection = MenuTest();
            if(MenuSelection != 0) {
		validInput = true;
            }
	}
			 
	//goes through the list of possible options that will be displayed unless is 10
	if(MenuSelection != 10) {
            MenuOptionSelected(MenuSelection,conn,stmt);
	}
	else {
            inMainMenu = false;
	}
      }
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}