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

public class EmissionHelper {
	/**
	 * 发射概率 key：v-充满 的形式
	 */
	HashMap<String, PosModel> matrixMap;
	
	/**
	 * 矩阵一行的总和 key: v的形式，表示v转移到其他的概率
	 */
	HashMap<String, Integer> sumOfLine;
	
	public EmissionHelper(){
		matrixMap = new HashMap<String, PosModel>();
		sumOfLine = new HashMap<String, Integer>();
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
				String[] lineArr = lineStr.split("  ");
				for (int i = 0; i < lineArr.length; i++) {
					String item = lineArr[i];
					String[] itemArr = item.split("/");
					if (itemArr.length < 2) {
						continue;
					}
					String wordStr = itemArr[itemArr.length - 2];
					String posStr = itemArr[itemArr.length - 1];
					if (wordStr.startsWith("[")) {
						wordStr = wordStr.substring(1);
					}
					int rightIndex = posStr.indexOf("]");
					if (rightIndex != -1) {
						posStr = posStr.substring(0, rightIndex);
					}
					String key = posStr + "-" + wordStr;
					PosModel model;
					if (!matrixMap.containsKey(key)) {
						model = new PosModel();
						model.iPos = posStr;
						model.jPos = wordStr;
						model.num = 1;
						matrixMap.put(key, model);
					}else{
						model = matrixMap.get(key);
						model.num += 1;
					}
					
					if (sumOfLine.containsKey(posStr)) {
						int sum = sumOfLine.get(posStr);
						sum ++;
						sumOfLine.put(posStr, sum);
					}else{
						sumOfLine.put(posStr, 1);
					}
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
			PosModel model = matrixMap.get(key);
			int sum = sumOfLine.get(model.iPos);
			model.frequency = ((double)model.num / sum);
		}
	}
	
	public void write(String path){
		try {
			PrintWriter writer = new PrintWriter(new File(path));
			Iterator<String> iterator = matrixMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				PosModel model = matrixMap.get(key);
				String tmpStr = model.iPos + "," + model.jPos + "," + model.num + "," + model.frequency + "\r\n";
				writer.write(tmpStr);
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args){
		EmissionHelper helper = new EmissionHelper();
		long start = System.currentTimeMillis();
		
		System.out.println("begin loaddict");
		helper.loadDict("dict/199801.txt");
		
		System.out.println("begin calfrequency");
		helper.calFrequency();
		
		System.out.println("begin write");
		helper.write("dict/emission.txt");
		System.out.println("end write");
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("time:"+time);
	}
}
