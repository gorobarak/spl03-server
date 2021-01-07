package bgu.spl.net.impl.BGRSServer;

import java.util.List;

public class courseData {
    private int availableSlots;
    private int maxSlots;
    private String courseNum;
    private String courseName;
    private List<String> kdams; //courseNums ordered in the same order as they appear in Courses.txt TODO: when printing make in the desired format

    public courseData(int availableSlots,int maxSlots,String courseNum,String courseName,List<String> kdams){
        this.availableSlots = availableSlots;
        this.maxSlots = maxSlots;
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.kdams = kdams;

    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void addStudent() {
        availableSlots--;
    }

    public void removeStudent() {
        availableSlots++;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<String> getKdams() {
        return kdams;
    }

    public String getCourseNum() {
        return courseNum;
    }
}
