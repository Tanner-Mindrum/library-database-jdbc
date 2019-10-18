/**
 * @author steve
 */
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class JavaLibraryCodeSql { // JDBC driver name and database URL
      static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
   static final String DB_URL = "jdbc:derby://localhost:1527/virtualLibrary";

   //  Database credentials
   static final String USER = "books";
   static final String PASS = "books123";
   
   //main menu list
   private final static String LIST = ("Select one of the following options (insert a number):\n1. "
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
            Integer.parseInt(userInput);
	}
       		 
	// Catch exception if input isn't an int
	catch(InputMismatchException | NumberFormatException exception) {
            System.out.println("That is not a valid input, please input a number.\n");
            return 0;
	}
		 
	if(Integer.parseInt(userInput) >= 1 && Integer.parseInt(userInput) <= 10) {
            return Integer.parseInt(userInput);
	}
	
	System.out.println("That is not a number between 1 and 10. Please try again.\n");
        return 0;
    }
	
    /**
    * List of options presented from the menu that the user selected
    */
    public static void MenuOptionSelected(int Option, Connection conn, Statement stmt) {

        // Option 1: List all writing groups
	if (Option == 1) {
            
            try{
            
            // Execute a query
            ResultSet rs = stmt.executeQuery("SELECT groupName FROM WritingGroup");
            
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
                    System.out.println("Books & publishers associated with \"" + userInput + "\": ");
                }
                
                //Retrieve data by column name and display values
                System.out.println("Book #" + (bookCount+1) + ": " + rs.getString("bookTitle") + ", "
                        + rs.getString("numberOfPages") + " pages. @" + rs.getString("yearPublished") + " "
                        + rs.getString("publisherName") + " (" + rs.getString("publisherAddress") + ", "
                        + rs.getString("publisherPhone") + ", " + rs.getString("publisherEmail") + ")");

                initialDisplay = false;
                bookCount++;
            }
            if (bookCount == 0) {
                System.out.println("\nThat writing group does not exist.");
            }
            System.out.println(); // Print new line to add white space before next menu print
            
            // Close environments
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

        // Option 3: List all Publishers
	else if (Option == 3) {
            
            try{
           
            // Execute query
            ResultSet rs = stmt.executeQuery("SELECT publisherName FROM Publisher");
            
            // Extract data from result set
            int pubCount = 1;
            while(rs.next()){
                
                // Retrieve data by column name and display values
                System.out.println("Publisher #" + pubCount + ": " + rs.getString("publisherName"));
                pubCount++;
            }
            System.out.println(); // Print new line to add white space before next menu print
            
            // Close environments
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
            sql = "SELECT publisherName, publisherAddress, publisherPhone, PublisherEmail FROM Publisher";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<String> publisherArrayList = new ArrayList<String>();
            //STEP 5: Extract data from result set
            //int pubCount = 1;
            while(rs.next()){
                //Retrieve by column name
                String pubName  = rs.getString("publisherName");                
                publisherArrayList.add(pubName);
                //String headWriter = rs.getString("headWriter");
                //String year = rs.getString("yearFormed");
                //String subject = rs.getString("subject");
                //Display values
                //System.out.println("Publisher #" + pubCount + ": " + pubName);
                //System.out.print(", Head Writer: " + headWriter);
                //System.out.print(", Year Made: " + year);
                //System.out.println(", Subject: " + subject);
               // pubCount++;


            }
            boolean flag = true;
            while(flag){
            System.out.println("Please select a publisher to display:\n");
            for(int i=0; i < publisherArrayList.size()-1;i++){
                System.out.println(publisherArrayList.get(i));
            }
            System.out.println("\nEnter Your Selection:\n");
            Scanner input = new Scanner(System.in);
            String userInput;
            userInput = input.nextLine();
            
            if(publisherArrayList.contains(userInput)){
                PreparedStatement myStmt = conn.prepareStatement("SELECT publisherName, publisherAddress, publisherPhone, PublisherEmail FROM Publisher Where PublisherName = ?");
                myStmt.setString(1, userInput);
                
                rs = myStmt.executeQuery();
                while(rs.next()){
                    String pubNamee = rs.getString("publisherName");
                    String pubAddress = rs.getString("publisherAddress");
                    String pubPhone = rs.getString("publisherPhone");
                    String pubEmail = rs.getString("publisherEmail");
                    
                    System.out.println("Publisher: " + pubNamee + ", Address: " + pubAddress + ", Phone: " + pubPhone + ", Email: " + pubEmail);
                }
                myStmt = conn.prepareStatement("SELECT groupName, headWriter, yearFormed, subject, bookTitle, yearPublished, numberOfPages From Books Natural Join Publisher Natural Join WritingGroup Where PublisherName = ?");
                myStmt.setString(1, userInput);
                rs = myStmt.executeQuery();
                while(rs.next()){
                    String groupName = rs.getString("groupName");
                    String headWriter = rs.getString("headWriter");
                    String yearFormed = rs.getString("yearFormed");
                    String subject = rs.getString("subject");
                    String bookTitle = rs.getString("bookTitle");
                    String yearPublished = rs.getString("yearPublished");
                    String numberPages = rs.getString("numberOfPages");
                    
                    System.out.println("Group Name: " + groupName + ", Head Writer: " + headWriter + ", Year Formed: " + yearFormed + ", Subject : " + subject + ""
                            + ", Book Title: " + bookTitle + ", Year Published: " + yearPublished + ", Number of Pages: " + numberPages);
                }
                flag = false;
            }
            else{
                System.out.println("This is publisher does not exist");
            }
            }
            boolean flag = true;
            while(flag){
            System.out.println("Please select a publisher to display:\n");
            for(int i=0; i < publisherArrayList.size()-1;i++){
                System.out.println(publisherArrayList.get(i));
            }
            System.out.println("\nEnter Your Selection:\n");
            Scanner input = new Scanner(System.in);
            String userInput;
            userInput = input.nextLine();
            
            if(publisherArrayList.contains(userInput)){
                PreparedStatement myStmt = conn.prepareStatement("SELECT publisherName, publisherAddress, publisherPhone, PublisherEmail FROM Publisher Where PublisherName = ?");
                myStmt.setString(1, userInput);
                
                rs = myStmt.executeQuery();
                while(rs.next()){
                    String pubNamee = rs.getString("publisherName");
                    String pubAddress = rs.getString("publisherAddress");
                    String pubPhone = rs.getString("publisherPhone");
                    String pubEmail = rs.getString("publisherEmail");
                    
                    System.out.println("Publisher: " + pubNamee + ", Address: " + pubAddress + ", Phone: " + pubPhone + ", Email: " + pubEmail);
                }
                myStmt = conn.prepareStatement("SELECT groupName, headWriter, yearFormed, subject, bookTitle, yearPublished, numberOfPages From Books Natural Join Publisher Natural Join WritingGroup Where PublisherName = ?");
                myStmt.setString(1, userInput);
                rs = myStmt.executeQuery();
                while(rs.next()){
                    String groupName = rs.getString("groupName");
                    String headWriter = rs.getString("headWriter");
                    String yearFormed = rs.getString("yearFormed");
                    String subject = rs.getString("subject");
                    String bookTitle = rs.getString("bookTitle");
                    String yearPublished = rs.getString("yearPublished");
                    String numberPages = rs.getString("numberOfPages");
                    
                    System.out.println("Group Name: " + groupName + ", Head Writer: " + headWriter + ", Year Formed: " + yearFormed + ", Subject : " + subject + ""
                            + ", Book Title: " + bookTitle + ", Year Published: " + yearPublished + ", Number of Pages: " + numberPages);
                }
                flag = false;
            }
            else{
                System.out.println("This is publisher does not exist");
            }
            }            
            //System.out.println();
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
        
        
        //List all book titles---------------------------------------------------------------
	else if (Option == 5) {
            
            try{
            // Open a database connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            // Execute a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT bookTitle FROM Books");
            
            // Extract data from result set
            int bookCount = 1; // Counter used for numbered display (e.g. group #1, #2...)
            while(rs.next()){

                // Retrieve data by column name and display values
                System.out.println("Book #" + bookCount + ": " + rs.getString("bookTitle"));
                bookCount++;
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
        
        
        //List all the data for a specific book-----------------------------------------------
	else if (Option == 6) {
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
            sql = "SELECT * From Books";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<String> BookArrayList = new ArrayList<String>();
            //STEP 5: Extract data from result set
            //int pubCount = 1;
            while(rs.next()){
                //Retrieve by column name
                String BookTitle  = rs.getString("BookTitle");                
                BookArrayList.add(BookTitle);
                //String headWriter = rs.getString("headWriter");
                //String year = rs.getString("yearFormed");
                //String subject = rs.getString("subject");
                //Display values
                //System.out.println("Publisher #" + pubCount + ": " + pubName);
                //System.out.print(", Head Writer: " + headWriter);
                //System.out.print(", Year Made: " + year);
                //System.out.println(", Subject: " + subject);
               // pubCount++;


            }
            boolean flag = true;
            while(flag){
            System.out.println("Please select a Book to display:\n");
            for(int i=0; i < BookArrayList.size();i++){
                System.out.println(BookArrayList.get(i));
            }

            }
            boolean flag = true;
            while(flag){
            System.out.println("Please select a Book to display:\n");
            for(int i=0; i < BookArrayList.size()-1;i++){
                System.out.println(BookArrayList.get(i));
            }

            System.out.println("\nEnter Your Selection:\n");
            Scanner input = new Scanner(System.in);
            String userInput;
            userInput = input.nextLine();
            
            if(BookArrayList.contains(userInput)){
                PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM Books Where BookTitle = ?");
                myStmt.setString(1, userInput);
                
                rs = myStmt.executeQuery();
                while(rs.next()){
                    String BookTitlee = rs.getString("BookTitle");
                    String groupName = rs.getString("groupName");
                    String PublisherName = rs.getString("publisherName");
                    String yearPublished = rs.getString("yearPublished");
                    String numberPages = rs.getString("numberOfPages");
                    
                    System.out.println("Book Title: " + BookTitlee + ", Group Name: " + groupName + ", Publisher Name: " + PublisherName + ", Year Published: " + yearPublished + ", Number of pages: " + numberPages);
                }
                myStmt = conn.prepareStatement("SELECT * FROM Books Natural Join WritingGroup Natural Join Publisher Where BookTitle = ?");
                myStmt.setString(1, userInput);
                rs = myStmt.executeQuery();
                while(rs.next()){
                    String groupName = rs.getString("groupName");
                    String headWriter = rs.getString("headWriter");
                    String yearFormed = rs.getString("yearFormed");
                    String subject = rs.getString("subject");
                    String pubName = rs.getString("publisherName");
                    String pubAddress = rs.getString("publisherAddress");
                    String pubPhone = rs.getString("publisherPhone");
                    String pubEmail = rs.getString("publisherEmail");
                    
                    System.out.println("Group Name: " + groupName + ", Head Writer: " + headWriter + ", Year Formed: " + yearFormed + ", Subject : " + subject + ""
                            + ", Publisher Name: " + pubName + ", Publisher Address: " + pubAddress + ", Publisher Phone: " + pubPhone + ", Publisher Email: " + pubEmail);
                }
                flag = false;
            }
            else{
                System.out.println("This is Book does not exist");
            }
            }            
            //System.out.println();
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
        
        //Insert a new book--------------------------------------------------------------------
        else if (Option == 7){
           try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);   

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();          
            // Prepared statements
            Scanner input = new Scanner(System.in);
             String groupNameInput="";
             String publisherInput="";
            boolean groupFound=false;
            while(groupFound==false){
            System.out.println("\nEnter the group name:\n");
            groupNameInput = input.nextLine();
            System.out.println("\nEnter the publisher:\n");
              publisherInput = input.nextLine();
           
              PreparedStatement allGroups = conn.prepareStatement("SELECT * FROM WritingGroup"
                    + " NATURAL JOIN Publisher WHERE groupName = ? AND publisherName=?");
              allGroups.setString(1,groupNameInput);
              allGroups.setString(2, publisherInput);
              ResultSet rs = allGroups.executeQuery();
              int count=0;
              if (rs.next()){
                  count++;
               }
              if(count>0){
                  groupFound=true;
              }else if(count==0){
                  System.out.println("Sorry! That writing group or publisher does not exist");
              }
            }
            
            String titleInput="";
            boolean bookExists=true;
            while(bookExists==true){
              System.out.println("\nEnter the book title:\n");
              titleInput = input.nextLine();
              PreparedStatement allBooks=conn.prepareStatement("SELECT * FROM Books WHERE groupName = ? AND bookTitle=?");
              allBooks.setString(1,groupNameInput);
              allBooks.setString(2, titleInput);
              ResultSet rs = allBooks.executeQuery();
              int count=0;
              if (rs.next()){
                  count++;
               }
              if(count>0){
                  System.out.println("Sorry! That book already exists for writing group "+groupNameInput);
              }else if(count==0){
                  bookExists=false;
              }
            }
              System.out.println("\nEnter the year published:\n");
            int yearInput = input.nextInt();
            System.out.println("\nEnter the number of pages:\n");
            int pagesInput = input.nextInt();
            
            String sql="INSERT INTO books(bookTitle,yearPublished,numberOfPages,"
                    + "groupName,publisherName)VALUES(?,?,?,?,?)";
            PreparedStatement myStmt = conn.prepareStatement(sql);
            myStmt.clearParameters();
            myStmt.setString(1,titleInput);
            myStmt.setInt(2, yearInput);
            myStmt.setInt(3, pagesInput);
            myStmt.setString(4, groupNameInput);
            myStmt.setString(5, publisherInput);
                
            myStmt.executeUpdate();
            myStmt.close();

            //STEP 6: Clean-up environment
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
      
      boolean inMainMenu = true;
      int MenuSelection = 0;
		 
      // this creates a main menu that is to be used by the user
      while (inMainMenu) {
          
        // Open a database connection and create statement 
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        
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