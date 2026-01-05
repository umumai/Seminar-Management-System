public class Coordinator extends User {
    
    public Coordinator(String id, String name){
        super(id, name, "Coordinator");
    }

    public void createSession(Session session){
        System.out.println("Session created: " + session.getSessionID());
    }

    public void generateReport(Report report){
        report.generate();
    }
}
