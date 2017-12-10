package ru.timick.javaiotserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private ConnectionListener listener;
    private Socket socket;
    private Thread rxThread;
    private DataOutputStream out;
    private DataInputStream in;
    private int type = 0; //0 - undefined; 1 - device; 2 - user

    public Connection(ConnectionListener listener, Socket socket) {
        this.listener = listener;
        this.socket = socket;

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) { listener.onException(Connection.this, e); }

        rxThread = new Thread(new Runnable() {
            public void run() {
                try {
                    listener.onConnect(Connection.this);
                    while(!rxThread.isInterrupted()) {
                        listener.onDataRecieve(Connection.this, in.readUTF());
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });
        rxThread.start();
    }

    public synchronized int getType() { return type; }
    public synchronized void setType(int type) { this.type = type; }

    public synchronized void sendData(String s) {
        try {
            out.writeUTF(s);
            out.flush();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
            disconnect();
        }

    }
    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
        }
    }
    @Override
    public String toString() {
        String type = "";
        if(this.type==1) type = "device";
        else if(this.type==2) type="user";
        return "Connection: " + socket.getInetAddress() + ". Type: " + type;
    }

}
