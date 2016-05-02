package siscafe.util;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.awt.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JLabel;

public class BasculeSerialPort {

    public BasculeSerialPort(JLabel display) {
        BasculeSerialPort.display = display;
    }
	
    public void connect () throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM1");
        if ( portIdentifier.isCurrentlyOwned() ){
            System.out.println("Error: Puerto serial del computador ocupados");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort ) {
                SerialPort serialPort = (SerialPort) commPort;
                System.out.println(serialPort);
                serialPort.setSerialPortParams(1200,SerialPort.DATABITS_7,SerialPort.STOPBITS_2,SerialPort.PARITY_EVEN);
                
                InputStream in = serialPort.getInputStream();
                (new Thread(new SerialReader(in))).start();

            } else {
                System.out.println("Error: No puede abrirse el puerto serial!");
            }
        }     
    }
    
    private static void capturingWeigh(InputStream in, BufferedReader reader) {
        String line = null;
        try{
           while ((line = reader.readLine()) != null) {
               getWieght(line);
           }
        }
        catch ( IOException e ) {
            System.out.println(e);
        }
    }
    
    private static void getWieght(String readLine) {
        char[] charLine = readLine.toCharArray();
        char isStable = charLine[2];
        String weigh = readLine.substring(5, 10);
        display.setText(weigh);
        if(isStable == '0') {
            display.setBackground(Color.GREEN);
        }
        else {
            display.setBackground(Color.RED);
        }
    }

    /** */
    public static class SerialReader implements Runnable {
        InputStream in;
        BufferedReader reader;
        
        public SerialReader ( InputStream in ) {
            this.reader = new BufferedReader(new InputStreamReader(in));
        }
        
        public void run () {
            capturingWeigh(this.in, this.reader);
        }
    }
        
    static JLabel display;
}

