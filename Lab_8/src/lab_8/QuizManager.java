/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Dell
 */
public class QuizManager {
    private List<QuizAttempt> attempts=new ArrayList<>();
    public Quiz getQuizForLesson(Lesson lesson){
        if(lesson==null){
            return null;
        }
        return lesson.getQuiz;
    }
    public int evaluateQuiz(Quiz quiz,List<Integer>userAnswers){
        if(quiz==null)
            return 0;
        return quiz.calculateScore(userAnswers);
    }
    public QuizAttempt saveAttempt(String studentId,String courseId,Quiz quiz,int score){
        boolean passed=quiz.isPassed(score);
        QuizAttempt attempt=new QuizAttempt(studentId,courseId,quiz.getLessonId(),score,passed);
        attempts.add(attempt);
        return attempt;
    }
    public boolean hasPassedLesson(String studentId,String lessonId){
        for(QuizAttempt a:attempts){
            if(a.getStudentId().equals(studentId) && a.getLessonId().equals(lessonId) && a.isPassed()){
                return true;
            }
        }
        return false;
    }
    public List<QuizAttempt> getStudentAttempts(String studentId){
        List<QuizAttempt> list=new ArrayList<>();
        for(QuizAttempt a:attempts){
            if(a.getStudentId().equals(studentId))
                list.add(a);
        }
        return list;
    }
    
}
