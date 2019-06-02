package com.example.aesophor.dingdong.network;

import android.util.Log;

import com.example.aesophor.dingdong.event.Event;
import com.example.aesophor.dingdong.event.EventManager;

import java.net.*;
import java.io.*;
public class SocketServer implements Runnable {

    private static SocketServer server;

    private final EventManager eventManager;
    private final int port;
    private ServerSocket serverSocket;
    private Socket socket;

    private SocketServer(int port) {
        this.eventManager = EventManager.getInstance();
        this.port = port;
    }

    public static SocketServer getInstance() {
        if (server == null) {
            server = new SocketServer(8001);
        }

        return server;
    }


    @Override
    public void run() {
        try {
            Log.i("SocketServer.java", "[*] Started listening for events on port " + port);
            serverSocket = new ServerSocket(port);

            while (true) {
                socket = serverSocket.accept();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String e = br.readLine();

                System.out.println("Received: " + e);
                eventManager.fireEvent(new Event(e).toConcreteEvent());

                socket.close();
            }
        } catch (IOException ex) {

        }

        Log.i("SocketServer.java", "[*] Shutting down...");
    }

    public void shutdown() {
        try {
            serverSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}