/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuessingGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author HP
 */
public class Client {
    public static void main(String[] args) throws Exception{
	String host = "127.0.0.1"; //www.apache.org
	int port = 7070; //80
	String file = "index.html";
	Socket s =
	    new Socket(host,port);
	
	PrintStream utdata =
	    new PrintStream(s.getOutputStream());
	utdata.println("GET /" + file + " HTTP/1.1");
        utdata.println("127.0.0.1"); //Host: www.apache.org
	utdata.println("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0");
        utdata.println();
	s.shutdownOutput();
	
	BufferedReader indata =
	    new BufferedReader(new InputStreamReader(s.getInputStream()));
	String str = "";
	while( (str = indata.readLine()) != null){
	    System.out.println(str);
	}
	s.close();
    }
}
