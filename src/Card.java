//package


//imports
  
public class Card {
    public int rank, suit;
    
    public final static int
            UNKNOWN = -1,
            KING    = 12,
            QUEEN   = 11,
            JACK    = 10,
            TEN     = 9,
            NINE    = 8,
            EIGHT   = 7,
            SEVEN   = 6,
            SIX     = 5,
            FIVE    = 4,
            FOUR    = 3,
            THREE   = 2,
            TWO     = 1,
            ACE     = 0;
    
    public static int
            HEART, DIAMOND, SPADE, CLUB;
    //Suit is not given values initially because one of the ways i want to reduce the
    //number of possibilites is by getting rid of differences in suits. (e.g. KK hearts will be
    //treated same as KK clubs)
    
    //creates unknown card
    Card() {
        rank = -1;
        suit = -1;
    }
    
    Card(int rank, int suit) {
        boolean rankValid = rank >= ACE && rank <= KING;
        boolean suitValid = suit >= -1 && suit <= 3;
        
        /*Suit has to be able to be -1, since when adding all combinations of card ranks to possible table, rank isnt included*/
        
        //sets it to be unknown but will change if the input is valid
        this.rank = -1;
        this.suit = -1;
        
        if (rankValid && suitValid) {
            //rank and suit are both valid, therefor sets rank and suit appropriately
            this.rank = rank;
            this.suit = suit;

        } else if (rankValid) {
            //rank is valid but suit is not, error message, rank and suit stay as -1
            System.err.println("Suit input for constructor: " + suit + "not valid... created unknown card.");
        } else if (suitValid) {
            //suit is valid but rank is not, error message, rank and suit stay as -1
            System.err.println("Rank input for constructor: " + suit + "not valid... created unknown card.");
        } else {
            //suit and rank are both invalid
            System.err.println("Rank and suit input for constructor: " + rank + " and " + suit + "not valid... created unknown card.");
        }
    }
    
    public boolean matches(Card c) {
        return this.rank == c.rank && this.suit == c.suit;
    }
    
//    public boolean comesBefore(Card c) {
//        //the c.rank == -1 and c.suit == -1 is redundant but is in place for any future code
//        if (this.rank == -1 && this.suit == -1)
//            return false;
//        if (c.rank == -1 && c.suit == -1)
//            return true;
//        return this.rank*4 + this.suit < c.rank*4 + c.suit;
//    }
    
    public boolean isUnknown() {
        return this.rank == -1 && this.suit == -1;
    }
    
//    public int[] orderingValues() {
//        return new int[]{rank, suit};
//    }
    
    public String stringName() {
        String[] rankNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        
        String[] suitNames = {"club", "diamond", "heart", "spade"};
        String[] suitSymbols = {"♣", "♦", "♥", "♠"};
        
        
        if (suit != -1)
            return rankNames[rank] + " of " + suitNames[suit]+"s";
        else
            return rankNames[rank] + " of unknown";
    }
        
    //returns whether a card is fully known (has a determined rank and suit)
    public boolean isFullyKnown() {
        return suit >= 0 && suit <= 12 && rank >= 0 && rank <= 12;
    }
}