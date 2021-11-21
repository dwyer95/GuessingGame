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
    String result;
    
    public Guess(int userId){
        this.userId = userId;
        setNumOfGuesses(0);
        setUserGuess(0);
        Random rng = new Random();
        setNumber(rng.nextInt(100) + 1);
    }
    
    public void runGame(int userGuess){  //(int numberOfGuesses, int userGuess, int number){
        setUserGuess(userGuess);
        compare();   
        setNumOfGuesses(++numOfGuesses);
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
    
    public void setUserGuess(int userGuess){
       this.userGuess = userGuess;
    }
    
    public int getNumOfGuesses(){
        return numOfGuesses;
    }
    
    public void setNumOfGuesses(int num){
        this.numOfGuesses = num;
    }
    
    public String getResult(){
        return result;
    }
    
    public void compare(){
        //this.result = "";
        if (userGuess > number){
            this.result = "lower";
        }
        else if (userGuess < number){
            this.result= "higher";
        }
        else if (userGuess == number){
            this.result = "win";
        }
        else {
            this.result = "something is wrong";
        }
    }
}