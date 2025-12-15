package model;

public class PostgraduateStudent extends Student {
    private String researchTopic; // extra

    public PostgraduateStudent(String id, String name, String department, int year, double gpa, String researchTopic) {
        super(id, name, department, year, gpa);
        this.researchTopic = (researchTopic == null) ? "" : researchTopic.trim();
    }

    public String getResearchTopic() { return researchTopic; }
    public void setResearchTopic(String researchTopic) {
        this.researchTopic = (researchTopic == null) ? "" : researchTopic.trim();
    }

    @Override
    public String getLevel() { return "Postgraduate"; }
}
