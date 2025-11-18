/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
/**
 *
 * @author Dell
 */
public class AuthResult {
     private boolean success;
    private String message;
    private User user;

    public AuthResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.user=null;
    }
    public AuthResult(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user =user;
    }
    public boolean isSuccess()
    {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
