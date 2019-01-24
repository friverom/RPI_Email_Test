/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpi_email_test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static java.lang.System.exit;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpio_client.Net_RPI_IO;

/**
 *
 * @author Federico
 */
public class RPI_Email_Test {
    
    static CheckEmailTask check = null;
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        
        new RPI_Email_Test().start();
        
    }
    
    public static void start() throws IOException, ClassNotFoundException{
        
        ServerSocket serversocket = new ServerSocket(30007);
        check = new CheckEmailTask();
        check.startTask();
        Socket socket = null;
        boolean status=false;
        boolean runFlag=true;
        
        while(true){
            socket = serversocket.accept();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            String request = (String) ois.readObject();
            
            switch(request){
                case "get status":
                    status = check.getStatus(); //False = OK True = Fail;
                    oos.writeObject(status);
                    break;
                    
                case "kill thread":
                    runFlag=false;
                    check.killThread();
            }
            oos.close();
            ois.close();
            socket.close();
            
        }
        
    }
    
    
    
        
}
