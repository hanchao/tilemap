package com.sogou.tilemap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class CacheQueue {
	
	final int MAX_SIZE = 64;
	 Map<Tile, TileImage> cache = new HashMap<Tile, TileImage>();
	 
	 public void put(Tile tile, TileImage image){
		synchronized(cache) {
			if(cache.containsKey(tile)){
				return;
			}
		
			if(cache.size() == MAX_SIZE){
				Tile deleteTile = null;
				for (Tile key : cache.keySet()) { 
	                 if(key.zoom != tile.zoom)
	                	 deleteTile = key;
				}
				if(deleteTile == null){
					double MaxLength = 0;
					for (Tile key : cache.keySet()) { 
						double length = Math.pow(key.pos.x - tile.pos.x, 2) + Math.pow(key.pos.y - tile.pos.y, 2);
		                 if(MaxLength < length){
		                	 MaxLength = length;
		                	 deleteTile = key;
		                 }
					}
				}
				
				cache.remove(deleteTile);
			}			    		
            
			cache.put(tile, image);
		}
	}
	
	public TileImage get(Tile tile){
		TileImage image = null;
		synchronized(cache) { 
			image = cache.get(tile);
		}
		return image;
	}
	
	public void clearUseless(IntRect rect, int zoom){
		synchronized(cache) { 
			
			for (Tile tile : cache.keySet()) { 
                 
                //Tile tile = entry.getKey();
                //cache.remove(tile);
			}
			
			for (Iterator<Entry<Tile, TileImage>> it = cache.entrySet().iterator(); it.hasNext();) { 
                Entry<Tile, TileImage> entry = it.next(); 
                Tile tile = entry.getKey();
                
  
//                if((tile.pos.x < rect.left-16 || tile.pos.x > rect.right+16 ||
//                		tile.pos.y < rect.top-16 || tile.pos.y > rect.bottom+16) &&
//                		tile.zoom == zoom){
//        			cache.remove(tile);
//        		}
//                else if((tile.pos.x < rect.left*2-16 || tile.pos.x > rect.right*2+16 ||
//                		tile.pos.y < rect.top*2-16 || tile.pos.y > rect.bottom*2+16) &&
//                		tile.zoom == zoom+1){
//                	
//                }
//                else if((tile.pos.x < rect.left/2-16 || tile.pos.x > rect.right/2+16 ||
//                		tile.pos.y < rect.top/2-16 || tile.pos.y > rect.bottom/2+16) &&
//                		tile.zoom == zoom-1){
//                	
//                }
			}
			
		
			
		}
	}
	
	public void clear(){
		synchronized(cache) { 
			cache.clear();
		}
	}
	
	
}
