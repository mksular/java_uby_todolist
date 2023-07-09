import java.time.LocalDate;

public class Todo {
    int id, categoryId, priorityId, complatePercent;
    String title, note;
    LocalDate startDate, endDate;
    public Todo(){
        this.complatePercent=0;
    }

    public Todo(int id, String title, LocalDate startDate, LocalDate endDate, int categoryId, int priorityId, int complatePercent, String note){
      this.id=id;
      this.title=title;
      this.startDate=startDate;
      this.endDate=endDate;
      this.categoryId=categoryId;
      this.priorityId=priorityId;
      this.complatePercent=complatePercent;
      this.note=note;        
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title=title;
    }



    
}