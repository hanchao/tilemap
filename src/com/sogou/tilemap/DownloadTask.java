package com.sogou.tilemap;

public class DownloadTask {

	
	Tile tile = new Tile();
	
	public DownloadTask(){
	
	}
	
	public DownloadTask(Tile tile){
		this.tile = tile;
	}
	
	public DownloadTask(IntPoint pos,int zoom){
		tile.pos = pos;
		tile.zoom = zoom;
	}
	
	@Override
	public int hashCode() {
		return tile.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		DownloadTask other = (DownloadTask) obj;
		return tile.equals(other.tile);
	}
}
