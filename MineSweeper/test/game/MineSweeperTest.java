package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static game.MineSweeper.CellState.*;
import static org.junit.jupiter.api.Assertions.*;

public class MineSweeperTest {
  private MineSweeper mineSweeper;
  private static int MAX_BOUND = 10;

  @BeforeEach
  public void setUp() {
    mineSweeper = new MineSweeper();
  }

  @Test
  public void canary() {
    assertTrue(true);
  }

  @Test
  public void exposeAnUnExposedCell() {
    mineSweeper.expose(3, 2);

    assertEquals(EXPOSED, mineSweeper.getCellStatus(3, 2));
  }

  @Test
  public void exposeAnExposedCell() {
    mineSweeper.expose(3, 2);
    mineSweeper.expose(3, 2);

    assertEquals(EXPOSED, mineSweeper.getCellStatus(3, 2));
  }

  @Test
  public void sealCellOutOfRange() {

    assertAll(
        () -> assertThrows(IndexOutOfBoundsException.class,
            () -> mineSweeper.toggleSeal(-2, 4)),
        () -> assertThrows(IndexOutOfBoundsException.class,
            () -> mineSweeper.toggleSeal(12, 4)),
        () -> assertThrows(IndexOutOfBoundsException.class,
            () -> mineSweeper.toggleSeal(2, -4)),
        () -> assertThrows(IndexOutOfBoundsException.class,
            () -> mineSweeper.toggleSeal(7, 14)));
  }

  @Test
  public void sealAnUnExposedCell() {
    mineSweeper.toggleSeal(3, 5);

    assertEquals(SEALED, mineSweeper.getCellStatus(3, 5));
  }

  @Test
  public void unsealAnSealedCell() {
    mineSweeper.toggleSeal(4, 2);
    mineSweeper.toggleSeal(4, 2);

    assertEquals(UNEXPOSED, mineSweeper.getCellStatus(4, 2));
  }

  @Test
  public void exposeCellOutOfRange() {

    assertAll(() -> assertThrows(ArrayIndexOutOfBoundsException.class, () -> mineSweeper.expose(-2, 4)),
        () -> assertThrows(ArrayIndexOutOfBoundsException.class, () -> mineSweeper.expose(12, 4)),
        () -> assertThrows(ArrayIndexOutOfBoundsException.class, () -> mineSweeper.expose(2, -4)),
        () -> assertThrows(ArrayIndexOutOfBoundsException.class, () -> mineSweeper.expose(7, 14)));
  }

  @Test
  public void exposeASealedCell() {
    mineSweeper.toggleSeal(5, 5);
    mineSweeper.expose(5, 5);

    assertEquals(SEALED, mineSweeper.getCellStatus(5, 5));
  }

  @Test
  public void sealAnExposedCell() {
    mineSweeper.expose(6, 7);
    mineSweeper.toggleSeal(6, 7);

    assertEquals(EXPOSED, mineSweeper.getCellStatus(6, 7));
  }

  @Test
  public void exposedCallExposeNeighbor() {
    boolean[] exposeNeighborsCalled = new boolean[1];

    MineSweeper mineSweeper = new MineSweeper() {
      public void exposeNeighbors(int row, int column) {
        exposeNeighborsCalled[0] = true;
      }
    };

    mineSweeper.expose(3, 2);

    assertTrue(exposeNeighborsCalled[0]);
  }

  @Test
  public void exposeDoesNotCallExposeNeighborsWhenCalledOnExposedCell() {

    boolean[] exposeNeighborsCalled = new boolean[1];

    MineSweeper mineSweeper = new MineSweeper() {
      public void exposeNeighbors(int row, int column) {
        exposeNeighborsCalled[0] = true;
      }
    };

    mineSweeper.expose(5, 6);
    exposeNeighborsCalled[0] = false;
    mineSweeper.expose(5, 6);

    assertFalse(exposeNeighborsCalled[0]);
  }

  @Test
  public void exposeCallsExposeNeighborOnSealedCell() {
    boolean[] exposeNeighborsCalled = new boolean[1];

    MineSweeper mineSweeper = new MineSweeper() {
      public void exposeNeighbors(int row, int column) {
        exposeNeighborsCalled[0] = true;
      }
    };

    mineSweeper.toggleSeal(5, 6);
    mineSweeper.expose(5, 6);

    assertFalse(exposeNeighborsCalled[0]);
  }

  @Test
  public void exposeNeighborCallsExposeOnEightNeighbors() {
    int row = 5, col = 5;
    int[] count = new int[1];
    int neighbors = 8;
    MineSweeper mineSweeper = new MineSweeper() {
      public void expose(int row, int column) {
        count[0]++;
      }
    };

    mineSweeper.exposeNeighbors(row, col);

    assertEquals(neighbors, count[0]);
  }

  @Test
  public void exposeNeighborWithinBoundsWhenTopLeftCellSelected() {
    int row = 9, col = 0;
    int neighbors = 3;
    int[] count = new int[1];
    MineSweeper mineSweeper = new MineSweeper() {
      public void expose(int row, int column) {
        {
          count[0]++;
        }
      }
    };

    mineSweeper.exposeNeighbors(row, col);

    assertEquals(neighbors, count[0]);
  }

  @Test
  public void exposeNeighborWithinBoundsWhenBottomRightCellSelected() {
    int row = 0, col = 9;
    int neighbors = 3;
    int[] count = new int[1];
    MineSweeper mineSweeper = new MineSweeper() {
      public void expose(int row, int column) {
        {
          count[0]++;
        }
      }
    };

    mineSweeper.exposeNeighbors(row, col);

    assertEquals(neighbors, count[0]);
  }

  @Test
  public void isMinePresentAtNonMineCell() {
    assertFalse(mineSweeper.isMineAt(3, 2));
  }

  @Test
  public void isMinePresentAtMineCell() {
    MineSweeper mineSweeper = new MineSweeper();

    mineSweeper.setMine(3, 2);

    assertTrue(mineSweeper.isMineAt(3, 2));
  }

  @Test
  public void isMinePresentOutOfRange() {
    assertAll(() -> assertFalse(mineSweeper.isMineAt(-1, 4)),
        () -> assertFalse(mineSweeper.isMineAt(10, 5)),
        () -> assertFalse(mineSweeper.isMineAt(5, -1)),
        () -> assertFalse(mineSweeper.isMineAt(7, 10)));
  }

  @Test
  public void adjacentCellDoesNotCallExposeNeighbors() {
    int neighbors = 0;
    int[] count = new int[1];
    MineSweeper mineSweeper = new MineSweeper() {
      public void exposeNeighbors(int row, int column) {
        {
          count[0]++;
        }
      }
    };

    mineSweeper.setMine(3,4);
    exposeAdjacentCellToMine(mineSweeper,3,5);

    assertEquals(neighbors, count[0]);
  }

  private void exposeAdjacentCellToMine(MineSweeper mineSweeper, int row, int column) {
    mineSweeper.expose(row, column);
  }

  @Test
  public void mineCellDoesNotCallExposeNeighbors() {
    int neighbors = 0;
    int[] count = new int[1];

    MineSweeper mineSweeper = new MineSweeper() {
      public void exposeNeighbors(int row, int column) {
        {
          count[0]++;
        }
      }

    };
    mineSweeper.setMine(3, 5);
    mineSweeper.expose(3, 5);

    assertEquals(neighbors, count[0]);
  }

  @Test
  public void verifyAdjacentMinesCountAt() {
    assertEquals(0, mineSweeper.adjacentMinesCountAt(4, 6));
  }

  @Test
  public void verifyAdjacentMinesCountOnMineCell() {
    MineSweeper mineSweeper = new MineSweeper();
    mineSweeper.setMine(3, 4);

    assertEquals(0, mineSweeper.adjacentMinesCountAt(3, 4));
  }

  @Test
  public void verifyAdjacentMinesCountWhenOneMinePresentAtNeighbor() {
    MineSweeper mineSweeper = new MineSweeper();
    mineSweeper.setMine(3, 4);

    assertEquals(1, mineSweeper.adjacentMinesCountAt(3, 5));
  }

  @Test
  public void verifyAdjacentMinesCountWhenTwoMinePresentAtNeighbor() {
    MineSweeper mineSweeper = new MineSweeper();
    mineSweeper.setMine(3, 4);
    mineSweeper.setMine(2, 6);

    assertEquals(2, mineSweeper.adjacentMinesCountAt(3, 5));
  }

  @Test
  public void verifyAdjacentMinesCountWhenMinePresentAtBoundary() {
    MineSweeper mineSweeper = new MineSweeper();
    mineSweeper.setMine(0, 1);
    mineSweeper.setMine(9, 8);

    assertAll(() -> assertEquals(1, mineSweeper.adjacentMinesCountAt(0, 0)),
        () -> assertEquals(1, mineSweeper.adjacentMinesCountAt(9, 9)));
  }

  @Test
  public void verifyAdjacentMinesCountWhenNoMinePresentAtBoundary() {
    assertAll(() -> assertEquals(0, mineSweeper.adjacentMinesCountAt(0, 9)),
        () -> assertEquals(0, mineSweeper.adjacentMinesCountAt(9, 0)));
  }

  @Test
  public void getGameStatus() {
    assertEquals(MineSweeper.GameStatus.INPROGRESS, mineSweeper.getGameStatus());
  }

  @Test
  public void getGameStatusReturnsLostWhenMineIsExposed() {
    MineSweeper mineSweeper = new MineSweeper();
    mineSweeper.setMine(3, 4);
    mineSweeper.expose(3, 4);

    assertEquals(MineSweeper.GameStatus.LOST, mineSweeper.getGameStatus());
  }

  @Test
  public void gameInProgressAfterAllMinesAreSealedButCellsRemainUnexposed() {
    MineSweeper mineSweeper = new MineSweeper();
    for (int i = 0; i < 5; i++) {
      mineSweeper.setMine(i, 4);
      mineSweeper.toggleSeal(i, 4);
    }

    assertEquals(MineSweeper.GameStatus.INPROGRESS, mineSweeper.getGameStatus());
  }

  @Test
  public void gameInProgressAfterAllMinesAreSealedButAnEmptyCellIsSealed() {
    MineSweeper mineSweeper = new MineSweeper();
    for (int i = 0; i < MAX_BOUND; i++) {
      mineSweeper.setMine(i, 4);
      mineSweeper.toggleSeal(i, 4);
    }
    sealEmptyCell(mineSweeper,6, 6);

    assertEquals(MineSweeper.GameStatus.INPROGRESS, mineSweeper.getGameStatus());
  }

  private void sealEmptyCell(MineSweeper mineSweeper, int row, int column) {
    mineSweeper.toggleSeal(row, column);
  }


  @Test
  public void gameInProgressAfterAllMinesAreSealedButAnAdjacentCellIsUnexposed() {
    MineSweeper mineSweeper = new MineSweeper();

    for (int i = 0; i < MAX_BOUND; i++) {
      mineSweeper.setMine(i, 4);
      mineSweeper.toggleSeal(i, 4);
    }
    exposeAdjacentCell(mineSweeper,6, 5);

    assertEquals(MineSweeper.GameStatus.INPROGRESS, mineSweeper.getGameStatus());
  }

  private void exposeAdjacentCell(MineSweeper mineSweeper, int row, int col) {
    mineSweeper.expose(row, col);
  }

  @Test
  public void gameWonAfterAllMinesAreSealedAndAllOtherCellsExposed() {
    MineSweeper mineSweeper = new MineSweeper();
    for (int i = 0; i < MAX_BOUND; i++) {
      mineSweeper.setMine(i, 4);
    }

    boolean[][] mines = mineSweeper.getMines();
    for (int i = 0; i < MAX_BOUND; i++)
      for (int j = 0; j < MAX_BOUND; j++) {
        if (mines[i][j]) {
          mineSweeper.toggleSeal(i, j);
        } else
          mineSweeper.expose(i, j);
      }

    assertEquals(MineSweeper.GameStatus.WON, mineSweeper.getGameStatus());
  }


  @Test
  public void gameWonAfterAllMinesAreUnexposedAndAllOtherCellsExposed() {
    MineSweeper mineSweeper = new MineSweeper();
    for (int i = 0; i < MAX_BOUND; i++) {
      mineSweeper.setMine(i, 4);
    }

    boolean[][] mines = mineSweeper.getMines();
    for (int i = 0; i < MAX_BOUND; i++) {
      for (int j = 0; j < MAX_BOUND; j++) {
        if (!mines[i][j]) {
          mineSweeper.expose(i, j);
        }
      }
    }

    assertEquals(MineSweeper.GameStatus.WON, mineSweeper.getGameStatus());
  }

  @Test
  public void setMinesRandomly() {
    int mineCount = 0;
    mineSweeper.setMines(0);
    boolean[][] totalMines = mineSweeper.getMines();

    for (boolean[] mineArray : totalMines) {
      for (boolean mine : mineArray) {
        if (mine) {
          mineCount++;
        }
      }
    }

    assertEquals(10, mineCount);
  }

  @Test
  public void differentMinesWithDifferentSeed() {
    int differentMineCount = 0;
    MineSweeper firstBoard= new MineSweeper();
    firstBoard.setMines(0);

    MineSweeper secondBoard= new MineSweeper();
    secondBoard.setMines(1);

    boolean[][] minesWithSeedZero = firstBoard.getMines();
    boolean[][] minesWithSeedOne = secondBoard.getMines();

    for (int i = 0; i < minesWithSeedZero.length; i++) {
      for (int j = 0; j < minesWithSeedZero.length; j++) {
        if (minesWithSeedZero[i][j] != minesWithSeedOne[i][j]) {
          differentMineCount += 1;
        }
      }
    }

    assertTrue(differentMineCount >= 1);
  }

  @Test
  public void allUnexposedCellInitially(){
    MineSweeper.CellState[][] cellStates = mineSweeper.getCellStates();
    int unexposed = 0;
    int size = cellStates.length;

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (cellStates[i][j] == UNEXPOSED) {
          unexposed += 1;
        }
      }
    }
    assertEquals(size * size,unexposed);
  }


}