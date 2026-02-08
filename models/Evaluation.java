package models;
public class Evaluation {
    private int clarityScore;
    private int methodologyScore;
    private int resultsScore;
    private int presentationScore;
    private String comments;

    public Evaluation( int c, int m, int r, int p, String comments){
        this.clarityScore = c;
        this.methodologyScore = m;
        this.resultsScore = r;
        this.presentationScore = p;
        this.comments = comments;
    }
    
    public int calculateTotalScore(){
        return clarityScore + resultsScore + presentationScore;
    }
    //getters
    public int getClarityScore() {
        return clarityScore;
    }

    public int getMethodologyScore() {
        return methodologyScore;
    }

    public int getResultsScore() {
        return resultsScore;
    }

    public int getPresentationScore() {
        return presentationScore;
    }

    public String getComments() {
        return comments;
    }

}
