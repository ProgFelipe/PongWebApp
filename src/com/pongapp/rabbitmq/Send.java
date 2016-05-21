package com.pongapp.rabbitmq;
import com.rabbitmq.client.Channel;

public class Send {
	private final static String QUEUE_NAME = "pingChannel";
	static Channel channel;
	
	public void setSendChannel(Channel sendChannel){
		channel = sendChannel;
	}
  public static void sendDisponibility(String messageNumber)throws Exception {
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    channel.basicPublish("", QUEUE_NAME, null, messageNumber.getBytes("UTF-8"));
  }
}