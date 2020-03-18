package com.company;

import java.net.*;
import java.io.*;


/**
 * Class used to communicate between Controller and Model
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ObjectOutputStream out[] = new ObjectOutputStream[10];
        ObjectInputStream in[] = new ObjectInputStream[10];
        Serializable data;
        ServerSocket server = new ServerSocket(4999);
        Socket[] client = new Socket[10];
        int i = 0;
        while (i < 8) {
            client[i] = server.accept();
            System.out.println("client connected");
            out[i] = new ObjectOutputStream(client[i].getOutputStream());
            in[i] = new ObjectInputStream(client[i].getInputStream());
            data = i;
            System.out.println(i);
            out[i].writeObject(data); // send number of player to the client
            i++;
        }
        System.out.println("Rozpoczynamy");
        Game gra = new Game(8,client, out, in );
        Thread thread = new Thread(gra);
        thread.start();
    }
}
