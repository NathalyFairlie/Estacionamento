/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Nathaly
 */
public class SenhaCripto {
     public static String senhaCripto(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(senha.getBytes());
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
    

