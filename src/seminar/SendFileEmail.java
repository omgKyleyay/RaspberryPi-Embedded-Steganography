/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminar;

/**
 *
 * @author Kyle
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class SendFileEmail extends Seminar{
    
     static SecretKey key;
    
     // Recipient's email ID needs to be mentioned.
      static String to = GUI.destinationEmail_text.getText();

      // Sender's email ID needs to be mentioned
      static String from = "nz.kylelothian@gmail.com";

      // Assuming you are sending email from localhost
      static String host = "localhost";

      // Get system properties
      static Properties props = System.getProperties();
      static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
      static String username = "nz.kylelothian@gmail.com";
      static String password = "";
       
      static EncryptedFileName eFileName = new EncryptedFileName(); //instances the name for the file to be encrypted and decrypted
      
      
      
   public static void SendEmail() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, InvalidKeyException, IOException{
      try {
          
            key = FileEncrypterDecrypter.generateKey(); //generates key
          
            // Setup mail server  
            Properties props = System.getProperties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.store.protocol", "pop3");
            props.put("mail.transport.protocol", "smtp");
            
        
            // Get the default Session object.
            Session session = Session.getDefaultInstance(props, 
                          new Authenticator(){
                             protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                             }});

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Create the message part 
            BodyPart messageBodyPart = new MimeBodyPart();
            BodyPart messageBodyPart2 = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText("This is message body");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            messageBodyPart2 = new MimeBodyPart();
            
            
            
            //this file will need to already exist, ideally after the lfsr has ran through it
            Encrypt(new File("6INxLAN.jpg"));  //file to be encrypted
            
//            String filename = "file.txt";

      
            String filename = eFileName.getEFileName(); //encrypted file to be emailed, name has already been set and encrypted by Encrypt method
            
            
            DataSource source = new FileDataSource(filename);            
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            
            
            
            DataSource AESKey = new FileDataSource("AESKey.txt");           
            messageBodyPart2.setDataHandler(new DataHandler(AESKey));
            messageBodyPart2.setFileName("your key");
            
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(messageBodyPart2);
            
            

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully.... also heres the key" + key + "\n here it is back in string" + key.toString() + 
                    "\n now here is the string back in bytes" + key.toString().getBytes());
            
        } catch (MessagingException mex) {
            mex.printStackTrace();
      }
   }
   
   private void SendImage(){
       
   }
   private BufferedImage stegIt() throws IOException{
       steg Stegging = new steg("imgSmall", "imgLarge"); //instance of steg class, sends small image and host image for steganography
       return Stegging.Steganography();
   }
   
   private static void Encrypt(File fileToBeEncrypted) throws InvalidKeyException{
       
       eFileName.askEFileName(); //sets name for output of encrypted file
       
       //creates an empty file primed for encryption        
       File encryptedFile = new File(eFileName.getEFileName());    
       
       //overwrites encryptedFile and actually encrypts it
       FileEncrypterDecrypter.fileProcessor(Cipher.ENCRYPT_MODE,key,fileToBeEncrypted,encryptedFile);    
   }
   
   private static void Decrypt(File fileToBeDecrypted) throws InvalidKeyException{
       eFileName.askDFileName(); //sets name for output of decrypted file
       
       File decryptedFile = new File(eFileName.getDFileName());  
       
       FileEncrypterDecrypter.fileProcessor(Cipher.DECRYPT_MODE,key,fileToBeDecrypted,decryptedFile);      
   }
   
   
}

