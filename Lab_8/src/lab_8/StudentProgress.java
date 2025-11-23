/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
import java.util.*;
/**
 *
 * @author Dell
 */
public class StudentProgress {

    private String studentId;
    private Map<Integer, List<String>> progress;
    private List<QuizAttempt> attempts;
    public StudentProgress(String studentId) {
        this.studentId = studentId;
        this.attempts = new ArrayList<>();
        this.progress=new HashMap<>();
    }

    public String getStudentId() {
        return studentId;
    }
   public Map<Integer, List<String>> getProgress() {
        return progress;
    }
   public void markLessonCompleted(int courseId, String lessonId) {
        progress.putIfAbsent(courseId, new ArrayList<>());
        List<String> completedLessons = progress.get(courseId);

        if (!completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId);
        }
    }

    public boolean isLessonCompleted(int courseId, String lessonId) {
        if (!progress.containsKey(courseId)) return false;
          return progress.get(courseId).contains(lessonId);
    }

    public List<String> getCompletedLessons(int courseId) {
        return progress.getOrDefault(courseId, new ArrayList<>());
    }

    public void addAttempt(QuizAttempt attempt) {
        if (attempt != null) {
            attempts.add(attempt);
        }
    }

    public List<QuizAttempt> getAttempts() {
        return attempts;
    }

    public QuizAttempt getLastAttempt(Quiz quiz) {
        for (int i = attempts.size() - 1; i >= 0; i--) {
            if (attempts.get(i).getQuiz().equals(quiz)) {
                return attempts.get(i);
            }
        }
        return null;
    }

    public double getAverageScoreForAttempts() {
        if (attempts.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (QuizAttempt attempt : attempts) {
            sum += attempt.getScore();
        }
        return sum / attempts.size();
    }

    public double getAverageScoreForQuiz(Quiz quiz) {
        //List<QuizAttempt> list =new ArrayList<>();
        //for (QuizAttempt a :attempts) {
        //     if (a.getQuiz().equals(quiz)) {
         //        list.add(a);
         //    }
       // }
        //if (list.isEmpty()) 
        //    return 0;
        //double sum = 0;
        //for (QuizAttempt a : list) {
         //    sum += a.getScore();
      //  }    
        //return sum / list.size();
       // }  
       double sum = 0;
        int count = 0;

        for (QuizAttempt a : attempts) {
            if (a.getQuiz().equals(quiz)) {
                sum += a.getScore();
                count++;
            }
        }

        return (count == 0) ? 0 : sum / count;
    }
}
