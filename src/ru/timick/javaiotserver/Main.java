package ru.timick.javaiotserver;

public class Main {

    public static void main(String[] args) {
        args = new String[1];
        args[0] = "3360";
        if(args.length != 1) {
            System.out.println("Usage: java -jar server.jar [port]");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);
        new Server(port);


    }

}
