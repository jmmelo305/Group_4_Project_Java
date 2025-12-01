/*
 * Name: Card Games Simulator
 * Purpose: 
 * Programmers: 
 * Date: 
 */

import java.util.Scanner;

public class CardGames_Group4 {
//Abstract Class
    public abstraction class Cards {
        public static int getNumbePlayers(int playerCount){
            
        }

    }

    public static void main(String[] args) {
        
    }
}

//Deck of Card Class
class Card {
    private String suit; //diamond, hearts, clubs, spades
    private char rank; //Ace, 2 - 10, Jack, Queen, King
    private int value; //Actual weight the card / face holds in a game

    

    private static int getValue(char rankIn) {
        Switch(rankIn) {
            case 'A':
                return 1
                break;
            case 'J':
                return 11
                break;
            case 'Q':
                return 12
                break;
            case 'K':
                return 13
                break;
            default:
                return rankIn
        }
    }

    public Card(String suit, int value) {
        this.suit = suit;
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
    
    
}

// CrazyEights Class
class CrazyEights{

}

