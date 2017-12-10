package ru.timick.javaiotserver;

public interface ConnectionListener {
    void onConnect(Connection c);
    void onDataReceive(Connection c, String data);
    void onDisconnect(Connection c);
    void onException(Connection c, Exception e);
}
