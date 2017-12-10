package ru.timick.javaiotserver;

import java.util.Scanner;

public class CommandExecutor {
    private Thread cmdThread;
    private Scanner in = new Scanner(System.in);
    private Server server;

    public CommandExecutor(Server server) {
        this.server = server;
        cmdThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(!cmdThread.isInterrupted()) {
                    System.out.print("> ");
                    String[] strs = in.nextLine().split(" ");
                    String[] args = new String[strs.length-1];
                    for(int i=0;i<args.length;i++) args[i] = strs[i+1];
                    server.executeCommand(strs[0], args);
                }

            }

        });
        cmdThread.start();
    }

}
