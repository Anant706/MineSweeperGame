package game;

import java.util.Random;

import static game.Constants.LOW_BOUNDS;
import static game.Constants.MAX_BOUNDS;

public class MineSweeper {

	public enum CellState {UNEXPOSED, EXPOSED, SEALED}
	public enum GameStatus {INPROGRESS, WON, LOST}
	private final int TOTAL_MINES = 10;

	private CellState[][] cellStates = new CellState[MAX_BOUNDS][MAX_BOUNDS];
	private boolean[][] mines = new boolean[MAX_BOUNDS][MAX_BOUNDS];

	public MineSweeper() {
    for (int i = LOW_BOUNDS; i < MAX_BOUNDS; i++) {
      for (int j = LOW_BOUNDS; j < MAX_BOUNDS; j++) {
        cellStates[i][j] = CellState.UNEXPOSED;
        mines[i][j] = false;
      }
    }
  }

  public void expose(int row, int column) {
		if (cellStates[row][column] == CellState.UNEXPOSED) {
			cellStates[row][column] = CellState.EXPOSED;
			if (!mines[row][column] && !isAdjacent(row, column))
				exposeNeighbors(row, column);
		}
	}

	private boolean isAdjacent(int row, int column) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}

				if (row + i >= LOW_BOUNDS && row + i < MAX_BOUNDS &&
						column + j >= LOW_BOUNDS && column + j < MAX_BOUNDS) {
					if (mines[row + i][column + j]) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void toggleSeal(int row, int column) {
		if (cellStates[row][column] == CellState.EXPOSED) return;

		if (cellStates[row][column] == CellState.SEALED) {
			cellStates[row][column] = CellState.UNEXPOSED;
		} else {
			cellStates[row][column] = CellState.SEALED;
		}
	}

	public CellState getCellStatus(int row, int column) {
		return cellStates[row][column];
	}

	public void exposeNeighbors(int row, int column) {

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}

				if (row + i >= LOW_BOUNDS && row + i < MAX_BOUNDS &&
						column + j >= LOW_BOUNDS && column + j < MAX_BOUNDS) {
					expose(row + i, column + j);
				}
			}
		}
	}

	public boolean isMineAt(int row, int column) {
		if ((row < LOW_BOUNDS || row >= MAX_BOUNDS) || (column < LOW_BOUNDS || column >= MAX_BOUNDS))
			return false;

		return mines[row][column];
	}


	public int adjacentMinesCountAt(int row, int column) {
		int mineCount = 0;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}

				if (row + i >= LOW_BOUNDS && row + i < MAX_BOUNDS &&
						column + j >= LOW_BOUNDS && column + j < MAX_BOUNDS) {
					if (mines[row + i][column + j]) {
						mineCount++;
					}
				}
			}
		}

		return mineCount;
	}


	public GameStatus getGameStatus() {
		int sealedMineCount = 0;
		int exposedCellCount = 0;

		for (int i = 0; i < MAX_BOUNDS; i++) {
			for (int j = 0; j < MAX_BOUNDS; j++) {
				if (mines[i][j] && cellStates[i][j] == CellState.EXPOSED)
					return GameStatus.LOST;

				if (mines[i][j] && cellStates[i][j] == CellState.SEALED) {
					sealedMineCount += 1;
				}

				if (!mines[i][j] && cellStates[i][j] == CellState.EXPOSED) {
					exposedCellCount += 1;
				}
			}
		}

		boolean areAllOtherCellsExposed = exposedCellCount == (MAX_BOUNDS * MAX_BOUNDS) - TOTAL_MINES;
		if ((sealedMineCount == TOTAL_MINES || sealedMineCount == 0) && areAllOtherCellsExposed)
			return GameStatus.WON;
		else
			return GameStatus.INPROGRESS;
	}

	public void setMines(int seed) {
		int count = 0;
		int bound = 10;

		while (count != bound) {
			Random generator = new Random(seed);
			int rowIndex = generator.nextInt(bound);
			int colIndex = generator.nextInt(bound);
			mines[rowIndex][colIndex] = true;
			count++;
			seed--;
		}
	}

	public void setMine(int row, int column) {
		mines[row][column] = true;
	}

	public boolean[][] getMines() {
		return mines;
	}

	public CellState[][] getCellStates() {
		return cellStates;
	}


}