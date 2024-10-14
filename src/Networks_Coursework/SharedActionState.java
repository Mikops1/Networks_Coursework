package Networks_Coursework;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
				else if (theInput.contains("ClientB")){
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
				Pattern pattern = Pattern.compile("Subtract_Money\\(([^,]+),\\s*([0-9.]+)\\)");
    			//Correct request
				Matcher matcher = pattern.matcher(theInput);

				if (matcher.find()) {
					// Extract Client and Amount
					Integer Client = null;
					String clientInput = matcher.group(1).trim();
					double amount = Double.parseDouble(matcher.group(2).trim());

					if (clientInput == "ClientA"){
						Client = 0;
					}
					else if (clientInput == "ClientB"){
						Client = 1;
					}
					else if (clientInput == "ClientC"){
						Client = 2;
					}
					else {
						System.out.println("Error - client not recognised.");
					}

					// Subtract the amount from the client's account
					mySharedVariable[Client] = mySharedVariable[Client] - amount;
					theOutput = "Your new account amount is: " + mySharedVariable[Client];
			}
    		else { //incorrect request
    			theOutput = "Unfortunately this command is not recognised, please try again.";
		
    		}
 
     		//Return the output message to the ActionServer
    		System.out.println(theOutput);
    		return theOutput;
    	}
		return theOutput;
	}
}

