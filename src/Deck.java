//package


//imports

public class Deck extends CardGroup {
    
    public Deck() {
        //initializes cards
        cards = new Card[52];
 
        //fills deck with all cards
        for (int rank = Card.ACE; rank <= Card.KING; rank++) {
            for (int suit = 0; suit <= 3; suit++) {
                cards[rank + 13*suit] = new Card(rank, suit);
            }
        }
    }

    
    @Override
    public void add(Card c) {
        cards[c.rank + 13*c.suit] = c;
    }
    
    @Override
    public void remove(Card c) {
        cards[c.rank + 13*c.suit] = new Card();
    }
    
    @Override
    public boolean contains(Card c) {
        return cards[c.rank + c.suit*13].isFullyKnown();
    }
    
    @Override
    public int rankCount(int rank) {
        int count = 0;
        for (int suit = 0; suit <= 3; suit++) {
            if (contains(new Card(rank, suit)))
                count++;
        }
        
        return count;
    }
    
    @Override
    public int suitCount(int suit) {
        int count = 0;
        for (int rank = Card.ACE; rank <= Card.KING; rank++) {
            if (contains(new Card(rank, suit)))
                count++;
        }
        
        return count;
    }
    
    @Override
    public int[] rankCount() {
        int[] count = new int[13];
        
        for (int rank = Card.ACE; rank <= Card.KING; rank++) {
            for (int suit = 0; suit <= 3; suit++) {
                if (contains(new Card(rank, suit)))
                    count[rank]++;
            }
        }
        return count;
    }
    
    @Override
    public int[] suitCount() {
        int[] count = new int[4];
        
        for (int suit = 0; suit <= 3; suit++) {
            for (int rank = Card.ACE; rank <= Card.KING; rank++) {
                if (contains(new Card(rank, suit)))
                    count[suit]++;
            }
        }
        return count;
    }
}
