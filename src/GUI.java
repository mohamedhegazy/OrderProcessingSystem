import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class GUI {

	private JFrame frame;
	private int state;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	
	JPanel panelLogin;
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 810, 597);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 12, 786, 545);
		frame.getContentPane().add(tabbedPane);
		
		panelLogin = new TableGUI(frame);
		ArrayList<String> fields = new ArrayList<>();
		fields.add("user name");
		fields.add("password ");
		ArrayList<String> bottons = new ArrayList<>();
		bottons.add("login");
		bottons.add("signup");
		((TableGUI)panelLogin).setFields(fields);
		((TableGUI)panelLogin).setBottons(bottons);
		tabbedPane.addTab("login", null, panelLogin, null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		
			

	
		
		
		
	}
}
