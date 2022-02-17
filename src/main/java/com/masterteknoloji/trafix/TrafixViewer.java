package com.masterteknoloji.trafix;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.masterteknoloji.trafix.domain.dto.AnalyzeOrderSummaryVM;
import com.masterteknoloji.trafix.service.DataService;
import com.masterteknoloji.trafix.util.Util;
import com.sun.jna.NativeLibrary;

@SpringBootApplication
@ComponentScan("com")
public class TrafixViewer extends JFrame{
	
	JPanel jPanel = new JPanel();
	
	VisionFrame visionFrame ;
	
	@Autowired
	DataService dataService;
	
	String data[][] = {};
	String column[] = { "ID", "DURUM", "VIDEO İSMİ","SENARYO İSMİ","BAŞLANGIÇ SAATİ","BİTİŞ SAATİ","VİDEO DOSYASI",  };
	
	DefaultTableModel model = new DefaultTableModel(data,column);
	
	List<AnalyzeOrderSummaryVM> analyzeOrderSummaryVMs;
	
	static String analyzeOrderId  =null;

	public static void main(String[] args){
		
		System.out.println("args = " +args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.println("args : "+ args[i]);
		}

		System.out.println("arch:"+System.getProperty("sun.arch.data.model"));

		System.setProperty("jna.library.path", "/snap/vlc/current/usr/lib/");
		
		System.out.println("path:"+System.getenv("$LD_LIBRARY_PATH")); 
		
		NativeLibrary.addSearchPath("vlc", "/snap/vlc/current/usr/lib/");
		System.out.println("test 2");
		 
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(TrafixViewer.class)
              .headless(false).run(args);
		System.out.println("test 3");
      EventQueue.invokeLater(() -> {

    	  TrafixViewer ex = (TrafixViewer)ctx.getBean(TrafixViewer.class);
      	try {
      		System.out.println("test 4");
			ex.createAndShowGUI(args);
			
		} catch (HeadlessException | InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("test 7");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	ex.setVisible(true);
//      	ex.startProcess();
      });
  }
	
	
	
	private  void createAndShowGUI(String[] args) throws HeadlessException, InvocationTargetException, InterruptedException, IOException{
		System.out.println("test 5");
		visionFrame = new VisionFrame(args);
		
    	setSize(1280, 720);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(jPanel);
		//heckPropertiesFile();
				
		//JTable jtable = new JTable(model);
		JLabel jLabel = new JLabel("TRAFIX VIEWER");
		jLabel.setBounds(getWidth()/2-100, 10, 500, 100);
		jLabel.setFont(new Font("Verdana", Font.PLAIN, 40));
		add(jLabel);
		
		JLabel jLabel2 = new JLabel("Izlemek İçin Tablo üzerindeki Satıra Çift Tıklayınız");
		jLabel2.setBounds(getWidth()/2-200, 50, 600, 100);
		jLabel2.setFont(new Font("Verdana", Font.PLAIN, 20));
		add(jLabel2);
		
		JTable jtable = new JTable(model) {
	        private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
	    
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    TableModel tableModel = jtable.getModel();

        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
        {
        	jtable.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
        }
		
        JTableHeader header = jtable.getTableHeader();
        header.setBackground(Color.yellow);
        header.setName("dfsdf");
        
		JScrollPane scrollPane = new JScrollPane(jtable);
		scrollPane.setVisible(true);
		scrollPane.setBounds(0, 150, getWidth(), getHeight());
		   
		setLayout(null);
		add(scrollPane);
		System.out.println("test 6");
		
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
				    	try {
							visionFrame.play(selected);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
			        }  
				
			  }
			});
		
		try {
			System.out.println("test 7");
			getData();
			jtable.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GRAY));
			
			jtable.getTableHeader().setBackground(new Color(0,102,102));
			jtable.getTableHeader().setForeground(Color.WHITE);
			
			//jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			
			//jtable.setBackground(new Color(161,158,152));
			
			jtable.setBorder(new LineBorder(new Color(0,102,102), 2, true));
		
			//jtable.setDefaultEditor(Cell.class, new MyTableCellEditor());
			
			jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			if(args.length>0) {
				Long analyzeOrderId = new Long(args[0]);
				for (Iterator iterator = analyzeOrderSummaryVMs.iterator(); iterator.hasNext();) {
					AnalyzeOrderSummaryVM analyzeOrderSummaryVM = (AnalyzeOrderSummaryVM) iterator.next();
					//System.out.println(analyzeOrderSummaryVM.getId() + ","+analyzeOrderId );
					if(analyzeOrderSummaryVM.getId().longValue()==analyzeOrderId.longValue()) {
						System.out.println("eşitlik sağlandı");
						this.setAlwaysOnTop(false);
						this.setVisible(false);
						visionFrame.setVisible(true);
						visionFrame.play(analyzeOrderSummaryVM);
					}
			}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	private void getData() throws Exception{
		analyzeOrderSummaryVMs = dataService.getAnalyzeOrderList();
		for (Iterator iterator = analyzeOrderSummaryVMs.iterator(); iterator.hasNext();) {
			AnalyzeOrderSummaryVM analyzeOrderSummaryVM = (AnalyzeOrderSummaryVM) iterator.next();
			
			String[] satir = { analyzeOrderSummaryVM.getId().toString(), 
					            analyzeOrderSummaryVM.getState(),
					            analyzeOrderSummaryVM.getVideoName(),
					            analyzeOrderSummaryVM.getStartDate(),
					            analyzeOrderSummaryVM.getEndDate(),
					            analyzeOrderSummaryVM.getScenarioPath(),
					            analyzeOrderSummaryVM.getVideoPath(),};
		    model.addRow(satir);
		}
		
		System.out.println("dsf");
	}
	
	public void checkPropertiesFile() {
		try {
			Properties a = Util.readPropertyFile();
			if(a.getProperty("connection.url")==null) {
				JOptionPane.showMessageDialog(null, "connection.url not found in property file");
				System.exit(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "property file cant find");
			System.exit(0);
		}
	}
	
}
