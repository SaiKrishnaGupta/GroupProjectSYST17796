package com.mycompany.unogame.Card;

import java.util.Random;

public class Deck {
    private int noOfCards = 116;
    private Object[][] deck = new Object[noOfCards][2];
    private int lastCard = this.noOfCards - 1;
    
    public Deck() {
        this.deck = new Object[this.noOfCards][2];
    }

    public void fillDeck() {
        int lightCardNo = 0;
        int darkCardNo = 0;
        for (int j = 0; j < 2; j++) {
            for (int i = 0, DSC = DarkSide.Color.values().length - 1; i < LightSide.Color.values().length; i++, DSC--) {

                if ((i == 0) || (i == 1)) {
                    this.deck[lightCardNo][0] = new LightSide(i);
                    lightCardNo++;
                } else {
                    for (int k = 1; k < LightSide.values.values().length; k++) {
                        this.deck[lightCardNo][0] = new LightSide(i, k);
                        lightCardNo++;
                    }
                }
                if ((DSC == 0) || (DSC == 1)) {
                    this.deck[darkCardNo][1] = new DarkSide(DSC);
                    darkCardNo++;
                } else {
                    for (int k = 1; k < DarkSide.values.values().length; k++) {
                        this.deck[darkCardNo][1] = new DarkSide(DSC, k);
                        darkCardNo++;
                    }
                }
            }
        }
        this.shuffleDeck();
    }


    public void shuffleDeck() {
        Object[][] bufferDeck = new Object[this.noOfCards][2];
        int workingCard = 0;
        Random random = new Random();
        while (workingCard < this.noOfCards) {
            int card = random.nextInt(this.noOfCards);
            if ((this.deck[card][0] != null) && (this.deck[card][1] != null)) {
                bufferDeck[workingCard][0] = this.deck[card][0];
                bufferDeck[workingCard][1] = this.deck[card][1];
                workingCard++;
                this.deck[card] = new Object[2];
            }
        }
        this.deck = bufferDeck;
    }

    public void nullsAtLast() {
        for (int i = 0; i < this.noOfCards; i++) {
            for (int j = 0; j < this.noOfCards; j++) {
                if ((this.deck[j][0] == null) && (this.deck[j][1] == null)) {
                    for (int k = j; k < this.noOfCards; k++) {
                        if ((k + 1) != 116) {
                            this.deck[k] = this.deck[k + 1];
                        }
                    }
                    this.deck[this.noOfCards - 1][0] = null;
                    this.deck[this.noOfCards - 1][1] = null;
                }
            }
        }
    }


    public Object[] popCard(int index) {
        Object[] card = new Object[2];
        card[0] = this.deck[index][0];
        card[1] = this.deck[index][1];
        this.deck[index][0] = null;
        this.deck[index][1] = null;
        this.nullsAtLast();
        return card;
    }

    public int getNoOfCards() {
        return noOfCards;
    }

    public Object[][] getDeck() {
        return deck;
    }

    public int getLastCard() {
        this.nullsAtLast();
        for (int i = 0; i < this.noOfCards; i++) {
            if ((this.deck[i][0] == null) && (this.deck[i][1] == null)) {
                this.lastCard = i - 1;
                break;
            } else {
                this.lastCard = this.noOfCards - 1;
            }
        }
        return lastCard;
    }

    public void printDeck() {
        for (int i = 0; i < this.getLastCard() + 1; i++) {
            System.out.println("Card: " + (i + 1));
            System.out.println("    " + this.deck[i][0]);
            System.out.println("    " + this.deck[i][1]);
        }
        if (this.getLastCard()==0){
            System.out.println("Card: 1");
            System.out.println("    " + this.deck[0][0]);
            System.out.println("    " + this.deck[0][1]);
        }
    }

    public void setDeck(Object[][] deck) {
        this.deck = deck;
    }
    public Object[] getCard(int index){
        return this.deck[index];
    }
}
