package Parsing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;

public class FromFrame extends JFrame {

	private JPanel contentPane;
	private JTree tree;
	JTextArea guideArea;
	JTextArea inputArea;
	JButton startBtn;
	JButton importBtn;
	
	private JScrollPane scrollPane;
	private Parsing parsing;
	private boolean isGuidLoaded = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FromFrame frame = new FromFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FromFrame() {
		parsing = new Parsing();
		initComponents();
		bindEvent();
	}
	
	private void bindEvent(){
		importBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String resultStr = parsing.readGuide("parsing_guide.txt");
				guideArea.setText(resultStr);
				isGuidLoaded = true;
			}
		});
		
		startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!isGuidLoaded) {
					JOptionPane.showConfirmDialog(null, "还未加载规则");
					return;
				}
				
				String inputStr = inputArea.getText();
				if (inputStr == null || inputStr.equals("")) {
					JOptionPane.showConfirmDialog(null, "内容不能为空");
					return ;
				}
				String[] arr = inputStr.split(" ");
				ArrayList<String> wordsList = new ArrayList<>();
				for (String string : arr) {
					wordsList.add(string);
				}
				parsing.reset();
				parsing.setWordsList(wordsList);
				parsing.calculate();
				tree = new JTree(parsing.getTreeNode());
				expandTree(tree);
				scrollPane.setViewportView(tree);
			}
		});
	}
	
	public void initTree(){
		tree = new JTree(parsing.getTreeNode());
		expandTree(tree);
		scrollPane.setViewportView(tree);
	}
	
	/** 
	* 展开一棵树 
	* 
	* @param tree 
	*/ 
	private void expandTree(JTree tree) { 
		TreeNode node = (TreeNode) tree.getModel().getRoot(); 
		expandAll(tree, new TreePath(node), true); 
	} 

	/** 
	* 完全展开一棵树或关闭一棵树 
	* 
	* @param tree 
	*            JTree 
	* @param parent 
	*            父节点 
	* @param expand 
	*            true 表示展开，false 表示关闭 
	*/ 
	private void expandAll(JTree tree, TreePath parent, boolean expand) { 
		TreeNode node = (TreeNode) parent.getLastPathComponent(); 
	
		if (node.getChildCount() > 0) { 
			for (Enumeration e = node.children(); e.hasMoreElements();) { 
				TreeNode n = (TreeNode) e.nextElement(); 
				TreePath path = parent.pathByAddingChild(n); 
				expandAll(tree, path, expand); 
		
			} 
		} 
		if (expand) { 
		tree.expandPath(parent); 
		} else { 
		tree.collapsePath(parent); 
		} 
	} 

	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 611, 445);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		scrollPane = new JScrollPane();
		
		startBtn = new JButton("\u5F00\u59CB\u5206\u6790");
		
		inputArea = new JTextArea();
		
		importBtn = new JButton("\u5BFC\u5165\u89C4\u5219");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(importBtn)
							.addGap(18)
							.addComponent(startBtn))
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(inputArea, GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(inputArea, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(importBtn)
								.addComponent(startBtn))
							.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		
		guideArea = new JTextArea();
		guideArea.setEditable(false);
		scrollPane_1.setViewportView(guideArea);
		
		tree = new JTree();
//		scrollPane.setViewportView(tree);
		panel.setLayout(gl_panel);
	}
}
