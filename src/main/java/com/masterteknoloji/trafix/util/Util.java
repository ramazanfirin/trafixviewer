package com.masterteknoloji.trafix.util;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafix.domain.dto.LineSummaryVM;
import com.masterteknoloji.trafix.domain.dto.VideoRecordQueryVM;

public class Util {

//	public static List<Point> calculatePointList(List<LineSummaryVM> lineSummaryVMs ) {
//		List<Point> result = new ArrayList<Point>();
//		for (Iterator iterator = lineSummaryVMs.iterator(); iterator.hasNext();) {
//			LineSummaryVM lineSummaryVM = (LineSummaryVM) iterator.next();
//			calculatePoint(lineSummaryVM);
//		}
//		
//		return result;
//	}
	
	public static List<Point> calculatePoint(LineSummaryVM lineSummaryVM ) {
		String points = lineSummaryVM.getPoints();
		List<Point> list = getPointList(points);
	
		return list;
	}
	
	public static List<Point> getPointList(String  points){
		
		List<Point>  result = new ArrayList<Point>();
		if(StringUtils.isEmpty(points))
			return result;
		
		String[] p1Points = points.split(";");
		
		for (int i = 0; i < p1Points.length; i++) {
			String[] p1Point = p1Points[i].split(",");
			int x1 = (int )Double.parseDouble(p1Point[0]);
			int y1 = (int )Double.parseDouble(p1Point[1]);
			result.add(new Point(x1, y1));
		}
		
		return result;
	}
	
	
	public static List<Point> calculateProjectedListPoint(List<Point> list,int w,int h,int destWidth,int destHeigth){
		List<Point> result = new ArrayList<Point>();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			result.add(getProjectedLine(point, w, h,destWidth,destHeigth));
		}
		
		return result;
	}
	
	public static Point getProjectedLine(Point source,int sourceWidth,int sourceHeight,int destWidth,int destHeigth) {
		Point result = new Point();
		
		int w = destWidth;
		int h = destHeigth;
		
		int start_X = (int)source.getX();
		int start_Y = (int)source.getY();
		
		Dimension videoDimension = new Dimension(sourceWidth, sourceHeight);
		
		int interpolated_start_x = (int) (1.0f * start_X * w / videoDimension.width);
		int interpolated_start_y = (int) (1.0f * start_Y * h / videoDimension.height);
		
		float aspectRatio = 1.0f * videoDimension.width / videoDimension.height;
		float surfaceRatio = 1.0f * w / h;
		
		if(surfaceRatio > aspectRatio) {
			int actualWidth = (int) (aspectRatio * h);				
			int borderSize = w - actualWidth; //left and right
			interpolated_start_x = (int) (1.0f * start_X * actualWidth / videoDimension.width) + borderSize/2;
		} else {
			int actualHeight = (int) (w / aspectRatio);		
			int borderSize = h - actualHeight; //top and down
			interpolated_start_y = (int) (1.0f * start_Y * actualHeight / videoDimension.height) + borderSize/2;
		}
		
		result.setLocation(interpolated_start_x, interpolated_start_y);
		
		return result;
	}
	
	public static Point findCentroid(List<Point> points) {
	    int x = 0;
	    int y = 0;
	    for (Point p : points) {
	        x += p.x;
	        y += p.y;
	    }
	    Point center = new Point(0, 0);
	    center.x = x / points.size();
	    center.y = y / points.size();
	    return center;
	}
}
