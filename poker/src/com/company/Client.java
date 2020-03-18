package com.company;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Class used to communicate between Model and Controller
 */
public class Client {
    private int mynumber, turn=-1;
    private Serializable answer;
    private Consumer<Serializable> callback;
    /**
     * Variable used in synchronizing
     */
    boolean didPlayerAnswered;
    Model tmp1 ;
    private ObjectOutputStream out;
    /** Thread establishing the connection between Server and Client
     *
     */
    private ConnectionThread connthread = new ConnectionThread();

    /**
     * Function used by the model to give information about players decision,
     * synchronized to avoid zakleszczenie
     * @param data the message to controller
     * @param turn
     */
    public synchronized void SetAnswer(Serializable data, int turn){
        answer = data;
        didPlayerAnswered = true;
        this.turn = turn;
        System.out.println("Sukces");
        notifyAll();
    }

    /**
     * Function used by client to send data to the controller
     */
    public synchronized void receive(){
        while(!didPlayerAnswered){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Dawaj");
        try {
            out.writeObject(answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        didPlayerAnswered = false;
        turn = -1;
    }
    public int Getmynumber(){
        return mynumber;
    }

    /**
     * Function established do push info about board to the model
     * @param callback
     */
    public Client(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true);
    }

    public void StartClient() {
        connthread.start();
    }


    private class ConnectionThread extends Thread {

        private ObjectInputStream in;

        /**
         * Function that organizes work of the client
         */
        public void run(){
            didPlayerAnswered = false;
            answer = null;
            Socket client = null;
            try {
                client = new Socket("localhost", 4999);
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert client != null;
                mynumber = (int) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            while (true) {
                while (turn != mynumber) {
                    Serializable message = null;
                    try {
                        message = (Serializable) in.readObject();
                        tmp1 = (Model)message;
                        turn=tmp1.GetTurn();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    callback.accept(message);
                }
                receive();
            }
        }
    }
}
