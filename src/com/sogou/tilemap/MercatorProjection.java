package com.sogou.tilemap;

public class MercatorProjection implements Projection {

	
	public static final double MinLatitude = -85.05112878;
	public static final double MaxLatitude = 85.05112878;
	public static final double MinLongitude = -180;
	public static final double MaxLongitude = 180;
    
	public static final double Axis = 6378137;
	public static final double Flattening = (1.0 / 298.257223563);
	
	public static final IntSize tileSize = new IntSize(256, 256);
	
	protected static double Clip(double n, double minValue, double maxValue)
    {
       return Math.min(Math.max(n, minValue), maxValue);
    }
	
	public IntSize GetTileMatrixMinXY(int zoom)
    {
       return new IntSize(0, 0);
    }

    public IntSize GetTileMatrixMaxXY(int zoom)
    {
       int xy = (1 << zoom);
       return new IntSize(xy - 1, xy - 1);
    }
    
    public IntSize GetTileMatrixSizeXY(int zoom)
    {
       IntSize sMin = GetTileMatrixMinXY(zoom);
       IntSize sMax = GetTileMatrixMaxXY(zoom);

       return new IntSize(sMax.width - sMin.width + 1, sMax.height - sMin.height + 1);
    }

    public IntSize GetTileMatrixSizePixel(int zoom)
    {
    	IntSize s = GetTileMatrixSizeXY(zoom);
       return new IntSize(s.width * tileSize.width, s.height * tileSize.height);
    }
    
	@Override
	public IntPoint FromLatLngToPixel(DoublePoint latLng, int zoom) {
		// TODO Auto-generated method stub
		IntPoint ret = new IntPoint();

		latLng.y = Clip(latLng.y, MinLatitude, MaxLatitude);
		latLng.x = Clip(latLng.x, MinLongitude, MaxLongitude);
        
         double x = (latLng.x + 180) / 360;
         double sinLatitude = Math.sin(latLng.y * Math.PI / 180);
         double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

         IntSize s = GetTileMatrixSizePixel(zoom);
         long mapSizeX = s.width;
         long mapSizeY = s.height;

         ret.x = (int)Clip(x * mapSizeX + 0.5, 0, mapSizeX - 1);
         ret.y = (int)Clip(y * mapSizeY + 0.5, 0, mapSizeY - 1);

         return ret;
	}

	@Override
	public DoublePoint FromPixelToLatLng(IntPoint pixel, int zoom) {
		// TODO Auto-generated method stub
		DoublePoint ret = new DoublePoint();

        IntSize s = GetTileMatrixSizePixel(zoom);
        double mapSizeX = s.width;
        double mapSizeY = s.height;

        double xx = (Clip(pixel.x, 0, mapSizeX - 1) / mapSizeX) - 0.5;
        double yy = 0.5 - (Clip(pixel.y, 0, mapSizeY - 1) / mapSizeY);

        ret.y = 90 - 360 * Math.atan(Math.exp(-yy * 2 * Math.PI)) / Math.PI;
        ret.x = 360 * xx;

        return ret;
	}

}
