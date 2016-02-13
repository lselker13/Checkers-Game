public class TestGame  {

	private Array theArray;

	public TestGame(Array theArray) {
		this.theArray = theArray;
	}

	public void move(Position pos, State theState) {
		if (!theArray.checkLocation(pos).equals(theState))
			theArray.setLocation(pos, theState);
		else{
			theArray.toggleSelected(pos); 
		}
			
	}

}
