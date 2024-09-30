package shapetools;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;
import java.util.Vector;

import shapetools.GShape.EAnchors;
public abstract class GShape implements Serializable{
	public enum EDrawingStyle{
		e2PStyle,
		eNPStyle
	}
	private Color color;
	protected Color fillColor;
	private float lineWidth = 1.0f;
	private EDrawingStyle eDrawingStyle;
	protected int x1, y1, x2, y2, ox2, oy2;
	protected Shape shape;
	
	// setters and getters
	public void setSelected(int x, int y) {
		this.anchors = new Ellipse2D.Float[9];
		for(int i=0; i<9; i++) {
			this.anchors[i] = new Ellipse2D.Float();
		}
	}
	
	public void clearSelected() {
		this.anchors = null;
	}
	 public void setColor(Color color) {
	        this.color = color;
	    }

	    public Color getColor() {
	        return this.color;
	    }
	    
	    public void setFillColor(Color fillColor) {
	        this.fillColor = fillColor;
	    }

	    public Color getFillColor() {
	        return fillColor;
	    }
	    
	    public void setLineWidth(float width) {
	        this.lineWidth = width;
	    }
	    public float getLineWidth() {
	        return this.lineWidth;
	    }
	    
	    
	public enum EAnchors{
		eRR(new Cursor(Cursor.HAND_CURSOR)),
		eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
		eWW(new Cursor(Cursor.W_RESIZE_CURSOR)),
		eSS(new Cursor(Cursor.S_RESIZE_CURSOR)),
		eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
		eNE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
		eNW(new Cursor(Cursor.NW_RESIZE_CURSOR)),
		eSE(new Cursor(Cursor.SE_RESIZE_CURSOR)),
		eSW(new Cursor(Cursor.SW_RESIZE_CURSOR)),
		eMM(new Cursor(Cursor.CROSSHAIR_CURSOR));

		private Cursor cursor;
		
		private EAnchors(Cursor cursor){
			this.cursor = cursor;
		}
		public Cursor getCursor(){
			return this.cursor;
		}
	}
	private EAnchors eSelectedAnchor;
	protected Ellipse2D.Float[] anchors;

	private double sx, sy;
	private double dx, dy;
	
	public void setSelected(Graphics graphics){
		drawAnchors(graphics);
		
	}
	public EAnchors geteSelectedAnchor(){
		return this.eSelectedAnchor;
	}

	public EDrawingStyle getEDrawingStyle(){
		return this.eDrawingStyle;
	}
	public Cursor getCursor(){
		return this.eSelectedAnchor.getCursor();
	}
	
	public GShape(EDrawingStyle eDrawingStyle, Shape shape) {
		this.eDrawingStyle = eDrawingStyle;
		this.shape = shape;
		this.anchors = null;
		
		this.x1=0;
		this.y1=0;
		this.x2=0;
		this.y2=0;
		this.ox2=0;
		this.oy2=0;
	}
	public abstract GShape clone();

	public void draw(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (fillColor != null) {
            graphics2D.setColor(fillColor);
            graphics2D.fill(shape); 
        }
        graphics2D.setColor(getColor());
        graphics2D.setStroke(new BasicStroke(this.lineWidth));
        graphics2D.draw(shape); 
    }

	
	private void drawAnchors(Graphics graphics){
		Graphics2D graphics2D =(Graphics2D)graphics;
		Rectangle rectangle = this.shape.getBounds();
		int x = rectangle.x;
		int y = rectangle.y;
		int w = rectangle.width;
		int h = rectangle.height;
		int ANCHOR_WIDTH = 10;
		int ANCHOR_HEIGHT = 10;
		this.anchors = new Ellipse2D.Float[EAnchors.values().length-1];
		this.anchors[EAnchors.eRR.ordinal()] = new Ellipse2D.Float(x+w/2, y-30, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNN.ordinal()] = new Ellipse2D.Float(x+w/2-2, y-5, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eWW.ordinal()] = new Ellipse2D.Float(x-5, y+h/2-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSS.ordinal()] = new Ellipse2D.Float(x+w/2-2, y+h-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eEE.ordinal()] = new Ellipse2D.Float(x+w-2, y+h/2-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNE.ordinal()] = new Ellipse2D.Float(x+w-2, y-5, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNW.ordinal()] = new Ellipse2D.Float(x-5, y-5, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSE.ordinal()] = new Ellipse2D.Float(x+w-2, y+h-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSW.ordinal()] = new Ellipse2D.Float(x-5, y+h-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		for (Ellipse2D.Float anchor : this.anchors) {
			graphics2D.draw(anchor);
		}
	}
	
	public void setOrigin(int x1, int y1) {
	//construct 초기값은 주는 버릇이 필요
		this.x1=x1;
		this.y1=y1;
		
		this.ox2=x1;
		this.oy2=y1;
		this.x2=x1;
		this.y2=y1;
	}
	public void movePoint(int x2, int y2) {
		this.ox2=this.x2;
		this.oy2=this.y2;
		this.x2=x2;
		this.y2=y2;
	}
	public abstract void drag(Graphics graphics);
	
	public void addPoint(int x2, int y2) {
		this.x2=x2;
		this.y2=y2;
	}
	
	public boolean onShape(int x, int y) {
		this.eSelectedAnchor = null;
		if(this.anchors != null){
			for(int i=0; i<EAnchors.values().length-1; i++){
				if(this.anchors[i].contains(x, y)){
					this.eSelectedAnchor = EAnchors.values()[i];
					return true;
				}
			}
		}
		boolean isOnShape = this.shape.contains(x, y);
		if(isOnShape) {
			this.eSelectedAnchor = EAnchors.eMM;
		}
		return isOnShape;
	}
	
	

	// Move 구현
	public void startMove(Graphics graphics, int x, int y){
		this.ox2=x;
		this.oy2=y;
		this.x2=x;
		this.y2=y;
	}

    public void keepMove(Graphics graphics, int x, int y){
        
    	Graphics2D graphics2D =(Graphics2D)graphics;
        graphics2D.setXORMode(graphics2D.getBackground()); 
        graphics2D.setColor(this.color); 
        graphics2D.setStroke(new BasicStroke(this.lineWidth)); // 현재 선 굵기로 설정
        graphics2D.draw(this.shape);
        
        if (this.anchors != null) {
            for (Ellipse2D.Float anchor : this.anchors) {
                graphics2D.draw(anchor);
            }
        }
        
        this.ox2 = this.x2;
        this.oy2 = this.y2;
        this.x2 = x;
        this.y2 = y;
        
    	AffineTransform affineTransform = new AffineTransform();
		affineTransform.setToTranslation(x2-ox2,y2-oy2);
		this.shape = affineTransform.createTransformedShape(this.shape);
		
		graphics2D.draw(this.shape);
		updateAnchors();  
		drawAnchors(graphics);  
		
	}

    public void stopMove(Graphics graphics, int x, int y) {
    	Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setPaintMode();
    	updateAnchors();  
	    drawAnchors(graphics); 
    }
    
    // Resize 구현
    public void startResize(Graphics graphics, int x, int y){
		this.ox2=x;
		this.oy2=y;
		this.x2=x;
		this.y2=y;
	}
    private Point2D getResizeFactor() {
    	// 가로와 세로 방향의 크기 조정 비율
    	sx = 1.0;
    	sy = 1.0;
    	
    	// 크기 조정 시 도형의 중심을 맞추기 위해 사용
    	dx = 0.0;
    	dy = 0.0;
    	
    	// 도형 중심값
    	double cx = 0;
    	double cy = 0;  	
    	
    	double w = this.shape.getBounds().getWidth();
    	double h = this.shape.getBounds().getHeight();
    	
    	 switch (this.eSelectedAnchor) {
         case eEE:
             sx = (w + x2 - ox2) / w;
             cx = this.anchors[EAnchors.eWW.ordinal()].getCenterX();
             dx = cx - cx * sx;
             break;
         case eWW:
             sx = (w + ox2 - x2) / w;
             cx = this.anchors[EAnchors.eEE.ordinal()].getCenterX();
             dx = cx - sx * cx;
             break;
         case eSS:
             sy = (h + y2 - oy2) / h;
             cy = this.anchors[EAnchors.eNN.ordinal()].getCenterY();
             dy = cy - cy * sy;
             break;
         case eNN:
             sy = (h + oy2 - y2) / h;
             cy = this.anchors[EAnchors.eSS.ordinal()].getCenterY();
             dy = cy - sy * cy;
             break;
         case eNE:
             sx = (w + x2 - ox2) / w;
             sy = (h + oy2 - y2) / h;
             cx = this.anchors[EAnchors.eWW.ordinal()].getCenterX();
             cy = this.anchors[EAnchors.eSS.ordinal()].getCenterY();
             dx = cx - cx * sx;
             dy = cy - sy * cy;
             break;
         case eNW:
             sx = (w + ox2 - x2) / w;
             sy = (h + oy2 - y2) / h;
             cx = this.anchors[EAnchors.eEE.ordinal()].getCenterX();
             cy = this.anchors[EAnchors.eSS.ordinal()].getCenterY();
             dx = cx - sx * cx;
             dy = cy - sy * cy;
             break;
         case eSE:
             sx = (w + x2 - ox2) / w;
             sy = (h + y2 - oy2) / h;
             cx = this.anchors[EAnchors.eWW.ordinal()].getCenterX();
             cy = this.anchors[EAnchors.eNN.ordinal()].getCenterY();
             dx = cx - cx * sx;
             dy = cy - cy * sy;
             break;
         case eSW:
             sx = (w + ox2 - x2) / w;
             sy = (h + y2 - oy2) / h;
             cx = this.anchors[EAnchors.eEE.ordinal()].getCenterX();
             cy = this.anchors[EAnchors.eNN.ordinal()].getCenterY();
             dx = cx - sx * cx;
             dy = cy - sy * cy;
             break;
         default:
             break;
     }
     return new Point2D.Double(sx, sy);
 }
    public void keepResize(Graphics graphics, int x, int y){
        this.ox2 = this.x2;
        this.oy2 = this.y2;
        this.x2 = x;
        this.y2 = y;
        
    	Graphics2D graphics2D =(Graphics2D)graphics;
        graphics2D.setXORMode(graphics2D.getBackground()); 
        drawAnchors(graphics);
        graphics2D.draw(this.shape);
              
        
        Point2D resizeFactor = getResizeFactor();
        
    	AffineTransform affineTransform = new AffineTransform();
		affineTransform.setToScale(resizeFactor.getX(),resizeFactor.getY());
		affineTransform.translate(dx, dy);
		this.shape = affineTransform.createTransformedShape(this.shape);
	
		
		graphics2D.draw(this.shape);
		
		 updateAnchors();
	     drawAnchors(graphics);
	}
    
    public void stopResize(Graphics graphics, int x, int y) {
        
    }
    
    // Rotate 구현
    public void startRotate(Graphics graphics, int x, int y) {
        this.ox2 = x;
        this.oy2 = y;
        this.x2 = x;
        this.y2 = y;
    }

    public void keepRotate(Graphics graphics, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());
        graphics2D.setColor(this.color);
        graphics2D.setStroke(new BasicStroke(this.lineWidth));
        graphics2D.draw(this.shape);

        double centerX = this.shape.getBounds2D().getCenterX();
        double centerY = this.shape.getBounds2D().getCenterY();
        double angle1 = Math.atan2(oy2 - centerY, ox2 - centerX);
        double angle2 = Math.atan2(y - centerY, x - centerX);
        double rotationAngle = angle2 - angle1;

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(rotationAngle, centerX, centerY);
        this.shape = affineTransform.createTransformedShape(this.shape);

        this.ox2 = x;
        this.oy2 = y;

        graphics2D.draw(this.shape);
    }

    public void stopRotate(Graphics graphics, int x, int y) {
       
    }
    
    private void updateAnchors() {
        Rectangle bounds = this.shape.getBounds();
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;
        int ANCHOR_WIDTH = 10;
        int ANCHOR_HEIGHT = 10;

        this.anchors[EAnchors.eRR.ordinal()] = new Ellipse2D.Float(x+w/2, y-30, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNN.ordinal()] = new Ellipse2D.Float(x+w/2-2, y-5, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eWW.ordinal()] = new Ellipse2D.Float(x-5, y+h/2-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSS.ordinal()] = new Ellipse2D.Float(x+w/2-2, y+h-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eEE.ordinal()] = new Ellipse2D.Float(x+w-2, y+h/2-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNE.ordinal()] = new Ellipse2D.Float(x+w-2, y-5, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNW.ordinal()] = new Ellipse2D.Float(x-5, y-5, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSE.ordinal()] = new Ellipse2D.Float(x+w-2, y+h-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSW.ordinal()] = new Ellipse2D.Float(x-5, y+h-2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
    }
	
}
