
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
    
    // constructor ashan loading from json files
    public Student(String userId, String username, String email, String passwordHash, ArrayList<Integer> enrolledCourses, HashMap<Integer, ArrayList<String>> progress)
    { 
        super(userId,Role.STUDENT, username, email, passwordHash);
        // to protect from null values
    this.enrolledCourses = (enrolledCourses != null) ? enrolledCourses : new ArrayList<>();
    this.progress = (progress != null) ? progress : new HashMap<>();

    }
    // constructor for new students wa2t signing up
     public Student(String userId, String username, String email, String passwordHash) {
        super(userId,Role.STUDENT, username, email, passwordHash);
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
    }
public ArrayList<Integer> getEnrolledCourses()
{ 
    return enrolledCourses;
}
public HashMap<Integer, ArrayList<String>> getProgress()
{
    return progress;
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
    
}
