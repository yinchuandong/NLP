package Parsing;

import java.util.LinkedList;

public class Node {
	String pos = null;
	int type = 0;//0代表非终端节点，1代表终端节点
	Node parent = null;
	LinkedList<Node> child = null;
}
