import objectdraw.*;
import java.awt.*;


public class SelectorGraphic {
	private FramedOval circle;
	private static final Color THE_COLOR=Color.YELLOW;
	
	public SelectorGraphic(int x, int y,int width, int height, DrawingCanvas canvas){
		circle=new FramedOval(x,y,width,height,canvas);
		circle.setColor(THE_COLOR);
	}
	public void hide(){
		circle.hide();
		
	}
	public void show(){
		circle.show();
	}

}
