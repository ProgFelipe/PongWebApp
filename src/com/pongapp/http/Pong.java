package com.pongapp.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pongapp.rabbitmq.Rabbit;
import com.pongapp.rabbitmq.Recive;
import com.pongapp.rabbitmq.Send;

/**
 * Servlet implementation class Pong
 */
@WebServlet(name="Pong", urlPatterns={"/Pong"})
public class Pong extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Rabbit rabbitmq;
    /**
     * @throws Exception 
     * @see HttpServlet#HttpServlet()
     */
    public Pong() throws Exception {
        super();
        if(rabbitmq == null){
		  rabbitmq = new Rabbit();
		  rabbitmq.createConnections();
		  rabbitmq.recive = new Recive();
		  rabbitmq.recive.setReciveChannel(rabbitmq.reciveChannel);
		  rabbitmq.send = new Send();
		  rabbitmq.send.setSendChannel(rabbitmq.sendChannel);
		  //rabbitmq.recive.reciveAsyncLayer();
		  rabbitmq.recive.reciveSyncLayer();
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("(Pong) Recived Messages: \n"+rabbitmq.recive.numRecivedMessages+"\n"+
		" Processed: \n"+rabbitmq.recive.processedMessages);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
