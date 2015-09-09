package games.stones;

import java.io.Serializable;
import java.util.Formatter;
import java.util.Random;

import games.stones.Field.Direction;

public class Field implements Serializable {

	private final Tile[][] tiles;
	private final int rowCount;
	private final int columnCount;

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private final long time;

	/**
	 * Constructor for game field, builds new field and generates numbers
	 * 
	 * @param rowCount
	 * @param columnCount
	 */
	public Field(int rowCount, int columnCount) {
		super();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.tiles = new Tile[rowCount][columnCount];
		generateField();
		shuffleField();
		
		time = System.currentTimeMillis();
	}

	/**
	 * Fills field with stones and spaces
	 */
	private void generateField() {
		int value = 1;
		int row = 0;
		int column = 0;
		for (row = 0; row < this.rowCount; row++) {
			for (column = 0; column < this.columnCount; column++) {
				tiles[row][column] = new Stone(value++);
			}
		}
		tiles[row - 1][column - 1] = new Space();
	}

	/**
	 * Shuffles tiles in field
	 */
	private void shuffleField() {
		Random random = new Random();
		int index = 2;
		int previousIndex;
		for (int i = 0; i < rowCount * columnCount * 10; i++) {
			previousIndex = index;
			index = random.nextInt(4);
			if (index == 0 && previousIndex == 1) {
				i--;
				continue;
			}
			if (index == 1 && previousIndex == 0) {
				i--;
				continue;
			}
			if (index == 2 && previousIndex == 3) {
				i--;
				continue;
			}
			if (index == 3 && previousIndex == 2) {
				i--;
				continue;
			}
			if (!move(Direction.values()[index], false)) {
				i--;
			}

		}
	}

	/**
	 * Finds space in field and returns as Tile
	 * 
	 * @return
	 */
	public Tile getSpace() {
		Tile tile = null;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				if (this.tiles[row][column] instanceof Space) {
					tile = this.tiles[row][column];
					return tile;
				}
			}
		}
		return null;
	}

	/**
	 * Returns tile at entered row and column
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public Tile getTile(int row, int column) {
		return tiles[row][column];
	}

	/**
	 * Returns row of inserted Tile
	 * 
	 * @param tile
	 * @return
	 */
	public int getTileRow(Tile tile) {
		int tileRow = 0;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				if (this.tiles[row][column].equals(tile)) {
					tileRow = row;
				}
			}
		}
		return tileRow;
	}

	/**
	 * Returns column of inserted Tile
	 * 
	 * @param tile
	 * @return
	 */
	public int getTileColumn(Tile tile) {
		int tileColumn = 0;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				if (this.tiles[row][column].equals(tile)) {
					tileColumn = column;
				}
			}
		}
		return tileColumn;
	}

	/**
	 * Swaps two tiles in field
	 * 
	 * @param tile1
	 * @param tile2
	 */
	private void swapTiles(Tile tile1, Tile tile2) {
		Tile helpfulTile = this.tiles[getTileRow(tile1)][getTileColumn(tile1)];
		this.tiles[getTileRow(tile1)][getTileColumn(tile1)] = this.tiles[getTileRow(tile2)][getTileColumn(tile2)];
		this.tiles[getTileRow(tile2)][getTileColumn(tile2)] = helpfulTile;
	}

	/**
	 * Swaps two tiles in field based on direction enum
	 * 
	 * @param direction
	 */
	public boolean move(Direction direction, boolean printErrors) {
		Tile tile1 = getSpace();
		int row = getTileRow(tile1);
		int column = getTileColumn(tile1);
		Tile tile2 = null;
		if (direction == Direction.UP) {
			if (row + 1 < rowCount) {
				tile2 = tiles[row + 1][column];
				swapTiles(tile1, tile2);
				return true;
			} else {
				if (printErrors) {
					System.err.println("You can't move here");
				}
				return false;
			}
		}
		if (direction == Direction.DOWN) {
			if (row - 1 >= 0) {
				tile2 = tiles[row - 1][column];
				swapTiles(tile2, tile1);
				return true;
			} else {
				if (printErrors) {
					System.err.println("You can't move here");
				}
				return false;
			}
		}
		if (direction == Direction.LEFT) {
			if (column + 1 < columnCount) {
				tile2 = tiles[row][column + 1];
				swapTiles(tile1, tile2);
				return true;
			} else {
				if (printErrors) {
					System.err.println("You can't move here");
				}
				return false;
			}
		}
		if (direction == Direction.RIGHT) {
			if (column - 1 >= 0) {
				tile2 = tiles[row][column - 1];
				swapTiles(tile2, tile1);
				return true;
			} else {
				if (printErrors) {
					System.err.println("You can't move here");
				}
				return false;
			}
		}
		return false;
	}

	public void moveTile(int rowTile, int columnTile) {
		Tile tile = tiles[rowTile][columnTile];
		Tile space = getSpace();
		int rowSpace = getTileRow(space);
		int columnSpace = getTileColumn(space);
		if ((rowTile - 1 == rowSpace && columnTile == columnSpace)
				|| (rowTile == rowSpace && columnTile - 1 == columnSpace)) {
			swapTiles(space, tile);
		}
		if ((rowTile == rowSpace && columnTile + 1 == columnSpace)
				|| (rowTile + 1 == rowSpace && columnTile == columnSpace)) {
			swapTiles(tile, space);
		}

	}

	/**
	 * if game is solved, changed game state to solved
	 */
	public boolean checkGameState() {
		int number1 = 0;
		int number2 = 0;
		Tile currentTile = null;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				currentTile = tiles[row][column];
				if (currentTile instanceof Stone) {
					number2 = currentTile.getValue();
					if (number2 > number1) {
						number1 = number2;
					} else {
						return false;
					}
				}
			}
		}
		if (currentTile instanceof Space) {
			return true;
		} else {
			return false;
		}
	}
	
	public long getTime() {
		return time;
	}

	/**
	 * Returns field as string
	 */
	@Override
	public String toString() {
		Formatter f = new Formatter();
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				f.format("%3s", tiles[row][column].toString());
			}
			f.format("%n");
		}
		return f.toString();
	}

}
