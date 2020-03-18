package com.company;


import java.io.*;
import java.net.*;

/**
 * Class representing the controller
 */
public class Game implements Runnable {
    private Model data;
    private Socket[] client;
    private ObjectOutputStream out[];
    private ObjectInputStream in2[];
    public int CheckWin(int winner) throws IOException{
        if(winner == -1)
            return 0;
        else{
            for(int i =0 ; i < data.GetNumber() ; i++) {
                out[i].writeObject(data);
            }
        }
        return -1;
    }

    /**
     *
     * @param number
     * @param client
     * @param out Must be transmitted, to avoid bugs in header
     * @param in Must be transmitted to avoid bugs in header
     * @throws IOException
     */
    public Game(int number, Socket[] client, ObjectOutputStream[] out, ObjectInputStream[] in) throws IOException {
        this.client = new Socket[10];
        data = new Model(number );
        this.client =  client;
        this.out = out;
        this.in2 = in;
    }

    /**
     * Phase of the game
     * @param x
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void Phase(int x) throws IOException, ClassNotFoundException {
        while(data.GetNext() != data.GetActivecount()) {
            if(data.GetActive(data.GetTurn()) == 0) {
                data.SetTurn(data.GetTurn()+1);
                continue;
            }
            for(int i = 0 ; i < data.GetNumber() ; i ++) {
                data.setMynum(i);
                out[i].writeObject(data); //send state of the model to View
                out[i].reset();
            }
            int decision = (int)in2[data.GetTurn()].readObject(); // Receive the decision of the user
            if(decision == 0) {
                data.Fold();
            }
            if(decision == 1) {
                data.Call();
            }
            if(decision == 3) {
                data.Wait();
            }
            if(decision!=1 && decision!=0 && decision!=3 ){
                data.Bet(decision);
            }
            if(data.GetTurn()+1 == data.GetNumber())
                data.SetTurn(0);
            else
                data.SetTurn(data.GetTurn()+1);
        }
        data.SetTurn(data.GetDealer()+1);
        if(data.getPhase()!=3)
        data.setPhase(data.getPhase()+1);


    }
    public void run() {
        data.SetDealer(-1);
        data.setPhase(0);
        while (true) {
            int winner=data.PrepareBlinds();
            try {
                if(CheckWin(winner)!=0)
                break;
            data.GetCards();
                Phase(0);
                if(data.GetNext()>1)
                    Phase(1);
                if(data.GetNext() >1)
                    Phase(2);
                if(data.GetNext()>1)
                    Phase(3);
                if(data.GetNext()>1)
                    data.WhoWon();
            }
            catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
            System.out.println("sukces");
        }
    }

}
