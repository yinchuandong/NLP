package Parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Stack;

import com.sun.corba.se.impl.oa.toa.TOA;
import com.sun.org.apache.bcel.internal.generic.LNEG;
import com.sun.org.apache.bcel.internal.generic.POP;

import sun.awt.CharsetString;
import sun.security.util.Length;

public class Parsing {
	
	HashMap<String, ArrayList<ArrayList<Node>>> guideMap;
	ArrayList<String> wordsList;
	ArrayList<Node> curNodeList;
	Stack<Node> nodeStack;
	Stack<Stack<Node>> backStack;//候选状态
	Stack<Integer> posStack;
	Stack<Integer> indexStack;
	int position = 0;//指向wordsList
	int index = 0;//指向curNodes
	
	public Parsing(){
		guideMap = new HashMap<String, ArrayList<ArrayList<Node>>>();
		wordsList = new ArrayList<>();
		curNodeList = new ArrayList<Node>();
		backStack = new Stack<>();
		nodeStack = new Stack<>();
		posStack = new Stack<>();
		indexStack = new Stack<>();
		init();
	}
	
	private void init(){
		wordsList.add("the");
		wordsList.add("boy");
		wordsList.add("saw");
		wordsList.add("a");
		wordsList.add("cat");
	}
	
	public void readGuide(String guidePath){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(guidePath));
			String lineStr = null;
			while((lineStr = reader.readLine()) != null){
				String[] lineArr = lineStr.split("->");
				if (lineArr.length < 2) {
					continue;
				}
				String fromStr = lineArr[0];
				String toStr = lineArr[1];
				String[] toArr = toStr.split("[ ]");
				ArrayList<Node> toList = new ArrayList<>();
				for (int i = 0; i < toArr.length; i++) {
					toList.add(new Node(toArr[i]));
				}
				
				//判断同一个from是否具有多条规则
				ArrayList<ArrayList<Node>> fromList = null;
				if (guideMap.containsKey(fromStr)) {
					fromList = guideMap.get(fromStr);
				}else{
					fromList = new ArrayList<ArrayList<Node>>();
					guideMap.put(fromStr, fromList);
				}
				fromList.add(toList);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void display(){
		Iterator<String> iterator = guideMap.keySet().iterator();
		while (iterator.hasNext()) {
			String fromStr = iterator.next();
			ArrayList<ArrayList<Node>> fromList = guideMap.get(fromStr);
			for (int i = 0; i < fromList.size(); i++) {
				ArrayList<Node> toList = fromList.get(i);
				System.out.print(fromStr + "->");
				for (int j = 0; j < toList.size(); j++) {
					Node node = toList.get(j);
					System.out.print(node.pos + " ");
				}
				System.out.println();
			}
			
		}
	}

	@SuppressWarnings("unchecked")
	public void calculate(){
		Stack<ArrayList<Node>> curListStack = new Stack<>();
		
		Node head = new Node("S");
//		curNodeList.add(head);
		nodeStack.push(head);
		while(!nodeStack.empty()){
			Node node = nodeStack.pop();
			curNodeList.add(node);
			
			//取得转移的规则S->NP VP的列表
			ArrayList<ArrayList<Node>> fromList = guideMap.get(node.pos);
			
			//到了终端字符
			String wordStr = wordsList.get(position);
			if (fromList == null || fromList.size() == 0) {
				if (node.pos.indexOf(wordStr) != -1) {//如果匹配终端字符
					position ++;
				}else{//不匹配终端字符
					if (backStack != null && backStack.size() != 0) {
						nodeStack = backStack.pop();
						position = posStack.pop();
					}
					index = indexStack.pop();
					while(index + 1 < curNodeList.size()){
						curNodeList.remove(index+1);
					}
					ArrayList<Node> backList = curListStack.pop();
					curNodeList.get(index).child.clear();
					curNodeList.get(index).child.addAll(backList);
				}
				continue;
			}
			
			try {
				//将候选状态入backstack栈
				for (int i = 1; i < fromList.size(); i++) {
					ArrayList<Node> tmpList = fromList.get(i);
					Stack<Node> tmpCurStack = (Stack<Node>) nodeStack.clone();
					for(int j = tmpList.size() - 1; j >= 0; j--){
						tmpCurStack.push(tmpList.get(j));
					}
					backStack.push(tmpCurStack);
					//记录发生歧义的位置
					posStack.push(position);
					//将curNodeList记录
//					curNodeList.addAll(tmpList);
					indexStack.push(curNodeList.size()-1);
					curListStack.push(tmpList);
				}
				
				//将当前状态加入nodestack
				int fLen = fromList.get(0).size();
				node.child.clear();
				node.child.addAll(fromList.get(0));
				for(int i=fLen - 1; i >= 0; i--){
					Node tmpNode = fromList.get(0).get(i);
//					tmpNode.parent = (Node)node.clone();
					tmpNode.parent = new Node(node.pos);
					nodeStack.push(tmpNode);
//					curNodeList.add(fromList.get(0).get(fLen - 1 - i));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) throws CloneNotSupportedException{
		Parsing parsing = new Parsing();
		parsing.readGuide("parsing_guide.txt");
		System.out.println("------------end--------------");
		parsing.display();
		parsing.calculate();
		System.out.println("calculate--end");
		
		for (Node node : parsing.curNodeList) {
			System.out.print(node.pos+" ");
		}
		
//		Node node1 = new Node("node1");
//		node1.child = new ArrayList<>();
//		node1.child.push(new Node("node1-childlist"));
//		node1.parent = new Node("node1-parent");
//		
//		Node node2 = (Node)node1.clone();
//		node2.pos = "node2";
//		node2.child.remove();
//		node2.parent.pos = "node2-parent";
//		System.out.println(node1.pos+":"+node1.child.size()+":"+node1.parent.pos);
//		System.out.println(node2.pos+":"+node2.child.size()+":"+node2.parent.pos);
	}
	
}
