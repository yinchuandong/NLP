package test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIService extends Remote{

	public byte[] getMessage(String msg) throws RemoteException;
	public String putMessage(String msg1, byte[] msg2) throws RemoteException;
}
