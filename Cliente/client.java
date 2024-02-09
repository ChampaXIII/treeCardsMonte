package Cliente;

import java.net.*; 
import java.io.*; 
//import java.util.Scanner;

public class client {
    private int credits = 500;

    public boolean choseBet(String bet){
        try {
            int intBet = Integer.parseInt(bet);

            if(this.credits < intBet){
                System.out.println("You don't have enough credits to bet that amount.");
                return false;
            }

            if(intBet < 5){
                System.out.println("Minimum bet is 5.");
                return false;
            }

            this.credits -= intBet;

            return true;

        } catch (Exception e) {
            if(bet.equals("exit")){
                return true;
            }
        }
        return false;
    }

    public boolean choseCard(String card){
        try {
            int intCard = Integer.parseInt(card);

            if(intCard < 1 || intCard > 3){
                System.out.println("Select one of the three positions.");
                return false;
            }

            return true;

        } catch (Exception e) {
            System.out.println("Select one of the three positions.");
            return false;
        }
    }

    public void setCredits(String credits){
        try {
            this.credits += Integer.parseInt(credits);
        } catch (Exception e) {
            return;
        }
    }

    public void setCreditTest(String winnings){
        String won[] = winnings.split(" ");

        try {
            this.credits += Integer.parseInt(won[2]);
        } catch (Exception e) {
            return;
        }
    }
    public void start()throws IOException { 
        Socket socket;
        BufferedReader is;
        DataOutputStream os;  
        String userInput;
        String serverResponse;

        //Connessione della Socket con il Server 
        socket = new Socket("127.0.0.1", 7777); 

        //Stream di byte da passare al Socket 
        os = new DataOutputStream(socket.getOutputStream()); 
        is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); 
        do{
            System.out.print("Wanna play a game? (yes/no): ");
            userInput = stdIn.readLine();
        }while(!userInput.equals("no") && !userInput.equals("yes"));
        
        if("no".equals(userInput)){
            os.writeBytes("exit" + '\n');
            os.flush();
            os.close(); 
            is.close(); 
            socket.close(); 
            return;
        }

        System.out.println("Remember, tree things in life are akways true; taxes, write \"exit\" to exit and the house always wins.");
        //Ciclo infinito per inserimento testo del Client 
        while (true){ 
            do {
                System.out.println("You have " + credits + " credits. How much do you want to bet?"); 
                userInput = stdIn.readLine();
            } while (!choseBet(userInput));

            if(userInput.equals("exit")){
                //Chiusura dello Stream e del Socket 
                os.close(); 
                is.close(); 
                socket.close();

                return;
            }
            os.writeBytes(userInput + '\n'); 
            os.flush();

            System.out.println("--------------------");

            serverResponse = is.readLine();
            System.out.println(serverResponse);

            do{
                System.out.println("Choose a card: ");
                userInput = stdIn.readLine();
            }while (!choseCard(userInput));

            os.writeBytes(userInput + '\n');
            os.flush();

            System.out.println("--------------------");

            serverResponse = is.readLine();
            System.out.println(serverResponse + "1");

            serverResponse = is.readLine();
            System.out.println(serverResponse + "2");

            setCreditTest(serverResponse);
           /*  serverResponse = is.readLine();
            setCredits(serverResponse);
            System.out.println(serverResponse + "3"); */

            /* serverResponse = is.readLine();
            System.out.println(serverResponse + "4"); */

        }

         
    } 
    
    public static void main (String[] args) throws Exception { 
        client client = new client(); 
        client.start(); 
    } 
}