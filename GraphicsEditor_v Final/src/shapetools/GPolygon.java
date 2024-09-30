package shapetools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

import global.Constants;

public class GPolygon extends GShape{
    
	private static final long serialVersionUID = 1L;
	private Polygon polygon;
    
    public GPolygon() {
        super(EDrawingStyle.eNPStyle, new Polygon());
           
    }

    @Override
    public GShape clone() {
        return new GPolygon();
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D =(Graphics2D)graphics;
		graphics2D.setXORMode(graphics2D.getBackground()); //그림이 있으면 지우고 없으면 그림
		// erase old shape 
		 Polygon polygon = (Polygon)this.shape;
		 graphics.drawLine(
				 polygon.xpoints[polygon.npoints-2], polygon.ypoints[polygon.npoints-2],
				 polygon.xpoints[polygon.npoints-1], polygon.ypoints[polygon.npoints-1]                                                    
				 );
		 polygon.xpoints[polygon.npoints-1]= x2;
		 polygon.ypoints[polygon.npoints-1]= y2;
		
		 graphics.drawLine(
				 polygon.xpoints[polygon.npoints-2], polygon.ypoints[polygon.npoints-2],
				 polygon.xpoints[polygon.npoints-1], polygon.ypoints[polygon.npoints-1]                                                    
				 );
    }

    @Override
    public void setOrigin(int x, int y) {
        //construct 초기값은 주는 버릇이 필요
        Polygon polygon = (Polygon)this.shape;
        polygon.addPoint(x, y);
        polygon.addPoint(x, y);;
    	
    }
    @Override
    public void addPoint(int x, int y){
    	  Polygon polygon = (Polygon)this.shape;
    	  polygon.addPoint(x, y);
    }

}
