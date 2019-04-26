/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminar;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author Kyle
 */
public class steg {
    public static BufferedImage EncryptedImg, decryptedImg, smallImg, bigImg;
    
    public steg(String imgSmall, String imgLarge) throws IOException{
        smallImg = ImageIO.read(new File(imgSmall));
        bigImg = ImageIO.read(new File(imgLarge));
    }
    
    private BufferedImage UndoLFSR() {                                           
        /* Resets LFSR to initial state to ensure I output the same sequence of 
        ** numbers for encryption & decryption process
        */
        LFSR.FreshLFSR();
        
        //If there is no image to decrypt or no sImg or check is false
//        if(EncryptedImg == null || sImg == null || check)
//            //Display this error message
//            JOptionPane.showMessageDialog(null, "You must encrypt an image in "
//                    + "order to decrypt it or select a new guest image");   
//        else
//        {
            /*Stores a copy of sImg into dImg to ensure the decoded image is 
            **displayed correctly (not the mostly-black image)
            */
        decryptedImg = ImageCopy(smallImg);
        //ex & ey are the eImg pixel coordinates containing an encoded bit
        //value will hold the value of the LFSR
        int ex, ey, value;
        int bWidth = bigImg.getWidth(), bHeight = bigImg.getHeight();

        //Arrays used to hold the bits of a particular sImg's pixel's colors
        int[] red = new int[8]; int[] green = new int[8]; int[] blue = new int[8];      

        //Iterates through the sImg's rows
        for(int x = 0; x < smallImg.getWidth(); x++)
        {
            //Iterates through the sImg's columns
            for (int y = 0; y < smallImg.getHeight(); y++) 
            {  
                /*For loop used to iterate through 8 pixels of the eImg and 
                **acquire the encrypted lsb within each one
                */
                for (int offset = 0; offset < 8; offset++) 
                {
                    /*Only breaks out of this loop if the LFSR value is a 
                    **valid pixel location
                    */
                    do
                    {
                        value = LFSR.getNextInt(bWidth, bHeight);
                    } while(value == -999);

                    //Acquire corresponding x,y coord. from LFSR value
                    ex = value % bWidth; ey = value / bWidth;                        
                    //Extract color of the eImg's pixel at coord. (ex, ey)
                    Color encColor = new Color(EncryptedImg.getRGB(ex, ey));

                    /*Stores the encrypted bit from eImg's pixel to an array 
                    **for each of the colors with an index that corresponds 
                    **to what power of 2 it is
                    */
                    red[offset] = encColor.getRed() & 0x01;
                    green[offset] = encColor.getGreen() & 0x01;
                    blue[offset] = encColor.getBlue() & 0x01;

                }
                /*Sets the dImg's pixel at (x,y) to the pixel at the same 
                **coord. of the original guest image
                */
                decryptedImg.setRGB(x, y, new Color(decVal(red), decVal(green), decVal(blue)).getRGB());
            }    
        }

            //Changes the titles in the GUI

            //Display the encrypted and hidden images
            //check = true;
//        }

        return decryptedImg;
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
    
    public BufferedImage Steganography(){                                          
        LFSR.FreshLFSR();
        //            File FileBufferLarge = new File(pathLarge);
        //            File FileBufferSmall = new File(pathSmall);
        //            
        //            BufferedImage imgLarge = ImageIO.read(FileBufferLarge); //Large imageIcon into a BufferedImage
        //            BufferedImage imgSmall = ImageIO.read(FileBufferSmall); //Small imageIcon into a BufferedImage
        EncryptedImg = ImageCopy(bigImg); //makes a copy of the large image that can be primed for encryption
        int XofBig; //x value of large image
        int YofBig; //y value of large image
        int value;
        int BigWidth = bigImg.getWidth();
        int BigHeight = bigImg.getHeight();
        for(int x = 0; x < smallImg.getWidth(); x++) //runs through small img rows
        {

            for (int y = 0; y < smallImg.getHeight(); y++) { //runs through small img columns

                Color smallColor = new Color(smallImg.getRGB(x, y)); //get RGB of small img at x, y


                for (int offset = 0; offset < 8; offset++)
                {
                    do{
                        value = LFSR.getNextInt(BigWidth, BigHeight);
                    }
                    while(value == -999); // LFSR pixel value


                    XofBig = value % BigWidth;
                    YofBig = value / BigWidth;


                    Color bigColor = new Color(bigImg.getRGB(XofBig, YofBig)); //retrieve large img pixel at x, y


                    int SmallRed = (smallColor.getRed() >> offset) & 0x01; //acquires small img least significant bits in RGB
                    int SmallGreen = (smallColor.getGreen() >> offset) & 0x01;
                    int SmallBlue = (smallColor.getBlue() >> offset) & 0x01;


                    int BigRed = ((bigColor.getRed() & 0xFE) | SmallRed); //aqcuires large img least significant bits in RGB
                    int BigGreen = ((bigColor.getGreen() & 0xFE) | SmallGreen);
                    int BigBlue = ((bigColor.getBlue() & 0xFE) | SmallBlue);

                    EncryptedImg.setRGB(XofBig, YofBig, new Color(BigRed, BigGreen, BigBlue).getRGB()); //puts encrypted pixel into copied large image at x,y

                }
            }
        }
        //Displays EncryptedImg to label
        //LabelSmall.setIcon(new ImageIcon(EncryptedImg));
        return EncryptedImg;
                  
    }
    
    //Converts an array of 8 bits w/ degree in ascending order to decimal
    private int decVal(int[] color)
    {
        int sum = 0;
        for (int i = 0; i < color.length; i++) 
        {
            sum += (color[i] * (int)(Math.pow(2, i)));
        }
        
        return sum;
    }
}
