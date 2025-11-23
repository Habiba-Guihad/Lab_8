package lab_8;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abdallah
 */

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.*;

public class JsonDatabaseManager {
private static final String USERS_FILE = "users.json";
private static final String COURSES_FILE = "courses.json";

private ArrayList<User> users = new ArrayList<>();
private ArrayList<Course> courses = new ArrayList<>();

public JsonDatabaseManager() {
    loadUsers();
    loadCourses();
}

private String readFile(String filename) {
    try {
        File f = new File(filename);
        if (!f.exists()) return "[]";

        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        return sb.toString();
    } catch (Exception e) {
        e.printStackTrace();
        return "[]";
    }
}

private void writeFile(String filename, String data) {
    try {
        FileWriter fw = new FileWriter(filename);
        fw.write(data);
        fw.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// ---------------- Users ----------------
public void loadUsers() {
    users.clear();
    JSONArray arr = new JSONArray(readFile(USERS_FILE));

    for (int i = 0; i < arr.length(); i++) {
        JSONObject obj = arr.getJSONObject(i);
        String userId = obj.getString("userId");
        String role = obj.getString("role");
        String username = obj.getString("username");
        String email = obj.getString("email");
        String passwordHash = obj.getString("passwordHash");

        if (role.equalsIgnoreCase("student")) {
            // Enrolled courses
            ArrayList<Integer> enrolled = new ArrayList<>();
            JSONArray enrolledArr = obj.optJSONArray("enrolledCourses");
            if (enrolledArr != null) {
                for (int j = 0; j < enrolledArr.length(); j++) enrolled.add(enrolledArr.getInt(j));
            }

            // Progress map
            HashMap<Integer, ArrayList<String>> progress = new HashMap<>();
            JSONObject progObj = obj.optJSONObject("progress");
            if (progObj != null) {
                for (String courseId : progObj.keySet()) {
                    JSONArray lessonArr = progObj.getJSONArray(courseId);
                    ArrayList<String> lessons = new ArrayList<>();
                    for (int k = 0; k < lessonArr.length(); k++) lessons.add(lessonArr.getString(k));
                    progress.put(Integer.parseInt(courseId), lessons);
                }
            }

            // Certificates
            ArrayList<Certificate> certificates = new ArrayList<>();
            JSONArray certArr = obj.optJSONArray("certificates");
            if (certArr != null) {
                for (int k = 0; k < certArr.length(); k++) {
                    JSONObject co = certArr.getJSONObject(k);
                    certificates.add(new Certificate(
                            co.getString("certificateId"),
                            userId,
                            co.getInt("courseId"),
                            co.getString("dateIssued"),
                            co.optString("filePath", "")
                    ));
                }
            }

            // Quiz scores (latest scores)
            HashMap<Integer, HashMap<String, Integer>> quizScores = new HashMap<>();
            JSONObject qsObj = obj.optJSONObject("quizScores");
            if (qsObj != null) {
                for (String courseId : qsObj.keySet()) {
                    JSONObject courseScoresObj = qsObj.getJSONObject(courseId);
                    HashMap<String, Integer> courseScores = new HashMap<>();
                    for (String quizTitle : courseScoresObj.keySet()) {
                        courseScores.put(quizTitle, courseScoresObj.getInt(quizTitle));
                    }
                    quizScores.put(Integer.parseInt(courseId), courseScores);
                }
            }

            // Construct Student (pass this db manager so student can auto-save)
            Student student = new Student(userId, username, email, passwordHash,
                    enrolled, progress, certificates, quizScores, this);

            // Load progressData (detailed completed lessons & attempts) if present
            JSONObject pdObj = obj.optJSONObject("progressData");
            if (pdObj != null) {
                loadStudentProgress(student, pdObj);
            }

            users.add(student);

        } else if (role.equalsIgnoreCase("instructor")) {
            ArrayList<String> createdCourses = new ArrayList<>();
            JSONArray createdCoursesArr = obj.optJSONArray("createdCourses");
            if (createdCoursesArr != null) {
                for (int j = 0; j < createdCoursesArr.length(); j++)
                    createdCourses.add(createdCoursesArr.getString(j));
            }

            ArrayList<String> createdLessons = new ArrayList<>();
            JSONArray createdLessonsArr = obj.optJSONArray("createdLessons");
            if (createdLessonsArr != null) {
                for (int j = 0; j < createdLessonsArr.length(); j++)
                    createdLessons.add(createdLessonsArr.getString(j));
            }

            Instructor ins = new Instructor(userId, username, email, passwordHash, createdCourses);
            for (String lessonId : createdLessons) ins.getCreatedLessons().add(lessonId);

            users.add(ins);

        } else if (role.equalsIgnoreCase("admin")) {
            users.add(new Admin(userId, username, email, passwordHash));
        }
    }
}

public void saveUsers() {
    JSONArray arr = new JSONArray();
    for (User u : users) {
        JSONObject obj = new JSONObject();
        obj.put("userId", u.getUserId());
        obj.put("role", u.getRole().toString().toLowerCase());
        obj.put("username", u.getUsername());
        obj.put("email", u.getEmail());
        obj.put("passwordHash", u.getPasswordHash());

        if (u instanceof Student s) {
            obj.put("enrolledCourses", new JSONArray(s.getEnrolledCourses()));

            JSONObject progObj = new JSONObject();
            for (int courseId : s.getProgress().keySet()) {
                progObj.put(String.valueOf(courseId), new JSONArray(s.getProgress().get(courseId)));
            }
            obj.put("progress", progObj);

            JSONArray certArr = new JSONArray();
            for (Certificate c : s.getCertificates()) {
                JSONObject co = new JSONObject();
                co.put("certificateId", c.getCertificateId());
                co.put("courseId", c.getCourseId());
                co.put("dateIssued", c.getDateIssued());
                co.put("filePath", c.getFilePath());
                certArr.put(co);
            }
            obj.put("certificates", certArr);

            JSONObject qsObj = new JSONObject();
            HashMap<Integer, HashMap<String, Integer>> quizScores = s.getQuizScores();
            if (quizScores != null) {
                for (int courseId : quizScores.keySet()) {
                    JSONObject courseScoresObj = new JSONObject();
                    for (String quizTitle : quizScores.get(courseId).keySet()) {
                        courseScoresObj.put(quizTitle, quizScores.get(courseId).get(quizTitle));
                    }
                    qsObj.put(String.valueOf(courseId), courseScoresObj);
                }
            }
            obj.put("quizScores", qsObj);

            JSONObject pdObj = studentProgressToJson(s.getProgressData());
            obj.put("progressData", pdObj);
        }

        if (u instanceof Instructor ins) {
            JSONArray createdCoursesArr = new JSONArray(ins.getCreatedCourses());
            obj.put("createdCourses", createdCoursesArr);

            JSONArray createdLessonsArr = new JSONArray(ins.getCreatedLessons());
            obj.put("createdLessons", createdLessonsArr);
        }

        arr.put(obj);
    }
    writeFile(USERS_FILE, arr.toString(4));
}

public void saveUser(User u) {
    boolean found = false;
    for (int i = 0; i < users.size(); i++) {
        if (users.get(i).getUserId().equals(u.getUserId())) {
            users.set(i, u);
            found = true;
            break;
        }
    }
    if (!found) users.add(u);
    saveUsers();
}

private JSONObject studentProgressToJson(StudentProgress progressData) {
    JSONObject json = new JSONObject();

    JSONObject lessonsJson = new JSONObject();
    Map<Integer, List<String>> completed = progressData.getProgress();
    for (Integer courseId : completed.keySet()) {
        lessonsJson.put(String.valueOf(courseId), new JSONArray(completed.get(courseId)));
    }
    json.put("completedLessons", lessonsJson);

    JSONArray attemptsArr = new JSONArray();
    List<QuizAttempt> attempts = progressData.getAttempts();
    for (QuizAttempt attempt : attempts) {
        JSONObject a = new JSONObject();
        a.put("quizTitle", attempt.getQuiz().getTitle());
        a.put("score", attempt.getScore());
        attemptsArr.put(a);
    }
    json.put("quizAttempts", attemptsArr);

    return json;
}

private void loadStudentProgress(Student student, JSONObject json) {
    StudentProgress pd = student.getProgressData();

    JSONObject lessonsJson = json.optJSONObject("completedLessons");
    if (lessonsJson != null) {
        for (String courseIdStr : lessonsJson.keySet()) {
            int courseId = Integer.parseInt(courseIdStr);
            JSONArray arr = lessonsJson.getJSONArray(courseIdStr);
            for (int i = 0; i < arr.length(); i++) {
                String lessonId = arr.getString(i);
                if (lessonId != null && !lessonId.isEmpty()) {
                    pd.markLessonCompleted(courseId, lessonId);
                }
            }
        }
    }

    JSONArray attemptsArr = json.optJSONArray("quizAttempts");
    if (attemptsArr != null) {
        for (int i = 0; i < attemptsArr.length(); i++) {
            JSONObject a = attemptsArr.getJSONObject(i);
            String quizTitle = a.getString("quizTitle");
            int score = a.getInt("score");
            Quiz dummyQuiz = new Quiz(quizTitle, "", 0, new ArrayList<>());
            pd.addAttempt(new QuizAttempt(dummyQuiz, score));
        }
    }
}

// ---------------- Courses ----------------
private void loadCourses() {
    courses.clear();
    JSONArray arr = new JSONArray(readFile(COURSES_FILE));

    for (int i = 0; i < arr.length(); i++) {
        JSONObject o = arr.getJSONObject(i);
        String courseId = o.getString("courseId");
        String title = o.getString("title");
        String description = o.getString("description");
        String instructorId = o.getString("instructorId");

        Course c = new Course(courseId, title, description, instructorId);

        if (o.has("approvalStatus"))
            c.setApprovalStatus(Course.ApprovalStatus.valueOf(o.getString("approvalStatus")));

        JSONArray studentsArr = o.optJSONArray("students");
        if (studentsArr != null) {
            for (int s = 0; s < studentsArr.length(); s++) c.getStudents().add(studentsArr.getString(s));
        }

        JSONArray lessonsArr = o.optJSONArray("lessons");
        if (lessonsArr != null) {
            for (int l = 0; l < lessonsArr.length(); l++) {
                JSONObject lo = lessonsArr.getJSONObject(l);
                Lesson lesson = new Lesson(lo.getString("lessonId"), lo.getString("title"), lo.getString("content"));

                if (lo.has("quiz")) {
                    JSONObject qObj = lo.getJSONObject("quiz");
                    List<Question> questions = new ArrayList<>();
                    JSONArray qArr = qObj.getJSONArray("questions");
                    for (int qi = 0; qi < qArr.length(); qi++) {
                        JSONObject qo = qArr.getJSONObject(qi);
                        JSONArray optionsArr = qo.getJSONArray("options");
                        List<String> options = new ArrayList<>();
                        for (int oi = 0; oi < optionsArr.length(); oi++) options.add(optionsArr.getString(oi));
                        questions.add(new Question(qo.getString("questionText"), options, qo.getInt("correctIndex")));
                    }
                    lesson.setQuiz(new Quiz(qObj.getString("title"), lesson.getLessonId(), qObj.getInt("passingScore"), questions));
                }

                c.getLessons().add(lesson);
            }
        }

        courses.add(c);
    }
}

public void saveCourses() {
    JSONArray arr = new JSONArray();

    for (Course c : courses) {
        JSONObject o = new JSONObject();
        o.put("courseId", c.getCourseId());
        o.put("title", c.getTitle());
        o.put("description", c.getDescription());
        o.put("instructorId", c.getInstructorId());
        o.put("approvalStatus", c.getApprovalStatus().toString());
        o.put("students", new JSONArray(c.getStudents()));

        JSONArray lessonArr = new JSONArray();
        for (Lesson l : c.getLessons()) {
            JSONObject lo = new JSONObject();
            lo.put("lessonId", l.getLessonId());
            lo.put("title", l.getTitle());
            lo.put("content", l.getContent());

            if (l.getQuiz() != null) {
                JSONObject qObj = new JSONObject();
                qObj.put("title", l.getQuiz().getTitle());
                qObj.put("passingScore", l.getQuiz().getPassingScore());
                JSONArray questionsArr = new JSONArray();
                for (Question q : l.getQuiz().getQuestions()) {
                    JSONObject qo = new JSONObject();
                    qo.put("questionText", q.getQuestionText());
                    qo.put("options", new JSONArray(q.getOptions()));
                    qo.put("correctIndex", q.getCorrectIndex());
                    questionsArr.put(qo);
                }
                qObj.put("questions", questionsArr);
                lo.put("quiz", qObj);
            }

            lessonArr.put(lo);
        }

        o.put("lessons", lessonArr);
        arr.put(o);
    }

    writeFile(COURSES_FILE, arr.toString(4));
}

public void updateCourse(Course updatedCourse) {
    List<Course> courses = getCourses();
    for (int i = 0; i < courses.size(); i++) {
        if (courses.get(i).getCourseId().equals(updatedCourse.getCourseId())) {
            courses.set(i, updatedCourse);
            break;
        }
    }
    saveCourses();
}

// ---------------- Other Methods (unchanged) ----------------
public ArrayList<User> getUsers() {
    return users;
}

public ArrayList<Course> getCourses() {
    return courses;
}

public void addUser(User u) {
    users.add(u);
    saveUsers();
}

public void addCourse(Course c) {
    courses.add(c);
    saveCourses();
}

public User findUserByEmail(String email) {
    for (User u : users) if (u.getEmail().equalsIgnoreCase(email)) return u;
    return null;
}

public Student getStudentById(String userId) {
    for (User u : users) {
        if (u.getUserId().equals(userId) && u instanceof Student s) {
            return s;
        }
    }
    return null;
}

public void generateCertificatePDF(Student student, Course course, Certificate cert) {
    try {
        File dir = new File("certificates");
        if (!dir.exists()) dir.mkdirs();
        String filePath = cert.getFilePath();
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(
                com.lowagie.text.Font.HELVETICA, 26, com.lowagie.text.Font.BOLD
        );
        com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("Certificate of Completion\n\n", titleFont);
        title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        document.add(title);

        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16);
        document.add(new com.lowagie.text.Paragraph("This is to certify that:\n" + student.getUsername() + "\n\n", normalFont));
        document.add(new com.lowagie.text.Paragraph("has successfully completed the course:\n" + course.getTitle() + "\n\n", normalFont));
        document.add(new com.lowagie.text.Paragraph("Date Issued: " + cert.getDateIssued() + "\n\n", normalFont));

        com.lowagie.text.Paragraph footer = new com.lowagie.text.Paragraph(
                "\n\nGenerated by Learning Management System",
                new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12)
        );
        footer.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error generating certificate PDF: " + e.getMessage());
    }
}

}
