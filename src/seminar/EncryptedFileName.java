/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminar;

import java.util.Scanner;

/**
 *
 * @author Kyle
 */
public class EncryptedFileName{
   private static String EfileName;
   private static String DfileName;

   public void askEFileName(){
       Scanner scanner = new Scanner (System.in);
       System.out.print("**WARNING** Name of encrypted file must Enter desired name of encrypted file: ");  
       EfileName = scanner.next();
   }
   
   public void askDFileName(){
       Scanner scanner = new Scanner (System.in);
       System.out.print("Enter desired name of decrypted file: ");  
       DfileName = scanner.next();
   }
   
   public String getEFileName(){
       return this.EfileName;
   }
   
   public String getDFileName(){
       return this.DfileName;
   }
}
