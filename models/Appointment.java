package models;

public class Appointment {
    private final int sessionID;
    private final String studentID;
    private String evaluatorID;
    private String timeSlot;
    private String status;

    public Appointment(int sessionID, String studentID, String evaluatorID, String timeSlot, String status){
        this.sessionID = sessionID;
        this.studentID = studentID;
        this.evaluatorID = evaluatorID;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    public int getSessionID() { return sessionID;}
    public String getStudentID() { return studentID;}
    public String getEvaluatorID() { return evaluatorID;}
    public String getTimeSlot() { return timeSlot;}
    public String getStatus() { return status; }

    public void setEvaluatorID(String evaluatorID) { this.evaluatorID = evaluatorID; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public void setStatus(String status) { this.status = status; }

}