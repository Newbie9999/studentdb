package onpu.nai166.cherniyenko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class DB {
    public static void main(String[] args) {
        HashMap<String, String> dict = new HashMap<>();
        dict.put("s", "student");
        dict.put("t", "teacher");
        dict.put("g", "group");
        dict.put("f", "faculty");
        DB m = new DB();
        String login = "admin";
        String password = "1234";
        boolean execute = true;
        String answer;
        String request;
        Scanner scanner = new Scanner(System.in);
        while (execute) {
            System.out.println("\nDo you want to search or update info? [s/u] \nEnter 'q' to exit");
            answer = scanner.next();
            if (answer.compareTo("s") == 0) {
                System.out.println("Do you need teacher, student, group or faculty? [t/s/g/f]");
                answer = scanner.next();
                answer = dict.get(answer);
                System.out.println("Enter name of your " + answer + " or 'all'");
                request = scanner.next();
                m.searchDB(login, password, request, answer);
            } else if (answer.compareTo("u") == 0) {
                System.out.println("Enter your login: [Tip: 'admin']");
                login = scanner.next();
                System.out.println("Enter your password: [Tip: '1234']");
                password = scanner.next();
                System.out.println("This is beta-version, you can enter only new faculties now.");
                System.out.println("Enter the name of new faculty: ");
                request = scanner.next();
                m.newFaculty(login, password, request);
            } else if (answer.compareTo("q") == 0)
                execute = false;
            //m.testDatabase(login, password);
        }
    }

    private void testDatabase(String login, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://127.0.0.1/students";
            Connection con = DriverManager.getConnection(url, login, password);
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM faculty");
                while (rs.next()) {
                    String str = rs.getString("name") + ":" + rs.getString(3);
                    System.out.println("Faculty:" + str);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void newFaculty(String login, String password, String request) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://127.0.0.1/students";
            Connection con = DriverManager.getConnection(url, login, password);
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM faculty");
                stmt.executeUpdate("insert into faculty (name) values ('"+request+"');");
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void searchDB(String login, String password, String request, String answer) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://127.0.0.1/students";
            Connection con = DriverManager.getConnection(url, login, password);
            try {
                Statement stmt = con.createStatement();
                ResultSet rs;
                if (request.compareTo("all") == 0)
                    rs = stmt.executeQuery("SELECT * FROM " + answer + ";");
                else
                    rs = stmt.executeQuery("SELECT * FROM " + answer + " where name = '" + request + "';");

                switch (answer) {
                    case "student":
                        while (rs.next()) {
                            System.out.println("Name:" + rs.getString("name"));
                            System.out.println("Surname: " + rs.getString("surname"));
                            System.out.println("Fathername: " + rs.getString("fathername"));
                            System.out.println("Age: " + rs.getInt("age"));
                            System.out.println("Mail: " + rs.getString("mail"));
                            System.out.println("Birthday: " + rs.getDate("birthday"));
                            System.out.println("Is");
                            if (!rs.getBoolean("budget"))
                                System.out.print("not ");
                            System.out.print("on budget");
                            System.out.println("Average mark: " + rs.getFloat("avg_mark"));
                            System.out.println("Group name: ");
                            ResultSet rs2 = stmt.executeQuery("SELECT name FROM stgroup where id = " + rs.getInt("stgroup") + ";");
                            rs2.next();
                            System.out.print(rs2.getString("name"));
                        }
                            break;
                    case "teacher":
                        while (rs.next()) {
                            System.out.println("Name:" + rs.getString("name"));
                            System.out.println("Surname: " + rs.getString("surname"));
                            System.out.println("Fathername: " + rs.getString("fathername"));
                            System.out.println("Age: " + rs.getInt("age"));
                            System.out.println("Mail: " + rs.getString("mail"));
                            System.out.println("Birthday: " + rs.getDate("birthday"));
                            System.out.println("Speciality: " + rs.getString("speciality"));
                            System.out.println("Degree: " + rs.getString("degree"));
                            String faculty = rs.getString("faculty");
                            Statement groups = con.createStatement();
                            ResultSet rs2 = stmt.executeQuery("SELECT name FROM faculty where id = " + faculty + ";");
                            rs2.next();
                            System.out.println ("Faculty: " + rs2.getString("name"));
                            groups.close();

                            rs.next();
                            ResultSet rs1 = stmt.executeQuery("SELECT stgroup FROM teaching where teacher = " + rs.getInt("id") + ";");
                            rs2.next();
                            System.out.println("Teaching in next groups: ");
                            while (rs1.next()) {
                                rs2 = stmt.executeQuery("SELECT name FROM stgroup where id = " + rs1.getInt("stgroup") + ";");
                                rs2.next();
                                System.out.println(rs2.getString("name"));
                                rs1.next();
                            }
                        }
                        break;


                }

                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong. Please check input data");
        }
    }
}