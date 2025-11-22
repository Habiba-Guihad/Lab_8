/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab_8;

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

// -------------------- Existing course/lesson methods --------------------
public String addCourse(CourseManager manager, String title, String description) {
    Course c = manager.addCourse(title, description, this.getUserId());
    if (c != null) {
        createdCourses.add(c.getCourseId());
        manager.getDbManager().saveUsers();
        return c.getCourseId();
    }
    return null;
}

public boolean editCourse(CourseManager manager, String courseId, String newTitle, String newDescription) {
    Course c = manager.getCourse(courseId);
    if (c == null || !c.getInstructorId().equals(this.getUserId())) return false;
    boolean edited = manager.editCourse(courseId, newTitle, newDescription);
    if (edited) manager.getDbManager().saveUsers();
    return edited;
}

public boolean deleteCourse(CourseManager manager, String courseId) {
    Course c = manager.getCourse(courseId);
    if (c == null || !c.getInstructorId().equals(this.getUserId())) return false;
    boolean deleted = manager.deleteCourse(courseId);
    if (deleted) {
        createdCourses.remove(courseId);
        manager.getDbManager().saveUsers();
    }
    return deleted;
}

public String addLesson(LessonManager manager, CourseManager courseManager,
                        String courseId, String title, String content) {
    Course course = courseManager.getCourse(courseId);
    if (course == null || !course.getInstructorId().equals(this.getUserId())) return null;

    Lesson lesson = manager.addLesson(course, title, content);
    if (lesson != null) {
        createdLessons.add(lesson.getLessonId());
        courseManager.getDbManager().saveCourses();
        return lesson.getLessonId();
    }
    return null;
}

public boolean editLesson(LessonManager manager, CourseManager courseManager,
                          String courseId, String lessonId,
                          String newTitle, String newContent) {
    Course course = courseManager.getCourse(courseId);
    if (course == null || !course.getInstructorId().equals(this.getUserId())) return false;
    if (!createdLessons.contains(lessonId)) return false;

    boolean edited = manager.editLesson(course, lessonId, newTitle, newContent);
    if (edited) courseManager.getDbManager().saveCourses();
    return edited;
}

public boolean deleteLesson(LessonManager manager, CourseManager courseManager,
                            String courseId, String lessonId) {
    Course course = courseManager.getCourse(courseId);
    if (course == null || !course.getInstructorId().equals(this.getUserId())) return false;

    boolean deleted = manager.deleteLesson(course, lessonId);
    if (deleted) {
        createdLessons.remove(lessonId);
        courseManager.getDbManager().saveCourses();
    }
    return deleted;
}

public List<String> viewEnrolledStudents(CourseManager manager, String courseId) {
    Course c = manager.getCourse(courseId);
    if (c == null || !c.getInstructorId().equals(this.getUserId())) return null;
    return new ArrayList<>(c.getStudents());
}

public ArrayList<String> getCreatedCourses() { return createdCourses; }
public ArrayList<String> getCreatedLessons() { return createdLessons; }

// -------------------- Quiz methods --------------------
public boolean addQuiz(LessonManager lessonManager, CourseManager courseManager,
                       String courseId, String lessonId, String quizTitle,
                       int passingScore, List<Question> questions) {

    Course course = courseManager.getCourse(courseId);
    if (course == null || !course.getInstructorId().equals(this.getUserId())) return false;
    if (!createdLessons.contains(lessonId)) return false;

    boolean added = lessonManager.addQuizToLesson(course, lessonId, quizTitle, passingScore, questions);
    if (added) courseManager.getDbManager().saveCourses();
    return added;
}

public Quiz getQuiz(LessonManager lessonManager, CourseManager courseManager,
                    String courseId, String lessonId) {

    Course course = courseManager.getCourse(courseId);
    if (course == null || !course.getInstructorId().equals(this.getUserId())) return null;
    if (!createdLessons.contains(lessonId)) return null;

    return lessonManager.getQuizFromLesson(course, lessonId);
}

public boolean addQuestionToQuiz(CourseManager courseManager,
LessonManager lessonManager,
String courseId,
String lessonId,
String quizTitle,
Question question) {
// 1. Find the course
Course course = courseManager.getCourse(courseId);
if (course == null || !course.getInstructorId().equals(this.getUserId())) {
return false;
}


// 2. Find the lesson
Lesson lesson = lessonManager.getLesson(course, lessonId);
if (lesson == null) {
    return false;
}

// 3. Get the quiz from the lesson
Quiz quiz = lesson.getQuiz();
if (quiz == null || !quiz.getTitle().equals(quizTitle)) {
    return false;
}

// 4. Add the question
quiz.getQuestions().add(question);

// 5. Save changes
courseManager.getDbManager().saveCourses();

return true;

}




}
