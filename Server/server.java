package Server;

import java.net.*;
import java.io.*;
/* import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException */;

//Creazione di una classe per il Multrithreading
class server extends Thread {
    private int bet;
    private boolean cards[] = {false, false, false};
    private Socket socket;

    public server (Socket socket) {
        this.socket = socket;
        System.out.println(this.socket.getInetAddress());
    }

    public void showCards(DataOutputStream os) throws IOException {
        String card = "";
        for(int i = 0; i < cards.length; i++){
            card += "[*] ";
            
        }
        os.writeBytes(card + "\n");
        os.flush();
    }

    public void checkAnswer(DataOutputStream os, String userInput) throws IOException {
        if(cards[Integer.parseInt(userInput) - 1]){
            os.writeBytes("You won " + bet * 2 + " credits!\n");
            os.flush();

        /*     os.writeBytes("Give this gentleman ");
            os.flush();

            os.writeBytes("" + bet * 2);
            os.flush();

            os.writeBytes(" credits.\n");
            os.flush(); */

            bet = 0;
        }else{
            os.writeBytes("You lost " + bet + " credits.\n");
            os.flush();

          /*   os.writeBytes("I'm so...");
            os.flush();

            os.writeBytes(" so sorry for yuor loss, wanna play again?\n");
            bet = 0; */
        }
    }

    public void revealCards(DataOutputStream os) throws IOException {
        String card = "";
        for(int i = 0; i < cards.length; i++){
            if(cards[i]){
                card += "[Q] ";
            }else{
                card += ("[X] ");
            }
        }
        os.writeBytes(card + "\n");
        os.flush();
    }

    public boolean hideQueen(String dealerInput) throws IOException {
        try {
            int card = Integer.parseInt(dealerInput) - 1;

            if(card < 0 || card > 2){
                System.out.println("Select one of the three positions.");
                return false;
            }
            
            for(int i = cards.length - 1; i >= 0; i--){
                cards[i] = false;
            }
            cards[card] = true;

            return true;

        } catch (Exception e) {
            System.out.println("Select one of the three positions.");
            return false;
        }
    }

    //esecuzione del Thread sul Socket
    public void run() {
        try{
            String dealerInput;
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); 

            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

            String[] userInput;

            //Ciclo infinito di ascolto del Client
            while (true) {
                userInput = is.readLine().split(" ");

                if(userInput[0].equals("exit")){
                    os.writeBytes("exit" + '\n');
                    os.close();
                    is.close();
                    socket.close();
                    return;
                }

                switch (userInput[0]) {
                    case "1":
                    case "2":
                    case "3":
                        revealCards(os);
                        checkAnswer(os, userInput[0]);

                        break;
                
                    default:
                        bet = Integer.parseInt(userInput[0]);

                        System.out.println("Bet: " + bet + " credits.");
                        do{
                            System.out.println("Choose where to put the queen: [1] [2] [3]");
                            dealerInput = stdIn.readLine();
                        }while (!hideQueen(dealerInput));

                        showCards(os);
                        
                        break;
                }
            }
        }catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
    
}
