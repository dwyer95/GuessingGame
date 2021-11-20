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
    int userGuess;
    int numOfGuesses;
    int userId;
    
    public Guess(int userId){
        this.userId = userId;
        Random rng = new Random();
        setNumber(rng.nextInt(100) + 1);
    }
    
       public void startGame(){  //(int numberOfGuesses, int userGuess, int number){
           userGuess = 0;
           setNumOfGuesses(0);
        
        while(true){
            //keep comparing user guess to number
            
            
            //player guesse number
            if(userGuess == getNumber()){
                // send something to server??
            }
            else{
                // set guessNumber to 
            }
        }
    }
    
    public int getNumber(){
        return number;
    }
    
    public void setNumber(int number){
        this.number = number;
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
    
    public void setUserGuess(int userGuess){
        this.userGuess = userGuess;
    }
    
    public String compare(){
        String result = "";
        if (userGuess > number){
            result = "lower";
        }
        else if (userGuess < number){
            result= "higher";
        }
        else if (userGuess == number){
            result = "win";
        }
        else {
            result = "something is wrong";
        }
        return result;
    }
}