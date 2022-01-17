package GuessingGame;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.StringTokenizer;

/**
 *
 * @author Jacob Dwyer, Sara Bertse
 */
public class WebClient {
    
    public static void main(String[] args){
        URL u;
        HttpURLConnection h;
        InputStream is;
        boolean hasCookie = false;
        String cookie = "0";
        String state = "";
        int guess = 50;
        int helper = 50;
                
        boolean gameWon = false;
                
        while (!gameWon) {
            try {
                //Sends request
                u = new URL("http://127.0.0.1:7070/index.html"); //https://www.kth.se
                h = (HttpURLConnection) u.openConnection();
                
                if(hasCookie){
                    h.setRequestMethod("POST");
                    h.setRequestProperty("Set-Cookie", "userId=" +cookie);
                    h.setDoOutput(true);
                    
                    if (state.equals("higher")) {
                        if (helper / 2 < 1) { helper = 1; } 
                        else { helper = helper / 2; }

                        guess = (guess + helper);
                    } else if (state.equals("lower")) {
                        if (helper / 2 < 1) { helper = 1; } 
                        else { helper = helper / 2; }

                        guess = guess - helper;

                    } else if(state.equals("win")){ 
                        break; 
                    }

                    OutputStream os = h.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
                    osw.write("number=" + guess + "&btn=Skicka+fr%E5ga");
                    osw.flush();
                    osw.close();
                    os.close();
                }
                h.connect();

                //Rcvs response in h
                
                //Den tredje headern är userId=value cookien
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < h.getHeaderFields().size(); i++) {
                    sb.append(h.getHeaderField(i));
                    System.out.println(h.getHeaderField(i));
                }
                
                String srvResponse = sb.toString();
                if(srvResponse.contains("userId")){
                    String cookieStr = (h.getHeaderField(3));
                    StringTokenizer tokens = new StringTokenizer(cookieStr, " =");
                    tokens.nextToken();
                    cookie = tokens.nextToken();
                    hasCookie=true;
                } else if(srvResponse.contains("lower") || srvResponse.contains("higher")
                        || srvResponse.contains("win")){
                    state = (h.getHeaderField(3));
                }
                
                is = h.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String str;
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                }
                
            } catch (java.net.MalformedURLException e) {
                System.out.println(e.getMessage());
            } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}