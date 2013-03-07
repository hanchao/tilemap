package com.sogou.tilemap;

public class IntPoint {
	@Override
	public int hashCode() {
		return x * 1024 + y;
	}

	@Override
	public boolean equals(Object obj) {
		IntPoint other = (IntPoint) obj;
		return x == other.x && y == other.y;
	}

	public int x;
	public int y;

	public IntPoint(){
		x = 0;
		y = 0;
	}
	
	public IntPoint(int ix, int iy){
		x = ix;
		y = iy;
	}
}
