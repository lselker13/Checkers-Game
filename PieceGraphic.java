import objectdraw.*;
import java.awt.*;


public class PieceGraphic {
	private FilledOval fill;
	private FramedOval frame;
	
	public PieceGraphic(int x, int y,int width, int height, 
			Color color, DrawingCanvas canvas){
		fill=new FilledOval(x,y,width,height,canvas);
		frame=new FramedOval(x,y,width,height,canvas);
		fill.setColor(color);
	}
	public void hide(){
		fill.hide();
		frame.hide();
		
		
	}
	public void show(){
		fill.show();
		frame.show();
	}

}
