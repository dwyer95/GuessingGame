/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuessingGame;

import java.util.Random;

/**
 *
 * @author HP
 */
public class Guess {
    int number;
    //int userGuess;
    int numOfGuesses;
    
    public static void main(String[] args){
        
        Random rng = new Random();
        setNumber(0);
        int number = rng.nextInt(100) + 1;
        userGuess = 0;
        
        startGame();
                
        
        
        while(true){
            //keep comparing user guess to number
            
            
            //player guesse number
            if(userGuess == number){
                // send something to server??
            }
            else{
                // set guessNumber to 
            }
        }
    }
    
    public void startGame(){
        setNumOfGuesses(0);
    }
    
    public int getNumber(){
        return number;
    }
    
    public void setNumber(){
        
    }
    
    public int getUserGuess(){
        return userGuess;
    }
    
    public void setUserGuess(){
        
    }
    
    public int getNumOfGuesses(){
        return numOfGuesses;
    }
    
    public void setNumOfGuesses(int num){
        numOfGuesses = num;
    }
}
