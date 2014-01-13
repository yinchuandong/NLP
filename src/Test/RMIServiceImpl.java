package test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServiceImpl extends UnicastRemoteObject implements RMIService{

	protected RMIServiceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] getMessage(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String putMessage(String msg1, byte[] msg2) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
