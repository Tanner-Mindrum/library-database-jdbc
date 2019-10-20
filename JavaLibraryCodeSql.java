/**
 * @author steve
 */
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class JavaLibraryCodeSql { // JDBC driver name and database URL
   static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
   static final String DB_URL = "jdbc:derby://localhost:1527/virtualLibrary";

   //  Database credentials
   static final String USER = "books";
   static final String PASS = "books123";
   
   //main menu list
   private final static String LIST = ("Select one of the following options (insert a number):\n1. "
	+ "List all writing groups\n2. List all the data for a specific writing group"
	+ "\n3. List all publishers\n4. List all the data for a specific publisher"
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
        Scanner in = new Scanner(System.in);
        String userInput;
        
	try {
            System.out.println(LIST + "\n");
            System.out.println("Enter an option: ");
            userInput = in.nextLine();
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
            try{
                
                ArrayList writingGroupNames = new ArrayList<String>();

                ResultSet rs = stmt.executeQuery("SELECT groupName FROM WritingGroup");
                System.out.println("Select a writing group to display: ");
                while (rs.next()) {
                    System.out.println(rs.getString("groupName"));
                    writingGroupNames.add(rs.getString("groupName"));
                }
                
                // Get a writing group from the user
                System.out.println("\nEnter a writing group: ");
                String userInput = inputs.nextLine();
                while (!(writingGroupNames).contains(userInput)) {
                    System.out.println("That writing group does not exist in our records. Please try again.");
                    System.out.println("\nEnter a writing group: ");
                    userInput = inputs.nextLine();
                }

                // Prepared statements
                PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM Books NATURAL JOIN WritingGroup"
                        + " NATURAL JOIN Publisher WHERE groupName = ?");
                myStmt.setString(1, userInput);

                // Execute queries
                rs = myStmt.executeQuery();

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

                System.out.println(); // Print new line to add white space before next menu print

                // Close environments
                myStmt.close();
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
        
        //List all the data for a specific publisher
	else if (Option == 4) {
                        
            try{ 
                
            ResultSet rs = stmt.executeQuery("SELECT publisherName, publisherAddress, publisherPhone,"
                    + "PublisherEmail FROM Publisher");
            
            ArrayList<String> publisherArrayList = new ArrayList<String>();

            while(rs.next()){
                // Retrieve by column name and add to array list
                publisherArrayList.add(rs.getString("publisherName"));
            }
            
            boolean flag = true;
            while(flag){
                
            System.out.println("Please select a publisher to display:\n");
            
            for(int i=0; i < publisherArrayList.size(); i++){
                System.out.println(publisherArrayList.get(i));
            }
            
            System.out.println("\nEnter Your Selection:\n");
            String userInput = inputs.nextLine();
            
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
                System.out.println("That publisher does not exist. Please try again.");
            }
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
        
        
        //List all book titles
	else if (Option == 5) {
            
            try{

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
        
        //List all the data for a specific book
	else if (Option == 6) {
            try{ 
                
            ResultSet rs = stmt.executeQuery("SELECT * FROM Books");
            ArrayList<String> BookArrayList = new ArrayList<String>();
            ArrayList publisherNames = new ArrayList<String>();
            ArrayList writingGroupNames = new ArrayList<String>();

            while(rs.next()){            
                BookArrayList.add(rs.getString("BookTitle"));
            }
            
            boolean flag = true;
            while(flag) {
                
                System.out.println("Please select a Book to display:\n");
                for(int i=0; i < BookArrayList.size() ;i++){
                System.out.println(BookArrayList.get(i));
                }

            System.out.println("\nEnter Your Selection:\n");
            String userInput = inputs.nextLine();
            
            if(BookArrayList.contains(userInput)){
                
                PreparedStatement myBookStmt = conn.prepareStatement("SELECT groupName, publisherName FROM Books"
                        + " WHERE bookTitle = ?");
                myBookStmt.setString(1, userInput);
                rs = myBookStmt.executeQuery();
                System.out.println("\nWriting groups and publishers assocaited with " + userInput + ": ");
                while (rs.next()) {
                    System.out.println("Writing group: " + rs.getString("groupName"));
                    writingGroupNames.add(rs.getString("groupName"));
                    System.out.println("Publisher: " + rs.getString("publisherName") + "\n");
                    publisherNames.add(rs.getString("publisherName"));
                }
                
                // User input for writing group or publisher name and input validation
                System.out.println("\nEnter the writing group or publisher name associated with the"
                        + " book you want to display: ");
                String WGPName = inputs.nextLine();
                while (!(publisherNames.contains(WGPName) || writingGroupNames.contains(WGPName))) {
                    System.out.println("They have not worked with " + userInput + ". Please try again.");
                    System.out.println("\nEnter the writing group or publisher name associated with the"
                        + " book you want to display: ");
                    WGPName = inputs.nextLine();
                }
                
                PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM Books Where BookTitle = ?"
                        + " AND (groupName = ? OR publisherName = ?)");
                myStmt.setString(1, userInput);
                myStmt.setString(2, WGPName);
                myStmt.setString(3, WGPName);
                rs = myStmt.executeQuery();
                
                while(rs.next()){
                    String BookTitle = rs.getString("BookTitle");
                    String groupName = rs.getString("groupName");
                    String PublisherName = rs.getString("publisherName");
                    String yearPublished = rs.getString("yearPublished");
                    String numberPages = rs.getString("numberOfPages");
                    
                    System.out.println("Book Title: " + BookTitle + ", Group Name: " + groupName + ", Publisher Name: "
                            + PublisherName + ", Year Published: " + yearPublished + ", Number of pages: " + numberPages);
                }
                
                myStmt = conn.prepareStatement("SELECT * FROM Books Natural Join WritingGroup Natural Join Publisher"
                        + " Where BookTitle = ?");
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
                System.out.println("This book does not exist in our records. Please try again. ");
            }
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
        
        //Insert a new book
        else if (Option == 7){
            try{
                
                Scanner in = new Scanner(System.in);
                ArrayList publisherNames = new ArrayList<String>();
                ArrayList writingGroupNames = new ArrayList<String>();

                ResultSet rs = stmt.executeQuery("SELECT groupName, publisherName FROM WritingGroup"
                        + " NATURAL JOIN Publisher");
                while (rs.next()) {
                    writingGroupNames.add(rs.getString("groupName"));
                    publisherNames.add(rs.getString("publisherName"));
                
                }
                
                System.out.println("\nEnter the group name:\n");
                String groupNameInput = in.nextLine();
                while (!(writingGroupNames.contains(groupNameInput))) {
                    System.out.println("That writing group does not exist in our records. Please try again.");
                    groupNameInput = in.nextLine();
                }
                System.out.println("\nEnter the publisher:\n");
                String publisherInput = in.nextLine();
                while (!(publisherNames.contains(publisherInput))) {
                    System.out.println("That publisher does not exist in our records. Please try again.");
                    publisherInput = in.nextLine();
                }
            
            String titleInput = "";
            boolean bookExists = true;
            while (bookExists == true) {

                System.out.println("\nEnter the book title:\n");
                titleInput = in.nextLine();

                PreparedStatement allBooks = conn.prepareStatement("SELECT * FROM Books WHERE (groupName = ? OR "
                        + "publisherName = ?) AND bookTitle = ?");
                allBooks.setString(1, groupNameInput);
                allBooks.setString(2, publisherInput);
                allBooks.setString(3, titleInput);
                rs = allBooks.executeQuery();

                int count = 0;
                if (rs.next()){
                      count++;
                }
                if (count > 0) {
                    System.out.println("Sorry! That book already exists for writing group " + groupNameInput + " or"
                    + " publisher " + publisherInput);
                }
                else if (count == 0){
                    bookExists = false;
                }
                rs.close();
            }
            
            int yearInput = 0;
            int count = 0;
            while (true) {
                try {
                    System.out.println("\nEnter the year published:\n");
                    String yearStringInput = in.nextLine();
                    yearInput = Integer.parseInt(yearStringInput);
                }
                catch (InputMismatchException | NumberFormatException exception) {
                    System.out.println("Not a valid year. Please try again");
                    count++;
                }
                if (count == 0) {
                    break;
                }
                count = 0;
            }
            
            int pagesInput = 0;
            count = 0;
            while (true) {
                try {
                    System.out.println("\nEnter the number of pages:\n");
                    String pagesStringInput = in.nextLine();
                    pagesInput = Integer.parseInt(pagesStringInput);
                }
                catch (InputMismatchException | NumberFormatException exception) {
                    System.out.println("Not a valid page number. Please try again");
                    count++;
                }
                if (count == 0) {
                    break;
                }
                count = 0;
            }
            
            String sql = "INSERT INTO books (bookTitle, yearPublished, numberOfPages, "
                    + "groupName, publisherName) VALUES (?,?,?,?,?)";
            PreparedStatement myStmt = conn.prepareStatement(sql);
            myStmt.clearParameters();
            myStmt.setString(1, titleInput);
            myStmt.setInt(2, yearInput);
            myStmt.setInt(3, pagesInput);
            myStmt.setString(4, groupNameInput);
            myStmt.setString(5, publisherInput);
                
            myStmt.executeUpdate();
            myStmt.close();
            
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
        //to be published by the new pubisher
        else if (Option == 8){	
            try{         
            // Prepared statements
            Scanner input = new Scanner(System.in);
            String publisherInput = "";
            
            boolean pubFound=false;
            while (pubFound == false) {
                System.out.println("\nEnter an existing publisher name:\n");
                publisherInput = input.nextLine();

                PreparedStatement allGroups = conn.prepareStatement("SELECT * FROM WritingGroup"
                      + " NATURAL JOIN Publisher WHERE publisherName = ?");
                allGroups.setString(1,publisherInput);
                ResultSet rs = allGroups.executeQuery();
                int count=0;
                if (rs.next()){
                    count++;
                }
                if(count>0){
                    pubFound=true;
                }
                else if(count==0){
                    System.out.println("Sorry! Publisher does not exist");
                }
                rs.close();
            }
            
            String updatedPublisherInput = "";
            boolean pubExists = true;
            
            while (pubExists == true) {
                System.out.println("\nEnter a new publisher:\n");
                updatedPublisherInput = input.nextLine();
                PreparedStatement allPublishers = conn.prepareStatement("SELECT * FROM Publisher"
                        + " WHERE publisherName = ?");
                allPublishers.setString(1,updatedPublisherInput);
                ResultSet rs = allPublishers.executeQuery();
                int count=0;
                if (rs.next()) {
                    count++;
                 }
                if(count>0) {
                    System.out.println("Sorry! That publisher already exists. Please try again.");
                }
                else if(count==0) {
                    pubExists=false;
                }
                rs.close();
            } 
            
            System.out.println("\nEnter the publisher's address:\n");
            String addressInput = input.nextLine();
            System.out.println("\nEnter the publisher's phone number:\n");
            String phoneInput = input.nextLine();
            System.out.println("\nEnter the publisher's email:\n");
            String emailInput = input.nextLine();
            
            String sql = "INSERT INTO Publisher(publisherName,publisherAddress,publisherPhone,"
                    + "publisherEmail)VALUES(?,?,?,?)";
            PreparedStatement myStmt = conn.prepareStatement(sql);
            myStmt.clearParameters();
            myStmt.setString(1,updatedPublisherInput);
            myStmt.setString(2, addressInput);
            myStmt.setString(3, phoneInput);
            myStmt.setString(4, emailInput);
            myStmt.executeUpdate();
            myStmt.close();
            
            PreparedStatement lastStmt = conn.prepareStatement("UPDATE Books SET publisherName = ? WHERE publisherName = ?");
            lastStmt.setString(1, updatedPublisherInput);
            lastStmt.setString(2, publisherInput);
            lastStmt.executeUpdate();
            
            System.out.println("\n" + updatedPublisherInput + " has bought out " + publisherInput + ". They are now "
                    + "associated with the following books: ");
            lastStmt = conn.prepareStatement("SELECT * FROM Books NATURAL JOIN WritingGroup NATURAL JOIN Publisher"
                    + " WHERE publisherName = ?");
            lastStmt.setString(1, updatedPublisherInput);
            ResultSet rs = lastStmt.executeQuery();
            
            int bookCount = 0;
            while (rs.next()) {
                
                //Retrieve data by column name and display values
                System.out.println("Book #" + (bookCount+1) + ": " + rs.getString("bookTitle") + ", "
                        + rs.getString("numberOfPages") + " pages. @" + rs.getString("yearPublished") + " "
                        + rs.getString("publisherName") + " (" + rs.getString("publisherAddress") + ", "
                        + rs.getString("publisherPhone") + ", " + rs.getString("publisherEmail") + ")");
                bookCount++;
            }
            
            System.out.println(); // Newline for whitespace
            

            // Close environments
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
        
        //Remove a book
	else {
            try{
                
             // Obtain book title data from Books to populate array list and display list of books on record
            ResultSet rs = stmt.executeQuery("SELECT bookTitle FROM Books");
            ArrayList bookNames = new ArrayList<String>();
            ArrayList publisherNames = new ArrayList<String>();
            ArrayList writingGroupNames = new ArrayList<String>();

            // Extract data from result set, display values, and populate array list
            System.out.println("Current books in our records: ");
            while(rs.next()){
                System.out.println(rs.getString("bookTitle"));
                bookNames.add(rs.getString("bookTitle"));
            }
            
            // User input on book title and input validation
            System.out.println("\nEnter a book's title to remove that book from our records: ");
            String bookToRemove = inputs.nextLine();
            while (!(bookNames.contains(bookToRemove))) {
                System.out.println("That book does not exist in our records. Please try again.");
                System.out.println("\nEnter a book's title to remove that book from our records: ");
                bookToRemove = inputs.nextLine();
            }
            
            // Prepared statement to obtain publisher and writing group data of selected book
            PreparedStatement myBookStmt = conn.prepareStatement("SELECT groupName, publisherName FROM Books WHERE"
                    + " bookTitle = ?");
            myBookStmt.setString(1, bookToRemove);
            rs = myBookStmt.executeQuery();
            
            // Extract data, display values, and populate array lists
            while (rs.next()) {
                System.out.println(bookToRemove + "'s writing group: ");
                System.out.println(rs.getString("groupName"));
                writingGroupNames.add(rs.getString("groupName"));
                
                System.out.println(bookToRemove + "'s publisher: ");
                System.out.println(rs.getString("publisherName") + "\n");
                publisherNames.add(rs.getString("publisherName"));
            }
            
            // User input for writing group or publisher name and input validation
            System.out.println("\nEnter the writing group or publisher name associated with that book: ");
            String WGPName = inputs.nextLine();
            while (!(publisherNames.contains(WGPName) || writingGroupNames.contains(WGPName))) {
                System.out.println("They have not worked with " + bookToRemove + ". Please try again.");
                System.out.println("\nEnter the writing group or publisher name associated with that book: ");
                WGPName = inputs.nextLine();
            }
            
            // Prepared statement to remove book given a book title and writing group or publisher name
            PreparedStatement myStmt = conn.prepareStatement("DELETE FROM Books WHERE bookTitle = ? AND"
                    + " (groupName = ? OR publisherName = ?)");
            myStmt.setString(1, bookToRemove);
            myStmt.setString(2, WGPName);
            myStmt.setString(3, WGPName);
            myStmt.executeUpdate();
            myStmt.close();

            System.out.println(); // Print new line to add white space before next menu print
            
            // Close environments
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
            MenuOptionSelected(MenuSelection, conn, stmt);
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