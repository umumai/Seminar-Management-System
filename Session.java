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
    
}
