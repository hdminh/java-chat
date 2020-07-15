package com.muc;

public class ServerMain {
    private static int PORT = 8800;
    static Server server;

    public static void main(String[] args) {
        server = new Server(PORT);
        server.start();
    }
}
