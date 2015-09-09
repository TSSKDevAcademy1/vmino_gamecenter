package games.stones;

public class Space extends Tile {
	
	private int value;
	
	public Space (){
		this.value = Integer.MAX_VALUE;
	}
	
	@Override
	public String toString() {
		return " ";
	}
	
	
}
