/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author Kyle
 */
public class FileEncrypterDecrypter {
    
//    static String SALT2 = GUI.aesKeyField.getText();
  
    static void fileProcessor(int cipherMode, SecretKey key, File inputFile, File outputFile) throws InvalidKeyException{
         try {
           Cipher cipher = Cipher.getInstance("AES");
           cipher.init(cipherMode, key);

           FileInputStream inputStream = new FileInputStream(inputFile);
           byte[] inputBytes = new byte[(int) inputFile.length()];
           inputStream.read(inputBytes);

           byte[] outputBytes = cipher.doFinal(inputBytes);

           FileOutputStream outputStream = new FileOutputStream(outputFile);
           outputStream.write(outputBytes);

           inputStream.close();
           outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException 
                | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }
    
    static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, FileNotFoundException, IOException{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        
        //writes key to txt file
        byte [] data = new byte[secretKey.getEncoded().length];
        FileOutputStream fos = new FileOutputStream("E:\\Netbeans_Projects\\Seminar\\AESKey.txt");
        fos.write(secretKey.getEncoded());
        
        return secretKey;
    }
    
    static void readKey() throws IOException{
        
        //reads key from txt file
        byte []keybyte = new byte[16];
        FileInputStream fin = new FileInputStream("E:\\Netbeans_Projects\\Seminar\\AESKey.txt");
        fin.read(keybyte);
        SecretKey skey = new SecretKeySpec(keybyte, 0, 16, "AES");
    }
    
    
    public static void main(String[] args) {
//	String key = "This is a secret";
//	File inputFile = new File("text.txt");
//	File encryptedFile = new File("text.encrypted");
//	File decryptedFile = new File("decrypted-text.txt");
//		
//	try {
//            FileEncrypterDecrypter.fileProcessor(Cipher.ENCRYPT_MODE,key,inputFile,encryptedFile);
//            FileEncrypterDecrypter.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
//            System.out.println("Sucess");
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
    }
}
