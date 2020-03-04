import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Banker {
	private int numberOfCustomers;	// the number of customers
	private int numberOfResources;	// the number of resources

	private int[] available; 	// the available amount of each resource
	private int[][] maximum; 	// the maximum demand of each customer
	private int[][] allocation;	// the amount currently allocated
	private int[][] need;		// the remaining needs of each customer

	/**
	 * Constructor for the Banker class.
	 * @param resources          An array of the available count for each resource.
	 * @param numberOfCustomers  The number of customers.
	 */
	public Banker (int[] resources, int numberOfCustomers) {
		// TODO: set the number of resources
this.numberOfCustomers = numberOfCustomers;
numberOfResources = resources.length;
maximum = new int[numberOfCustomers][resources.length];
available = resources;
allocation = new int[numberOfCustomers][resources.length];
need = new int[numberOfCustomers][numberOfResources];

		// TODO: set the number of customers

		// TODO: set the value of bank resources to available

		// TODO: set the array size for maximum, allocation, and need

	}

	/**
	 * Sets the maximum number of demand of each resource for a customer.
	 * @param customerIndex  The customer's index (0-indexed).
	 * @param maximumDemand  An array of the maximum demanded count for each resource.
	 */
	public void setMaximumDemand(int customerIndex, int[] maximumDemand) {
		// TODO: add customer, update maximum and need
maximum[customerIndex]=maximumDemand; // 2D array
for(int i  = 0 ; i <numberOfCustomers; i ++){
for(int j = 0 ; j < numberOfResources; j ++){
need[i][j] = maximum[i][j] - allocation[i][j] ; // common sense demand supply

}
}
	}

	/**
	 * Prints the current state of the bank.
	 */
	public void printState() {
        System.out.println("\nCurrent state:");
        // print available
        System.out.println("Available:");
        System.out.println(Arrays.toString(available));
        System.out.println("");

        // print maximum
        System.out.println("Maximum:");
        for (int[] aMaximum : maximum) {
            System.out.println(Arrays.toString(aMaximum));
        }
        System.out.println("");
        // print allocation
        System.out.println("Allocation:");
        for (int[] anAllocation : allocation) {
            System.out.println(Arrays.toString(anAllocation));
        }
        System.out.println("");
        // print need
        System.out.println("Need:");
        for (int[] aNeed : need) {
            System.out.println(Arrays.toString(aNeed));
        }
        System.out.println("");
	}

	/**
	 * Requests resources for a customer loan.
	 * If the request leave the bank in a safe state, it is carried out.
	 * @param customerIndex  The customer's index (0-indexed).
	 * @param request        An array of the requested count for each resource.
	 * @return true if the requested resources can be loaned, else false.
	 */
	public synchronized boolean requestResources(int customerIndex, int[] request) {
		// TODO: print the request
		System.out.println("Customer " + customerIndex + " requesting");
        System.out.println(Arrays.toString(request));
		// TODO: check if request larger than need
		for(int i = 0 ; i < numberOfResources; i ++){
if(need[customerIndex][i] < request[i]){
return false;
}
if(available[i] < request[i]){ // larger than avail
return false;
}
}

		
		
		// TODO: check if the state is safe or not
if(checkSafe(customerIndex, request)){

for(int i = 0 ; i < numberOfResources; i ++){
available[i]  -= request[i]; //grant it
allocation[customerIndex][i] += request[i] ; //update reserves
need[customerIndex][i] -= request[i];
}

return true;

}
else{

return false;
}
		
		// TODO: if it is safe, allocate the resources to customer customerNumber
		
	//	return true;
	}

	/**
	 * Releases resources borrowed by a customer. Assume release is valid for simplicity.
	 * @param customerIndex  The customer's index (0-indexed).
	 * @param release        An array of the release count for each resource.
	 */
	public synchronized void releaseResources(int customerIndex, int[] release) {
		// TODO: print the release
		System.out.println("Customer " + customerIndex + " releasing");
		System.out.println(Arrays.toString(release));
		
		// TODO: release the resources from customer customerNumber
		
for(int i = 0 ; i < numberOfResources; i ++){
allocation[customerIndex][i] -= release[i];
available[i] += release[i];
need[customerIndex][i] += release[i];

}
	}

	/**
	 * Checks if the request will leave the bank in a safe state.
	 * @param customerIndex  The customer's index (0-indexed).
	 * @param request        An array of the requested count for each resource.
	 * @return true if the requested resources will leave the bank in a
	 *         safe state, else false
	 */
	private synchronized boolean checkSafe(int customerIndex, int[] request) {
		// TODO: check if the state is safe
		
		int[][] requirements= new int[numberOfCustomers][numberOfResources]; // our total pool
                int[] remain = new int[numberOfResources] ; //number of available resources
                int[][] satisfy = new int[numberOfCustomers][numberOfResources]; //  to allocate
                int[] work = new int[numberOfCustomers];

                boolean[] can = new boolean[numberOfCustomers]; // to complete it
                boolean success;  // our temp variable for checkingsafe


for(int i = 0 ; i <= this.numberOfResources-1 ; i ++){

remain[i] = this.available[i] - request[i]; // allocating
work[i] = remain[i];

for(int j  = 0 ; j <= this.numberOfCustomers-1; j++){

if(customerIndex == j){
//our customer woohoo
requirements[customerIndex][i] = this.need[customerIndex][i] - request[i];
satisfy[customerIndex][i] = this.allocation[customerIndex][i] +request[i];
 
}
else{
requirements[j][i] = this.need[j][i];

satisfy[j][i] = this.allocation[j][i];

}
}
}


for (int k = 0 ; k <= numberOfCustomers-1; k++){
can[k] = false;
}
success = true;

while(success){
success = false;

for(int i  = 0 ; i <this.numberOfCustomers; i ++){

boolean brim = true;

for(int j = 0 ; j <=this.numberOfResources-1 ; j++){

if(work[j] < requirements[i][j] ){

brim = false;
}
}
if(can[i] ==false && brim){
success = true;
for(int j  = 0; j <=this.numberOfResources-1 ; j++){

work[j] += satisfy[i][j];
}
can[i]= true;
}

}


}

boolean isSafe = true;

for(int i = 0 ; i <= this.numberOfCustomers-1; i++){

if(can[i] ==false)
isSafe  = false;
}
return isSafe;
	}


	/**
	 * Parses and runs the file simulating a series of resource request and releases.
	 * Provided for your convenience.
	 * @param filename  The name of the file.
	 */
	public static void runFile(String filename) {

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(filename));

			String line = null;
			String [] tokens = null;
			int [] resources = null;

			int n, m;

			try {
				n = Integer.parseInt(fileReader.readLine().split(",")[1]);
			} catch (Exception e) {
				System.out.println("Error parsing n on line 1.");
				fileReader.close();
				return;
			}

			try {
				m = Integer.parseInt(fileReader.readLine().split(",")[1]);
			} catch (Exception e) {
				System.out.println("Error parsing n on line 2.");
				fileReader.close();
				return;
			}

			try {
				tokens = fileReader.readLine().split(",")[1].split(" ");
				resources = new int[tokens.length];
				for (int i = 0; i < tokens.length; i++)
					resources[i] = Integer.parseInt(tokens[i]);
			} catch (Exception e) {
				System.out.println("Error parsing resources on line 3.");
				fileReader.close();
				return;
			}

			Banker theBank = new Banker(resources, n);

			int lineNumber = 4;
			while ((line = fileReader.readLine()) != null) {
				tokens = line.split(",");
				if (tokens[0].equals("c")) {
					try {
						int customerIndex = Integer.parseInt(tokens[1]);
						tokens = tokens[2].split(" ");
						resources = new int[tokens.length];
						for (int i = 0; i < tokens.length; i++)
							resources[i] = Integer.parseInt(tokens[i]);
						theBank.setMaximumDemand(customerIndex, resources);
					} catch (Exception e) {
						System.out.println("Error parsing resources on line "+lineNumber+".");
						fileReader.close();
						return;
					}
				} else if (tokens[0].equals("r")) {
					try {
						int customerIndex = Integer.parseInt(tokens[1]);
						tokens = tokens[2].split(" ");
						resources = new int[tokens.length];
						for (int i = 0; i < tokens.length; i++)
							resources[i] = Integer.parseInt(tokens[i]);
						theBank.requestResources(customerIndex, resources);
					} catch (Exception e) {
						System.out.println("Error parsing resources on line "+lineNumber+".");
						fileReader.close();
						return;
					}
				} else if (tokens[0].equals("f")) {
					try {
						int customerIndex = Integer.parseInt(tokens[1]);
						tokens = tokens[2].split(" ");
						resources = new int[tokens.length];
						for (int i = 0; i < tokens.length; i++)
							resources[i] = Integer.parseInt(tokens[i]);
						theBank.releaseResources(customerIndex, resources);
					} catch (Exception e) {
						System.out.println("Error parsing resources on line "+lineNumber+".");
						fileReader.close();
						return;
					}
				} else if (tokens[0].equals("p")) {
					theBank.printState();
				}
			}
			fileReader.close();
		} catch (IOException e) {
			System.out.println("Error opening: "+filename);
		}

	}

	/**
	 * Main function
	 * @param args  The command line arguments
	 */
	public static void main(String [] args) {
		if (args.length > 0) {
			runFile(args[0]);
		}
	}

}
