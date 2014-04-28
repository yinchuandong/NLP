package PCFGParsing;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Pcfg {

	private HashMap<String, Double> guideMap = null;//语符集规则
	private ArrayList<String> wordsList = null;//等待分析的词
	private ArrayList<String> nonTermList = null;//非终端语符集
	
	/**
	 *每一个方格内的得分，key为0,1,NP的形式,分别代表i行，j列，NP词性
	 */
	private HashMap<String, Double> scoreMap = null;

	public Pcfg(){
		guideMap = new HashMap<String, Double>();
		wordsList = new ArrayList<String>();
		nonTermList = new ArrayList<String>();
		scoreMap = new HashMap<String, Double>();
		this.init();
	}
	
	private void init(){
		wordsList.add("fish");
		wordsList.add("people");
		wordsList.add("fish");
		wordsList.add("tanks");
	}
	
	public String loadGuide(String filePath){
		guideMap.clear();
		nonTermList.clear();
		String resultStr = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String lineStr = null;
			while((lineStr = reader.readLine()) != null){
				resultStr += lineStr + "\r\n";
				String[] lineArr = lineStr.split(",");
				if(lineArr.length < 2)	continue;
				guideMap.put(lineArr[0], Double.parseDouble(lineArr[1]));
				nonTermList.add(lineArr[0]);

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * 预处理词
	 */
	public void preprocess(){
		for(int i=0; i<wordsList.size();i++){
			for(String A : nonTermList){
				String key = A + "->" + wordsList.get(i);
				if(guideMap.containsKey(key)){
					double score = guideMap.get(key);
					String skey = i + "," + (i+1) + "," + A;
					scoreMap.put(skey,score);
				}
			}
		}
	}


	public static void main(String[] args){
		Pcfg model = new Pcfg();
		String nonTermStr = model.loadGuide("pcfg_nonterm_guide.txt");
		System.out.println(nonTermStr);
		model.preprocess();
	}

}
