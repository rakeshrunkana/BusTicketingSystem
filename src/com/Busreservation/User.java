package com.Busreservation;
import java.util.*;

class User {
    String username;
    String password;
    String gender;
    int age;
    List<Ticket> tickets;

    public User(String username, String password, String gender, int age) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.tickets = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}

class Ticket {
    int ticketId;
    String busNumber;
    String source;
    String destination;
    Date travelDate;
    String seatNumber;
    double price;
    String timings;
    String username;
    String gender;

    public Ticket(int ticketId, String busNumber, String source, String destination, Date travelDate, String seatNumber, double price, String timings, String username, String gender) {
        this.ticketId = ticketId;
        this.busNumber = busNumber;
        this.source = source;
        this.destination = destination;
        this.travelDate = travelDate;
        this.seatNumber = seatNumber;
        this.price = price;
        this.timings = timings;
        this.username = username;
        this.gender = gender;
    }

    public int getTicketId() {
        return ticketId;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketId + ", Username: " + username + ", Gender: " + gender + 
               ", Bus Number: " + busNumber + ", From: " + source + ", To: " + destination +
               ", Travel Date: " + travelDate + ", Seat: " + seatNumber + ", Price: $" + price +
               ", Timings: " + timings;
    }
}

class Bus {
    String busNumber;
    String source;
    String destination;
    String timings;

    public Bus(String busNumber, String source, String destination, String timings) {
        this.busNumber = busNumber;
        this.source = source;
        this.destination = destination;
        this.timings = timings;
    }

    @Override
    public String toString() {
        return "Bus Number: " + busNumber + ", From: " + source + " To: " + destination + ", Timings: " + timings;
    }
}

class BusTicketReservationSystem {
    private Map<String, User> users;
    private int ticketCounter;
    private List<Bus> buses;
    private Map<String, Map<String, Double>> distanceMap;

    public BusTicketReservationSystem() {
        users = new HashMap<>();
        ticketCounter = 1;
        buses = new ArrayList<>();
        distanceMap = new HashMap<>();
        initializeBuses();
        initializeDistanceMap();
    }

    private void initializeBuses() {
        buses.add(new Bus("HYD-VZG-001", "Hyderabad", "Vizag", "10:00 AM - 05:00 PM"));
        buses.add(new Bus("VZG-VZM-002", "Vizag", "Vizianagaram", "06:00 PM - 07:00 PM"));
        buses.add(new Bus("VZM-PVP-003", "Vizianagaram", "Parvathipuram", "08:00 PM - 10:00 PM"));
        buses.add(new Bus("HYD-VJA-004", "Hyderabad", "Vijayawada", "09:00 AM - 01:00 PM"));
        buses.add(new Bus("VJA-GNT-005", "Vijayawada", "Guntur", "02:00 PM - 03:00 PM"));
        buses.add(new Bus("HYD-MUM-006", "Hyderabad", "Mumbai", "07:00 AM - 09:00 PM"));
        buses.add(new Bus("HYD-CHN-007", "Hyderabad", "Chennai", "08:00 AM - 08:00 PM"));
        buses.add(new Bus("HYD-BLR-008", "Hyderabad", "Bengaluru", "06:00 AM - 06:00 PM"));
        buses.add(new Bus("BLR-PUN-009", "Bengaluru", "Pune", "09:00 AM - 09:00 PM"));
        buses.add(new Bus("DEL-MUM-010", "Delhi", "Mumbai", "05:00 AM - 08:00 PM"));
    }

    private void initializeDistanceMap() {
        // Initialize distances in kilometers
        addDistance("Hyderabad", "Vizag", 620);
        addDistance("Vizag", "Vizianagaram", 40);
        addDistance("Vizianagaram", "Parvathipuram", 90);
        addDistance("Hyderabad", "Vijayawada", 275);
        addDistance("Vijayawada", "Guntur", 35);
        addDistance("Hyderabad", "Mumbai", 710);
        addDistance("Hyderabad", "Chennai", 630);
        addDistance("Hyderabad", "Bengaluru", 570);
        addDistance("Bengaluru", "Pune", 840);
        addDistance("Delhi", "Mumbai", 1440);
    }

    private void addDistance(String city1, String city2, double distance) {
        distanceMap.computeIfAbsent(city1, k -> new HashMap<>()).put(city2, distance);
        distanceMap.computeIfAbsent(city2, k -> new HashMap<>()).put(city1, distance); // assuming symmetrical distance
    }

    private double calculatePrice(String source, String destination) {
        if (distanceMap.containsKey(source) && distanceMap.get(source).containsKey(destination)) {
            double distance = distanceMap.get(source).get(destination);
            return distance * 0.5; // Price per kilometer
        }
        return 0.0;
    }

    public boolean register(String username, String password, String gender, int age) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return false;
        }
        users.put(username, new User(username, password, gender, age));
        System.out.println("Registration successful!");
        return true;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            System.out.println("Login successful!");
            return user;
        }
        System.out.println("Invalid username or password!");
        return null;
    }

    public void bookTicket(User user, String source, String destination, Date travelDate, String seatNumber) {
        List<Bus> availableBuses = new ArrayList<>();
        for (Bus bus : buses) {
            if (bus.source.equals(source) && bus.destination.equals(destination)) {
                availableBuses.add(bus);
            }
        }

        if (!availableBuses.isEmpty()) {
            System.out.println("Available buses for your route:");
            for (int i = 0; i < availableBuses.size(); i++) {
                System.out.println((i + 1) + ". " + availableBuses.get(i));
            }

            System.out.print("Choose a bus by number: ");
            Scanner scanner = new Scanner(System.in);
            int busChoice = scanner.nextInt();
            Bus selectedBus = availableBuses.get(busChoice - 1);

            double price = calculatePrice(source, destination);
            Ticket ticket = new Ticket(ticketCounter++, selectedBus.busNumber, source, destination, travelDate, seatNumber, price, selectedBus.timings, user.getUsername(), user.getGender());
            user.addTicket(ticket);
            System.out.println("Ticket booked successfully! Ticket ID: " + ticket.getTicketId());
            System.out.println(ticket);
        } else {
            System.out.println("No buses available for the selected route.");
        }
    }

    public void cancelTicket(User user, int ticketId) {
        List<Ticket> tickets = user.getTickets();
        Ticket ticketToCancel = null;
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId() == ticketId) {
                ticketToCancel = ticket;
                break;
            }
        }
        if (ticketToCancel != null) {
            user.removeTicket(ticketToCancel);
            System.out.println("Ticket cancelled successfully!");
        } else {
            System.out.println("Ticket not found!");
        }
    }

    public void rescheduleTicket(User user, int ticketId, Date newTravelDate) {
        List<Ticket> tickets = user.getTickets();
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId() == ticketId) {
                ticket.setTravelDate(newTravelDate);
                System.out.println("Ticket rescheduled successfully!");
                return;
            }
        }
        System.out.println("Ticket not found!");
    }

    public void viewTickets(User user) {
        List<Ticket> tickets = user.getTickets();
        if (tickets.isEmpty()) {
            System.out.println("No tickets booked.");
        } else {
            for (Ticket ticket : tickets) {
                System.out.println(ticket);
            }
        }
    }

    public void showPaymentDetails(User user) {
        List<Ticket> tickets = user.getTickets();
        if (tickets.isEmpty()) {
            System.out.println("No payment details available.");
        } else {
            double totalAmount = 0.0;
            for (Ticket ticket : tickets) {
                totalAmount += ticket.getPrice();
            }
            System.out.println("Total amount paid by " + user.getUsername() + ": $" + totalAmount);
        }
    }
}

class BusTicketReservationSystemApp {
    public static void main(String[] args) {
    	System.out.println("Welcome to ABC Travells");
        BusTicketReservationSystem system = new BusTicketReservationSystem();
        Scanner scanner = new Scanner(System.in);
        User loggedInUser = null;

        while (true) {
            System.out.println("\n1. Register");
            System.out.println("\n2. Login");
            System.out.println("\n3. Book Ticket");
            System.out.println("\n4. Reschedule Ticket");
            System.out.println("\n5. Cancel Ticket");
            System.out.println("\n6. View Tickets");
            System.out.println("\n7. Show Payment Details");
            System.out.println("\n8. Logout");
            System.out.println("\n9. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter gender (M/F): ");
                    String gender = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    system.register(username, password, gender, age);
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    password = scanner.nextLine();
                    loggedInUser = system.login(username, password);
                    break;
                case 3:
                    if (loggedInUser != null) {
                        System.out.print("Enter source city: ");
                        String source = scanner.nextLine();
                        System.out.print("Enter destination city: ");
                        String destination = scanner.nextLine();
                        System.out.print("Enter travel date (yyyy-mm-dd): ");
                        String dateString = scanner.nextLine();
                        Date travelDate = new GregorianCalendar(Integer.parseInt(dateString.split("-")[0]), Integer.parseInt(dateString.split("-")[1]) - 1, Integer.parseInt(dateString.split("-")[2])).getTime();
                        System.out.print("Enter seat number: ");
                        String seatNumber = scanner.nextLine();
                        system.bookTicket(loggedInUser, source, destination, travelDate, seatNumber);
                    } else {
                        System.out.println("Please login first!");
                    }
                    break;
                case 4:
                    if (loggedInUser != null) {
                        System.out.print("Enter ticket ID to reschedule: ");
                        int ticketId = scanner.nextInt();
                        scanner.nextLine();  // Consume newline
                        System.out.print("Enter new travel date (yyyy-mm-dd): ");
                        String newDateString = scanner.nextLine();
                        Date newTravelDate = new GregorianCalendar(Integer.parseInt(newDateString.split("-")[0]), Integer.parseInt(newDateString.split("-")[1]) - 1, Integer.parseInt(newDateString.split("-")[2])).getTime();
                        system.rescheduleTicket(loggedInUser, ticketId, newTravelDate);
                    } else {
                        System.out.println("Please login first!");
                    }
                    break;
                case 5:
                    if (loggedInUser != null) {
                        System.out.print("Enter ticket ID to cancel: ");
                        int ticketId = scanner.nextInt();
                        scanner.nextLine();  // Consume newline
                        system.cancelTicket(loggedInUser, ticketId);
                    } else {
                        System.out.println("Please login first!");
                    }
                    break;
                case 6:
                    if (loggedInUser != null) {
                        system.viewTickets(loggedInUser);
                    } else {
                        System.out.println("Please login first!");
                    }
                    break;
                case 7:
                    if (loggedInUser != null) {
                        system.showPaymentDetails(loggedInUser);
                    } else {
                        System.out.println("Please login first!");
                    }
                    break;
                case 8:
                    loggedInUser = null;
                    System.out.println("Logged out successfully.");
                    break;
                case 9:
                    System.out.println("Exiting the system.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}


