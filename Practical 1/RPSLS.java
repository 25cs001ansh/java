import java.util.Random;
import java.util.Scanner;

public class RPSLS {

    // (a) Define enum Move
    enum Move {
        ROCK, PAPER, SCISSORS, LIZARD, SPOCK
    }

    // (b) winner(Move a, Move b): returns 1 if a beats b, -1 if b beats a, 0 for tie
    public static int winner(Move a, Move b) {
        if (a == b) {
            return 0;
        }

        return switch (a) {
            case ROCK -> (b == Move.SCISSORS || b == Move.LIZARD) ? 1 : -1;
            case PAPER -> (b == Move.ROCK || b == Move.SPOCK) ? 1 : -1;
            case SCISSORS -> (b == Move.PAPER || b == Move.LIZARD) ? 1 : -1;
            case LIZARD -> (b == Move.SPOCK || b == Move.PAPER) ? 1 : -1;
            case SPOCK -> (b == Move.SCISSORS || b == Move.ROCK) ? 1 : -1;
        };
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        Move[] allMoves = Move.values();

        int playerScore = 0;
        int computerScore = 0;

        System.out.println("=== Rock-Paper-Scissors-Lizard-Spock (5 Rounds) ===");
        System.out.println("Valid moves: ROCK, PAPER, SCISSORS, LIZARD, SPOCK\n");

        // (c) Play 5 rounds
        for (int round = 1; round <= 5; round++) {
            System.out.println("--- Round " + round + " ---");

            Move playerMove = null;
            while (playerMove == null) {
                System.out.print("Enter your move: ");
                String input = sc.nextLine().trim().toUpperCase();
                try {
                    playerMove = Move.valueOf(input);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid move! Choose from ROCK, PAPER, SCISSORS, LIZARD, SPOCK.");
                }
            }

            Move computerMove = allMoves[random.nextInt(allMoves.length)];
            int res = winner(playerMove, computerMove);

            System.out.println("Player: " + playerMove + " | Computer: " + computerMove);

            if (res == 1) {
                System.out.println("Round result: You win this round!");
                playerScore++;
            } else if (res == -1) {
                System.out.println("Round result: Computer wins this round!");
                computerScore++;
            } else {
                System.out.println("Round result: It's a tie!");
            }
            System.out.println("Current score: You " + playerScore + " - " + computerScore + " Computer\n");
        }

        // (d) After 5 rounds, print overall winner
        System.out.println("==================================");
        if (playerScore > computerScore) {
            System.out.println("You win " + playerScore + "–" + computerScore);
        } else if (computerScore > playerScore) {
            System.out.println("Computer wins " + computerScore + "–" + playerScore);
        } else {
            System.out.println("Match tied " + playerScore + "–" + computerScore);
        }
        System.out.println("==================================");

        sc.close();
    }
}

