import common.ChatIF;

import java.io.IOException;
import java.util.Scanner;
import client.ChatClient;

public class ServerConsole implements ChatIF {

	Scanner fromConsole;
	
	EchoServer server;
	
	final public static int DEFAULT_PORT = 5555;
	
	
	  public ServerConsole(int port) 
	  {
	    try 
	    {
	      server= new EchoServer(port, this);
	      server.listen();
	      
	    } 
	    catch(Exception exception) 
	    {
	      System.out.println("Error: Can't setup connection!"
	                + " Terminating server.");
	      System.exit(1);
	    }
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }
	  
	  
	  public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
	  public void display(String message) 
	  {
		  System.out.println(message);
	  }
	
	  /**
	   * This method is responsible for the creation of 
	   * the server instance (there is no UI in this phase).
	   *
	   * @param args[0] The port number to listen on.  Defaults to 5555 
	   *          if no argument is entered.
	   */
	  public static void main(String[] args) 
	  {
	    int port = 0; //Port to listen on
	    
	    
	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
	    
	    ServerConsole console = new ServerConsole(port);
	    console.accept(); //wait for console data
	  }
	
}
