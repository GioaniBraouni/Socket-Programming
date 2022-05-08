package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Server {
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<SmsPacket> packets = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server() {
        int counter = 1;
        System.out.println("Waiting from client");
        try (ServerSocket serverSocket = new ServerSocket(5131)) {

            while (true) {
                Socket socket = serverSocket.accept();
                if (counter == 1) {
                    System.out.println("Connection from client has been established");
                }


                System.out.println();

                Scanner fromClient = new Scanner(socket.getInputStream());
                String input = fromClient.nextLine();

                if (input.equals("1")) {
                    receivePacket(socket);
                    savePacketToFile();
                } else if (input.equals("2")) {
                    sendPacket(socket, packets);
                } else if (input.equals("5")) {
                    System.out.println("Connection from client has been terminated");
                    socket.close();
                    return;
                } else if (input.equals("3")) {

                    listFilesForFolder(socket);
                }else if (input.equals("4")) {

                    listContent(socket);
                }
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        counter = 0;
    }

    private void receivePacket(Socket socket) {
        try {
            Scanner fromClient = new Scanner(socket.getInputStream());
            int counter = 0;
            do {
                String input = fromClient.nextLine();
                list.add(input);
                counter++;
                if (counter == 4) {
                    SmsPacket packet = new SmsPacket();
                    packet.setPhoneNumber(list.get(0));
                    packet.setSmsChoice(list.get(1));
                    packet.setHomeAddress(list.get(2));
                    packet.setRecipientName(list.get(3));
                    System.out.println("Sms has been successfully received from: " + packet.getPhoneNumber());
                    System.out.println();
                    packets.add(packet);
                    list.clear();
                    counter = 0;
                }
            }
            while (fromClient.hasNextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePacketToFile() {
        for (SmsPacket packet : packets) {
            try {
                File usersName = new File("Usernames");
                usersName.mkdir();
                File path = new File(usersName + "/" + packet.getPhoneNumber() + ".txt");
                PrintWriter pw = new PrintWriter(path);
                pw.println("Phone number: " + packet.getPhoneNumber());
                pw.println("Sms choice: " + packet.getSmsChoice());
                pw.println("Home address: " + packet.getHomeAddress());
                pw.println("Recipient name: " + packet.getRecipientName());
                System.out.println("Sms has been successfully saved to: " + path.getAbsolutePath());
                System.out.println();
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPacket(Socket socket, ArrayList<SmsPacket> packets) {
        try {
            System.out.println("Request for sms received from clint");
            PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
            if (packets.isEmpty())
                toClient.println("Server's database is empty");
            else {
                for (SmsPacket packet : packets) {
                    toClient.println(packet.toString());
                }
                System.out.println("Sending...");
            }
            System.out.println("Number of sms sent to client: " + packets.size());
            toClient.println("null");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public  void listFilesForFolder(Socket socket) {
        try{
            System.out.println("Request for files history from client");
            PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
            File folder = new File("Usernames");
            File[] listOfFiles = folder.listFiles();

            assert listOfFiles != null;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    toClient.println(file.getName());
                }
            }
            toClient.close();
            System.out.println("Sending...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void listContent(Socket socket) throws IOException {
        try {
            System.out.println("Request for analytically sms history from client");
            PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
            File folder = new File("Usernames");
            File[] listOfFiles = folder.listFiles();

            assert listOfFiles != null;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        toClient.println(line);

                    }

                }
            }
            System.out.println("Sending...");
            toClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
