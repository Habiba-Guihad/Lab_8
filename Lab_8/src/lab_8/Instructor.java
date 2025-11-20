/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {

    private ArrayList<String> createdCourses;
    private ArrayList<String> createdLessons;

    // Constructor when loading from database
    public Instructor(String userId, String username, String email, String passwordHash,
                      ArrayList<String> createdCourses) {
        super(userId, Role.INSTRUCTOR, username, email, passwordHash);
        this.createdCourses = (createdCourses != null) ? createdCourses : new ArrayList<>();
        this.createdLessons = new ArrayList<>();
    }

    // Constructor when newly created (sign up)
    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, Role.INSTRUCTOR, username, email, passwordHash);
        this.createdCourses = new ArrayList<>();
        this.createdLessons = new ArrayList<>();
    }

    // Create a course and save instructor's courses to users.json
    public String addCourse(CourseManager manager, String title, String description) {
        Course c = manager.addCourse(title, description, this.getUserId());
        if (c != null) {
            createdCourses.add(c.getCourseId());
            manager.getDbManager().saveUsers(); // save instructor's updated createdCourses
            return c.getCourseId();
        }
        return null;
    }

    // Edit only if this instructor owns the course
    public boolean editCourse(CourseManager manager, String courseId, String newTitle, String newDescription) {
        Course c = manager.getCourse(courseId);
        if (c == null || !c.getInstructorId().equals(this.getUserId())) return false;

        boolean edited = manager.editCourse(courseId, newTitle, newDescription);
        if (edited) {
            manager.getDbManager().saveUsers(); // save any potential changes
        }
        return edited;
    }

    // Delete only if instructor owns the course
    public boolean deleteCourse(CourseManager manager, String courseId) {
        Course c = manager.getCourse(courseId);
        if (c == null || !c.getInstructorId().equals(this.getUserId())) return false;

        boolean deleted = manager.deleteCourse(courseId);
        if (deleted) {
            createdCourses.remove(courseId);
            manager.getDbManager().saveUsers(); // save updated instructor info
        }
        return deleted;
    }

    // Add lesson only if instructor owns the course
    public String addLesson(LessonManager manager, CourseManager courseManager,
                            String courseId, String title, String content) {

        Course course = courseManager.getCourse(courseId);
        if (course == null || !course.getInstructorId().equals(this.getUserId())) return null;

        Lesson lesson = manager.addLesson(course, title, content);
        if (lesson != null) {
            createdLessons.add(lesson.getLessonId());
            courseManager.getDbManager().saveCourses(); // save the updated course with new lesson
            return lesson.getLessonId();
        }
        return null;
    }

    // Edit lesson only in instructor's own course
    public boolean editLesson(LessonManager manager, CourseManager courseManager,
                              String courseId, String lessonId,
                              String newTitle, String newContent) {

        Course course = courseManager.getCourse(courseId);
        if (course == null || !course.getInstructorId().equals(this.getUserId())) return false;
        if (!createdLessons.contains(lessonId)) return false;

        boolean edited = manager.editLesson(course, lessonId, newTitle, newContent);
        if (edited) {
            courseManager.getDbManager().saveCourses(); // save changes to courses.json
        }
        return edited;
    }

    // Delete lesson only in instructor's own course
    public boolean deleteLesson(LessonManager manager, CourseManager courseManager,
                                String courseId, String lessonId) {

        Course course = courseManager.getCourse(courseId);
        if (course == null || !course.getInstructorId().equals(this.getUserId())) return false;

        boolean deleted = manager.deleteLesson(course, lessonId);
        if (deleted) {
            createdLessons.remove(lessonId);
            courseManager.getDbManager().saveCourses(); // save changes to courses.json
        }
        return deleted;
    }

    // View students only in instructor's own course
    public List<String> viewEnrolledStudents(CourseManager manager, String courseId) {
        Course c = manager.getCourse(courseId);
        if (c == null || !c.getInstructorId().equals(this.getUserId())) return null;
        return new ArrayList<>(c.getStudents());
    }

    public ArrayList<String> getCreatedCourses() {
        return createdCourses;
    }

    public ArrayList<String> getCreatedLessons() {
        return createdLessons;
    }
}
