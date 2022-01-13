package GuessingGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author Jacob Dwyer, Sara Bertse
 */
public class Server {
    public static void main(String[] args) throws IOException{
                    SSLServerSocket ss = null;
           SSLServerSocketFactory ssf; // = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();

        try {
            KeyStore ks = null;
            ks = KeyStore.getInstance("JKS", "SUN");
            //ks = KeyStore.getInstance("jks");
            InputStream is = null;
            //is = new FileInputStream(new File("C:/Program Files/Java/jdk1.8.0_271/jre/lib/security/cacerts"));
            // "C:/Users/sarab/cert.pfx"
            is = new FileInputStream(new File("C:/Users/sarab/cert.pfx"));
            char[] pwd = "qwerty".toCharArray();
            ks.load(is, pwd);

            SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            System.out.println(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, pwd);
            ctx.init(kmf.getKeyManagers(), null, null);
            //ctx.init(null, null, null);
            ssf = ctx.getServerSocketFactory();

            System.out.println("Supported:");
          //     for(int i = 0; i < ssf.getSupportedCipherSuites().length; i++)
          //        System.out.println(ssf.getSupportedCipherSuites()[i]);
            
            ss = (SSLServerSocket) ssf.createServerSocket(443);
            //String[] cipher = {"SSL_DH_anon_WITH_RC4_128_MD5"};
           // String[] cipher = {"TLS_RSA_WITH_AES_128_CBC_SHA"};
           String[] cipher = {"TLS_AES_256_GCM_SHA384"};
            ss.setEnabledCipherSuites(cipher);
            //System.out.println("Choosen:");
             for(int i = 0; i < ss.getEnabledCipherSuites().length; i++)
                 System.out.println(ss.getEnabledCipherSuites()[i]);
         //   SSLSocket socket = (SSLSocket) ss.accept();
           // BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           // reader.close();
      
            //!!!!!!!!!!!!!!!!! Enable different cipher suites and see if it works then!!!!!!!!!!!
            
            String row = null;
            /* System.out.println("----------received HTTPS request----------");
            while( ((row=reader.readLine()) != null) || (row=="\\r\\n"))  {
                System.out.println(row);
                System.out.println("stuck spinning");
                if(!(row.trim().equals(""))){ 
                    break;
                }
            }*/

            

            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
	System.out.println("Creating Serversocket");
	//ServerSocket ss = new ServerSocket(7070);
        boolean ispost = false;
        int userId = 1;
        ArrayList<Guess> sessionList = new ArrayList<Guess>();
        int cookie;
        boolean hasCookie;
        Guess gSession = null;

                
	while(true){
           // ss = (SSLServerSocket) ssf.createServerSocket(7070);
            hasCookie= false;
            cookie = 0;
	    System.out.println("--------------------------Waiting for client-----------------------------------");
	    SSLSocket socket = (SSLSocket)ss.accept();
	    System.out.println("Client connected");            
                  
            BufferedReader request =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("buffered reader created");
            
             
	    String str = request.readLine();
            System.out.println("request.readline working");
           
            /*StringBuilder sb1 = new StringBuilder();
            while( (row=request.readLine()) != null) {
                System.out.println(row);
                sb1.append(row);
            }
            request.close();
            String str = sb1.toString();*/
            
         //   request.close();
	    System.out.println(str);
           
            if (str.contains("POST")) { ispost = true; }
                
	    StringTokenizer tokens =
		new StringTokenizer(str," ?"); // " " and "?" are delimiters
	    tokens.nextToken(); // The word GET
	    String requestedDocument = tokens.nextToken();
	    while((str = request.readLine()) != null && str.length() > 0){
                //Toggle next bit on to see the GET/POST
		System.out.println(str);
                
                if(str.contains("Cookie")){
                    StringTokenizer cookieTkns = new StringTokenizer(str,"=");
                    cookieTkns.nextToken(); // skip "userID=" part
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
            while (ispost && (c = request.read()) != 38) {
                // Toggle on next line to print guessed number in body
                System.out.print((char) c);
                guess = (char) c;
                if (i > 6) { sb.append(guess); }
                i++;
            }
            numstr=sb.toString();
            
            int userGuess = -1;
            if (ispost){ userGuess = Integer.parseInt(numstr); }
            ispost = false;
        
            System.out.println("");
	    System.out.println("Request processed.");
	 //   socket.shutdownInput();
            
            
            String result = "none";
            int numOfGuesses = 0;
            
            System.out.println("before cookie check");
            if (sessionList.isEmpty()){
                Guess guessSession = new Guess(userId);
                sessionList.add(guessSession);
            } else {
                // if user has no cookie, start new game session
                if(!hasCookie){
                    Guess guessSession = new Guess(userId);
                    sessionList.add(guessSession);
                    
                // compares cookie to id stored in sessions
                } else {
                    for(Guess sessions : sessionList){
                        if(cookie == sessions.userId && !("/favicon.ico".equals(requestedDocument))){
                            sessions.runGame(userGuess);
                            result = sessions.getResult();
                            numOfGuesses = sessions.getNumOfGuesses();
                             // Toggle on to see session info
                            System.out.println("result: " + result);
                            System.out.println("num of guesses: " + numOfGuesses);
                            System.out.println("user ID: " + userId);
                            System.out.println("the number is " + sessions.getNumber());
                            System.out.println("userGuess is " + userGuess);
                            //*/
                            gSession = sessions;
                        }
                    }
                }
            }
            
            System.out.println("before handshake");
          //  SSLSocket expResponseSocket = null;
          //  String host = "localhost";
         //   expResponseSocket = (SSLSocket) sf.createSocket(host, 443); //Maybe change this???
            System.out.println("created new socket row 195");

        /*    try {
                expResponseSocket.startHandshake();
                System.out.println("handshake");
            } catch (IOException e) {
                System.out.println("*************" + e.getMessage());
            }*/
            
            System.out.println("row 204, after try-catch");
           //  SSLSocket sslSocket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(socket, host, 443, true);
         
	    PrintStream response =
		new PrintStream(socket.getOutputStream()); //was earlier "socket"
	    response.println("HTTP/1.1 200 OK");
	    response.println("Server: Trash 0.1 Beta");
	    if(requestedDocument.indexOf(".html") != -1)
		response.println("Content-Type: text/html");
	    if(requestedDocument.indexOf(".gif") != -1)
		response.println("Content-Type: image/gif");
	    
            if(!hasCookie){
                response.println("Set-Cookie: userId="+userId);
                userId++;
            }
             
            response.println("Message: "+result);
            
	    response.println();
            if(!("/favicon.ico".equals(requestedDocument))){ // Ignore any additional request to retrieve the bookmark-icon.
                if((cookie == 0) || (result.equals("newgame"))){ 
                    File f = new File("."+requestedDocument);
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
                            + "<div><form><button formaction=\"index.html\">New game</button></form></div></body>");
                    response.println("</html>");
                    gSession.resetGame();
                    // Toggle on to see session reset
                    //System.out.println("Game is reset: " + gSession.getNumOfGuesses() + " " + gSession.getResult());
                    
                } else if (userGuess == -1){
                    File f = new File("."+requestedDocument);
                    FileInputStream infil = new FileInputStream(f);
                    byte[] b = new byte[1024];
                    while( infil.available() > 0){
                        response.write(b,0,infil.read(b));
                    }
                } else {
                    response.println("<html>");
                    response.println("<head><title>Guessing Game</title><meta charset=\"windows-1252\">"
                            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head>");
                    response.println("<body><div>Wrong answer. Try " + result +  ". You have made " + numOfGuesses + " guesses. Press button to try again.</div>"
                            + "<div>What's your guess?<form action=\"index.html\" method=\"POST\">"
                            + "<input type=\"number\" min='1' max='100' value='5' name=\"number\"><input name=\"btn\" type=\"submit\">"
                            + "</form></body>");
                    response.println("</html>");
                }
            }
              request.close(); //maybe remove
              socket.shutdownOutput();
              socket.shutdownInput();
              socket.close();
        }
          }
             catch (Exception e){
                    
                    }
    }
}