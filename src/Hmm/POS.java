package Hmm;

public class POS {
	
	/** 前项 */
	String iPos;
	/** 后项 */
	String jPos;
	/** 数量 */
	int num;
	/** 频率 */
	double frequency;
	
	public POS() {
	}
	
	public POS(String iPos, int num, double frequency) {
		super();
		this.iPos = iPos;
		this.num = num;
		this.frequency = frequency;
	}

	public POS(String iPos, String jPos, int num, double frequency) {
		super();
		this.iPos = iPos;
		this.jPos = jPos;
		this.num = num;
		this.frequency = frequency;
	}


}
