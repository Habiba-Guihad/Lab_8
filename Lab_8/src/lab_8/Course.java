/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;

/**
 *
 * @author New Eng
 */
import java.util.ArrayList;
import java.util.List;


public class Course {

    private String courseId;      
    private String title;
    private String description;
    private String instructorId;

    private List<Lesson> lessons = new ArrayList<>();
    private List<String> students = new ArrayList<>();

    public Course(String courseId, String title, String description, String instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
    }

    public String getCourseId() { 
      return courseId; }
    public String getTitle() {
      return title; }
    public String getDescription() { 
      return description; }
    public String getInstructorId() { 
      return instructorId; }

    public void setTitle(String title) {
      this.title = title; }
    public void setDescription(String description) {
      this.description = description; }

    public List<Lesson> getLessons() { 
      return lessons; }
    public List<String> getStudents() { 
      return students; }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public boolean deleteLesson(String id) {
        return lessons.removeIf(l -> l.getLessonId().equals(id));
    }

    @Override
    public String toString() {
        return courseId + " | " + title + " | " + description;
    }
}
