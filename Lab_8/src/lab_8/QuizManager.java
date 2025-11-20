/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author Dell
 */
public class QuizManager {

    private List<QuizAttempt> attempts = new ArrayList<>();
    private HashMap<String, StudentProgress> studentProgressMap = new HashMap<>();

    public Quiz getQuizForLesson(Lesson lesson) {
        if (lesson == null) {
            return null;
        }
        return lesson.getQuiz();
    }

    public int evaluateQuiz(Quiz quiz, List<Integer> userAnswers) {
        if (quiz == null) {
            return 0;
        }
        return quiz.calculateScore(userAnswers);
    }

    public QuizAttempt saveAttempt(String studentId, String courseId, Quiz quiz, int score) {
        boolean passed = quiz.isPassed(score);
        QuizAttempt attempt = new QuizAttempt(studentId, courseId, quiz.getLessonId(), score, passed, quiz);
        attempts.add(attempt);
        StudentProgress sp = studentProgressMap.getOrDefault(studentId, new StudentProgress(studentId));
        sp.addAttempt(attempt);
        studentProgressMap.put(studentId, sp);
        return attempt;
    }

    public void addAttempt(QuizAttempt attempt) {
        attempts.add(attempt);
    }

    public StudentProgress getStudentProgress(String studentId) {
        return studentProgressMap.get(studentId);
    }

    public boolean hasPassedLesson(String studentId, String lessonId) {
        StudentProgress sp = studentProgressMap.get(studentId);
        if (sp == null) {
            return false;
        }
        for (QuizAttempt a : sp.getAttempts()) {
            if (a.getLessonId().equals(lessonId) && a.isPassed()) {
                return true;
            }
        }
        return false;
    }

    public List<QuizAttempt> getStudentAttempts(String studentId) {
        StudentProgress sp = studentProgressMap.get(studentId);
        if (sp == null) {
            return new ArrayList<>();
        }
        return sp.getAttempts();
    }

}
