import java.sql.*;
import java.util.Scanner;

public class EmployeeApp {
    
    static final String URL = "jdbc:mysql://localhost:3308/employee_db";
    static final String USER = "root";     
    static final String PASS = "My@12345"; 

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Scanner sc = new Scanner(System.in)) {

            System.out.println(" Connected to database!");

            while (true) {
                System.out.println("\n--- Employee Management ---");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> addEmployee(conn, sc);
                    case 2 -> viewEmployees(conn);
                    case 3 -> updateEmployee(conn, sc);
                    case 4 -> deleteEmployee(conn, sc);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter department: ");
        String dept = sc.nextLine();
        System.out.print("Enter salary: ");
        double salary = sc.nextDouble();

        String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, salary);
            ps.executeUpdate();
            System.out.println("Employee added!");
        }
    }

    private static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM employees";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Employee List ---");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        }
    }

    // private static void updateEmployee(Connection conn, Scanner sc, String department) throws SQLException {
    //     System.out.print("Enter employee ID to update: ");
    //     int id = sc.nextInt();
    //     sc.nextLine();

    //     System.out.print("Enter new name: ");
    //     String name = sc.nextLine();
    //     System.out.print("Enter new department: ");
    //     String dept = sc.nextLine();
    //     System.out.print("Enter new salary: ");
    //     double salary = sc.nextDouble();

        
    //     String sql = "UPDATE employees SET name=?, department=?, salary=? WHERE id=?";
    //     try (PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setInt(1, id);
    //         ps.setString(2, name);
    //         ps.setString(3, department);
    //         ps.setDouble(4, salary);
          
    //         int rows = ps.executeUpdate();
    //         if (rows > 0){
    //             System.out.println(" Employee updated!");
    //         }
    //         else{
    //             System.out.println(" Employee ID not found.");
    //         }
    //     }
    // }


    public static void updateEmployee(Connection conn, Scanner sc) throws SQLException {
    System.out.print("Enter employee ID to update: ");
    int id = sc.nextInt();
    sc.nextLine(); // consume newline
    
    System.out.print("Enter new name: ");
    String name = sc.nextLine();
    System.out.print("Enter new department: ");
    String dept = sc.nextLine();
    System.out.print("Enter new salary: ");
    double salary = sc.nextDouble();
    
    String sql = "UPDATE employees SET name = ?, department = ?, salary = ? WHERE id = ?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, name);
    pstmt.setString(2, dept);
    pstmt.setDouble(3, salary);
    pstmt.setInt(4, id);
    
    int rows = pstmt.executeUpdate();
    if (rows > 0) {
        System.out.println("Employee updated!");
    } else {
        System.out.println("Employee ID not found.");
    }
}

    private static void deleteEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM employees WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println(" Employee deleted!");
            else
                System.out.println(" Employee ID not found.");
        }
    }
}
