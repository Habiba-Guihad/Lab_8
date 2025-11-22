package lab_8;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abdallah
 */

public class Student extends User {
private ArrayList<Integer> enrolledCourses;
private HashMap<Integer, ArrayList<String>> progress;
private ArrayList<Certificate> certificates;

// ---------- ORIGINAL CONSTRUCTORS ----------
public Student(String userId, String username,
               String email, String passwordHash,
               ArrayList<Integer> enrolledCourses,
               HashMap<Integer, ArrayList<String>> progress,
               ArrayList<Certificate> certificates) { 
    super(userId,Role.STUDENT, username, email, passwordHash);
    this.enrolledCourses = (enrolledCourses != null) ? enrolledCourses : new ArrayList<>();
    this.progress = (progress != null) ? progress : new HashMap<>();
    this.certificates = (certificates != null) ? certificates : new ArrayList<>();
}

public Student(String userId, String username, String email, String passwordHash) {
    super(userId,Role.STUDENT, username, email, passwordHash);
    this.enrolledCourses = new ArrayList<>();
    this.progress = new HashMap<>();
    this.certificates = new ArrayList<>();
}

// ---------- ORIGINAL METHODS ----------
public ArrayList<Integer> getEnrolledCourses() { return enrolledCourses; }
public HashMap<Integer, ArrayList<String>> getProgress() { return progress; }
public ArrayList<Certificate> getCertificates() { return certificates; }

public void enrollInCourse(int courseId) {
    if(!enrolledCourses.contains(courseId)) {
        enrolledCourses.add(courseId);
        progress.put(courseId, new ArrayList<>());
    }
}

public void completeLesson(int courseId, String lessonId) {
    progress.putIfAbsent(courseId, new ArrayList<>());
    if(!progress.get(courseId).contains(lessonId)) {
        progress.get(courseId).add(lessonId);
    }
}

public boolean isLessonCompleted(int courseId, String lessonId) {
    return progress.containsKey(courseId) && progress.get(courseId).contains(lessonId);
}

public void awardCertificate(int courseId) {
    String certId = java.util.UUID.randomUUID().toString();
    String date = java.time.LocalDate.now().toString();
    String filePath = "certificates/" + getUserId() + "_C" + courseId + ".pdf";
    certificates.add(new Certificate(certId, this.getUserId(), courseId, date, filePath));
}

public boolean hasCompletedCourse(Course course) {
    int courseId = Integer.parseInt(course.getCourseId().replaceAll("\\D", ""));
    ArrayList<String> completedLessons = progress.get(courseId);
    if (completedLessons == null || completedLessons.size() < course.getLessons().size()) {
        return false;
    }
    return true;
}

// ---------- ADDED FIELDS ----------
// Track quiz scores per course
private HashMap<Integer, HashMap<String, Integer>> quizScores; // courseId -> (quizTitle -> score)

// ---------- ADDED CONSTRUCTOR ----------
public Student(String userId, String username,
               String email, String passwordHash,
               ArrayList<Integer> enrolledCourses,
               HashMap<Integer, ArrayList<String>> progress,
               ArrayList<Certificate> certificates,
               HashMap<Integer, HashMap<String, Integer>> quizScores) {
    super(userId, Role.STUDENT, username, email, passwordHash);
    this.enrolledCourses = (enrolledCourses != null) ? enrolledCourses : new ArrayList<>();
    this.progress = (progress != null) ? progress : new HashMap<>();
    this.certificates = (certificates != null) ? certificates : new ArrayList<>();
    this.quizScores = (quizScores != null) ? quizScores : new HashMap<>();
}

// ---------- ADDED METHODS ----------
// Add a quiz score for a specific quiz in a course
public void addQuizScore(int courseId, String quizTitle, int score) {
    if (quizScores == null) quizScores = new HashMap<>();
    quizScores.putIfAbsent(courseId, new HashMap<>());
    quizScores.get(courseId).put(quizTitle, score);
}

// Retrieve a quiz score for a specific quiz in a course
public Integer getQuizScore(int courseId, String quizTitle) {
    if (quizScores != null && quizScores.containsKey(courseId)) {
        return quizScores.get(courseId).get(quizTitle);
    }
    return null;
}

// Getter for quizScores map
public HashMap<Integer, HashMap<String, Integer>> getQuizScores() {
    return quizScores;
}

}
