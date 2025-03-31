import javax.swing.*;
import java.awt.*;
import java.util.Random;

/* This program is a simulation of the game Blackjack */
/*I have tried to use your help(your comments) but it was played with me bad joke,
so I rewrote the code in different way, I hope that it is not problem
*/

public class Blackjack {
    private static int[] deck = new int[52];
    private static int nextCardIndex = 0;

    public static void main(String[] args) {
        // Declare the array to hold the human player cards
        int[] humanCards = new int[9];
        // Declare the array to hold the computer player cards
        int[] computerCards = new int[9];


        initializeDeck(deck);
        shuffleDeck(deck);


        dealInitialHands(deck, humanCards, computerCards);

        int humanCardCount = 2;
        int computerCardCount = 2;

        boolean gameOn = true;

        while (gameOn) {

            displayRankAndSuitHands(humanCards, computerCards, humanCardCount, computerCardCount);

            char winner = checkForWinner(humanCards, computerCards, humanCardCount, computerCardCount);
            if (winner != 'n') {
                displayWinner(winner);
                break;
            }

            int choice = JOptionPane.showConfirmDialog(null, "Would you like to stick?", "Select Option", JOptionPane.YES_NO_OPTION);

            while (choice == JOptionPane.CANCEL_OPTION) {
                choice = JOptionPane.showConfirmDialog(null, "Would you like to stick?", "Select Option", JOptionPane.YES_NO_OPTION);
            }


            if (choice == JOptionPane.NO_OPTION) {
                humanCards[humanCardCount++] = dealNextCard();
            }


            if (choice == JOptionPane.YES_OPTION) {
                while (handValue(computerCards, computerCardCount) < 17) {
                    computerCards[computerCardCount++] = dealNextCard();
                }
            }


            winner = checkForWinner(humanCards, computerCards, humanCardCount, computerCardCount);
            displayWinner(winner);
            gameOn = false;
        }
        System.exit(0);
    }

    public static void initializeDeck(int[] deck) {
        for (int i = 0; i < deck.length; i++) {
            deck[i] = i + 1; // Cards are represented from 1 to 52
        }
    }

    public static void shuffleDeck(int[] deck) {
        Random rand = new Random();
        for (int i = 0; i < deck.length; i++) {
            int randomIndex = rand.nextInt(deck.length);
            int temp = deck[i];
            deck[i] = deck[randomIndex];
            deck[randomIndex] = temp;
        }
    }

    public static void dealInitialHands(int[] deck, int[] humanCards, int[] computerCards) {
        humanCards[0] = dealNextCard();
        humanCards[1] = dealNextCard();
        computerCards[0] = dealNextCard();
        computerCards[1] = dealNextCard();
    }

    public static int dealNextCard() {
        return deck[nextCardIndex++];
    }

    public static int handValue(int[] cards, int count) {
        int value = 0;
        int aces = 0;
        for (int i = 0; i < count; i++) {
            int card = cards[i];
            if (card > 10) {
                value += 10; // J, Q, K
            } else if (card == 1) {
                aces++;
                value += 11;
            } else {
                value += card;
            }
        }

        // Adjust for Aces
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public static char checkForWinner(int[] humanCards, int[] computerCards, int humanCount, int computerCount) {
        int humanValue = handValue(humanCards, humanCount);
        int computerValue = handValue(computerCards, computerCount);

        if (humanValue > 21) return 'c'; // Human busted
        if (computerValue > 21) return 'h'; // Computer busted
        if (humanValue == computerValue) return 't'; // Tie
        return (humanValue > computerValue) ? 'h' : 'c'; // Determine the winner
    }

    private static void displayWinner(char winner) {
        if (winner == 'c')
            JOptionPane.showMessageDialog(null, "You are busted! Computer player wins", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
        else if (winner == 'h')
            JOptionPane.showMessageDialog(null, "Computer player is busted! You win", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
        else if (winner == 't')
            JOptionPane.showMessageDialog(null, "Game was tied. Both players had the same score!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void displayRankAndSuitHands(int[] humanCards, int[] computerCards, int humanCount, int computerCount) {
        JTextArea textArea = initialiseTextArea();

        // Human Hand
        textArea.append(String.format("%-18s", "Human Hand:   "));
        for (int i = 0; i < humanCount; i++) {
            textArea.append(String.format("%-4s", mapNumericCardToRankAndSuitCard(humanCards[i])));
        }

        // Computer Hand
        textArea.append(String.format("\n\n%-18s", "Computer Hand:   "));
        for (int i = 0; i < computerCount; i++) {
            textArea.append(String.format("%-4s", mapNumericCardToRankAndSuitCard(computerCards[i])));
        }

        JOptionPane.showMessageDialog(null, textArea);
    }

    public static JTextArea initialiseTextArea() {
        JTextArea textArea = new JTextArea();
        Font fontTextArea = new Font("monospaced", Font.BOLD, 12);
        textArea.setFont(fontTextArea);
        return textArea;
    }

    public static String mapNumericCardToRankAndSuitCard(int card) {
        String[] suits = {"\u2660", "\u2661", "\u2662", "\u2663"}; // Spades, Hearts, Diamonds, Clubs
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        int rankIndex = (card - 1) % 13;
        int suitIndex = (card - 1) / 13;

        return ranks[rankIndex] + suits[suitIndex];
    }
}