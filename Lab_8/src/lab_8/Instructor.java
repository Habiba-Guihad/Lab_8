/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab_8;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

// -------------------- Ownership Helpers --------------------
private boolean ownsCourse(Course course) {
    return course != null && course.getInstructorId().equals(this.getUserId());
}

private boolean ownsLesson(String lessonId) {
    return createdLessons.contains(lessonId);
}

// -------------------- Course Methods --------------------
public String addCourse(CourseManager manager, String title, String description) {
    Course c = manager.addCourse(title, description, this.getUserId());
    if (c != null) {
        if (!createdCourses.contains(c.getCourseId())) {
            createdCourses.add(c.getCourseId());
            manager.getDbManager().saveUsers(); // Save instructor info
        }
        return c.getCourseId();
    }
    return null;
}

public boolean editCourse(CourseManager manager, String courseId, String newTitle, String newDescription) {
    Course c = manager.getCourse(courseId);
    if (!ownsCourse(c)) return false;

    boolean edited = manager.editCourse(courseId, newTitle, newDescription);
    if (edited) manager.getDbManager().saveUsers();
    return edited;
}

public boolean deleteCourse(CourseManager manager, String courseId) {
    Course c = manager.getCourse(courseId);
    if (!ownsCourse(c)) return false;

    boolean deleted = manager.deleteCourse(courseId);
    if (deleted) {
        createdCourses.remove(courseId);
        manager.getDbManager().saveUsers(); // Save updated instructor info
    }
    return deleted;
}

// -------------------- Lesson Methods --------------------
public String addLesson(LessonManager lessonManager, CourseManager courseManager,
                        String courseId, String title, String content) {

    Course course = courseManager.getCourse(courseId);
    if (!ownsCourse(course)) return null;

    Lesson lesson = lessonManager.addLesson(course, title, content);
    if (lesson != null) {
        if (!createdLessons.contains(lesson.getLessonId())) {
            createdLessons.add(lesson.getLessonId());
            courseManager.getDbManager().saveUsers(); // Save instructor info
        }
        courseManager.getDbManager().saveCourses(); // Save course with new lesson
        return lesson.getLessonId();
    }
    return null;
}

public boolean editLesson(LessonManager lessonManager, CourseManager courseManager,
                          String courseId, String lessonId,
                          String newTitle, String newContent) {

    Course course = courseManager.getCourse(courseId);
    if (!ownsCourse(course) || !ownsLesson(lessonId)) return false;

    boolean edited = lessonManager.editLesson(course, lessonId, newTitle, newContent);
    if (edited) courseManager.getDbManager().saveCourses();
    return edited;
}

public boolean deleteLesson(LessonManager lessonManager, CourseManager courseManager,
                            String courseId, String lessonId) {

    Course course = courseManager.getCourse(courseId);
    if (!ownsCourse(course)) return false;

    boolean deleted = lessonManager.deleteLesson(course, lessonId);
    if (deleted) {
        createdLessons.remove(lessonId);
        courseManager.getDbManager().saveUsers();   // Save instructor info
        courseManager.getDbManager().saveCourses(); // Save updated course info
    }
    return deleted;
}

// -------------------- Quiz Methods --------------------
public boolean addQuiz(LessonManager lessonManager, CourseManager courseManager,
                       String courseId, String lessonId, String quizTitle,
                       int passingScore, List<Question> questions) {

    Course course = courseManager.getCourse(courseId);
    if (!ownsCourse(course)) return false;

    Lesson lesson = lessonManager.getLesson(course, lessonId);
    if (lesson == null) return false;

    Quiz quiz = new Quiz(quizTitle, lesson.getLessonId(), passingScore, questions);
    lesson.setQuiz(quiz);

    if (!ownsLesson(lessonId)) {
        createdLessons.add(lessonId);
        courseManager.getDbManager().saveUsers(); // Save instructor info
    }

    courseManager.getDbManager().saveCourses();
    return true;
}

public Quiz getQuiz(LessonManager lessonManager, CourseManager courseManager,
                    String courseId, String lessonId) {
    Course course = courseManager.getCourse(courseId);
    if (!ownsCourse(course) || !ownsLesson(lessonId)) return null;
    return lessonManager.getQuizFromLesson(course, lessonId);
}

public boolean addQuestionToQuiz(CourseManager courseManager,
                                 LessonManager lessonManager,
                                 String courseId,
                                 String lessonId,
                                 String quizTitle,
                                 Question question) {

    Course course = courseManager.getCourse(courseId);
    if (!ownsCourse(course)) return false;

    Lesson lesson = lessonManager.getLesson(course, lessonId);
    if (lesson == null) return false;

    Quiz quiz = lesson.getQuiz();
    if (quiz == null || !quiz.getTitle().equals(quizTitle)) return false;

    quiz.getQuestions().add(question);
    courseManager.getDbManager().saveCourses();
    return true;
}

// -------------------- Analytics Methods --------------------
public Map<String, Double> calculateQuizAverages(Course course, List<Student> students) {
    Map<String, List<Integer>> scoresMap = new HashMap<>();
    for (Lesson lesson : course.getLessons()) {
        Quiz quiz = lesson.getQuiz();
        if (quiz != null) {
            scoresMap.put(quiz.getTitle(), new ArrayList<>());
        }
    }

    int courseId = parseNumericId(course.getCourseId());
    for (Student s : students) {
        if (!s.getEnrolledCourses().contains(courseId)) continue;

        for (Lesson lesson : course.getLessons()) {
            Quiz quiz = lesson.getQuiz();
            if (quiz != null) {
                Integer score = s.getQuizScore(courseId, quiz.getTitle());
                if (score != null) {
                    scoresMap.get(quiz.getTitle()).add(score);
                }
            }
        }
    }

    Map<String, Double> averages = new HashMap<>();
    for (String quizTitle : scoresMap.keySet()) {
        List<Integer> scores = scoresMap.get(quizTitle);
        double avg = scores.isEmpty() ? 0 : scores.stream().mapToInt(Integer::intValue).average().orElse(0);
        averages.put(quizTitle, avg);
    }
    return averages;
}

public Map<String, Double> calculateLessonCompletion(Course course, List<Student> students) {
    Map<String, Double> completionMap = new HashMap<>();
    int courseId = parseNumericId(course.getCourseId());

    for (Lesson lesson : course.getLessons()) {
        int completedCount = 0;
        int enrolledCount = 0;
        for (Student s : students) {
            if (s.getEnrolledCourses().contains(courseId)) {
                enrolledCount++;
                if (s.isLessonCompleted(courseId, lesson.getLessonId())) {
                    completedCount++;
                }
            }
        }
        double percentage = (enrolledCount == 0) ? 0 : (completedCount * 100.0 / enrolledCount);
        completionMap.put(lesson.getTitle(), percentage);
    }
    return completionMap;
}

public double calculateQuizAveragesForLesson(Course course, Lesson lesson, List<Student> students) {
    Quiz quiz = lesson.getQuiz();
    if (quiz == null) return 0;

    int courseId = parseNumericId(course.getCourseId());
    List<Integer> scores = new ArrayList<>();

    for (Student s : students) {
        if (!s.getEnrolledCourses().contains(courseId)) continue;
        Integer score = s.getQuizScore(courseId, quiz.getTitle());
        if (score != null) scores.add(score);
    }

    return scores.isEmpty() ? 0 : scores.stream().mapToInt(Integer::intValue).average().orElse(0);
}

public double calculateCourseCompletion(Course course, List<Student> students) {
    int totalLessons = course.getLessons().size();
    if (totalLessons == 0 || students.isEmpty()) return 0.0;

    int courseId = parseNumericId(course.getCourseId());
    double totalCompletion = 0.0;

    for (Student s : students) {
        if (!s.getEnrolledCourses().contains(courseId)) continue;

        int completedLessons = 0;
        for (Lesson lesson : course.getLessons()) {
            if (s.isLessonCompleted(courseId, lesson.getLessonId())) {
                completedLessons++;
            }
        }
        totalCompletion += (completedLessons * 100.0 / totalLessons);
    }
    return totalCompletion / students.size();
}

// -------------------- Getters --------------------
public ArrayList<String> getCreatedCourses() {
    return createdCourses;
}

public ArrayList<String> getCreatedLessons() {
    return createdLessons;
}

// -------------------- Helper --------------------
private int parseNumericId(String id) {
    try {
        return Integer.parseInt(id.replaceAll("\\D", ""));
    } catch (NumberFormatException e) {
        return -1;
    }
}

public ArrayList<Student> viewEnrolledStudents(CourseManager courseManager, String courseId) {

    ArrayList<Student> enrolledStudents = new ArrayList<>();

    // Get the course
    Course course = courseManager.getCourse(courseId);
    if (course == null) {
        return enrolledStudents; // empty list
    }

    // Loop through student IDs in the course
    for (String studentId : course.getStudents()) {

        // Find the student in the database
        User u = courseManager.getDbManager().getStudentById(studentId);

        if (u instanceof Student s) {
            enrolledStudents.add(s);
        }
    }

    return enrolledStudents;
}


}