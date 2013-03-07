package com.sogou.tilemap;

public class Tile {

	IntPoint pos;
	@Override
	public int hashCode() {
		return zoom *2 + pos.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		Tile other = (Tile) obj;
		return pos.equals(other.pos) && zoom == other.zoom;
	}

	int zoom;
	
	public Tile(){
		pos = new IntPoint();
		zoom = 0;
	}
	
	public Tile(IntPoint pos,int zoom){
		this.pos = pos;
		this.zoom = zoom;
	}
	
	
}
