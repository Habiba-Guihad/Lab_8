/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
/**
 *
 * @author Dell
 */
public enum Role {
    STUDENT,INSTRUCTOR;
    public static Role fromString(String role) {
        if(role==null)
            return null;
        switch(role.toLowerCase()){
            case "student":
                return STUDENT;
            case "instructor": 
                return INSTRUCTOR;
            default: 
                return null;
        }
    }
}
