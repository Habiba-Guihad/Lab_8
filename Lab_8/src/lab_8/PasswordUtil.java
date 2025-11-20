/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
import java.security.MessageDigest;//class for hashing
/**
 *
 * @author Dell
 */
public class PasswordUtil {
    public static String hash(String password)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes()); //ye7awel password mn string le byte w yeraga3 array mn bytes
            StringBuilder sb = new StringBuilder(); 
            for (byte b : hashed) sb.append(String.format("%02x", b));//ye7awel kol byte le rakameen hexadecimal
            return sb.toString();//return hash as string 3shan jason msh byfham el bytes 
        
        }
        catch (Exception e) { 
            return null;
        }
    }

}
