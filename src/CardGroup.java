//package


//imports

/*
Checking for whether the card to be added/removed to the group is contained in
the group already is left for the subclasses, since the contains(Card) method
will vary across subclasses (i.e. Deck has a more efficient method than going
though all the cards).
*/

public class CardGroup {

    public Card[] cards;
    
    public CardGroup() {
    
    }
    
    public CardGroup(int length) {
        //creates card array with specified length
        cards = new Card[length];
        
        //initializes all of the cards
        for (int i = 0; i < cards.length; i++) {
            //fills array with unknown cards
            cards[i] = new Card();
        }
    }


    //adds all known or partially known cards in a specified cardGroup
    public void add(CardGroup cg) {
        for (int i = 0; i < cg.length(); i++) {
            if (!cg.cards[i].isUnknown()) {
                this.add(cg.cards[i]);
            }
        }
    }
    
    //replaces first unknown card with a specified card
    public void add(Card c) {
        for (int i = 0; i < this.length(); i++) {
            if (this.cards[i].isUnknown()) {
                this.cards[i] = new Card(c.rank, c.suit);
                break;
            }
        }
    }
    
    //removes a specified card and shifts cards to close gap created
    public void remove(Card c) {
        for (int i = 0; i < length(); i++) {
            if (c.matches(cards[i]))
                remove(i);
        }
    }
    
    //removes a card a specified index and shifts the cards to close the gap created
    public void remove(int index) {
        System.arraycopy(cards, index+1, cards, index, length()-index - 1);
        cards[length()-1] = new Card();
    }
    
    //sets a card at a particular index
    public void setCard(Card c, int index) {
        cards[index] = c;
    }
    
    //returns the number of cards that are known (have a determined suit and rank)
    public int numKnownCards() {
        int count = 0;
        for (Card c : cards) if (c.isFullyKnown()) count++;
        return count;
    }
    
    //returns an the number of occurances of a specified rank
    public int rankCount(int rank) {
        int count = 0;
        for (Card c : cards) if (c.rank == rank) count++;
        return count;
    }
    
    //returns an the number of occurances of a specified suit
    public int suitCount(int suit) {
        int count = 0;
        for (Card card : cards) if (card.suit == suit) count++;
        return count;
    }
    
    //returns an array containing the number of occurances of rank == index
    public int[] rankCount() {
        int[] output = new int[13];
        
        for (int i = 0; i < cards.length; i++) {
            //TEMP
            //System.out.println(cards[i].rank);
            
            output[cards[i].rank]++;
        }
            
        
        return output;
    }
    
    //returns an array containing the number of occurances of suit == index
    public int[] suitCount() {
        int[] output = new int[13];
        
        for (Card c : cards)
            output[c.rank]++;
        
        return output;
    }

    //returns the number of cards
    public int length() {
        return cards.length;
    }
    
    //returns the card at a specified index
    public Card cardAt(int index) {
        return cards[index];
    }
    
    //returns whether or not a card is in the cardgroup
    public boolean contains(Card c) {
        //if c is unknown
        if (c.isUnknown())
            return numKnownCards() < length();
        
        for (Card card : cards) {
            if (card.matches(c)) {
                return true;
            }
        }
        return false;
    }
    
    public void empty() {
        cards = new Card[cards.length];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card();
        }
    }    
    
    
    
//    //Not used
//    public void sort() {
//        //Creates an array of cardgroups and separates all the cards into their own cardGroup
//        CardGroup[] cgArray = new CardGroup[cards.length];
//        for (int i = 0; i < cgArray.length; i++) {
//            cgArray[i] = new CardGroup(1);
//            cgArray[i].add(cards[i]);
//        }
//        
//        while (true) {
//            //breaks the loop after the cardGroups have all merged
//            if (cgArray.length == 1) {
//                //sets this cardGroup to the sorted one
//                this.cards = cgArray[0].cards;
//                break;
//            }
//            
//            //create the outline for the next generation of the cardGroup array.
//            /*
//            the array will be half the length of the orginal length however, if the 
//            number of groups of the inital array is odd, include the remaining group
//            as a group of its own (e.g. 4 -> 2, and 5 -> 3) hence the + 1 to length
//            since java rounds down when dividing integers. ([4 + 1]/2 = 5/2 = 2) so the
//            rule still works for even numbers
//            */
//            CardGroup[] nextCgArray = new CardGroup[(cgArray.length + 1)/2];
//            
//            for (int i = 0; i < cgArray.length; i += 2) {
//                //in the case that this is the last group to be created and is the one that isnt going to be merged
//                if (i == cgArray.length - 1 && cgArray.length % 2 == 1) {
//                    //just set it to the cardGroup (no need for merging)
//                    nextCgArray[(i + 1)/2] = cgArray[i];
//                    continue;
//                }
//
//                //sets the length of the card group in the next gen to be the sum of
//                //the lengths of the cardGroups about to be merged
//                nextCgArray[(i + 1)/2] = new CardGroup(cgArray[i].cards.length + cgArray[i+1].cards.length);
//
//                //going to go through all the cards in the cardgroups about to be merged
//                for (int j = 0; j < cgArray[i].cards.length + cgArray[i+1].cards.length; j++) {
//                     if (cgArray[i].cards[0].comesBefore(cgArray[i+1].cards[0])) {
//                        //current smallest card is in first cardGroup
//
//                        //adds card to cardGroup in new array
//                        nextCgArray[(i + 1)/2].add(cgArray[i].cards[0]);
//                        
//                        //removes it in current array so that it isn't repeated
//                        if (!cgArray[i].cards[0].isUnknown())
//                            cgArray[i].remove(0);
//                    } else {
//                        //current smallest card is in second cardGroup
//
//                        //adds card to cardGroup in new array
//                        nextCgArray[(i + 1)/2].add(cgArray[i+1].cards[0]);
//                        //removes it in current carray so that it isn't repeated
//                        if (!cgArray[i+1].cards[0].isUnknown())
//                            cgArray[i+1].remove(0);
//                    }
//                }
//            }
//            
//            //sets up for the next merge
//            cgArray = nextCgArray;
//        }
//    }

}