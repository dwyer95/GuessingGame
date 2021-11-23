/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuessingGame;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.StringTokenizer;

/**
 *
 * @author HP
 */
public class WebClient {
    
    public static void main(String[] args){
        try{
            URL u;
            HttpURLConnection h;
            InputStream is;
            u = new URL("http://127.0.0.1:7070/index.html"); //https://www.kth.se
            h = (HttpURLConnection)u.openConnection();
            h.setRequestProperty("Set-Cookie", "en-US");
            h.connect();
           
         
            //Den tredje headern är userId=value cookien
         /*   for(int i = 0; i<h.getHeaderFields().size(); i++){
               cookie=(h.getHeaderField(i));
            }*/
         
            String cookieStr=(h.getHeaderField(3));
            StringTokenizer tokens = new StringTokenizer(cookieStr," =");
            tokens.nextToken();
            String cookie = tokens.nextToken();
            System.out.println(cookie);
            
            
            //Denna kan man använda för att skicka en POST
            //h.setRequestMethod("POST");
            
            
            
            //h.getHeaderField(name)
            is = h.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str;
            while( (str = br.readLine()) != null){
                System.out.println(str);
            }
        }
        catch(java.net.MalformedURLException e){
            System.out.println(e.getMessage());
        }
        catch(java.io.IOException e){
            System.out.println(e.getMessage());
        }
    }
}
