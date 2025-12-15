package service;

import model.Student;
import repo.StudentRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
    private final StudentRepository repo;

    public ReportService(StudentRepository repo) {
        this.repo = repo;
    }

    public String reportTopByGpa(int topN) {
        List<Student> sorted = repo.sortByGpaDesc();
        StringBuilder sb = new StringBuilder();
        sb.append("=== Report: Top ").append(topN).append(" by GPA ===\n");
        for (int i = 0; i < Math.min(topN, sorted.size()); i++) {
            sb.append(i + 1).append(") ").append(sorted.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String reportByDepartment() {
        List<Student> all = repo.listAll();
        Map<String, Long> counts = all.stream()
                .collect(Collectors.groupingBy(Student::getDepartment, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        sb.append("=== Report: Students per Department ===\n");
        counts.forEach((dept, count) -> sb.append(dept).append(" -> ").append(count).append("\n"));
        return sb.toString();
    }

    public String reportByYear() {
        List<Student> all = repo.listAll();
        Map<Integer, Long> counts = all.stream()
                .collect(Collectors.groupingBy(Student::getYear, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        sb.append("=== Report: Students per Year ===\n");
        counts.forEach((year, count) -> sb.append("Year ").append(year).append(" -> ").append(count).append("\n"));
        return sb.toString();
    }
}
