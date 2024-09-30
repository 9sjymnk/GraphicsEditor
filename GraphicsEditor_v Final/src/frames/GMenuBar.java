package frames;

import menus.GFileMenu;

public class GMenuBar extends javax.swing.JMenuBar {
	private static final long serialVersionUID = 1L;

	public GFileMenu fileMenu;
	
	private GDrawingPanel drawingPanel;
	
	public GMenuBar() {
		this.fileMenu = new GFileMenu("file");
		this.add(this.fileMenu);
	}
	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		
		this.fileMenu.associate(drawingPanel);
	}
	
	public void initialize() {
		
	}
}
