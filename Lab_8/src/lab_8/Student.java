package lab_8;
import java.util.ArrayList;
import java.util.HashMap;

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
    
    // constructor ashan loading from json files
    public Student(String userId, String username,
            String email, String passwordHash,
            ArrayList<Integer> enrolledCourses, HashMap<Integer,
            ArrayList<String>> progress, ArrayList<Certificate> certificates)
    { 
        super(userId,Role.STUDENT, username, email, passwordHash);
        // to protect from null values
    this.enrolledCourses = (enrolledCourses != null) ? enrolledCourses : new ArrayList<>();
    this.progress = (progress != null) ? progress : new HashMap<>();
    this.certificates = (certificates != null) ? certificates : new ArrayList <>();
    }
    // constructor for new students wa2t signing up
     public Student(String userId, String username, String email, String passwordHash) {
        super(userId,Role.STUDENT, username, email, passwordHash);
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.certificates = new ArrayList <>();
    }
public ArrayList<Integer> getEnrolledCourses()
{ 
    return enrolledCourses;
}
public HashMap<Integer, ArrayList<String>> getProgress()
{
    return progress;
}
 
public ArrayList<Certificate> getCertificates()
{
    return certificates;
}
public void enrollInCourse(int courseId)
{
    if(!enrolledCourses.contains(courseId))
    {
        enrolledCourses.add(courseId);
        progress.put(courseId, new ArrayList<>());
    }
}
public void completeLesson(int courseId, String lessonId) 
{
    progress.putIfAbsent(courseId, new ArrayList<>());
    if(!progress.get(courseId).contains(lessonId))
    {
        progress.get(courseId).add(lessonId);
    }
}
public boolean isLessonCompleted(int courseId, String lessonId)
{
    return progress.containsKey(courseId) && progress.get(courseId).contains(lessonId);
}

public void awardCertificate(int courseId) 
{
    String certId = java.util.UUID.randomUUID().toString();
    String date = java.time.LocalDate.now().toString();
    certificates.add(new Certificate(certId, this.getUserId(), courseId, date));
}

public boolean hasCompletedCourse(Course course) 
{
      int courseId = Integer.parseInt(course.getCourseId().replaceAll("\\D", ""));
      ArrayList<String> completedLessons = progress.get(courseId);
      if (completedLessons == null ||
        completedLessons.size() < course.getLessons().size()) {
        return false;
    }
      
      return true;
  
}
    
}
