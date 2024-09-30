package global;

import shapetools.GHeart;
import shapetools.GLine;
import shapetools.GOval;
import shapetools.GPolygon;
import shapetools.GRectangle;
import shapetools.GShape;
import shapetools.GTriangle;

public class Constants {
	public enum EShapeButtons{
		eRectangle(null, new GRectangle()),
		eTriangle(null, new GTriangle()),
		eHeart(null, new GHeart()),
		eOval(null, new GOval()),
		eLine(null, new GLine()),
		ePolygon(null, new GPolygon()),
		eErase(null, null),
		eTool("tool", null);
		
		private String text;
		private GShape shapeTool;
		private EShapeButtons(String text, GShape shapeTool){
			this.text = text;
			this.shapeTool = shapeTool;
		}
		public String getText() {return this.text;}
		public GShape getShapeTool() {return this.shapeTool;}
	}
	public final static int NUM_POINTS = 20;
	
	 public enum EToolButtons {
	        eFill("fill"),
		 	eColor("color"),
	        eStroke("stroke"),
	        eBackground("background"); 

	        private String text;

	        private EToolButtons(String text) {
	            this.text = text;
	        }

	        public String getText() {
	            return this.text;
	        }
	    }
	 
	public static class GMainFrame {
		public final static int WIDTH = 400;
		public final static int HEIGHT = 600;
	}
	public static class GMenuBar{
		public enum EMenu {
			eFile("파일"),
			eEdit("편집");
			private String text;
			private EMenu (String text) {
				this.text = text;
			}
			public String getText() {return this.text;}
		}		
	}
}