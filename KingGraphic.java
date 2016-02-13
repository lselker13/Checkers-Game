import objectdraw.*;
import java.awt.*;


public class KingGraphic {
	public static final Color CENTER=Color.BLUE;
	private FilledOval background;
	private FramedOval interior;
	
	public KingGraphic(int x, int y,int width, int height, 
			Color color, DrawingCanvas canvas){
		background=new FilledOval(x,y,width,height,canvas);
		background.setColor(color);
		interior=new FramedOval(x+width/4,y+height/4,width/2,height/2,canvas);
		interior.setColor(CENTER);
	}
	public void hide(){
		background.hide();
		interior.hide();
	}
	public void show(){
		background.show();
		interior.show();
	}

}
