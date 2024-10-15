package Networks_Coursework;
import java.io.*;
import java.net.*;


public class ActionServer {
  public static void main(String[] args) throws IOException {

	ServerSocket ActionServerSocket = null;
    boolean listening = true;
    String ActionServerName = "ActionServer";
    int ActionServerNumber = 4545;
    
    double[] ClientAccounts = {1000, 1000, 1000};

    //Create the shared object in the global scope...
    
    SharedActionState ourSharedActionStateObject = new SharedActionState(ClientAccounts);
        
    // Make the server socketS

    try {
      ActionServerSocket = new ServerSocket(ActionServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + ActionServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(ActionServerName + " started");

    int threadCounter = 0;
    
    while (listening){
      String threadName = "ActionServerThread" + threadCounter++;
      new ActionServerThread(ActionServerSocket.accept(), threadName, ourSharedActionStateObject).start();
      System.out.println("New " + ActionServerName + " thread started: " + threadName);
    }
    ActionServerSocket.close();
  }
}