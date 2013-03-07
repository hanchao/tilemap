package com.sogou.tilemap;

public class DoubleRect {
	public double left;
	public double top;
	public double right;
	public double bottom;
	
	public DoubleRect(){
		left = 0;
		top = 0;
		right = 0;
		bottom = 0;
	}
	
	public DoubleRect(double left,double top,double right,double bottom){
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public double Width(){
		return right > left ? right - left : left - right;
	}
	
	public double Height(){
		return top > bottom ? top - bottom : bottom - top;
	}
}
