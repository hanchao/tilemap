package com.sogou.tilemap;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class BingSatelliteTileProvider implements TileProvider {

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
    	String UrlFormat = "http://ecn.dynamic.t1.tiles.virtualearth.net/comp/ch/%s?mkt=en-us&it=A&shading=hill&og=3&n=z";
    	String key = TileXYToQuadKey(pos, zoom);
       return String.format(UrlFormat, key);
    }

	@Override
    public String MakeTileImageCachePath(IntPoint pos, int zoom)
    {
    	String PathFormat = "E:/BingSatellite/%d/%d/%d.jpeg";
       return String.format(PathFormat, zoom, pos.x, pos.y);
    }
    
    String TileXYToQuadKey(IntPoint pos, int zoom)
    {
       StringBuilder quadKey = new StringBuilder();
       for(int i = zoom; i > 0; i--)
       {
          char digit = '0';
          int mask = 1 << (i - 1);
          if((pos.x & mask) != 0)
          {
             digit++;
          }
          if((pos.y & mask) != 0)
          {
             digit++;
             digit++;
          }
          quadKey.append(digit);
       }
       return quadKey.toString();
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
