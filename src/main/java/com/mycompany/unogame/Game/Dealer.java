package com.mycompany.unogame.Game;

import java.util.Scanner;

import com.mycompany.unogame.Card.Deck;
import com.mycompany.unogame.Card.LightSide;
import com.mycompany.unogame.Card.DarkSide;
import java.util.Arrays;

public class Dealer {

    private int noOfPlayers;
    private static Scanner input = new Scanner(System.in);
    private String[] names;
    private int[] order;
    private Deck[] hands;
    private int noOfStartingCards = 7;
    private Deck deck;
    private Object[] startingCard;
    private Deck discardedDeck;
    private int currentSide = 0;
    private Object[] bufferCard = new Object[2];

    public Dealer(Deck deck) {
        this.deck = deck;
        this.discardedDeck = new Deck();
    }

    public void fixPlayers() {
        System.out.println("Enter No. of Players: ");
        while (true) {
            this.noOfPlayers = input.nextInt();
            if ((this.noOfPlayers < 2) || (this.noOfPlayers > 8)) {
                System.out.println("This is a 2-8 player Game\nEnter No. of Players: ");
            } else {
                input.nextLine();
                break;
            }

        }
        this.names = new String[this.noOfPlayers];
        this.order = new int[this.noOfPlayers];
        for (int i = 0; i < this.noOfPlayers; i++) {
            this.order[i] = i;
            System.out.println("Enter Name of Player" + (i + 1));
            names[i] = input.nextLine();
        }
    }

    public void distributeCards() {
        this.hands = new Deck[this.noOfPlayers];
        for (int i = 0; i < this.noOfPlayers; i++) {
            this.hands[i] = new Deck();
            for (int j = 0; j < this.noOfStartingCards; j++) {
                this.hands[i].getDeck()[this.hands[i].getLastCard() + 1] = this.deck.popCard(0);
            }
        }
        this.discardedDeck.getDeck()[0] = (this.deck.popCard(0));
        while (((LightSide) (this.discardedDeck.getCard(0)[0])).getValue() == LightSide.values.NONE) {
            this.deck.getDeck()[this.deck.getLastCard() + 1] = this.discardedDeck.popCard(0);
            this.discardedDeck.getDeck()[0] = (this.deck.popCard(0));
        }
    }

    public void reverse() {
        if (this.noOfPlayers != 2) {
            int[] bufferOrder = new int[this.noOfPlayers];
            for (int i = this.noOfPlayers - 1, j = 0; i >= 0; i--, j++) {
                bufferOrder[i] = this.order[j];
            }
            this.order = bufferOrder;
        } else {
            int buffer = this.order[0];
            this.order[0] = this.order[1];
            this.order[1] = buffer;
        }
    }

    public void flip() {
        this.currentSide = (this.currentSide == 0) ? 1 : 0;
    }

    public Deck getDeck() {
        return deck;
    }

    public Deck[] getHands() {
        return hands;
    }

    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    public String[] getNames() {
        return names;
    }

    public void printStartingCard() {
        this.startingCard = this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()];
        System.out.println("========================================================================================");
        System.out.println("Top Card");
        System.out.println(this.startingCard[this.currentSide]);
        System.out.println("========================================================================================");
    }

    public int getCurrentSide() {
        return currentSide;
    }

    public int nextTurn(int turn) {
        int newTurn;
        int newTurnIndex = (Arrays.binarySearch(this.order, turn)) + 1;
        System.out.println(newTurnIndex);
        newTurnIndex = (newTurnIndex < this.noOfPlayers) ? newTurnIndex : 0;
        newTurn = this.order[newTurnIndex];
        return newTurn;
    }

    public int cardsInHand(int turn) {
        return hands[turn].getLastCard();
    }

    public int lightSidePlay(int turn, int cardPlayed, String calledUno) {
        boolean condition;
        LightSide cardSelected = (LightSide) hands[turn].getCard(cardPlayed)[0];
        condition = ((cardSelected.getColor() == ((LightSide) (this.startingCard[0])).getColor())
                || (cardSelected.getValue() == ((LightSide) (this.startingCard[0])).getValue())
                || (cardSelected.getValue() == (LightSide.values.NONE)));
        if (condition) {
            if (!((calledUno.toLowerCase().equals("UNO".toLowerCase())) || (calledUno.toLowerCase().equals(" UNO".toLowerCase())))) {
                for (int i = 0; i < 5; i++) {
                    this.drawCard(turn);
                }
            }
            this.discardedDeck.getDeck()[this.discardedDeck.getLastCard() + 1] = (hands[turn].popCard(cardPlayed));
            if (cardSelected.getValue() == LightSide.values.DRAW_ONE) {
                turn = this.nextTurn(turn);
                System.out.println(this.names[turn]);
                System.out.println("Draw one Card from Deck");
                this.hands[turn].getDeck()[this.hands[turn].getLastCard() + 1] = (this.deck.popCard(0));
                System.out.println("Turn Skipped.");
            } else if (cardSelected.getValue() == LightSide.values.FLIP) {
                this.flip();

            } else if (cardSelected.getValue() == LightSide.values.REVERSE) {
                this.reverse();
                if (this.noOfPlayers == 2) {
                    turn = this.nextTurn(turn);
                }

            } else if (cardSelected.getValue() == LightSide.values.SKIP) {
                turn = this.nextTurn(turn);
            } else if (cardSelected.getValue() == LightSide.values.NONE) {
                if (cardSelected.getColor() == LightSide.Color.WILD) {
                    System.out.println("Which Color do you want to proceed with? ");
                    for (int i = 2; i < 6; i++) {
                        System.out.println(i - 1 + ": " + (LightSide.Color.values()[i]));
                    }
                    int selectedColor;
                    do {
                        selectedColor = input.nextInt();
                    } while ((selectedColor >= 5 || selectedColor <= 0));
                    ((LightSide) (this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()][0]))
                            .setColor((LightSide.Color.values()[selectedColor + 1]));
                } else if (cardSelected.getColor() == LightSide.Color.WILD_DRAW_TWO) {
                    int newTurn = this.nextTurn(turn);
                    System.out.println(this.names[newTurn] + " do you want to challenge " + this.names[turn] + "\nY/N");
                    char answer;
                    do {
                        answer = input.next().charAt(0);
                    } while (!(answer == 'Y' || answer == 'N' || answer == 'y' || answer == 'n'));
                    int validChallenge = newTurn;
                    if (answer == 'Y' || answer == 'y') {
                        for (int i = 0; i < this.hands[turn].getLastCard() + 1; i++) {
                            if (((LightSide) (this.hands[turn].getCard(i)[0]))
                                    .getColor() == ((LightSide) (this.startingCard[0])).getColor()) {
                                validChallenge = turn;
                                break;
                            }
                        }
                        for (int i = 0; i < 4; i++) {
                            this.hands[validChallenge].getDeck()[this.hands[validChallenge].getLastCard()
                                    + 1] = this.deck.popCard(0);
                        }
                        if (validChallenge == newTurn) {
                            turn = this.nextTurn(turn);
                            System.out.println("Which Color do you want to proceed with? ");
                            for (int i = 2; i < 6; i++) {
                                System.out.println(i - 1 + ": " + (LightSide.Color.values()[i]));
                            }
                            int selectedColor;
                            do {
                                selectedColor = input.nextInt();
                            } while ((selectedColor >= 5 || selectedColor <= 0));
                            ((LightSide) (this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()][0]))
                                    .setColor((LightSide.Color.values()[selectedColor + 1]));
                        } else if (validChallenge == turn) {
                            this.bufferCard = this.discardedDeck.getCard(this.discardedDeck.getLastCard());
                            this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()] = this.discardedDeck
                                    .getCard(this.discardedDeck.getLastCard() - 1);
                            this.discardedDeck.getDeck()[this.discardedDeck.getLastCard() - 1] = this.bufferCard;
                        }
                    } else {
                        for (int i = 0; i < 2; i++) {
                            turn = this.nextTurn(turn);
                            this.hands[turn].getDeck()[this.hands[turn].getLastCard() + 1] = this.deck.popCard(0);
                        }
                    }
                }
            }
            turn = this.nextTurn(turn);
            return turn;
        } else {
            System.out.println("Card Not Matching. ");
            return turn;
        }
    }

    public int lightSidePlay(int turn) {
        if (((LightSide) (this.startingCard[0])).getValue() == LightSide.values.DRAW_ONE) {
            System.out.println(this.names[turn]);
            System.out.println("Drawing One Card from Deck");
            this.hands[turn].getDeck()[this.hands[turn].getLastCard() + 1] = this.deck.popCard(0);
            System.out.println("Turn Skipped");
            turn = this.nextTurn(turn);
        } else if (((LightSide) (this.startingCard[0])).getValue() == LightSide.values.FLIP) {
            this.flip();
            this.printStartingCard();
        } else if (((LightSide) (this.startingCard[0])).getValue() == LightSide.values.REVERSE) {
            this.reverse();
            if (this.noOfPlayers != 2) {
                turn = this.nextTurn(turn);
            }
        } else if (((LightSide) (this.startingCard[0])).getValue() == LightSide.values.SKIP) {
            turn = this.nextTurn(turn);
        }
        return turn;
    }

    public int darkSidePlay(int turn, int cardPlayed, String calledUno) {
        boolean condition;
        DarkSide cardSelected = (DarkSide) hands[turn].getCard(cardPlayed)[1];
        condition = ((cardSelected.getColor() == ((DarkSide) (this.startingCard[1])).getColor())
                || (cardSelected.getValue() == ((DarkSide) (this.startingCard[1])).getValue())
                || (cardSelected.getValue() == (DarkSide.values.NONE)));
        if (condition) {
            if (!((calledUno.toLowerCase().equals("UNO".toLowerCase())) || (calledUno.toLowerCase().equals(" UNO".toLowerCase())))) {
                for (int i = 0; i < 5; i++) {
                    this.drawCard(turn);
                }
            }
            this.discardedDeck.getDeck()[this.discardedDeck.getLastCard() + 1] = (hands[turn].popCard(cardPlayed));
            if (cardSelected.getValue() == DarkSide.values.DRAW_FIVE) {
                turn = this.nextTurn(turn);
                System.out.println(this.names[turn]);
                System.out.println("Draw five Cards from Deck");
                for (int i = 0; i < 5; i++) {
                    this.hands[turn].getDeck()[this.hands[turn].getLastCard() + 1] = (this.deck.popCard(0));
                }
                System.out.println("Turn Skipped.");
            } else if (cardSelected.getValue() == DarkSide.values.FLIP) {
                this.flip();
            } else if (cardSelected.getValue() == DarkSide.values.REVERSE) {
                this.reverse();
                if (this.noOfPlayers == 2) {
                    turn = this.nextTurn(turn);
                }
            } else if (cardSelected.getValue() == DarkSide.values.NONE) {
                if (cardSelected.getColor() == DarkSide.Color.WILD) {
                    System.out.println("Which Color do you want to proceed with? ");
                    for (int i = 2; i < 6; i++) {
                        System.out.println(i - 1 + ": " + (DarkSide.Color.values()[i]));
                    }
                    int selectedColor;
                    do {
                        selectedColor = input.nextInt();
                    } while ((selectedColor >= 5 || selectedColor <= 0));
                    ((DarkSide) (this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()][1]))
                            .setColor((DarkSide.Color.values()[selectedColor + 1]));
                }
                if (cardSelected.getColor() == DarkSide.Color.WILD_DRAW_COLOR) {
                    int newTurn = this.nextTurn(turn);
                    System.out.println("Which Color do you want to proceed with? ");
                    for (int i = 2; i < 6; i++) {
                        System.out.println(i - 1 + ": " + (DarkSide.Color.values()[i]));
                    }
                    int selectedColor;
                    do {
                        selectedColor = input.nextInt();
                    } while ((selectedColor >= 5 || selectedColor <= 0));
                    System.out.println(this.names[newTurn] + " do you want to challenge " + this.names[turn] + "\nY/N");
                    char answer;
                    do {
                        answer = input.next().charAt(0);
                    } while (!(answer == 'Y' || answer == 'N' || answer == 'y' || answer == 'n'));
                    int validChallenge = newTurn;
                    if (answer == 'Y' || answer == 'y') {
                        for (int i = 0; i < this.hands[turn].getLastCard() + 1; i++) {
                            if (((DarkSide) (this.hands[turn]).getCard(i)[1])
                                    .getColor() == ((DarkSide) (this.startingCard[1])).getColor()) {
                                validChallenge = turn;
                                break;
                            }
                        }
                        while (((DarkSide) (this.deck.getCard(0)[1]))
                                .getColor() == DarkSide.Color.values()[selectedColor + 1]) {
                            this.hands[validChallenge].getDeck()[this.hands[validChallenge].getLastCard()
                                    + 1] = this.deck.popCard(0);
                        }
                        this.hands[validChallenge].getDeck()[this.hands[validChallenge].getLastCard() + 1] = this.deck
                                .popCard(0);
                        this.hands[validChallenge].getDeck()[this.hands[validChallenge].getLastCard() + 1] = this.deck
                                .popCard(0);
                        if (validChallenge == newTurn) {
                            turn = this.nextTurn(turn);
                            System.out.println("Turn Skipped.");
                            ((DarkSide) (this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()][1]))
                                    .setColor((DarkSide.Color.values()[selectedColor + 1]));
                        } else {
                            this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()] = this.discardedDeck
                                    .getCard(this.discardedDeck.getLastCard() - 1);
                            this.discardedDeck.getDeck()[this.discardedDeck.getLastCard() - 1] = this.bufferCard;

                        }
                    } else {
                        while (((DarkSide) (this.deck.getCard(0)[1]))
                                .getColor() == DarkSide.Color.values()[selectedColor + 1]) {
                            this.hands[validChallenge].getDeck()[this.hands[validChallenge].getLastCard()
                                    + 1] = this.deck.popCard(0);
                        }

                    }
                }
            } else if (cardSelected.getValue() == DarkSide.values.SKIP_EVERYONE) {
                return turn;
            }
            turn = this.nextTurn(turn);

            return turn;
        } else {
            System.out.println("Card Not Matching. ");
            return turn;
        }
    }

    public int drawCard(int turn) {
        this.hands[turn].getDeck()[this.hands[turn].getLastCard() + 1] = this.deck.popCard(0);
        return this.nextTurn(turn);
    }

    public void askColorLight(int turn) {
        System.out.println(this.names[turn] + " choose a color.");
        for (int i = 2; i < 6; i++) {
            System.out.println(i - 1 + ": " + (LightSide.Color.values()[i]));
        }
        int selectedColor;
        do {
            selectedColor = input.nextInt();
        } while ((selectedColor >= 5 || selectedColor <= 0));
        ((LightSide) (this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()][this.currentSide]))
                .setColor((LightSide.Color.values()[selectedColor]));
    }

    public void askColorDark(int turn) {
        System.out.println(this.names[turn] + " choose a color.");
        for (int i = 2; i < 6; i++) {
            System.out.println(i - 1 + ": " + (DarkSide.Color.values()[i]));
        }
        int selectedColor;
        do {
            selectedColor = input.nextInt();
        } while ((selectedColor >= 5 || selectedColor <= 0));
        ((DarkSide) (this.discardedDeck.getDeck()[this.discardedDeck.getLastCard()][this.currentSide]))
                .setColor((DarkSide.Color.values()[selectedColor]));
    }

    public void refillDeck() {
        for (int i = 1; i < this.discardedDeck.getLastCard() + 1; i++) {
            this.deck.getDeck()[this.deck.getLastCard() + 1] = this.discardedDeck.popCard(0);
        }

    }

    public void startGame() {
        int turn = 0;
        int firstRound = 0;
        int prevTurn = 0;
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            // System.out.println((Arrays.binarySearch(this.order, turn)));
            prevTurn = turn;
            this.printStartingCard();
            if (this.currentSide == 0) {
                if (this.startingCard[this.currentSide] == LightSide.values.NONE) {
                    this.askColorLight(prevTurn);
                }
            } else if (this.currentSide == 1) {
                if (this.startingCard[this.currentSide] == DarkSide.values.NONE) {
                    this.askColorDark(prevTurn);
                }
            }
            if (firstRound == 0) {
                firstRound = 1;
                turn = lightSidePlay(turn);
                if (turn != 0) {
                    continue;
                }
            }
            System.out.println(this.names[turn]);
            int lastCard = this.cardsInHand(turn);
            for (int i = 0; i < lastCard + 1; i++) {
                System.out.println("Card: " + (i + 1));
                System.out.println(this.hands[turn].getCard(i)[this.currentSide]);
            }
            System.out.println("Enter The Card no. you want to play. Enter '404' to draw a card from the Deck");
            int cardPlayed;
            String calledUno;
            if (this.hands[turn].getLastCard() == 1) {
                cardPlayed = input.nextInt() - 1;
                calledUno = input.nextLine();
            } else {
                cardPlayed = input.nextInt() - 1;
                calledUno = "UNO";
            }
            if (cardPlayed <= lastCard) {
                if (this.currentSide == 0) {
                    turn = lightSidePlay(turn, cardPlayed, calledUno);
                } else {
                    turn = darkSidePlay(turn, cardPlayed, calledUno);
                }
            } else if (cardPlayed == 403) {
                turn = this.drawCard(turn);
            } else {
                System.out.println("Enter One Card from the hand");
                continue;
            }
            if (this.hands[prevTurn].getLastCard() == -1) {
                System.out.println("Congratulations '" + this.names[prevTurn] + "' you Won!!!");
                break;
            }
            System.out.println(
                    "========================================================================================");
        }
    }
}
