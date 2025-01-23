package stormTP.stream;

import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Classe permettant aux spout d'écouter un flux UDP
 */

public class StreamBuffer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Queue<String> fifo = null;
	private int port = -1;
	String ipM = "";

	
	public StreamBuffer(int port, String ip){
			this.port = port;
			this.ipM = ip;
			this.fifo = new ConcurrentLinkedQueue<String>();

		
	}
	
	
	public void listenStream()
    {

		MulticastSocket mcSocket = null;
		InetAddress mcIPAddress = null;
		NetworkInterface netInf = null;
		InetSocketAddress group = null;

		try{
			mcIPAddress = InetAddress.getByName(this.ipM);
			group = new InetSocketAddress( mcIPAddress, this.port );
			netInf = NetworkInterface.getByName("enp1s0");
			mcSocket = new MulticastSocket(this.port);

			//System.out.println("Multicast ("+ this.ipM +") Receiver running at:"+ mcSocket.getLocalSocketAddress());
			mcSocket.joinGroup(new InetSocketAddress(mcIPAddress, 0), netInf);

			DatagramPacket packet = new DatagramPacket(new byte[3072], 3072);

			String msg = null;

			mcSocket.receive(packet);
			msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
			System.out.println( msg );
			this.fifo.add( msg );

		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				mcSocket.leaveGroup(group,netInf);
				mcSocket.close();
			}catch(IOException ioe){ ioe.printStackTrace();}
		}


	}
	
	
	 public String readTuple() throws Exception {
	      
		 return  this.fifo.poll();
		  
		   
	  }
	
	
	
	 
		

}
