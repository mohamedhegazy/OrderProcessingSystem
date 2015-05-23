import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class TableGUI extends JPanel{

	JFrame parent;
	
	ArrayList<TextField> fields;
	ArrayList<JButton> bottons;
	JTable table;
	
	JPanel bottonsPanel,fieldsPanel;
	JScrollPane tablePanel;
	int width,height;
	
	public TableGUI(JFrame frame) {
		this.parent = frame;
		width = parent.getWidth();
		height = parent.getHeight();
		
		setLayout(null);
		setSize(width, height);
		
		fieldsPanel = new JPanel();
		bottonsPanel = new JPanel();
		tablePanel = new JScrollPane();
		
		
		add(fieldsPanel);
		add(bottonsPanel);
		add(tablePanel);
		
		setBorder();
		
	}
	
	
	
	public void setFields(ArrayList<String> f) {
		
		fieldsPanel.setLayout(new GridLayout(5,3));
		fields = new ArrayList<TextField>();
		
		for(int i = 0; i < f.size(); i++) {
			TextField ff = new TextField(f.get(i));
			fields.add(ff);
			fieldsPanel.add(ff);
		}
		
		
		
	}
	
	
	public void setBottons(ArrayList<String> f) {
		
		bottonsPanel.setLayout(new FlowLayout());
		bottons = new ArrayList<JButton>();
		
		for(int i = 0; i < f.size(); i++) {
			JButton ff = new JButton(f.get(i));
			JPanel pp = new JPanel();
			pp.setSize(100, 100);
			pp.add(ff);
			
			bottons.add(ff);
			bottonsPanel.add(pp);
		}
		
		
		
	}
	
	public void setTable(ArrayList<String> f) {
		table = new JTable();
		DefaultTableColumnModel colomns = new DefaultTableColumnModel();
		for(int i = 0; i < f.size(); i++) {
			colomns.addColumn(new TableColumn());
			colomns.getColumn(i).setHeaderValue(f.get(i));
		}
		table.setColumnModel(colomns);
		
		
		
		tablePanel.setViewportView(table);
		
	
		
	}
	
	void setBorder() {
		fieldsPanel.setBounds(0, 0, width, height/3);
		bottonsPanel.setBounds(0, height/3,width,height/3);
		tablePanel.setBounds(0, height*2/3,width,height/3);
	}
	
	void fillTable( ResultSet result) {
		try  {
			
			int nCol = result.getMetaData().getColumnCount();
			ArrayList<String[]> tmp = new ArrayList<>();
			while( result.next()) {
			    String[] row = new String[nCol];
			    for( int iCol = 1; iCol <= nCol; iCol++ ){
			        row[iCol-1] = result.getObject( iCol ).toString();
			    }
			    tmp.add(row);
			}
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
		        model.removeRow(i);
		    }
			for(int i = 0; i < tmp.size(); i++) {
				model.addRow(tmp.get(i));
			}
			
		
		}catch(Exception e) {
			
		}
		
	}
	
	
	
	@Override
	public void paint(Graphics arg0) {
		// TODO Auto-generated method stub
		
		
		super.paint(arg0);
		
	}
	
	
}
