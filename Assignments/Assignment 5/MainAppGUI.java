package shippingstore;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * MainAppGUI class for the graphical user interface (GUI) of shippingstore. 
 * Class extends JFrame implementing Swing & AWT libraries.
 *
 * @version 1.0
 * @version November 21, 2017 (1.0)
 * Main access point
 */
public class MainAppGUI extends JFrame {
    
    private static ShippingStore clone = ShippingStore.readDatabase(), ss = (clone == null) ?
            new ShippingStore() : clone;
    private static final Logger logger = Logger.getLogger(MainAppGUI.class.getName());
    private static FileHandler fh;
    
    final static String BOXPANEL = "BOX";
    final static String CRATEPANEL = "CRATE";
    final static String DRUMPANEL = "DRUM";
    final static String ENVELOPEPANEL = "ENVELOPE";
    final static String PACKAGEPANEL = "PACKAGE";
    final static String EMPLOYEEPANEL = "EMPLOYEE";
    final static String CUSTOMERPANEL = "CUSTOMER";
    private final static int extraWindowWidth = 100;   

    private JButton exitFrom,
                    exitFrom2,
                    exitFrom3, 
                    exitFrom4, 
                    exitFrom5;
    
    /**
     * Constructor
     */
    public MainAppGUI() {
    }

    /**
     * Class CustomFormatter overrides the format method of the superclass Formatter in order to format the
     * Logger output when writing to a file to a readable format (as opposed to the default XML format)
     */
    class CustomFormatter extends Formatter {
        private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        /**
         * format() method overrides the superclass's method in order to obtain a much simpler output
         * @param record refers to the LogRecord to change
         * @return builder.toString(), the String which will be the new format for each log event
         */
        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append(df.format(new Date(record.getMillis()))).append(" - ");
            builder.append("[").append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append("] :");
            builder.append("\n");
            builder.append("\t[").append(record.getLevel()).append("] - ");
            builder.append(formatMessage(record));
            builder.append("\n");
            return builder.toString();
        }

        public String getHead (Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }
    }

    /**
     * initLogger() method initializes the Logger environment for the class upon call
     */
    private void initLogger() {
        logger.setUseParentHandlers(false);
        CustomFormatter formatter = new CustomFormatter();
        try {
            fh = new FileHandler("log.txt");
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "FileHandler threw IOException", e);
        }
    }    
    
    /**
     * MainAppGUI represents the main menu of the GUI, allows user to complete operations of a
     * shippingstore
     * @param title, sets the title of the JFrame containing the main menu
     */
    private MainAppGUI (String title) {
        super(title);
        initLogger();
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(10, 0));
        JLabel intro = new JLabel("Please select an option below:");
        JTextField field = new JTextField(35);
        intro.setHorizontalAlignment(SwingConstants.CENTER);
        field.setText("STATUS: waiting for user input ...");

        // Create buttons to be used 
        JButton exist = new JButton("Show all existing packages in the database.");
        JButton addPackage = new JButton("Add a new package to the database.");
        JButton delPackage = new JButton("Delete a package from a database (given its tracking number).");
        JButton searchPackage = new JButton("Search for a package (given its tracking number).");
        JButton listUsers = new JButton("Show list of users.");
        JButton addUser = new JButton("Add a new user to the database.");
        JButton upUser = new JButton("Update user info (given their id).");
        JButton deliver = new JButton("Deliver a package.");
        JButton showTransac = new JButton("Show a list of transactions.");
        JButton exit = new JButton("Exit program.");
        
        // Align buttons
        exist.setHorizontalAlignment(SwingConstants.CENTER);
        addPackage.setHorizontalAlignment(SwingConstants.CENTER);
        delPackage.setHorizontalAlignment(SwingConstants.CENTER);
        searchPackage.setHorizontalAlignment(SwingConstants.CENTER);
        listUsers.setHorizontalAlignment(SwingConstants.CENTER);
        addUser.setHorizontalAlignment(SwingConstants.CENTER);
        upUser.setHorizontalAlignment(SwingConstants.CENTER);
        deliver.setHorizontalAlignment(SwingConstants.CENTER);
        showTransac.setHorizontalAlignment(SwingConstants.CENTER);
        exit.setHorizontalAlignment(SwingConstants.CENTER);

        // Add action listeners for buttons
        exist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Show all existing packages in the database. button pressed.");
                field.setText("STATUS: displaying all packages ...");
                Thread qThread = new Thread() {
                    public void run() {
                        showAllPackages();
                    }
                };
                qThread.start();
            }
        });

        addPackage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Add a new package to the database. button pressed.");
                field.setText("STATUS: in adding package ...");
                Thread qThread = new Thread() {
                    public void run() {
                        addPackage();
                    }
                };
                qThread.start();
            }
        });

        delPackage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Delete a package from a database (given its tracking number). button pressed.");
                field.setText("STATUS: in deleting package ...");
                Thread qThread = new Thread() {
                    public void run() {
                        deletePackageGUI();
                    }
                };
                qThread.start();
            }
        });

        searchPackage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Search for a package (given its tracking number). button pressed.");
                field.setText("STATUS: in searching inventory ...");
                Thread qThread = new Thread() {
                    public void run() {
                        searchPackageGUI();
                    }
                };
                qThread.start();
            }
        });
        
        listUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Show list of users.");
                field.setText("STATUS: in show list of users ...");
                Thread qThread = new Thread() {
                    public void run() {
                        displayUsers();
                    }
                };
                qThread.start();
            }
        });

        addUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Add a new user to the database. button pressed.");
                field.setText("STATUS: in add user ...");
                Thread qThread = new Thread() {
                    public void run() {
                        addUserGUI();
                    }
                };
                qThread.start();
            }
        });

        upUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Update user info (given their id). button pressed.");
                field.setText("STATUS: updating user ...");
                Thread qThread = new Thread() {
                    public void run() {
                        updateUserGUI();
                    }
                };
                qThread.start();
            }
        });

        deliver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Deliver a package. button pressed.");
                field.setText("STATUS: delivering a package ...");
                Thread qThread = new Thread() {
                    public void run() {
                        deliverPackageGUI();
                    }
                };
                qThread.start();
            }
        });

        showTransac.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Show a list of transactions. button pressed.");
                field.setText("STATUS: showing transactions ...");
                Thread qThread = new Thread() {
                    public void run() {
                        listTransactionsGUI();
                    }
                };
                qThread.start();
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "Exit program button pressed.");
                Thread qThread = new Thread() {
                    public void run() {
                        terminateSession();
                    }
                };
                qThread.start();
                field.setText("STATUS: Saving session . . . Goodbye!");
            }
        }); 
        
        this.setContentPane(mainPanel);
        mainPanel.add(intro, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(field, BorderLayout.SOUTH);
        buttonPanel.add(exist);
        buttonPanel.add(addPackage);
        buttonPanel.add(delPackage);
        buttonPanel.add(searchPackage);
        buttonPanel.add(listUsers);
        buttonPanel.add(addUser);
        buttonPanel.add(upUser);
        buttonPanel.add(deliver);
        buttonPanel.add(showTransac);
        buttonPanel.add(exit);   
        
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        logger.log(Level.INFO, "GUI main menu loaded.");
    }
    
    /**
     * showAllPackages() method creates a new JFrame of the current shippingstore inventory. 
     */
    public void showAllPackages() {
        if (ss.getListSize() == 0) {
            JOptionPane.showMessageDialog(null, "No packages currently exist in database.", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "showAllPackages() : empty database");
            return;
        }

        String[] header = {"PACKAGE TYPE", "TRACKING #", "SPECIFICATION", "MAILING CLASS", 
            "WEIGHT", "VOLUME", "MORE DETAILS", "MORE DETAILS 2"};
        String dimension, loadWeight, content, material, diameter, height, width;
        Object[][] data = new Object[ss.getListSize()][header.length];
        try {
            for (int i = 0; i < ss.getListSize(); ++i) {
                if (ss.packagePosition(i) instanceof Box) {
                    data[i][0] = "Box";
                    data[i][1] = ss.packagePosition(i).getPtn();
                    data[i][2] = ss.packagePosition(i).getSpecification();
                    data[i][3] = ss.packagePosition(i).getMailingClass();
                    data[i][4] = ss.packagePosition(i).getWeight();
                    data[i][5] = ss.packagePosition(i).getVolume();
                    dimension = Integer.toString(((Box) ss.packagePosition(i)).getDimension());
                    data[i][6] = dimension;
                    data[i][7] = "N/A";
                } else if (ss.packagePosition(i) instanceof Crate) {
                    data[i][0] = "Crate";
                    data[i][1] = ss.packagePosition(i).getPtn();
                    data[i][2] = ss.packagePosition(i).getSpecification();
                    data[i][3] = ss.packagePosition(i).getMailingClass();
                    data[i][4] = ss.packagePosition(i).getWeight();
                    data[i][5] = ss.packagePosition(i).getVolume();
                    content = ((Crate) ss.packagePosition(i)).getContent();
                    data[i][6] = content;
                    data[i][7] = "N/A";
                } else if (ss.packagePosition(i) instanceof Drum) {
                    data[i][0] = "Drum";
                    data[i][1] = ss.packagePosition(i).getPtn();
                    data[i][2] = ss.packagePosition(i).getSpecification();
                    data[i][3] = ss.packagePosition(i).getMailingClass();
                    data[i][4] = ss.packagePosition(i).getWeight();
                    data[i][5] = ss.packagePosition(i).getVolume();
                    material = ((Drum) ss.packagePosition(i)).getMaterial();
                    data[i][6] = material;
                    diameter = Float.toString(((Drum) ss.packagePosition(i)).getDiameter());
                    data[i][7] = diameter;
                }  else if (ss.packagePosition(i) instanceof Envelope) {
                    data[i][0] = "Envelope";
                    data[i][1] = ss.packagePosition(i).getPtn();
                    data[i][2] = ss.packagePosition(i).getSpecification();
                    data[i][3] = ss.packagePosition(i).getMailingClass();
                    data[i][4] = ss.packagePosition(i).getWeight();
                    data[i][5] = ss.packagePosition(i).getVolume();
                    height = Integer.toString(((Envelope) ss.packagePosition(i)).getHeight());
                    data[i][7] = height;
                    width  = Integer.toString(((Envelope) ss.packagePosition(i)).getWidth());
                    data[i][7] = width;
                } else {
                    data[i][0] = ss.packagePosition(i).getType();
                    data[i][1] = ss.packagePosition(i).getPtn();
                    data[i][2] = ss.packagePosition(i).getSpecification();
                    data[i][3] = ss.packagePosition(i).getMailingClass();
                    data[i][4] = ss.packagePosition(i).getWeight();
                    data[i][5] = ss.packagePosition(i).getVolume();
                    data[i][6] = "N/A";
                    data[i][7] = "N/A";
                }
            }
        }
        catch (ClassCastException e) {
            logger.log(Level.SEVERE, "ClassCastException thrown, possible database corruption");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Unknown exception thrown! See stack trace..", e);
            e.printStackTrace();
        }
        
        JFrame display = new JFrame("Package List");
        final JTable table = new JTable(data, header);
        table.setPreferredScrollableViewportSize(new Dimension(800, 100));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        display.add(scrollPane);

        display.pack();
        display.setVisible(true);
        display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        logger.log(Level.INFO, "User in 'Package List' window");
    }
    
    /**
     * addPackage() method creates a new JFrame for adding a new package to the database.
     */
    public void addPackage() {
        JFrame frame = new JFrame("Adding package . . . ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        MainAppGUI a = new MainAppGUI();
        a.newPanelComponentPackage(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        logger.log(Level.INFO, "User in 'Add Package' window");
    }
    
    /**
     * delPackageGUI() provides a GUI for deleting a package from the shippingstore 
     */
    public void deletePackageGUI() {
        JFrame frame = new JFrame("Deleting package . . . ");
        
        if (ss.getListSize() == 0) {
            JOptionPane.showMessageDialog(null, "Database is currently empty.", "Failure",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to view empty database");
            return;
        }
        
        JPanel panel = new JPanel();
        JLabel intro = new JLabel("Enter the tracking number of the package to be deleted: ");
        JTextField ptn = new JTextField(12);
        JButton enter = new JButton("Enter");
        JButton exit = new JButton("Exit");

        frame.setContentPane(panel);
        panel.add(intro);
        panel.add(ptn);
        panel.add(enter);
        panel.add(exit);
        
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "PTN (string) submitted for deletion");
                Thread qThread = new Thread() {
                    public void run() {
                        if (ss.deletePackage(ptn.getText())) {
                            JOptionPane.showMessageDialog(frame, "Removal successful", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            logger.log(Level.INFO, "Package removed via PTN (string).");
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "Removal unsuccessful.", "Failure",
                                    JOptionPane.ERROR_MESSAGE);
                            logger.log(Level.INFO, "PTN (string) not found");
                        }
                    }
                };
                qThread.start();
            }
        });
        
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                frame.dispose();
                logger.log(Level.INFO, "'Exit' button pressed in deletePackageGUI");
            }
        });

        //Display the window.
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        logger.log(Level.INFO, "Opened GUI option to delete a package.");
    }
    
    /**
     * searchPackageGUI() allows a user to search for a package by tracking number. The search may be successful
     * or unsuccessful and displays the appropriate message dialog to the user in each event
     */
    public void searchPackageGUI() {
        JFrame frame = new JFrame("Package search");
        if (ss.getListSize() == 0) {
            JOptionPane.showMessageDialog(frame, "The database is currently empty.", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to search an empty database");
            return;
        }
        JPanel panel = new JPanel();
        JLabel intro = new JLabel("Enter the package tracking number: ");
        JTextField search = new JTextField(15);
        JButton enter = new JButton("Enter");
        JButton exit = new JButton("Exit");

        frame.setContentPane(panel);
        panel.add(intro);
        panel.add(search);
        panel.add(enter);
        panel.add(exit);

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                frame.dispose();
                logger.log(Level.INFO, "User has exited from 'Package Search' window");
            }
        });

        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User attempted to submit a string value to search for");
                Thread qThread = new Thread() {
                    public void run() {
                        String[] header = {"PACKAGE TYPE", "TRACKING #", "SPECIFICATION", "MAILING CLASS", 
                            "WEIGHT", "VOLUME", "MORE DETAILS", "MORE DETAILS 2"};
                        String dimension, loadWeight, content, material, diameter, height, width;
                        Object[][] data = new Object[ss.getListSize()][header.length];

                        int index = 0;
                        try {
                            if ((index = ss.basicSearch(search.getText())) != ss.getListSize()) {
                                if (ss.packagePosition(index) instanceof Box) {
                                    data[0][0] = "Box";
                                    data[0][1] = ss.packagePosition(index).getPtn();
                                    data[0][2] = ss.packagePosition(index).getSpecification();
                                    data[0][3] = ss.packagePosition(index).getMailingClass();
                                    data[0][4] = ss.packagePosition(index).getWeight();
                                    data[0][5] = ss.packagePosition(index).getVolume();
                                    dimension = Integer.toString(((Box) ss.packagePosition(index)).getDimension());
                                    data[0][6] = dimension;
                                    data[0][7] = "N/A";
                                } else if (ss.packagePosition(index) instanceof Crate) {
                                    data[0][0] = "Crate";
                                    data[0][1] = ss.packagePosition(index).getPtn();
                                    data[0][2] = ss.packagePosition(index).getSpecification();
                                    data[0][3] = ss.packagePosition(index).getMailingClass();
                                    data[0][4] = ss.packagePosition(index).getWeight();
                                    data[0][5] = ss.packagePosition(index).getVolume();
                                    content = ((Crate) ss.packagePosition(index)).getContent();
                                    data[0][6] = content;
                                    data[0][7] = "N/A";
                                } else if (ss.packagePosition(index) instanceof Drum) {
                                    data[0][0] = "Drum";
                                    data[0][1] = ss.packagePosition(index).getPtn();
                                    data[0][2] = ss.packagePosition(index).getSpecification();
                                    data[0][3] = ss.packagePosition(index).getMailingClass();
                                    data[0][4] = ss.packagePosition(index).getWeight();
                                    data[0][5] = ss.packagePosition(index).getVolume();
                                    material = ((Drum) ss.packagePosition(index)).getMaterial();
                                    data[0][6] = material;
                                    diameter = Float.toString(((Drum) ss.packagePosition(index)).getDiameter());
                                    data[0][7] = diameter;
                                }  else if (ss.packagePosition(index) instanceof Envelope) {
                                    data[0][0] = "Envelope";
                                    data[0][1] = ss.packagePosition(index).getPtn();
                                    data[0][2] = ss.packagePosition(index).getSpecification();
                                    data[0][3] = ss.packagePosition(index).getMailingClass();
                                    data[0][4] = ss.packagePosition(index).getWeight();
                                    data[0][5] = ss.packagePosition(index).getVolume();
                                    height = Integer.toString(((Envelope) ss.packagePosition(index)).getHeight());
                                    data[0][6] = height;
                                    width  = Integer.toString(((Envelope) ss.packagePosition(index)).getWidth());
                                    data[0][7] = width;
                                } else {
                                    data[0][0] = ss.packagePosition(index).getType();
                                    data[0][1] = ss.packagePosition(index).getPtn();
                                    data[0][2] = ss.packagePosition(index).getSpecification();
                                    data[0][3] = ss.packagePosition(index).getMailingClass();
                                    data[0][4] = ss.packagePosition(index).getWeight();
                                    data[0][5] = ss.packagePosition(index).getVolume();
                                    data[0][6] = "N/A";
                                    data[0][7] = "N/A";
                                }

                                JFrame display = new JFrame("Result(s)");
                                final JTable table = new JTable(data, header);
                                table.setPreferredScrollableViewportSize(new Dimension(800, 100));
                                table.setFillsViewportHeight(true);
                                table.setEnabled(false);

                                JScrollPane scrollPane = new JScrollPane(table);
                                display.add(scrollPane);

                                display.pack();
                                display.setVisible(true);
                                display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                            } else {
                                JOptionPane.showMessageDialog(frame, "PTN not found in the database.", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        catch (ClassCastException e) {
                            logger.log(Level.SEVERE, "ClassCastException unexpectedly thrown. Refer to stack trace", e);
                            e.printStackTrace();
                        }
                    }
                };
                qThread.start();
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        logger.log(Level.INFO, "User entered 'Package Search' window");
    }
    
    /**
     * displayUsers() method displays in a new JFrame (running on a separate thread from the main GUI) the current
     * users in the database (includes Employee and Customer objects) in a JTable
     */
    public void displayUsers() {
        if (ss.getUserDatabaseSize() == 0) {
            JOptionPane.showMessageDialog(null, "There database is currently empty! "
                    + "Now exiting..", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to view an empty user database");
            return;
        }

        String[] header = {"TYPE", "ID#", "FIRST NAME", "LAST NAME", "PHONE NUMBER", "ADDRESS", "MONTHLY SALARY",
                            "BANK ACCT#", "SS#"};
        Object[][] data = new Object[ss.getUserDatabaseSize()][header.length];
        try {
            for (int i = 0; i < ss.getUserDatabaseSize(); ++i) {
                if (ss.getUserAtPosition(i) instanceof Customer) {
                    data[i][0] = "Customer";
                    data[i][4] = ((Customer) ss.getUserAtPosition(i)).getPhoneNumber();
                    data[i][5] = ((Customer) ss.getUserAtPosition(i)).getAddress();
                    data[i][6] = "N/A";
                    data[i][7] = "N/A";
                } else if (ss.getUserAtPosition(i) instanceof Employee) {
                    data[i][0] = "Employee";
                    data[i][4] = "N/A";
                    data[i][5] = "N/A";
                    data[i][6] = ((Employee) ss.getUserAtPosition(i)).getMonthlySalary();
                    data[i][7] = ((Employee) ss.getUserAtPosition(i)).getBankAccountNumber();
                    data[i][8] = ((Employee) ss.getUserAtPosition(i)).getSocialSecurityNumber();
                }

                data[i][1] = ss.getUserAtPosition(i).getId();
                data[i][2] = ss.getUserAtPosition(i).getFirstName();
                data[i][3] = ss.getUserAtPosition(i).getLastName();
            }
        }
        catch (ClassCastException e) {
            logger.log(Level.SEVERE, "ClassCastException thrown unexpectedly, refer to stack trace", e);
            e.printStackTrace();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Unknown exception thrown, refer to stack trace", e);
        }

        JFrame display = new JFrame("User List");
        final JTable table = new JTable(data, header);
        table.setPreferredScrollableViewportSize(new Dimension(800, 100));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        display.add(scrollPane);

        display.pack();
        display.setVisible(true);
        display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        logger.log(Level.INFO, "User has entered 'User List'");
    }
    
    /**
     * addUserGUI() allows the user to add either a Customer or Employee to the database via GUI. The GUI uses
     * a CardLayout type such that it allows one JPanel to be active while keeping other JPanel contents active.
     * A user can enter 1 tab, input some information into the text fields and enter another JPanel, key in some
     * into the text fields and return to the other tab with the information still intact
     */
    public void addUserGUI() {
        JFrame frame = new JFrame("Adding new user");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        MainAppGUI x = new MainAppGUI();
        x.newPanelComponentUser(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        logger.log(Level.INFO, "User has entered 'Adding new user'");
    }
    
    /**
     * updateUserGUI() allows the user to update a specific user given their ID number. 
     * Able to update Employee & Customer objects. Appropriate message dialogs 
     * reported on failure and success.
     */
    public void updateUserGUI() {
        JFrame frame = new JFrame("Update a user");
        JPanel panel = new JPanel();
        JTextField entry = new JTextField(12);
        JLabel id_name = new JLabel("Enter the ID Number of the user to be updated: ");
        JButton submit = new JButton("Submit");

        panel.add(id_name);
        panel.add(entry);
        panel.add(submit);

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Submit'");
                int q = 0;
                for (int i = 0; i < ss.getUserDatabaseSize(); ++i, q++) {
                    try {
                        if (ss.getUserAtPosition(i).getId() == Integer.parseInt(entry.getText())) {
                            break;
                        }
                    }
                    catch (NumberFormatException e) {
                        logger.log(Level.SEVERE, "User submitted non-integer values", e);
                    }
                    catch (Exception ex) {
                        logger.log(Level.SEVERE, "Unknown exception occurred", ex);
                    }
                }
                final User temp = ss.getUserAtPosition(q);
                if (temp == null) {
                    JOptionPane.showMessageDialog(frame, "User ID not found!", "Failure!",
                            JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.INFO, "User did not enter a valid ID#");
                    return;
                }
                else {
                    JPanel subpanel = new JPanel(new GridLayout(5,1,2,5));
                    JLabel first = new JLabel("First Name: ");
                    JTextField firstname = new JTextField(temp.getFirstName(), 12);
                    JLabel last = new JLabel("Last Name: ");
                    JTextField lastname = new JTextField(temp.getLastName(), 12);
                    JButton sub_submit = new JButton("Submit Changes");
                    JLabel phoneNum = new JLabel("Phone Number: ");
                    JTextField phoneNumb = new JTextField(12);
                    JLabel add = new JLabel("Address (String): ");
                    JTextField addTF = new JTextField(12);
                    JLabel salary = new JLabel("Monthly Salary: ");
                    JTextField salaryTF = new JTextField(12);
                    JLabel bank = new JLabel("Bank Account #: ");
                    JTextField bankTF = new JTextField(12);
                    JLabel ssNum = new JLabel("Social Security #: ");
                    JTextField ssNumTF = new JTextField(12);
                    JLabel type = new JLabel();

                    subpanel.add(first);
                    subpanel.add(firstname);
                    subpanel.add(last);
                    subpanel.add(lastname);

                    if (temp instanceof Customer) {
                        phoneNumb.setText(((Customer) temp).getPhoneNumber());
                        addTF.setText((((Customer) temp).getAddress()));
                        type.setText("Type: Customer");

                        subpanel.add(phoneNum);
                        subpanel.add(phoneNumb);
                        subpanel.add(add);
                        subpanel.add(addTF);
                    }
                    else {
                        salaryTF.setText(Float.toString(((Employee) temp).getMonthlySalary()));
                        bankTF.setText(Integer.toString(((Employee) temp).getBankAccountNumber()));
                        ssNumTF.setText(Integer.toString(((Employee) temp).getSocialSecurityNumber()));
                        type.setText("Type: Employee");

                        subpanel.add(salary);
                        subpanel.add(salaryTF);
                        subpanel.add(bank);
                        subpanel.add(bankTF);
                        subpanel.add(ssNum);
                        subpanel.add(ssNumTF);
                    }

                    subpanel.add(sub_submit);
                    subpanel.add(type);

                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(subpanel);
                    frame.validate();
                    frame.pack();

                    sub_submit.addActionListener(new ActionListener() {
                        public void actionPerformed (ActionEvent x) {
                            temp.setFirstName(firstname.getText());
                            temp.setLastName(lastname.getText());
                            if (temp instanceof Customer) {
                                ((Customer)temp).setPhoneNumber(phoneNumb.getText());
                                ((Customer)temp).setAddress((addTF.getText()));
                            }
                            else {
                                ((Employee)temp).setMonthlySalary(Float.parseFloat(salaryTF.getText()));
                                ((Employee)temp).setBankAccountNumber(Integer.parseInt(bankTF.getText()));
                                ((Employee)temp).setSocialSecurityNumber(Integer.parseInt(ssNumTF.getText()));
                            }
                            JOptionPane.showMessageDialog(frame, "User has been successfully updated!", "Success!",
                                    JOptionPane.INFORMATION_MESSAGE);
                            logger.log(Level.INFO, "User able to update a user");
                            frame.dispose();
                        }
                    });
                }
            }
        });

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        logger.log(Level.INFO, "User entered 'Update User' operation");
    }
    
    /**
     * deliverPackageGUI() method allows the user (assuming users ID matches an 
     * Employee object) to deliver a package to another
     */
    public void deliverPackageGUI(){
        JFrame frame = new JFrame("Delivering a package");
        JPanel mainpanel = new JPanel();
        JLabel qCustID = new JLabel("Enter the customer's ID: ");
        JTextField eCustID = new JTextField(12);
        JButton submit = new JButton("Submit");

        mainpanel.add(qCustID);
        mainpanel.add(eCustID);
        mainpanel.add(submit);

        submit.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent x) {
                // checking user exists
                if ((Integer.parseInt(eCustID.getText()) < 0)) {
                    JOptionPane.showMessageDialog(frame, "INVALID INPUT: ID cannot be a negative value", "Failure!",
                            JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.WARNING, "User entered bad input, a negative value");
                }
                else if (ss.userExists(Integer.parseInt(eCustID.getText())) &&
                        ss.getUserAtPosition(Integer.parseInt(eCustID.getText()) - 1) instanceof Customer) {
                    final int custID = Integer.parseInt(eCustID.getText());
                    JPanel subpanel = new JPanel();
                    JLabel qEmpID = new JLabel("Enter your employee ID: ");
                    JTextField eEmpID = new JTextField(12);
                    JButton sub_submit = new JButton("Submit");

                    subpanel.add(qEmpID);
                    subpanel.add(eEmpID);
                    subpanel.add(sub_submit);

                    sub_submit.addActionListener(new ActionListener() {
                        public void actionPerformed (ActionEvent x) {
                            // checking employee exists
                            if ((Integer.parseInt(eEmpID.getText()) < 0)) {
                                JOptionPane.showMessageDialog(frame, "INVALID INPUT: ID cannot be a negative value", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "User entered bad input, a negative value");
                            }
                            else if (ss.userExists(Integer.parseInt(eEmpID.getText())) &&
                                    ss.getUserAtPosition(Integer.parseInt(eEmpID.getText()) - 1) instanceof Employee) {
                                final int employeeID = Integer.parseInt(eEmpID.getText());
                                JPanel subsubpanel = new JPanel();
                                JLabel qPTN = new JLabel("Enter the package's PTN: ");
                                JTextField ePTN = new JTextField(12);
                                JButton subsub_submit = new JButton("Submit");

                                subsubpanel.add(qPTN);
                                subsubpanel.add(ePTN);
                                subsubpanel.add(subsub_submit);

                                subsub_submit.addActionListener(new ActionListener() {
                                   public void actionPerformed (ActionEvent x) {
                                       JPanel subsubsubpanel = new JPanel(new GridLayout(6, 4, 4, 4));
                                       if (ss.packageMatch(ePTN.getText())) {
                                           final String PTN = ePTN.getText();
                                           Date dateOfTransc = new Date(System.currentTimeMillis());
                                           Date shippingDate = new Date(System.currentTimeMillis());
                                           JLabel qPrice = new JLabel("Enter price of delivery: ");
                                           JTextField ePrice = new JTextField(12);
                                           JLabel loggedinas = new JLabel("Logged in as: " +
                                                   ""+ss.getUserAtPosition(employeeID - 1).getFirstName()+" " +
                                                   ""+ss.getUserAtPosition(employeeID - 1).getLastName()+" " +
                                                   "(id == "+ss.getUserAtPosition(employeeID - 1).getId()+")");
                                           JButton subsubsub_submit = new JButton("Submit");

                                           subsubsub_submit.addActionListener(new ActionListener() {
                                               public void actionPerformed (ActionEvent x) {
                                                   if (Float.parseFloat(ePrice.getText()) < 0) {
                                                      JOptionPane.showMessageDialog(frame, "Price cannot be negative! " +
                                                                       "Please check your input and try again; or exit window\n", "Failure!",
                                                               JOptionPane.ERROR_MESSAGE);
                                                       logger.log(Level.WARNING, "User entered a negative value for 'price'");
                                                   }
                                                   else {
                                                       Transaction trans = new Transaction(custID, employeeID,
                                                               PTN, shippingDate, dateOfTransc, Float.parseFloat(ePrice.getText()));

                                                       if (!ss.addTransactionGUI(trans)) {
                                                           JOptionPane.showMessageDialog(frame, "addTransactionGUI() failed!\n" +
                                                                           "Aborting transaction...", "Failure!",
                                                                   JOptionPane.ERROR_MESSAGE);
                                                           logger.log(Level.SEVERE,"addTransactionGUI() failed, transaction not added to ss");
                                                           frame.dispose();
                                                       }

                                                       if (!ss.deletePackageByStr(PTN)) {
                                                           JOptionPane.showMessageDialog(frame, "deletePackageByStr failed!\n" +
                                                                           "Aborting transaction...", "Failure!",
                                                                   JOptionPane.ERROR_MESSAGE);
                                                           logger.log(Level.SEVERE, "deletePackageByStr failed");
                                                           frame.dispose();
                                                       }

                                                       JOptionPane.showMessageDialog(frame, "Transaction complete!\n " +
                                                                       "The window will close after pressing \"OK\"", "Success!",
                                                               JOptionPane.INFORMATION_MESSAGE);
                                                       logger.log(Level.INFO, "User successful with adding transaction");
                                                       frame.dispose();
                                                   }
                                               }
                                           });

                                           subsubsubpanel.add(qPrice);
                                           subsubsubpanel.add(ePrice);
                                           subsubsubpanel.add(subsubsub_submit);
                                           subsubsubpanel.add(loggedinas);

                                       }
                                       else {
                                           JOptionPane.showMessageDialog(frame, "PTN does not exist! " +
                                                           "Please check your input and try again; or exit window\n", "Failure!",
                                                   JOptionPane.ERROR_MESSAGE);
                                           logger.log(Level.WARNING, "User entered an invalid PTN or it does not exist");
                                       }

                                       frame.getContentPane().removeAll();
                                       frame.getContentPane().add(subsubsubpanel);
                                       frame.validate();
                                       frame.pack();
                                   }
                                });
                                frame.getContentPane().removeAll();
                                frame.getContentPane().add(subsubpanel);
                                frame.validate();
                                frame.pack();
                            }
                            else {
                                JOptionPane.showMessageDialog(frame, "Employee does not exist! Please check your input and try again.\n" +
                                                "Need a valid employee ID to continue with transaction.", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "User entered an ID that does not exist");
                            }
                        }
                    });

                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(subpanel);
                    frame.validate();
                    frame.pack();
                }
                else {
                    JOptionPane.showMessageDialog(frame, "User does not exist! Please check your input and try again.\n" +
                                    "You may need to add the customer to the database before proceeding with a sale.", "Failure!",
                            JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.WARNING, "User entered a User ID that does not exist");
                }
            }
        });

        frame.setContentPane(mainpanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        logger.log(Level.INFO, "User entered 'Deliver a package' operation");
    }
    
    /**
     * listTransactionsGUI() display the appropriate transaction data in a JTable
     */
    public void listTransactionsGUI() {
        if (ss.getSaleTransactionSize() == 0) {
            JOptionPane.showMessageDialog(null, "The database is currently empty. "
                    + "Now exiting..", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to view an empty transaction database");
            return;
        }
        String[] header = {"CUSTOMER ID", "EMPLOYEE ID", "PTN", "SHIPPING DATE", 
            "DELIVERY DATE", "PRICE"};
        Object[][] data = new Object[ss.getSaleTransactionSize()][header.length];

        for (int i = 0; i < ss.getSaleTransactionSize(); ++i) {
            data[i][0] = ss.getTransactionAtPosition(i).getCustomerId();
            data[i][1] = ss.getTransactionAtPosition(i).getEmployeeId();
            data[i][2] = ss.getTransactionAtPosition(i).getPtn();
            data[i][3] = ss.getTransactionAtPosition(i).getShippingDate();
            data[i][4] = ss.getTransactionAtPosition(i).getDeliveryDate();
            data[i][5] = ss.getTransactionAtPosition(i).getPrice();
        }

        JFrame display = new JFrame("Transaction List");
        final JTable table = new JTable(data, header);

        table.setPreferredScrollableViewportSize(new Dimension(800, 100));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        display.add(scrollPane);

        display.pack();
        display.setVisible(true);
        display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        logger.log(Level.INFO, "User is in list transactions operation");
    }

    /**
     * terminateSession() will write the database as a serialized object then exit the GUI main menu/program.
     */
    public void terminateSession() {
        ss.writeDatabase();
        logger.log(Level.INFO, "User has closed the program via 'Exit' in main menu, exit successful!");
        System.exit(0);
    }
    
    
    /**
     * newPanelComponentUser() method is a helper method for addUserGUI(), utilizes CardLayout to allow multiple
     * JPanels within a single JFrame w/o losing entered information
     * @param pane
     */
    public void newPanelComponentUser(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for adding a new Customer
        JPanel card1 = new JPanel(new GridLayout(6,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel cFName = new JLabel("First Name (String)");
        JTextField cFNameTF = new JTextField("", 12);
        card1.add(cFName);
        card1.add(cFNameTF);
        JLabel cLName = new JLabel("Last Name (String)");
        JTextField cLNameTF = new JTextField("", 12);
        card1.add(cLName);
        card1.add(cLNameTF);
        JLabel cPhone = new JLabel("Phone Number (String)");
        JTextField cPhoneTF = new JTextField("", 12);
        card1.add(cPhone);
        card1.add(cPhoneTF);
        JLabel cDLN = new JLabel("Address (string)");
        JTextField cDLNTF = new JTextField("", 12);
        card1.add(cDLN);
        card1.add(cDLNTF);

        JButton cSUBMIT = new JButton ("Submit");
        card1.add(cSUBMIT);
        JButton cCLEAR = new JButton("Clear");
        card1.add(cCLEAR);
        exitFrom = new JButton("Exit");
        card1.add(exitFrom);

        exitFrom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Container frame = exitFrom.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        cSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String first, last, phone, address;
                        ArrayList<String> err = new ArrayList<>();

                        if (cFName.getText().length() == 0) {
                            err.add("Field 'First Name' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field text (First Name)");
                        }

                        first = cFNameTF.getText();


                        if (cLNameTF.getText().length() == 0) {
                            err.add("Field 'Last Name' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field (Last Name)");

                        }

                        last = cLNameTF.getText();

                        if (cPhoneTF.getText().length() == 0) {
                            err.add("Field 'Phone Number' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field (Phone Number)");
                        }

                        phone = cPhoneTF.getText();

                        if (cDLNTF.getText().length() == 0) {
                            err.add("'Address' value is invalid");
                            logger.log(Level.WARNING, "User submitted empty field (Address)");
                        }
                        
                        address = cDLNTF.getText();

                        try {
                            if (err.isEmpty()) {
                                Customer newObj = new Customer(ss.userIdCounter++, first, last, phone, address);
                                if (ss.addUserDirectly(newObj)) {
                                    Container frame = card1.getParent();
                                    do {
                                        frame = frame.getParent();
                                    } while (!(frame instanceof JFrame));
                                    JOptionPane.showMessageDialog(frame, "User has been successfully added!\n" +
                                                    "You may continue to add users by pressing \"Ok\" \n" +
                                                    "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                    "then \"Exit\" (in the 'Adding new user' window)",
                                            "Success!", JOptionPane.INFORMATION_MESSAGE);
                                    logger.log(Level.INFO, "New user added successfully");
                                } else {
                                    Container frame = card1.getParent();
                                    do {
                                        frame = frame.getParent();
                                    } while (!(frame instanceof JFrame));
                                    JOptionPane.showMessageDialog(frame, "addUserDirectly(User) method failed, " +
                                                    "despite criteria being met. An unknown error has occurred!", "Failure!",
                                            JOptionPane.ERROR_MESSAGE);
                                    logger.log(Level.SEVERE, "addUserDirectly(User) failed");
                                }
                            } else {
                                Container frame = card1.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                for (String i : err) {
                                    JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        catch (NumberFormatException e) {
                            logger.log(Level.SEVERE, "User submitted non-integer values", e);
                            e.printStackTrace();
                        }
                        catch (Exception e) {
                            logger.log(Level.SEVERE, "Unknown exception thrown, refer to stack trace");
                            e.printStackTrace();
                        }
                    }
                };
                qThread.start();
            }
        });

        cCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User cleared the form");
                cFNameTF.setText("");
                cLNameTF.setText("");
                cPhoneTF.setText("");
                cDLNTF.setText("");
            }
        });

        // Panel for adding a new Employee
        JPanel card2 = new JPanel(new GridLayout(6,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel eFName = new JLabel("First Name (String)");
        JTextField eFNameTF = new JTextField("", 12);
        card2.add(eFName);
        card2.add(eFNameTF);
        JLabel eLName = new JLabel("Last Name (String)");
        JTextField eLNameTF = new JTextField("", 12);
        card2.add(eLName);
        card2.add(eLNameTF);
        JLabel eSalary = new JLabel("Monthly Salary (float)");
        JTextField eSalaryTF = new JTextField("", 12);
        card2.add(eSalary);
        card2.add(eSalaryTF);
        JLabel eBAN = new JLabel("Bank Account # (int)");
        JTextField eBANTF = new JTextField("", 12);
        card2.add(eBAN);
        card2.add(eBANTF);
        JLabel eSS = new JLabel("Social Security # (int)");
        JTextField eSSTF = new JTextField("", 12);
        card2.add(eSS);
        card2.add(eSSTF);
        JButton eSUBMIT = new JButton ("Submit");
        card2.add(eSUBMIT);
        JButton eCLEAR = new JButton("Clear");
        card2.add(eCLEAR);
        exitFrom2 = new JButton("Exit");
        card2.add(exitFrom2);

        exitFrom2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User is exiting the add user window");
                Container frame = exitFrom2.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        eSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String first, last;
                        int bank, socSec;
                        float salary;
                        ArrayList<String> err = new ArrayList<>();

                        if (eFNameTF.getText().length() == 0) {
                            err.add("Field 'First Name' is empty");
                        }

                        first = eFNameTF.getText();


                        if (eLNameTF.getText().length() == 0) {
                            err.add("Field 'Last Name' is empty");
                        }

                        last = eLNameTF.getText();

                        if (eBANTF.getText().length() == 0) {
                            err.add("Field 'Bank Account #' is empty");
                        }
                        else if (Integer.parseInt(eBANTF.getText()) < 0) {
                            err.add("Bank account number cannot be a negative value");
                        }

                        bank = Integer.parseInt(eBANTF.getText());

                        if (eSalaryTF.getText().length() == 0) {
                            err.add("Field 'Monthly Salary' is empty");
                        }
                        else if (Float.parseFloat(eSalaryTF.getText()) < 0) {
                            err.add("Monthly Salary cannot be a negative value");
                        }

                        salary = Float.parseFloat(eSalaryTF.getText());
                        
                        if (eSS.getText().length() == 0) {
                            err.add("Field 'Social Security #' is empty");
                        }
                        else if (Integer.parseInt(eSSTF.getText()) < 0) {
                            err.add("Social Security number cannot be a negative value");
                        }

                        socSec = Integer.parseInt(eBANTF.getText());

                        if (err.isEmpty()) {
                            Employee newObj = new Employee(ss.userIdCounter++, first, last, socSec, salary, bank);
                            if (ss.addUserDirectly(newObj)) {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "User has been successfully added!\n" +
                                                "You may continue to add users by pressing \"Ok\" \n" +
                                                "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                "then \"Exit\" (in the 'Adding new user' window)",
                                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "User successfully adds a new Employee");
                            }
                            else {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addUserDirectly(User) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.SEVERE, "Unable to add a new user despite criteria being met in addUserDirectly()");
                            }
                        }
                        else {
                            Container frame = card2.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "Program displaying error message logs to user");
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        eCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User has cleared the form");
                eFNameTF.setText("");
                eLNameTF.setText("");
                eBANTF.setText("");
                eSalaryTF.setText("");
            }
        });

        tabbedPane.addTab(CUSTOMERPANEL, card1);
        tabbedPane.addTab(EMPLOYEEPANEL, card2);

        pane.add(tabbedPane, BorderLayout.WEST);
    }

    /**
     * newPanelComponentPackage() is a helper method for addPackage()
     * @param pane
     */
    public void newPanelComponentPackage(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for adding a new Box
        JPanel card1 = new JPanel(new GridLayout(10,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel bPTN = new JLabel("PTN (string)");
        JTextField bPTNTF = new JTextField("", 12);
        card1.add(bPTN);
        card1.add(bPTNTF);
        JLabel bTYPE = new JLabel("Type (string)");
        JTextField bTYPETF = new JTextField("", 12);
        card1.add(bTYPE);
        card1.add(bTYPETF);
        JLabel bSPEC = new JLabel("Specification (string)");
        JTextField bSPECTF = new JTextField("", 12);
        card1.add(bSPEC);
        card1.add(bSPECTF);
        JLabel bMC = new JLabel("Mailing Class (string)");
        JTextField bMCTF = new JTextField("", 12);
        card1.add(bMC);
        card1.add(bMCTF);
        JLabel bWEIGHT = new JLabel("Weight (float)");
        JTextField bWEIGHTTF = new JTextField("", 12);
        card1.add(bWEIGHT);
        card1.add(bWEIGHTTF);
        JLabel bVOL = new JLabel("Volume (int)");
        JTextField bVOLTF = new JTextField("", 12);
        card1.add(bVOL);
        card1.add(bVOLTF);
        JLabel bDIM = new JLabel("Dimension (int)");
        JTextField bDIMTF = new JTextField("", 12);
        card1.add(bDIM);
        card1.add(bDIMTF);
        JButton bSUBMIT = new JButton ("Submit");
        card1.add(bSUBMIT);
        JButton bCLEAR = new JButton("Clear");
        card1.add(bCLEAR);
        exitFrom = new JButton("Exit");
        card1.add(exitFrom);

        exitFrom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User exited from adding package window gracefully");
                Container frame = exitFrom.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        bSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String PTN, type, spec, mailClass;
                        int volume, dimension;
                        float weight;
                        ArrayList<String> err = new ArrayList<>();

                        if (bPTNTF.getText().length() == 0) {
                            err.add("Field 'PTN' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, PTN");
                        }
                        else if (bPTNTF.getText().length() > 5) {
                            err.add("'PTN' length is too long!");
                            logger.log(Level.WARNING, "User submitted String that is too long");
                        }
                        else if (ss.packageMatch(bPTNTF.getText())) {
                            err.add("'PTN' already exists in the database!");
                            logger.log(Level.WARNING, "User submitted PTN that already exists");
                        }

                        PTN = bPTNTF.getText();

                        if (bTYPETF.getText().length() == 0) {
                            err.add("Field 'Type' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Type");
                        }

                        type = bTYPETF.getText();
                        boolean corr = false;
        
                        corr = type.equalsIgnoreCase("Box");
                        if (!(corr)) {
                            err.add("Field 'Type' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Type");
                        }

                        if (bSPECTF.getText().length() == 0) {
                            err.add("Field 'Specification' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Specification");
                        }

                        spec = bSPECTF.getText();

                        corr = spec.equalsIgnoreCase("Fragile") || spec.equalsIgnoreCase("Books") 
                            || spec.equalsIgnoreCase("Catalogs");
                        corr = corr || spec.equalsIgnoreCase("Do-not-bend") || spec.equalsIgnoreCase("N/A");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (bMCTF.getText().length() == 0) {
                            err.add("'Mailing Class' value is invalid");
                            logger.log(Level.WARNING, "User submitted an empty field, Mailing Class");
                        }

                        mailClass = bMCTF.getText();
                        
                        corr = spec.equalsIgnoreCase("First-Class") || spec.equalsIgnoreCase("Priority") 
                            || spec.equalsIgnoreCase("Retail");
                        corr = corr || spec.equalsIgnoreCase("Ground") || spec.equalsIgnoreCase("Metro");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (bWEIGHTTF.getText().length() == 0) {
                            err.add("Field 'Weight' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Weight");
                        }
                        else if (Float.parseFloat(bWEIGHTTF.getText()) < 0) {
                            err.add("'Weight' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Weight");
                        }

                        weight = Float.parseFloat(bWEIGHTTF.getText());

                        if (bVOLTF.getText().length() == 0) {
                            err.add("Field 'Volume' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Volume");
                        }
                        else if (Integer.parseInt(bVOLTF.getText()) < 0) {
                            err.add("'Volume' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value for 'Volume'");
                        }

                        volume = Integer.parseInt(bVOLTF.getText());

                        if (bDIMTF.getText().length() == 0) {
                            err.add("Field 'Dimension' is empty");
                            logger.log(Level.WARNING, "User submitted negative value for 'Dimension'");
                        }

                        dimension = Integer.parseInt(bDIMTF.getText());

                        if (err.isEmpty()) {
                            Box newObj = new Box(PTN, type, spec,  mailClass, 
                                weight, volume, dimension);
                            if (ss.addPackageDirectly(newObj)) {
                                Container frame = card1.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "Box has been successfully added!\n" +
                                        "You may continue to add packages by pressing \"Ok\" \n" +
                                        "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                        "then \"Exit\" (in the 'Adding package' window)",
                                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "User able to succesfully add a Box object");
                            }
                            else {
                                Container frame = card1.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addPackageDirectly(Package) method failed, " +
                                        "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.SEVERE, "addPackageDirectly() failed unexpectedly");
                            }
                        }
                        else {
                            Container frame = card1.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.INFO, "Message dialog shown to user indicating bad input");
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        bCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User interacted with clear form button");
                bPTNTF.setText("");
                bTYPETF.setText("");
                bSPECTF.setText("");
                bMCTF.setText("");
                bWEIGHTTF.setText("");
                bVOLTF.setText("");
                bDIMTF.setText("");
            }
        });

        // Panel for adding a new Crate
        JPanel card2 = new JPanel(new GridLayout(10,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel mPTN = new JLabel("VIN (string)");
        JTextField mPTNTF = new JTextField("", 12);
        card2.add(mPTN);
        card2.add(mPTNTF);
        JLabel mTYPE = new JLabel("Type (string)");
        JTextField mTYPETF = new JTextField("", 12);
        card2.add(mTYPE);
        card2.add(mTYPETF);
        JLabel mSPEC = new JLabel("Specification (string)");
        JTextField mSPECTF = new JTextField("", 12);
        card2.add(mSPEC);
        card2.add(mSPECTF);
        JLabel mMC = new JLabel("Mailing Class (string)");
        JTextField mMCTF = new JTextField("", 12);
        card2.add(mMC);
        card2.add(mMCTF);
        JLabel mWEIGHT = new JLabel("Weight (int)");
        JTextField mWEIGHTTF = new JTextField("", 12);
        card2.add(mWEIGHT);
        card2.add(mWEIGHTTF);
        JLabel mVOL = new JLabel("Volume (int)");
        JTextField mVOLTF = new JTextField("", 12);
        card2.add(mVOL);
        card2.add(mVOLTF);
        JLabel mLW = new JLabel("Load Weight (float)");
        JTextField mLWTF = new JTextField("", 12);
        card2.add(mLW);
        card2.add(mLWTF);
        JLabel mCON = new JLabel("Content (String)");
        JTextField mCONTF = new JTextField("", 12);
        card2.add(mCON);
        card2.add(mCONTF);
        JButton mSUBMIT = new JButton ("Submit");
        card2.add(mSUBMIT);
        JButton mCLEAR = new JButton("Clear");
        card2.add(mCLEAR);
        exitFrom2 = new JButton("Exit");
        card2.add(exitFrom2);

        exitFrom2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User exited from Add Package gracefully via exit JButton");
                Container frame = exitFrom2.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        mSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String PTN, type, spec, mailClass, content;
                        int volume;
                        float weight, loadWeight;
                        ArrayList<String> err = new ArrayList<>();

                        if (mPTNTF.getText().length() == 0) {
                            err.add("Field 'PTN' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, PTN");
                        }
                        else if (mPTNTF.getText().length() > 5) {
                            err.add("'PTN' length is too long!");
                            logger.log(Level.WARNING, "User submitted String that is too long");
                        }
                        else if (ss.packageMatch(mPTNTF.getText())) {
                            err.add("'PTN' already exists in the database!");
                            logger.log(Level.WARNING, "User submitted PTN that already exists");
                        }

                        PTN = mPTNTF.getText();


                        if (mTYPETF.getText().length() == 0) {
                            err.add("Field 'Type' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Type");
                        }

                        type = mTYPETF.getText();
                        boolean corr = false;
        
                        corr = type.equalsIgnoreCase("Crate");
                        if (!(corr)) {
                            err.add("Field 'Type' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Type");
                        }

                        if (mSPECTF.getText().length() == 0) {
                            err.add("Field 'Specification' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Specification");
                        }

                        spec = mSPECTF.getText();

                        corr = spec.equalsIgnoreCase("Fragile") || spec.equalsIgnoreCase("Books") 
                            || spec.equalsIgnoreCase("Catalogs");
                        corr = corr || spec.equalsIgnoreCase("Do-not-bend") || spec.equalsIgnoreCase("N/A");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (mMCTF.getText().length() == 0) {
                            err.add("'Mailing Class' value is invalid");
                            logger.log(Level.WARNING, "User submitted an empty field, Mailing Class");
                        }

                        mailClass = mMCTF.getText();
                        
                        corr = spec.equalsIgnoreCase("First-Class") || spec.equalsIgnoreCase("Priority") 
                            || spec.equalsIgnoreCase("Retail");
                        corr = corr || spec.equalsIgnoreCase("Ground") || spec.equalsIgnoreCase("Metro");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (mWEIGHTTF.getText().length() == 0) {
                            err.add("Field 'Weight' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Weight");
                        }
                        else if (Float.parseFloat(mWEIGHTTF.getText()) < 0) {
                            err.add("'Weight' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Weight");
                        }

                        weight = Float.parseFloat(mWEIGHTTF.getText());

                        if (mVOLTF.getText().length() == 0) {
                            err.add("Field 'Volume' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Volume");
                        }
                        else if (Integer.parseInt(mVOLTF.getText()) < 0) {
                            err.add("'Volume' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value for 'Volume'");
                        }

                        volume = Integer.parseInt(mVOLTF.getText());
                        
                        if (mLWTF.getText().length() == 0) {
                            err.add("Field 'Load Weight' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Load Weight");
                        }
                        else if (Float.parseFloat(mLWTF.getText()) < 0) {
                            err.add("'Load Weight' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Load Weight");
                        }

                        loadWeight = Float.parseFloat(mLWTF.getText());
                        
                        if (mCONTF.getText().length() == 0) {
                            err.add("'Content' value is invalid");
                            logger.log(Level.WARNING, "User submitted an empty field, Content");
                        }

                        content = mCONTF.getText();


                        if (err.isEmpty()) {
                            Crate newObj = new Crate(PTN, type, spec, mailClass, weight, volume, loadWeight, content);
                            if (ss.addPackageDirectly(newObj)) {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "Crate has been successfully added!\n" +
                                                "You may continue to add packages by pressing \"Ok\" \n" +
                                                "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                "then \"Exit\" (in the 'Adding package' window)",
                                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "Crate object added successfully!");
                            }
                            else {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addPackageDirectly(Package) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.INFO, "addPackageDirectly failed unexpectedly");
                            }
                        }
                        else {
                            Container frame = card2.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        mCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User cleared the form for adding a motorcycle");
                mPTNTF.setText("");
                mTYPETF.setText("");
                mSPECTF.setText("");
                mMCTF.setText("");
                mWEIGHTTF.setText("");
                mVOLTF.setText("");
                mLWTF.setText("");
                mCONTF.setText("");
            }
        });

        // Panel for adding a new Drum
        JPanel card3 = new JPanel(new GridLayout(10,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel tPTN = new JLabel("PTN (string)");
        JTextField tPTNTF = new JTextField("", 12);
        card3.add(tPTN);
        card3.add(tPTNTF);
        JLabel tTYPE = new JLabel("Type (string)");
        JTextField tTYPETF = new JTextField("", 12);
        card3.add(tTYPE);
        card3.add(tTYPETF);
        JLabel tSPEC = new JLabel("Specification (string)");
        JTextField tSPECTF = new JTextField("", 12);
        card3.add(tSPEC);
        card3.add(tSPECTF);
        JLabel tMC = new JLabel("Mailing Class (string)");
        JTextField tMCTF = new JTextField("", 12);
        card3.add(tMC);
        card3.add(tMCTF);
        JLabel tWEIGHT = new JLabel("Weight (int)");
        JTextField tWEIGHTTF = new JTextField("", 12);
        card3.add(tWEIGHT);
        card3.add(tWEIGHTTF);
        JLabel tVOL = new JLabel("Volume (int)");
        JTextField tVOLTF = new JTextField("", 12);
        card3.add(tVOL);
        card3.add(tVOLTF);
        JLabel tDIAM = new JLabel("Diameter (float)");
        JTextField tDIAMTF = new JTextField("", 12);
        card3.add(tDIAM);
        card3.add(tDIAMTF);
        JLabel tMAT = new JLabel("Material (String)");
        JTextField tMATTF = new JTextField("", 12);
        card3.add(tMAT);
        card3.add(tMATTF);
        JButton tSUBMIT = new JButton ("Submit");
        card3.add(tSUBMIT);
        JButton tCLEAR = new JButton("Clear");
        card3.add(tCLEAR);
        exitFrom3 = new JButton("Exit");
        card3.add(exitFrom3);
       

        exitFrom3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Container frame = exitFrom3.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        tSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String PTN, type, spec, mailClass, material;
                        int volume;
                        float weight, diameter;
                        ArrayList<String> err = new ArrayList<>();

                        if (tPTNTF.getText().length() == 0) {
                            err.add("Field 'PTN' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, PTN");
                        }
                        else if (tPTNTF.getText().length() > 5) {
                            err.add("'PTN' length is too long!");
                            logger.log(Level.WARNING, "User submitted String that is too long");
                        }
                        else if (ss.packageMatch(tPTNTF.getText())) {
                            err.add("'PTN' already exists in the database!");
                            logger.log(Level.WARNING, "User submitted PTN that already exists");
                        }

                        PTN = tPTNTF.getText();

                        if (tTYPETF.getText().length() == 0) {
                            err.add("Field 'Type' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Type");
                        }

                        type = tTYPETF.getText();
                        boolean corr = false;
        
                        corr = type.equalsIgnoreCase("Drum");
                        if (!(corr)) {
                            err.add("Field 'Type' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Type");
                        }

                        if (tSPECTF.getText().length() == 0) {
                            err.add("Field 'Specification' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Specification");
                        }

                        spec = tSPECTF.getText();

                        corr = spec.equalsIgnoreCase("Fragile") || spec.equalsIgnoreCase("Books") 
                            || spec.equalsIgnoreCase("Catalogs");
                        corr = corr || spec.equalsIgnoreCase("Do-not-bend") || spec.equalsIgnoreCase("N/A");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (tMCTF.getText().length() == 0) {
                            err.add("'Mailing Class' value is invalid");
                            logger.log(Level.WARNING, "User submitted an empty field, Mailing Class");
                        }

                        mailClass = tMCTF.getText();
                        
                        corr = spec.equalsIgnoreCase("First-Class") || spec.equalsIgnoreCase("Priority") 
                            || spec.equalsIgnoreCase("Retail");
                        corr = corr || spec.equalsIgnoreCase("Ground") || spec.equalsIgnoreCase("Metro");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (tWEIGHTTF.getText().length() == 0) {
                            err.add("Field 'Weight' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Weight");
                        }
                        else if (Float.parseFloat(tWEIGHTTF.getText()) < 0) {
                            err.add("'Weight' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Weight");
                        }

                        weight = Float.parseFloat(tWEIGHTTF.getText());

                        if (tVOLTF.getText().length() == 0) {
                            err.add("Field 'Volume' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Volume");
                        }
                        else if (Integer.parseInt(tVOLTF.getText()) < 0) {
                            err.add("'Volume' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value for 'Volume'");
                        }

                        volume = Integer.parseInt(tVOLTF.getText());
                        
                        if (tDIAMTF.getText().length() == 0) {
                            err.add("Field 'Load Weight' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Load Weight");
                        }
                        else if (Float.parseFloat(tDIAMTF.getText()) < 0) {
                            err.add("'Load Weight' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Load Weight");
                        }

                        diameter = Float.parseFloat(tDIAMTF.getText());
                        
                        if (tMATTF.getText().length() == 0) {
                            err.add("'Content' value is invalid");
                            logger.log(Level.WARNING, "User submitted an empty field, Content");
                        }

                        material = tMATTF.getText();

                        if (err.isEmpty()) {
                            Drum newObj = new Drum(PTN, type, spec, mailClass, 
                                    weight, volume, material, diameter);
                            if (ss.addPackageDirectly(newObj)) {
                                Container frame = card3.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "Drum has been successfully added!\n" +
                                                "You may continue to add packages by pressing \"Ok\" \n" +
                                                "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                "then \"Exit\" (in the 'Adding package' window)",
                                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "User added a drum object successfully");
                            }
                            else {
                                Container frame = card3.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addPackageDirectly(Package) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.SEVERE, "addPackageDirectly failed");
                            }
                        }
                        else {
                            Container frame = card3.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        tCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tPTNTF.setText("");
                tTYPETF.setText("");
                tSPECTF.setText("");
                tMCTF.setText("");
                tWEIGHTTF.setText("");
                tVOLTF.setText("");
                tDIAMTF.setText("");
                tMATTF.setText("");
            }
        });
        
        // Panel for adding a new Envelope
        JPanel card4 = new JPanel(new GridLayout(10,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel ePTN = new JLabel("PTN (string)");
        JTextField ePTNTF = new JTextField("", 12);
        card4.add(ePTN);
        card4.add(ePTNTF);
        JLabel eTYPE = new JLabel("Type (string)");
        JTextField eTYPETF = new JTextField("", 12);
        card4.add(eTYPE);
        card4.add(eTYPETF);
        JLabel eSPEC = new JLabel("Specification (string)");
        JTextField eSPECTF = new JTextField("", 12);
        card4.add(eSPEC);
        card4.add(eSPECTF);
        JLabel eMC = new JLabel("Mailing Class (string)");
        JTextField eMCTF = new JTextField("", 12);
        card4.add(eMC);
        card4.add(eMCTF);
        JLabel eWEIGHT = new JLabel("Weight (int)");
        JTextField eWEIGHTTF = new JTextField("", 12);
        card4.add(eWEIGHT);
        card4.add(eWEIGHTTF);
        JLabel eVOL = new JLabel("Volume (int)");
        JTextField eVOLTF = new JTextField("", 12);
        card4.add(eVOL);
        card4.add(eVOLTF);
        JLabel eHEIGHT = new JLabel("Height (int)");
        JTextField eHEIGHTTF = new JTextField("", 12);
        card4.add(eHEIGHT);
        card4.add(eHEIGHTTF);
        JLabel eWIDTH = new JLabel("Width (int)");
        JTextField eWIDTHTF = new JTextField("", 12);
        card4.add(eWIDTH);
        card4.add(eWIDTHTF);
        JButton eSUBMIT = new JButton ("Submit");
        card4.add(eSUBMIT);
        JButton eCLEAR = new JButton("Clear");
        card4.add(eCLEAR);
        exitFrom4 = new JButton("Exit");
        card4.add(exitFrom4);
       

        exitFrom4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Container frame = exitFrom4.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        eSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String PTN, type, spec, mailClass;
                        int volume, height, width;
                        float weight;
                        ArrayList<String> err = new ArrayList<>();

                        if (ePTNTF.getText().length() == 0) {
                            err.add("Field 'PTN' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, PTN");
                        }
                        else if (ePTNTF.getText().length() > 5) {
                            err.add("'PTN' length is too long!");
                            logger.log(Level.WARNING, "User submitted String that is too long");
                        }
                        else if (ss.packageMatch(ePTNTF.getText())) {
                            err.add("'PTN' already exists in the database!");
                            logger.log(Level.WARNING, "User submitted PTN that already exists");
                        }

                        PTN = ePTNTF.getText();


                        if (eTYPETF.getText().length() == 0) {
                            err.add("Field 'Type' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Type");
                        }

                        type = eTYPETF.getText();
                        boolean corr = false;
        
                        corr = type.equalsIgnoreCase("Envelope");
                        if (!(corr)) {
                            err.add("Field 'Type' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Type");
                        }

                        if (eSPECTF.getText().length() == 0) {
                            err.add("Field 'Specification' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Specification");
                        }

                        spec = eSPECTF.getText();

                        corr = spec.equalsIgnoreCase("Fragile") || spec.equalsIgnoreCase("Books") 
                            || spec.equalsIgnoreCase("Catalogs");
                        corr = corr || spec.equalsIgnoreCase("Do-not-bend") || spec.equalsIgnoreCase("N/A");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (eMCTF.getText().length() == 0) {
                            err.add("'Mailing Class' value is invalid");
                            logger.log(Level.WARNING, "User submitted an empty field, Mailing Class");
                        }

                        mailClass = eMCTF.getText();
                        
                        corr = spec.equalsIgnoreCase("First-Class") || spec.equalsIgnoreCase("Priority") 
                            || spec.equalsIgnoreCase("Retail");
                        corr = corr || spec.equalsIgnoreCase("Ground") || spec.equalsIgnoreCase("Metro");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (eWEIGHTTF.getText().length() == 0) {
                            err.add("Field 'Weight' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Weight");
                        }
                        else if (Float.parseFloat(eWEIGHTTF.getText()) < 0) {
                            err.add("'Weight' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Weight");
                        }

                        weight = Float.parseFloat(eWEIGHTTF.getText());

                        if (eVOLTF.getText().length() == 0) {
                            err.add("Field 'Volume' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Volume");
                        }
                        else if (Integer.parseInt(eVOLTF.getText()) < 0) {
                            err.add("'Volume' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value for 'Volume'");
                        }

                        volume = Integer.parseInt(eVOLTF.getText());
                        
                        if (eHEIGHTTF.getText().length() == 0) {
                            err.add("Field 'Height' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Height");
                        }
                        else if (Integer.parseInt(eHEIGHTTF.getText()) < 0) {
                            err.add("'Height' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Height");
                        }

                        height = Integer.parseInt(eHEIGHTTF.getText());
                        
                        if (eWIDTHTF.getText().length() == 0) {
                            err.add("Field 'Width' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Width");
                        }
                        else if (Integer.parseInt(eWIDTHTF.getText()) < 0) {
                            err.add("'Width' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Width");
                        }

                        width = Integer.parseInt(eWIDTHTF.getText());

                        if (err.isEmpty()) {
                            Envelope newObj = new Envelope(PTN, type, spec, mailClass, 
                                    weight, volume, height, width);
                            if (ss.addPackageDirectly(newObj)) {
                                Container frame = card4.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "Envelope has been successfully added!\n" +
                                                "You may continue to add packages by pressing \"Ok\" \n" +
                                                "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                "then \"Exit\" (in the 'Adding package' window)",
                                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "User added a Envelope object successfully");
                            }
                            else {
                                Container frame = card4.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addPackageDirectly(Package) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.SEVERE, "addPackageDirectly failed");
                            }
                        }
                        else {
                            Container frame = card4.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        eCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ePTNTF.setText("");
                eTYPETF.setText("");
                eSPECTF.setText("");
                eMCTF.setText("");
                eWEIGHTTF.setText("");
                eVOLTF.setText("");
                eHEIGHTTF.setText("");
                eWIDTHTF.setText("");
            }
        });
        
        // Panel for adding a new Package
        JPanel card5 = new JPanel(new GridLayout(10,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel oPTN = new JLabel("PTN (string)");
        JTextField oPTNTF = new JTextField("", 12);
        card5.add(oPTN);
        card5.add(oPTNTF);
        JLabel oTYPE = new JLabel("Type (string)");
        JTextField oTYPETF = new JTextField("", 12);
        card5.add(oTYPE);
        card5.add(oTYPETF);
        JLabel oSPEC = new JLabel("Specification (string)");
        JTextField oSPECTF = new JTextField("", 12);
        card5.add(oSPEC);
        card5.add(tSPECTF);
        JLabel oMC = new JLabel("Mailing Class (string)");
        JTextField oMCTF = new JTextField("", 12);
        card5.add(oMC);
        card5.add(oMCTF);
        JLabel oWEIGHT = new JLabel("Weight (int)");
        JTextField oWEIGHTTF = new JTextField("", 12);
        card5.add(oWEIGHT);
        card5.add(oWEIGHTTF);
        JLabel oVOL = new JLabel("Volume (int)");
        JTextField oVOLTF = new JTextField("", 12);
        card5.add(oVOL);
        card5.add(oVOLTF);
        JButton oSUBMIT = new JButton ("Submit");
        card5.add(oSUBMIT);
        JButton oCLEAR = new JButton("Clear");
        card5.add(oCLEAR);
        exitFrom5 = new JButton("Exit");
        card5.add(exitFrom5);
       

        exitFrom5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Container frame = exitFrom5.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        oSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String PTN, type, spec, mailClass, material;
                        int volume;
                        float weight, diameter;
                        ArrayList<String> err = new ArrayList<>();

                        if (oPTNTF.getText().length() == 0) {
                            err.add("Field 'PTN' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, PTN");
                        }
                        else if (oPTNTF.getText().length() > 5) {
                            err.add("'PTN' length is too long!");
                            logger.log(Level.WARNING, "User submitted String that is too long");
                        }
                        else if (ss.packageMatch(oPTNTF.getText())) {
                            err.add("'PTN' already exists in the database!");
                            logger.log(Level.WARNING, "User submitted PTN that already exists");
                        }

                        PTN = oPTNTF.getText();

                        if (oTYPETF.getText().length() == 0) {
                            err.add("Field 'Type' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Type");
                        }

                        type = oTYPETF.getText();
                        boolean corr = false;
        
                        corr = type.equalsIgnoreCase("PostCard") || type.equalsIgnoreCase("Letter") 
                            || type.equalsIgnoreCase("Packet");
                        corr = corr || type.equalsIgnoreCase("Roll") || type.equalsIgnoreCase("Tube");
                        if (!(corr)) {
                            err.add("Field 'Type' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Type");
                        }

                        if (oSPECTF.getText().length() == 0) {
                            err.add("Field 'Specification' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Specification");
                        }

                        spec = oSPECTF.getText();

                        corr = spec.equalsIgnoreCase("Fragile") || spec.equalsIgnoreCase("Books") 
                            || spec.equalsIgnoreCase("Catalogs");
                        corr = corr || spec.equalsIgnoreCase("Do-not-bend") || spec.equalsIgnoreCase("N/A");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (oMCTF.getText().length() == 0) {
                            err.add("'Mailing Class' value is invalid");
                            logger.log(Level.WARNING, "User submitted an empty field, Mailing Class");
                        }

                        mailClass = oMCTF.getText();
                        
                        corr = spec.equalsIgnoreCase("First-Class") || spec.equalsIgnoreCase("Priority") 
                            || spec.equalsIgnoreCase("Retail");
                        corr = corr || spec.equalsIgnoreCase("Ground") || spec.equalsIgnoreCase("Metro");
                        if (!(corr)) {
                            err.add("Field 'Specification' is incorrect");
                            logger.log(Level.WARNING, "User submitted an incorrect field, Specification");
                        }

                        if (oWEIGHTTF.getText().length() == 0) {
                            err.add("Field 'Weight' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Weight");
                        }
                        else if (Float.parseFloat(oWEIGHTTF.getText()) < 0) {
                            err.add("'Weight' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Weight");
                        }

                        weight = Float.parseFloat(oWEIGHTTF.getText());

                        if (oVOLTF.getText().length() == 0) {
                            err.add("Field 'Volume' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Volume");
                        }
                        else if (Integer.parseInt(oVOLTF.getText()) < 0) {
                            err.add("'Volume' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value for 'Volume'");
                        }

                        volume = Integer.parseInt(oVOLTF.getText());

                        if (err.isEmpty()) {
                            Package newObj = new Package(PTN, type, spec, mailClass, 
                                    weight, volume);
                            if (ss.addPackageDirectly(newObj)) {
                                Container frame = card5.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "Package has been successfully added!\n" +
                                                "You may continue to add packages by pressing \"Ok\" \n" +
                                                "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                "then \"Exit\" (in the 'Adding package' window)",
                                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "User added a package object successfully");
                            }
                            else {
                                Container frame = card5.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addPackageDirectly(Package) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.SEVERE, "addPackageDirectly failed");
                            }
                        }
                        else {
                            Container frame = card5.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        oCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                oPTNTF.setText("");
                oTYPETF.setText("");
                oSPECTF.setText("");
                oMCTF.setText("");
                oWEIGHTTF.setText("");
                oVOLTF.setText("");
            }
        });

        tabbedPane.addTab(BOXPANEL, card1);
        tabbedPane.addTab(CRATEPANEL, card2);
        tabbedPane.addTab(DRUMPANEL, card3);
        tabbedPane.addTab(ENVELOPEPANEL, card4);
        tabbedPane.addTab(PACKAGEPANEL, card5);

        pane.add(tabbedPane, BorderLayout.WEST);
    }
    
    /**
     * The main method of the program.
     *
     * @param args the command line arguments
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainAppGUI exe = new MainAppGUI("ShippingStore Management Application v1.0");
                exe.pack();
                exe.setVisible(true);
            }
        });

    }
}
