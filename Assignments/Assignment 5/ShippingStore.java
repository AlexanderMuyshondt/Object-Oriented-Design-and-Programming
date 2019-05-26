package shippingstore;

import java.io.*;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a package shippingstore software
 * Available operations:
 * 1. Show all existing packages in the database.
 * 2. Add a new package to the database.
 * 3. Delete a package from a database (given its tracking number).
 * 4. Search for a package (given its tracking number).
 * 5. Show list of users.
 * 6. Add a new user to the database.
 * 7. Update user info (given their id).
 * 8. Deliver a package.
 * 9. Show a list of transactions.
 * 10. Exit program.
 */
public class ShippingStore {

    private final List<Package> packageList;
    private final List<User> users;
    private final List<Transaction> transactions;

    protected int userIdCounter = 1;
    private final Scanner sc;
     
    /**
     * Default constructor. Initializes the package list, users, and transactions
     */
    public ShippingStore() {
        this.packageList = new ArrayList<Package>();
        this.users = new ArrayList<User>();
        this.transactions = new ArrayList<Transaction>();
        this.sc = new Scanner(System.in);
    }

    /**
     * Constructor. Initializes the package list, users, and transactions to
     * given values.
     *
     * @param packageList List of packages
     * @param users List of Users
     * @param transactions List of Transactions
     */
    public ShippingStore(List<Package> packageList, List<User> users, List<Transaction> transactions) {
        this.packageList = packageList;
        this.users = users;
        this.transactions = transactions;
        this.sc = new Scanner(System.in);
    }

    /**
     * setUserIdCounter()
     * 
     * @param Counter
     */
    public void setUserIdCounter(int Counter) {
        this.userIdCounter = Counter;
    }
    
    /**
     * Auxiliary method used to find a package in the database, given its
     * tracking number.
     *
     * @param ptn
     * @return The package found, or otherwise null.
     */
    public Package findPackage(String ptn) {
        for (Package p : packageList) {
            if (p.getPtn().equals(ptn)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns true if the package exists in the database.
     * @param ptn
     * @return
     */
    public boolean packageExists(String ptn) {
        if (findPackage(ptn) != null)
            return true;
        return false;
    }
    
    /**
     * packagePosition() is a helper method for the GUI to return the Package 
     * object at the position i
     * @param i an int
     * @return packageList.get(i), a Package object
     */
    public Package packagePosition(int i) {
        return packageList.get(i);
    }

    /**
     * addEnvelope()
     * 
     * @param ptn
     * @param type
     * @param specification
     * @param mailingClass
     * @param weight
     * @param volume
     * @param height
     * @param width
     */
    public void addEnvelope(String ptn, String type, String specification, String mailingClass, 
            float weight, int volume, int height, int width) {
        Envelope env = new Envelope(ptn, type, specification, mailingClass, weight, 
            volume, height, width);
        packageList.add(env);
    }
    
    /**
     * addBox()
     * 
     * @param ptn
     * @param type
     * @param specification
     * @param mailingClass
     * @param weight
     * @param dimension
     * @param volume
     */
    public void addBox(String ptn, String type, String specification, String mailingClass, 
            float weight, int volume, int dimension) {
        Box box = new Box(ptn, type, specification, mailingClass, weight, 
            volume, dimension);
        packageList.add(box);
    }
    
    /**
     * addCrate()
     * 
     * @param ptn
     * @param type
     * @param specification
     * @param mailingClass
     * @param weight
     * @param volume
     * @param dimension
     * @param loadWeight
     * @param content
     */
    public void addCrate(String ptn, String type, String specification, String mailingClass, 
            float weight, int volume, float loadWeight, String content) {
        Crate crate = new Crate(ptn, type, specification, mailingClass, weight, 
            volume, loadWeight, content);
        packageList.add(crate);
    }
    
    /**
     * addDrum()
     * 
     * @param ptn
     * @param type
     * @param specification
     * @param mailingClass
     * @param weight
     * @param volume
     * @param material
     * @param diameter
     */
    public void addDrum(String ptn, String type, String specification, String mailingClass, 
            float weight, int volume, String material, float diameter) {
        Drum drum = new Drum(ptn, type, specification, mailingClass, weight, volume, material, diameter);
        packageList.add(drum);
    }
    
    /**
     * addOrder()
     * 
     * @param ptn
     * @param type
     * @param specification
     * @param mailingClass
     * @param weight
     * @param volume
     */
    public void addOrder(String ptn, String type, String specification, String mailingClass, 
            float weight, int volume) {
        Package package1 = new Package(ptn, type, specification, mailingClass, weight, volume);
        packageList.add(package1);
    }
    

    /**
     * This method allows the user to delete a package from the inventory
     * database given its tracking number.
     * @param ptn The package tracking number
     * @return True if the package was found and was deleted. False otherwise.
     */
    public boolean deletePackage(String ptn) {
        
        for (Package p : packageList) {
            if (p.getPtn().equals(ptn)) {
                packageList.remove(p);
                return true;
            }
        }
        return false;
    }
    

    /**
     * Auxiliary private method to return a list of packages in a formatted
     * manner.
     */
    private String getFormattedPackageList(List<Package> packages) {
        String text = "---------------------------------------------------"
                + "----------------------------------------------------------\n";
        text += String.format("| %12s | %12s | %13s | %13s | %22s                       |%n",
                "PACKAGE TYPE", "TRACKING #", "SPECIFICATION", "MAILING CLASS", "OTHER DETAILS");
        text +=  "---------------------------------------------------"
                + "----------------------------------------------------------\n";
        for (Package p : packages) {text += p.getFormattedText();
        }
        text += "---------------------------------------------------"
                + "----------------------------------------------------------\n";
        
        return text;
    }

    /**
     * This method return all the packages currently in the inventory, in a
     * formatted manner.
     * @return 
     */
    public String getAllPackagesFormatted() {
        return getFormattedPackageList(packageList);
    }
    
    /**
     * getPackageFormatted()
     * 
     * @param ptn
     * @return
     */
    public String getPackageFormatted(String ptn) {
        ArrayList<Package> matchingPackage = new ArrayList<Package>(1);
        matchingPackage.add(findPackage(ptn));
        
        return getFormattedPackageList(matchingPackage);
    }
    
    /**
     * getListSize() method returns the current size of packageList
     * @return packageList.size(), an int
     */
    public int getListSize() {
        return packageList.size();
    }
    
    /**
     * basicSearch() 
     * @return packageList.size(), an int
     */
    public int basicSearch(String search) {
        int x = 0;

        for (Package i : packageList) {
            if (i.getPtn().equalsIgnoreCase(search)) {
                return x;
            }
            ++x;
        }
        return packageList.size();
    }
    
    /**
     * addUserDirectly() is a helper method to the GUI and adds a User obj directly to users
     * @param obj of User datatype
     * @return true if users.add(obj) is successful, false otherwise
     */
    public boolean addUserDirectly(User obj) {
        return users.add(obj);
    }
    
    /**
     * addPackageDirectly() accepts a Package object 
     * @return boolean, packageList.add(obj)
     */
    public boolean addPackageDirectly(Package obj) {
        return packageList.add(obj);
    }
    
    /**
     * addCustomer()
     * 
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @param address
     */
    public void addCustomer(String firstName, String lastName, String phoneNumber, String address) {
        users.add(new Customer(userIdCounter++, firstName, lastName, phoneNumber, address));
    }
    
    /**
     * addEmployee()
     * 
     * @param firstName
     * @param lastName
     * @param ssn
     * @param monthlySalary
     * @param bankAccNumber
     */
    public void addEmployee(String firstName, String lastName, int ssn, float monthlySalary, int bankAccNumber) {
        users.add(new Employee(userIdCounter++, firstName, lastName, ssn, monthlySalary, bankAccNumber));
    }
    
    /**
     * Auxiliary private method to return a list of users in a formatted
     * manner.
     */
    private String getFormattedUserList(List<User> users) {
        String text ="---------------------------------------------------"
                + "------------------------------------------------"
                + "---------------\n";
        text += String.format("| %10s | %9s | %12s | %12s | %35s                    | %n",
                "USER TYPE", "USER ID", "FIST NAME", "LAST NAME", "OTHER DETAILS");
        text += "---------------------------------------------------"
                + "-----------------------------------------------"
                + "---------------\n";
        for (User u : users) {
            text += u.getFormattedText();
        }
        text += "---------------------------------------------------"
                + "-----------------------------------------------"
                + "---------------\n";
        
        return text;
    }

    /**
     * Returns a string list of all users in the database in a formatted manner.
     * @return a formatted string of all the users in the database.
     */
    public String getAllUsersFormatted() {
        return getFormattedUserList(users);
    }
    
    /**
     * userExists()
     * 
     * @param userID
     * @return
     */
    public boolean userExists(int userID) {
        if (findUser(userID) != null)
            return true;
        
        return false;
    }
    
    /**
     * findUser()
     * 
     * @param userID
     * @return
     */
    public User findUser(int userID) {
        User user = null;
        
        for (User u : users) {
            if (u.getId() == userID) {
                user = u;
            }
        }
        
        return user;
    }
    
    /**
     *
     * @param userID
     * @return
     */
    public boolean isCustomer(int userID) {
        User user = findUser(userID);
        if (user instanceof Customer)
            return true;
        return false;
    }
    
    /**
     *
     * @param userID
     * @return
     */
    public boolean isEmployee(int userID) {
        User user = findUser(userID);
        if (user instanceof Employee)
            return true;
        return false;
    }
    
    /**
     *
     * @param userID
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @param address
     */
    public void updateCustomer(int userID, String firstName, String lastName,
            String phoneNumber, String address) {
        Customer customer = (Customer) findUser(userID);
        if (customer == null) {
            System.err.println("Customer not found!");
            return;
        }
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
    }
    
    /**
     *
     * @param userID
     * @param firstName
     * @param lastName
     * @param ssn
     * @param monthlySalary
     * @param bankAccNumber
     */
    public void updateEmployee(int userID, String firstName, String lastName,
            int ssn, float monthlySalary, int bankAccNumber) {
        Employee employee = (Employee) findUser(userID);
        if (employee == null) {
            System.err.println("Employee not found!");
            return;
        }
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setSocialSecurityNumber(ssn);
        employee.setMonthlySalary(monthlySalary);
        employee.setBankAccountNumber(bankAccNumber);
    }

    /**
     *
     * @param customerId
     * @param employeeId
     * @param ptn
     * @param shippingDate
     * @param deliveryDate
     * @param price
     */
    public void addShppingTransaction(int customerId, int employeeId, String ptn,
                       Date shippingDate, Date deliveryDate, float price) {
        Transaction trans = new Transaction(customerId, employeeId, ptn, shippingDate, deliveryDate, price);
        transactions.add(trans);
    }
    
    /**
     * addTransactionGUI is a helper method of the GUI to add a transaction to the list 
     * @param obj transaction
     * @return transactions.add(obj);
     */
    public boolean addTransactionGUI(Transaction obj) {
        return transactions.add(obj);
    }
    
    /**
     * getSaleTransactionSize returns the size of the ArrayList of SaleTransactions
     * @return transactions.size(), an int
     */
    public int getSaleTransactionSize() {
        return transactions.size();
    }
    
    /**
     * getTransactionAtPosition() returns the SaleTransaction object at position i
     * @param i, an int
     * @return transactions.users.get(i), a SaleTransaction object
     */
    public Transaction getTransactionAtPosition(int i) {
        return transactions.get(i);
    }
    
    /**
     * deletePackageByStr() removes a Package object based upon the common search 
     * String passed. Only removes a Package object if there is a match, returns false otherwise
     * @param search
     * @return true if search matched a PTN, false otherwise
     */
    public boolean deletePackageByStr(String search) {
        for (Package i : packageList) {
            if (i.getPtn().equalsIgnoreCase(search)) {
                packageList.remove(i);
                return true;
            }
        }
        return false;
    }
    

    /**
     * Return a list of all recorded transactions.
     * 
     * @return transactions
     */
    public String getAllTransactionsText() {
        String transText = "";
        for (Transaction trans : transactions) {
            transText += trans.toString();
        }
        return transText;
    }


    /**
     * This method is used to read the database from a file, serializable
     * objects.
     *
     * @return A new ShippingStore object.
     */
    @SuppressWarnings("unchecked") // This will prevent Java unchecked operation warning when
    // convering from serialized Object to Arraylist<>
    public static ShippingStore readDatabase() {
        System.out.print("Reading database...");
        
        File dataFile = new File("ShippingStore.ser");
        
        ShippingStore ss = null;

        // Try to read existing dealership database from a file
        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;
        try {
            if (!dataFile.exists()) {
                System.out.println("Data file does not exist. Creating a new database.");
                ss = new ShippingStore();
                return ss;
            }
            file = new FileInputStream(dataFile);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            // Read serilized data
            List<Package> packageList = (ArrayList<Package>) input.readObject();
            List<User> users = (ArrayList<User>) input.readObject();
            List<Transaction> transactions = (ArrayList<Transaction>) input.readObject();
            ss = new ShippingStore(packageList, users, transactions);
            ss.userIdCounter = input.readInt();

            input.close();
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.toString());
        } catch (FileNotFoundException ex) {
            System.err.println("Database file not found.");
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            close(file);
        }
        System.out.println("Done.");

        return ss;
    }

    /**
     * This method is used to save the Dealership database as a serializable
     * object.
     */
    public void writeDatabase() {
        System.out.print("Writing database...");
        //serialize the database
        OutputStream file = null;
        OutputStream buffer = null;
        ObjectOutput output = null;
        try {
            file = new FileOutputStream("ShippingStore.ser");
            buffer = new BufferedOutputStream(file);
            output = new ObjectOutputStream(buffer);

            output.writeObject(packageList);
            output.writeObject(users);
            output.writeObject(transactions);
            output.writeInt(userIdCounter);

            output.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            close(file);
        }
        System.out.println("Done.");
    }
    
    public boolean packageMatch(String ptn) {
        for (Package i : packageList) {
            if (i.getPtn().equalsIgnoreCase(ptn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Auxiliary convenience method used to close a file and handle possible
     * exceptions that may occur.
     *
     * @param c
     */
    public static void close(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
    * This method serves as the main interface between the program and the user.
    * The method interacts with the user by printing out a set of options, and
    * asking the user to select one.
    */
   public void runSoftware() {
       int choice = 0;
       boolean exitProgram = false;
       do {
           printMenu();
           try {
               choice = sc.nextInt();

               switch (choice) {
                   case 1: showAllPackages(); break;
                   case 2: addNewPackage(); break;
                   case 3: deletePackage(); break;
                   case 4: searchPackage(); break;
                   case 5: showAllUsers(); break;
                   case 6: addNewUser(); break;
                   case 7: updateUser(); break;
                   case 8: deliverPackage(); break;
                   case 9: showAllTransactions(); break;
                   case 10: writeDatabase(); exitProgram = true; break; 
                   default: System.err.println("Please select a number between 1 and 10.");
               }
           } catch (InputMismatchException ex) {
               System.err.println("Input missmatch. Please Try again.");
               continue;
           } catch (BadInputException ex) {
               System.err.println("Bad input. "+ex.getMessage());
               System.err.println("Please try again.");
               continue;
           }
       } while (!exitProgram);
   }

   /**
    * Auxiliary method that prints out the operations menu.
    */
   private static void printMenu() {
       System.out.println(
               "\n 1. Show all existing packages in the database.\n" +
               " 2. Add a new package to the database. \n" +
               " 3. Delete a package from a database (given its tracking number).\n" +
               " 4. Search for a package (given its tracking number).\n" +
               " 5. Show list of users.\n" +
               " 6. Add a new user to the database.\n" +
               " 7. Update user info (given their id).\n" +
               " 8. Deliver a package.\n" +
               " 9. Show a list of transactions.\n" +
               "10. Exit program.\n");
   }
   
   
   /**
     * This method allows the user to enter a new package to the list
     * database.
     * @throws shippingstore.BadInputException bad input
     */
    public void addNewPackage() throws BadInputException {

        System.out.println("\nEnter tracking number (string): ");
        String ptn = sc.nextLine();
        if (ptn.length() > 5) {
            throw new BadInputException("Tracking number should not be more that 5 characters long.");
        }

        if (packageExists(ptn)) {
            System.out.println("\nPackage with the same tracking number exists, try again");
            return;
        }
        
        System.out.println("\nEnter package type (PostCard, Letter, Envelope, Packet, Box,"
                + "Crate, Drum, Roll, Tube): ");
        String type = sc.nextLine();
        boolean corr = false;
        
        corr = type.equalsIgnoreCase("PostCard") || type.equalsIgnoreCase("Letter") 
                || type.equalsIgnoreCase("Envelope") || type.equalsIgnoreCase("Packet")
                || type.equalsIgnoreCase("Box") || type.equalsIgnoreCase("Crate");
        corr = corr || type.equalsIgnoreCase("Roll") || type.equalsIgnoreCase("Tube")
                || type.equalsIgnoreCase("Drum");
        if (!(corr)) {
            throw new BadInputException("Type must be one of following: "
                + "Postcard, Letter, Envelope, Packet, Box, Crate, Drum, Roll, Tube.");
        }

        System.out.println("\nEnter Specification: Fragile, Books, Catalogs, Do-not-bend, or N/A");
        String specification = sc.nextLine();
        boolean correct = false;

        correct = specification.equalsIgnoreCase("Fragile") || specification.equalsIgnoreCase("Books") || specification.equalsIgnoreCase("Catalogs");
        correct = correct || specification.equalsIgnoreCase("Do-not-bend") || specification.equalsIgnoreCase("N/A");

        if (!(correct)) {
            throw new BadInputException("Specifications can only be one of the following: Fragile, Books, Catalogs, Do-not-bend, or N/A");
        }

        System.out.println("\nEnter mailing class can be First-Class, Priority, Retail, Ground, or Metro.");
        String mailingClass = sc.nextLine();

        correct = mailingClass.equalsIgnoreCase("First-Class") || mailingClass.equalsIgnoreCase("Priority") || mailingClass.equalsIgnoreCase("Retail");
        correct = correct || mailingClass.equalsIgnoreCase("Ground") || mailingClass.equalsIgnoreCase("Metro");
        if (!(correct)) {
            throw new BadInputException("Specifications can only be one of the following: Fragile, Books, Catalogs, Do-not-bend, or N/A");
        }
        
        System.out.println("\nEnter maximum load weight (lb), (float): ");

            float weight = 0.0f;
            if (sc.hasNextFloat()) {
                weight = sc.nextFloat();
                sc.nextLine();
                if (weight < 0.0f) {
                    throw new BadInputException("Maximum load weight of Crate cannot be negative.");
                }
            } else {
                sc.nextLine();
                throw new BadInputException("Max load should be float");
            }
            
        System.out.println("\nEnter volume (inch^3), (int): ");

            int volume = 0;
            if (sc.hasNextInt()) {
                volume = sc.nextInt();
                sc.nextLine();
                if (volume < 0) {
                    throw new BadInputException("Volume of Box cannot be negative.");
                }
            } else {
                sc.nextLine();
                throw new BadInputException("Volume should be integer.");
            }

        if (type.equals("Envelope")) {
            System.out.println("\nEnter height (inch), (int): ");
            int height = 0;
            if (sc.hasNextInt()) {
                height = sc.nextInt();
                sc.nextLine();
                if (height < 0) {
                    throw new BadInputException("Height of Envelope cannot be negative.");
                }
            } else {
                sc.nextLine();
                throw new BadInputException("Height of Envelope is integer.");
            }

            int width = 0;
            System.out.println("\nEnter width (inch), (int): ");
            if (sc.hasNextInt()) {
                width = sc.nextInt();
                sc.nextLine();
                if (width < 0) {
                    throw new BadInputException("Width of Envelope cannot be negative.");
                }
            } else {
                sc.nextLine();
                throw new BadInputException("Width of Envelope is integer.");
            }

            addEnvelope(ptn, type, specification, mailingClass, weight, volume, height, width);

        } else if (type.equals("Box")) {
            System.out.println("\nEnter largest dimension (inch), (int): ");

            int dimension = 0;
            if (sc.hasNextInt()) {
                dimension = sc.nextInt();
                sc.nextLine();
                if (dimension < 0) {
                    throw new BadInputException("Largest dimension of Box cannot be negative.");
                }
            } else {
                sc.nextLine();
                throw new BadInputException("Dimension should be integer.");
            }

            addBox(ptn, type, specification, mailingClass, weight, volume, dimension);

        } else if (type.equals("Crate")) {
            System.out.println("\nEnter maximum load weight (lb), (float): ");

            float loadWeight = 0.0f;
            if (sc.hasNextFloat()) {
                loadWeight = sc.nextFloat();
                sc.nextLine();
                if (loadWeight < 0.0f) {
                    throw new BadInputException("Maximum load weight of Crate cannot be negative.");
                }
            } else {
                sc.nextLine();
                throw new BadInputException("Max load should be float");
            }

            System.out.println("\nEnter content (string): ");
            String content = sc.nextLine();

            addCrate(ptn, type, specification, mailingClass, weight, volume, loadWeight, content);
           
        } else if (type.equals("Drum")) {

            System.out.println("\nEnter material (Plastic / Fiber): ");
            String material = sc.nextLine();
            if (!(material.equalsIgnoreCase("Plastic") || material.equalsIgnoreCase("Fiber"))) {
                throw new BadInputException("Material of Drum can only be plastic or fiber.");
            }

            float diameter = 0.0f;
            System.out.println("\nEnter diameter (float): ");
            if (sc.hasNextFloat()) {
                diameter = sc.nextFloat();
                sc.nextLine();
                if (diameter < 0.0f) {
                    throw new BadInputException("Diameter of Drum cannot be negative.");
                }
            } else {
                sc.nextLine();
                throw new BadInputException("Diameter should be float");
            }

            addDrum(ptn, type, specification, mailingClass, weight, 
            volume, material, diameter);
            
        } else {
            addOrder(ptn, type, specification, mailingClass, weight, volume);
        }
    }
    
    /**
     * This method prints out all the package currently in the inventory, in a
     * formatted manner.
     */
    public void showAllPackages() {
        System.out.println(getAllPackagesFormatted());
    }
    
    /**
     * getUserDatabaseSize() is a helper function for the GUI, returns the users.size()
     * @return an int, users.size()
     */
    public int getUserDatabaseSize() {
        return users.size();
    }
    
    
    /**
     * getUserAtPosition() returns the User object at position i. It is guaranteed 
     * that i will not be greater than, less than, or equal to size of users.
     * @param i, the position at which is requested
     * @return a User object, users.get(i)
     */
    public User getUserAtPosition(int i) {
        return users.get(i);
    }
    
    /**
     * This method allows the user to delete a package from the inventory
     * database.
     */
    public void deletePackage() {
        sc.nextLine();
        System.out.print("\nEnter tracking number of pacakge to delete (string): ");
        String ptn = sc.nextLine();

        if (deletePackage(ptn)) 
            System.out.println("Package deleted.");
        else 
            System.out.println("Package with given tracking number not found in the database.");
    }
    
    /**
     * This method allows the users to search for a package given its tracking number
     * and then it prints details about the package.
     */
    public void searchPackage() {
        sc.nextLine();
        System.out.print("\nEnter tracking number of package to search for (string): ");
        String ptn = sc.nextLine();

        if (packageExists(ptn))
            System.out.println(getPackageFormatted(ptn));
        else
            System.out.println("Package with PTN " + ptn + " not found in the database");
    }
    
    /**
     * Prints out a list of all users in the database.
     */
    public void showAllUsers() {
        System.out.println(getAllUsersFormatted());
    }
    
    /**
     * This method allows a new user to be added to the database.
     *
     */
    public void addNewUser() {
        boolean success;
        // Add fields for new user
        int userType = 0;
        boolean check = false;

        while (!check) {
            System.out.println("Select user type:\n"
                    + "1. Customer\n"
                    + "2. Employee");

            if (sc.hasNextInt()) {
                userType = sc.nextInt();

                if (userType < 1 || userType > 2) {
                    System.out.println("Wrong integer value: choose 1 or 2");
                } else {
                    check = true;
                }
            } else {
                System.out.println("Please select 1 or 2");
            }
        }

        sc.nextLine();
        System.out.println("\nEnter first name (string): ");
        String firstName = sc.nextLine();

        System.out.println("\nEnter last name (string): ");
        String lastName = sc.nextLine();

        if (userType == 1) {
            System.out.println("\nEnter phone number (string): ");
            String phoneNumber = sc.nextLine();

            System.out.println("\nEnter address (string): ");
            String address = sc.nextLine();

            addCustomer(firstName, lastName, phoneNumber, address);

        } else if (userType == 2) {

            check = false;
            float monthlySalary = 0.0f;

            while (!check) {

                System.out.println("\nEnter monthly salary (float): ");

                if (sc.hasNextFloat()) {
                    monthlySalary = sc.nextFloat();
                    if (monthlySalary < 0.0f) {
                        System.out.println("Monthly salary cannot be negative.");
                    } else {
                        check = true;
                    }
                    sc.nextLine();

                } else {
                    System.out.println("Please enter monthly salary as a non-zero float value.");
                    sc.nextLine();
                }
            }

            int ssn = 0;
            check = false;
            while (!check) {

                System.out.println("\nEnter SSN (9-digital int): ");
                if (sc.hasNextInt()) {
                    ssn = sc.nextInt();
                    if (String.valueOf(ssn).length() != 9) {
                        System.out.println("\nThat is not a nine digit number");
                    } else if (ssn < 10000000 || ssn > 999999999) {
                        System.out.println("\nGive a correct 9 digit integer");
                    } else {
                        check = true;
                    }
                    sc.nextLine();
                } else {
                    System.out.println("\nNot a number!");
                    sc.nextLine();
                } //end if
            }// end while

            check = false;
            int bankAccNumber = 0;
            while (!check) {
                System.out.println("\nEnter bank account number (int): ");
                if (sc.hasNextInt()) {
                    bankAccNumber = sc.nextInt();
                    if (bankAccNumber < 0) {
                        System.out.println("\nBank account cannot be negative");
                    } else {
                        check = true;
                    }
                    sc.nextLine();
                } else {
                    System.out.println("Invalid bank Account format, please try again");
                    sc.nextLine();
                }

            }//end while

            addEmployee(firstName, lastName, ssn, monthlySalary, bankAccNumber);
        } else {
            System.out.println("Unknown user type. Please try again.");
        }

    }
    
    /**
     * This method can be used to update a user's information, given their user
     * ID.
     *
     * @throws shippingstore.BadInputException
     */
    public void updateUser() throws BadInputException {
        boolean check = false;
        System.out.print("\nEnter user ID: ");
        int userID = sc.nextInt();

        if (!userExists(userID)) {
            System.out.println("User not found.");
            return;
        }

        sc.nextLine();
        System.out.print("\nEnter first name (string): ");
        String firstName = sc.nextLine();

        System.out.print("\nEnter last name (string): ");
        String lastName = sc.nextLine();

        if (isCustomer(userID)) {
            System.out.print("\nEnter phone number (string): ");
            String phoneNumber = sc.nextLine();
            System.out.print("\nEnter address (string): ");
            String address = sc.nextLine();
            
            updateCustomer(userID, firstName, lastName, phoneNumber, address);

        } else { //User is an employee

            float monthlySalary = 0.0f;
            check = false;
            while (!check) {

                System.out.println("\nEnter monthly salary (float): ");

                if (sc.hasNextFloat()) {
                    monthlySalary = sc.nextFloat();
                    if (monthlySalary < 0.0f) {
                        new BadInputException("Monthly salary cannot be negative.");
                    } else {
                        check = true;
                    }
                    sc.nextLine();
                } else {
                    System.out.println("Please enter monthly salary as a non-zero float value.");
                    sc.nextLine();
                }
            }

            int ssn = 0;
            check = false;
            while (!check) {

                System.out.println("\nEnter SSN (9-digital int): ");
                if (sc.hasNextInt()) {
                    ssn = sc.nextInt();
                    if (String.valueOf(ssn).length() != 9) {
                        new BadInputException("\nThat is not a nine digit number");
                    } else if (ssn < 10000000 || ssn > 999999999) {
                        new BadInputException("\nGive a correct 9 digit integer");

                    } else {
                        check = true;
                    }
                } //end if
                sc.nextLine();

            }// end while

            int bankAccNumber = 0;
            check = false;
            while (!check) {
                System.out.println("\nEnter bank account number (int): ");

                if (sc.hasNextInt()) {
                    bankAccNumber = sc.nextInt();
                    if (bankAccNumber < 0) {
                        new BadInputException("Bank account cannot be negative");
                    } else {
                        check = true;
                    }
                    sc.nextLine();
                } else {
                    System.out.println("Invalid bank Account format, please try again");
                    sc.nextLine();
                }
            } //end while

            updateEmployee(userID, firstName, lastName, ssn, monthlySalary, bankAccNumber);
        }
    }
    
    /**
     * This method is used to complete a package shipping/delivery transaction.
     *
     * @throws shippingstore.BadInputException
     */
    public void deliverPackage() throws BadInputException {

        Date currentDate = new Date(System.currentTimeMillis());

        sc.nextLine();
        System.out.println("\nEnter customer ID (int): ");
        int customerId = sc.nextInt();
        //Check that the customer exists in database
        boolean customerExists = userExists(customerId);

        if (!customerExists) {
            System.out.println("\nThe customer ID you have entered does not exist in the database.\n"
                    + "Please add the customer to the database first and then try again.");
            return;
        }

        System.out.println("\nEnter employee ID (int): ");

        int employeeId = 0;
        if (sc.hasNextInt()) {
            employeeId = sc.nextInt();
        }
        //Check that the employee exists in database
        boolean employeeExists = userExists(employeeId);

        if (!employeeExists) {
            System.out.println("\nThe employee ID you have entered does not exist in the database.\n"
                    + "Please add the employee to the database first and then try again.");
            return;
        }

        sc.nextLine();
        System.out.println("\nEnter tracking number (string): ");
        String ptn = sc.nextLine();

        //Check that the package exists in database
        if (!packageExists(ptn)) {
            System.out.println("\nThe package with the tracking number you are trying to deliver "
                    + "does not exist in the database. Aborting transaction.");
            return;
        }

        System.out.println("\nEnter price (float): ");
        float price = sc.nextFloat();
        if (price < 0.0f) {
            throw new BadInputException("Price cannot be negative.");
        }

        addShppingTransaction(customerId, employeeId, ptn, currentDate, currentDate, price);
        deletePackage(ptn);

        System.out.println("\nTransaction Completed!");
    }
    
    
    /**
     * Prints out a list of all recorded transactions.
     */
    public void showAllTransactions() {
        System.out.println(getAllTransactionsText());
    }

}
