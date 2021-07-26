/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2018 Caprica Software Limited.
 */

package com.masterteknoloji.trafix.player;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import com.masterteknoloji.trafix.domain.dto.LineCrossedVM;
import com.masterteknoloji.trafix.domain.dto.LineSummaryVM;
import com.masterteknoloji.trafix.util.Util;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

/**
 * This simple test player shows how to get direct access to the video frame data.
 * <p>
 * This implementation uses the new (1.1.1) libvlc video call-backs function.
 * <p>
 * Since the video frame data is made available, the Java call-back may modify the contents of the
 * frame if required.
 * <p>
 * The frame data may also be rendered into components such as an OpenGL texture.
 */
public class ViewerOverlay2 {
	
//	int carIndex=0;

//	Camera camera ;
	
    // The size does NOT need to match the mediaPlayer size - it's the size that
    // the media will be scaled to
    // Matching the native size will be faster of course
    private final int width;

    private final int height;

    // private final int width = 1280;
    // private final int height = 720;

    /**
     * Image to render the video frame data.
     */
    private final BufferedImage image;

    private MediaPlayerFactory factory;

    private final DirectMediaPlayer mediaPlayer;
    
    private String connectionUrl;

    private final ImagePane imagePane;
    
    List<LineSummaryVM> lineSummaryVMs = new ArrayList<LineSummaryVM>();
    
    BindMouseMove movingAdapt = new BindMouseMove();
    


    public ViewerOverlay2(int width, int height,String[] args) throws InterruptedException, InvocationTargetException {
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height);
        image.setAccelerationPriority(1.0f);
        
        this.width = width;
        this.height = height;
     
        imagePane = new ImagePane(image);
        imagePane.setSize(width, height);
        imagePane.setMinimumSize(new Dimension(width, height));
        imagePane.setPreferredSize(new Dimension(width, height));
        
        factory = new MediaPlayerFactory(args);
        mediaPlayer = factory.newDirectMediaPlayer(new TestBufferFormatCallback(), new TestRenderCallback());

        
    }

    @SuppressWarnings("serial")
    private final class ImagePane extends JPanel {

    	private int x;

    	private int y;
    	
    	private  BufferedImage image = null;

        private final Font font = new Font("Sansserif", Font.BOLD, 36);

        public ImagePane(BufferedImage image) {
            this.image = image;
            addMouseMotionListener(movingAdapt);

			addMouseListener(movingAdapt);
        }
        

        @Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(image, null, 0, 0);
			g2.setComposite(AlphaComposite.SrcOver.derive(0.5f));
			int i=0;
			
			for (Iterator iterator = lineSummaryVMs.iterator(); iterator.hasNext();) {
				LineSummaryVM lineSummaryVM = (LineSummaryVM) iterator.next();
				g.setColor(lineSummaryVM.getColor());	

				Polygon polygon3 = new Polygon();
				for (Iterator iterator2 = lineSummaryVM.getProjectedPointList().iterator(); iterator2.hasNext();) {
					Point point = (Point) iterator2.next();
					polygon3.addPoint(point.x, point.y);
				}

				g2.fillPolygon(polygon3);
				i++;
			}

			Font myFont = new Font("Courier New", 1, 13);
			g2.setFont(myFont);
			g2.setColor(Color.BLACK);
			g2.setComposite(AlphaComposite.SrcOver.derive(1f));
			
			for (Iterator iterator = lineSummaryVMs.iterator(); iterator.hasNext();) {
				LineSummaryVM line = (LineSummaryVM) iterator.next();
				Point centre = Util.findCentroid(line.getProjectedPointList());
				g2.drawString(line.getCount().toString(), (int) centre.getX(), (int) centre.getY());
				
			}

			i=0;
			for (Iterator iterator = lineSummaryVMs.iterator(); iterator.hasNext();) {
				LineSummaryVM line = (LineSummaryVM) iterator.next();
				g.setColor(line.getColor());
				drawReport(g2, line,line.getColor());
				i++;
			}
		}
	}
    
    public void drawReport( Graphics2D g2,LineSummaryVM line,Color color) {
    	g2.setColor(color);
    	g2.setComposite(AlphaComposite.SrcOver.derive(0.8f));
    	g2.fill(line.getRectangle());
    	Font myFont2 = new Font ("Courier New", 1, 20);
		g2.setFont (myFont2);
		g2.setColor(Color.black);
		int index=0;
		int x=(int)line.getRectangle().getX();
		int y = (int)line.getRectangle().getY()+20;
    	for (Iterator iterator = line.getLastDatas().iterator(); iterator.hasNext();) {
    		LineCrossedVM type = (LineCrossedVM) iterator.next();
    		g2.drawString(prepareReportMessage(type) , x, y+(index*20));
    		index++;
		}
    }
    
    public String prepareReportMessage(LineCrossedVM type) {
    	String result ="";
    	result = "type:"+type.getType()+ " - speed:"+"0" ;
    	
    	
    	return result;
    }
    
    private final class TestRenderCallback extends RenderCallbackAdapter {

        public TestRenderCallback() {
            super(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        }

        @Override
        public void onDisplay(DirectMediaPlayer mediaPlayer, int[] data) {
            imagePane.repaint();
        }
    }

    private final class TestBufferFormatCallback implements BufferFormatCallback {
        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            return new RV32BufferFormat(width, height);
        }

    }
   
 
 public void play() {
	 mediaPlayer.playMedia(connectionUrl);
 }

 public void prepare() {
	 mediaPlayer.prepareMedia(connectionUrl); 
 }
 

public MediaPlayerFactory getFactory() {
	return factory;
}

public String getConnectionUrl() {
	return connectionUrl;
}

public void setConnectionUrl(String connectionUrl) {
	this.connectionUrl = connectionUrl;
}

public JPanel getImagePane() {
	return imagePane;
}

public DirectMediaPlayer getMediaPlayer() {
	return mediaPlayer;
}

public List<LineSummaryVM> getLineSummaryVMs() {
	return lineSummaryVMs;
}

public void setLineSummaryVMs(List<LineSummaryVM> lineSummaryVMs) {
	this.lineSummaryVMs = lineSummaryVMs;
}


class BindMouseMove extends MouseAdapter {

	private int x;
	private int y;

	@Override
	public void mousePressed(MouseEvent event) {
		x = event.getX();
		y = event.getY();
		System.out.println(x + " " +y);
	}
	@Override
	public void mouseDragged(MouseEvent event) {

		int dx = event.getX() - x;
		int dy = event.getY() - y;
		System.out.println("dx" +dx + " " +dy);
		for (Iterator iterator = lineSummaryVMs.iterator(); iterator.hasNext();) {
			LineSummaryVM line = (LineSummaryVM) iterator.next();
			Rectangle rectangle = line.getRectangle();
			if (rectangle.getBounds2D().contains(x, y)) {
				rectangle.x += dx;
				rectangle.y += dy;
				imagePane.repaint();
			}
		}
		x += dx;
		y += dy;

	}
}


	
}

