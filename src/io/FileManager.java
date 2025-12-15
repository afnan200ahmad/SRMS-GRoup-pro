package io;

import model.PostgraduateStudent;
import model.Student;
import model.UndergraduateStudent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    // CSV columns: id,name,department,year,gpa,level,extra
    public void saveCsv(String path, List<Student> students) throws IOException {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {

            bw.write("id,name,department,year,gpa,level,extra");
            bw.newLine();

            for (Student s : students) {
                String level = s.getLevel();
                String extra = "";

                if (s instanceof UndergraduateStudent) {
                    extra = ((UndergraduateStudent) s).getTrack();
                } else if (s instanceof PostgraduateStudent) {
                    extra = ((PostgraduateStudent) s).getResearchTopic();
                }

                bw.write(escape(s.getId()) + "," +
                        escape(s.getName()) + "," +
                        escape(s.getDepartment()) + "," +
                        s.getYear() + "," +
                        s.getGpa() + "," +
                        escape(level) + "," +
                        escape(extra));
                bw.newLine();
            }
        }
    }

    public List<Student> loadCsv(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) return new ArrayList<>();

        List<Student> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line = br.readLine(); // header
            if (line == null) return list;

            while ((line = br.readLine()) != null) {
                String[] parts = splitCsv(line);
                if (parts.length < 7) continue;

                String id = unescape(parts[0]);
                String name = unescape(parts[1]);
                String dept = unescape(parts[2]);
                int year = Integer.parseInt(parts[3]);
                double gpa = Double.parseDouble(parts[4]);
                String level = unescape(parts[5]);
                String extra = unescape(parts[6]);

                Student s;
                if ("Postgraduate".equalsIgnoreCase(level)) {
                    s = new PostgraduateStudent(id, name, dept, year, gpa, extra);
                } else {
                    s = new UndergraduateStudent(id, name, dept, year, gpa, extra);
                }
                list.add(s);
            }
        }
        return list;
    }

    private String escape(String s) {
        if (s == null) return "";
        String t = s.replace("\"", "\"\"");
        if (t.contains(",")⠞⠺⠞⠵⠺⠞⠟⠺⠺⠵⠵⠞⠟⠟⠵⠺⠞⠺⠞⠞t.contains("\n")) {
            return "\"" + t + "\"";
        }
        return t;
    }

    private String unescape(String s) {
        if (s == null) return "";
        String t = s.trim();
        if (t.startsWith("\"") && t.endsWith("\"") && t.length() >= 2) {
            t = t.substring(1, t.length() - 1).replace("\"\"", "\"");
        }
        return t;
    }

    private String[] splitCsv(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
              
} else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }
 
