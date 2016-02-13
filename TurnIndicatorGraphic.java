import objectdraw.*;
import java.awt.*;
public class TurnIndicatorGraphic {
	
	
	private FilledOval indicator;
	
	public TurnIndicatorGraphic(int x, int y, int width, int height,DrawingCanvas canvas){
		indicator=new FilledOval(x,y,width,height,canvas);
	}
	public void setColor(Color c){
		indicator.setColor(c);
	}
	public void show(){
		indicator.show();
	}
	public void hide(){
		indicator.hide();
	}

}
