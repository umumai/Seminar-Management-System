package models;
public class Award {
    private String awardType;
    private Student winner;

    public Award(String awardType, Student winner){
        this.awardType = awardType;
        this.winner = winner;
    }
    
}
