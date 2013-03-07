package com.sogou.tilemap;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GoogleSatelliteTileProvider implements TileProvider {

	@Override
	public TileImage GetTileImage(IntPoint pos, int zoom) {
		// TODO Auto-generated method stub
		String url = MakeTileImageUrl(pos, zoom);
		String cachePath = MakeTileImageUrl(pos, zoom);
		if(download(url,cachePath)){
			try {
				BufferedImage image = ImageIO.read(new File(cachePath));
				TileImage tileImage = new TileImage();
				tileImage.img = image;
				return tileImage;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String MakeTileImageUrl(IntPoint pos, int zoom)
    {
		String UrlFormat = "http://mt2.google.cn/vt/lyrs=s@125&hl=zh-CN&gl=CN&src=app&x=%d&y=%d&z=%d&s=Ga";
       return String.format(UrlFormat, pos.x,pos.y,zoom);
    }

	@Override
    public String MakeTileImageCachePath(IntPoint pos, int zoom)
    {
    	String PathFormat = "E:/GoogleSatellite/%d/%d/%d.jpeg";
       return String.format(PathFormat, zoom, pos.x, pos.y);
    }
        
    public boolean download(String urlSever ,String pathLocal){
		try {
			URL url = new URL(urlSever);
		
	        //打开网络输入流     
	        DataInputStream dis = new DataInputStream(url.openStream());

	        //建立一个新的文件
	        FileOutputStream fos = new FileOutputStream(new File(pathLocal));
	       
	        byte[] buffer = new byte[1024];
	       
	        int length;
	       
	        //开始填充数据
	       
	        while((length = dis.read(buffer))>0){
	       
	        	fos.write(buffer,0,length);
	       
	        }
	       
	        dis.close();
	       
	        fos.close();
	        return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}      	
	}



}
