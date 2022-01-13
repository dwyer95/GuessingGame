package GuessingGame;

import java.util.Random;

/**
 *
 * @author Jacob Dwyer, Sara Bertse
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
        this.result = "newgame";
    }
    
    public void runGame(int userGuess){
        setUserGuess(userGuess);
        compare();   
        setNumOfGuesses(++numOfGuesses);
        System.out.println("Number of guesses " + numOfGuesses + " userId: " + userId);
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
    
    public void resetGame(){
        setNumOfGuesses(-1);
        setUserGuess(0);
        Random rng = new Random();
        setNumber(rng.nextInt(100) + 1);
        this.result = "newgame";
    }
}