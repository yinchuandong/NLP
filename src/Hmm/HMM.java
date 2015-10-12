package Hmm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HMM {

	public class Vet {
		/** ��ǰ�ʵ�id */
		int id;
		/** ��ǰ��ά�رȸ��� */
		double frequency;
		/** �ʵ����� */
		String word;
		/** ��ע */
		String pos;
		/** ǰ���ڵ� */
		Vet preVet;
	}

	/** ������˹ƽ��m */
	public double m = 1000000;
	/** ������˹ƽ��p */
	public double p = 0.5;

	/** ���Ա�ע�� */
	private HashSet<String> posSet;
	/** ��� */
	private ArrayList<String> wordList;

	private HashMap<String, POS> initMap;
	private HashMap<String, POS> emitMap;
	private HashMap<String, POS> tranMap;
	
	/** ÿһ�з�����ʵĺ� */
	private HashMap<String, Integer> sumOfRowEmit;
	/** ÿһ��ת�Ƹ��ʵĺ� */
	private HashMap<String, Integer> sumOfRowTran;

	HashMap<String, Vet> resultMap;
	ArrayList<Vet> finalVetList;
	Vet maxFinalVet;
	
	public HMM(){
		
	}
	
	private void init(){
		
	}
	
	private void loadInitProb(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("dict/hmm_init.txt")));
			
			String buff = null;
			while((buff = reader.readLine()) != null){
				String[] arr = buff.split(",");
				if (arr.length != 3) {
					continue;
				}
				
				String iPos = arr[0];
				int num = Integer.parseInt(arr[1]);
				double frequency = Double.parseDouble(arr[2]);
				POS pos = new POS(iPos, num, frequency);
				initMap.put(iPos, pos);
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadEmitProb(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("dict/hmm_emission.txt")));
			
			String buff = null;
			while((buff = reader.readLine()) != null){
				String[] arr = buff.split(",");
				if (arr.length != 4) {
					continue;
				}
				
				String iPos = arr[0];
				String jPos = arr[1];
				int num = Integer.parseInt(arr[2]);
				double frequency = Double.parseDouble(arr[3]);
				String key = iPos + "-" + jPos;
				POS pos = new POS(iPos, jPos, num, frequency);
				emitMap.put(key, pos);
				
				if (!sumOfRowEmit.containsKey(iPos)) {
					sumOfRowEmit.put(iPos, 1);
				}else{
					sumOfRowEmit.put(iPos, sumOfRowEmit.get(iPos) + 1);
				}
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void loadTranProb(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("dict/hmm_tran.txt")));
			
			String buff = null;
			while((buff = reader.readLine()) != null){
				String[] arr = buff.split(",");
				if (arr.length != 4) {
					continue;
				}
				
				String iPos = arr[0];
				String jPos = arr[1];
				int num = Integer.parseInt(arr[2]);
				double frequency = Double.parseDouble(arr[3]);
				String key = iPos + "-" + jPos;
				POS pos = new POS(iPos, jPos, num, frequency);
				tranMap.put(key, pos);
				
				if (!posSet.contains(iPos)) {
					posSet.add(iPos);
				}
				
				if (!sumOfRowTran.containsKey(iPos)) {
					sumOfRowTran.put(iPos, 1);
				}else{
					sumOfRowTran.put(iPos, sumOfRowTran.get(iPos) + 1);
				}
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public static void main(String[] args){
		
		
		
		
		
		
		
	}
	
	
}
