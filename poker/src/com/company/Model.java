package com.company;
import java.io.Serializable;
import java.util.Random;

/**
 * Class representing model
 */
public class Model implements Serializable {
    /**
     * bankrolls of players, how many coins do they have in the whole game
     */
    private int[] bankroll;
    /**
     * how big is the stack
     */
    private int stack;
    /**
     * A number of client, to wchich the model is transmitted
     */
    private int mynum;
    /**
     * How many coins has a player used in the current distribution
     */
    private int[] current;
    /** Cards on the table */
    private Card[] table;
    /** Cards of players */
    private Card[] card1;
    private Card[] card2;
    /** How many players are in the game */
    private int number;
    /**
     * Who is the current dealer
     */
    private int dealer;
    /**
     * Amount of the small blind
     */
    private int sblind;
    /**
     * How many coins is needed to check
     */
    private int tocheck;
    /**
     * Which players are still taking part in a current distribution
     */
    private int[] active;
    /** how many players are ready for the next round */
    private int next;
    /**how many players stay in the distribution */
    private int activecount;
    /** whose turn to make a decision it is */
    private int turn;
    /** which phase it is */
    private int phase;

    public Model(int number){
        this.number = number;
        bankroll = new int[10];
        table = new Card[10];
        card1 = new Card[10];
        card2 = new Card[10];
        current = new int[10];
        active = new int[10];
        sblind = 10;
        for(int i = 0 ; i < number ; i++){
            bankroll[i]=100;
        }
    }
    public void Fold() {
        active[turn]=0;
        activecount--;
        System.out.println("FOLD!");

    }

    public void Call() {
        stack += (tocheck - current[turn]);
        bankroll[turn] -= (tocheck - current[turn]);
        current[turn] = tocheck;
        next++;
        System.out.println("Call");
    }

    public void Bet(int amount) {
        stack += (amount + tocheck - current[turn]);
        bankroll[turn] -= (amount + tocheck - current[turn]);
        current[turn] = tocheck + amount;
        tocheck += amount;
        next = 1;
    }
    public void Wait(){
        next++;
    }
    public int PrepareBlinds(){
        activecount=0;
        int who=0;
        for (int i = 0; i < number; i++) {
            if (bankroll[i] > 0){ // Players who lost will be constantly inactive
                active[i] = 1;
                activecount++;
                who=i;
            }
        }
        if(activecount == 1)
            return who;
        if (dealer < number-1)
            dealer++;
        else
            dealer = 0;
        stack = 3 * sblind;
        turn = dealer;
        current[turn]+=sblind;
        bankroll[turn++] -= sblind;
        if(turn == number)
            turn = 0;
        current[turn]+=2*sblind;
        bankroll[turn++] -= 2 * sblind;
        if(turn == number)
            turn = 0;
        next = 1;
        tocheck = 2*sblind;
        return -1;

    }

    /**
     * Function distributing cards to players
     */
    public void GetCards(){
        Random rand = new Random();
        for (int i = 0; i < number; i++) {
            int[] tmp = new int[6];
            for (int j = 0; j < 6; j += 2)
                tmp[j] = rand.nextInt(13);
            for (int j = 1; j < 6; j += 2)
                tmp[j] = rand.nextInt(4);
            table[i] = new Card();
            card1[i] = new Card();
            card2[i] = new Card();
            table[i].Card(tmp[0], tmp[1]);
            card1[i].Card(tmp[2], tmp[3]);
            card2[i].Card(tmp[4], tmp[5]);
        }

    }
    /** Function checking who won the game */
    public void WhoWon() {

    }
    public int GetNumber(){
        return number;
    }
    public void SetDealer(int dealer){
        this.dealer = dealer;
    }
    public Card GetTable(int index){
        return table[index];
    }
    public int GetNext(){
        return next;
    }
    public int GetBankroll(int index){
        return bankroll[index];
    }
    public int GetTurn(){
        return turn;
    }
    public int GetActivecount() {
        return activecount;
    }
    public int GetActive(int getTurn) {
        return active[getTurn];
    }
    public Card GetCard1(int getTurn) {
        return card1[getTurn];
    }
    public Card GetCard2(int getTurn) { return card2[getTurn]; }
    public void SetTurn(int value){
        turn = value;
    }
    public int GetDealer(){ return dealer; }
    public int GetCurrent(int index){
        return current[index];
    }
    public int getMynum() {
        return mynum;
    }

    public void setMynum(int mynum) {
        this.mynum = mynum;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }
}
