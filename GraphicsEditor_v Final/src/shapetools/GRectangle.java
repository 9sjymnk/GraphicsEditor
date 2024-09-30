package shapetools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RectangularShape;

import org.w3c.dom.css.Rect;

import shapetools.GShape.EDrawingStyle;

public class GRectangle extends GShape {
  
	private static final long serialVersionUID = 1L;

public GRectangle() {
      super(EDrawingStyle.e2PStyle, new Rectangle());
      this.shape = new Rectangle();
   }
   
   public GRectangle clone() {
      return new GRectangle();
   }
   
   @Override
   public void drag(Graphics graphics) {
      Graphics2D graphics2D =(Graphics2D)graphics;
      graphics2D.setXORMode(graphics2D.getBackground()); //그림이 있으면 지우고 없으면 그림
      RectangularShape rectangularShape = (RectangularShape)this.shape;
      graphics2D.draw(rectangularShape);
      rectangularShape.setFrame(x1, y1, x2-x1, y2-y1);
      graphics2D.draw(rectangularShape);
      
   }
   
}