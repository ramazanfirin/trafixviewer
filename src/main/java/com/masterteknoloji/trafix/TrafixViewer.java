package com.masterteknoloji.trafix;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.masterteknoloji.trafix.domain.dto.AnalyzeOrderSummaryVM;
import com.masterteknoloji.trafix.service.DataService;

@SpringBootApplication
@ComponentScan("com")
public class TrafixViewer extends JFrame{
	
	JPanel jPanel = new JPanel();
	
	VisionFrame visionFrame ;
	
	@Autowired
	DataService dataService;
	
	String data[][] = {};
	String column[] = { "ID", "STATE", "videoName", "videoPath", "scenarioPath" };
	
	DefaultTableModel model = new DefaultTableModel(data,column);
	
	List<AnalyzeOrderSummaryVM> analyzeOrderSummaryVMs;

	public static void main(String[] args){

		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(TrafixViewer.class)
              .headless(false).run(args);

      EventQueue.invokeLater(() -> {

    	  TrafixViewer ex = (TrafixViewer)ctx.getBean(TrafixViewer.class);
      	try {
			ex.createAndShowGUI();
		} catch (HeadlessException | InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	ex.setVisible(true);
//      	ex.startProcess();
      });
  }
	
	
	private  void createAndShowGUI() throws HeadlessException, InvocationTargetException, InterruptedException{
	    
		visionFrame = new VisionFrame();
		
    	setSize(600, 800);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(jPanel);
		
				
		//JTable jtable = new JTable(model);
		
		JTable jtable = new JTable(model) {
	        private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
		
		JScrollPane scrollPane = new JScrollPane(jtable);
		scrollPane.setVisible(true);
		scrollPane.setBounds(0, 0, 600, 100);
		   
		setLayout(null);
		add(scrollPane);
		
		
		jtable.addMouseListener(new MouseAdapter(){
			  public void mouseClicked(MouseEvent event) {
				  
				  if (event.getClickCount() == 2 && jtable.getSelectedRow() != -1) {
			            // your valueChanged overridden method 
			    		int rowindex = jtable.getSelectedRow();
				    	//table.getModel().get
				    	//JPopUpMenu.show(jtable, event.getX(), event.getY());
				    	ListSelectionModel rowSelMod = jtable.getSelectionModel();
				    	AnalyzeOrderSummaryVM selected= analyzeOrderSummaryVMs.get(rowindex);
				    	//JOptionPane.showMessageDialog(null, selected.getVideoName());
				    	visionFrame.setVisible(true);
				    	visionFrame.play(selected);
			        }  
				
			  }
			});
		
		
//		jtable.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent me) {
//				if (SwingUtilities.isRightMouseButton(me) == true) {
//					int row = table1.rowAtPoint(me.getPoint());
//
//					table1.clearSelection();
//					table1.addRowSelectionInterval(row, row);
//					// your popup menu goes here.
//				}
//			}
//		});
		
		getData();
		
		jtable.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GRAY));
	
		jtable.getTableHeader().setBackground(new Color(0,102,102));
		jtable.getTableHeader().setForeground(Color.WHITE);
		
		//jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		//jtable.setBackground(new Color(161,158,152));
		
		jtable.setBorder(new LineBorder(new Color(0,102,102), 2, true));
	
		//jtable.setDefaultEditor(Cell.class, new MyTableCellEditor());
		
		jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private void getData() {
		analyzeOrderSummaryVMs = dataService.getAnalyzeOrderList();
		for (Iterator iterator = analyzeOrderSummaryVMs.iterator(); iterator.hasNext();) {
			AnalyzeOrderSummaryVM analyzeOrderSummaryVM = (AnalyzeOrderSummaryVM) iterator.next();
			
			String[] satir = { analyzeOrderSummaryVM.getId().toString(), analyzeOrderSummaryVM.getState(),analyzeOrderSummaryVM.getVideoName(),analyzeOrderSummaryVM.getVideoPath(),analyzeOrderSummaryVM.getScenarioPath()  };
		    model.addRow(satir);
		}
		
		System.out.println("dsf");
	}
	
}