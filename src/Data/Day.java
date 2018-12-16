package Data;

import java.util.ArrayList;
import java.util.List;

public class Day {
    private int dayNumber_;
    private String dayName_;
    private int week_;
    private List<Lesson> lessons_;

    public static List<String> GetDayNames(){
        List<String> dayNames = new ArrayList<>();
        dayNames.add("Monday"); dayNames.add("Thursday"); dayNames.add("Wednesday");
        dayNames.add("Thursday"); dayNames.add("Friday"); dayNames.add("Saturday");
        return dayNames;
    }

    public Day(int dayNumber, int week, List<Lesson> lessons){
        dayNumber_ = dayNumber;
        week_ = week;
        dayName_ = GetDayNames().get(dayNumber);
        lessons_ = lessons;
    }

    public Day(int dayNumber, int week){
        dayNumber_ = dayNumber;
        week_ = week;
        dayName_ = GetDayNames().get(dayNumber);
        for(int i = 0; i < 5; i++){
            lessons_.add(new Lesson(i));
        }
    }

    public int getDayNumber() {
        return dayNumber_;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber_ = dayNumber;
    }

    public String getDayName() {
        return dayName_;
    }

    public void setDayName(String dayName) {
        this.dayName_ = dayName;
    }

    public int getWeek() {
        return week_;
    }

    public void setWeek(int week) {
        this.week_ = week;
    }

    public List<Lesson> getLessons() {
        return lessons_;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons_ = lessons;
    }
}
