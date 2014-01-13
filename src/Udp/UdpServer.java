package Udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import sun.nio.cs.MS1250;

public class UdpServer {

	int port = 7007;
	private DatagramSocket socket;
	private InetAddress remoteIP;
	private int remotePort;
	
	public UdpServer(){
		try {
			socket = new DatagramSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		new Thread(){
			public void run(){
				
				try {
					String msg = null;
					while((msg = receive())!=null){
						 
						 System.out.println(msg);
						 send("20111003632 Òü´¨¶«");
//						 break;
					}
					System.out.println("==========");
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	  
	  
	  public void send(String msg) throws IOException {
		  byte[] buff = msg.getBytes("GBK");
		  DatagramPacket packet = new DatagramPacket(buff, buff.length, remoteIP, remotePort);
		  socket.send(packet);
	  }
	  
	  public String receive() throws IOException{
		  byte[] buff = new byte[1024];
		  DatagramPacket packet = new DatagramPacket(buff, buff.length);
		  socket.receive(packet);
		  remoteIP = packet.getAddress();
		  remotePort = packet.getPort();
		  System.out.println(remoteIP + ":" + remotePort);
		  return new String(packet.getData(),0,packet.getLength(), "GBK");
	  }
	  
	  public void close(){
		  socket.close();
	  }
	
	public static void main(String[] args){
		UdpServer server = new UdpServer();
		server.start();
		System.out.println("°ó¶¨");
	}
}
