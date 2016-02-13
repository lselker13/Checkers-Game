import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class CheckersGame implements ActionListener, Game {

	private Array theArray;
	private Turn theTurn = Turn.P1;
	private int turnIndex = 0;
	// whether you currently are selecting a king
	private boolean kingSelected = false;

	private static final int NROWS = BoardController.NROWS;
	private static final int NCOLUMNS = BoardController.NCOLUMNS;
	private static final int SETUP_WIDTH = 3;

	private JButton endTurn = new JButton("End turn");
	// saves if a particular piece just jumped so could double jump. Null if no
	// one jumped
	private Position jumping = null;

	public CheckersGame(Array theArray, Container contentPane) {
		this.theArray = theArray;
		this.setup();
		contentPane.add(endTurn, BorderLayout.SOUTH);
		endTurn.addActionListener(this);
		contentPane.validate();

	}

	private enum Turn { // Keeps track of the players' colors and directions of
						// movement

		P1(State.RED, State.KRED, Direction.UP), P2(State.BLACK, State.KBLACK,
				Direction.DOWN);
		private State s;
		private State ks;
		private Direction dir;

		private Turn(State s, State ks, Direction dir) {
			this.s = s;
			this.dir = dir;
			this.ks = ks;
		}

		public State theState() {
			return s;
		}

		public State theKState() {
			return ks;
		}

		public Direction theDirection() {
			return dir;
		}

		public State theOtherState() {
			if (s == State.RED)
				return State.BLACK;
			else
				return State.RED;
		}

		public State theOtherKState() {
			if (ks == State.KRED)
				return State.KBLACK;
			else
				return State.KRED;
		}

	}

	public void move(Position pos) { // Executes if you click
		// refers to the piece to be moved
		Position prev = theArray.whichSelected();
		// check if a king is selected for the getmoves and getjumps methods
		kingSelected = theArray.checkLocation(prev) == theTurn.theKState();
		// checks if either your piece or your king is selected, and if that piece can 
		// be moved based on the jumping situation; if not the for
		// loops will be skipped
		boolean moveableSelected = (theArray.checkLocation(prev) == theTurn
				.theState() || kingSelected)&&(jumping==null||jumping.equals(prev));
		Position[] moves = getMoves(prev);
		Position[] jumps = getJumps(prev);
		

		// goes through the arrays and checks for if the position is a legal
		// move. If so,
		// it makes the move, switches the turn, and exits, or deals with double
		// jumping.

		// moves.length is half of jumps.length so we can use the same for loop
		// for both -- the first half of jumps has the destinations in it.

		if (moveableSelected) {
			for (int i = 0; i < moves.length; i++) {
				if (pos.equals(moves[i])&&jumping==null) {
					moveTo(prev, pos);
					nextTurn();
					return;
				}
				if (pos.equals(jumps[i])) {
					jumpTo(prev, jumps[i + moves.length], pos);
					return;
				}
			}
		}

		// if you click your own piece
		if (theArray.checkLocation(pos).equals(theTurn.theState())) {
			theArray.toggleSelected(pos);

		}
		// if you click your own king
		else if (theArray.checkLocation(pos).equals(theTurn.theKState())) {

			theArray.toggleSelected(pos);

		}

		// Any other result: You click an empty non-move, you click an opposing
		// piece or king
		else {
			theArray.deSelect();

		}

		/**
		 * 
		 * The old code to run the game -- still works
		 * 
		 * 
		 * // if you click empty space and it's a legal non-jump move, and
		 * you're // not in // the middle of a double jump else if
		 * (this.isMove(pos) && jumping == null) { this.moveTo(prev, pos);
		 * nextTurn();
		 * 
		 * } // if you click empty space and it's a legal jump else if (jumping
		 * == null || jumping.equals(prev)) { this.tryJump(pos, prev); }
		 * 
		 * // if you click on an empty square but it's not a move else {
		 * theArray.deSelect(); kingSelected = false;
		 * System.out.println("You clicked an empty space that wasn't a move");
		 * }
		 */

	}


	// used as a shortcut to check diagonally
	private Position checkUp(Position pos, Direction dir, int mult) {
		Position posTemp = pos.shift(theTurn.theDirection(), mult).shift(dir,
				mult);
		return posTemp;
	}

	// used as a shortcut to jump a piece
	private void jumpTo(Position prev, Position mid, Position pos) {
		moveTo(prev, pos);
		theArray.setLocation(mid, State.EMPTY);
		Position[] jumps=getJumps(pos);
		boolean canJump=false;
		for(int i=0;i<jumps.length;i++){
			if (jumps[i]!=null)canJump=true;
		}
		if(canJump){
			theArray.select(pos);
			endTurn.setVisible(true);
			jumping = pos;
		}
		else nextTurn();
	}

	// Moves the piece where it needs to go. Also handles kinging.
	private void moveTo(Position start, Position end) {
		State temp = theArray.checkLocation(start);
		theArray.setLocation(start, State.EMPTY);
		// kinging
		if (end.shift(theTurn.theDirection(), 1).getRow() < 0
				|| end.shift(theTurn.theDirection(), 1).getRow() > NROWS - 1) {
			theArray.setLocation(end, theTurn.theKState());
		} else
			theArray.setLocation(end, temp);

	}

	// returns the non-jump moves available for a piece
	private Position[] getMoves(Position pos) {
		// a null array
		Position[] nullpos = { null };
		if (pos==null)return nullpos;
		

		// define positions and booleans for if it's occupied
		Position upLeft = checkUp(pos, Direction.LEFT, 1);
		Position upRight = checkUp(pos, Direction.RIGHT, 1);
		// left and right switched because of the negative multiplier
		Position downLeft = checkUp(pos, Direction.RIGHT, -1);
		Position downRight = checkUp(pos, Direction.LEFT, -1);
		boolean leftFront = (theArray.checkLocation(upLeft) == State.EMPTY);
		boolean rightFront = (theArray.checkLocation(upRight) == State.EMPTY);
		boolean leftBack = (theArray.checkLocation(downLeft) == State.EMPTY);
		boolean rightBack = (theArray.checkLocation(downRight) == State.EMPTY);

		if (theArray.checkLocation(pos) == theTurn.theState()) {
			Position[] temp = new Position[2];
			if (leftFront)
				temp[0] = upLeft;
			if (rightFront)
				temp[1] = upRight;
			return temp;
		}

		if (theArray.checkLocation(pos) == theTurn.theKState()) {
			Position[] temp = new Position[4];
			if (leftFront)
				temp[0] = upLeft;
			if (rightFront)
				temp[1] = upRight;
			if (leftBack)
				temp[2] = downLeft;
			if (rightBack)
				temp[3] = downRight;
			return temp;
		}

		// otherwise return null

		return nullpos;
	}

	private Position[] getJumps(Position pos) { // returns an array of the
												// possible jump targets,
		// followed by the pieces that would be removed
		Position[] nullpos = { null };
		if (pos==null)return nullpos;

		// define positions for the piece you are jumping over
		Position upLeft1 = checkUp(pos, Direction.LEFT, 1);
		Position upRight1 = checkUp(pos, Direction.RIGHT, 1);
		// left and right switched because of the negative multiplier
		Position downLeft1 = checkUp(pos, Direction.RIGHT, -1);
		Position downRight1 = checkUp(pos, Direction.LEFT, -1);

		// positions for where your destination is
		Position upLeft2 = checkUp(pos, Direction.LEFT, 2);
		Position upRight2 = checkUp(pos, Direction.RIGHT, 2);
		// left and right switched because of the negative multiplier
		Position downLeft2 = checkUp(pos, Direction.RIGHT, -2);
		Position downRight2 = checkUp(pos, Direction.LEFT, -2);

		// booleans for the piece you jump over
		boolean leftFront1 = (theArray.checkLocation(upLeft1) == theTurn
				.theOtherState())
				|| (theArray.checkLocation(upLeft1) == theTurn.theOtherKState());
		boolean rightFront1 = (theArray.checkLocation(upRight1) == theTurn
				.theOtherState())
				|| (theArray.checkLocation(upRight1) == theTurn.theOtherKState());
		boolean leftBack1 = (theArray.checkLocation(downLeft1) == theTurn
				.theOtherState())
				|| (theArray.checkLocation(downLeft1) == theTurn.theOtherKState());
		boolean rightBack1 = (theArray.checkLocation(downRight1) == theTurn
				.theOtherState())
				|| (theArray.checkLocation(downRight1) == theTurn.theOtherKState());

		// booleans for the spot you jump to
		boolean leftFront2 = (theArray.checkLocation(upLeft2) == State.EMPTY);
		boolean rightFront2 = (theArray.checkLocation(upRight2) == State.EMPTY);
		boolean leftBack2 = (theArray.checkLocation(downLeft2) == State.EMPTY);
		boolean rightBack2 = (theArray.checkLocation(downRight2) == State.EMPTY);

		// check if a move lands somewhere empty and jumps a piece. If so return
		// destination,
		// then the piece jumped
		if (theArray.checkLocation(pos) == theTurn.theState()) {
			Position[] temp = new Position[4];
			if (leftFront1 && leftFront2) {
				temp[0] = upLeft2;
				temp[2] = upLeft1;
			}
			if (rightFront1 && rightFront2) {
				temp[1] = upRight2;
				temp[3] = upRight1;
			}
			return temp;
		}

		if (theArray.checkLocation(pos) == theTurn.theKState()) {
			Position[] temp = new Position[8];
			if (leftFront1 && leftFront2) {
				temp[0] = upLeft2;
				temp[4] = upLeft1;
			}
			if (rightFront1 && rightFront2) {
				temp[1] = upRight2;
				temp[5] = upRight1;
			}
			if (leftBack1 && leftBack2) {
				temp[2] = downLeft2;
				temp[6] = downLeft1;
			}
			if (rightBack1 && rightBack2) {
				temp[3] = downRight2;
				temp[7] = downRight1;
			}
			return temp;
		}

		// otherwise return null
		return nullpos;
	}

	
	
	/**
	 * Old code-unused at this point
	 * 
	 */

	// checks if a particular click is a normal move
	private boolean isMove(Position pos) { // checks whether a square is a
											// forward, diagonal move

		if (!kingSelected) {
			return theArray.isSelected(checkBack(pos, Direction.LEFT, 1))
					|| theArray.isSelected(checkBack(pos, Direction.RIGHT, 1));
		} else
			return theArray.isSelected(checkBack(pos, Direction.LEFT, 1))
					|| theArray.isSelected(checkBack(pos, Direction.RIGHT, 1))
					|| theArray.isSelected(checkBack(pos, Direction.LEFT, -1))
					|| theArray.isSelected(checkBack(pos, Direction.RIGHT, -1));
	}

	private void tryJump(Position pos, Position prev) {// If possible, jumps
		if (this.isJumpLeft(pos)) {

			this.moveTo(prev, pos);
			theArray.setLocation(checkBack(pos, Direction.LEFT, 1), State.EMPTY);
			theArray.select(pos);
			endTurn.setVisible(true);
			jumping = pos;

		}
		if (this.isJumpRight(pos)) {

			this.moveTo(prev, pos);
			theArray.setLocation(checkBack(pos, Direction.RIGHT, 1),
					State.EMPTY);
			theArray.select(pos);
			endTurn.setVisible(true);
			jumping = pos;
		}
		if (kingSelected) {
			if (this.isBJumpLeft(pos)) {

				this.moveTo(prev, pos);
				// Note the negative sign switches left/right
				theArray.setLocation(checkBack(pos, Direction.RIGHT, -1),
						State.EMPTY);
				theArray.select(pos);
				endTurn.setVisible(true);
				jumping = pos;
			}
			if (this.isBJumpRight(pos)) {

				this.moveTo(prev, pos);
				theArray.setLocation(checkBack(pos, Direction.LEFT, -1),
						State.EMPTY);
				theArray.select(pos);
				endTurn.setVisible(true);
				jumping = pos;
			}

		}
	}

	private boolean isJumpLeft(Position pos) {// checks if a particular click is
												// a jump left
		Position backLeft1 = checkBack(pos, Direction.LEFT, 1);
		Position backLeft2 = checkBack(pos, Direction.LEFT, 2);

		boolean jumpLeft = (theArray.checkLocation(backLeft1) == theTurn
				.theOtherState() || theArray.checkLocation(backLeft1) == theTurn
				.theOtherKState())
				&& theArray.isSelected(backLeft2);

		return jumpLeft;

	}

	private boolean isJumpRight(Position pos) {// checks if a particular click
												// is a jump right
		Position backRight1 = checkBack(pos, Direction.RIGHT, 1);
		Position backRight2 = checkBack(pos, Direction.RIGHT, 2);
		boolean jumpRight = (theArray.checkLocation(backRight1) == theTurn
				.theOtherState() || theArray.checkLocation(backRight1) == theTurn
				.theOtherKState())
				&& theArray.isSelected(backRight2);
		return jumpRight;
	}

	private boolean isBJumpLeft(Position pos) {// checks if a particular click
												// is
		// a backwards jump left. Note the negative multiplier switches
		// left/right
		Position upLeft1 = checkBack(pos, Direction.RIGHT, -1);
		Position upLeft2 = checkBack(pos, Direction.RIGHT, -2);

		boolean jumpLeft = (theArray.checkLocation(upLeft1) == theTurn
				.theOtherState() || theArray.checkLocation(upLeft1) == theTurn
				.theOtherKState())
				&& theArray.isSelected(upLeft2);

		return jumpLeft;

	}

	private boolean isBJumpRight(Position pos) {// checks if a particular click
		// is a upwards jump right. Note the negative multiplier switches
		// left/right
		Position upRight1 = checkBack(pos, Direction.LEFT, -1);
		Position upRight2 = checkBack(pos, Direction.LEFT, -2);
		boolean jumpRight = (theArray.checkLocation(upRight1) == theTurn
				.theOtherState() || theArray.checkLocation(upRight1) == theTurn
				.theOtherKState())
				&& theArray.isSelected(upRight2);
		return jumpRight;
	}

	private Position checkBack(Position pos, Direction dir, int mult) {
		Position posTemp = pos.shift(theTurn.theDirection(), -mult).shift(dir,
				mult);
		return posTemp;
	}
	
	
	/**
	 * End of old unused code
	 */
	
	private void setup() {
		endTurn.setVisible(false);
		theArray.setIndicators(turnIndex, true);
		for (int i = 0; i < SETUP_WIDTH; i++) {
			for (int j = 0; j < NCOLUMNS; j++) {
				if ((i + j) % 2 == 0)
					theArray.setLocation(new Position(i, j), State.BLACK);
				else
					theArray.setLocation(new Position(i, j), State.EMPTY);
			}
		}

		for (int i = SETUP_WIDTH; i < NROWS - SETUP_WIDTH; i++) {
			for (int j = 0; j < NCOLUMNS; j++) {
				theArray.setLocation(new Position(i, j), State.EMPTY);
			}
		}

		for (int i = NROWS - SETUP_WIDTH; i < NROWS; i++) {
			for (int j = 0; j < NCOLUMNS; j++) {
				if ((i + j) % 2 == 0)
					theArray.setLocation(new Position(i, j), State.RED);
				else
					theArray.setLocation(new Position(i, j), State.EMPTY);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		nextTurn();
		

	}

	private void nextTurn() {
		endTurn.setVisible(false);
		theArray.setIndicators(turnIndex, false);
		turnIndex = (turnIndex + 1) % 2;
		theTurn = Turn.values()[turnIndex];
		theArray.setIndicators(turnIndex, true);
		theArray.deSelect();
		kingSelected = false;
		jumping=null;
	}
}
