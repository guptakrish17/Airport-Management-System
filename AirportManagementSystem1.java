import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

// Interface representing airport experiences
interface Experience {
    void start(); // Interface method for starting the experience
}

// Interface representing safety procedures
interface SafetyProcedure {
    void performSecurityCheck(); // Interface method for performing security checks
}

// Abstract class representing common airport procedures
abstract class AirportProcedure {
    public abstract void displayAvailableFlights(); // Abstract method for displaying available flights

    public abstract void experiencesAtTheAirport(); // Abstract method for experiencing the airport
    public abstract void visaRegulations(); // Abstract method for displaying visa regulations

    public abstract void covid19Advisory(); // Abstract method for displaying COVID-19 advisories
}

// Class representing a Flight
class Flight {
    private String flightNumber; // Variable to store flight number

    private String destination; // Variable to store flight destination

    private Date departureTime; // Variable to store departure time

    private int availableSeats; // Variable to store the number of available seats

    // Constructor for Flight class
    public Flight(String flightNumber, String destination, Date departureTime, int availableSeats) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    // Getters for Flight class variables
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}

// Class representing an Airport
class Airport extends AirportProcedure implements SafetyProcedure {
    private ArrayList<Flight> flights; // Use ArrayList instead of array
    private int flightCount; // Variable to keep track of the number of flights
    private int maxFlights; // Maximum number of flights
    // JDBC configuration
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/Airport_Management_System";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Krish123#";

    // Arrays to track hotel and lounge availability
    private boolean[] hotelAvailability = new boolean[24];
    private boolean[] loungeAvailability = new boolean[24];
    private boolean[] culturalShops = { false, false, false };

    // SimpleDateFormat for date parsing
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Constructor for Airport class
    public Airport(int maxFlights) {
        this.maxFlights = maxFlights;
        flights = new ArrayList<>(maxFlights);
        flightCount = 0;
    }

    // Method to add a flight to the flights array
    public void addFlight(Flight flight) {
        if (flightCount < maxFlights) {
            flights.add(flight);
            flightCount++;
            // Update the database
            updateFlightInDatabase(flight);
        } else {
            System.out.println("Airport is full. Cannot add more flights.");
        }
    }

    // Method to update flight in the database
    private void updateFlightInDatabase(Flight flight) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String insertQuery = "INSERT INTO flights (flight_number, destination, departure_time, available_seats) "
                        +
                        "VALUES (?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, flight.getFlightNumber());
                    preparedStatement.setString(2, flight.getDestination());

                    // Convert Date to SQL Timestamp
                    java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(flight.getDepartureTime().getTime());
                    preparedStatement.setTimestamp(3, sqlTimestamp);

                    preparedStatement.setInt(4, flight.getAvailableSeats());

                    // Execute the update
                    preparedStatement.executeUpdate();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating flight in the database.");
        }
    }

    public void performSecurityCheck() {
        System.out.println("Security Check Procedure:");
        System.out.println("Please proceed through the security check area.");

        System.out.println("Step 1: Present your boarding pass and identification.");
        System.out.println("Step 2: Place all belongings in the scanning machine.");
        System.out.println("Step 3: Walk through the metal detector.");
        System.out.println("Step 4: Additional security screening may be required for certain items.");

        System.out.println("Security check complete. Have a safe and pleasant journey!");
    }

    public void displayAvailableFlights() {
        System.out.println("Available Flights:");
        for (Flight flight : flights) {
            System.out.println("Flight Number: " + flight.getFlightNumber());
            System.out.println("Destination: " + flight.getDestination());
            System.out.println("Departure Time: " + flight.getDepartureTime());
            System.out.println("Available Seats: " + flight.getAvailableSeats());
            System.out.println();
        }
    }

    public void experiencesAtTheAirport() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayAvailableExperiences();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    bookAirportTransitHotel();
                    break;
                case 2:
                    bookAirportLoungeAccess();
                    break;
                case 3:
                    exploreCulturalShops();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayAvailableExperiences() {
        System.out.println("Experiences at the Airport:");
        System.out.print("1. Book Airport Transit Hotel                              ");
        System.out.println("2. Book Airport Lounge Access");
        System.out.print("3. Explore Cultural Shops                                  ");
        System.out.println("4. Back to Main Menu");
        System.out.print("Select an option: ");
    }

    private void bookAirportTransitHotel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                "Layover for long in Mumbai? Why rush into the hustle-bustle of the city, when you can stay cocooned at the Airport Transit Hotel. For a weary traveler, our hotel is a treat for your sensory organs.\r\n"
                        + "\r\n" + "Don’t go too far!\r\n"
                        + "The transit hotel extends its welcome to you right in the heart of the airport. All you must do is show up and check in.\r\n"
                        + "\r\n"
                        + "Nestled within the airport, Terminal 2\r\n" + "Instant Arrival and Departure assistance\r\n"
                        + "Dynamic pricing\r\n" + "Hourly rates\r\n"
                        + "24-hour check-ins and check-outs\r\n" + "Quick luggage storage");

        System.out.print("Enter your desired check-in time (yyyy-MM-dd HH:mm:ss): ");
        String checkInTimeString = scanner.nextLine();

        try {
            Date checkInTime = dateFormat.parse(checkInTimeString);
            int hour = checkInTime.getHours();

            if (hour >= 0 && hour < 24) {
                if (!hotelAvailability[hour]) {
                    System.out.println("Airport Transit Hotel booked successfully for " + checkInTimeString);
                    hotelAvailability[hour] = true;
                } else {
                    System.out.println("The hotel for this time slot is already booked. Please choose another time.");
                }
            } else {
                System.out.println("Invalid time slot. Please enter a valid time between 00:00:00 and 23:59:59.");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm:ss format.");
        }
    }

    private void bookAirportLoungeAccess() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Relax before you take-off with special privileges at Lounge.");

        System.out.print("Enter your desired entry time (yyyy-MM-dd HH:mm:ss): ");
        String entryTimeString = scanner.nextLine();

        try {
            Date entryTime = dateFormat.parse(entryTimeString);
            int hour = entryTime.getHours();

            if (hour >= 0 && hour < 24) {
                if (!loungeAvailability[hour]) {
                    System.out.println("Airport Lounge Access booked successfully for " + entryTimeString);
                    loungeAvailability[hour] = true;
                } else {
                    System.out.println("The lounge for this time slot is already booked. Please choose another time.");
                }
            } else {
                System.out.println("Invalid time slot. Please enter a valid time between 00:00:00 and 23:59:59.");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm:ss format.");
        }
    }

    private void exploreCulturalShops() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Explore Cultural Shops:");
        System.out.println("1. Visit Shop 1");
        System.out.println("2. Visit Shop 2");
        System.out.println("3. Visit Shop 3");
        System.out.println("4. Return to Experiences Menu");
        System.out.print("Select an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice >= 1 && choice <= 3) {
            int shopIndex = choice - 1;
            if (!culturalShops[shopIndex]) {
                System.out.println("You are exploring Shop " + (shopIndex + 1) + " at the airport.");
                culturalShops[shopIndex] = true;
            } else {
                System.out.println("You have already visited this shop.");
            }
        } else if (choice == 4) {
            // Optional: Add logic if needed when returning to the main menu
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    // Method to provide visa regulations for international travelers
    public void visaRegulations() {
        System.out.println("Visa Regulations for International Travelers:");
        System.out.println("1. Visa requirements vary by country and purpose of travel.");
        System.out.println("2. Please check with your country's embassy or consulate for specific visa information.");
        System.out.println("3. Ensure your passport is valid for at least six months beyond your planned return date.");
        System.out.println();
    }

    // Method to provide COVID-19 advisory
    public void covid19Advisory() {
        System.out.println("COVID-19 Advisory:");
        System.out.println("1. Follow all airport and airline guidelines regarding COVID-19 safety measures.");
        System.out.println("2. Wear a mask and maintain social distancing.");
        System.out.println("3. Stay updated on travel restrictions and requirements.");
        System.out.println("\n");
    }

    // Method for web check-in
    public void webCheckIn() {
        System.out.println("Web check-in is available. Please visit the airline's website for check-in.");
    }
}

class Entry {
    static {
        System.out.println("Welcome to Chhatrapati Shivaji International Airport");
    }
}

// Main class for the Airport Management System
public class AirportManagementSystem1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airport airport = new Airport(10); // Maximum of 10 flights

        while (true) {
            System.out.println("Welcome to Chhatrapati Shivaji International Airport");
            System.out.print("1. Display Available Flights                                           ");
            System.out.println("2. Experiences at the Airport");
            System.out.print("3. Add a Flight                                                        ");
            System.out.println("4. Web Check-in");
            System.out.print("5. Visa Regulations for International Travelers                        ");
            System.out.println("6. COVID-19 Advisory");
            System.out.print("7. Security Checking Procedure                                         ");
            System.out.println("8. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    airport.displayAvailableFlights();
                    break;
                case 2:
                    airport.experiencesAtTheAirport();
                    break;
                case 3:
                    addFlight(airport);
                    break;
                case 4:
                    airport.webCheckIn();
                    break;
                case 5:
                    airport.visaRegulations();
                    break;
                case 6:
                    airport.covid19Advisory();
                    break;
                case 7:
                    airport.performSecurityCheck();
                    break;
                case 8:
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method to add a flight
    private static void addFlight(Airport airport) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Flight Number: ");
        String flightNumber = scanner.nextLine();
        System.out.print("Enter Destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter Departure Time (yyyy-MM-dd HH:mm:ss): ");
        String departureTimeString = scanner.nextLine();
        System.out.print("Enter Available Seats: ");
        int availableSeats = scanner.nextInt();
        scanner.nextLine();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date departureTime;
        try {
            departureTime = dateFormat.parse(departureTimeString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Flight not added.");
            return;
        }

        Flight newFlight = new Flight(flightNumber, destination, departureTime, availableSeats);
        airport.addFlight(newFlight);
        System.out.println("Flight added successfully.");
    }
}
