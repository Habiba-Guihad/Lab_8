/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;

/**
 *
 * @author New Eng
 */
import java.util.*;
import java.util.stream.Collectors;
//dd
public class CourseManager {

    private JsonDatabaseManager db;

    public CourseManager(JsonDatabaseManager db) {
        this.db = db;
    }

    public JsonDatabaseManager getDbManager() {
        return db;
    }

    private String generateCourseId() {
        ArrayList<Course> courses = db.getCourses();
        return "C" + (courses.size() + 1);
    }

    public Course addCourse(String title, String description, String instructorId) {
        if (title == null || title.trim().isEmpty())
            return null;

        Course c = new Course(generateCourseId(), title, description, instructorId);
        c.setApprovalStatus(Course.ApprovalStatus.PENDING);

        db.getCourses().add(c);
        db.saveCourses();
        db.saveUsers();
        return c;
    }

    public boolean editCourse(String id, String newTitle, String newDescription) {
        Course c = getCourse(id);
        if (c == null)
            return false;

        if (newTitle != null && !newTitle.trim().isEmpty())
            c.setTitle(newTitle);

        if (newDescription != null && !newDescription.trim().isEmpty())
            c.setDescription(newDescription);

        db.saveCourses();
        return true;
    }

    public boolean deleteCourse(String id) {
        Course c = getCourse(id);
        if (c == null)
            return false;

        db.getCourses().remove(c);
        db.saveCourses();
        return true;
    }

    public Course getCourse(String id) {
        for (Course c : db.getCourses()) {
            if (c.getCourseId().equals(id))
                return c;
        }
        return null;
    }

    public ArrayList<Course> getAllCourses() {
        return db.getCourses();
    }

    public List<Course> getCoursesByInstructor(String instructorId) {
        return db.getCourses().stream()
                .filter(c -> c.getInstructorId().equals(instructorId))
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // THE CRITICAL FIX — Add course to instructor upon approval
    // ---------------------------------------------------------
    public boolean approveCourse(String courseId) {
        Course c = getCourse(courseId);
        if (c == null)
            return false;

        c.setApprovalStatus(Course.ApprovalStatus.APPROVED);

        // Find the instructor and update createdCourses
        for (User u : db.getUsers()) {
            if (u instanceof Instructor ins && ins.getUserId().equals(c.getInstructorId())) {

                if (!ins.getCreatedCourses().contains(courseId)) {
                    ins.getCreatedCourses().add(courseId);
                }

                db.saveUsers();
                break;
            }
        }

        db.saveCourses();
        return true;
    }

    public boolean rejectCourse(String courseId) {
        Course c = getCourse(courseId);
        if (c == null)
            return false;

        c.setApprovalStatus(Course.ApprovalStatus.REJECTED);
        db.saveCourses();
        return true;
    }

    public ArrayList<Course> getPendingCourses() {
        ArrayList<Course> pending = new ArrayList<>();
        for (Course c : db.getCourses()) {
            if (c.getApprovalStatus() == Course.ApprovalStatus.PENDING)
                pending.add(c);
        }
        return pending;
    }

    public ArrayList<Course> getApprovedCourses() {
        ArrayList<Course> approved = new ArrayList<>();
        for (Course c : db.getCourses()) {
            if (c.getApprovalStatus() == Course.ApprovalStatus.APPROVED)
                approved.add(c);
        }
        return approved;
    }

    public boolean enrollStudent(String courseId, String studentId) {
        Course c = getCourse(courseId);
        if (c == null)
            return false;

        if (c.getApprovalStatus() != Course.ApprovalStatus.APPROVED)
            return false;

        if (c.getStudents().contains(studentId))
            return false;

        c.getStudents().add(studentId);
        db.saveCourses();
        return true;
    }
}
