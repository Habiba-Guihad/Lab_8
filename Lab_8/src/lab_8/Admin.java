/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;

/**
 *
 * @author New Eng
 */
public class Admin extends User {

    public Admin(String userId, String username, String email, String passwordHash) {
        super(userId, Role.ADMIN, username, email, passwordHash);
    }

    public boolean approveCourse(CourseManager courseManager, String courseId) {
        return courseManager.approveCourse(courseId);
    }

    public boolean rejectCourse(CourseManager courseManager, String courseId) {
        return courseManager.rejectCourse(courseId);
    }
}
