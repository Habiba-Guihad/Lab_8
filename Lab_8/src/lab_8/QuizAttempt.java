/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author Dell
 */
public class QuizAttempt {
    private String studentId;
    private String courseId;
    private String lessonId; 
    private int score;
    private boolean passed;
    private LocalDateTime attemptTime;

    public QuizAttempt(String studentId, String courseId, String lessonId, int score, boolean passed, LocalDateTime attemptTime) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.score=score;
        this.passed=passed;
        this.attemptTime =attemptTime;
        
    }

    public QuizAttempt(String studentId, String courseId, String lessonId, int score, boolean passed) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.score = score;
        this.passed = passed;
        this.attemptTime = LocalDateTime.now();
    }
    

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getLessonId() {
        return lessonId;
    }
    public int getScore() {
        return score;
    }

    public boolean isPassed() {
        return passed;
    }

    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }
    
}
