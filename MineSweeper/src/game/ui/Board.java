package game.ui;

import game.MineSweeper;

import java.util.Random;

import static game.Constants.LOW_BOUNDS;
import static game.Constants.MAX_BOUNDS;

public class Board extends MineSweeper {
	private CellUI[][] cell;
	
	Board() {
		cell = new CellUI[MAX_BOUNDS][MAX_BOUNDS];
	}
	
	public CellUI[][] create() {
		initializeMines();
		boolean[][] mines = getMines();
		MineSweeper.CellState[][] cellStates = getCellStates();
		
		for (int i = 0; i < MAX_BOUNDS; i++) {
			for (int j = 0; j < 10; j++) {
				cell[i][j] = new CellUI(mines[i][j], cellStates[i][j]);
			}
		}
		
		return cell;
	}
	
	public void exposeCell(int row, int column) {
		super.expose(row, column);
		exposeCellUI(row, column);
	}
	
	private void exposeCellUI(int row, int column) {
		boolean isMineCell = cell[row][column].isMine();
		boolean isAdjacentCell = isAdjacentCell(row, column);
		
		if (cell[row][column].getCellState() == CellState.UNEXPOSED) {
			cell[row][column].setCellState(CellState.EXPOSED);
			if (isMineCell) {
				cell[row][column].drawMineCell();
			} else if (isAdjacentCell) {
				int mineCount = adjacentMinesCountAt(row, column);
				cell[row][column].drawAdjacentCell(mineCount);
			} else {
				cell[row][column].drawEmptyCell();
			}
			if (!isMineCell && !isAdjacentCell)
				exposeNeighborsCell(row, column);
		}
		
	}
	
	public void exposeAllMines() {
		for (int i = 0; i < MAX_BOUNDS; i++) {
			for (int j = 0; j < MAX_BOUNDS; j++) {
				if (cell[i][j].isMine() && cell[i][j].getCellState() != CellState.SEALED) {
						cell[i][j].drawMineCell();
				}
				if(!cell[i][j].isMine() && cell[i][j].getCellState() == CellState.SEALED){
					cell[i][j].drawCross();
				}
			}
		}
	}
	
	private void exposeNeighborsCell(int row, int column) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				
				if (row + i >= LOW_BOUNDS && row + i < MAX_BOUNDS &&
						column + j >= LOW_BOUNDS && column + j < MAX_BOUNDS) {
					exposeCellUI(row + i, column + j);
				}
			}
		}
	}
	
	private void initializeMines() {
		int randomSeed = new Random().nextInt(5);
		setMines(randomSeed);
	}
	
	public boolean isAdjacentCell(int row, int column) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				
				if (row + i >= LOW_BOUNDS && row + i < MAX_BOUNDS &&
						column + j >= LOW_BOUNDS && column + j < MAX_BOUNDS) {
					if (cell[row + i][column + j].isMine()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void toggleSealCell(Integer row, Integer column) {
		toggleSeal(row, column);
		
		CellState cellState = cell[row][column].getCellState();
		if (cellState == CellState.EXPOSED) return;
		
		if (cellState == CellState.SEALED) {
			cell[row][column].setCellState(CellState.UNEXPOSED);
			cell[row][column].drawUnexposedCell();
		} else {
			cell[row][column].setCellState(CellState.SEALED);
			cell[row][column].drawSealedCell();
		}
	}
	
}
