package Networks_Coursework;

import java.util.ArrayList;

public class SharedActionState{
	
	private SharedActionState mySharedObj;
	private String myThreadName;
	private double [] mySharedVariable;
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers

// Constructor	
	
	SharedActionState(double [] SharedVariable) {
		mySharedVariable = SharedVariable;
	}

//Attempt to aquire a lock
	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        System.out.println(me.getName()+" is attempting to acquire a lock!");	
	        ++threadsWaiting;
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    --threadsWaiting;
		    accessing = true;
		    System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		  }
	
	
    /* The processInput method */

	public synchronized String processInput(String myThreadName, String theInput) {
    		System.out.println(myThreadName + " received "+ theInput);
    		String theOutput = null;
    		// Check what the client said
    		if (theInput.contains("Add_Money")) {
				System.out.println("Add_Money request received");
				StringBuilder amountPart = new StringBuilder();
				char[] charArray = theInput.toCharArray();
				try {
					for (char c : charArray) {
						if (Character.isDigit(c) || c == '.' || c == '-') {
							amountPart.append(c);
							System.out.println("Amount: " + amountPart.toString());
						}
					}
					
				} 
				catch (Exception e) {
					theOutput = "Error: " + e;
					e.printStackTrace();
				}
				System.out.println("Amount: " + amountPart.toString());
				String amountString = amountPart.toString();
				Double amount = Double.valueOf(amountString);
				System.out.println("aaaaaa: " + amount);
				
				System.out.println("Match");
				// Extract Client and Amount
				Integer Client = null;
				if (theInput.contains("ClientA")){
					Client = 0;
				}
				else if (theInput.contains("ClientB")){
					Client = 1;					}
				else if (theInput.contains("ClientC")){
					Client = 2;
				}
				else {
					System.out.println("Error - client not recognised.");
					}
				// Add the amount to the client's account
				mySharedVariable[Client] = mySharedVariable[Client] + amount;
				theOutput = "Your new account amount is: " + mySharedVariable[Client];
			}
			else if (theInput.contains("Subtract_Money")) {
				System.out.println("Subtract_Money request received");
				StringBuilder amountPart = new StringBuilder();
				char[] charArray = theInput.toCharArray();
				try {
					for (char c : charArray) {
						if (Character.isDigit(c) || c == '.' || c == '-') {
							amountPart.append(c);
							System.out.println("Amount: " + amountPart.toString());
						}
					}
				} 
				catch (Exception e) {
					theOutput = "Error: " + e;
					e.printStackTrace();
				}
				System.out.println("Amount: " + amountPart.toString());
				String amountString = amountPart.toString();
				Double amount = Double.valueOf(amountString);
				System.out.println("aaaaaa: " + amount);
				

				System.out.println("Match");
				// Extract Client and Amount
				Integer Client = null;
				if (theInput.contains("ClientA")){
					Client = 0;
				}
				else if (theInput.contains("ClientB")){
					Client = 1;					
				}
				else if (theInput.contains("ClientC")){
					Client = 2;
				}
				else {
					System.out.println("Error - client not recognised.");
					}
				// Add the amount to the client's account
				mySharedVariable[Client] = mySharedVariable[Client] - amount;
				theOutput = "Your new account amount is: " + mySharedVariable[Client];
     		//Return the output message to the ActionServer
    		System.out.println(theOutput);
    		return theOutput;
    	}
		else if (theInput.contains("Transfer_Money")) {
			System.out.println("Transfer_Money request received");
			StringBuilder amountPart = new StringBuilder();
			char[] charArray = theInput.toCharArray();
			try {
				for (char c : charArray) {
					if (Character.isDigit(c) || c == '.' || c == '-') {
						amountPart.append(c);
						System.out.println("Amount: " + amountPart.toString());
					}
				}
			} 
			catch (Exception e) {
				theOutput = "Error: " + e;
				e.printStackTrace();
			}
			System.out.println("Amount: " + amountPart.toString());
			String amountString = amountPart.toString();
			Double amount = Double.valueOf(amountString);
			System.out.println("aaaaaa: " + amount);

			String[] parts = theInput.split("[(),]");
			String account1 = parts[1];
			String account2 = parts[2];



			// Extract Client and Amount
			Integer ClientFrom = null;
			if (account1.contains("ClientA")){
				ClientFrom = 0;
			}
			else if (theInput.contains("ClientB")){
				ClientFrom = 1;			
			}
			else if (theInput.contains("ClientC")){
				ClientFrom = 2;
			}
			else {
				System.out.println("Error - client not recognised.");
				}

				Integer ClientTo = null;
			if (account1.contains("ClientA")){
				ClientTo = 0;
			}
			else if (theInput.contains("ClientB")){
				ClientTo = 1;			
			}
			else if (theInput.contains("ClientC")){
				ClientTo = 2;
			}
			else {
				System.out.println("Error - client not recognised.");
				}
			// Add the amount to the client's account
			mySharedVariable[ClientFrom] = mySharedVariable[ClientFrom] - amount;
			mySharedVariable[ClientTo] = mySharedVariable[ClientTo] + amount;
			theOutput = "You have transferred " + amount + " from " + account1 + " to " + account2;
		 //Return the output message to the ActionServer
		System.out.println(theOutput);
		return theOutput;
		}
		return theOutput;
	}
}

