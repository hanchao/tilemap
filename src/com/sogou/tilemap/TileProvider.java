package com.sogou.tilemap;

public interface TileProvider {

	public TileImage GetTileImage(IntPoint pos, int zoom);
	
	public String MakeTileImageUrl(IntPoint pos, int zoom);
	
	public String MakeTileImageCachePath(IntPoint pos, int zoom);
	
}
