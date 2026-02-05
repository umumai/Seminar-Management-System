package models;

public class Appointment {
    private final String sessionID;
    private final String studentID;
    private final String evaluatorID;
    private final int timeSlot;

    public Appointment(String sessionID, String studentID, String evaluatorID, int timeSlot){
        this.sessionID = sessionID;
        this.studentID = studentID;
        this.evaluatorID = evaluatorID;
        this.timeSlot = timeSlot;
    }

    public String getSessionID() { return sessionID;}
    public String getStudentID() { return studentID;}
    public String getEvaluatorID() { return evaluatorID;}
    public int gettimeSlot() { return timeSlot;}
    
}
