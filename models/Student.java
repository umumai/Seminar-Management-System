package models;

public class Student extends User {
    private String supervisorName;
    private String researchTitle;
    private String abstractText;
    private String presentationType;

    public Student( String id, String name, String supervisorName, String researchTitle, String abstractText, String presentationType){
        super(id, name, "Student");
        this.supervisorName = supervisorName;
        this.researchTitle = researchTitle;
        this.abstractText = abstractText;
        this.presentationType = presentationType;
    }

    public void registerSeminar(){
        System.out.println(getName() + " registered for seminar.");
    }
}
