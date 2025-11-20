/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;

import java.io.Serializable;

/**
 *
 * @author Abdallah
 */
public class Certificate implements Serializable {
    private String studentId;
    private String certificateId;
    private int courseId;
    private String dateIssued;
    private String filePath;
    
    public Certificate(String studentId, String certificateId, int courseId, String dateIssued, String filePath) {
        this.certificateId =  certificateId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.dateIssued = dateIssued;
        this.filePath = filePath;
    }
    
    public String getStudentId() 
    {
        return studentId;
    }
    
    public String getCertificateId()
    {
        return certificateId;
    }
    
    public int getCourseId()
    {
        return courseId;
    }
    
    public String getDateIssued()
    {
        return dateIssued;
    }
    
    public String getFilePath()
    {
        return filePath;
    }
}
