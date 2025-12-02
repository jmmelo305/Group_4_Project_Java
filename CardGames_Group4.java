/*
 * Name: Card Games Simulator
 * Purpose: Create a program that simulates multiple card games including War, Go Fish, Blackjack, and Crazy Eights.
 * Programmers: Christopher Markham, Biruk Yidnekachew, Elizabeth Taiwo, Jose Melo
 * Date: 12 / 1 / 2025 to 12 / ....
 */


import java.util.Scanner;
import java.util.ArrayList; 

public class CardGames_Group4 {

    public static void main(String[] args) {
        
    }
}

public abstract class CardGame {
    ArrayList<Card> cardDeck = new ArrayList<>();
        for (int s = 0; s < 4; s++) {
            for (int c = 0; c < 14; c++) {
                
            }
        }
}
//Deck of Card Class
class Card {
    private String suit; //diamond, hearts, clubs, spades
    private char rank; //Ace, 2 - 10, Jack, Queen, King
    private int value; //Actual weight the card / face holds in a game
    // Assigns data to the value of the card
    switch(rank) {
            case 'A':
                value = 1;
                break;
            case 'J':
                value = 11;
                break;
            case 'Q':
                value = 12;
                break;
            case 'K':
                value = 13;
                break;
            default:
                return rankIn
    }
    private static int getValue(char rankIn) {
        
        }
    }

    public Card(String suit, char rank) {
        this.sank = rankuit = suit;
        this.r
    }
    
    
    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }
    
}

//War Class 
class War{
}

//Go Fish Class
class Gofish{
}

//Blackjack Class
class Blackjack{
//objective: get as close to 21 without going over

    
    
}

// CrazyEights Class
class CrazyEights{

}

