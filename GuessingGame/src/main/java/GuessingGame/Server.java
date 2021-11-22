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
import java.util.ArrayList;
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
   //     Guess guessSession;
        Random rng = new Random();
     //   guessSession.setNumber(rng.nextInt(100) + 1);
        int userId = 1;
        ArrayList<Guess> sessionList = new ArrayList<Guess>();
        int cookie;// = 0;
        boolean hasCookie;
        Guess gSession = null;
                
	while(true){
   //         System.out.println("First line in while-loop. userId is " + userId);
            hasCookie= false;
            cookie = 0;
	    System.out.println("Waiting for client...");
	    Socket s = ss.accept();
	    System.out.println("Client connected");
            
            BufferedReader request =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
	    String str = request.readLine();
	    System.out.println(str);
           
                if (str.contains("POST")){
                    ispost = true;
   //                 System.out.println("--------------------testing first---------------------");
                    
                }
                
                
                
	    StringTokenizer tokens =
		new StringTokenizer(str," ?"); // " " and "?" are delimiters
	    tokens.nextToken(); // The word GET
	    String requestedDocument = tokens.nextToken();
	    while((str = request.readLine()) != null && str.length() > 0){
       //     for(int i = 0; i<30; i++){
         //       str = request.readLine();
		System.out.println(str);
                
                if(str.contains("Cookie")){
                    StringTokenizer cookieTkns = new StringTokenizer(str,"=");
                    System.out.println("### cookie found ###");
                    cookieTkns.nextToken(); //skip "userID=" part
                    String cookieStr = cookieTkns.nextToken();
                    cookie = Integer.parseInt(cookieStr);
                    hasCookie = true;
                }
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
            //     System.out.println("Guess in loop: " + guess);
                 if (i > 6){
                     sb.append(guess);
                 }
                 i++;     
            }
            String comparison = "";
            numstr=sb.toString();
        //    System.out.println("Guess " + guess);
         //   System.out.println("Numstr " + numstr);
            int userGuess = -1;
            if (ispost){
                userGuess = Integer.parseInt(numstr); // guess string
           //     System.out.println("Number " + userGuess);
            //guessSession.setUserGuess(number);
            //comparison = guessSession.compare();
         //       System.out.println("Comparison string " + comparison);
            }
            
        
            
           
            //System.out.println("New " + request.readLine());
            System.out.println("");
	    System.out.println("Request processed.");
	    s.shutdownInput();
            
            
            
            String result = "none";
            int numOfGuesses = 0;
            //Guess game = new Guess();
            
            
            if (sessionList.isEmpty()){
        //        System.out.println("session size = 0");
                Guess guessSession = new Guess(userId);
                sessionList.add(guessSession);
                //userId++;
            } else {
       //         System.out.println("Skipped session size = 0");
                //If-statement always true; PROBLEM
                // kollar vilken userID på cookien: om finns, hämtas dess session
                // om inte finns, skapas ny session
                
                //if(cookie != sessionList.get(sessionList.size()-1).userId){
                if(!hasCookie){
            //        System.out.println("Vafan " + cookie + " är");
            //        System.out.println("Vafan " + sessionList.get(sessionList.size()-1).userId + " är");
                    Guess guessSession = new Guess(userId);
                    sessionList.add(guessSession);
                      //userId++;
                    
                    //Den kan hämta cookien och kolla vilken användare det är
                    //Så om den får cookie 2, så ska den kolla igenom listan
                    //och kolla om användare 2 finns.
                }
                else{
                    
                    for(Guess sessions : sessionList){
                        if(cookie == sessions.userId && !("/favicon.ico".equals(requestedDocument))){
                   //         System.out.println("returning user");
                            sessions.runGame(userGuess);
                            result = sessions.getResult();
                            System.out.println("result: " + result);
                            numOfGuesses = sessions.getNumOfGuesses();
                            System.out.println("num of guesses: " + numOfGuesses);
                       //     userId = sessions.userId;
                            System.out.println("user ID: " + userId);
                            gSession = sessions;
  
                        }
                    }
                }
                    
                //Guess guessSession = new Guess(userId);
            
                //sessionList.add(guessSession);
            }
         
	    PrintStream response =
		new PrintStream(s.getOutputStream());
	    response.println("HTTP/1.1 200 OK");
	    response.println("Server: Trash 0.1 Beta");
	    if(requestedDocument.indexOf(".html") != -1)
		response.println("Content-Type: text/html");
	    if(requestedDocument.indexOf(".gif") != -1)
		response.println("Content-Type: image/gif");
	    
            if(!hasCookie){
                response.println("Set-Cookie: userId="+userId); //Remove date to make it a session-cookie
                System.out.println("Setting cookie. UserId is before " + userId);
                userId++;
                System.out.println("Setting cookie. UserId is after " + userId);
            }//     response.println("Set-Cookie: lastGuess=0");
            if (ispost){
       //      response.println("Set-Cookie: result=" + comparison);      
            }
            //  "; expires=Wednesday,31-Dec-21 21:00:00 GMT"
            
            System.out.println("Session List is " + sessionList.size());
             System.out.println("Session List string " + sessionList.toString());
            
             
             
            // here lied session processing etc
             
            
            response.println("Message: "+result);
            
            //TODO. Result sätts till "none" i början av while-loopen.
            //Men om man hämtar result från Guesslcass istället borde det kanske funka
            //Bara att man får tänka på ordningen, så att en guessclass
            //faktiskt har skapats när man anropar getResult
            //som kanske måste läggas till
            System.out.println("----------------------------------" + result);
            
            
            
            
	    response.println();
            if(!("/favicon.ico".equals(requestedDocument))){ // Ignore any additional request to retrieve the bookmark-icon.
                
                
                if((cookie == 0) && ((result.equals("newgame") || (result.equals("none"))))){
                    File f = new File("."+requestedDocument);
                    //System.out.println(f.getAbsolutePath()); //requestedDocument
                    //System.out.println("req doc: " + requestedDocument);
                    FileInputStream infil = new FileInputStream(f);
                    byte[] b = new byte[1024];
                    while( infil.available() > 0){
                        response.write(b,0,infil.read(b));
                    }
                }
                else if (result.equals("win")){
                    response.println("<html>");
                    response.println("<head><title>Guessing Game</title><meta charset=\"windows-1252\">"
                            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head>");
                    response.println("<body><div>You made it in " + numOfGuesses + " guesses. Press button to try again.</div>"
                            + "<form action=\"index.html\" method=\"POST\"><input name=\"btn\" type=\"submit\"></form></body>");
                    response.println("</html>");
                    gSession.resetGame();
                    System.out.println("Game is reset: " + gSession.getNumOfGuesses() + gSession.getResult());
                    
                }
                else {
                    response.println("<html>");
                    response.println("<head><title>Guessing Game</title><meta charset=\"windows-1252\">"
                            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head>");
                    response.println("<body><div>Wrong answer. Try " + result +  ". You have made " + numOfGuesses + " guesses. Press button to try again.</div>"
                            + "<div>What's your guess?<form action=\"index.html\" method=\"POST\">"
                            + "<input type=\"number\" min='1' max='100' value='5' name=\"number\"><input name=\"btn\" type=\"submit\">"
                            + "</form></body>");
                    response.println("</html>");
                  //  gSession.resetGame();
                }
                
                
            }
          
              ispost = false;
               s.shutdownOutput();
              s.close();
            }
        }
    }
    


