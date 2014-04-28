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

	private HashMap<String, Double> nonTermsGuide = null;//ÖÕ¶ËÓï·û¼¯
	private HashMap<String, Double> termsGuide = null;//·ÇÖÕ¶ËÓï·û¼¯
	private ArrayList<String> wordsList = null;//µÈ´ı·ÖÎöµÄ´Ê
	private ArrayList<String> noTermList = null;//·ÇÖÕ¶ËÓï·û¼¯
	
	public Pcfg(){
		nonTermsGuide = new HashMap<String, Double>();
		termsGuide = new HashMap<String, Double>();
	}
	
	private void init(){
		
	}
	
	public String loadNonTermsFromFile(String filePath){
		nonTermsGuide.clear();
		String resultStr = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String lineStr = null;
			while((lineStr = reader.readLine()) != null){
				resultStr += lineStr + "\r\n";
				String[] lineArr = lineStr.split(",");
				if(lineArr.length < 2)	continue;
				nonTermsGuide.put(lineArr[0], Double.parseDouble(lineArr[1]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultStr;
	}	

	public String loadTermsFromFile(String filePath){
		termsGuide.clear();
		String resultStr = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String lineStr = null;
			while((lineStr = reader.readLine()) != null){
				resultStr += lineStr + "\r\n";
				String[] lineArr = lineStr.split(",");
				if(lineArr.length < 2) continue;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultStr;
	}

	public static void main(String[] args){
		Pcfg model = new Pcfg();
		String termStr = model.loadTermsFromFile("pcfg_term_guide.txt");
		String nonTermStr = model.loadNonTermsFromFile("pcfg_nonterm_guide.txt");
		System.out.println(termStr);
		System.out.println(nonTermStr);
	}

}
