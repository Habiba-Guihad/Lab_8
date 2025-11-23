package lab_8;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
private HashMap<Integer, ArrayList<String>> progress; // for quick access
private ArrayList<Certificate> certificates;
private HashMap<Integer, HashMap<String, Integer>> quizScores; // latest scores
private StudentProgress progressData; // detailed progress and quiz attempts


private JsonDatabaseManager dbManager;
// Constructor for creating a brand-new student (no data yet)
public Student(String userId, String username, String email, String passwordHash) {
    super(userId, Role.STUDENT, username, email, passwordHash);

    this.enrolledCourses = new ArrayList<>();
    this.progress = new HashMap<>();
    this.certificates = new ArrayList<>();
    this.quizScores = new HashMap<>();
    this.dbManager = null; // will be set later when loading from DB
    this.progressData = new StudentProgress(userId);
}
// Constructor used for testing or when quizScores/dbManager are not provided
public Student(String userId, String username, String email, String passwordHash,
               ArrayList<Integer> enrolledCourses,
               HashMap<Integer, ArrayList<String>> progress,
               ArrayList<Certificate> certificates) {
    super(userId, Role.STUDENT, username, email, passwordHash);

    this.enrolledCourses = (enrolledCourses != null) ? enrolledCourses : new ArrayList<>();
    this.progress = (progress != null) ? progress : new HashMap<>();
    this.certificates = (certificates != null) ? certificates : new ArrayList<>();
    this.quizScores = new HashMap<>();   // default empty
    this.dbManager = null;               // no DB manager passed
    this.progressData = new StudentProgress(userId);

    // initialize progressData from progress
    for (Integer courseId : this.progress.keySet()) {
        for (String lessonId : this.progress.get(courseId)) {
            this.progressData.markLessonCompleted(courseId, lessonId);
        }
    }
}

// ---------- CONSTRUCTOR ----------
public Student(String userId, String username, String email, String passwordHash,
               ArrayList<Integer> enrolledCourses,
               HashMap<Integer, ArrayList<String>> progress,
               ArrayList<Certificate> certificates,
               HashMap<Integer, HashMap<String, Integer>> quizScores,
               JsonDatabaseManager dbManager) {
    super(userId, Role.STUDENT, username, email, passwordHash);
    this.enrolledCourses = (enrolledCourses != null) ? enrolledCourses : new ArrayList<>();
    this.progress = (progress != null) ? progress : new HashMap<>();
    this.certificates = (certificates != null) ? certificates : new ArrayList<>();
    this.quizScores = (quizScores != null) ? quizScores : new HashMap<>();
    this.dbManager = dbManager;
    this.progressData = new StudentProgress(userId);
    // initialize progressData from progress map
    for (Integer courseId : this.progress.keySet()) {
        for (String lessonId : this.progress.get(courseId)) {
            this.progressData.markLessonCompleted(courseId, lessonId);
        }
    }
    // initialize quiz attempts from quizScores (one attempt per stored score)
    for (Integer courseId : this.quizScores.keySet()) {
        for (String quizTitle : this.quizScores.get(courseId).keySet()) {
            int score = this.quizScores.get(courseId).get(quizTitle);
            Quiz dummyQuiz = new Quiz(quizTitle, "", 0, new ArrayList<>()); // placeholder
            this.progressData.addAttempt(new QuizAttempt(dummyQuiz, score));
        }
    }
}
public void setDbManager(JsonDatabaseManager dbManager) {
    this.dbManager = dbManager;
}

// ---------- ACCESSORS ----------
public ArrayList<Integer> getEnrolledCourses() { return enrolledCourses; }
public HashMap<Integer, ArrayList<String>> getProgress() { return progress; }
public ArrayList<Certificate> getCertificates() { return certificates; }
public HashMap<Integer, HashMap<String, Integer>> getQuizScores() { return quizScores; }
public StudentProgress getProgressData() { return progressData; }

// ---------- COURSE METHODS ----------
public void enrollInCourse(int courseId) {
    if(!enrolledCourses.contains(courseId)) {
        enrolledCourses.add(courseId);
        progress.put(courseId, new ArrayList<>());
        progressData.markLessonCompleted(courseId, ""); // ensure course exists in progressData
        if(dbManager != null) dbManager.saveUser(this);
    }
}

public void completeLesson(int courseId, String lessonId) {
    progress.putIfAbsent(courseId, new ArrayList<>());
    if(!progress.get(courseId).contains(lessonId)) {
        progress.get(courseId).add(lessonId);
        progressData.markLessonCompleted(courseId, lessonId);
        if(dbManager != null) dbManager.saveUser(this);
    }
}

public boolean isLessonCompleted(int courseId, String lessonId) {
    return progressData.isLessonCompleted(courseId, lessonId);
}

public void awardCertificate(int courseId) {
    String certId = java.util.UUID.randomUUID().toString();
    String date = java.time.LocalDate.now().toString();
    String filePath = "certificates/" + getUserId() + "_C" + courseId + ".pdf";
    certificates.add(new Certificate(certId, getUserId(), courseId, date, filePath));
    if(dbManager != null) dbManager.saveUser(this);
}

public boolean hasCompletedCourse(Course course) {
    int courseId = Integer.parseInt(course.getCourseId().replaceAll("\\D", ""));
    ArrayList<String> completedLessons = progress.get(courseId);
    return completedLessons != null && completedLessons.size() >= course.getLessons().size();
}
public boolean hasCertificateForCourse(int courseId) {   //halla added
    if (certificates == null) return false;
    for (Certificate c : certificates) {
        if (c.getCourseId() == courseId) return true;
    }
    return false;
}
public boolean hasCompletedEverything(Course course) {
    int courseIdInt = Integer.parseInt(course.getCourseId().replaceAll("\\D", ""));

    List<Lesson> lessons = course.getLessons();
    List<String> completedLessons = this.getProgress().get(courseIdInt);

    if (completedLessons == null) return false;

    // 1️⃣ All lessons must be marked completed
    for (Lesson lesson : lessons) {
        if (!completedLessons.contains(lesson.getLessonId()))
            return false;
    }

    // 2️⃣ All lesson quizzes must be passed
    for (Lesson lesson : lessons) {
        Quiz q = lesson.getQuiz();
        if (q == null) return false;

        Integer attemptScore = this.getQuizScore(courseIdInt, lesson.getLessonId());
        if (attemptScore == null) return false;
        if (!q.isPassed(attemptScore)) return false;
    }

    return true;
}
// ---------- QUIZ METHODS ----------
public void addQuizScore(int courseId, Quiz quiz, int score) {
    // update latest score
    quizScores.putIfAbsent(courseId, new HashMap<>());
    quizScores.get(courseId).put(quiz.getTitle(), score);
    // add attempt to progressData
    progressData.addAttempt(new QuizAttempt(quiz, score));
    if(dbManager != null) dbManager.saveUser(this);
}

public Integer getQuizScore(int courseId, String quizTitle) {
    if (quizScores.containsKey(courseId)) {
        return quizScores.get(courseId).get(quizTitle);
    }
    return null;
}

public double getAverageScoreForQuiz(Quiz quiz) {
    return progressData.getAverageScoreForQuiz(quiz);
}

public double getOverallAverageScore() {
    return progressData.getAverageScoreForAttempts();
}

}
