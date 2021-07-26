package com.masterteknoloji.trafix.domain.dto;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LineSummaryVM {

	Long id;
	String name;
	String points;
	
	List<Point> pointList = new ArrayList<Point>();
	
	List<Point> projectedPointList = new ArrayList<Point>();
	
	Long count = 0l;
	
	Rectangle rectangle;
	
	Color color;
	
	Color originalColor;
	
	Queue<LineCrossedVM> lastDatas = new LinkedList<LineCrossedVM>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	
	public List<Point> getPointList() {
		return pointList;
	}
	public void setPointList(List<Point> pointList) {
		this.pointList = pointList;
	}
	public List<Point> getProjectedPointList() {
		return projectedPointList;
	}
	public void setProjectedPointList(List<Point> projectedPointList) {
		this.projectedPointList = projectedPointList;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Rectangle getRectangle() {
		return rectangle;
	}
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getOriginalColor() {
		return originalColor;
	}
	public void setOriginalColor(Color originalColor) {
		this.originalColor = originalColor;
	}
	public Queue<LineCrossedVM> getLastDatas() {
		return lastDatas;
	}
	public void setLastDatas(Queue<LineCrossedVM> lastDatas) {
		this.lastDatas = lastDatas;
	}
}
