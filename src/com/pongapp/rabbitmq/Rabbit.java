package com.pongapp.rabbitmq;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Rabbit{

	  public static Connection connection;
	  public static Channel sendChannel;
	  public static Channel reciveChannel;
	  public static Send send;
	  public static Recive recive;
	  
	  public void createConnections()throws Exception {
		    ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    connection = factory.newConnection();
		    reciveChannel = connection.createChannel();
		    sendChannel = connection.createChannel();
	  }
	  
	  public void closeConnection()throws Exception {
			sendChannel.close();
			connection.close();  
			}
}