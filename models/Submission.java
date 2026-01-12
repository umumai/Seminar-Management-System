package models;
import java.util.Date;

public class Submission {
    private String submissionID;
    private String filePath;
    private Date submissionDate;

    public Submission(String id, String filePath) {
        this.submissionID = id;
        this.filePath = filePath;
        this.submissionDate = new Date();
    }

    public String getSubmissionID(){
        return submissionID;
    }
}
