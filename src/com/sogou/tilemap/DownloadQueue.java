package com.sogou.tilemap;

import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;


public class DownloadQueue implements Runnable {

	//ArrayList<DownloadTask> queue = new ArrayList<DownloadTask>();
	Queue<DownloadTask> queue = new LinkedBlockingQueue<DownloadTask>();
	
	TileProvider tileProvider;
	Frame frame;
	boolean run = true;
	
	public DownloadQueue(TileProvider tileProvider){
		this.tileProvider = tileProvider;		
	}
	
	public void setFrame(Frame frame){
		this.frame = frame;
	}
	
	public synchronized void push(DownloadTask task){
		synchronized(queue) { 
			if(!queue.contains(task))
				queue.offer(task);
		}
	}
	
	public synchronized DownloadTask poll(){
		DownloadTask task = null;
		synchronized(queue) { 
			task = queue.poll();
		}
		return task;
	}
	
	public synchronized void clearUseless(IntRect rect, int zoom){
		synchronized(queue) { 
			for (DownloadTask task : queue) { 
                if(task.tile.pos.x < rect.left || task.tile.pos.x > rect.right ||
                		task.tile.pos.y < rect.top || task.tile.pos.y > rect.bottom ||
                		task.tile.zoom != zoom || true){
                			queue.remove(task);
                		}
			}
			//queue.clear();
		}
	}
	
	public synchronized void clear(){
		synchronized(queue) { 
			queue.clear();
		}
	}
	
	public void exit(){
		run = false;
	}
	
	public boolean download(String urlSever ,String pathLocal){
		try {
			URL url = new URL(urlSever);
		
	        //打开网络输入流     
	        DataInputStream dis = new DataInputStream(url.openStream());

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        
	        
	        
	        byte[] buffer = new byte[1024];
	       
	        int length;
	       
	        //开始填充数据
	       
	        while((length = dis.read(buffer))>0){
	       
	        	bos.write(buffer,0,length);
	       
	        }
	        dis.close();
	        
	        //建立一个新的文件
	        
	        FileOutputStream fos = new FileOutputStream(new File(pathLocal));
	        FileLock filelock = fos.getChannel().tryLock();
	        if(filelock!=null){
	        	bos.writeTo(fos);
	        	filelock.release();
	        	bos.close();	       
		        fos.close();
		        return true;
	        }
	        
	        bos.close();	       
	        fos.close();
	        new File(pathLocal).delete();
	        return true;
		} catch(OverlappingFileLockException e){
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}      	
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(run){
			DownloadTask task = poll();
			if(task == null){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				 continue;
			}
			System.out.format("%d %d %d\n", task.tile.pos.x ,task.tile.pos.y,task.tile.zoom);
			
			String imageUrl = tileProvider.MakeTileImageUrl(task.tile.pos, task.tile.zoom);
			
			String pathLocal = tileProvider.MakeTileImageCachePath(task.tile.pos, task.tile.zoom);
			
			File file = new File(pathLocal);
	
			if(!file.exists()){
				if (!file.getParentFile().exists())
					file.getParentFile().mkdirs();
				download(imageUrl,pathLocal);
					
								
			}
			frame.repaint();
		}
	}

}
