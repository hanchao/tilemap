package com.sogou.tilemap;

public interface Projection {

	public IntPoint FromLatLngToPixel(DoublePoint latLng, int zoom);

    public DoublePoint FromPixelToLatLng(IntPoint pixel, int zoom);
  
}
