package model;

public class UndergraduateStudent extends Student {
    private String track; // extra

    public UndergraduateStudent(String id, String name, String department, int year, double gpa, String track) {
        super(id, name, department, year, gpa);
        this.track = (track == null) ? "" : track.trim();
    }

    public String getTrack() { return track; }
    public void setTrack(String track) { this.track = (track == null) ? "" : track.trim(); }

    @Override
    public String getLevel() { return "Undergraduate"; }
}