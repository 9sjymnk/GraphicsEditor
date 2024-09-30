package shapetools;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class GLine extends GShape {
	public GLine() {
		super(EDrawingStyle.e2PStyle, new Line2D.Float());
	}
	public GLine clone() {
		return new GLine();
	}
	@Override
	public void drag(Graphics graphics) {
		Graphics2D graphics2D =(Graphics2D)graphics;
		graphics2D.setXORMode(graphics2D.getBackground()); //그림이 있으면 지우고 없으면 그림
		
		Line2D.Float shape = (Line2D.Float)this.shape;
//		shape.setLine(x1, y1, x2, y2);
		graphics2D.draw(shape);
		shape.setLine(x1, y1, x2, y2);
		graphics2D.draw(shape);
	}
	
}
