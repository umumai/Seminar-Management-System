package models;

//object that holds the session information
public class Session {
    private String sessionID;
    private String date;
    private String venue;
    private String sessionType;

    public Session(String id, String date, String venue, String type){
        this.sessionID = id;
        this.date = date;
        this.venue = venue;
        this.sessionType = type;
    }

    public String getSessionID(){
        return sessionID;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getVenue() {
        return venue;
    }
    
    public String getSessionType() {
        return sessionType;
    }
    
    public String getDetails() { //returns the session information in a formatted string (presentation type is chosen by student in registration form)
        return String.format("Date: %s | Venue: %s", date, venue);
    }
}
