package com.pongapp.rabbitmq;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Recive extends Send{
	
	private final static String QUEUE_NAME = "pongChannel";
	static int waitingTime = 2000; //milliseconds, time to wait for response.
	static Channel channel;
	public static int numRecivedMessages;
	public static int processedMessages;
	
	public void setReciveChannel(Channel reciveChannel){
		channel = reciveChannel;
	}
	
	public void reciveAsyncLayer() throws Exception {
        int threadNumber = 4;
        final ExecutorService threadPool =  new ThreadPoolExecutor(threadNumber, threadNumber,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        pongReceptionAsync(channel, threadPool);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
            	System.out.println("Invoking shutdown hook...");
            	System.out.println("Shutting down thread pool...");
                threadPool.shutdown();
                try {
                    while(!threadPool.awaitTermination(10, TimeUnit.SECONDS));
                } catch (InterruptedException e) {
                	System.out.println("Interrupted while waiting for termination");
                }
                System.out.println("Thread pool shut down.");
                System.out.println("Done with shutdown hook.");
            }
        });
	}
	
    public static void pongReceptionAsync(final Channel channel, final ExecutorService threadPool) throws Exception{
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      System.out.println("*(Pong): Waiting for Messages.");
	    Consumer consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		          throws IOException {
		    	  try {
		    		  numRecivedMessages++;
			    	  waitingOperation(threadPool, body);
		    	  }catch (Exception e) {
	                    e.printStackTrace();
	              }
		      }
		    };		    
		    channel.basicConsume(QUEUE_NAME, true, consumer);
	  }
	  
    public static void reciveSyncLayer() throws IOException{
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);	        
	    Consumer consumer = new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	          throws IOException {
	    	  numRecivedMessages++;
	        try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            processedMessages++;
            System.out.println(String.format("(Pong): %s processed!", new String(body)));
            try {
				sendDisponibility(new String(body));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	    };
	    channel.basicConsume(QUEUE_NAME, true, consumer);
    }
    
	 private static void waitingOperation(final ExecutorService threadPool,  byte[] body){
         threadPool.submit(new Runnable() {
             public void run() {
                 try {
                	 Thread.sleep(waitingTime);
                     processedMessages++;
                     System.out.println(String.format("(Pong): %s processed!", new String(body)));
                     sendDisponibility(new String(body));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
         });
	  }
}
