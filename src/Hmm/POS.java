package Hmm;

public class POS {
	
	/** ǰ�� */
	public String iPos;
	/** ���� */
	public String jPos;
	/** ���� */
	public int num;
	/** ���� */
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
