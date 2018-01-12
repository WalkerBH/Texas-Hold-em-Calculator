//package


//imports

public class HoleCards extends CardGroup {
    //whether the opponent has folded or not, will never be true for the players holecards
    
    HoleCards() {
        //sets size of holeCards
        cards = new Card[2];
        
        //initializes holeCards
        cards[0] = new Card();
        cards[1] = new Card();
        
        //sets the player to be in the game
    }
    
    public int[] handValue(Table table, int suit) {
        /*
        Straight Flush:  8
        Quads:           7
        Full House:      6
        Flush:           5
        Straight:        4
        Three of a Kind: 3
        Two Pair:        2
        Pair:            1
        High Card:       0
        */
        int[] value;
        
        //combination of the table and hole cards
        CardGroup hand = new CardGroup(7);
        hand.add(this);
        hand.add(table);
        
        System.out.println("Hole Cards:");
        for (Card c : this.cards) {
            System.out.print("[" + c.rank + ", " + c.suit +"]");
        }
        System.out.println();
        
        System.out.println("Table:");
        for (Card c : table.cards) {
            System.out.print("[" + c.rank + ", " + c.suit +"]");
        }
        System.out.println();
        
        //TEMP
        for (Card c : hand.cards) {
            System.out.print("[" + c.rank + ", " + c.suit + "]");
        }
        
        int[] allRankCounts = hand.rankCount();
        
        //Straight Flush 8
        if (suit != -1) {
            value = straightFlush(hand, suit);
            if (value[0] != -1)
                return value;
        }
        
        //Quads 7
        value = quads(allRankCounts);
        if (value[0] != -1)
            return value;
        
        //Full House 6
        value = fullHouse(allRankCounts);
        if (value[0] != -1)
            return value;
        
        //Flush 5
        if (suit != -1) {
            value = flush(hand, suit);
            if (value[0] != -1)
                return value;
        }
        
        //Straight 4
        value = straight(hand);
        if (value[0] != -1)
            return value;
        
        //Three of a kind 3
        value = trips(allRankCounts);
        if (value[0] != -1)
            return value;
        
        //Two pair
        value = twoPair(allRankCounts);
        if (value[0] != -1)
            return value;
        
        //Pair
        value = pair(allRankCounts);
        if (value[0] != -1)
            return value;
        
        //High Card
        return highCard(allRankCounts);
    }
    
    //8
    private int[] straightFlush(CardGroup hand, int suit) {
        //tries to get rid of a lot of possibilities early
        if (hand.suitCount(suit) < 5)
            return new int[]{-1};

        CardGroup suited = new CardGroup(hand.suitCount(suit));
        for (Card c : hand.cards) {
            if (c.suit == suit)
                suited.add(c);
        }
        
        int[] results = straight(suited);
        
        if (results[0] == -1) {
            return new int[]{-1};
        } else {
            return new int[]{8, results[1]};
        }
    }
    
    //7
    private int[] quads(int[] rankCount) {
        int quad = -1;
        int kicker = -1;
        //[7, quadRank, kicker]
        
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 4) {
                if (i == 0)
                    quad = 13;
                else
                    quad = i;
            } else if (rankCount[i] > 0 && kicker != 13) {
                if (i == 0)
                    kicker = 13;
                else
                    kicker = i;
            }
        }
        
        if (quad == -1 || kicker == -1) {
            return new int[]{-1};
        } else {
            return new int[]{7, quad, kicker};
        }
    }
    
    //6
    private int[] fullHouse(int[] rankCount) {
        int trip = -1;
        int pair = -1;
        //[6, trips, pair]
        
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 3 && trip != 13) {
                if (i == 0)
                    trip = 13;
                else
                    trip = i;
            }
        }
        
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] >= 2 && i != trip && pair != 13 && !(i == 0 && trip == 13)) {
                if (i == 0)
                    pair = 13;
                else
                    pair = i;
            }
        }
        
        if (trip == -1 || pair == -1) {
            return new int[]{-1};
        } else {
            return new int[]{6, trip, pair};
        }
    }    
    
    //5
    private int[] flush(CardGroup hand, int suit) {
        if (hand.suitCount(suit) < 5)
            return new int[] {-1};
        
        int[] kicker = new int[5];
        
        for (int i = 0; i < hand.cards.length; i++) {
            if (hand.cardAt(i).suit == suit) {
                for (int j = 0; j < kicker.length-1; j++) {
                    if (hand.cardAt(i).rank == 0) {
                        System.arraycopy(kicker, 0, kicker, 1, kicker.length-1);
                        kicker[0] = 13;
                        break;
                    } else if (hand.cardAt(i).rank > kicker[j]) {
                        System.arraycopy(kicker, j, kicker, j+1, kicker.length-1-j);
                        kicker[j] = hand.cardAt(i).rank;
                        break;
                    }
                }
            }
        }
        return new int[] {5, kicker[0], kicker[1], kicker[2], kicker[3], kicker[4]};
    }

    //4
    private int[] straight(CardGroup hand) {
        boolean[] containsRank = new boolean[14];
        for (Card c : hand.cards) {
            if (c.rank == 0)
                containsRank[13] = true;
            
            containsRank[c.rank] = true;
        }
        
        int counter = 0;
        for (int i = 0; i < containsRank.length; i++) {
            if (containsRank[i]) {
                counter++;
            } else {
                if (counter >= 5) {
                    return new int[] {4, i-1};
                } else {
                    counter = 0;
                }
            }
            
            if (i == containsRank.length-1 && counter >= 5) {
                return new int[] {4, i};
            }
        }
        return new int[] {-1};
    }
    
    //3
    private int[] trips(int[] rankCount) {
        int trip = -1;
        int[] kicker = new int[2];
        
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 3 && trip != 13) {
                if (i == 0)
                    trip = 13;
                else
                    trip = i;
            }
        }
        
        for (int i = 0; i < rankCount.length; i++) {
            if (i != trip && !(trip == 13 && i == 0) && rankCount[i] == 1) {
                if (i == 0) {
                    kicker[1] = kicker[0];
                    kicker[0] = 13;
                } else if (i > kicker[0]) {
                    kicker[1] = kicker[0];
                    kicker[0] = i;
                } else if (i > kicker[1]) {
                    kicker[1] = i;
                }
            }
        }
        
        if (trip == -1) {
            return new int[]{-1};
        } else {
            return new int[]{3, trip, kicker[0], kicker[1]};
        }
    }
    
    //2
    private int[] twoPair(int[] rankCount) {
        int[] pair = new int[] {-1, -1};
        int kicker = -1;
        
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 2) {
                if (i == 0) {
                    pair[1] = pair[0];
                    pair[0] = 13;
                } else if (i > pair[0]) {
                    pair[1] = pair[0];
                    pair[0] = i;
                } else if (i > pair[1]) {
                    pair[1] = i;
                }
            }
        }
        
        for (int i = 0; i < rankCount.length; i++) {
            if (i != pair[0] && i != pair[1] && !(i == 0 && pair[0] == 13) && rankCount[i] > 0) {
                if (i == 0) {
                    kicker = 13;
                    break;
                } else if (i > kicker) {
                    kicker = i;
                }
            }
        }
        
        if (pair[0] == -1 || pair[1] == -1 || kicker == -1)
            return new int[]{-1};
        else
            return new int[]{2, pair[0], pair[1], kicker};
    }
    
    //1
    private int[] pair(int[] rankCount) {
        int pair = -1;
        int[] kicker = new int[3];
        
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 2) {
                if (i == 0)
                    pair = 13;
                else
                    pair = i;
                break;
            } 
        }
        
        int counter = 0;
        
        if (rankCount[0] > 0 && pair != 13)
            kicker[counter++] = 13;

        for (int i = rankCount.length-1; i >= 1; i--) {
            if (i != pair && rankCount[i] > 0) {
                kicker[counter++] = i;
            }
            if (counter == 3)
                break;
        }
        
        if (pair == -1)
            return new int[] {-1};
        else
            return new int[] {1, pair, kicker[0], kicker[1], kicker[2]};
    }
    
    //0
    private int[] highCard(int[] rankCount) {
        int[] kicker = new int[5];
        
        int counter = 0;
        if (rankCount[0] == 1)
            kicker[counter++] = 13;

        for (int i = rankCount.length-1; i >= 1; i--) {
            if (rankCount[i] == 1) {
                kicker[counter++] = i;
            }
            if (counter == 5)
                break;
        }
        
        return new int[] {0, kicker[0], kicker[1], kicker[2], kicker[3], kicker[4]};
    }
    
}
