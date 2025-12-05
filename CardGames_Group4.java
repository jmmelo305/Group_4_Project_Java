/*
 * Name: Card Games Simulator
 * Purpose: Create a program that simulates multiple card games including War, Go Fish, Blackjack, and Crazy Eights.
 * Programmers: Christopher Markham, Biruk Yidnekachew, Elizabeth Taiwo, Jose Melo
 * Date: 12 / 1 / 2025 to 12 / ....
 */


import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.Collections;
 
class Card {
    String suit = "";
    String faceId = "";
    int id = 0;
    int value = 0;
    Card(String suit, int id, int value) {
        this.suit = suit;
        this.id = id;
        this.value = value;
        switch (id) {
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
            String suit = (s == 0 ? "♥️" : (s == 1 ? "♠️" : (s == 2 ? "♦️" : "♣️")));
            for (int i = 0; i < 13; i++) {
                Card newCard = new Card(suit, i, worthMap.get(i));
                deck.add(newCard);
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
    void displayRules() {
        System.out.println("Welcome to WAR!");
        System.out.println("Win by stealling all of your opponents cards!");
        System.out.println("Steal by drawing a higher card than your opponent!");
    }
 
    @Override
    void setupDeck() {
        // Establishes worthMap, id = worth
        Map<Integer, Integer> worthMap = new HashMap<>();
        for (int i = 0; i < 13; i++) {
            worthMap.put(i, i+1);
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
        while (true) {
            // If you run out of cards you lose
            if (useIndex != 0) {
                int p1Size = player1Deck.size();
                int p2Size = player2Deck.size();
                if (p1Size == p2Size && p1Size < 3) {
                    System.out.println("Stalemate! Game Over!");
                    return 0;
                } else if (p1Size < 3) {
                    System.out.println("Player 2 won!");
                    return 2;
                } else if (p2Size < 3) {
                    System.out.println("Player 1 won!");
                    return 1;
                }
            } else {
                if (player1Deck.size() == 0) {
                    System.out.println("Player 2 won!");
                    return 2;
                } else if (player2Deck.size() == 0) {
                    System.out.println("Player 1 won!");
                    return 1;
                }
            }
 
            Card player1Card = player1Deck.get(useIndex);
            Card player2Card = player2Deck.get(useIndex);
            System.out.println("Player1 has "+player1Deck.size()+" card(s)");
            System.out.println("Player2 has "+player2Deck.size()+" card(s)");
            System.out.println("Player1 drew "+ player1Card.faceId + player1Card.suit);
            System.out.println("Player2 drew "+ player2Card.faceId + player2Card.suit);
            player1Deck.remove(useIndex);
            player2Deck.remove(useIndex);
            winPool.add(player1Card);
            winPool.add(player2Card);
 
            for (int i = useIndex - 1; i >= 0; i--) {
                Card card1 = player1Deck.get(i);
                Card card2 = player2Deck.get(i);
                player1Deck.remove(i);
                player2Deck.remove(i);
                winPool.add(card1);
                winPool.add(card2);
            }
 
            if (player1Card.value > player2Card.value) {
                System.out.print("Player1 won the battle and won ");
                Collections.shuffle(winPool);
                for (Card c : winPool) {
                    System.out.print(c.faceId + c.suit + " , ");
                    player1Deck.add(c);
                }
                System.out.println("");
                break;
            } else if (player1Card.value < player2Card.value) {
                System.out.print("Player2 won the battle and won ");
                Collections.shuffle(winPool);
                for (Card c : winPool) {
                    System.out.print(c.faceId + c.suit + " , ");
                    player2Deck.add(c);
                }
                System.out.println("");
                break;
            } else {
                System.out.println("A draw! This means WAR!");
                useIndex = 2;
            }
        }
        return -1;
    }
 
    @Override
    String getInput() {
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

    GoFish() {
        player1Hand = new ArrayList<>();
        player2Hand = new ArrayList<>();
        deck = new ArrayList<>();
        player1Books = 0;
        player2Books = 0;
        rand = new Random();
    }

    @Override
    void displayRules() {
        System.out.println("Welcome to GO FISH!");
        System.out.println("Win by collecting the most 'books' (4 cards of the same rank)!");
        System.out.println("Ask your opponent for a card rank you have in your hand.");
        System.out.println("If they have it, they must give you all cards of that rank.");
        System.out.println("If not, you 'Go Fish' and draw from the deck!");
    }

    @Override
    void setupDeck() {
        Map<Integer, Integer> worthMap = new HashMap<>();
        for (int i = 0; i < 13; i++) {
            worthMap.put(i, i + 1);
        }

        deck = GameHandler.makeDeck(worthMap);
        Collections.shuffle(deck);

        for (int i = 0; i < 7; i++) {
            player1Hand.add(deck.remove(0));
            player2Hand.add(deck.remove(0));
        }

        checkAndRemoveBooks(player1Hand, 1);
        checkAndRemoveBooks(player2Hand, 2);
    }

    @Override
    int playGame() {
        System.out.println("\n=== PLAYER 1'S TURN ===");
        System.out.println("Your hand: " + formatHand(player1Hand));
        System.out.println("Books - You: " + player1Books + " | Opponent: " + player2Books);
        
        if (player1Hand.isEmpty()) {
            if (!deck.isEmpty()) {
                player1Hand.add(deck.remove(0));
                System.out.println("Your hand was empty. Drew a card.");
            } else {
                return checkWinner();
            }
        }

        String askRank = getInput();
        if (askRank.equalsIgnoreCase("stop")) {
            System.out.println("Game Terminated!");
            return checkWinner();
        }

        if (!hasRankInHand(player1Hand, askRank)) {
            System.out.println("You don't have that rank in your hand! Try again.");
            return -1;
        }

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

        checkAndRemoveBooks(player1Hand, 1);

        if (isGameOver()) {
            return checkWinner();
        }

        System.out.println("\n=== PLAYER 2'S TURN ===");
        
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

        checkAndRemoveBooks(player2Hand, 2);

        if (isGameOver()) {
            return checkWinner();
        }

        return -1;
    }

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

    private String formatHand(ArrayList<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card c : hand) {
            sb.append(c.faceId).append(c.suit).append(" ");
        }
        return sb.toString();
    }

    private boolean hasRankInHand(ArrayList<Card> hand, String rank) {
        for (Card c : hand) {
            if (c.faceId.equalsIgnoreCase(rank)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Card> takeCardsFromHand(ArrayList<Card> hand, String rank) {
        ArrayList<Card> taken = new ArrayList<>();
        for (int i = hand.size() - 1; i >= 0; i--) {
            if (hand.get(i).faceId.equalsIgnoreCase(rank)) {
                taken.add(hand.remove(i));
            }
        }
        return taken;
    }

    private void checkAndRemoveBooks(ArrayList<Card> hand, int player) {
        Map<String, Integer> rankCount = new HashMap<>();
        
        for (Card c : hand) {
            rankCount.put(c.faceId, rankCount.getOrDefault(c.faceId, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 4) {
                String rank = entry.getKey();
                System.out.println("Player " + player + " completed a book of " + rank + "s!");
                
                for (int i = hand.size() - 1; i >= 0; i--) {
                    if (hand.get(i).faceId.equals(rank)) {
                        hand.remove(i);
                    }
                }

                if (player == 1) {
                    player1Books++;
                } else {
                    player2Books++;
                }
            }
        }
    }

    private String getAIRankChoice() {
        if (player2Hand.isEmpty()) {
            return "A";
        }
        int randomIndex = rand.nextInt(player2Hand.size());
        return player2Hand.get(randomIndex).faceId;
    }

    private boolean isGameOver() {
        return deck.isEmpty() && (player1Hand.isEmpty() || player2Hand.isEmpty());
    }

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
    ArrayList<Card> deck;
    ArrayList<Card> playerHand;
    ArrayList<Card> dealerHand;

    Scanner input = new Scanner (System.in);
    
    @Override

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

        // -------- PLAYER TURN ----------
        while (true) {
            System.out.println();
            System.out.print("Hit or Stand? Stop to end game. ");
            String choice = getInput().toLowerCase();

            if (choice.equals("stop")){
                System.out.println("Game Terminated! ");
                return 0;
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

        // -------- DEALER TURN ----------
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

        // -------- DETERMINE WINNER ----------
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

    // -------- Helper Functions --------

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


//Tester Class
public class Main {
 
    public static void main(String[] args) {
       
        while (true) {
            System.out.println("Select a game to play!");
            System.out.println("1 - War");
            System.out.println("2 - Go Fish");
            System.out.println("3 - Black Jack");
            System.out.println("4 - Crazy Eights");
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
            
            //else if Game 4


            else if (nextGame.equals("0")){
                System.out.println();
                System.out.println("Game Over");
                System.out.println("Thanks for playing!" );
                break;
            }
            
            else {
                System.out.println("Input 1 through 4!");
            }
        }
    }
}

//Slapjack Game Class
class Slapjack extends GameHandler {
    ArrayList<Card> player1Deck;
    ArrayList<Card> player2Deck;
 
    @Override
    void displayRules() {
        System.out.println("Welcome to Slapjack");
        System.out.println("Keep playing cards until you see a jack.")
        System.out.println("When you see one, slap it so you can claim the whole deck.");
        System.out.println("If you don't slap it, the computer will.")
        System.out.println("The person who gets the whole deck wins!");
    }

    @Override
    void setupDeck() {
        // Establishes worthMap, id = worth
        Map<Integer, Integer> worthMap = new HashMap<>();
        for (int i = 0; i < 13; i++) {
            worthMap.put(i, i+1);
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
        while (true) {
            // If you run out of cards you lose
            if (player1Deck.size() == 0) {
                    System.out.println("Player 2 won!");
                    return 2;
                } else if (player2Deck.size() == 0) {
                    System.out.println("Player 1 won!");
                    return 1;
                }

            Card player1Card = player1Deck.get(0);
            
            Card player2Card = player2Deck.get(0);
            System.out.println("Player1 has "+player1Deck.size()+" card(s)");
            System.out.println("Player2 has "+player2Deck.size()+" card(s)");
            System.out.println("Player1 drew "+ player1Card.faceId + player1Card.suit);
            System.out.println("Player2 drew "+ player2Card.faceId + player2Card.suit);
            player1Deck.remove(useIndex);
            player2Deck.remove(useIndex);
            winPool.add(player1Card);
            winPool.add(player2Card);

           if (player1Card.value > player2Card.value) {
                System.out.print("Player1 won the battle and won ");
                Collections.shuffle(winPool);
                for (Card c : winPool) {
                    System.out.print(c.faceId + c.suit + " , ");
                    player1Deck.add(c);
                }
                System.out.println("");
                break;
            } else if (player1Card.value < player2Card.value) {
                System.out.print("Player2 won the battle and won ");
                Collections.shuffle(winPool);
                for (Card c : winPool) {
                    System.out.print(c.faceId + c.suit + " , ");
                    player2Deck.add(c);
                }
                System.out.println("");
                break;
            } else {
                System.out.println("A draw! This means WAR!");
                useIndex = 2;
            }
        }
        return -1;
    }
 
    @Override
    String getInput() {
        System.out.println("Continue ...");
        Scanner input = new Scanner(System.in);
        String text = input.nextLine();
        return text;
    } 
}