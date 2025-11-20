/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {

    private ArrayList<String> createdCourses;   
    private ArrayList<String> createdLessons;   

    // Constructor when login (get from database)
    public Instructor(String userId, String username, String email, String passwordHash,
                      ArrayList<String> createdCourses) {

        super(userId, Role.INSTRUCTOR, username, email, passwordHash);//beynady constructor of abstract class
        this.createdCourses = (createdCourses != null) ? createdCourses : new ArrayList<>();
        this.createdLessons = new ArrayList<>();
    }

    // Constructor lama bey signin (creat new one)
    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, Role.INSTRUCTOR, username, email, passwordHash);//beynady constructor of abstract class
        this.createdCourses = new ArrayList<>();
        this.createdLessons = new ArrayList<>();
    }
    public String addCourse(CourseManager manager, String title, String description) {
        Course c = manager.addCourse(title, description, this.getUserId());
        if (c != null) {
            createdCourses.add(c.getCourseId());
            return c.getCourseId();
        }
        return null;
    }

    public boolean editCourse(CourseManager manager, String courseId, String newTitle, String newDescription) {
        if (!createdCourses.contains(courseId)) return false;
        return manager.editCourse(courseId, newTitle, newDescription);
    }

    public boolean deleteCourse(CourseManager manager, String courseId) {
        if (!createdCourses.contains(courseId)) return false;

        boolean deleted = manager.deleteCourse(courseId);
        if (deleted) {
            createdCourses.remove(courseId);
        }
        return deleted;
    }

    public String addLesson(LessonManager manager, CourseManager courseManager,
                            String courseId, String title, String content) {

        if (!createdCourses.contains(courseId)) return null;

        Course course = courseManager.getCourse(courseId);
        if (course == null) return null;

        Lesson lesson = manager.addLesson(course, title, content);
        if (lesson != null) {
            createdLessons.add(lesson.getLessonId());
            return lesson.getLessonId();
        }
        return null;
    }

    public boolean editLesson(LessonManager manager, CourseManager courseManager,
                              String courseId, String lessonId,
                              String newTitle, String newContent) {

        if (!createdLessons.contains(lessonId)) return false;

        Course course = courseManager.getCourse(courseId);
        if (course == null) return false;

        return manager.editLesson(course, lessonId, newTitle, newContent);
    }

    public boolean deleteLesson(LessonManager manager, CourseManager courseManager,
                                String courseId, String lessonId) {
        Course course = courseManager.getCourse(courseId);
        if (course == null) return false;

        boolean deleted = manager.deleteLesson(course, lessonId);
        if (deleted) {
            createdLessons.remove(lessonId);
        }
        return deleted;
    }
    
    public List<String> viewEnrolledStudents(CourseManager manager, String courseId) {
        if (!createdCourses.contains(courseId)) return null;

        Course c = manager.getCourse(courseId);
        if (c == null) return null;

        return new ArrayList<>(c.getStudents());
    }

    public ArrayList<String> getCreatedCourses() {
        return createdCourses;
    }

    public ArrayList<String> getCreatedLessons() {
        return createdLessons;
    }
}
