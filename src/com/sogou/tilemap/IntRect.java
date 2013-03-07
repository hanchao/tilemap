package com.sogou.tilemap;

public class IntRect {
	public int left;
	public int top;
	public int right;
	public int bottom;
	
	public IntRect(){
		left = 0;
		top = 0;
		right = 0;
		bottom = 0;
	}
	
	public IntRect(int left,int top,int right,int bottom){
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public int Width(){
		return right > left ? right - left : left - right;
	}
	
	public int Height(){
		return top > bottom ? top - bottom : bottom - top;
	}
}
