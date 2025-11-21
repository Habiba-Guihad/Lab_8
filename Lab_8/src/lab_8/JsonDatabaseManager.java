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

    // constructor loads everything 
    public JsonDatabaseManager() {
        loadUsers();
        loadCourses();
    }
    
    // read json files w bet return as a string 
    private String readFile(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists()) {
                return "[]";
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }
     //write string to json 
    private void writeFile(String filename, String data) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(data);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        users.clear();

        JSONArray arr = new JSONArray(readFile(USERS_FILE));

        for (int i = 0; i < arr.length(); i++) {

            JSONObject obj = arr.getJSONObject(i);

            String userId = obj.getString("userId");
            String role = obj.getString("role");
            String username = obj.getString("username");
            String email = obj.getString("email");
            String passwordHash = obj.getString("passwordHash");

            if (role.equals("student")) {

                ArrayList<Integer> enrolled = new ArrayList<>();
                JSONArray enrolledArr = obj.getJSONArray("enrolledCourses");
                for (int j = 0; j < enrolledArr.length(); j++) {
                    enrolled.add(enrolledArr.getInt(j));
                }

                HashMap<Integer, ArrayList<String>> progress = new HashMap<>();
                JSONObject progObj = obj.getJSONObject("progress");

                for (String courseId : progObj.keySet()) {
                    JSONArray lessonArr = progObj.getJSONArray(courseId);

                    ArrayList<String> lessons = new ArrayList<>();
                    for (int k = 0; k < lessonArr.length(); k++) {
                        lessons.add(lessonArr.getString(k));
                    }

                    progress.put(Integer.parseInt(courseId), lessons);
                }

                ArrayList<Certificate> certificates = new ArrayList<>();
                JSONArray certArr = obj.optJSONArray("certificates");

                if (certArr != null) {
                    for (int k = 0; k < certArr.length(); k++) {
                        JSONObject co = certArr.getJSONObject(k);

                        Certificate c = new Certificate(
                                co.getString("certificateId"),
                                userId,
                                co.getInt("courseId"),
                                co.getString("dateIssued"),
                                co.optString("filePath", "")
                        );
                        certificates.add(c);
                    }
                }

                users.add(new Student(userId, username, email, passwordHash, enrolled, progress, certificates));
            } 
            else if (role.equals("instructor")) {

                ArrayList<String> created = new ArrayList<>();
                JSONArray createdArr = obj.getJSONArray("createdCourses");
                for (int j = 0; j < createdArr.length(); j++) {
                    created.add(createdArr.getString(j));
                }

                users.add(new Instructor(userId, username, email, passwordHash, created));
            }
            
            else if (role.equals("admin")) {
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
                    progObj.put(String.valueOf(courseId),
                            new JSONArray(s.getProgress().get(courseId)));
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
            }

            if (u instanceof Instructor ins) {
                obj.put("createdCourses", new JSONArray(ins.getCreatedCourses()));
            }

            arr.put(obj);
        }

        writeFile(USERS_FILE, arr.toString(4));
    }
   public void saveUser(User u) {
    boolean found = false;
    for (int i = 0; i < users.size(); i++) {
        if (users.get(i).getUserId().equals(u.getUserId())) {
            users.set(i, u);  // Replace existing user object
            found = true;
            break;
        }
    }
    if (!found) {
        users.add(u);  // New user
    }
    saveUsers();
}
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

            if (o.has("approvalStatus")) {
                c.setApprovalStatus(
                        Course.ApprovalStatus.valueOf(o.getString("approvalStatus"))
                );
            }

            JSONArray studentsArr = o.getJSONArray("students");
            for (int s = 0; s < studentsArr.length(); s++) {
                c.getStudents().add(studentsArr.getString(s));
            }

            JSONArray lessonsArr = o.getJSONArray("lessons");
            for (int l = 0; l < lessonsArr.length(); l++) {
                JSONObject lo = lessonsArr.getJSONObject(l);

                Lesson lesson = new Lesson(
                        lo.getString("lessonId"),
                        lo.getString("title"),
                        lo.getString("content")
                );

                c.getLessons().add(lesson);
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

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public int generateUserId() {
        int max = 0;

        for (User u : users) {
            try {
                // convert string userId to int safely
                int id = Integer.parseInt(String.valueOf(u.getUserId()));

                if (id > max) {
                    max = id;
                }
            } catch (Exception e) {
                // if something goes wrong, skip this ID
                System.out.println("Invalid userId format: " + u.getUserId());
            }
        }

        return max + 1;
    }

    public int generateCourseId() {
        int max = 0;
        for (Course c : courses) {
            try {
                int id = Integer.parseInt(c.getCourseId().replaceAll("\\D", ""));
                if (id > max)
                    max = id;
            } catch (Exception e) {}
        }
        return max + 1;
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
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
    public void generateCertificatePDF(Student student, Course course, Certificate cert) {

    try {
        // Create certificates folder if missing
        File dir = new File("certificates");
        if (!dir.exists()) dir.mkdirs();

        String filePath = cert.getFilePath(); 

        com.lowagie.text.Document document = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        // Title
        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(
                com.lowagie.text.Font.HELVETICA, 26, com.lowagie.text.Font.BOLD
        );
        com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("Certificate of Completion\n\n", titleFont);
        title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        document.add(title);

        // Student
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(
                com.lowagie.text.Font.HELVETICA, 16
        );
        document.add(new com.lowagie.text.Paragraph(
                "This is to certify that:\n" +
                student.getUsername() + "\n\n",
                normalFont));

        // Course info
        document.add(new com.lowagie.text.Paragraph(
                "has successfully completed the course:\n" +
                course.getTitle() + "\n\n",
                normalFont));

        // Date
        document.add(new com.lowagie.text.Paragraph(
                "Date Issued: " + cert.getDateIssued() + "\n\n",
                normalFont));

        
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

