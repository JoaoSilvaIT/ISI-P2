package jdbc;

import java.util.Scanner;

import jdbc.UI.App;

import java.io.IOException;
import java.sql.*;

/*
* 
* @author MP
* @version 1.0
* @since 2024-11-07
*/
public class Model {

    static String inputData(String str) throws IOException {
        // IMPLEMENTED
        /*
         * Gets input data from user
         * 
         * @param str Description of required input values
         * 
         * @return String containing comma-separated values
         */
        Scanner key = new Scanner(System.in); // Scanner closes System.in if you call close(). Don't do it
        System.out.println("Enter corresponding values, separated by commas of:");
        System.out.println(str);
        return key.nextLine();
    }

    static void addUser(User userData, Card cardData) {
        // PARCIALLY IMPLEMENTED
        /**
         * Adds a new user with associated card to the database
         * 
         * @param userData User information
         * @param cardData Card information
         * @throws SQLException if database operation fails
         */
        final String INSERT_PERSON = "INSERT INTO person(email, taxnumber, name) VALUES (?,?,?) RETURNING id";
        final String INSERT_CARD = "INSERT INTO card(credit, typeof, client) VALUES (?,?,?)";
        final String INSERT_USER = "INSERT INTO client(person, dtregister) VALUES (?,?)";

        try (
                Connection conn = DriverManager.getConnection(UI.getInstance().getConnectionString());
                PreparedStatement pstmtPerson = conn.prepareStatement(INSERT_PERSON, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement pstmtCard = conn.prepareStatement(INSERT_CARD);
                PreparedStatement pstmtUser = conn.prepareStatement(INSERT_USER);) {
            conn.setAutoCommit(false);

            // Insert person
            pstmtPerson.setString(1, userData.getEmail());
            pstmtPerson.setInt(2, userData.getTaxNumber());
            pstmtPerson.setString(3, userData.getName());

            int affectedRows = pstmtPerson.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating person failed, no rows affected.");
            }

            int personId;
            try (ResultSet generatedKeys = pstmtPerson.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personId = generatedKeys.getInt(1);
                } else {
                    throw new RuntimeException("Creating person failed, no ID obtained.");
                }
            }
            
            
            
            // CONTINUE
            pstmtUser.setInt(1, personId);
            pstmtUser.setTimestamp(2, userData.getRegistrationDate());
            pstmtUser.executeUpdate();

            // Insert card
            pstmtCard.setDouble(1, cardData.getCredit());
            pstmtCard.setString(2, cardData.getTypeOf());
            pstmtCard.setInt(3, personId);
            pstmtCard.executeUpdate();



            conn.commit();
            if (pstmtUser != null)
                pstmtUser.close();
            if (pstmtCard != null)
                pstmtCard.close();
            if (pstmtPerson != null)
                pstmtPerson.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error on insert values");
            // e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * To implement from this point forward. Do not need to change the code above.
     * -------------------------------------------------------------------------------
     * IMPORTANT:
     * --- DO NOT MOVE IN THE CODE ABOVE. JUST HAVE TO IMPLEMENT THE METHODS BELOW
     * ---
     * -------------------------------------------------------------------------------
     **/

    static void listOrders(String[] orders) {
        /**
         * Lists orders based on specified criteria
         * 
         * @param orders Criteria for listing orders
         * @throws SQLException if database operation fails
         */
        final String VALUE_CMD = " TO BE DONE";
        // try(
        // Connection conn =
        // DriverManager.getConnection(UI.getInstance().getConnectionString());
        // PreparedStatement pstmt1 = conn.prepareStatement(VALUE_CMD);
        // ){

        // }
    }

    
    public static void listReplacementOrders(int stationId, Timestamp startDate, Timestamp endDate) throws SQLException {
        /**
         * Lists replacement orders for a specific station in a given time period
         * @param stationId Station ID
         * @param startDate Start date for period
         * @param endDate End date for period
         * @throws SQLException if database operation fails
         */
        final String QUERY = "SELECT * FROM replacementorder WHERE station = ? AND dtorder BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(UI.getInstance().getConnectionString());
             PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            pstmt.setInt(1, stationId);
            pstmt.setTimestamp(2, startDate);
            pstmt.setTimestamp(3, endDate);
            ResultSet rs = pstmt.executeQuery();
            App.printResults(rs);
        }
    }



    public static void travel(String[] values) throws SQLException {
        /**
         * Processes a travel operation (start or stop)
         * @param values Array containing [operation, client_id, scooter_id, station_id, timestamp]
         * @throws SQLException if database operation fails
         */
        String operation = values[0];
        int clientId = Integer.parseInt(values[1]);
        int scooterId = Integer.parseInt(values[2]);
        int stationId = Integer.parseInt(values[3]);
        Timestamp timestamp = Timestamp.valueOf(values[4]);

        if ("START".equalsIgnoreCase(operation)) {
            startTravel(clientId, scooterId, stationId, timestamp);
        } else if ("STOP".equalsIgnoreCase(operation)) {
            stopTravel(clientId, scooterId, stationId, timestamp);
        } else {
            throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }
    
    public static int getClientId(String name) throws SQLException {
        /** Auxiliar method -- if you want
         * Gets client ID by name from database
         * @param name The name of the client
         * @return client ID or -1 if not found
         * @throws SQLException if database operation fails
         */
        final String QUERY = "SELECT person FROM client WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(UI.getInstance().getConnectionString());
             PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("person");
            } else {
                return -1;
            }
        }
    }

    public static void startTravel(int clientId, int scooterId, int stationId, Timestamp timestamp) throws SQLException {
        /**
         * Starts a new travel
         * @param clientId Client ID
         * @param scooterId Scooter ID
         * @param stationId Station ID
         * @param timestamp Start timestamp
         * @throws SQLException if database operation fails
         */
        final String START_TRAVEL = "INSERT INTO travel (dtinitial, client, scooter, stinitial) VALUES (?, ?, ?, ?)";
        final String UPDATE_SCOOTER = "UPDATE scooter SET state = 'in use' WHERE id = ?";
        final String UPDATE_DOCK = "UPDATE dock SET state = 'free', scooter = NULL WHERE scooter = ?";

        try (Connection conn = DriverManager.getConnection(UI.getInstance().getConnectionString());
             PreparedStatement pstmtTravel = conn.prepareStatement(START_TRAVEL);
             PreparedStatement pstmtScooter = conn.prepareStatement(UPDATE_SCOOTER);
             PreparedStatement pstmtDock = conn.prepareStatement(UPDATE_DOCK)) {
            conn.setAutoCommit(false);

            // Start travel
            pstmtTravel.setTimestamp(1, timestamp);
            pstmtTravel.setInt(2, clientId);
            pstmtTravel.setInt(3, scooterId);
            pstmtTravel.setInt(4, stationId);
            pstmtTravel.executeUpdate();

            // Update scooter state
            pstmtScooter.setInt(1, scooterId);
            pstmtScooter.executeUpdate();

            // Update dock state
            pstmtDock.setInt(1, scooterId);
            pstmtDock.executeUpdate();

            conn.commit();
        }
    }

    
    public static void stopTravel(int clientId, int scooterId, int stationId, Timestamp timestamp) throws SQLException {
        /**
         * Stops an ongoing travel
         * @param clientId Client ID
         * @param scooterId Scooter ID
         * @param stationId Destination station ID
         * @param timestamp Stop timestamp
         * @throws SQLException if database operation fails
         */
        final String STOP_TRAVEL = "UPDATE travel SET dtfinal = ?, stfinal = ? WHERE client = ? AND scooter = ? AND dtfinal IS NULL";
        final String UPDATE_SCOOTER = "UPDATE scooter SET state = 'available' WHERE id = ?";
        final String UPDATE_DOCK = "UPDATE dock SET state = 'occupy', scooter = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(UI.getInstance().getConnectionString());
             PreparedStatement pstmtTravel = conn.prepareStatement(STOP_TRAVEL);
             PreparedStatement pstmtScooter = conn.prepareStatement(UPDATE_SCOOTER);
             PreparedStatement pstmtDock = conn.prepareStatement(UPDATE_DOCK)) {
            conn.setAutoCommit(false);

            // Stop travel
            pstmtTravel.setTimestamp(1, timestamp);
            pstmtTravel.setInt(2, stationId);
            pstmtTravel.setInt(3, clientId);
            pstmtTravel.setInt(4, scooterId);
            pstmtTravel.executeUpdate();

            // Update scooter state
            pstmtScooter.setInt(1, scooterId);
            pstmtScooter.executeUpdate();

            // Update dock state
            pstmtDock.setInt(1, scooterId);
            pstmtDock.setInt(2, stationId);
            pstmtDock.executeUpdate();

            conn.commit();
        }
    }

    public static void updateDocks(int dockId, String state, Integer scooterId) throws SQLException {
        /**
         * Updates the state of docks
         * @param dockId Dock ID
         * @param state New state of the dock
         * @param scooterId Scooter ID (can be null)
         * @throws SQLException if database operation fails
         */
        final String UPDATE_DOCK = "UPDATE dock SET state = ?, scooter = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(UI.getInstance().getConnectionString());
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_DOCK)) {
            pstmt.setString(1, state);
            if (scooterId != null) {
                pstmt.setInt(2, scooterId);
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            pstmt.setInt(3, dockId);
            pstmt.executeUpdate();
        }
    }


    public static void userSatisfaction() throws SQLException {
        /**
         * Analyzes user satisfaction for each scooter model
         * @throws SQLException if database operation fails
         */
        final String QUERY = "SELECT model, AVG(evaluation) AS avg_rating, COUNT(*) AS total_travels, " +
                             "SUM(CASE WHEN evaluation >= 4 THEN 1 ELSE 0 END) * 100.0 / COUNT(*) AS satisfaction_percentage " +
                             "FROM travel JOIN scooter ON travel.scooter = scooter.id " +
                             "GROUP BY model ORDER BY avg_rating DESC";
    
        try (Connection conn = DriverManager.getConnection(UI.getInstance().getConnectionString());
             PreparedStatement pstmt = conn.prepareStatement(QUERY);
             ResultSet rs = pstmt.executeQuery()) {
            App.printResults(rs);
        }
    }

    public static void occupationStation() throws SQLException {
        /**
         * Lists the top three stations with the highest occupation rates
         * @throws SQLException if database operation fails
         */
        final String QUERY = "SELECT station, COUNT(*) AS occupation_count " +
                             "FROM dock WHERE state = 'occupy' " +
                             "GROUP BY station ORDER BY occupation_count DESC LIMIT 3";
    
        try (Connection conn = DriverManager.getConnection(UI.getInstance().getConnectionString());
             PreparedStatement pstmt = conn.prepareStatement(QUERY);
             ResultSet rs = pstmt.executeQuery()) {
            App.printResults(rs);
        }
    }
}