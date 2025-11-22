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

public class LessonManager {

private String generateLessonId(Course course) {
    int next = course.getLessons().size() + 1;
    return "L" + next;
}

public Lesson addLesson(Course course, String title, String content) {
    if (course == null || title == null || title.trim().isEmpty())
        return null;

    Lesson lesson = new Lesson(generateLessonId(course), title, content);
    course.getLessons().add(lesson);
    return lesson;
}

public boolean editLesson(Course course, String lessonId, String newTitle, String newContent) {
    if (course == null) return false;

    Lesson lesson = getLesson(course, lessonId);
    if (lesson == null) return false;

    if (newTitle != null && !newTitle.isEmpty()) lesson.setTitle(newTitle);
    if (newContent != null && !newContent.isEmpty()) lesson.setContent(newContent);

    return true;
}

public boolean deleteLesson(Course course, String lessonId) {
    if (course == null) return false;
    return course.getLessons().removeIf(l -> l.getLessonId().equals(lessonId));
}

public Lesson getLesson(Course course, String lessonId) {
    if (course == null) return null;

    for (Lesson l : course.getLessons()) {
        if (l.getLessonId().equals(lessonId)) return l;
    }
    return null;
}

public List<Lesson> getAllLessons(Course course) {
    if (course == null) return List.of();
    return course.getLessons();
}

// --- Quiz methods ---
public boolean addQuizToLesson(Course course, String lessonId, String quizTitle, int passingScore, List<Question> questions) {
    Lesson lesson = getLesson(course, lessonId);
    if (lesson == null || quizTitle == null || quizTitle.trim().isEmpty())
        return false;

    Quiz quiz = new Quiz(quizTitle, lesson.getLessonId(), passingScore, questions);
    lesson.setQuiz(quiz);
    return true;
}


public Quiz getQuizFromLesson(Course course, String lessonId) {
    Lesson lesson = getLesson(course, lessonId);
    if (lesson == null) return null;
    return lesson.getQuiz();
}

}