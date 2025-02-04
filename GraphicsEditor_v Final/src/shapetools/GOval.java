package shapetools;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

import shapetools.GShape.EDrawingStyle;

public class GOval extends GShape {
	public GOval() {
		super(EDrawingStyle.e2PStyle, new Ellipse2D.Float());
	}
	public GOval clone() {
		return new GOval();
	}
	@Override
	public void drag(Graphics graphics) {
		Graphics2D graphics2D =(Graphics2D)graphics;
		graphics2D.setXORMode(graphics2D.getBackground()); //그림이 있으면 지우고 없으면 그림
		
		RectangularShape shape = (RectangularShape)this.shape;
		shape.setFrame(x1, y1, ox2-x1, oy2-y1);
		graphics2D.draw(shape);
		shape.setFrame(x1, y1, x2-x1, y2-y1);
		graphics2D.draw(shape);
	}
}
