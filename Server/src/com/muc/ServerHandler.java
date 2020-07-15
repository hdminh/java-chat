package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerHandler extends Thread{

    private final Socket clientSocket;
    private final Server server;
    String login = null;
    private OutputStream outputStream;

    public ServerHandler(Server server, Socket clientSocket){
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            clientHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clientHandler() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");
            if (tokens.length > 0) {
                String cmd = tokens[0];
                if ("q".equalsIgnoreCase(cmd)) {
                    exitHandler();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    loginHandler(outputStream, tokens);
                } else {
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        clientSocket.close();
    }

    private void exitHandler() throws IOException{
        server.removeUser(this);
        clientSocket.close();

    }

    public String getLogin(){
        return login;
    }

    private void loginHandler(OutputStream outputStream, String[] tokens) throws IOException{
        if (tokens.length == 2){
            List<ServerHandler> listUser = server.getList();
            String login = tokens[1];

            for (ServerHandler user: listUser){
                if (user.getLogin() != null && user.getLogin().equals(login)){
                    outputStream.write(("Already logged in").getBytes());
                    return;
                }
            }
            this.login = login;

            outputStream.write(("\nLogin success " + login +"\n").getBytes());
            String onlineMsg = login + " online\n";

            for (ServerHandler user: listUser){
                //show all online users
                if (!login.equals(user.getLogin()) && user.getLogin() != null) {
                    String msg = user.getLogin() + " online\n";
                    sendMsg(msg);
                }
                //send online status to other users
                user.sendMsg(onlineMsg);
            }
        }
        else {
            outputStream.write("error\n".getBytes());
        }
    }

    private void sendMsg(String msg) throws IOException {
        if (login != null) {
            outputStream.write(msg.getBytes());
        }
    }
}
