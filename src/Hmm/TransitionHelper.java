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

import com.sun.xml.internal.ws.api.ha.StickyFeature;

public class TransitionHelper {
	
	/**
	 * 转移概率 key：v-n的形式
	 */
	HashMap<String, PosModel> matrixMap;
	
	/**
	 * 矩阵一行的总和 key: v的形式，表示v转移到其他的概率
	 */
	HashMap<String, Integer> sumOfLine;
	
	public TransitionHelper(){
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
				for (int i = 0; i < lineArr.length - 1; i++) {
					String preItem = lineArr[i];
					String[] preArr = preItem.split("/");
					String nextItem = lineArr[i+1];
					String[] nextArr = nextItem.split("/");
					if (preArr.length < 2 || nextArr.length < 2) {
						continue;
					}
					String iPosStr = preArr[preArr.length - 1];
					String jPosStr = nextArr[nextArr.length - 1];
					int iIndex = iPosStr.indexOf("]");
					if (iIndex != -1) {
						iPosStr = iPosStr.substring(0, iIndex);
					}
					
					int jIndex = jPosStr.indexOf("]");
					if (jIndex != -1) {
						jPosStr = jPosStr.substring(0, jIndex);
					}
					
					PosModel posModel;
					String key = iPosStr + "-" + jPosStr;
					if (!matrixMap.containsKey(key)) {
						posModel = new PosModel();
						posModel.iPos = iPosStr;
						posModel.jPos = jPosStr;
						posModel.num = 1;
						matrixMap.put(key, posModel);
					}else{
						posModel = matrixMap.get(key);
						posModel.num += 1;
					}
					
					if (sumOfLine.containsKey(iPosStr)) {
						int sum = sumOfLine.get(iPosStr);
						sum ++;
						sumOfLine.put(iPosStr, sum);
					}else{
						sumOfLine.put(iPosStr, 1);
					}
				}
				
			}
			reader.close();
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
		TransitionHelper helper = new TransitionHelper();
		long start = System.currentTimeMillis();
		
		System.out.println("begin loaddict");
		helper.loadDict("dict/199801.txt");
		
		System.out.println("begin calfrequency");
		helper.calFrequency();
		
		System.out.println("begin write");
		helper.write("dict/tran.txt");
		System.out.println("end write");
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("time:"+time);
	}
}
