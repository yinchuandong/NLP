package Hmm;

public class POS {
	
	/** 前项 */
	public String iPos;
	/** 后项 */
	public String jPos;
	/** 数量 */
	public int num;
	/** 概率 */
	public double prob;
	
	public POS() {
	}
	
	public POS(String iPos, int num, double prob) {
		super();
		this.iPos = iPos;
		this.num = num;
		this.prob = prob;
	}

	public POS(String iPos, String jPos, int num, double prob) {
		super();
		this.iPos = iPos;
		this.jPos = jPos;
		this.num = num;
		this.prob = prob;
	}


}
