public class Evaluator extends User {
    public Evaluator (String id, String name, String password){
        super(id, name, "Evaluator", password);
    }
    
    public void evaluateSubmission(Submission submission, Evaluation evaluation){
        System.out.println(
            getName() + " evaluated submission " + submission.getSubmissionID()
        );
    }
}
