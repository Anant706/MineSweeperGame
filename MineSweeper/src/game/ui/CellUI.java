package game.ui;

import game.MineSweeper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import static game.Constants.TILE_SIZE;

public class CellUI extends Pane {
	private MineSweeper.CellState cellState;
	private boolean isMine;
	private static final String MINE_PNG_PATH = "/resources/mine.png";
	private static final String FLAG_PNG_PATH = "/resources/flag.png";
	private static final String UNEXPOSED_CELL_STYLE = "-fx-border-color: black; -fx-background-color:  #6B8E23";
	private static final String EMPTY_CELL_STYLE = "-fx-background-color: #D2691E;-fx-border-color: black;";
	
	
	public CellUI(boolean isMine, MineSweeper.CellState cellState) {
		this.cellState = cellState;
		this.isMine = isMine;
		drawUnexposedCell();
	}
	
	public void drawUnexposedCell() {
		getChildren().clear();
		setStyle(UNEXPOSED_CELL_STYLE);
		setPrefSize(TILE_SIZE, TILE_SIZE);
	}
	
	public void drawMineCell() {
		double w = getWidth(), h = getHeight();
		Image mineImage = new Image(MINE_PNG_PATH);
		ImageView imageView = new ImageView();
		imageView.setFitWidth(w);
		imageView.setFitHeight(h);
		imageView.setImage(mineImage);
		getChildren().addAll(imageView);
	}
	
	public void drawEmptyCell() {
		setStyle(EMPTY_CELL_STYLE);
	}
	
	public void drawAdjacentCell(int mineCount) {
		double w = getWidth(), h = getHeight();
		Text text = new Text();
		text.setText(String.valueOf(mineCount));
		text.setX(h / 2);
		text.setY(w / 2);
		getChildren().add(text);
	}
	
	public void drawSealedCell() {
		double w = getWidth(), h = getHeight();
		ImageView imageView = new ImageView();
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(w);
		imageView.setFitHeight(h);
		
		Image mineImage = new Image(FLAG_PNG_PATH);
		imageView.setImage(mineImage);
		getChildren().addAll(imageView);
	}
	
	public boolean isMine() {
		return isMine;
	}
	
	public MineSweeper.CellState getCellState() {
		return cellState;
	}
	
	public void setCellState(MineSweeper.CellState cellState) {
		this.cellState = cellState;
	}
	
	public void drawCross() {
		double w = getWidth(), h = getHeight();
		Line line1 = new Line(10.0f, 10.0f, w - 10.0f, h - 10.0f);
		line1.setStroke(Color.WHITE);
		Line line2 = new Line(10.0f, h - 10.0f, w - 10.0f, 10.0f);
		line2.setStroke(Color.WHITE);
		getChildren().addAll(line1, line2);
	}
}


