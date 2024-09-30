package shapetools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import shapetools.GShape.EDrawingStyle;

public class GHeart extends GShape {

    private static final long serialVersionUID = 1L;

    public GHeart() {
        super(EDrawingStyle.e2PStyle, new Path2D.Double());
        this.shape = new Path2D.Double();
    }

    public GHeart clone() {
        return new GHeart();
    }

    @Override
    public void drag(Graphics graphics) {       
    	Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());
        Path2D path = (Path2D) this.shape;
        graphics2D.draw(path);

        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);

        path.reset();
        path.moveTo(x1 + width / 2, y1 + height / 4);
        path.curveTo(x1 + width / 2, y1, x1, y1, x1, y1 + height / 4);
        path.curveTo(x1, y1 + height / 2, x1 + width / 2, y1 + height, x1 + width / 2, y1 + height);
        path.curveTo(x1 + width / 2, y1 + height, x2, y1 + height / 2, x2, y1 + height / 4);
        path.curveTo(x2, y1, x1 + width / 2, y1, x1 + width / 2, y1 + height / 4);
        path.closePath();

        graphics2D.draw(path);
    }
}