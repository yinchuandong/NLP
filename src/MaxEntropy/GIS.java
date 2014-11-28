package MaxEntropy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 最大熵的GIS算法
 * @author wangjiewen
 *
 */
public class GIS {

	/**
	 * 训练样本的集合 N
	 */
	private ArrayList<String[]> trainList = null;
	
	private HashSet<String> labelSet = null;
	
	/**
	 * 特征的集合 n
	 */
	private HashMap<String, Integer> featuresMap = null;
	
	/**
	 * 特征和id下标的映射
	 */
	private HashMap<String, Integer> indexMap = null;
	
	/**
	 * 经验概率
	 */
	private double[] ep_ = null;
	
	/**
	 * 期望概率
	 */
	private double[] ep = null;
	
	/**
	 * 拉格朗日乘子 λ
	 */
	private double[] lambda = null;
	
	/**
	 * 上一次迭代的拉格朗日乘子
	 */
	private double[] lambdaOld = null;
	
	/**
	 * 判断收敛的界限
	 */
	private double epsilon = 0.01;
	
	/**
	 * GIS 算法的最大迭代次数
	 */
	private double maxIter = 1000;
	
	/**
	 * 样本的数量
	 */
	private int N = 0;
	
	/**
	 * GIS的系数
	 */
	private int C = 0;
	
	
	
	public GIS(){
		trainList = new ArrayList<String[]>();
		labelSet = new HashSet<String>();
		featuresMap = new HashMap<String, Integer>();
		indexMap = new HashMap<String, Integer>();
		
		//初始化操作
		loadData();
		initParam();
	}
	
	private void loadData(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("MaxEntropy/train.txt")));
			String buff = null;
			while((buff = reader.readLine()) != null){
				String[] fields = buff.split(" ");
				String label = fields[0];
				labelSet.add(label);
				
				for (int i = 1; i < fields.length; i++) {
					String key = genKey(label, fields[i]);
					if(!featuresMap.containsKey(key)){
						featuresMap.put(key, 1);
					}else{
						int count = featuresMap.get(key) + 1;
						featuresMap.put(key, count);
					}
				}
				trainList.add(fields);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initParam(){
		this.N = trainList.size();
		
		for (String[] fields : trainList) {
			int len = fields.length - 1; //第一列是label
			if(len > this.C){
				this.C = len;
			}
		}
		
		//计算经验概率 ep_
		Iterator<String> iter = featuresMap.keySet().iterator();
		int featLen = featuresMap.size();
		this.ep_ = new double[featLen];
		for (int i = 0; i < featLen; i++) {
			String key = iter.next();
			int count = this.featuresMap.get(key);
			double prob = (double)count / (double)this.N;
			
			this.ep_[i] = prob;
			indexMap.put(key, i);
		}
		
		//初始化 λ
		this.lambda = new double[featLen];
		this.lambdaOld = lambda.clone();
	}
	
	private static String genKey(String label, String field){
		return label + "-" + field;
	}
	
	/**
	 * 计算Z里面的 exp(∑ λi*fi(a,b))
	 * @param features 特征数组
	 * @param label 分类的标签
	 * @return
	 */
	private double zFunc(String[] features, String label){
		double sum = 0.0;
		for (int i = 0; i < features.length; i++) {
			String feat = features[i];
			String key = genKey(label, feat);
			if (this.featuresMap.containsKey(key)) {//即：fi(a,b) == 1
				int index = this.indexMap.get(key);
				sum += this.lambda[index];
			}
		}
		sum = Math.exp(sum);
		return sum;
	}
	
	/**
	 * 计算 p`(b|a) = (1/Z)*exp(∑ λi*fi(a,b))
	 * @param features
	 * @param label
	 * @return
	 */
	private double pFunc(String[] features, String label){
		double prob = 0.0;
		
		//求 z = ∑exp(∑ λi*fi(a,b))
		double Z = 0.0;
		for(String l : this.labelSet){
			Z += zFunc(features, l);
		}
		
		prob = (1.0/Z) * zFunc(features, label);
		return prob;
	}
	
	/**
	 * 计算期望概率 Ep
	 * @return
	 */
	private double[] calcEp(){
		double[] ep = new double[this.featuresMap.size()];
		
		for (String[] record : this.trainList) {
			//第一项是label
			String[] features = Arrays.copyOfRange(record, 1, record.length);
			
			for (String label : this.labelSet) {
				double prob = pFunc(features, label);
				
				for (String feature : features) {
					String key = genKey(label, feature);
					if (this.featuresMap.containsKey(key)) {
						int index = this.indexMap.get(key);
						// ∑ p(a) * p(b|a) * f(a,b), p(a) = 1/N
						ep[index] += (1.0/N) * prob;
					}
				}
			}
		}
		
		return ep;
	}
	
	/**
	 * 判断是否满足GIS收敛的条件
	 * @param lambda
	 * @param lambdaOld
	 * @return
	 */
	private boolean isConvergent(double[] lambda, double[] lambdaOld){
		int len = lambda.length;
		for(int i = 0; i < len; i++){
			if(Math.abs(lambda[i] - lambdaOld[i]) >= this.epsilon){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 进行训练
	 */
	public void train(){
		for (int k = 0; k < this.maxIter; k++) {
			System.out.println("iter -->" + k);
			if (k >= 621) {
				System.out.println("----");
			}
			this.ep = this.calcEp();
			this.lambdaOld = this.lambda.clone();
			
			int len = this.lambda.length;
			for (int i = 0; i < len; i++) {
				//update lambda
				this.lambda[i] += (1.0 / C) * Math.log(this.ep_[i] / this.ep[i]);
			}
			
			//判断是否收敛
			if (this.isConvergent(this.lambda, this.lambdaOld)) {
				break;
			}
		}
	}
	
	
	public void predict(String[] features){
		for (String label : this.labelSet) {
			double prob = this.pFunc(features, label);
			System.out.println(label + " " + prob);
		}
	}
	
	
	
	public static void main(String[] args){
		GIS entropy = new GIS();
		entropy.train();
		System.out.println();
		System.out.println("---------------");
		entropy.predict(new String[]{"Sunny","Happy", "Dry"});
		
		System.out.println("---------------");
		entropy.predict(new String[]{"Cloudy","Sad", "Humid"});
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
