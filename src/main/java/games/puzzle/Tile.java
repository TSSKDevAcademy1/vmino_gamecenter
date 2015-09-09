package games.puzzle;

import java.io.Serializable;

public class Tile implements Serializable {
	
	private int value;
	
	public Tile(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
}
