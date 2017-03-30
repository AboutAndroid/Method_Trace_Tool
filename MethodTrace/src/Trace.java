import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumn;

public class Trace {
	public static void main(String[] args) {
		String packageName = null;

		JFrame window = new JFrame("App method trace analysis");
		window.setSize(1280, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);// �����С���ɱ�

		JMenuBar menubar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		menubar.add(menuFile);
		JMenuItem itemOpen = new JMenuItem("Open");
		menuFile.add(itemOpen);
		window.setJMenuBar(menubar);

		JPanel panel = new JPanel();
		JPanel root = new JPanel();
		window.setContentPane(root);
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5,
				true, false));

		JPanel path = new JPanel();
		path.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT, 10, 5));
		path.add(new JLabel("File Path:"));
		JLabel JlPath = new JLabel("...");
		path.add(JlPath);
		panel.add(path);

		JPanel top = new JPanel();
		top.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT, 10, 5));
		top.add(new JLabel("Filter Package Name:"));
		JTextField jpName = new JTextField("package name", 30);// com.jushi.trading
		jpName.setBounds(10, 10, 100, 20);
		// �����ı���ˮƽ���뷽ʽ
		jpName.setHorizontalAlignment(JTextField.CENTER);
		top.add(jpName, BorderLayout.PAGE_START);
		JButton jGo = new JButton("analysis");
		top.add(jGo);
		panel.add(top);

		JPanel jpStatus = new JPanel();
		jpStatus.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT, 10,
				5));
		JLabel jlStatus = new JLabel("Info:");
		jpStatus.add(jlStatus);
		panel.add(jpStatus);
		jpStatus.setVisible(false);

		root.add(panel, BorderLayout.PAGE_START);

		JTable table = new JTable(new MethodTabModel());
		JScrollPane scrollPane = new JScrollPane(table);
		root.add(scrollPane, BorderLayout.CENTER);

		jGo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JlPath.getText().equals("...")) {
					JOptionPane.showMessageDialog(root, "�뵼��trace�ļ�", "��ʾ��Ϣ",
							JOptionPane.WARNING_MESSAGE);
				} else if (!JlPath.getText().endsWith("trace")) {
					JOptionPane.showMessageDialog(root, "�뵼����Ч��trace�ļ�",
							"��ʾ��Ϣ", JOptionPane.WARNING_MESSAGE);
				} else if (jpName.getText().equals("package name")
						|| jpName.getText().equals("")) {
					JOptionPane.showMessageDialog(root, "����������˵İ���", "��ʾ��Ϣ",
							JOptionPane.WARNING_MESSAGE);
				} else {

					TraceScanner scanner = new TraceScanner(new File(JlPath
							.getText()));
					// ��ֵ
					scanner.setPackageName(jpName.getText());
					jlStatus.setText("analysis...");
					MethodTabModel model = (MethodTabModel) table.getModel();
					model.setContent(Utils.mapConvert2Array(scanner
							.convertFile(JlPath.getText())));
					Utils.fitTableColumns(table);
				}
			}
		});

		itemOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "ѡ��");
				File file = jfc.getSelectedFile();
				if (file.isDirectory()) {
					System.out.println("�ļ���:" + file.getAbsolutePath());
				} else if (file.isFile()) {
					System.out.println("�ļ�:" + file.getAbsolutePath());
				}
				JlPath.setText(file.getAbsolutePath());
				jpName.setText(Utils.getPackageName(file.getAbsolutePath()));
			}
		});
		window.setVisible(true);
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//��ǰϵͳ���
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}*/
	}

}
