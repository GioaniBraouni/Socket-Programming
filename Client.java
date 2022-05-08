package client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client
{

    public static void main(String[] args)
    {
        Client client = new Client();
    }

    public Client()
    {
        int counter = 1;
        while(true)
        {
            try(Socket clientSocket = new Socket("localhost",5131))
            {
                if (counter == 1)
                {
                    System.out.println("Connection to server has been established");
                }

                System.out.println();
                int inputAnswear = printMenu();
                System.out.println();
                if(inputAnswear == 1)
                {
                    sendPacket(clientSocket);
                }
                else if(inputAnswear == 2)
                {
                    receivePacket(clientSocket);
                }
                else if(inputAnswear == 5)
                {
                    PrintWriter toServer = new PrintWriter(clientSocket.getOutputStream(), true);
                    toServer.println("5");
                    clientSocket.close();
                    return;
                }else if(inputAnswear == 3)
                {
                    getContent(clientSocket);

                }else if(inputAnswear == 4)
                {
                    getFiles(clientSocket);
                }



                counter++;
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }
    }
    private void receivePacket(Socket clientSocket)
    {
        try
        {
            PrintWriter toServer = new PrintWriter(clientSocket.getOutputStream(), true);
            toServer.println("2");
            Scanner fromClient = new Scanner(clientSocket.getInputStream());
            System.out.println("Received from server: ");
            while(true)
            {
                String serverRespond = fromClient.nextLine();
                if(serverRespond.equals("null"))
                    return;
                System.out.println(serverRespond);
                System.out.println();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    private int printMenu()
    {
        Scanner input = new Scanner(System.in);

        System.out.println("Παρακαλω επελεξε ενα απο τα παρακατω");
        System.out.println("Αποστολη(1)");
        System.out.println("Ληψη(2)");
        System.out.println("Αρχεία(3)");
        System.out.println("Πλήρη Ιστορικό(4)");
        System.out.println("Εξοδος(5)");

        return input.nextInt();
    }
    private void sendPacket(Socket clientSocket) throws IOException
    {
        PrintWriter toServer = new PrintWriter(clientSocket.getOutputStream(), true);
        Scanner input = new Scanner(System.in);

        toServer.println("1");

        System.out.println("Παρακαλω πληκτρολογησε τον αριθμο τηλεφωνου σου");
        String phoneNumber = input.nextLine();
        toServer.println(phoneNumber);
        System.out.println();

        System.out.println("Παρακαλω πληκτρολογησε τον κωδικο μετακινησης");
        System.out.println();

        System.out.println("Μετάβαση σε φαρμακείο ή επίσκεψη στον γιατρό(1)");
        System.out.println("Μετάβαση σε εν λειτουργία κατάστημα προμηθειών αγαθών πρώτης ανάγκης(2)");
        System.out.println("Σωματική άσκηση σε εξωτερικό χώρο ατομικά(3)");

        String smsChoice = input.nextLine();
        System.out.println();
        toServer.println(smsChoice);

        System.out.println("Παρακαλω πληκτρολογησε την διευθυνση κατοικιας");

        String homeAddress = input.nextLine();
        System.out.println();
        toServer.println(homeAddress);

        String recipientNumber = "13033";
        toServer.println(recipientNumber);
        System.out.println("Sms has been sucessfully sent to: " + recipientNumber);
        System.out.println();
    }


    private void getContent(Socket clientSocket) throws IOException {
        PrintWriter toServer = new PrintWriter(clientSocket.getOutputStream(), true);
        toServer.println("3");
        Scanner fromClient = new Scanner(clientSocket.getInputStream());
        while (fromClient.hasNext())
        {
            String serverRespond = fromClient.nextLine();
            System.out.println(serverRespond);
        }

        System.out.println();
        toServer.flush();

    }

    private void getFiles(Socket clientSocket) throws IOException {
        PrintWriter toServer = new PrintWriter(clientSocket.getOutputStream(), true);
        toServer.println("4");
        Scanner fromClient = new Scanner(clientSocket.getInputStream());
        int count = 0;
        while (fromClient.hasNext())
        {
            count ++;
            String serverRespond = fromClient.nextLine();
            System.out.println(serverRespond);
            if (count == 4)
            {
                System.out.println();
                count = 0;
            }

        }



        System.out.println();

    }


}