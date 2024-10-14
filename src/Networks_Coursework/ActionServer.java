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

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerThread1", ourSharedActionStateObject).start();
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerThread2", ourSharedActionStateObject).start();
      new ActionServerThread(ActionServerSocket.accept(), "ActionServerThread3", ourSharedActionStateObject).start();
      System.out.println("New " + ActionServerName + " thread started.");
    }
    ActionServerSocket.close();
  }
}