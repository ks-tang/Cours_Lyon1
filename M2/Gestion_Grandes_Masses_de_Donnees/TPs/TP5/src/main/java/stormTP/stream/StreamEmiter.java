package stormTP.stream;


import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.text.SimpleDateFormat;

/**
 * Classe permettant d'émettre un output en UDP.
 */
public class StreamEmiter implements Serializable{
	

	private static final long serialVersionUID = 4262369370788016342L;;
	
	 private int port = -1;
	 String ipM = null;
	 DatagramSocket udpSocket = null;
	 InetAddress mcIPAddress = null;
	 DatagramPacket packet = null;
	 InetSocketAddress group = null;
	 MulticastSocket skt = null;
	 NetworkInterface netIf = null;

	public StreamEmiter(int port, String ip){
			this.port = port;
			this.ipM = ip;
		
	}


	public void send(String row){

		 try{


		udpSocket = new DatagramSocket();
		mcIPAddress = InetAddress.getByName(this.ipM);
		group = new InetSocketAddress( mcIPAddress, this.port );
		netIf = NetworkInterface.getByName("enp1s0");
		skt = new MulticastSocket( this.port );
		packet = null;

		skt.joinGroup( new InetSocketAddress(mcIPAddress, 0), netIf );
		byte[] msg = null;
		msg = row.getBytes();
		try{
			packet = new DatagramPacket(msg, msg.length, group);
			skt.send(packet);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
			System.out.println( sdf.format(System.currentTimeMillis()) + ":" + row.substring(0,100)   );

		}catch(IOException e){ e.printStackTrace();}

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		 }catch(IOException e){
			 e.printStackTrace();
		 }finally{
			udpSocket.close();
		 }

	}


	@Override
		public String toString(){
			return "StreamEmiter[port="+ this.port +", ipMultiC="+ this.ipM +"]"; 
		}
		

}
