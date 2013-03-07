package com.sogou.tilemap;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;



public class MapView {
	
	
	//MapQuestTileProvider TileProvider = new MapQuestTileProvider();
	OpenStreetMapTileProvider TileProvider = new OpenStreetMapTileProvider();
	MercatorProjection projection = new  MercatorProjection();
	
	int zoom = 0;
	IntSize screenView = new IntSize(0,0);
	DoublePoint latLngCenter = new DoublePoint(0,0);
	
	
	DownloadQueue downloadQueue = new DownloadQueue(TileProvider);
	LoadQueue loadQueue = new LoadQueue(TileProvider);
	final ExecutorService pool = Executors.newFixedThreadPool(8);
	
	
	public void init(Frame frame){
		downloadQueue.setFrame(frame);
		screenView.width = frame.getWidth();
		screenView.height = frame.getHeight();
		for(int i = 0;i<8;i++)
			pool.execute(downloadQueue);
	}
	
	public void exit(){
		
		downloadQueue.clear();
		downloadQueue.exit();
	}
	
	public void setCenter(double x, double y){
		latLngCenter.x = x;
		latLngCenter.y = y;
	}
	public void setZoom(int zoom){
		if(zoom<0){
			zoom=0;
		}
		if(zoom>=18){
			zoom = 18;
		}
		this.zoom = zoom;
	}
	public int getZoom(){
		return zoom;
	}
	
	public DoubleRect GetMapBounds(){
		return null;
	}
	
	public IntPoint FromScreenToPixel(IntPoint Screen) {
		IntPoint ret = new IntPoint();
		
		IntPoint pixelCenter = projection.FromLatLngToPixel(latLngCenter, zoom);
		ret.x = Screen.x + (pixelCenter.x - screenView.width/2);
		ret.y = Screen.y + (pixelCenter.y - screenView.height/2);
		
         return ret;
	}

	public IntPoint FromPixelToScreen(IntPoint pixel) { 
		IntPoint ret = new IntPoint();

		IntPoint pixelCenter = projection.FromLatLngToPixel(latLngCenter, zoom);
		ret.x = pixel.x - (pixelCenter.x - screenView.width/2);
		ret.y = pixel.y - (pixelCenter.y - screenView.height/2);
		
        return ret;
	}
	
	
	public IntRect GetPixelBounds(){
		IntPoint pixelCenter = projection.FromLatLngToPixel(latLngCenter, zoom);
		
		return new IntRect(pixelCenter.x - screenView.width/2,pixelCenter.y - screenView.height/2,
				pixelCenter.x + screenView.width/2,pixelCenter.y + screenView.height/2);
	}
	
	public void move(int x, int y){
		IntPoint pixelCenter = projection.FromLatLngToPixel(latLngCenter, zoom);
		
		pixelCenter.x += x;
		pixelCenter.y += y;
		
		latLngCenter = projection.FromPixelToLatLng(pixelCenter, zoom);
	}
	

	public void paint(Graphics g){
		
		IntRect pixelBounds = GetPixelBounds();
		
		int leftViewTile = pixelBounds.left/projection.tileSize.width;
		int rightViewTile = pixelBounds.right/projection.tileSize.width;
		int topViewTile = pixelBounds.top/projection.tileSize.height;
		int bottomViewTile = pixelBounds.bottom/projection.tileSize.height;
		
		IntSize MinXY = projection.GetTileMatrixMinXY(zoom);
		IntSize MaxXY = projection.GetTileMatrixMaxXY(zoom);
		
		if(leftViewTile<MinXY.width){
			leftViewTile = MinXY.height;
		}
		if(rightViewTile>MaxXY.width){
			rightViewTile = MaxXY.width;
		}
		if(topViewTile<MinXY.height){
			topViewTile = MinXY.height;
		}
		if(bottomViewTile>MaxXY.height){
			bottomViewTile = MaxXY.height;
		}

		downloadQueue.clearUseless(new IntRect(leftViewTile,topViewTile,rightViewTile,bottomViewTile), zoom);
		
		try {
			for(int row=topViewTile;row<=bottomViewTile;row++){
				for(int col=leftViewTile;col<=rightViewTile;col++){

			        Tile tile = new Tile(new IntPoint(col,row), zoom);
			        IntPoint screenPos = FromPixelToScreen(new IntPoint(col*projection.tileSize.width,row*projection.tileSize.height));
					TileImage tileImage = loadQueue.load(tile);
					if(tileImage != null){											
						g.drawImage(tileImage.img,screenPos.x, screenPos.y, null);
					}else{
						downloadQueue.push(new DownloadTask(tile));						
						g.drawRect(screenPos.x, screenPos.y, projection.tileSize.width, projection.tileSize.height);
					}
			        g.drawString(String.format("%d %d", col,row), screenPos.x+4, screenPos.y+16);
				}
			}
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
