package games.puzzle;

import java.io.Serializable;
import java.util.Formatter;
import java.util.Random;

public class Field implements Serializable {

	private final Tile[][] tiles;
	private final int rowCount;
	private final int columnCount;
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
		this.time = System.currentTimeMillis();
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
				tiles[row][column] = new Tile(value++);
			}
		}
	}

	/**
	 * Shuffles tiles in field
	 */
	private void shuffleField() {
		Random random = new Random();
		int row1;
		int column1;
		int row2;
		int column2;
		for (int i = 0; i < rowCount * columnCount * 10; i++) {
			row1 = random.nextInt(rowCount);
			column1 = random.nextInt(columnCount);
			row2 = random.nextInt(rowCount);
			column2 = random.nextInt(columnCount);
			swapTiles(tiles[row1][column1], tiles[row2][column2]);
		}
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
	public void swapTiles(Tile tile1, Tile tile2) {
		if (getTileRow(tile1) == getTileRow(tile2)){
			if (getTileColumn(tile1) < getTileColumn(tile2)){
				this.tiles[getTileRow(tile1)][getTileColumn(tile1)] = tile2;
				this.tiles[getTileRow(tile2)][getTileColumn(tile2)] = tile1;
			} else {
				this.tiles[getTileRow(tile2)][getTileColumn(tile2)] = tile1;
				this.tiles[getTileRow(tile1)][getTileColumn(tile1)] = tile2;
			}
		} else if (getTileRow(tile1) < getTileRow(tile2)){
			this.tiles[getTileRow(tile1)][getTileColumn(tile1)] = tile2;
			this.tiles[getTileRow(tile2)][getTileColumn(tile2)] = tile1;
		} else {
			this.tiles[getTileRow(tile2)][getTileColumn(tile2)] = tile1;
			this.tiles[getTileRow(tile1)][getTileColumn(tile1)] = tile2;
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
				number2 = currentTile.getValue();
				if (number2 > number1) {
					number1 = number2;
				} else {
					return false;
				}
			}
		}
		return true;
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
