package Hmm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import sun.net.www.content.audio.aiff;

public class HMM {

	/**
	 * ά�رȽڵ�ĸ���
	 * @author yinchuandong
	 *
	 */
	public class Viterbi {
		/** ��ǰ�ʵ�id */
		int id;
		/** ��ǰ��ά�رȸ��� */
		double prob;
		/** �ʵ����� */
		String word;
		/** ��ע */
		String pos;
		/** ǰ���ڵ� */
		Viterbi preVit;
		
		/**
		 * 
		 * @param id ��ǰ�ʵ�id
		 * @param prob ��ǰ��ά�رȸ���
		 * @param word �ʵ�����
		 * @param pos ��ע
		 * @param preVit ǰ���ڵ�
		 */
		public Viterbi(int id, double prob, String word, String pos, Viterbi preVit) {
			super();
			this.id = id;
			this.prob = prob;
			this.word = word;
			this.pos = pos;
			this.preVit = preVit;
		}
	}

	/** ������˹ƽ��m */
	public double m = 1000000;
	/** ������˹ƽ��p */
	public double p = 0.5;
	public double minProb = 0.00000000000000001;

	/** ���Ա�ע�� */
	private ArrayList<String> posList;
	/** ��� */
	private ArrayList<String> wordList;

	private HashMap<String, POS> initMap;
	private HashMap<String, POS> emitMap;
	private HashMap<String, POS> tranMap;
	
	/** ÿһ�з�����ʵĺ� */
	private HashMap<String, Integer> sumOfRowEmit;
	/** ÿһ��ת�Ƹ��ʵĺ� */
	private HashMap<String, Integer> sumOfRowTran;

	/** �洢����·�ߣ�ģ����ά���� keyΪ: [id-word-pos] */
	HashMap<String, Viterbi> resultMap;
	Viterbi maxFinalVit = null;
	
	public HMM(){
	}
	
	public void init(){
		posList = new ArrayList<String>();
		wordList = new ArrayList<String>();
		initMap = new HashMap<String, POS>();
		emitMap = new HashMap<String, POS>();
		tranMap = new HashMap<String, POS>();
		sumOfRowEmit = new HashMap<String, Integer>();
		sumOfRowTran = new HashMap<String, Integer>();
		resultMap = new HashMap<String, Viterbi>();
		
	}
	
	
	public void run(String sentence){
		if (sentence == null || sentence.equals("")) {
			wordList.add("���");
			wordList.add("��");
			wordList.add("����");
			wordList.add("ʱ");
		}else{
			String[] arr = sentence.split(" ");
			for (String word : arr) {
				wordList.add(word);
			}
		}
		loadInitProb();
		loadEmitProb();
		loadTranProb();
		calcInitProb();
		calcViterbiProb();
		display();
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
			HashSet<String> posSet = new HashSet<String>();
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
			
			//��posSet��Ԫ�ظ���posList
			for(Iterator<String> iter = posSet.iterator(); iter.hasNext(); ){
				posList.add(iter.next());
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void calcInitProb(){
		
		String beginWord = wordList.get(0);
		//�����ʼά�رȸ���
		for (String iPos : posList) {
			String resultKey = "0-" + beginWord + "-" + iPos; //����ά����
			String emitKey = iPos + "-" + beginWord;
			double initProb = initMap.containsKey(iPos) ? initMap.get(iPos).prob : minProb;
			
			double emitProb = minProb;
			if (emitMap.containsKey(emitKey)) {
				emitProb = emitMap.get(emitKey).prob;
			}
			emitProb *= initProb;
			
			Viterbi beginVit = new Viterbi(0, emitProb, beginWord, iPos, null);
			resultMap.put(resultKey, beginVit);
		}
	}
	
	/**
	 * ��ʽ��V_t(j) = max(V_(t-1)_i * a_i_j) * b_j(O_t)
	 * ���У�i Ϊ ǰ��ipos, jΪ��ǰjpos
	 * ����ά�رȸ���
	 */
	private void calcViterbiProb(){
		//��������ľ���
		for (int t = 1; t < wordList.size(); t++) {
			String curWord = wordList.get(t);
			String preWord = wordList.get(t - 1);
			
			//���Ƶ�ǰʱ��: t������max(V_(t-1)_i * a_i_j)
			for (String curPos : posList) {
				double maxPreVitProb = 0;
				Viterbi maxPreVit = null;
				
				//����ǰһʱ��: t-1
				for (String prePos : posList) {
					//��Ӧת�Ƹ���: a_i_j
					double tranProb = minProb;
					String tranKey = prePos + "-" + curPos;
					if (tranMap.containsKey(tranKey)) {
						tranProb = tranMap.get(tranKey).prob;
					}
					
					//��Ӧ(t-1)ʱ�̵�viterbi����
					String preVitKey = (t - 1) + "-" + preWord + "-" + prePos;
					Viterbi preVit = resultMap.get(preVitKey);
					//Vt-1 * a_i_j
					double tmpProb = preVit.prob * tranProb;
					if (tmpProb > maxPreVitProb) {
						maxPreVitProb = tmpProb;
						maxPreVit = preVit;
					}
				}
				
				//����emitProb, ��Ӧ b_j(O_t)�� �������
				String emitKey = curPos + "-" + curWord;
				double emitProb = minProb;
				if (emitMap.containsKey(emitKey)) {
					emitProb = emitMap.get(emitKey).prob;
				}
				
				double vitProb = maxPreVitProb * emitProb;
				Viterbi curVit = new Viterbi(t, vitProb, curWord, curPos, maxPreVit);
				String curVitKey = t + "-" + curWord + "-" + curPos;
				resultMap.put(curVitKey, curVit);
				
				//�������һ���ڵ�
				if (t == wordList.size() - 1) {
					if (maxFinalVit == null || curVit.prob > maxFinalVit.prob) {
						maxFinalVit = curVit;
					}
				}
				
			}
			
		}
	}
	
	private void display(){
		String str = "";
		Viterbi tmpVit = maxFinalVit;
		while(tmpVit != null){
			str = tmpVit.word + "/" + tmpVit.pos + "   " + str;
			tmpVit = tmpVit.preVit;
		}
		System.out.println(str);
	}
	
	
	
	
	public static void main(String[] args){
		
		System.out.println("start, please input a sentence, splited by space:");
		Scanner scanner = new Scanner(System.in);
		String sentence = scanner.nextLine().trim();
		HMM model = new HMM();
		model.init();
		model.run(sentence);
		System.out.println("end");
		
	}
	
	
}
