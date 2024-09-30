package frames;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import global.Constants;
import global.Constants.EShapeButtons;
import global.Constants.EToolButtons;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    // attributes

    // components
    private GMenuBar menuBar;
    private GShapeToolBar shapeToolBar;
    private GDrawingPanel drawingPanel;

    // constructor
    public GMainFrame() {
        // set attributes
        // setLocation 만들기
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create components
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        this.menuBar = new GMenuBar();
        this.setJMenuBar(this.menuBar);

        ShapeActionHandler shapeActionHandler = new ShapeActionHandler();
        this.shapeToolBar = new GShapeToolBar(shapeActionHandler);
        this.add(shapeToolBar, BorderLayout.NORTH);

        this.drawingPanel = new GDrawingPanel();
        this.add(drawingPanel, BorderLayout.CENTER);

        // associate
        this.menuBar.associate(this.drawingPanel);
        this.shapeToolBar.associate(this.drawingPanel);
    }

    // methods
    public void initialize() {
        this.menuBar.initialize();
        this.shapeToolBar.initialize();
        this.drawingPanel.initialize();
    }

    public class ShapeActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                EShapeButtons eShapeButton = EShapeButtons.valueOf(e.getActionCommand());
                if (eShapeButton == EShapeButtons.eErase) {
                    drawingPanel.clear();
                } else if (eShapeButton == EShapeButtons.eTool) {

                } else {
                    drawingPanel.setShapeTool(eShapeButton.getShapeTool());
                }
                shapeToolBar.setSelectedButton(e.getActionCommand());
            } catch (IllegalArgumentException ex) {
                EToolButtons eToolButton = EToolButtons.valueOf(e.getActionCommand());
                if (eToolButton == EToolButtons.eColor) {
                    drawingPanel.chooseColor();
                } else if (eToolButton == EToolButtons.eStroke) {
                    drawingPanel.chooseLineWidth();
                } else if (eToolButton == EToolButtons.eBackground) { 
                    drawingPanel.chooseBackgroundColor();
                } else if (eToolButton == EToolButtons.eFill) { 
                    drawingPanel.chooseFillColor();
                    }
            }
        }
    }

    public class MenuActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub

        }
    }
}