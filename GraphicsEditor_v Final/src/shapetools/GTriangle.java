package shapetools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class GTriangle extends GShape {

    private static final long serialVersionUID = 1L;

    public GTriangle() {
        super(EDrawingStyle.e2PStyle, new Polygon());
        this.shape = new Polygon();
    }

    public GTriangle clone() {
        return new GTriangle();
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());
        Polygon polygon = (Polygon) this.shape;
        graphics2D.draw(polygon);

        int[] xPoints = { x1, (x1 + x2) / 2, x2 };
        int[] yPoints = { y2, y1, y2 };

        polygon.reset();
        polygon.addPoint(xPoints[0], yPoints[0]);
        polygon.addPoint(xPoints[1], yPoints[1]);
        polygon.addPoint(xPoints[2], yPoints[2]);

        graphics2D.draw(polygon);
    }
}