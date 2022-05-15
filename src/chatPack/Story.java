package chatPack;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;

//Task for : Mohamed yehia
public class Story {
    private final int storyUploaderId;
    private String storyText;
    private final Date storyUploadedDate;
    private final Time storyUploadedTime;

    public Story(int storyUploaderId, String storyText, Date storyUploadedDate, Time storyUploadedTime) {
        this.storyUploaderId = storyUploaderId;
        this.storyText = storyText;
        this.storyUploadedTime = storyUploadedTime;
        this.storyUploadedDate = storyUploadedDate;
    }


    public int getStoryUploaderId() {
        return storyUploaderId;
    }

    public String getStoryText() {
        return storyText;
    }

    public Time getStoryUploadedTime() {
        return storyUploadedTime;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public Date getStoryUploadedDate() {
        return storyUploadedDate;
    }


}
