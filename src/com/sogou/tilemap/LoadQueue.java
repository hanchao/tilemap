package com.sogou.tilemap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLock;

import javax.imageio.ImageIO;


public class LoadQueue {

	CacheQueue cache = new CacheQueue();
	TileProvider tileProvider;
	
	public LoadQueue(TileProvider tileProvider){
		this.tileProvider = tileProvider;		
	}
	
	public TileImage load(Tile tile){
		TileImage image = cache.get(tile);
		if(image == null){
			String pathLocal = tileProvider.MakeTileImageCachePath(tile.pos, tile.zoom);
			try {
				File file = new File(pathLocal);
				if (file.exists()){
					FileInputStream input = new FileInputStream(file);
					FileLock filelock = input.getChannel().tryLock(0, Long.MAX_VALUE, true);
					if(filelock != null){
						BufferedImage img = ImageIO.read(input);
						filelock.release();					
						image = new TileImage(img);
						cache.put(tile, image);
						input.close();
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				new File(pathLocal).delete();
			}
		}
		return image;
	}
}
