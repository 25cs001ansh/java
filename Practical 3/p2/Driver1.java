package p2;

public class Driver1 {

    public static void main(String[] args) {

        Card[] cards = new Card[5];

        Card[] newCards = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Queen", "Diamonds"),
            new Card("Ace", "Spades"),
            new Card("Jack", "Clubs")
        };

        int count = 0;

        for (int i = 0; i < newCards.length; i++) {

            Card currentCard = newCards[i];

            boolean duplicateFound = false;

            for (int j = 0; j < count; j++) {

                if (currentCard.equals(cards[j])) {

                    System.out.println(
                        "Duplicate found: " + currentCard
                    );

                    duplicateFound = true;
                    break;
                }
            }

            if (duplicateFound) {
                break;
            }

            cards[count] = currentCard;
            count++;
        }
    }
}