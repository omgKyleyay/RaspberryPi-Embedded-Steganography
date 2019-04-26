/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Steganography {
    public String hostPath;
    public String guestPath;
    public static BufferedImage EncryptedImg;
    
    public BufferedImage decrypt(String hostPath, Dimension d) {   
//        public BufferedImage decrypt(BufferedImage host, Dimension d) { 
        BufferedImage decryptedImg = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
        BufferedImage host = null;
        LFSR.FreshLFSR();

        try
        {
          host = ImageIO.read(new File(hostPath));  
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        
        //hostX & hostY are the eImg pixel coordinates containing an encoded bit
        //value will hold the value of the LFSR
        int hostX, hostY, value;
        int hostWidth = host.getWidth(), hostHeight = host.getHeight();
        int hostSize = hostWidth * hostHeight - 1;

        //Arrays used to hold the bits of a particular sImg's pixel's colors
        int red, green, blue;      

        for(int x = 0; x < decryptedImg.getWidth(); x++)
        {
            for (int y = 0; y < decryptedImg.getHeight(); y++) 
            {  
                red = 0; green = 0; blue = 0;
                for (int offset = 0; offset < 8; offset++) 
                {
                    value = LFSR.getNextInt(hostSize);

                    
                    //Acquire corresponding x,y coord. from LFSR value
                    hostX = value % hostWidth; hostY = value / hostWidth;                        

                    Color decColor = new Color(host.getRGB(hostX, hostY));

                    red |= (decColor.getRed() & 0x01) << offset;
                    green |= (decColor.getGreen() & 0x01) << offset;
                    blue |= (decColor.getBlue() & 0x01) << offset;

                }
                decryptedImg.setRGB(x, y, new Color(red, green, blue).getRGB());
            }    
        }
        return decryptedImg;
    }
    public void saveBufferedImageToFile(BufferedImage bi, String fileName)
    {
        try 
        {
            ImageIO.write(bi, "png", new File(fileName));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    BufferedImage ImageCopy(BufferedImage image) //copies large image pixel by pixel
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < image.getWidth(); row++) {
            for (int column = 0; column < image.getHeight(); column ++) {
                int value = image.getRGB(row, column);
                newImage.setRGB(row, column, value);
            }
        } 
        return newImage;
    }
    public BufferedImage encrypt(String hostPath, String guestPath){    
        BufferedImage EncryptedImg = null;
        try {

            LFSR.FreshLFSR();
            
            BufferedImage host = ImageIO.read(new File(hostPath)); //Large imageIcon into a BufferedImage
            BufferedImage guest = ImageIO.read(new File(guestPath)); //Small imageIcon into a BufferedImage
            System.out.println("host dimensions: "+host.getWidth()+" "+host.getHeight());
            System.out.println("guest dimensions: "+guest.getWidth()+" "+guest.getHeight());
            EncryptedImg = ImageCopy(host); //makes a copy of the large image that can be primed for encryption

            int hostX, hostY; //x, y coordinate of host
            int value;
            int hostWidth = host.getWidth();
            int hostHeight = host.getHeight();
            int hostSize = hostWidth * hostHeight - 1;

            for(int x = 0; x < guest.getWidth(); x++) //runs through guest img rows
            {   
                for (int y = 0; y < guest.getHeight(); y++) { //runs through guest img columns
                    
                    Color guestColor = new Color(guest.getRGB(x, y)); //get RGB of guest img at x, y

                    for (int offset = 0; offset < 8; offset++)
                    {
                        value = LFSR.getNextInt(hostSize);

                        hostX = value % hostWidth; 
                        hostY = value / hostWidth;
                        
                        
                        Color hostColor = new Color(host.getRGB(hostX, hostY)); //retrieve host img pixel at x, y

                        int guestRed = (guestColor.getRed() >> offset) & 0x01; //acquires guest img least significant bits in RGB
                        int guestGreen = (guestColor.getGreen() >> offset) & 0x01;
                        int guestBlue = (guestColor.getBlue() >> offset) & 0x01;
                        

                        int hostRed = ((hostColor.getRed() & 0xFE) | guestRed); //aqcuires host img least significant bits in RGB
                        int hostGreen = ((hostColor.getGreen() & 0xFE) | guestGreen);
                        int hostBlue = ((hostColor.getBlue() & 0xFE) | guestBlue);
                         
                        EncryptedImg.setRGB(hostX, hostY, new Color(hostRed, hostGreen, hostBlue).getRGB()); //puts encrypted pixel into copied host image at x,y
                        
                    }    
                }
            }

        } catch (IOException ex) {
            System.out.println("Exception="+ex);
        }
        saveBufferedImageToFile(EncryptedImg, ".\\steg encrypted image\\steg.png");
        return EncryptedImg;
            
        
    }
}
