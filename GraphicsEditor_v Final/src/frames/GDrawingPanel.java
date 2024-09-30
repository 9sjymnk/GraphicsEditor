package frames;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import shapetools.GShape;
import shapetools.GShape.EAnchors;
import shapetools.GShape.EDrawingStyle;

public class GDrawingPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private enum EDrawingState {
        eIdle,
        e2PState,
        eNPState,
        eTransformState;
    }

    private EDrawingState eDrawingState;

    private Color currentColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE; 
    private Vector<GShape> shapes;
    private GShape shapeTool;
    private GShape currentShape;
    private Color fillColor = Color.WHITE;
    private Image offScreenImage;
    private Graphics offScreenGraphics;

    // constructor
    public GDrawingPanel() {
        // attributes
        this.setBackground(Color.WHITE);
        this.eDrawingState = EDrawingState.eIdle;

        // components
        MouseEventHandler mouseEventHandler = new MouseEventHandler();
        this.addMouseListener(mouseEventHandler);
        this.addMouseMotionListener(mouseEventHandler);

        // Dynamic component
        this.shapes = new Vector<GShape>();
    }

    public void setShapeTool(GShape shapeTool) {
        this.shapeTool = shapeTool;
    }
    
    
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (offScreenImage == null || offScreenImage.getWidth(this) != getWidth() || offScreenImage.getHeight(this) != getHeight()) {
            offScreenImage = createImage(getWidth(), getHeight());
            offScreenGraphics = offScreenImage.getGraphics();
        }

        offScreenGraphics.setColor(backgroundColor);
        offScreenGraphics.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D graphics2D = (Graphics2D) offScreenGraphics;

        for (GShape shape : shapes) {
            shape.draw(graphics2D);
        }

        if (currentShape != null && eDrawingState == EDrawingState.eTransformState) {
            currentShape.draw(graphics2D);
            currentShape.setSelected(graphics2D);
        }

        graphics.drawImage(offScreenImage, 0, 0, this);
    }
    // 지우개 메소드
    public void clear() {
        shapes.clear();
        repaint();
    }
    
    // 색채우기 메소드
    public void chooseFillColor() {
        Color newFillColor = JColorChooser.showDialog(this, "채우기 색상 선택", fillColor);
        if (newFillColor != null) {
            fillColor = newFillColor;
            if (currentShape != null) {
                currentShape.setFillColor(newFillColor);
            }
            shapeTool.setFillColor(newFillColor); 
            repaint();
        }
    }
    // 색변경 메소드
    public void chooseColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose a color", currentColor);
        if (newColor != null) {
            currentColor = newColor;
            if (currentShape != null) {
                currentShape.setColor(newColor);
            }
            shapeTool.setColor(newColor);
            repaint();
        }
    }
    // 배경색 변경 메소드
    public void chooseBackgroundColor() {
        Color newBackgroundColor = JColorChooser.showDialog(this, "Choose a background color", backgroundColor);
        if (newBackgroundColor != null) {
            backgroundColor = newBackgroundColor;
            repaint();
        }
    }
    // 선굵기 조절 메소드
    public void chooseLineWidth() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (currentShape != null) {
                    float width = slider.getValue();
                    currentShape.setLineWidth(width);
                    repaint();
                }
            }
        });
        JOptionPane.showMessageDialog(null, slider, "Select Line Width", JOptionPane.QUESTION_MESSAGE);
    }

    private void startDrawing(int x, int y) {
        currentColor = Color.BLACK;
        currentShape = shapeTool.clone();
        currentShape.setOrigin(x, y);
        currentShape.setColor(currentColor);
    }

    private void keepDrawing(int x, int y) {
        if (eDrawingState == EDrawingState.eNPState) {
            currentShape.drag(getGraphics());
        }
        currentShape.movePoint(x, y);
        currentShape.drag(getGraphics());
    }

    private void stopDrawing(int x, int y) {
        shapes.add(currentShape);
        currentShape.setSelected(getGraphics());
    }

    private void continueDrawing(int x, int y) {
        currentShape.addPoint(x, y);
    }

    public void initialize() {
        offScreenImage = createImage(getWidth(), getHeight());
        offScreenGraphics = offScreenImage.getGraphics();
    }

    public Vector<GShape> getShapes() {
        return shapes;
    }

    public void setShapes(Object shapes) {
        this.shapes = (Vector<GShape>) shapes;
        this.repaint();
    }

    private GShape onShape(int x, int y) {
        for (GShape shape : this.shapes) {
            boolean isShape = shape.onShape(x, y);
            if (isShape) {
                return shape;
            }
        }
        return null;
    }

    private void changeCursor(int x, int y) {
        GShape shape = onShape(x, y);
        if (shape == null) {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            this.setCursor(shape.getCursor());
        }
    }

    private class MouseEventHandler implements MouseListener, MouseMotionListener {
        private void mouse1Clicked(MouseEvent e) {
            if (eDrawingState == EDrawingState.eNPState) {
                continueDrawing(e.getX(), e.getY());
                eDrawingState = EDrawingState.eNPState;
            } else if (shapeTool.getEDrawingStyle() == EDrawingStyle.eNPStyle) {
                startDrawing(e.getX(), e.getY());
                eDrawingState = EDrawingState.eNPState;
            }
        }

        private void mouse2Clicked(MouseEvent e) {
            if (eDrawingState == EDrawingState.eNPState) {
                stopDrawing(e.getX(), e.getY());
                eDrawingState = EDrawingState.eIdle;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                mouse1Clicked(e);
            } else if (e.getClickCount() == 2) {
                mouse2Clicked(e);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (eDrawingState == EDrawingState.eIdle) {
                changeCursor(e.getX(), e.getY());
            } else if (eDrawingState == EDrawingState.eNPState) {
                keepDrawing(e.getX(), e.getY());
                eDrawingState = EDrawingState.eNPState;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (eDrawingState == EDrawingState.eIdle) {
                currentShape = onShape(e.getX(), e.getY());
                if (currentShape == null) {
                    if (shapeTool.getEDrawingStyle() == EDrawingStyle.e2PStyle) {
                        startDrawing(e.getX(), e.getY());
                        eDrawingState = EDrawingState.e2PState;
                    }
                } else {
                    if (currentShape.geteSelectedAnchor() == EAnchors.eMM) {
                        currentShape.startMove(getGraphics(), e.getX(), e.getY());
                    } else if (currentShape.geteSelectedAnchor() == EAnchors.eRR) {

                    } else {
                    currentShape.startResize(getGraphics(), e.getX(), e.getY());
                    }
                    eDrawingState = EDrawingState.eTransformState;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (eDrawingState == EDrawingState.e2PState) {
                keepDrawing(e.getX(), e.getY());
            } else if (eDrawingState == EDrawingState.eTransformState) {
                if (currentShape.geteSelectedAnchor() == EAnchors.eMM) {
                    currentShape.keepMove(getGraphics(), e.getX(), e.getY());
                } else if (currentShape.geteSelectedAnchor() == EAnchors.eRR) {
                	currentShape.keepRotate(getGraphics(), e.getX(), e.getY());
                } else {
                    currentShape.keepResize(getGraphics(), e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (eDrawingState == EDrawingState.e2PState) {
                stopDrawing(e.getX(), e.getY());
                eDrawingState = EDrawingState.eIdle;
            } else if (eDrawingState == EDrawingState.eTransformState) {
                currentShape.stopMove(getGraphics(), e.getX(), e.getY());
                if (currentShape.geteSelectedAnchor() == EAnchors.eMM) {
                    currentShape.stopMove(getGraphics(), e.getX(), e.getY());
                } else if (currentShape.geteSelectedAnchor() == EAnchors.eRR) {
                	currentShape.stopRotate(getGraphics(), e.getX(), e.getY());
                } else {
                    currentShape.stopResize(getGraphics(), e.getX(), e.getY());
                }
                eDrawingState = EDrawingState.eIdle;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}