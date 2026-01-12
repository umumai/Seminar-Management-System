package models;

public class Evaluator extends User {
    public Evaluator (String id, String name){
        super(id, name, "Evaluator");
    }
    
    public void evaluateSubmission(Submission submission, Evaluation evaluation){
        System.out.println(
            getName() + " evaluated submission " + submission.getSubmissionID()
        );
    }
}
