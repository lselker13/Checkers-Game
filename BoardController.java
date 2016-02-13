import objectdraw.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class BoardController extends WindowController implements ActionListener {
	public static final int NROWS = 8;
	public static final int NCOLUMNS = 8;
	public static final int XSTART = 10;
	public static final int YSTART = 10;
	public static final int BOTTOM_SPACE = 50;
	public static final int RIGHT_SPACE = 30;

	private State theState = State.BLACK;
	private Array theArray;
	public Game theGame;

	private int separation;

	private JButton button1, button2;
	private Container panel = new JPanel();

	public void begin() {

		// buttons
		Container contentPane = getContentPane();
		button1 = new JButton("Black");
		button2 = new JButton("Red");
		panel.add(button1);
		panel.add(button2);
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.validate();
		contentPane.validate();
	    // buttons not used in checkers
		panel.setVisible(false);

		// listeners
		button1.addActionListener(this);
		button2.addActionListener(this);

		// Check separation
		int xsep, ysep;
		xsep = (canvas.getWidth() - 2 * XSTART - RIGHT_SPACE) / NCOLUMNS;
		ysep = (canvas.getHeight() - 2 * XSTART - BOTTOM_SPACE) / NROWS;
		separation = Math.min(xsep, ysep);

		int i;

		// draw the shaded squares, first so the lines are on top
		for (i = 0; i < NCOLUMNS ; i++) {
			for (int j = 0; j < NROWS ; j++) {
				if ((i + j) % 2 == 0)
					new FilledRect(XSTART + j * separation, YSTART + i
							* separation, separation, separation, canvas)
							.setColor(Color.GRAY);
			}
		}
		//draw the grid on top of the rectangles
		for (i = 0; i < NCOLUMNS + 1; i++) {
			new Line(XSTART + i * separation, YSTART, XSTART + i * separation,
					YSTART + NROWS * separation, canvas);
		}
		for (i = 0; i < NROWS + 1; i++) {
			new Line(XSTART, YSTART + i * separation, XSTART + NCOLUMNS
					* separation, YSTART + i * separation, canvas);
		}

		// create an array and an instance of the game controller

		theArray = new Array(separation, canvas);
		theGame = new CheckersGame(theArray, contentPane);
	}

	public void onMouseClick(Location pt) {
		// check where you click, if it's in the grid, tell the game to move
		int arx, ary;
		double x, y;
		Position pos;
		x = pt.getX();
		y = pt.getY();
		arx = (int) Math.floor((x - XSTART) / separation);
		ary = (int) Math.floor((y - YSTART) / separation);
		pos = new Position(ary, arx);
		if (x > XSTART && x < XSTART + NROWS * separation && y > YSTART
				&& y < YSTART + NROWS * separation) {
			theGame.move(pos);

		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == button1)
			theState = State.BLACK;
		if (e.getSource() == button2)
			theState = State.RED;

	}

}
