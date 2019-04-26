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
public class LFSR { //taken from C code and rewritten as java, with ricardo's help

    static int start_state = 0x4B891;
    static int lfsr = start_state;
    static int bit;
    static long period = 0;
    
    public LFSR(){}
    
    public static int getNextInt(int width, int height)
    {
        int area = (width * height) - 1;
        
        bit = ((lfsr >> 0) ^ (lfsr >> 17)) & 1;
        
        lfsr = (lfsr >> 1) | (bit << 19);
        
        if((lfsr > area) || (lfsr < 0))
            return -1;
        

        if(lfsr == start_state)
            return 0;
        
        return lfsr;
    }
    
    public static void FreshLFSR()
    {
        lfsr = start_state;
        period = 0;
    }
    
    
    
}
