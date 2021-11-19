/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuessingGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 * @author Jacob Dwyer, Sara Bertse
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
	System.out.println("Creating Serversocket");
	ServerSocket ss = new ServerSocket(7070);
        boolean ispost = false;
        Guess guessSession = new Guess();
        Random rng = new Random();
        guessSession.setNumber(rng.nextInt(100) + 1);
                
	while(true){
	    System.out.println("Waiting for client...");
	    Socket s = ss.accept();
	    System.out.println("Client connected");
            
            BufferedReader request =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
	    String str = request.readLine();
	    System.out.println(str);
           
                if (str.contains("POST")){
                    ispost = true;
                    System.out.println("--------------------testing first---------------------");
                    
                }
	    StringTokenizer tokens =
		new StringTokenizer(str," ?"); // " " and "?" are delimiters
	    tokens.nextToken(); // The word GET
	    String requestedDocument = tokens.nextToken();
	    while((str = request.readLine()) != null && str.length() > 0){
       //     for(int i = 0; i<30; i++){
         //       str = request.readLine();
		System.out.println(str);
              }
            
            StringBuilder sb = new StringBuilder();
            int i = 0;
            char guess = 0;
            int c = 0;
            String numstr = "0";
            while (ispost && (c = request.read())!= 38){
               // System.out.println("New " + request.readLine());
                 System.out.print((char)c);
                 guess = (char)c;
                 System.out.println("Guess in loop: " + guess);
                 if (i > 6){
                     sb.append(guess);
                 }
                 i++;     
            }
            String comparison = "";
            numstr=sb.toString();
            System.out.println("Guess " + guess);
            System.out.println("Numstr " + numstr);
            if (ispost){
                int number = Integer.parseInt(numstr);
                System.out.println("Number " + number);
                guessSession.setUserGuess(number);
                comparison = guessSession.compare();
                System.out.println("Comparison string " + comparison);
            }
            
        
            
           
                //System.out.println("New " + request.readLine());
            System.out.println("");
	    System.out.println("Request processed.");
	    s.shutdownInput();
            
            
         //   Guess game = new Guess();
            
	    
	    PrintStream response =
		new PrintStream(s.getOutputStream());
	    response.println("HTTP/1.1 200 OK");
	    response.println("Server: Trash 0.1 Beta");
	    if(requestedDocument.indexOf(".html") != -1)
		response.println("Content-Type: text/html");
	    if(requestedDocument.indexOf(".gif") != -1)
		response.println("Content-Type: image/gif");
	    
            
	    response.println("Set-Cookie: numOfGuesses=0"); //Remove date to make it a session-cookie
            response.println("Set-Cookie: lastGuess=0");
            if (ispost){
             response.println("Set-Cookie: result=" + comparison);      
            }
            //  "; expires=Wednesday,31-Dec-21 21:00:00 GMT"
            
	    response.println();
            if(!("/favicon.ico".equals(requestedDocument))){ // Ignore any additional request to retrieve the bookmark-icon.
                File f = new File("."+requestedDocument);
                //System.out.println(f.getAbsolutePath()); //requestedDocument
                //System.out.println("req doc: " + requestedDocument);
                FileInputStream infil = new FileInputStream(f);
                byte[] b = new byte[1024];
                while( infil.available() > 0){
                    response.write(b,0,infil.read(b));
                }
            }
          
              ispost = false;
               s.shutdownOutput();
              s.close();
            }
        }
    }
    


