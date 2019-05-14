package game.ui;

import game.MineSweeper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

import static game.Constants.*;

public class MineSweeperUI extends Application {

  private GridPane gridPane = new GridPane();

  @Override
  public void start(Stage primaryStage) {
    initializeBoard(new Board());

    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(gridPane);

    Scene scene = new Scene(borderPane, BOARD_WIDTH, BOARD_HEIGHT);
    primaryStage.setTitle("MineSweeper");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void initializeBoard(Board board) {
    CellUI[][] cells = board.create();
    for (int i = 0; i < MAX_BOUNDS; i++) {
      for (int j = 0; j < MAX_BOUNDS; j++) {
        cells[i][j].setOnMouseClicked(event -> handleMouseClick(event, board));
        gridPane.add(cells[i][j], j, i);
      }
    }
  }


  private void handleMouseClick(MouseEvent event, Board board) {
    Node source = (Node) event.getSource();
    Integer colIndex = GridPane.getColumnIndex(source);
    Integer rowIndex = GridPane.getRowIndex(source);
    MouseButton button = event.getButton();

    if ((button == MouseButton.PRIMARY)) {
      board.exposeCell(rowIndex, colIndex);
    } else if (button == MouseButton.SECONDARY) {
      board.toggleSealCell(rowIndex, colIndex);
    }

    showPlayerStatus(board);
  }


  private void showDialog(String statusLabel) {
    ButtonType restartButton = new ButtonType("Close");
    Alert alert = new Alert(Alert.AlertType.INFORMATION, " ", restartButton);
    alert.setHeaderText("");
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.setTitle("Game Status");
    alert.setContentText(statusLabel);
    alert.initStyle(StageStyle.UNDECORATED);

    Optional<ButtonType> result = alert.showAndWait();
    if ((result.isPresent()) && (result.get() == restartButton)) {
      Platform.exit();
    }
  }

  private void showPlayerStatus(Board board) {
    String statusLabel;
    MineSweeper.GameStatus gameStatus = board.getGameStatus();
    if (gameStatus == MineSweeper.GameStatus.LOST) {
      board.exposeAllMines();
      statusLabel = "YOU LOST";
      showDialog(statusLabel);
    } else if (gameStatus == MineSweeper.GameStatus.WON) {
      statusLabel = "YOU WON";
      showDialog(statusLabel);
    } else {
    }

  }

  public static void main(String[] args) {
    launch(args);
  }

}
