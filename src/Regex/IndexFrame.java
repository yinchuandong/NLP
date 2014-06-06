package Regex;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.print.DocFlavor.STRING;
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
import javax.swing.JLabel;
import java.awt.Font;

public class IndexFrame extends JFrame {

	private JPanel contentPane;
	JTextArea bodyArea;
	JTextArea titleArea;
	JTextArea inputArea;
	JButton startBtn;
	private JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndexFrame frame = new IndexFrame();
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
	public IndexFrame() {
		//初始化pcfg模型

		initComponents();
		bindEvent();
		this.inputArea.setText("http://www.baidu.com");
	}
	
	private void bindEvent(){
		
		startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String url = inputArea.getText();
				RegexHelper helper = new RegexHelper(url);
				String title = helper.getTitle();
				ArrayList<String> hrefList = helper.getHref();
				titleArea.setText(title);
				bodyArea.setText("");
				for (String href : hrefList) {
					bodyArea.append(href+"\r\n");
				}
			}
		});
	}
	

	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 611, 531);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		startBtn = new JButton("\u5F00\u59CB\u5206\u6790");
		
		inputArea = new JTextArea();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		titleArea = new JTextArea();
		
		JLabel label_1 = new JLabel("\u6807\u9898\uFF1A");
		label_1.setFont(new Font("宋体", Font.PLAIN, 14));
		
		label = new JLabel("\u6B63\u6587\uFF1A");
		label.setFont(new Font("宋体", Font.PLAIN, 14));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(startBtn)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(10)
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)))
					.addGap(83))
				.addComponent(titleArea, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(523, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(inputArea, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(inputArea, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(startBtn)
					.addGap(10)
					.addComponent(label_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(titleArea, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		bodyArea = new JTextArea();
		bodyArea.setEditable(false);
		scrollPane_1.setViewportView(bodyArea);
		
		panel.setLayout(gl_panel);
	}
}
