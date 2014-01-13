package test;

import java.rmi.Naming;

public class IndexServer1 {
	public static void main(String[] args){
		
		try {
			
//			String url = "rmi://192.168.233.15:1099/";//rmi://222.201.101.15:1099/
//			RMIClientService clientService = (RMIClientService)Naming.lookup(url + "RMIClientService");
//			String msg = clientService.getYourName();
//			System.out.println(msg);
			
//			String url = "rmi://192.168.233.15:1099/";//rmi://222.201.101.15:1099/
			String url = "rmi://222.201.101.15:1099/";//rmi://222.201.101.15:1099/
			RMIService serverService = (RMIService)Naming.lookup(url + "RMIService");
			//String msg = serverService.echo("20111003632 尹川东");
			byte[] msg = serverService.getMessage("20111003632 尹川东");
			System.out.println(new String(msg,"utf-8"));
			String msg1 = "20111003632 尹川东";
			String msg2 = "经过一学期的学习，我觉得互联网程序设计让我掌握了不少的知识，特别是java的编程。对socket,mysql,rmi都有了很深入的了解，老师讲课讲得很好，大家做实验的机会也很多，希望以后还有机会能选谢院长的课";
			String msg3 = serverService.putMessage(msg1, msg2.getBytes());
			System.out.println(msg3);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
