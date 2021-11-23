/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author HP
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
        //for(int j = 0; j < 3; j++){
            try {
                //Sends request
                u = new URL("http://127.0.0.1:7070/index.html"); //https://www.kth.se
                h = (HttpURLConnection) u.openConnection();
                
                if(hasCookie){
                    h.setRequestMethod("POST");
                    h.setRequestProperty("Set-Cookie", "userId=" +cookie);
                    h.setDoOutput(true);

                    //String state = "";
                    
                    
                    if (state.equals("higher")) {
                        if (helper / 2 < 1) {
                            helper = 1;
                        } else {
                            helper = helper / 2;
                        }

                        guess = (guess + helper);
                    } else if (state.equals("lower")) {
                        if (helper / 2 < 1) {
                            helper = 1;
                        } else {
                            helper = helper / 2;
                        }

                        guess = guess - helper;

                    } else if (state.equals("win")) {
                        //gameWon = true;
                        break;
                    } else {
                        //gameWon = true;
                        //System.out.println("Game interrupted: error");
                        //break;

                    }
                    
                    System.out.println("uesr guess: " + guess);

                    OutputStream os = h.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
                    osw.write("number=" + guess + "&btn=Skicka+fr%E5ga");
                    osw.flush();
                    osw.close();
                    os.close();
                    System.out.println("cookie to server: " +cookie);
                }
                h.connect();

                //Rcvs response in h
                
                //Den tredje headern är userId=value cookien
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < h.getHeaderFields().size(); i++) {
                    //cookie=(h.getHeaderField(i));
                    sb.append(h.getHeaderField(i));
                    System.out.println(h.getHeaderField(i));
                }
                
                String srvResponse = sb.toString();
                if(srvResponse.contains("userId")){
                    String cookieStr = (h.getHeaderField(3));
                    System.out.println("cookieStr: " + cookieStr);
                    StringTokenizer tokens = new StringTokenizer(cookieStr, " =");
                    tokens.nextToken();
                    cookie = tokens.nextToken();
                    System.out.println("the cookie: "+cookie);
                    hasCookie=true;
                } else if(srvResponse.contains("lower") || srvResponse.contains("higher")
                        || srvResponse.contains("win")){
                    state = (h.getHeaderField(3));
                    System.out.println("stateStr: " + state);
                    //StringTokenizer tokens = new StringTokenizer(stateeStr, " =");
                    //tokens.nextToken();
                    //cookie = tokens.nextToken();
                    //System.out.println("the cookie: "+cookie);
                    //hasCookie=true;
                }

                
                //Denna kan man använda för att skicka en POST
                //h.setRequestMethod("POST");
                //h.getHeaderField(name)
                
                
                is = h.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String str;
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                }
                
                
                
                
                //int test = 25;
                //System.out.println(test/2);
                
            } catch (java.net.MalformedURLException e) {
                System.out.println(e.getMessage());
            } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}