public class Report {
    private String reportType;

    public Report(String reportType){
        this.reportType = reportType;
    }

    public void generate(){
        System.out.println("Report generated: " + reportType);
    }
    
}
