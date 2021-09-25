package com.masterteknoloji.trafix;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.springframework.beans.factory.annotation.Autowired;

import com.masterteknoloji.trafix.domain.dto.AnalyzeOrderSummaryVM;
import com.masterteknoloji.trafix.domain.dto.LineCrossedVM;
import com.masterteknoloji.trafix.domain.dto.LineSummaryVM;
import com.masterteknoloji.trafix.player.ViewerOverlay2;
import com.masterteknoloji.trafix.service.DataService;
import com.masterteknoloji.trafix.util.Util;

public class VisionFrame extends JFrame {

	int viewWitdh = 1280;
	int viewHeight= 720;
	
	ViewerOverlay2 directTestPlayer;
	JPanel jPanel = new JPanel();
	
	@Autowired
	DataService dataService = new DataService();
	
	List<LineSummaryVM> lineSummaryVMs;
	
    List<Color> colorList = new ArrayList<Color>();
    
    List<Timer> timerList = new ArrayList<Timer>();
	
	public VisionFrame(String[] args) throws HeadlessException, InvocationTargetException, InterruptedException, IOException {
		super();
		createAndShowGUI(args);
		dataService.getConnectionUrl();
	}
	private  void createAndShowGUI(String[] args) throws InvocationTargetException, InterruptedException{
		
		directTestPlayer = new ViewerOverlay2(viewWitdh, viewHeight,args);
		
		setSize(1280, 720);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel imagePane = directTestPlayer.getImagePane();
		jPanel.add(imagePane);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(java.awt.event.WindowEvent e) {
	        	clearTimers();
	        }
	    });
		
		add(jPanel);
		prepareColor();
	}
	
	public void clearTimers() {
		for (Iterator iterator = timerList.iterator(); iterator.hasNext();) {
			Timer timer = (Timer) iterator.next();
			timer.stop();
		}
		
		timerList = new ArrayList<Timer>();
	}
	
	public void play(AnalyzeOrderSummaryVM selected) throws IOException  {
		
		clearTimers();
		lineSummaryVMs = dataService.getLineList(selected.getScenarioId()); 
		int i=0;
		for (Iterator iterator = lineSummaryVMs.iterator(); iterator.hasNext();) {
			LineSummaryVM lineSummaryVM = (LineSummaryVM) iterator.next();
			
			lineSummaryVM.setPointList(Util.calculatePoint(lineSummaryVM.getPoints()));
			lineSummaryVM.setProjectedPointList(Util.calculateProjectedListPoint(lineSummaryVM.getPointList(),832 ,468,viewWitdh, viewHeight));
			
			lineSummaryVM.setEntryPointList(Util.calculatePoint(lineSummaryVM.getEntryPolygonPoints()));
			lineSummaryVM.setEntryProjectedPointList(Util.calculateProjectedListPoint(lineSummaryVM.getEntryPointList(),832 ,468,viewWitdh, viewHeight));
			
			lineSummaryVM.setExitPointList(Util.calculatePoint(lineSummaryVM.getExitPolygonPoints()));
			lineSummaryVM.setExitProjectedPointList(Util.calculateProjectedListPoint(lineSummaryVM.getExitPointList(),832 ,468,viewWitdh, viewHeight));
			
			
			Point centre = Util.findCentroid(lineSummaryVM.getProjectedPointList());
			Rectangle rectangle = new Rectangle(centre,new Dimension(280, 100));
			lineSummaryVM.setRectangle(rectangle);
			lineSummaryVM.setColor(colorList.get(i));
			lineSummaryVM.setOriginalColor(colorList.get(i));
			i++;
		}
		
		List<LineCrossedVM> lineCrossedVMs = dataService.getCrossData(selected.getId()); 
		processdata(lineCrossedVMs);
		
		//String intersectionDay = "D:\\KBB\\intersection\\input_multiple_regions.m4v";
		//directTestPlayer.setConnectionUrl(intersectionDay);
		directTestPlayer.setLineSummaryVMs(lineSummaryVMs);
		directTestPlayer.setConnectionUrl("file:///"+selected.getVideoPath());
		directTestPlayer.prepare();
		directTestPlayer.play();
	}
	
	public void processdata(List<LineCrossedVM> lineCrossedVMs) {
    			for (LineCrossedVM line : lineCrossedVMs) {
					Timer timer = new Timer(line.getDuration().intValue(), new ActionListener() {
						  @Override
						  public void actionPerformed(ActionEvent arg0) {
							lineCrossed(line);
						  }
						});
			    	timer.setRepeats(false); // Only execute once
			    	timer.start(); // Go go go!
			    	timerList.add(timer);
					}
    }
	
	public void lineCrossed(LineCrossedVM crossedVM) {
    	if(crossedVM!=null) {
    		LineSummaryVM line = findLine(crossedVM.getLineId());
    		
    		line.getLastDatas().add(crossedVM);
    		if(line.getLastDatas().size()>5) {
    			line.getLastDatas().remove();
    		}

    		
        	line.setCount(line.getCount()+1);
	    	line.setColor(Color.red);
	    	
	    	Timer timer = new Timer(100, new ActionListener() {
				  @Override
				  public void actionPerformed(ActionEvent arg0) {
					line.setColor(line.getOriginalColor());
				  }
				});
	    	timer.setRepeats(false); // Only execute once
	    	timer.start(); // Go go go!
	    	
    	}else {
    		System.out.println("line=null");
    	}
    }
	
	public LineSummaryVM findLine(Long id) {
		for (Iterator iterator = lineSummaryVMs.iterator(); iterator.hasNext();) {
			LineSummaryVM lineSummaryVM = (LineSummaryVM) iterator.next();
			if(lineSummaryVM.getId()==id)
				return lineSummaryVM;
		}
		
		return null;
	}
	
	public void prepareColor() {
		for (int i = 0; i < 100; i++) {
			colorList.add(Color.yellow);
			colorList.add(Color.cyan);
			colorList.add(Color.orange);
			colorList.add(Color.green);
		}
		
	}
}
