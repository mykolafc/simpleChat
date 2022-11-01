// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;
import common.ChatIF;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  serverUI.display("Message received: "+msg.toString()+" from "+client.getInfo("loginID"));
	  if(msg.toString().startsWith("#login ")) {
		  if(client.getInfo("commandSent").toString()!="true") {
			  try {
				  String s = client.getInfo("loginID").toString();
				  serverUI.display(s+" has logged on.");
				  this.sendToAllClients(s+" has logged on.");
			  }catch(Exception e){
				  String s = msg.toString().split(" ")[1];
				  client.setInfo("loginID", s);
				  serverUI.display(s+" has logged on.");
				  this.sendToAllClients(s+" has logged on.");
			  }
			  client.setInfo("commandSent", "true");
		  }
		  else {
			  try {
				  client.sendToClient("You've logged in already. Try again later. Goodbye");
				  client.close();
			  }catch(Exception e) {
				  serverUI.display("Error sending error message.");
			  }
		  }
	  }else {
	    this.sendToAllClients(client.getInfo("loginID")+" > "+String.valueOf(msg));
	  }
  }
  
  public void handleMessageFromServerUI(Object msg) {
	  if(msg.toString().charAt(0)!='#') {
	  	  msg = "SERVER MSG > "+String.valueOf(msg);
		  serverUI.display(String.valueOf(msg));
		  this.sendToAllClients(msg);
	  }
	  else 
	  {
		  handleCommand(msg.toString());
	  }
  }
  
  public void handleCommand(String msg) {
	  if(msg.startsWith("#setport ")) {
		  int port = Integer.parseInt(msg.split(" ")[1]);
		  this.setPort(port);
	  }
	  else {
		  switch(msg) {
		  case "#quit":
			  try {
				serverUI.display("Quitting server...");
			  	this.close();
			  	System.exit(0);
			  }catch(Exception e) {
				  serverUI.display("Couldn't quit the program.");
			  }
			  break;
		  case "#stop":
			  try {
				  serverUI.display("Server no longer listening...");
				  this.stopListening();
			  }catch(Exception e) {
				  serverUI.display("Couldn't stop the server.");
			  }
			  break;
		  case "#close":
			  try {
				  serverUI.display("Closing all connections...");
				  this.close();
			  }catch(Exception e) {
				  serverUI.display("Couldn't close the server.");
			  }
			  break;
		  case "#start":
			  try {
				  serverUI.display("Starting server!");
				  this.listen();
			  }catch(Exception e) {
				  serverUI.display("Couldn't start listening.");
			  }
			  break;
		  case "#getport":
			  serverUI.display(String.valueOf(this.getPort()));
			  break;
		  default:
			  serverUI.display("Unknown server command.");
		  }
	  }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	  serverUI.display
      ("Server has stopped listening for connections.");
  }
  
  protected void clientConnected(ConnectionToClient client) 
  {
	  client.setInfo("commandSent", "false");
	  serverUI.display("A new client has connected to the server.");
  }
  
  synchronized protected void clientDisconnected(ConnectionToClient client) 
  {
	  serverUI.display("Client "+client.getInfo("loginID")+" disconnected.");
	  this.sendToAllClients("Client "+client.getInfo("loginID")+" disconnected.");
  }
  
  //Class methods ***************************************************
  
}
//End of EchoServer class
