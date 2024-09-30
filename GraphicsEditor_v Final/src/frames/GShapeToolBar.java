package frames;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import global.Constants.EShapeButtons;
import global.Constants.EToolButtons;

public class GShapeToolBar extends JToolBar {
    private static final long serialVersionUID = 1L;
    private GDrawingPanel drawingPanel;
    private JPopupMenu toolPopupMenu;

    public GShapeToolBar(GMainFrame.ShapeActionHandler shapeActionHandler) {
        ButtonGroup buttonGroup = new ButtonGroup();
        
        for (EShapeButtons eShapeButtons : EShapeButtons.values()) {
            if (eShapeButtons != EShapeButtons.eTool) {
                JRadioButton button = new JRadioButton(eShapeButtons.getText());
                // 이미지 아이콘 추가
                ImageIcon icon = new ImageIcon("resource/" + eShapeButtons.name() + ".png");
                Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(img);
                button.setIcon(scaledIcon);

                button.addActionListener(shapeActionHandler);
                button.setActionCommand(eShapeButtons.toString());
                add(button);
                buttonGroup.add(button);
            }
        }
        // Tool 버튼 추가
        JButton toolButton = new JButton("tool");
        toolButton.addActionListener(e -> toolPopupMenu.show(toolButton, toolButton.getWidth(), toolButton.getHeight()));
        add(toolButton);

        // Tool 팝업 메뉴 구성
        toolPopupMenu = new JPopupMenu();
        for (EToolButtons eToolButton : EToolButtons.values()) {
            JMenuItem menuItem = new JMenuItem(eToolButton.getText());
            menuItem.addActionListener(e -> {
                if (eToolButton == EToolButtons.eColor) {
                    drawingPanel.chooseColor();
                } else if (eToolButton == EToolButtons.eStroke) {
                    drawingPanel.chooseLineWidth();
                } else if (eToolButton == EToolButtons.eBackground) { 
                    drawingPanel.chooseBackgroundColor();
                }else if (eToolButton == EToolButtons.eFill) {
                    drawingPanel.chooseFillColor();
                   }
            });
            toolPopupMenu.add(menuItem);
        }
    }
    // 에러 방지를 위해 시작 시 Rectangle을 기본으로 설정
    public void initialize() {
        JRadioButton defaultButton = (JRadioButton) this.getComponent(EShapeButtons.eRectangle.ordinal());
        defaultButton.doClick();
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
    // 눌린 버튼 색깔 표시하기
    public void setSelectedButton(String actionCommand) {
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) instanceof JRadioButton) {
                JRadioButton button = (JRadioButton) getComponent(i);
                if (button.getActionCommand().equals(actionCommand)) {
                    button.setBackground(Color.LIGHT_GRAY); 
                } else {
                    button.setBackground(null); 
                }
            }
        }
    }

    private void setShapeTool(EShapeButtons eShapeButton) {
        drawingPanel.setShapeTool(eShapeButton.getShapeTool());
    }

    private class ShapeActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EShapeButtons eShapeButton = EShapeButtons.valueOf(e.getActionCommand());
            setShapeTool(eShapeButton);
        }
    }
}