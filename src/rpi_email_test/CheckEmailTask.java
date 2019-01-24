/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpi_email_test;

import java.util.logging.Level;
import java.util.logging.Logger;
import rpio_client.Net_RPI_IO;

/**
 *
 * @author Federico
 */
public class CheckEmailTask {
    
    private boolean status = false;
    private boolean runFlag = true;
    

    public void startTask(){
        Thread task = new Thread(new CheckStatus(),"Check Email Task");
        task.start();
    }    
    public boolean getStatus(){
        return status;
    }
    
    public void killThread(){
        runFlag=false;
    }
    
    private class CheckStatus implements Runnable{
        
        Net_RPI_IO rpio = new Net_RPI_IO("localhost",30000);
        
        @Override
        public void run() {
            int counter=0;
            int value=0;
            String reply;
            
            while(runFlag){
                reply=rpio.getControlReg(10, 10);
                String[] parts = reply.split(",");
                if(parts.length==3){
                    
                    value=Integer.parseInt(parts[2]);
                    value = value & 128;
                    
                    if(value == 0){
                        counter++;
                    } else {
                        counter=0;
                    }
                    if(counter>=60){
                        status=true;
                    }else{
                        status=false;
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CheckEmailTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        }
        
    }
    
}
