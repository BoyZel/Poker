package com.company;

import java.io.Serializable;

/**
 * Represents a card
 */
public class Card implements Serializable {
    private CardColor color;
    private CardNumber number;

    public Card(int a, int b){
        number = CardNumber.values()[a];
        color = CardColor.values()[b];
    }
    public Card(){
        number = CardNumber.values()[0];
        color = CardColor.values()[0];
    }
    public void Card(int a, int b){
        number = CardNumber.values()[a];
        color = CardColor.values()[b];
    }
    public CardNumber number() { return number; }
    public CardColor color() { return color; }
}
