// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String userID;
 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String id, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.userID = id;
    this.clientUI = clientUI;
    openConnection();
	this.sendToServer("#login "+userID);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	  if(message.charAt(0)!='#') {
	    try
	    {
	      sendToServer(message);
	    }
	    catch(IOException e)
	    {
	      clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
	  }
	  else 
	  {
		handleCommand(message);
	  }
  }
  
  
  public void handleCommand(String message) 
  {
	  if(message.startsWith("#sethost ")) {
		  String host = message.split(" ")[1];
		  setHost(host);
	  }
	  else if(message.startsWith("#setport ")) {
		  int port = Integer.parseInt(message.split(" ")[1]);
		  setPort(port);
	  }
	  else if(message.startsWith("#login"))
	  {
		  try {
			  String login = message.split(" ")[1];
			  userID = login;
			  openConnection();
			  this.sendToServer("#login "+login);
		  }catch(Exception e) {
			  clientUI.display("Couldn't log in.");
		  }
		  
	  }
	  else {
		  switch(message) {
		  case "#quit":
			  clientUI.display("Quitting...");
			  quit();
			  break;
		  case "#logoff":
			  try {
				  clientUI.display("Logging off...");
				  this.closeConnection();
				  break;
			  }
			  catch(Exception e)
			  {
				  clientUI.display("Couldn't log off.");
				  break;
			  }
		  case "#gethost":
			  clientUI.display(getHost());
			  break;
			  
		  case "#getport":
			  clientUI.display(String.valueOf(getPort()));
			  break;
			  
		  default:
			  clientUI.display("Unknown command.");
			  break;
			  
		  }
		  
			  
	  }
  }
  /**
   * This method terminates the client once the server has stopped
   */
  public void connectionClosed() 
  {
	  clientUI.display("Connection to server has been closed.");
  }
  
  public String getUserID() {
	  return userID;
  }
  
  /**
   * This method terminates the client when an exception is thrown by its thread
   * 
   * @param exception The exception thrown by the server thread.
   */
  public void connectionException(java.lang.Exception exception) 
  {
	  clientUI.display("The server has shut down.");
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
