package ru.timick.javaiotserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements ConnectionListener {
    private int port;
    private ServerSocket serverSocket;
    private ArrayList<Connection> connections = new ArrayList<>();

    public Server(int port) {
        this.port = port;

        System.out.println("Starting IoT server...");


        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started! Waiting for connections...");
            new CommandExecutor(this);
            while(true) {
                try {
                    new Connection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("Connection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeCommand(String cmd, String args[]) {
        switch(cmd) {
            case "stop": stop();
                break;
            case "connections":
                for(int i=0;i<connections.size();i++) {
                    System.out.println(connections.get(i));
                }
                break;
            case "type":

                break;
            default: System.out.println("Unknown command");
        }
    }

    public void stop() {
        System.out.println("Server stopped");
        for(int i=0;i<connections.size();i++) connections.get(i).disconnect();
        try {
            serverSocket.close();
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnect(Connection c) {
        connections.add(c);

    }

    @Override
    public synchronized void onDataReceive(Connection c, String data) {
        switch(data) {
            //Device
            case "device":
                c.setType(1);
                System.out.println(c.toString());
                break;
            //User
            case "user":
                c.setType(2);
                System.out.println(c.toString());
                break;
        }
    }

    @Override
    public synchronized void onDisconnect(Connection c) {
        connections.remove(c);
    }

    @Override
    public synchronized void onException(Connection c, Exception e) {
        System.out.println("Connection exception: " + e);

    }

}
