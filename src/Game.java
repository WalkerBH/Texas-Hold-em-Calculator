//package


//imports
import CustomUtilities.*;

public class Game {
    public Deck  deck  = new Deck();
    public Table table = new Table();
    public HoleCards user = new HoleCards();
    public HoleCards opponent = new HoleCards();
    
    Game() {
        
    }
    
    
    //returns the odds of winning
    public double odds() {
        
        //if the table is filled with known cards
        if (table.numKnownCards() == 5) {
            
            int[] suitCount = table.suitCount();
            int posblFlushSuit = -1;
            for (int i = 0; i < suitCount.length; i++) {
                if (suitCount[i] >= 3)
                    posblFlushSuit = i;
            }
           return odds_forFilledTable(table, deck, new CardGroup(), posblFlushSuit);
        }
        
        
        //number of combinations of filling up the table cant use nCr because the factorials are too large
        int numCombos = 0;

        //cumulative chances of winning 
        double cumOdds = 0;
        
        
        //create a cardgroup to represent the possible cards to add to the table
        CardGroup cardsToAdd = new CardGroup(5 - table.numKnownCards());
        
        
        //initializes the card group, starting at the lowest possible ranks (0, 0, 0,...)
        for (int i = 0; i < cardsToAdd.length(); i++) {
            cardsToAdd.add(new Card(0, -1));
        }
        
        int[] deckRankCount = deck.rankCount();
        
        //go through each possible combination of ranks
        while (cardsToAdd != null) {
            //boolean to store whether the combination of ranks is possible
            boolean possible = true;
            
            //rank counts for cards to add
            int[] cardsToAddRankCount = cardsToAdd.rankCount();
            
            //checks if the combination is possible
            for (int i = Card.ACE; i < Card.KING; i++) {
                //if cardsToAdd asks for more cards then there are, its impossible
                if (cardsToAddRankCount[i] > deckRankCount[i]) {
                    possible = false;
                    break;
                }
            }
            
            //if its possible, find the chance of winning with this rank combination
            if (possible) {
                Object[] results = odds_forRankCombo(cardsToAdd);
                numCombos += (int) results[0];
                cumOdds += (double) results[1];
            }
            
            //go to next iteration
            cardsToAdd = odds_nextCombo(cardsToAdd);
            //TEMP
        }
        
        //this works on the premise of all of the combinations being equally likely
        //(since literally every possible combination of rank and suit is accounted for)
        return cumOdds/numCombos;
    }
    
    private CardGroup odds_nextCombo(CardGroup cg) {
        //last case: last card is 12, there is no way to increase
        if (cg.cardAt(cg.length()-1).rank == 12) {
            System.out.println("TEMP - return null");
            return null;
        }
        
        //Create next iteration
        for (int i = 0; i < cg.length(); i++) {
            //continues if value is 12
            //else it increases the value and all previous values
            if (cg.cardAt(i).rank != 12) {
                //goes to the start and sets value of all previous to its increased value
                //e.g. {12, 12, 12, 4, 0} -> {5, 5, 5, 5, 0}
                for (int j = 0; j <= i; j++) {
                    cg.cardAt(j).rank = cg.cardAt(i).rank + 1; 
                }
                //exits
                break;
            }
        }
        return cg;
    }
    
    private HoleCards odds_nextCombo(HoleCards cg) {
        //last case: last card is 12, there is no way to increase
        if (cg.cardAt(cg.length()-1).rank == 12) {
            return null;
        }
        
        //Create next iteration
        for (int i = 0; i < cg.length(); i++) {
            //continues if value is 12
            //else it increases the value and all previous values
            if (cg.cardAt(i).rank != 12) {
                //goes to the start and sets value of all previous to its increased value
                //e.g. {12, 12, 12, 4, 0} -> {5, 5, 5, 5, 0}
                for (int j = 0; j <= i; j++) {
                    cg.cardAt(j).rank = cg.cardAt(i).rank + 1; 
                    cg.cardAt(j).suit = -1;
                }
                //exits
                break;
            }
        }
        return cg;
    }
    
    private Object[] odds_forRankCombo(CardGroup cg) {
        CardGroup cardsToAdd = new CardGroup(cg.length());
        cardsToAdd.add(cg);
        
        
        //TEMP
        System.out.println(cardsToAdd.cards[0].rank);
        //the total number of combinations in this iteration
        //found by multiplying by self, so starts at 1
        int rankCombo_numCombos = 1;
        double rankCombo_cumOdds = 0;
        
        //array to store the number of cards of each rank in cardsToAdd
        //initialized as 0,0,0,... automatically
        int[] cardsToAddRankCount = cardsToAdd.rankCount();
        
        //creates a new deck to represent the deck in this rank combo
        Deck posblDeck = new Deck();
        for (int i = 0; i < posblDeck.length(); i++) {
            if (deck.cards[i].isUnknown()){
                posblDeck.setCard(new Card(), i);
            }
        }

        //creates a new table to represent the table in this rank combo
        Table posblTable = new Table();
        posblTable.add(table);
        
        //TEMP
        for (Card c : posblTable.cards) {
            System.out.print("[" + c.rank + ", " + c.suit + "]");
        }
        System.out.println();
        
        
        //cardsToAdd length may change
        int newLength = cardsToAdd.length();
        
        //go through ace to king
        for (int rank = Card.ACE; rank <= Card.KING; rank++) {
            //if there is a card of this rank and the number of cards needed of this rank is the same as the number available...
            if (cardsToAddRankCount[rank] != 0 && cardsToAddRankCount[rank] == posblDeck.rankCount(rank)) {
                //go through each suit
                for (int suit = 0; suit <= 3; suit++) {
                    //if the deck contains this rank and suit
                    if (posblDeck.contains(new Card(rank, suit))) {
                        //moves it from deck to table
                        posblTable.add(new Card(rank, suit));
                        posblDeck.remove(new Card(rank, suit));
                        
                        //doesn't need to add it to the table anymore
                        cardsToAdd.remove(new Card(rank, -1));
                        //cardsToAdd shrinks
                        newLength--;
                        //RankCount drops
                        cardsToAddRankCount[rank]--;
                        
                        //TEMP
                        System.out.println("KJ");
                        
                    }
                }
                                
                //resizes cardsToAdd array
                System.arraycopy(cardsToAdd.cards, 0, cardsToAdd.cards = new Card[newLength], 0, newLength);
                
            } else if (cardsToAddRankCount[rank] != 0) {
                //from the cards available, choose the number of 
                rankCombo_numCombos *= CustomMath.C(deck.rankCount(rank), cardsToAddRankCount[rank]);
            } 
        }
        
        int rankCombo_combosWPosblFlush = 0;
        
        //go through all suits
        //SCard represents a card where suit = s
        for (int s = 0; s <= 3; s++) {
            //<editor-fold>
            
            
            //the number of combinations that contain a possible flush are the number of combinations with 3 or more of a suit.
            //the number of cards (that have suit s) needed in the cards that have been added to the existing table in order for there to be a possible flush                
            int numSCardsNeeded = 3 - table.suitCount(s);

            //if the counter doesnt have enough cards
            if (numSCardsNeeded > cardsToAdd.length()) {
                continue;
            }
            
            //indexCanBeSuitI stores the indexes for the cards that are available in suit "i", indexCantBeSuitI stores all the other indexes
            //initiall at the largest possible value, but will change it to fit the data later
            int[] indexOfPossibleSCards = new int[cardsToAdd.length()];
            //variable that will go up as the indexCanBeSuitI is filled to idetify where to put the variable in the array
            int counter2 = 0;
            
            //</editor-fold>
            
            //fills the indexCanBeSuit
            for (int j = 0; j < cardsToAdd.length(); j++) {
                //makes sure that A: the card is available in suit i and B: the card isnt a duplicate (cant have two aces of hearts)
                if (deck.contains(new Card(cardsToAdd.cardAt(j).rank, s)) && (j == 0 || !cardsToAdd.cardAt(j-1).matches(cardsToAdd.cardAt(j)))){
                    indexOfPossibleSCards[counter2++] = j;
                }
            }
            
            //Cut indexCanBeSuitI to proper length
            System.arraycopy(indexOfPossibleSCards, 0, indexOfPossibleSCards = new int[counter2], 0, counter2);
                        
            //will create all combinations of 1s and 0s that is the length of the number of cards that can be suit 'i'
            for (int j = 0; j < Math.pow(2, indexOfPossibleSCards.length); j++) {
                //<editor-fold>
                //same as normal rankcounts, but specifically for the non-SCards
                int[] specificRankCount = new int[13];
                System.arraycopy(cardsToAddRankCount, 0, specificRankCount, 0, 13);
                
                String binaryStr = Integer.toBinaryString(j);
                //makes sure that the preceeding 0's are included to make the length equal to the number of cards that can be suit 'i'
                while (binaryStr.length() < indexOfPossibleSCards.length) {
                    binaryStr = "0" + binaryStr;
                }
                //the length after getting rid of 0s will give the number of 'true'/'1' values, which must be >= numCardsOfSuitINeeded
                if (binaryStr.replace("0", "").length() >= numSCardsNeeded) {
                    //goes through all of the cards that can be SCards
                    //</editor-fold>
                    for (int k = 0; k < indexOfPossibleSCards.length; k++) {
                        //<editor-fold>
                        //sets them to SCards if the combination calls for it;
                        if (binaryStr.charAt(k) == '1') {
                            cardsToAdd.cardAt(indexOfPossibleSCards[k]).suit = s;
                            specificRankCount[cardsToAdd.cardAt(indexOfPossibleSCards[k]).rank]--;
                        } else {
                            cardsToAdd.cardAt(indexOfPossibleSCards[k]).suit = -1;
                        }
                        //</editor-fold>
                    }
                    //<editor-fold>
                    //number of combinations of other suits for this combination
                    //of suits (it gets kind of confusing)
                    //starts at one since it is obtained throough multiplication
                    int numWaysThisCanHappen = 1;
                    //</editor-fold>
                    //goes through each rank
                    for (int k = 0; k < specificRankCount.length; k++) {
                        //<editor-fold>
                        /*if the deck has a card of this rank and suit it means
                        that the combination (1s and 0s thing above) did not
                        select it to be an SCard, so it as an SCard should not
                        be included in the possibilities. Hence the -1 to
                        possible suits the card can be*/ 
                        if (posblDeck.contains(new Card(k, s))) {
                            /*possibleDeck.rankCount(k)-1 will never be 0, since
                            then it wouldve been added to the possible table
                            earlier when taking into account that some cards in
                            cardToBeAdded can only be one particular suit*/
                            numWaysThisCanHappen *= CustomMath.C(posblDeck.rankCount(k)-1, specificRankCount[k]);
                        } else {
                            numWaysThisCanHappen *= CustomMath.C(posblDeck.rankCount(k), specificRankCount[k]);
                        }
                        //</editor-fold>
                    }

                    rankCombo_combosWPosblFlush += numWaysThisCanHappen;
                    if (numWaysThisCanHappen != 0) {
                        rankCombo_cumOdds += numWaysThisCanHappen * odds_forFilledTable(posblTable, posblDeck, cardsToAdd, s); 
                    }
                    
                }
                
            }

        }
        
        for (Card c : cardsToAdd.cards) {
            c.suit = -1;
        }
        
        if (rankCombo_numCombos-rankCombo_combosWPosblFlush != 0) {
            //TEMP
            //System.out.print(rankCombo_numCombos-rankCombo_combosWPosblFlush);            
            
            //TEMP
            //System.out.println(cardsToAdd.cards[0].rank);
            
            rankCombo_cumOdds += (rankCombo_numCombos-rankCombo_combosWPosblFlush) * odds_forFilledTable(posblTable, posblDeck, cardsToAdd, -1);
        }
        
        return new Object[] {rankCombo_numCombos, rankCombo_cumOdds};
    }
    
    public void addToTable(Card c, int cardIndex) {
        add(c, table, cardIndex);
    }
    
    public void removeFromTable(int index) {
        deck.add(table.cardAt(index));
        table.remove(index);
    }
    
    public void addToUser(Card c, int cardIndex) {
        add(c, user, cardIndex);
    }
    
    public void removeFromUser(int index) {
        //TEMP
        System.out.println(user.cardAt(index).stringName());
                
        deck.add(user.cardAt(index));
        user.remove(index);
    }
    
    public void addToOpponent(Card c, int cardIndex) {
        add(c, opponent, cardIndex);
    }
    
    public void removeFromOpponent(int index) {
        deck.add(opponent.cardAt(index));
        opponent.remove(index);
    }
    
    private boolean add(Card c, CardGroup cg, int cardIndex) {
        //Ensure that card to be added is not unknown
        if (c.isUnknown()) {
            System.out.println("Error in Game.add(Card, CardGroup, int): tried to add unknonwn card");
            return false;
        }
        
        //Ensure card is in deck before being added
        if (!deck.contains(c)) {
            System.out.println("Error in Game.add(Card, CardGroup, int): tried to add card not in deck");
            return false;
        }
        
        //Ensure that cardIndex is not out of bounds
        if (cardIndex >= cg.length()) {
            System.out.println("Error in Game.add(Card, CardGroup, int): cardIndex(" + cardIndex + ") out of bounds");
            return false;
        }
        
        //if there is a card in the spot where the card is about to be added, the card currently in the position will be placed in the deck
        if (!cg.cardAt(cardIndex).isUnknown()) {
            deck.add(cg.cardAt(cardIndex));
        }
        
        cg.setCard(c, cardIndex);
        deck.remove(c);
        return true;
    }
        
    private double odds_forFilledTable(Table posblTable, Deck posblDeck, CardGroup cardsToAdd, int suit) {
        Table t = new Table();
        t.add(posblTable);
        
        Deck d = new Deck();
        System.arraycopy(posblDeck.cards, 0, d.cards, 0, 52);
        
        if (cardsToAdd.cards != null && cardsToAdd.length() != 0) {
            t.add(cardsToAdd);
            for (Card c : cardsToAdd.cards) {
                if (c.suit == suit && suit != -1) {
                    d.remove(c);
                } else {
                    for (int i = 0; i <= 3; i++) {
                        if (i != suit && d.contains(new Card(c.rank, i))) {
                            d.remove(new Card(c.rank, i));
                            break;
                        }
                    }
                }
            }
        }

        int[] usrHandValue = user.handValue(t, suit);        
        
        if (opponent.numKnownCards() == 2 &&hand1Wins(usrHandValue, opponent.handValue(t, suit)))
            return 100d;
        else if (opponent.numKnownCards() == 2)
            return 0d;
        
        HoleCards posblOpntCards = new HoleCards();
        posblOpntCards.add(new Card(0, -1));
        posblOpntCards.add(new Card(0, -1));
        
        int[] dRankCounts = d.rankCount();
        
        int numWinningHands = 0;
        int numHands = 0;
        
        while (posblOpntCards != null) {
            if (posblOpntCards.cardAt(0).rank == posblOpntCards.cardAt(1).rank)
                numHands += CustomMath.C(dRankCounts[posblOpntCards.cardAt(0).rank], 2);
            else
                numHands += dRankCounts[posblOpntCards.cardAt(0).rank] * dRankCounts[posblOpntCards.cardAt(1).rank];
            
            
            if (suit != -1) {
                if ( //card ranks are same
                        posblOpntCards.cardAt(0).rank == posblOpntCards.cardAt(1).rank
                        && d.contains(new Card(posblOpntCards.cardAt(0).rank, suit))
                        && dRankCounts[posblOpntCards.cardAt(1).rank] > 1
                ) {
                    //one suited
                    posblOpntCards.cards[0].suit = suit;
                    posblOpntCards.cards[1].suit = -1;
                    if (hand1Wins(posblOpntCards.handValue(t, suit), usrHandValue)) {
                        numWinningHands += dRankCounts[posblOpntCards.cardAt(0).rank]-1;
                    }
                } else { //card ranks are different
                    //only first one suited
                    if (
                            d.contains(new Card(posblOpntCards.cardAt(0).rank, suit))
                            && dRankCounts[posblOpntCards.cardAt(1).rank] > 0
                    ) {
                        posblOpntCards.cards[0].suit = suit;
                        posblOpntCards.cards[1].suit = -1;
                        if (hand1Wins(posblOpntCards.handValue(t, suit), usrHandValue)) {
                            numWinningHands += dRankCounts[posblOpntCards.cardAt(1).rank];
                            //removes possibility of inculding both suited case
                            if (d.contains(new Card(posblOpntCards.cardAt(1).rank, suit))) {
                                numWinningHands--;
                            }
                        }
                    }
                    
                    //only second one suited
                    if (
                            d.contains(new Card(posblOpntCards.cardAt(1).rank, suit))
                            && dRankCounts[posblOpntCards.cardAt(0).rank] > 0
                    ) {
                        posblOpntCards.cards[0].suit = -1;
                        posblOpntCards.cards[1].suit = suit;
                        if (hand1Wins(posblOpntCards.handValue(t, suit), usrHandValue)) {
                            numWinningHands += dRankCounts[posblOpntCards.cardAt(0).rank];
                            //removes possibility of inculding both suited case
                            if (d.contains(new Card(posblOpntCards.cardAt(0).rank, suit))) {
                                numWinningHands--;
                            }
                        }
                    }
                    
                    //both suited
                    if (
                        d.contains(new Card(posblOpntCards.cardAt(0).rank, suit))
                        && d.contains(new Card(posblOpntCards.cardAt(1).rank, suit))
                    ) {
                        posblOpntCards.cards[0].suit = suit;
                        posblOpntCards.cards[1].suit = suit;
                        if (hand1Wins(posblOpntCards.handValue(t, suit), usrHandValue)) {
                            numWinningHands += 1;
                        }
                    }
                }
            }
            posblOpntCards.cards[0].suit = -1;
            posblOpntCards.cards[1].suit = -1;
            //none are suited
            if (posblOpntCards.cardAt(0).rank == posblOpntCards.cardAt(1).rank) { //same ranks
                int cardsAvail = dRankCounts[posblOpntCards.cardAt(0).rank];
                if (suit != -1 && d.contains(new Card(posblOpntCards.cardAt(0).rank, suit)))
                    cardsAvail--;
                if (cardsAvail >= 2) {
                    if (hand1Wins(posblOpntCards.handValue(t, suit), usrHandValue)) {
                        numWinningHands += CustomMath.C(cardsAvail, 2);
                    }
                }
            } else {  //different ranks
                int cardsAvailA = dRankCounts[posblOpntCards.cardAt(0).rank];
                int cardsAvailB = dRankCounts[posblOpntCards.cardAt(1).rank];
                if (suit != -1 && d.contains(new Card(posblOpntCards.cardAt(0).rank, suit)))
                    cardsAvailA--;
                if (suit != -1 && d.contains(new Card(posblOpntCards.cardAt(1).rank, suit)))
                    cardsAvailB--;
                if (cardsAvailA >= 1 && cardsAvailB >= 1) {
                    if (hand1Wins(posblOpntCards.handValue(t, suit), usrHandValue)) {
                        numWinningHands += cardsAvailA * cardsAvailB;
                    }
                }
            }
            posblOpntCards = (HoleCards) odds_nextCombo(posblOpntCards);
        }
                
        return 100d-((double)numWinningHands/(double)numHands *100d);
    }
    
    private boolean hand1Wins(int[] handValue1, int[] handValue2) {
        for (int i = 0; i < handValue1.length; i++) {
            if (handValue1[i] > handValue2[i]) {
                //win
                return true;
            } else if (handValue2[i] > handValue1[i]) {
                //lose
                return false;
            }
        }
        //chop (still counts as not winning)
        return false;
    }
}