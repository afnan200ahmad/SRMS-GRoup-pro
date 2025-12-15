package app;

import io.FileManager;
import repo.StudentRepository;
import service.ReportService;
import threading.AutoSaveTask;
import model.*;

import java.util.List;
import java.util.Scanner;

public class SRMSApp {
    private static final String DATA_FILE = "data/students.csv";

    public static void main(String[] args) {
        StudentRepository repo = new StudentRepository();
        FileManager fm = new FileManager();
        ReportService rs = new ReportService(repo);

        // Load at start
        try {
            List<Student> loaded = fm.loadCsv(DATA_FILE);
            repo.getAll().addAll(loaded);
        } catch (Exception e) {
            System.out.println("No previous data loaded (first run or file not found).");
        }

        // Start autosave thread
        AutoSaveTask task = new AutoSaveTask(repo, fm, DATA_FILE, 30); // every 30s
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== SRMS Menu ===");
            System.out.println("1) Add Student");
            System.out.println("2) Remove Student");
            System.out.println("3) Search Student");
            System.out.println("4) List All");
            System.out.println("5) Reports");
            System.out.println("6) Save Now");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addStudent(sc, repo);
                    case "2" -> removeStudent(sc, repo);
                    case "3" -> searchStudent(sc, repo);
                    case "4" -> listAll(repo);
                    case "5" -> rs.printAllReports();
                    case "6" -> fm.saveCsv(DATA_FILE, repo.getAll());
                    case "0" -> {
                        fm.saveCsv(DATA_FILE, repo.getAll());
                        System.out.println("Saved. Bye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void addStudent(Scanner sc, StudentRepository repo) {
        System.out.print("ID: ");
        String id = sc.nextLine();

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Department: ");
        String dept = sc.nextLine();

        System.out.print("Year (1-6): ");
        int year = Integer.parseInt(sc.nextLine());

        System.out.print("GPA (0-5): ");
        double gpa = Double.parseDouble(sc.nextLine());

        System.out.print("Type (U=Undergraduate, P=Postgraduate): ");
        String type = sc.nextLine().trim().toUpperCase();

        Student s;
        if (type.equals("U")) {
            System.out.print("Track: ");
            String track = sc.nextLine();
            s = new UndergraduateStudent(id, name, dept, year, gpa, track);
        } else {
            System.out.print("Research Topic: ");
            String topic = sc.nextLine();
            s = new PostgraduateStudent(id, name, dept, year, gpa, topic);
        }

        repo.add(s);
        System.out.println("Added: " + s);
    }

    private static void removeStudent(Scanner sc, StudentRepository repo) {
        System.out.print("Enter ID to remove: ");
        String id = sc.nextLine();
        boolean ok = repo.removeById(id);
        System.out.println(ok ? "Removed." : "Not found.");
    }

    private static void searchStudent(Scanner sc, StudentRepository repo) {
        System.out.print("Enter ID to search: ");
        String id = sc.nextLine();
        Student s = repo.findById(id);
        System.out.println(s == null ? "Not found." : s.toString());
    }

    private static void listAll(StudentRepository repo) {
        if (repo.getAll().isEmpty()) {
            System.out.println("No students.");
            return;
        }
        for (Student s : repo.getAll()) System.out.println(s);
    }
}
