package games.minesweeper;

/**
 * Tile of a field.
 */
public abstract class Tile {
    
    /** Tile states. */
    public enum State {
        /** Open tile. */
        OPEN, 
        /** Closed tile. */
        CLOSED,
        /** Marked tile. */
        MARKED
    }
    
    /** Tile state. */
    private State state = State.CLOSED;
        
    /**
     * Returns current state of this tile.
     * @return current state of this tile
     */
    public State getState() {
        return state;
    }

    /**
     * Sets current current state of this tile.
     * @param state current state of this tile
     */
    void setState(State state) {
        this.state = state;
    }
    /**
     * returns state of tile. If state is OPEN, returns value of clue or mine
     */
    @Override
    public String toString() {
    	String s = "";
    	if(this.state == State.CLOSED){
    		s+="-";
    	}
    	else if(this.state == State.MARKED){
    		s+="M";
    	}
    	return s;
    }
}
