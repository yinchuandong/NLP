package Parsing;

import java.util.ArrayList;

public class Node implements Cloneable{
	String pos = null;
	int type = 0;//0代表非终端节点，1代表终端节点
	Node parent = null;
	ArrayList<Node> child = null;
	
	public Node(){
//		parent = new Node();
//		child = new ArrayList<>();
	}
	
	public Node(String pos){
		this(pos, 0);
	}
	
	public Node(String pos, int type){
		this.pos = pos;
		this.type = type;
		parent = new Node();
		child = new ArrayList<>();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Node node = (Node)super.clone();
		if (parent != null) {
			node.parent = (Node)parent.clone();
		}
		if (child != null) {
			node.child = (ArrayList<Node>)child.clone();
		}
		return node;
	}
	
	
}
