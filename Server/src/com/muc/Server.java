package com.muc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    private final int serverPort;
    private List<ServerHandler> clientList;


    public Server(int serverPort){
        this.serverPort = serverPort;
        this.clientList = new ArrayList<>();
    }

    public List<ServerHandler> getList(){
        return clientList;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true){
                System.out.println("Waiting for connection...\n");
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress() + " connected!\n");
                ServerHandler serverHandler = new ServerHandler(this, clientSocket);
                clientList.add(serverHandler);
                serverHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(ServerHandler serverHandler) {
        clientList.remove(serverHandler);
    }
}
