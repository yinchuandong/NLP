package GA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MyGa {
	
	/**
	 * 种群规模
	 */
	private int scale;
	
	/**
	 * 城市数量
	 */
	private int cityNum;
	
	/**
	 * 城市列表
	 */
	private ArrayList<Integer> cityList;
	
	/**
	 * 最大运行代数
	 */
	private int maxGen;
	
	/**
	 * 当前运行代数
	 */
	private int curGen;
	
	/**
	 * 交叉概率
	 */
	private double pc;
	
	/**
	 * 变异概率
	 */
	private double pm;
	
	/**
	 * 种群中个体的累计概率
	 */
	private double[] pi;
	
	/**
	 *  初始种群，父代种群，行数表示种群规模，一行代表一个个体，即染色体，列表示染色体基因片段
	 */
	private int[][] oldPopulation;
	
	/**
	 * 新的种群，子代种群
	 */
	private int[][] newPopulation;
	
	/**
	 * 种群适应度，表示种群中各个个体的适应度
	 */
	private int[] fitness;
	
	/**
	 * 距离矩阵，每行代表一条染色体
	 */
	private double[][] distance;
	
	/**
	 * 最佳出现代数
	 */
	private int bestGen;
	
	/**
	 * 最佳长度
	 */
	private int bestLen;
	
	/**
	 * 最佳路径
	 */
	private int[] bestRoute;
	
	/**
	 * 随机数
	 */
	private Random random;

	/**
	 * 
	 * @param scale 种群规模
	 * @param maxGen 运行代数
	 * @param pc 交叉概率
	 * @param pm 变异概率
	 */
	public MyGa(int scale, int maxGen, double pc, double pm){
		this.scale = scale;
		this.maxGen = maxGen;
		this.pc = pc;
		this.pm = pm;
	}
	
	/**
	 * 初始化算法，从file中加载数据文件
	 * @param filename
	 * @throws IOException 
	 */
	public void init(String filename) throws IOException{
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		this.cityList = new ArrayList<Integer>();
		
		//读取城市坐标信息
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String tmpStr = null;
		while((tmpStr = reader.readLine()) != null){
			String[] strArr = tmpStr.split(" ");
			this.cityList.add(Integer.parseInt(strArr[0]));//城市Id
			x.add(Integer.parseInt(strArr[1]));//x 坐标
			y.add(Integer.parseInt(strArr[2]));//y 坐标
		}
		reader.close();
		
		this.cityNum = this.cityList.size();
		this.distance = new double[cityNum][cityNum];
		
		//通过欧式距离计算距离矩阵
		for (int i = 0; i < cityNum - 1; i++) {
			distance[i][i] = 0; //对角线为0
			for (int j = i+1; j < cityNum; j++) {
				int xi = x.get(i);
				int yi = y.get(i);
				int xj = x.get(j);
				int yj = y.get(j);
				double rij = Math.sqrt(((xi - xj)*(xi - xj) + (yi - yj)*(yi - yj)) / 10.0);
				int tij = (int)Math.round(rij);
				if (tij < rij) {
					distance[i][j] = tij + 1;
					distance[j][i] = tij + 1;
				}else{
					distance[i][j] = tij;
					distance[j][i] = tij;
				}
			}
		}
		distance[cityNum - 1][cityNum - 1] = 0;//最后一个城市的距离为0，for循环里没有初始化
		
		this.bestLen = Integer.MAX_VALUE;
		this.bestGen = 0;
		this.bestRoute = new int[cityNum];
		this.curGen = 0;
		
		this.newPopulation = new int[scale][cityNum];
		this.oldPopulation = new int[scale][cityNum];
		this.fitness = new int[scale];
		this.pi = new double[scale];
		
		this.random = new Random(System.currentTimeMillis());
	}
	
	/**
	 * 初始化种群
	 */
	private void initGroup(){
		int i, j, k;
		for (k = 0; k < scale; k++) {
			for (i = 0; i < cityNum; ) {
				oldPopulation[k][i] = getRandomNum() % cityNum;
				//确保随机产生的染色体中没有重复的基因
				for (j = 0; j < i; j++) {
					if (oldPopulation[k][i] == oldPopulation[k][j]) {
						break;
					}
				}
				if (i == j) {
					i++;
				}
			}
		}
	}
	
	private int evaluate(int[] chromosome){
		return 0;
	}
	
	/**
	 * 生成一个0-65535之间的随机数
	 * @return
	 */
	private int getRandomNum(){
		return this.random.nextInt(65535);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
