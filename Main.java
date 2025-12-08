/*
 * Name: Card Games Simulator
 * Purpose: Create a program that simulates multiple card games including War, Go Fish, Blackjack, and Slapjackk.
 * Programmers: Christopher Markham, Biruk Yidnekachew, Elizabeth Taiwo, Jose Melo
 * Date: 12 / 1 / 2025 to 12 / 7 / 2025
 */


//All Imports
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.Collections;


//Card Deck Creator Class
class Card {
    String suit = ""; // The card's suit
    String faceId = ""; // The card's face value
    int id = 0; // The id of the card
    int value = 0; // The card's actual value
    
    // Card Object constructor
    Card(String suit, int id, int value) {
        this.suit = suit;
        this.id = id;
        this.value = value;
        switch (id) { // Turns the id into the face value
            case 0:
                this.faceId = "A";
                break;
            case 10:
                this.faceId = "J";
                break;
            case 11:
                this.faceId = "Q";
                break;
            case 12:
                this.faceId = "K";
                break;
            default:
                this.faceId = String.valueOf(id);
        }
    }
}

//Abstract Class
abstract class GameHandler {
 
    abstract void displayRules(); // Displays the rules
 
    abstract void setupDeck(); // Establishes the current deck
 
    abstract int playGame(); // One game function/action
 
    abstract String getInput(); // Gets user input
 
    public static ArrayList<Card> makeDeck(Map<Integer, Integer> worthMap) {
        ArrayList<Card> deck = new ArrayList<>();
        for (int s = 0; s < 4; s++) {
            String suit = (s == 0 ? "♥️" : (s == 1 ? "♠️" : (s == 2 ? "♦️" : "♣️"))); // Turns an int to it's associated suit string
            for (int i = 0; i < 13; i++) {
                Card newCard = new Card(suit, i, worthMap.get(i)); // Generates a new card
                deck.add(newCard); // Adds card to the deck
            }
        }
        return deck;
    }
}

//War Game Class
class War extends GameHandler {
    ArrayList<Card> player1Deck;
    ArrayList<Card> player2Deck;
 
    @Override
    void displayRules() { // Displays the instructions
        System.out.println("Welcome to WAR!");
        System.out.println("Win by stealling all of your opponents cards!");
        System.out.println("Steal by drawing a higher card than your opponent!");
    }
 
    @Override
    void setupDeck() {
        // Establishes worthMap, id = worth
        Map<Integer, Integer> worthMap = new HashMap<>();
        for (int i = 0; i < 13; i++) {
            worthMap.put(i, i+1); // War has each card value in accending order
        }
 
        // Makes and scrambles deck
        ArrayList<Card> deck = GameHandler.makeDeck(worthMap);
        Collections.shuffle(deck);
 
        // Splits deck
        int middleIndex = deck.size()/2;
        player1Deck = new ArrayList<>(deck.subList(0, middleIndex));
        player2Deck = new ArrayList<>(deck.subList(middleIndex, deck.size()));
    }
 
    @Override
    int playGame() {
        ArrayList<Card> winPool = new ArrayList<>();
        int useIndex = 0;
        while (true) { // Repeats until someone loses or someone draws a better card
            // If you run out of cards you lose
            if (useIndex != 0) { // Three card war move
                int p1Size = player1Deck.size();
                int p2Size = player2Deck.size();
                if (p1Size == p2Size && p1Size < 3) { // Not enough cards to continue
                    System.out.println("Stalemate! Game Over!");
                    return 0;
                } else if (p1Size < 3) { // Player 1 ran out of cards
                    System.out.println("Player 2 won!");
                    return 2;
                } else if (p2Size < 3) { // Player 2 ran out of cards
                    System.out.println("Player 1 won!");
                    return 1;
                }
            } else { // Normal move
                if (player1Deck.size() == 0) {
                    System.out.println("Player 2 won!");
                    return 2;
                } else if (player2Deck.size() == 0) {
                    System.out.println("Player 1 won!");
                    return 1;
                }
            }
 
            // Gets first card
            Card player1Card = player1Deck.get(useIndex);
            Card player2Card = player2Deck.get(useIndex);

            // Displays players total cards
            System.out.println("Player1 has "+player1Deck.size()+" card(s)");
            System.out.println("Player2 has "+player2Deck.size()+" card(s)");

            // Display first drawn card
            System.out.println("Player1 drew "+ player1Card.faceId + player1Card.suit);
            System.out.println("Player2 drew "+ player2Card.faceId + player2Card.suit);
            
            // Move first card to win pool
            player1Deck.remove(useIndex);
            player2Deck.remove(useIndex);
            winPool.add(player1Card);
            winPool.add(player2Card);
 
            // If 3 card move get and remove 2nd and 3rd card
            for (int i = useIndex - 1; i >= 0; i--) {
                Card card1 = player1Deck.get(i);
                Card card2 = player2Deck.get(i);
                player1Deck.remove(i);
                player2Deck.remove(i);
                winPool.add(card1);
                winPool.add(card2);
            }
 
            // Checks which player drew the highest card
            if (player1Card.value > player2Card.value) {
                System.out.print("Player1 won the battle and won ");
                Collections.shuffle(winPool);
                for (Card c : winPool) { // Displays all won cards
                    System.out.print(c.faceId + c.suit + " , ");
                    player1Deck.add(c);
                }
                System.out.println("");
                break;
            } else if (player1Card.value < player2Card.value) {
                System.out.print("Player2 won the battle and won ");
                Collections.shuffle(winPool);
                for (Card c : winPool) { // Displays all won cards
                    System.out.print(c.faceId + c.suit + " , ");
                    player2Deck.add(c);
                }
                System.out.println("");
                break;
            } else { // Both players drew the same value card so a 3 card move is needed to break the tie
                System.out.println("A draw! This means WAR!");
                useIndex = 2;
            }
        }
        return -1;
    }
 
    @Override
    String getInput() { // Delays the display so it does not crash trying to load the entire game
        System.out.println("Continue ...");
        Scanner input = new Scanner(System.in);
        String text = input.nextLine();
        return text;
    }
}
 
//GoFish Class Game
class GoFish extends GameHandler {
    ArrayList<Card> player1Hand;
    ArrayList<Card> player2Hand;
    ArrayList<Card> deck;
    int player1Books;
    int player2Books;
    Random rand;
    // Constructor to initialize all variables
    GoFish() {
        player1Hand = new ArrayList<>();
        player2Hand = new ArrayList<>();
        deck = new ArrayList<>();
        player1Books = 0;
        player2Books = 0;
        rand = new Random();
    }
    // Displays the rules for Go Fish
    @Override
    void displayRules() {
        System.out.println("Welcome to GO FISH!");
        System.out.println("Win by collecting the most 'books' (4 cards of the same rank)!");
        System.out.println("Ask your opponent for a card rank you have in your hand.");
        System.out.println("If they have it, they must give you all cards of that rank.");
        System.out.println("If not, you 'Go Fish' and draw from the deck!");
    }
      // Sets up and deals the deck
    @Override
    void setupDeck() {
        Map<Integer, Integer> worthMap = new HashMap<>();
        for (int i = 0; i < 13; i++) {
            worthMap.put(i, i + 1);
        }
   // Makes and shuffles the deck
        deck = GameHandler.makeDeck(worthMap);
        Collections.shuffle(deck);
    // Deals 7 cards to each player
        for (int i = 0; i < 7; i++) {
            player1Hand.add(deck.remove(0));
            player2Hand.add(deck.remove(0));
        }
  // Checks for any initial books
        checkAndRemoveBooks(player1Hand, 1);
        checkAndRemoveBooks(player2Hand, 2);
    }
// Main game loop for one round
    @Override
    int playGame() {
        System.out.println("\n=== PLAYER 1'S TURN ===");
        System.out.println("Your hand: " + formatHand(player1Hand));
        System.out.println("Books - You: " + player1Books + " | Opponent: " + player2Books);
        // If hand is empty, draw a card
        if (player1Hand.isEmpty()) {
            if (!deck.isEmpty()) {
                player1Hand.add(deck.remove(0));
                System.out.println("Your hand was empty. Drew a card.");
            } else {
                return checkWinner();
            }
        }
  // Gets the rank player wants to ask for
        String askRank = getInput();
        if (askRank.equalsIgnoreCase("stop")) {
            System.out.println("Game Terminated!");
            return checkWinner();
        }
   // Validates player has the rank they're asking for
        if (!hasRankInHand(player1Hand, askRank)) {
            System.out.println("You don't have that rank in your hand! Try again.");
            return -1;
        }
 // Attempts to take cards from opponent
        ArrayList<Card> takenCards = takeCardsFromHand(player2Hand, askRank);
        if (!takenCards.isEmpty()) {
            System.out.println("Player 2 had " + takenCards.size() + " card(s)! You got: ");
            for (Card c : takenCards) {
                System.out.print(c.faceId + c.suit + " ");
                player1Hand.add(c);
            }
            System.out.println();
        } else {
            System.out.println("Go Fish!");
            if (!deck.isEmpty()) {
                Card drawn = deck.remove(0);
                player1Hand.add(drawn);
                System.out.println("You drew: " + drawn.faceId + drawn.suit);
            } else {
                System.out.println("The deck is empty!");
            }
        }
  // Checks for completed books
        checkAndRemoveBooks(player1Hand, 1);
// Checks if game is over
        if (isGameOver()) {
            return checkWinner();
        }
// 2nd Players (ai) turn
        System.out.println("\n=== PLAYER 2'S TURN ===");
     // If AI hand is empty, draw a card
        if (player2Hand.isEmpty()) {
            if (!deck.isEmpty()) {
                player2Hand.add(deck.remove(0));
                System.out.println("Player 2's hand was empty. They drew a card.");
            } else {
                return checkWinner();
            }
        }

        String aiAskRank = getAIRankChoice();
        System.out.println("Player 2 asks for: " + aiAskRank);
 // AI selects a rank to ask for
        ArrayList<Card> aiTakenCards = takeCardsFromHand(player1Hand, aiAskRank);
        if (!aiTakenCards.isEmpty()) {
            System.out.println("You had " + aiTakenCards.size() + " card(s)! Player 2 took them.");
            for (Card c : aiTakenCards) {
                player2Hand.add(c);
            }
        } else {
            System.out.println("You didn't have any. Player 2 goes fishing!");
            if (!deck.isEmpty()) {
                Card drawn = deck.remove(0);
                player2Hand.add(drawn);
                System.out.println("Player 2 drew a card.");
            } else {
                System.out.println("The deck is empty!");
            }
        }
// AI attempts to take cards from player
        checkAndRemoveBooks(player2Hand, 2);

        if (isGameOver()) {
            return checkWinner();
        }
// Returns -1 to continue the game
        return -1;
    }
// Gets user input for rank selection
    @Override
    String getInput() {
        System.out.print("Ask for a rank (A, 2-10, J, Q, K) or type 'stop' to quit: ");
        Scanner input = new Scanner(System.in);
        String text = input.nextLine().trim().toUpperCase();
        if (text.equals("1")) {
            text = "A";
        }
        return text;
    }
  // Formats hand into a readable string
    private String formatHand(ArrayList<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card c : hand) {
            sb.append(c.faceId).append(c.suit).append(" ");
        }
        return sb.toString();
    }
 // Checks if a specific rank exists in hand
    private boolean hasRankInHand(ArrayList<Card> hand, String rank) {
        for (Card c : hand) {
            if (c.faceId.equalsIgnoreCase(rank)) {
                return true;
            }
        }
        return false;
    }
 // Takes all cards of a specific rank from hand
    private ArrayList<Card> takeCardsFromHand(ArrayList<Card> hand, String rank) {
        ArrayList<Card> taken = new ArrayList<>();
        for (int i = hand.size() - 1; i >= 0; i--) {
            if (hand.get(i).faceId.equalsIgnoreCase(rank)) {
                taken.add(hand.remove(i));
            }
        }
        return taken;
    }
 // Checks for and removes completed books (4 of a kind)
    private void checkAndRemoveBooks(ArrayList<Card> hand, int player) {
        Map<String, Integer> rankCount = new HashMap<>();
          // Counts how many of each rank are in hand
        for (Card c : hand) {
            rankCount.put(c.faceId, rankCount.getOrDefault(c.faceId, 0) + 1);
        }
// Checks each rank for a book (4 cards)
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 4) {
                String rank = entry.getKey();
                System.out.println("Player " + player + " completed a book of " + rank + "s!");
                 // Removes all 4 cards of that rank from hand
                for (int i = hand.size() - 1; i >= 0; i--) {
                    if (hand.get(i).faceId.equals(rank)) {
                        hand.remove(i);
                    }
                }
// Increments the player's book count
                if (player == 1) {
                    player1Books++;
                } else {
                    player2Books++;
                }
            }
        }
    }
// AI picks a random rank from its hand
    private String getAIRankChoice() {
        if (player2Hand.isEmpty()) {
            return "A";
        }
        int randomIndex = rand.nextInt(player2Hand.size());
        return player2Hand.get(randomIndex).faceId;
    }
// Checks if the game is over
    private boolean isGameOver() {
        return deck.isEmpty() && (player1Hand.isEmpty() || player2Hand.isEmpty());
    }
// Determines and displays the winner
    private int checkWinner() {
        System.out.println("\n=== GAME OVER ===");
        System.out.println("Final Score:");
        System.out.println("Player 1: " + player1Books + " books");
        System.out.println("Player 2: " + player2Books + " books");

        if (player1Books > player2Books) {
            System.out.println("Player 1 wins!");
            return 1;
        } else if (player2Books > player1Books) {
            System.out.println("Player 2 wins!");
            return 2;
        } else {
            System.out.println("It's a tie!");
            return 0;
        }
    }
}

//BlackJack Class Game
class BlackJack extends GameHandler {

    //Initializes Variables used throughout the BlackJack Class
    ArrayList<Card> deck;
    ArrayList<Card> playerHand;
    ArrayList<Card> dealerHand;
    Scanner input = new Scanner (System.in);
    
    @Override
    
    //Method to print out the rules of Blacjack
    void displayRules(){
        System.out.println("===BLACKJACK===");
        System.out.println("Goal: Get closer to 21 than the dealer without going over. ");
        System.out.println("Aces count as 1 or 11. ");
        System.out.println("Player may hit or stand. ");
        System.out.println("If player's total is over 21, you bust, you lose. ");
        System.out.println("If dealer's total is over 21, they bust, you win! ");
        System.out.println("If dealer and player are under 21, closest to 21 wins ");
        System.out.println("-----------------------");
    }

    //Method to setup deck specifically for BlackJack
    @Override
    void setupDeck(){
        // Card values: Ace = 1 or 11 (player decides), 2 - 10 = face value, J/Q/K = 10
        Map <Integer, Integer> worthMap = new HashMap<>();
        worthMap.put(0,11);
        for (int i = 1; i <= 9; i++) worthMap.put(i, i + 1);
        worthMap.put(10,10);
        worthMap.put(11,10);
        worthMap.put(12,10);
        
        deck = GameHandler.makeDeck(worthMap);
        Collections.shuffle(deck);
    }

    //Method that runs the BlackJack Game
    @Override
    int playGame(){
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        playerHand.add(drawCard());
        playerHand.add(drawCard());
        dealerHand.add(drawCard());
        dealerHand.add(drawCard());

        // Show player & dealer info
        System.out.println("Your cards: " + handToString(playerHand) + 
                           " (Total: " + getTotal(playerHand) + ")");
        System.out.println();
        System.out.println("Dealer shows: " + cardToString(dealerHand.get(0)));

        // Player's Turn
        while (true) {
            System.out.println();
            System.out.print("Hit or Stand? or Stop to end game. ");
            String choice = getInput().toLowerCase();

            if (choice.equals("stop")){
                System.out.println("Game Terminated! ");
                return 0; //Game kill switch
            }
            if (choice.equals("hit")) {
                playerHand.add(drawCard());
                System.out.println("You drew: " +
                    cardToString(playerHand.get(playerHand.size() - 1)));
                System.out.println("Your total: " + getTotal(playerHand));

                if (getTotal(playerHand) > 21) {
                    System.out.println("You bust! Dealer wins.");
                    return 2; // dealer wins
                }

            } else if (choice.equals("stand")) {
                break;
            } else {
                System.out.println("Type 'hit' or 'stand' or 'stop' to end game.");
                
            }
        }

        // Dealer's Turn
        System.out.println("Dealer's hand: " + handToString(dealerHand) +
                           " (Total: " + getTotal(dealerHand) + ")");
        System.out.println();

        while (getTotal(dealerHand) < 17) {
            dealerHand.add(drawCard());
            System.out.println("Dealer draws: " +
                cardToString(dealerHand.get(dealerHand.size() - 1)));
            System.out.println();
        }

        int playerTotal = getTotal(playerHand);
        int dealerTotal = getTotal(dealerHand);

        System.out.println("Final totals → Player: " + playerTotal +
                           " | Dealer: " + dealerTotal);
        System.out.println();

        // Determines Winner
        if (dealerTotal > 21) {
            System.out.println("Dealer busts! You win!");
            return 1;
        }

        if (playerTotal > dealerTotal) {
            System.out.println("You win!");
            return 1;
        } else if (dealerTotal > playerTotal) {
            System.out.println("Dealer wins.");
            return 2;
        } else {
            System.out.println("Push (tie).");
            return 0;
        }
    }

    @Override
    String getInput() {
        return input.nextLine();
    }

    //Helper Functions

    Card drawCard() {
        return deck.remove(0);
    }

    int getTotal(ArrayList<Card> hand) {
        int sum = 0;
        int aces = 0;

        for (Card c : hand) {
            sum += c.value;
            if (c.faceId.equals("A")) aces++;
        }

        // Convert Aces from 11 → 1 if necessary
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }

        return sum;
    }

    String cardToString(Card c) {
        return c.faceId + c.suit;
    }

    String handToString(ArrayList<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card c : hand) sb.append(cardToString(c)).append(" ");
        return sb.toString();
    }
}

//SlapJack Class Game

class Slapjack extends GameHandler { 
    @Override
    // Method to display slapjack rules implemented
    void displayRules() {
        System.out.println("Welcome to Slapjack");
        System.out.println("Keep playing cards until you see a jack.");
        System.out.println("When you see one, slap it so you can claim the whole deck.");
        System.out.println("If you don't slap it, the computer will.");
        System.out.println("If you slap by accident, the computer gets the deck.");
        System.out.println("The person who gets the whole deck wins!");
    }

    ArrayList<Card> playerDeck; // Declares player's hand
    ArrayList<Card> computerDeck; // Declares computer's hand

    @Override
    // Method to setup the deck implemented
    void setupDeck() {
        // Card values: Ace = 1 or 11 (player decides), 2 - 10 = face value, J/Q/K = 10
        Map <Integer, Integer> worthMap = new HashMap<>();
        worthMap.put(0,11);
        for (int i = 1; i <= 9; i++) worthMap.put(i, i + 1);
        worthMap.put(10,10);
        worthMap.put(11,10);
        worthMap.put(12,10);
        
        // Makes and shuffles deck
        ArrayList<Card> deck = GameHandler.makeDeck(worthMap);
        Collections.shuffle(deck);
 
        // Splits deck evenly between two players
        int middleIndex = deck.size()/2;
        playerDeck = new ArrayList<>(deck.subList(0, middleIndex));
        computerDeck = new ArrayList<>(deck.subList(middleIndex, deck.size()));
    } 

    @Override
    // Method to play game implemented
    int playGame() {
        ArrayList<Card> winPool = new ArrayList<>(); // The pool of cards claimed by a player when a jack is slapped
        Card turnCard; // The card at the top of the pool

        // Loops through player-computer turns until someone has won
        while (true) {
            // Sets up logic of a player losing if they're out of cards
            if (playerDeck.size() == 0) {
                System.out.println("The computer won!");
                return 2;
            } else if (computerDeck.size() == 0) {
                System.out.println("You won!");
                return 1;
            }

            // Start of player turn
            turnCard = playerDeck.get(0); // Card played
            System.out.println("You have "+playerDeck.size()+" card(s)");
            System.out.println("The computer has "+computerDeck.size()+" card(s)");
            System.out.println("You played "+ turnCard.faceId + turnCard.suit);
            playerDeck.remove(0); // Removed from player hand
            winPool.add(turnCard);

            System.out.println();

            // Offers choice of slapping card
            System.out.print("Would you like to slap or pass? (type Stop to end game.) ");
            String choice = getInput().toLowerCase();

            // Loops as error handling
            while (true){
                if (choice.equals("stop")){
                    System.out.println("Game Terminated! ");
                    return 0; //Game kill switch
                }
                // Logic of a slapjack turn (who claims winPool)
                if (choice.equals("slap")) {
                    if (turnCard.faceId.equals("J")) {
                        System.out.println("Correct! A jack was played.");
                        Collections.shuffle(winPool);
                        for (Card c : winPool) {
                            playerDeck.add(c);
                        }
                        winPool.clear();
                        break;
                    } else{
                        System.out.println("Sorry, a jack wasn't played. The computer takes the pool.");
                        Collections.shuffle(winPool);
                        for (Card c : winPool) {
                            computerDeck.add(c);
                        }
                        winPool.clear();
                        break;
                    }

                } else if (choice.equals("pass")) {
                    if (turnCard.faceId.equals("J")) {
                        System.out.println("Sorry, a jack was played. The computer takes the pool.");
                        Collections.shuffle(winPool);
                        for (Card c : winPool) {
                            computerDeck.add(c);
                        }
                        winPool.clear();
                        break;
                    } else{
                        System.out.println("Correct! A jack wasn't played.");
                        break;
                    }
                } else {
                    System.out.println("Type 'slap' or 'pass' or 'stop' to end game.");
                    choice = getInput().toLowerCase();
                    
                }
            }
            
            // Start of computer turn
            turnCard = computerDeck.get(0); // Card played
            System.out.println("You have "+playerDeck.size()+" card(s)");
            System.out.println("The computer has "+computerDeck.size()+" card(s)");
            System.out.println("The computer played "+ turnCard.faceId + turnCard.suit);
            computerDeck.remove(0); // Removed from computer hand
            winPool.add(turnCard);

            System.out.println();

            // Computer gets first dibs if it plays the jack
            if (turnCard.faceId.equals("J")) {
                System.out.println("A jack was played and the computer slapped it.");
                Collections.shuffle(winPool);
                for (Card c : winPool) {
                    computerDeck.add(c);
                }
                winPool.clear();
            } else{
                // Player may make a mistake, so is asked for input anyway
                System.out.print("Would you like to slap or pass? (type Stop to end game.) ");
                choice = getInput().toLowerCase();
                // Loops as error handling
                while(true){
                    if (choice.equals("stop")){
                        System.out.println("Game Terminated! ");
                        return 0; //Game kill switch
                    }
                    else if (choice.equals("slap")) {
                        System.out.println("Sorry, a jack wasn't played. The computer takes the pool.");
                        Collections.shuffle(winPool);
                        for (Card c : winPool) {
                            computerDeck.add(c);
                        }
                        winPool.clear();
                        break;
                    } else if (choice.equals("pass")) {
                        System.out.println("Correct! A jack wasn't played.");
                        break;
                    } else{
                        System.out.println("Type 'slap' or 'pass' or 'stop' to end game.");
                        choice = getInput().toLowerCase();
                    }
                }               
            }
        }
    }
 
    @Override
    // Implements getInput method
    String getInput() {
        System.out.println("Continue ...");
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    } 
} 


//Tester Class
public class Main {
 
    public static void main(String[] args) {
       
        while (true) {
            // Main menu for game
            System.out.println("Select a game to play!");
            System.out.println("1 - War");
            System.out.println("2 - Go Fish");
            System.out.println("3 - Black Jack");
            System.out.println("4 - Slap Jack");
            System.out.println("0 - Stop Session");
            Scanner input = new Scanner(System.in);
            String nextGame = input.nextLine();
 
            // Handles the game of War
            if (nextGame.equals("1")) {
                War game = new War();
                game.displayRules();
                game.setupDeck();
                while (true) {
                    int results = game.playGame();
                    if (results != -1) { // -1 continues the game. Anything else terminates the game
                        break;
                    }
                    String text = game.getInput();
                    if (text.toLowerCase().equals("stop")) { // Terminates the game early
                        System.out.println("Game Terminated!");
                        break;
                    }
                }
                
            }
            // Handles the game of GoFish
            else if (nextGame.equals("2")){
                GoFish game = new GoFish();
                game.displayRules();
                game.setupDeck();
                while (true) {
                    int results = game.playGame();
                    if (results != -1) {
                        break;
                    }
                }
            }
            
            // Handles the game of BlackJack
            else if(nextGame.equals("3")){
                BlackJack game = new BlackJack();
                game.displayRules();
                game.setupDeck();
                while (true) {
                    int results = game.playGame();
                    if (results != -1) {
                        break;
                    }
                    String text = game.getInput();
                    if (text.toLowerCase().equals("stop")) {
                        System.out.println("Game Terminated!");
                        break;
                    }
                }
            }

            // Handles the game of SlapJack
            else if (nextGame.equals("4")){
                Slapjack game = new Slapjack();
                game.displayRules();
                System.out.println("");
                game.setupDeck();
                while (true){
                    int results = game.playGame();
                    if (results != -1){
                        break;
                    }
                    String text = game.getInput();
                    if (text.toLowerCase().equals("stop")){
                        System.out.println("Game Terminated");
                        break;
                    }
                }
            }
            
            // Kill Switch for the entire program
            else if (nextGame.equals("0")){
                System.out.println();
                System.out.println("Game Over");
                System.out.println("Thanks for playing!" );
                break;
            }
            
            // Error handling for any number other than 0 - 4
            else {
                System.out.println("Input 1 through 4!");
            }
        }
    }
}
