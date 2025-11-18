/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
/**
 *
 * @author Dell
 */
public abstract class User {
    protected String userId;
    protected Role role;
    protected String username;
    protected String email;
    protected String passwordHash;

    public User(String userId, Role role, String userName, String email, String passwordHash) {
        this.userId = userId;
        this.role = role;
        this.username = userName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    
}

