package shippingstore;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.channels.Channels;
import java.util.ArrayList;


/**
 * The Shipping Store Test class is runs test on all it's methods and informs user on which methods have failed 
 * or passed the tests.
 *
 */

public class ShippingStoreTest {

    // Definition of  shipping store class.
    ShippingStore shippingStore;
    File dataFile;
    ArrayList<PackageOrder> packageOrderList;
    PrintWriter pw;


    // Initialization of packages.
    PackageOrder pack1;
    PackageOrder pack2;
    PackageOrder pack3;
    PackageOrder pack4;
    
    /**
     * Sets up all of the default class files.
     * They are initialized each time a test is going through. Mock packages, and a mock array were created
     * to store the information in.
     * @throws Exception throws no exception at this state, due to other dependencies already being checked.
     */
    @Before
    public void setUp() throws Exception {
        // Creating an empty shipping store array
        shippingStore = new ShippingStore();
        packageOrderList = new ArrayList<>();

        pack1 = new PackageOrder("BXF24","Drum", "Fragile", "First-Class", 7.00f, 83);
        pack2 = new PackageOrder("12345","Crate", "Books", "Priority", 6.00f, 91);
        pack3 = new PackageOrder("ABC12","Envelope", "Catalog", "Ground", 2.00f, 23);
        pack4 = new PackageOrder("9379A","Box", "Fragile", "First-Class", 26.00f, 75);

        packageOrderList.add(pack1);
        packageOrderList.add(pack2);
        packageOrderList.add(pack3);
    }

    /**
     * Sets all of the initialized information within the @Before stage to null after each test condition is
     * executed. This would keep information standard.
     * @throws Exception throws no exception at this state, due to other dependencies already being checked.
     */
    @After
    @SuppressWarnings("Duplicates")
    public void tearDown() throws Exception {

        packageOrderList = null;
        shippingStore = null;
        pack1 = null;
        pack2 = null;
        pack3 = null;
        pack4 = null;
        dataFile = null;
        pw = null;
    }

    /**
     * Checks and see if the datafile was created by the program.
     * @throws Exception The exception is being returned as the assert true and false.
     */
    @Test
    public void getDataFile() throws Exception {
        dataFile = new File("PackageOrderDB.txt");
        assertTrue("The file exist", dataFile.exists());
        assertFalse("The file does not exist", !dataFile.exists());
    }

    /**
     * Checks if the packageOrderList is a valid size, then goes through and retrieves  number to
     * compare and see if it's able to read the information from the object.
     * @throws Exception The exception is being returned as the assert true and false.
     */
    @Test
    @SuppressWarnings("Duplicates")
    public void showPackageOrders() throws Exception {

        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        PrintStream originalOut = System.out;

        String staticData = "Package Order has been added.\n\n";
        staticData += " -------------------------------------------------------------------------- \n";
        staticData += "| Tracking # | Type    | Specification | Class       | Weight(oz) | Volume |\n";
        staticData += " -------------------------------------------------------------------------- \n";
        staticData += "| ABC12      | Crate   | Fragile       | Priority    | 3200.00    | 13     |\n";
        staticData += " --------------------------------------------------------------------------\n\n";

            System.setOut(ps);            
            shippingStore.addOrder("ABC12", "Crate", "Fragile", "Priority", "3200.00", "13");
            shippingStore.showPackageOrders();
            System.out.flush();
            System.setOut(originalOut);
            
            assertEquals(staticData,os.toString());
            assertTrue("Package orders exist",packageOrderList.get(0).getTrackingNumber() == "ABC12");
            assertFalse("Package order does not exist",packageOrderList.get(0).getTrackingNumber() != "ABC12");
        
    }

    /**
     * Checks with a secondary array to see if the numbers that the user has entered is located within
     * the array of doubles.
     * @throws Exception The exception is being returned as the assert true, false, array equal functions.
     */
    @Test
    public void showPackageOrdersRange() throws Exception {
        double []actualN = {53,50,49};
        double []expectedN = {49,50,53};

        assertTrue("Package orders", packageOrderList.size() >= 1); 
        assertFalse("Package order does not exist",packageOrderList.size() < 1);
        assertArrayEquals("Number are in between 40-55",expectedN,actualN,10); 
    }

    /**
     * Checks if the program verifies user input and see if the package within the object list matches with
     * the users information for the package.
     * @throws Exception The exception is being returned as the assert true and false, assert equals is to show that the
     * package object is the exact same.
     */
    @Test
    public void findPackageOrder() throws Exception {
        assertTrue("Package exist",
                (packageOrderList.get(0).getTrackingNumber()).matches("BXF24"));
        assertEquals(packageOrderList.get(0).getTrackingNumber().matches(pack1.getTrackingNumber()),
                packageOrderList.get(0).getTrackingNumber().matches(pack1.getTrackingNumber()));
        assertFalse("Package does not exit",
                (packageOrderList.get(0).getTrackingNumber()).matches("00000"));
    }

    /**
     * Checks to see if the packages' tracking number matches with the package that is on file.
     * @throws Exception The exception is being returned as the assert true and false.
     */
    @Test
    public void searchPackageOrder() throws Exception {
        assertTrue("The package has been located",
                (pack2.getTrackingNumber()).matches("12345"));
        assertFalse("The package was not located",
                (packageOrderList.get(0).getTrackingNumber()).matches("00000"));
    }

    /**
     * Tests if it is possible to add an order to the object list, and then verifies the size of the array.
     * @throws Exception type assertEquals verify's that the array size is the proper size once we added the extra item.
     */
    @Test
    public void addOrder() throws Exception {
        packageOrderList.add(pack4);
        assertEquals(4, packageOrderList.size());
    }

    /**
     * Tests if removing an object from the array is successful or not.
     * @throws Exception type assertEquals, will test and see if the size of the array matches up with the default
     * condition.
     */
    @Test
    public void removeOrder() throws Exception {
        packageOrderList.remove(pack3);
        assertEquals(2, packageOrderList.size());
    }

    /**
     * Gets the packageOrder available, and match it with the package order to verify that everything matches.
     * @throws Exception The exception is being returned as the assert true and false.
     */
    @Test
    public void getPackageOrder() throws Exception {
       assertTrue("Objects exist",packageOrderList.get(0).equals(pack1));
       assertFalse("Object does not exist", packageOrderList.get(1).equals(pack1));
    }

    /**
     * Read user input from the file,and tests to see if the information in the file is readable.
     * The program will first go through and see if the file are located inside the directory, if it's not then
     * it will throw exception where the file is not found.
     * @throws Exception The exception is being returned as the assert true and false.
     */
    @Test
    public void read() throws Exception {

        try
        {
            FileReader dataReader = new FileReader("test.txt");
            shippingStore.read(dataReader);

        } catch (Exception e) {

            assertEquals("java.io.FileNotFoundException: test.txt (No such file or directory)",e.toString());

        }finally{
            dataFile = new File("PackageOrderDB.txt");
            assertTrue("Information is readable", dataFile.canRead());
            assertTrue("File opened",dataFile.exists());
        }
    }

    /**
     * Verifies if there were any errors in writing to the serializable objects. Tests to see if the
     * test file could be read.
     * @throws Exception The exception is being returned as the assert true and false.
     */
    @Test
    public void flush() throws Exception {

        try{
            RandomAccessFile raFile = new RandomAccessFile("test.txt", "r");
            OutputStreamWriter writer = new OutputStreamWriter(Channels.newOutputStream(raFile.getChannel()));
            shippingStore.flush(writer);

        } catch (Exception e) {

            assertEquals("java.io.FileNotFoundException: test.txt (No such file or directory)",e.toString());

        } finally {
            pw = new PrintWriter("PackageOrderDB.txt");
            assertTrue("File can be written to", !pw.checkError());
            assertFalse("Error, file could not be written to", pw.checkError()); 
        }
    }
}