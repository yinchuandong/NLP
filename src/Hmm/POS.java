package Hmm;

public class POS {
	
	/** ǰ�� */
	String iPos;
	/** ���� */
	String jPos;
	/** ���� */
	int num;
	/** Ƶ�� */
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
