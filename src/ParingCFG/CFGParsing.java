package ParingCFG;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

public class CFGParsing {
	
	HashMap<String, ArrayList<ArrayList<Node>>> guideMap;
	ArrayList<String> wordsList;
	ArrayList<Node> curNodeList;
	Stack<Node> nodeStack;
	Stack<Stack<Node>> backStack;//候选状态
	Stack<Integer> posStack;
	Stack<Integer> indexStack;
	int position = 0;//指向wordsList
	int index = 0;//指向curNodes
	
	public CFGParsing(){
		guideMap = new HashMap<String, ArrayList<ArrayList<Node>>>();
		wordsList = new ArrayList<>();
		curNodeList = new ArrayList<Node>();
		backStack = new Stack<>();
		nodeStack = new Stack<>();
		posStack = new Stack<>();
		indexStack = new Stack<>();
		reset();
	}
	
	/**
	 * 重置内部数据
	 */
	public void reset(){
		wordsList.clear();
		curNodeList.clear();
		backStack.clear();
		nodeStack.clear();
		posStack.clear();
		indexStack.clear();
		position = 0;
		index = 0;
//		wordsList.add("the");
//		wordsList.add("boy");
//		wordsList.add("saw");
//		wordsList.add("a");
//		wordsList.add("dirty");
//		wordsList.add("cat");
	}
	
	public void setWordsList(ArrayList<String> wordsList){
		this.wordsList = wordsList;
	}
	
	public String readGuide(String guidePath){
		guideMap.clear();
		String resultStr = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(guidePath));
			String lineStr = null;
			while((lineStr = reader.readLine()) != null){
				resultStr += lineStr + "\r\n";
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
		return resultStr;
	}
	
	/**
	 * 控制台输出规则信息
	 */
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
	
	/**
	 * 获得规则列表，每一条规则
	 * @param pos
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private ArrayList<ArrayList<Node>> getGuideMap(String pos){
		ArrayList<ArrayList<Node>> fromList = guideMap.get(pos);
		ArrayList<ArrayList<Node>> parentList = new ArrayList<>();
		try {
			if (fromList == null) {
				return null;
			}
			for (ArrayList<Node> parent : fromList) {
				ArrayList<Node> sonList = new ArrayList<>();
				for (Node node : parent) {
					Node cloneNode = new Node(node.pos);
					sonList.add(cloneNode);
				}
				parentList.add(sonList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parentList;
	}

	@SuppressWarnings("unchecked")
	public void calculate(){
		Stack<ArrayList<Node>> curListStack = new Stack<>();
		
		Node head = new Node("S");
		nodeStack.push(head);
		while(!nodeStack.empty()){
			Node node = nodeStack.pop();
			//将当前访问的节点加入到访问过的集合中
			curNodeList.add(node);
			
			//取得转移的规则S->NP VP的列表
			ArrayList<ArrayList<Node>> fromList = guideMap.get(node.pos);
//			ArrayList<ArrayList<Node>> fromList = this.getGuideMap(node.pos);
			
			//到了终端字符
			String wordStr = wordsList.get(position);
			if (fromList == null || fromList.size() == 0) {
				if (node.pos.indexOf(wordStr) != -1) {//如果匹配终端字符
					node.word = wordStr;
					position ++;
				}else{//不匹配终端字符
					if (backStack != null && backStack.size() != 0) {
						nodeStack = backStack.pop();
						position = posStack.pop();
					}
					//移除不匹配的元素，直到出分支的位置
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
					ArrayList<Node> tmpList = (ArrayList<Node>)fromList.get(i).clone();
					Stack<Node> tmpCurStack = (Stack<Node>) nodeStack.clone();
					for(int j = tmpList.size() - 1; j >= 0; j--){
						Node tmpNode = tmpList.get(j);
						tmpNode.parent = node;
						tmpCurStack.push(tmpNode);
					}
					//记录候选状态
					backStack.push(tmpCurStack);
					posStack.push(position);
					////记录发生歧义的位置,如NP->ART ADJ N  NP->ART N，则保存np的index
					indexStack.push(curNodeList.size()-1);
					curListStack.push(tmpList);
				}
				
				//将当前状态加入nodestack
				int fLen = fromList.get(0).size();
				ArrayList<Node> tmpCloneList = (ArrayList<Node>)fromList.get(0).clone();
				node.child.clear();
				node.child.addAll(tmpCloneList);
				for(int i=fLen - 1; i >= 0; i--){
					Node tmpNode = tmpCloneList.get(i);
//					tmpNode.parent = (Node)node.clone();
					tmpNode.parent = new Node(node.pos);
					nodeStack.push(tmpNode);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("end calculate");
	}
	
	
	/**
	 * 输出字符串类型(decrapt)
	 */
	public void output(){
		String result = "";
		Stack<String> lBracketStack = new Stack<>();
		Stack<String> rBracketStack = new Stack<>();
		Stack<Node> listStack = new Stack<>();
		listStack.push(curNodeList.get(0));
		lBracketStack.push("(");
		rBracketStack.push(")");
		Node node = listStack.peek();
		while(!listStack.empty()){
			if (node.child == null || node.child.size() == 0) {
				String lb = lBracketStack.pop();
				String rb = rBracketStack.pop();
				listStack.pop();
				result = lb + node.pos + rb;
				System.out.println(result);
				if (listStack.size() == 0) {
					continue;
				}
				node = listStack.peek();
				
				if (node.child.size() > 0) {
					node.child.remove(0);
				}
//				if (node.child.size() >= 1) {
//					listStack.push(node.child.get(0));
//				}
			}else{
				Node tmp = node.child.get(0);
				listStack.push(tmp);
				lBracketStack.push("(");
				rBracketStack.push(")");
				node = tmp;
			}
		}
		
	}
	
	/**
	 * 将结果生成一颗树
	 * @return
	 */
	public DefaultMutableTreeNode getTreeNode(){
		LinkedList<DefaultMutableTreeNode> treeQueue = new LinkedList<>();
		LinkedList<Node> nodeQueue = new LinkedList<>();
		Node headNode = curNodeList.get(0);
		DefaultMutableTreeNode headTree = new DefaultMutableTreeNode(headNode);
		DefaultMutableTreeNode pTree = null;
		treeQueue.add(headTree);
		nodeQueue.offer(headNode);
		while(nodeQueue.size() != 0){
			Node tmpNode = nodeQueue.poll();
			pTree = treeQueue.poll();
			if (tmpNode.child != null && tmpNode.child.size() != 0) {
				for (Node node : tmpNode.child) {
					nodeQueue.offer(node);
					DefaultMutableTreeNode tmpTree = new DefaultMutableTreeNode(node);
					pTree.add(tmpTree);
					treeQueue.offer(tmpTree);
				}
			}
		}
		return headTree;
	}
	
	public static void main(String[] args) throws CloneNotSupportedException{
		CFGParsing parsing = new CFGParsing();
		parsing.readGuide("guide/cfg_guide.txt");
		System.out.println("------------end--------------");
		parsing.display();
		parsing.calculate();
		System.out.println("calculate--end");
		
		parsing.getTreeNode();
		
//		parsing.output();
//		for (Node node : parsing.curNodeList) {
//			System.out.print(node.pos+" ");
//		}
		
		ArrayList<Node> list1 = new ArrayList<>();
		list1.add(new Node("list11"));
		list1.add(new Node("list12"));
		
		ArrayList<Node> list2 = (ArrayList<Node>)list1.clone();
		list2.get(0).pos = "list21";
		list2.remove(1);
//		
//		ArrayList<String> list1 = new ArrayList<>();
//		list1.add("list11");
//		list1.add("list12");
//		
//		ArrayList<String> list2 = (ArrayList<String>)list1.clone();
////		list2.get(0) = "list21";
//		list2.remove(1);
		
		System.out.println(list1.get(0));
		
	}
	
}
