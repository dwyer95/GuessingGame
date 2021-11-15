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
	ServerSocket ss = new ServerSocket(8080);
	while(true){
	    System.out.println("Waiting for client...");
	    Socket s = ss.accept();
	    System.out.println("Client connected");
	    BufferedReader request =
		new BufferedReader(new InputStreamReader(s.getInputStream()));
	    String str = request.readLine();
	    System.out.println(str);
	    StringTokenizer tokens =
		new StringTokenizer(str," ?"); // " " and "?" are delimiters
	    tokens.nextToken(); // The word GET
	    String requestedDocument = tokens.nextToken();
	    while( (str = request.readLine()) != null && str.length() > 0){
		System.out.println(str);
	    }
	    System.out.println("Request processed.");
	    s.shutdownInput();
            
            
            Guess game = new Guess();
            
	    
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
                s.shutdownOutput();
                s.close();
            }
        }
    }
    
}
