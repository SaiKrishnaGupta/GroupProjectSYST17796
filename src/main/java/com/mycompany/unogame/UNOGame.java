/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.unogame;

import com.mycompany.unogame.Card.Deck;
import com.mycompany.unogame.Game.Dealer;

/**
 *
 * @author Sai Krishna Gupta
 */
public class UNOGame {

    static Deck deck = new Deck();

    public static void deckCreate() {
        deck.fillDeck();
        deck.shuffleDeck();
        deck.nullsAtLast();
    }

    public static void main(String[] args) {
        deckCreate();
        Dealer dealer = new Dealer(deck);
        dealer.fixPlayers();
        dealer.distributeCards();
        dealer.startGame();
    }
}
