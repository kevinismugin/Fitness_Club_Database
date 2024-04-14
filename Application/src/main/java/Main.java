import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.*;

public class Main {
    private static Connection connection;
    static String userType = "member";
    static int routineId = 1;
    static int userId = -1;

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/Tester";
        String user = "postgres";
        String pass = "admin";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, pass);

            System.out.println("1. User Login");
            System.out.println("2. User Registration");
            System.out.println("3. Exit\n");

            Scanner input = new Scanner(System.in);
            System.out.print("Please make a selection: ");

            switch (input.nextInt()) {
                case 1:
                    loginUser(input);
                    break;
                case 2:
                    registerUser(input);
                    break;
                case 3:
                    System.exit(0);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void loginUser(Scanner input) throws SQLException {
        String query;
        int choice = 0;
        PreparedStatement statement;

        System.out.println("\n1. Member Login");
        System.out.println("2. Staff Login\n");

        while (choice != 1 && choice != 2) {
            System.out.print("Please make a selection: ");
            choice = input.nextInt();
        }

        System.out.print("\nEnter your username: ");
        String username = input.next();
        System.out.print("Enter your password: ");
        String password = input.next();

        if (choice == 1) {
            query = "SELECT member_id FROM members WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
        }
        else {
            query = "SELECT staff_id, type FROM staff WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
        }

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            if (choice == 1) {
                userId = resultSet.getInt(1);
            }
            else {
                userId = resultSet.getInt("staff_id");
                userType = resultSet.getString("type");
            }
            displayDashboard(userType, input);
        }
        else {
            System.out.println("\nFailed to login user.");
            loginUser(input);
        }
    }

    private static void registerUser(Scanner input) throws SQLException {
        String query;
        int choice = 0;
        PreparedStatement statement;

        System.out.println("\n1. Member Registration");
        System.out.println("2. Staff Registration\n");

        while (choice != 1 && choice != 2) {
            System.out.print("Please make a selection: ");
            choice = input.nextInt();
        }

        System.out.print("\nEnter your username: ");
        String username = input.next();
        System.out.print("Enter your password: ");
        String password = input.next();

        if (choice == 1) {
            query = "INSERT INTO members (username, password) VALUES (?, ?) RETURNING member_id";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
        }
        else {
            while (!userType.equals("trainer") && !userType.equals("admin")) {
                System.out.print("Enter your position (trainer/admin): ");
                userType = input.next().toLowerCase();
            }

            query = "INSERT INTO staff (username, password, type) VALUES (?, ?, ?) RETURNING staff_id";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, userType);
        }

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            userId = resultSet.getInt(1);
            query = "INSERT INTO personal_information (member_id) VALUES (?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.executeUpdate();
            query = "INSERT INTO health_metrics (member_id) VALUES (?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.executeUpdate();
            query = "INSERT INTO billings (member_id) VALUES (?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.executeUpdate();
            displayDashboard(userType, input);
        }
        else {
            System.out.println("\nFailed to register user.");
        }
    }

    private static void displayDashboard(String userType, Scanner input) throws SQLException {
        switch (userType.toLowerCase()) {
            case "member":
                memberDashboard(input);
                break;
            case "trainer":
                trainerDashboard(input);
                break;
            case "admin":
                administratorDashboard(input);
                break;
        }
    }

    private static void memberDashboard(Scanner input) throws SQLException {
        while (true) {
            System.out.println("\nMember Options:");
            System.out.println("1. Manage Profile");
            System.out.println("2. View Dashboard");
            System.out.println("3. Manage Schedule");
            System.out.println("4. Exit\n");

            System.out.print("Please make a selection: ");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    manageProfile(input);
                    break;
                case 2:
                    viewDashboard(input);
                    break;
                case 3:
                    manageScheduleMember(input);
                    break;
                case 4:
                    System.exit(0);
            }
        }
    }

    private static void manageProfile(Scanner input) throws SQLException {
        int choice = 0;

        while (choice != 6) {
            System.out.println("\nProfile Management:");
            System.out.println("1. Personal Information");
            System.out.println("2. Fitness Goals");
            System.out.println("3. Health Metrics");
            System.out.println("4. Exercise Routines");
            System.out.println("5. Fitness Achievements");
            System.out.println("6. Return to Member Options\n");

            System.out.print("Please make a selection: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    updatePersonalInfo(input);
                    break;
                case 2:
                    manageFitnessGoals(input);
                    break;
                case 3:
                    updateHealthMetrics(input);
                    break;
                case 4:
                    addExerciseRoutines(input);
                    break;
                case 5:
                    addFitnessAchievements(input);
                    break;
                case 6:
                    break;
            }
        }
    }

    private static void viewDashboard(Scanner input) throws SQLException {
        int choice = 0;

        while (choice != 4) {
            System.out.println("\nDashboard Display:");
            System.out.println("1. Exercise Routines");
            System.out.println("2. Fitness Achievements");
            System.out.println("3. Health Metrics");
            System.out.println("4. Return to Member Options\n");

            System.out.print("Please make a selection: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    displayExerciseRoutines(input);
                    break;
                case 2:
                    displayFitnessAchievements(input);
                    break;
                case 3:
                    displayHealthMetrics(input);
                    break;
                case 4:
                    break;
            }
        }
    }

    private static void updatePersonalInfo(Scanner input) throws SQLException {
        String query = "SELECT * FROM personal_information WHERE member_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            System.out.println("\nCurrent Profile:");
            System.out.println("1. First Name: " + resultSet.getString("first_name"));
            System.out.println("2. Last Name: " + resultSet.getString("last_name"));
            System.out.println("3. Year of Birth: " + resultSet.getInt("year_of_birth"));
            System.out.println("4. Email: " + resultSet.getString("email"));

            System.out.print("\nEnter the number of the field you want to update or 0 to exit: ");
            int choice = input.nextInt();
            input.nextLine();

            if (choice > 0 && choice <= 7) {
                System.out.print("Enter the new value for the selected field: ");
                String newValue = input.nextLine();

                String[] fields = {"first_name", "last_name", "year_of_birth", "email"};
                String sqlUpdate = "UPDATE personal_information SET " + fields[choice - 1] + " = ? WHERE member_id = ?";

                PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate);

                if (choice == 3) {
                    updateStmt.setInt(1, Integer.parseInt(newValue));
                } else {
                    updateStmt.setString(1, newValue);
                }

                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
                System.out.println("\nPersonal information updated successfully.");
            }
        } else {
            System.out.println("\nNo profile found.");
        }
    }

    private static void manageFitnessGoals(Scanner input) throws SQLException {
        String query = "SELECT * FROM fitness_goals WHERE member_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        HashMap<Integer, String> goalMap = new HashMap<>();

        while (true) {
            int currentOption = 1;
            System.out.println("\nCurrent Fitness Goals:");
            while (resultSet.next()) {
                System.out.println(currentOption + ". " + resultSet.getString("goal"));
                goalMap.put(currentOption, resultSet.getString("goal"));
                currentOption++;
            }

            System.out.println("\nOptions:");
            System.out.println("1. Add a new goal");
            System.out.println("2. Update an existing goal");
            System.out.println("3. Remove a goal");
            System.out.println("4. Return to Member Options\n");

            System.out.print("Please make a selection: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    addFitnessGoal(input);
                    break;
                case 2:
                    updateFitnessGoal(input, goalMap);
                    break;
                case 3:
                    removeFitnessGoal(input, goalMap);
                    break;
                case 4:
                    return;
            }

            resultSet = statement.executeQuery();
            goalMap.clear();
        }
    }

    private static void addFitnessGoal(Scanner input) throws SQLException {
        System.out.print("\nEnter the new fitness goal: ");
        String newGoal = input.nextLine();
        String insertQuery = "INSERT INTO fitness_goals (member_id, goal) VALUES (?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
        insertStmt.setInt(1, userId);
        insertStmt.setString(2, newGoal);
        insertStmt.executeUpdate();
        System.out.println("\nFitness goal added successfully.");
    }

    private static void updateFitnessGoal(Scanner input, HashMap<Integer, String> goalMap) throws SQLException {
        System.out.print("\nEnter the number of the goal you want to update: ");
        int choice = input.nextInt();
        input.nextLine();
        System.out.print("Enter the new goal: ");
        String newGoal = input.nextLine();

        String currentGoal = goalMap.get(choice);

        String updateQuery = "UPDATE fitness_goals SET goal = ? WHERE member_id = ? AND goal = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        updateStmt.setString(1, newGoal);
        updateStmt.setInt(2, userId);
        updateStmt.setString(3, currentGoal);
        updateStmt.executeUpdate();

        System.out.println("\nFitness goal updated successfully.");
    }

    private static void removeFitnessGoal(Scanner input, HashMap<Integer, String> goalMap) throws SQLException {
        System.out.print("\nEnter the number of the goal you want to remove: ");
        int choice = input.nextInt();

        String currentGoal = goalMap.get(choice);

        String deleteQuery = "DELETE FROM fitness_goals WHERE member_id = ? AND goal = ?";
        PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
        deleteStmt.setInt(1, userId);
        deleteStmt.setString(2, currentGoal);
        int affectedRows = deleteStmt.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("\nFitness goal removed successfully.");
        } else {
            System.out.println("\nFailed to remove goal.");
        }
    }

    private static void addExerciseRoutines(Scanner input) throws SQLException {
        while (true) {
            System.out.print("\nEnter an exercise that is part of this routine: ");
            String exercise = input.nextLine();
            System.out.print("Enter the total time of the exercise in minutes: ");
            int time = input.nextInt();

            String insertQuery = "INSERT INTO exercise_routines (member_id, routine_id, exercise, total_time) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, routineId);
            insertStmt.setString(3, exercise);
            insertStmt.setInt(4, time);
            insertStmt.executeUpdate();

            System.out.print("\nAre there more exercises part of this routine (true/false): ");
            boolean choice = input.nextBoolean();
            input.nextLine();

            if (!choice) {
                break;
            }
        }
    }

    private static void addFitnessAchievements(Scanner input) throws SQLException {
        System.out.print("\nEnter the new fitness achievement: ");
        String newAchievement = input.nextLine();
        String insertQuery = "INSERT INTO fitness_achievements (member_id, achievement) VALUES (?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
        insertStmt.setInt(1, userId);
        insertStmt.setString(2, newAchievement);
        insertStmt.executeUpdate();
        System.out.println("\nFitness achievement added successfully.");
    }

    private static void displayExerciseRoutines(Scanner input) throws SQLException {
        String query = "SELECT * FROM exercise_routines WHERE member_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        int currentRoutineId = 1;
        System.out.println("\nRoutine 1:");

        while (resultSet.next()) {
            int routineId = resultSet.getInt("routine_id");
            String exercise = resultSet.getString("exercise");
            int totalTime = resultSet.getInt("total_time");

            if (routineId != currentRoutineId) {
                System.out.println("\nRoutine " + routineId + " :");
                currentRoutineId = routineId;
            }

            System.out.println("Exercise: " + exercise + ", Total Time: " + totalTime);
        }

        System.out.print("\nPress enter to return to dashboard: ");
        input.nextLine();
    }

    private static void displayFitnessAchievements(Scanner input) throws SQLException {
        String query = "SELECT * FROM fitness_achievements WHERE member_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        System.out.println();

        while (resultSet.next()) {
            System.out.println("Achievement: " + resultSet.getString("achievement"));
        }

        System.out.print("\nPress enter to return to dashboard: ");
        input.nextLine();
    }

    private static void displayHealthMetrics(Scanner input) throws SQLException {
        String query = "SELECT * FROM health_metrics WHERE member_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int bodyWeight = resultSet.getInt("body_weight");
            int bodyFatPercentage = resultSet.getInt("body_fat_percentage");
            int restingHeartRate = resultSet.getInt("heart_rate");

            System.out.println();

            System.out.println("Body Weight: " + bodyWeight + " kg");
            System.out.println("Body Fat Percentage: " + bodyFatPercentage + "%");
            System.out.println("Resting Heart Rate: " + restingHeartRate + " bpm");
        }

        System.out.print("\nPress enter to return to dashboard: ");
        input.nextLine();
    }

    private static void updateHealthMetrics(Scanner input) throws SQLException {
        int choice = 0;

        System.out.println("\nWhich metric would you like to update?");
        System.out.println("1: Body Weight");
        System.out.println("2: Body Fat Percentage");
        System.out.println("3: Resting Heart Rate\n");

        while (choice < 1 || choice > 3) {
            System.out.print("Please make a selection: ");
            choice = input.nextInt();
        }

        String attribute = "";
        String query = "UPDATE health_metrics SET %s = ? WHERE member_id = ?";
        PreparedStatement statement;

        attribute = switch (choice) {
            case 1 -> "body_weight";
            case 2 -> "body_fat_percentage";
            case 3 -> "heart_rate";
            default -> attribute;
        };

        query = String.format(query, attribute);
        statement = connection.prepareStatement(query);

        System.out.print("\nEnter the new value for " + attribute + ": ");
        int newValue = input.nextInt();
        statement.setInt(1, newValue);
        statement.setInt(2, userId);

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("\nUpdated successfully.");
        } else {
            System.out.println("\nUpdate failed.");
        }
    }

    private static void manageScheduleMember(Scanner input) throws SQLException {
        int choice = 0;
        int selection;
        String query;
        ResultSet resultSet;
        PreparedStatement statement;

        while (choice != 3) {
            System.out.println("\nSchedule Management:");
            System.out.println("1. Personal Sessions");
            System.out.println("2. Group Classes");
            System.out.println("3. Return to Member Options\n");

            System.out.print("Please make a selection: ");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    query = "SELECT * FROM trainer_schedule WHERE member_id = ?";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, userId);
                    resultSet = statement.executeQuery();

                    System.out.println("\nYour Current Personal Sessions Schedule:");
                    int count = 0;

                    while (resultSet.next()) {
                        count++;
                        System.out.println(resultSet.getInt("schedule_id") + ". " + "Trainer ID: " + resultSet.getInt("staff_id") +
                                ", Time Slot: " + resultSet.getInt("time_slot"));
                    }

                    if (count == 0) {
                        System.out.println("No personal sessions scheduled.");
                    }

                    System.out.println("\n1. Schedule Personal Sessions");
                    System.out.println("2. Reschedule Personal Sessions");
                    System.out.println("3. Cancel Personal Sessions");
                    System.out.println("4. Exit\n");

                    System.out.print("Please make a selection: ");
                    choice = input.nextInt();

                    switch (choice) {
                        case 1:
                            query = "SELECT * FROM trainer_schedule WHERE is_booked = FALSE";
                            statement = connection.prepareStatement(query);
                            resultSet = statement.executeQuery();

                            System.out.println();

                            while (resultSet.next()) {
                                System.out.println(resultSet.getInt("schedule_id") + ". " + "Trainer ID: " +
                                        resultSet.getInt("staff_id") + ", Time Slot: " + resultSet.getInt("time_slot"));
                            }
                            System.out.print("\nPlease enter the number of the session to schedule: ");
                            selection = input.nextInt();

                            query = "UPDATE trainer_schedule SET member_id = ?, is_booked = TRUE WHERE schedule_id = ?";
                            statement = connection.prepareStatement(query);
                            statement.setInt(1, userId);
                            statement.setInt(2, selection);
                            statement.executeUpdate();
                            System.out.println("\nPersonal session scheduled successfully.");
                            return;
                        case 2:
                            System.out.print("\nPlease select the session you want to reschedule: ");
                            selection = input.nextInt();

                            query = "UPDATE trainer_schedule SET member_id = NULL, is_booked = FALSE WHERE schedule_id = ?";
                            statement = connection.prepareStatement(query);
                            statement.setInt(1, selection);
                            statement.executeUpdate();

                            query = "SELECT * FROM trainer_schedule WHERE is_booked = FALSE";
                            statement = connection.prepareStatement(query);
                            resultSet = statement.executeQuery();

                            System.out.println();

                            while (resultSet.next()) {
                                System.out.println(resultSet.getInt("schedule_id") + ". " + "Trainer ID: " +
                                        resultSet.getInt("staff_id") + ", Time Slot: " + resultSet.getInt("time_slot"));
                            }
                            System.out.print("\nPlease enter the number of the session to schedule: ");
                            selection = input.nextInt();

                            query = "UPDATE trainer_schedule SET member_id = ?, is_booked = TRUE WHERE schedule_id = ?";
                            statement = connection.prepareStatement(query);
                            statement.setInt(1, userId);
                            statement.setInt(2, selection);
                            statement.executeUpdate();
                            System.out.println("\nPersonal session rescheduled successfully.");
                            return;
                        case 3:
                            System.out.print("\nPlease select the session you want to cancel: ");
                            selection = input.nextInt();

                            query = "UPDATE trainer_schedule SET member_id = NULL, is_booked = FALSE WHERE schedule_id = ?";
                            statement = connection.prepareStatement(query);
                            statement.setInt(1, selection);
                            statement.executeUpdate();
                            System.out.println("\nPersonal session cancelled successfully.");
                            return;
                        case 4:
                            return;
                    }
                case 2:
                    query = "SELECT gc.* FROM group_classes gc LEFT JOIN class_members cm ON gc.class_id = cm.class_id AND cm.member_id = ? WHERE cm.member_id IS NULL";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, userId);
                    resultSet = statement.executeQuery();

                    System.out.println();

                    while (resultSet.next()) {
                        System.out.println(resultSet.getInt("class_id") + ". " + "Trainer ID: " +
                                resultSet.getInt("staff_id") + ", Start Time: " + resultSet.getInt("start_time"));
                    }

                    System.out.print("\nPlease enter the number of the class you want to book: ");
                    selection = input.nextInt();

                    String insertQuery = "INSERT INTO class_members (class_id, member_id) VALUES (?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                    insertStmt.setInt(1, selection);
                    insertStmt.setInt(2, userId);
                    insertStmt.executeUpdate();
                    System.out.println("\nGroup class joined successfully.");
                    break;
                case 3:
                    break;
            }
        }
    }

    private static void trainerDashboard(Scanner input) throws SQLException {
        while (true) {
            System.out.println("\nTrainer Options:");
            System.out.println("1. Manage Schedule");
            System.out.println("2. View Member Profile");
            System.out.println("3. Exit\n");

            System.out.print("Please make a selection: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    manageScheduleTrainer(input);
                    break;
                case 2:
                    viewMemberProfile(input);
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }

    private static void manageScheduleTrainer(Scanner input) throws SQLException {
        int selection;
        int choice = 0;

        while (choice != 3) {
            System.out.println("\n1. Add New Availability");
            System.out.println("2. Update Trainer Schedule");
            System.out.println("3. Return to Trainer Options\n");

            System.out.print("Please make a selection: ");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("\nEnter the hour you are available using military time (8 to 20): ");
                    selection = input.nextInt();

                    String insertQuery = "INSERT INTO trainer_schedule (staff_id, time_slot) VALUES (?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, selection);
                    insertStmt.executeUpdate();
                    System.out.println("\nSchedule updated successfully.");
                    break;
                case 2:
                    String query = "SELECT schedule_id, time_slot FROM trainer_schedule WHERE staff_id = ? AND is_booked = FALSE";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, userId);
                    ResultSet resultSet = statement.executeQuery();

                    System.out.println();

                    while (resultSet.next()) {
                        System.out.println(resultSet.getInt("schedule_id") + ". " + "Time Slot: " + resultSet.getInt("time_slot"));
                    }

                    System.out.print("\nPlease enter the number of the availability to update: ");
                    selection = input.nextInt();
                    System.out.print("\nEnter the new time slot hour using military time (8 to 20): ");
                    int hour = input.nextInt();

                    String updateQuery = "UPDATE trainer_schedule SET time_slot = ? WHERE schedule_id = ?";
                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                    updateStmt.setInt(1, hour);
                    updateStmt.setInt(2, selection);
                    updateStmt.executeUpdate();
                    System.out.println("\nSchedule updated successfully.");
                    break;
                case 3:
                    break;
            }
        }
    }

    private static void viewMemberProfile(Scanner input) throws SQLException {
        System.out.print("\nEnter the member's first name: ");
        String first = input.nextLine();
        System.out.print("Enter the member's last name: ");
        String last = input.nextLine();

        String query = "SELECT * FROM personal_information WHERE first_name = ? AND last_name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, first);
        statement.setString(2, last);
        ResultSet resultSet = statement.executeQuery();

        System.out.println();

        if (resultSet.next()) {
            System.out.println("Member ID: " + resultSet.getString("member_id"));
            System.out.println("First Name: " + resultSet.getString("first_name"));
            System.out.println("Last Name: " + resultSet.getString("last_name"));
            System.out.println("Year of Birth: " + resultSet.getInt("year_of_birth"));
            System.out.println("Email: " + resultSet.getString("email"));
        }
        else {
            System.out.println("\nMember could not be found.");
        }

        System.out.print("\nPress enter to return to dashboard: ");
        input.nextLine();
    }

    private static void administratorDashboard(Scanner input) throws SQLException {
        while (true) {
            System.out.println("\nAdministrator Dashboard:");
            System.out.println("1. Manage Room Booking");
            System.out.println("2. Monitor Equipment Maintenance");
            System.out.println("3. Update Class Schedule");
            System.out.println("4. Process Billing and Payments");
            System.out.println("5. Exit\n");

            System.out.print("Please make a selection: ");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    manageRoomBookings(input);
                    break;
                case 2:
                    monitorEquipmentMaintenance(input);
                    break;
                case 3:
                    updateClassSchedule(input);
                    break;
                case 4:
                    processBillingPayments(input);
                    break;
                case 5:
                    System.exit(0);
            }
        }
    }

    private static void manageRoomBookings(Scanner input) throws SQLException {
        int choice = 0;

        while (choice != 3) {
            System.out.println("\n1. Add Room Booking");
            System.out.println("2. Remove Room Booking");
            System.out.println("3. Return to Admin Options\n");

            System.out.print("Please make a selection: ");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("\nEnter the ID of the trainer/admin using the room: ");
                    int id = input.nextInt();
                    System.out.print("Enter the room number: ");
                    int room = input.nextInt();
                    System.out.print("Enter the hour the booking starts using military time (8 to 20): ");
                    int start = input.nextInt();

                    String insertQuery = "INSERT INTO room_bookings (staff_id, room_number, start_time) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                    insertStmt.setInt(1, id);
                    insertStmt.setInt(2, room);
                    insertStmt.setInt(3, start);
                    insertStmt.executeUpdate();
                    System.out.println("\nRoom booking added successfully.");
                    break;
                case 2:
                    String query = "SELECT * FROM room_bookings";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    System.out.println();

                    while (resultSet.next()) {
                        System.out.println(resultSet.getInt("booking_id") + ". " + "Trainer ID: " + resultSet.getInt("staff_id") +
                                ", Room Number: " + resultSet.getInt("room_number") + ", Start Time: " +
                                resultSet.getInt("start_time"));
                    }

                    System.out.print("\nPlease enter the number of the room booking to remove: ");
                    int selection = input.nextInt();

                    String deleteQuery = "DELETE FROM room_bookings WHERE booking_id = ?";
                    PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
                    deleteStmt.setInt(1, selection);
                    int affectedRows = deleteStmt.executeUpdate();

                    if (affectedRows > 0) {
                        System.out.println("\nRoom booking removed successfully.");
                    } else {
                        System.out.println("\nFailed to remove room booking.");
                    }
                    break;
                case 3:
                    break;
            }
        }
    }

    private static void monitorEquipmentMaintenance(Scanner input) throws SQLException {
        int choice = 0;

        while (choice != 3) {
            System.out.println("\n1. Add Equipment");
            System.out.println("2. Update Equipment Maintenance");
            System.out.println("3. Return to Admin Options\n");

            System.out.print("Please make a selection: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("\nEnter the type of equipment: ");
                    String equipment = input.nextLine();

                    int currentMonth = LocalDate.now().getMonthValue();

                    String insertQuery = "INSERT INTO equipments (equipment_type, last_maintenance_month) VALUES (?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                    insertStmt.setString(1, equipment);
                    insertStmt.setInt(2, currentMonth);
                    insertStmt.executeUpdate();
                    System.out.println("\nEquipment added successfully.");
                    break;
                case 2:
                    String query = "SELECT * FROM equipments";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    System.out.println();

                    while (resultSet.next()) {
                        System.out.println(resultSet.getInt("equipment_id") + ". " + "Equipment Type: " + resultSet.getString("equipment_type") +
                                ", Last Maintenance Month: " + resultSet.getInt("last_maintenance_month"));
                    }

                    System.out.print("\nEnter the number of the equipment you want to update: ");
                    int selection = input.nextInt();

                    System.out.print("Enter the most recent maintenance month for this equipment (1 to 12): ");
                    int month = input.nextInt();

                    String updateQuery = "UPDATE equipments SET last_maintenance_month = ? WHERE equipment_id = ?";
                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                    updateStmt.setInt(1, month);
                    updateStmt.setInt(2, selection);
                    updateStmt.executeUpdate();
                    System.out.println("\nEquipment maintenance updated successfully.");
                    break;
                case 3:
                    break;
            }
        }
    }

    private static void updateClassSchedule(Scanner input) throws SQLException {
        int choice = 0;

        while (choice != 3) {
            System.out.println("\n1. Add Group Class");
            System.out.println("2. Remove Group Class");
            System.out.println("3. Return to Admin Options\n");

            System.out.print("Please make a selection: ");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("\nEnter the ID of the trainer running the class: ");
                    int id = input.nextInt();
                    System.out.print("Enter the hour the class starts using military time (8 to 20): ");
                    int start = input.nextInt();

                    String insertQuery = "INSERT INTO group_classes (staff_id, start_time) VALUES (?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                    insertStmt.setInt(1, id);
                    insertStmt.setInt(2, start);
                    insertStmt.executeUpdate();
                    System.out.println("\nGroup class added successfully.");
                    break;
                case 2:
                    String query = "SELECT * FROM group_classes";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    System.out.println();

                    while (resultSet.next()) {
                        System.out.println(resultSet.getInt("class_id") + ". " + "Trainer ID: " +
                                resultSet.getInt("staff_id") + ", Start Time: " + resultSet.getInt("start_time"));
                    }

                    System.out.print("\nPlease enter the number of the group class to remove: ");
                    int selection = input.nextInt();

                    String deleteQuery = "DELETE FROM class_members WHERE class_id = ?";
                    PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
                    deleteStmt.setInt(1, selection);
                    deleteStmt.executeUpdate();

                    deleteQuery = "DELETE FROM group_classes WHERE class_id = ?";
                    deleteStmt = connection.prepareStatement(deleteQuery);
                    deleteStmt.setInt(1, selection);
                    deleteStmt.executeUpdate();
                    System.out.println("\nGroup class removed successfully.");
                    break;
                case 3:
                    break;
            }
        }
    }

    private static void processBillingPayments(Scanner input) throws SQLException {
        String query = "SELECT * FROM billings";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        System.out.println();

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("member_id") + ". " + "Amount to Pay: " +
                    resultSet.getString("amount_to_pay") + ", Is Paid: " + resultSet.getBoolean("is_paid"));
        }

        System.out.print("\nEnter the number of the bill you want to update: ");
        int selection = input.nextInt();

        System.out.print("Enter the new amount to pay as an integer: ");
        int amount = input.nextInt();

        System.out.print("Enter whether or not the bill has been paid (true/false): ");
        boolean paid = input.nextBoolean();

        String updateQuery = "UPDATE billings SET amount_to_pay = ?, is_paid = ? WHERE member_id = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        updateStmt.setInt(1, amount);
        updateStmt.setBoolean(2, paid);
        updateStmt.setInt(3, selection);
        updateStmt.executeUpdate();
        System.out.println("\nBilling updated successfully.");
    }
}