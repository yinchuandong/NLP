package Parsing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class FromFrame extends JFrame {

	private JPanel contentPane;
	private JTree tree;
	private JScrollPane scrollPane;
	private JTree tree_1;
	private Parsing parsing;

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
		parsing.readGuide("parsing_guide.txt");
		System.out.println("------------end--------------");
		parsing.display();
		parsing.calculate();
		System.out.println("calculate--end");
		
		initComponents();
		initTree();
	}
	
	public void initTree(){
//		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(new Node("s"));
//		node1.add(new DefaultMutableTreeNode("NP"));
//		node1.add(new DefaultMutableTreeNode("VP"));
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
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(54)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE))
		);
		
		tree = new JTree();
		scrollPane.setViewportView(tree);
		panel.setLayout(gl_panel);
	}
}
