package Hmm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

public class InitHelper {
	/**
	 * ��ʼ���� key��v-���� ����ʽ
	 */
	HashMap<String, POS> matrixMap;
	
	int sumOfMatrix = 0;
	
	public InitHelper(){
		matrixMap = new HashMap<String, POS>();
	}
	
	public void loadDict(String path){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String lineStr = null;
			while((lineStr = reader.readLine()) != null ){
				if (lineStr.length() == 0) {
					continue;
				}
				lineStr = lineStr.substring(23);
				String[] lineArr = lineStr.split("[��|��]/w  ");
				for (int i = 0; i < lineArr.length; i++) {
					String item = lineArr[i];
					String[] itemArr = item.split("  ");
					String tmpStr = itemArr[0];
					String[] tmpArr = tmpStr.split("/");
					if (tmpStr.length() < 2) {
						continue;
					}
					String wordStr = tmpArr[tmpArr.length - 2];
					String posStr = tmpArr[tmpArr.length - 1];
					if (wordStr.startsWith("[")) {
						wordStr = wordStr.substring(1);
					}
					int rightIndex = posStr.indexOf("]");
					if (rightIndex != -1) {
						posStr = posStr.substring(0, rightIndex);
					}
					String key = posStr;
					POS model;
					if (!matrixMap.containsKey(key)) {
						model = new POS();
						model.iPos = posStr;
						model.num = 1;
						matrixMap.put(key, model);
					}else{
						model = matrixMap.get(key);
						model.num += 1;
					}
					
					//�ܵı�ע��Ŀ����һ��
					sumOfMatrix ++;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void calFrequency(){
		Iterator<String> iterator = matrixMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			POS model = matrixMap.get(key);
			model.frequency = ((double)model.num / sumOfMatrix);
		}
		System.out.println(sumOfMatrix);
	}
	
	public void write(String path){
		try {
			PrintWriter writer = new PrintWriter(new File(path));
			Iterator<String> iterator = matrixMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				POS model = matrixMap.get(key);
				String tmpStr = model.iPos + "," + model.num + "," + model.frequency + "\r\n";
				writer.write(tmpStr);
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args){
		InitHelper helper = new InitHelper();
		long start = System.currentTimeMillis();
		
		System.out.println("begin loaddict");
		helper.loadDict("dict/199801.txt");
		
		System.out.println("begin calfrequency");
		helper.calFrequency();
		
		System.out.println("begin write");
		helper.write("dict/hmm_init.txt");
		System.out.println("end write");
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("time:"+time);
	}
}
