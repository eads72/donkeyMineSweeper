package donkeyMineSweeper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class MineApplication extends Application {

	private final int height = 10;
	private final int width = 10;
	private Tile[][] grid = new Tile[width][height];

	// private class constant and some variables
	private static final Integer STARTTIME = 30;
	private Timeline timeline = new Timeline();
	private Label timerLabel = new Label();
	// Make timeSeconds a Property
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

	public static void main(String[] args) {
		// We launch the application
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {

		// Creating the image views
		String path = "resources/pick.png";
		Image cursorimage = new Image(new FileInputStream(path));

		String pathTurned = "resources/pickTurned.png"; // if in windows fix path
		Image cursorTurned = new Image(new FileInputStream(pathTurned));

		String pathMan = "resources/man.png";
		Image manImage = new Image(new FileInputStream(pathMan));
		ImageView manView = new ImageView(manImage);

		String pathSmile = "resources/manSmile.png";
		Image smileImage = new Image(new FileInputStream(pathSmile));
		ImageView smileView = new ImageView(smileImage);
		smileView.setFitHeight(70);
		smileView.setPreserveRatio(true);

		String pathSad = "resources/manSad.png";
		Image sadImage = new Image(new FileInputStream(pathSad));
		ImageView sadView = new ImageView(sadImage);
		sadView.setFitHeight(70);
		sadView.setPreserveRatio(true);

		String pathDonkey = "resources/donkey.png";
		Image donkeyImage = new Image(new FileInputStream(pathDonkey));
		ImageView donkeyView = new ImageView(donkeyImage);
		donkeyView.setFitHeight(30);
		donkeyView.setPreserveRatio(true);

		timerLabel.textProperty().bind(timeSeconds.asString());

		// the donkey button to reveal the bombs if needed
		Button donkeyButton = new Button();
		donkeyButton.setMinSize(50, 50);
		donkeyButton.setGraphic(getDonkeyView());
		donkeyButton.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = donkeyButton.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putImage(donkeyView.getImage());
				db.setContent(content);
				donkeyView.setFitHeight(30);
				donkeyView.setPreserveRatio(true);
				event.consume();
			}
		});

		donkeyButton.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* the drag and drop gesture ended */
				if (event.getTransferMode() == TransferMode.MOVE) {
				}
			}
		});

		// Layout for the game screen
		VBox paneTopLeft = new VBox();
		Text timerText = new Text("Time Left");
		paneTopLeft.setMinSize(100, 60);
		paneTopLeft.setPadding(new Insets(10, 10, 20, 20));
		paneTopLeft.getChildren().addAll(timerLabel, timerText);

		StackPane paneTopMiddle = new StackPane();
		paneTopMiddle.setMinSize(200, 60);
		paneTopMiddle.getChildren().add(smileView);

		VBox paneTopRight = new VBox();
		Text donkeyText = new Text("Let the donkey help you," + "\r\n" + "just drag over the tiles");
		paneTopRight.setMinSize(200, 60);
		paneTopRight.setPadding(new Insets(10, 10, 20, 20));
		paneTopRight.getChildren().addAll(donkeyButton, donkeyText);

		StackPane paneBottom = new StackPane();
		paneBottom.setMinSize(200, 60);
		paneBottom.setStyle("-fx-background-color: #ffe519;");
		Label gameOver = new Label("GAME OVER");

		HBox paneTop = new HBox();
		paneTop.setPadding(new Insets(10, 10, 10, 150));
		paneTop.setStyle("-fx-background-color: #ffe599;");
		paneTop.getChildren().addAll(paneTopLeft, paneTopMiddle, paneTopRight);

		GridPane paneMines = new GridPane();
		paneMines.setStyle("-fx-background-color: #ffe519;");
		paneMines.setAlignment(Pos.CENTER);
		paneMines.setVgap(2);
		paneMines.setHgap(2);
		paneMines.setCursor(new ImageCursor(cursorimage));

		VBox root = new VBox();
		root.getChildren().addAll(paneTop, paneMines, paneBottom);

		// Layout for start screen
		// Creating the Combo Box to choose difficulty.
		// What I actually wanted to do was to modify the random variable
		// 	based on the chosen difficulty, but I got many errors and gave up
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.getItems().addAll("Easy", "Medium", "Difficult");
		comboBox.setPromptText("Choose Difficulty");
		comboBox.setStyle("-fx-font: 20px \"Serif\";");

		Pane manPane = new Pane();
		manPane.getChildren().add(manView);

		Button startbutton = new Button("start");
		startbutton.setAlignment(Pos.CENTER);
		Label buttonLabel = new Label("Mine Sweeper Game");

		VBox buttonPane = new VBox();
		buttonPane.setSpacing(50);
		buttonPane.setPadding(new Insets(70, 20, 10, 10));
		buttonPane.getChildren().addAll(buttonLabel, comboBox, startbutton);
		HBox rootStartScreen = new HBox();
		rootStartScreen.setMinSize(500, 500);

		rootStartScreen.getChildren().addAll(manPane, buttonPane);

		Scene scene2 = new Scene(root, 800, 700);
		Scene scene1 = new Scene(rootStartScreen);

		startbutton.setOnAction(e -> {
			primaryStage.setScene(scene2);
			if (timeline != null) {
				timeline.stop();
			}
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timeSeconds, 0)));

			timeline.setOnFinished(e2 -> {
				timerLabel.setTextFill(Color.RED);
				paneTopMiddle.getChildren().add(sadView);

			});

			timeline.playFromStart();
		});

		scene2.getStylesheets().add(MineApplication.class.getResource("mineSweeper.css").toExternalForm());
		scene1.getStylesheets().add(MineApplication.class.getResource("mineSweeper.css").toExternalForm());

		scene2.setCursor(Cursor.HAND);
		primaryStage.setScene(scene1);
		primaryStage.show();
		primaryStage.setTitle("Mine Sweeper");

		// creating the tiles
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// String difficulty = new String(comboBox.getValue());
				double random = 0.15;
				// if(difficulty.equals("Easy")) {random = 0.1;};
				// if(difficulty.equals("Medium")) {random = 0.2;};
				// if(difficulty.equals("Hard")) {random = 0.3;};
				boolean bombbool = (Math.random() < random);

				Ground ground = new Ground(x, y, bombbool);
				Tile tile = new Tile(x, y, bombbool, false);
				ground.getChildren().add(tile);
				tile.setMouseHover();
				tile.ground = ground;

				// pick digging effect and revealing the tiles when clicked
				tile.setOnMousePressed(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent e) {
						tile.setCursor(new ImageCursor(cursorTurned));

						if (tile.hasBomb) {
							paneMines.setStyle("-fx-background-color: #e84d4d;");
							paneBottom.getChildren().add(gameOver);
							paneBottom.setStyle("-fx-background-color: #e84d4d;");
							paneTopMiddle.getChildren().add(sadView);
							timeline.stop();
							for (int y = 0; y < height; y++) {
								for (int x = 0; x < width; x++) {

									if (grid[x][y].hasBomb) {
										try {
												grid[x][y].setGraphic(getBombView());
											} catch (FileNotFoundException e1) {
												e1.printStackTrace();
											}
										
										}
									 else {
										grid[x][y].open();
									}
								}
							}

						}

						else {
							tile.open();
						}
						System.out.print(ground.text.getText());
					}
				});
				tile.setOnMouseReleased(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						tile.setCursor(new ImageCursor(cursorimage));
					}
				});

				// Drag the donkey to reveal the bombs
				tile.setOnDragEntered(new EventHandler<DragEvent>() {
					public void handle(DragEvent event) {
						if (event.getGestureSource() != tile && event.getDragboard().hasImage()) {
							tile.setStyle("-fx-background-color: MediumSeaGreen");
						}
					}

				});
				tile.setOnDragExited(new EventHandler<DragEvent>() {
					public void handle(DragEvent event) {
						if (tile.hasBomb) {
							try {
								tile.setGraphic(getBombView());
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
						} else {
							tile.setVisible(false);
						}
						;

					}

				});

				paneMines.add(ground, x, y);
				grid[x][y] = tile;
			}

		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int sum = sumBombs(grid[x][y]);
				grid[x][y].setSumB(sum);

				if (sum > 0) {
					grid[x][y].ground.text.setText(Integer.toString(sum));
				}

			}
		}

	}

	private ImageView getBombView() throws FileNotFoundException {
		String path2 = "resources/bomb.png";
		Image bombimage = new Image(new FileInputStream(path2));
		ImageView bombview = new ImageView(bombimage);
		bombview.setFitHeight(30);
		bombview.setPreserveRatio(true);
		return bombview;
	}

	private ImageView getDonkeyView() throws FileNotFoundException {
		String path3 = "resources/donkey.png";
		Image donkeyimage = new Image(new FileInputStream(path3));
		ImageView donkeyview = new ImageView(donkeyimage);
		donkeyview.setFitHeight(30);
		donkeyview.setPreserveRatio(true);
		return donkeyview;
	}

	private List<Tile> getNeighbours(Tile tile) {
		// create a List for the neighbours
		List<Tile> neighbours = new ArrayList<>();

		int x = tile.getx();
		int y = tile.gety();

		// there are 8 possible neighbours for a tile however we check first if these
		// neighbours exist or not
		if (x > 0 && y > 0)
			neighbours.add(grid[x - 1][y - 1]);
		if (x > 0)
			neighbours.add(grid[x - 1][y]);
		if (x > 0 && y < height - 1)
			neighbours.add(grid[x - 1][y + 1]);
		if (y > 0)
			neighbours.add(grid[x][y - 1]);
		if (y < height - 1)
			neighbours.add(grid[x][y + 1]);
		if (x < width - 1 && y > 0)
			neighbours.add(grid[x + 1][y - 1]);
		if (x < width - 1)
			neighbours.add(grid[x + 1][y]);
		if (x < width - 1 && y < height - 1)
			neighbours.add(grid[x + 1][y + 1]);

		return neighbours;
	}

	public int sumBombs(Tile tile) {
		int sum = 0;

		List<Tile> neighbours = this.getNeighbours(tile);

		for (Tile t : neighbours) {
			if (t.hasBomb) {
				sum++;
			}
		}

		return sum;
	}

	public class Tile extends Button {
		public int x, y;
		public boolean hasBomb, isOpen;
		public int sumB;
		public Ground ground;

		public Tile(int x, int y, boolean hasBomb, boolean isOpen) {
			this.x = x;
			this.y = y;
			this.hasBomb = hasBomb;
			this.isOpen = false;

			{
				this.setMinHeight(50);
				this.setMinWidth(50);

			}

		}

		boolean hasBomb() {
			return hasBomb;
		}

		public int getx() {
			return x;
		}

		public int gety() {
			return y;
		}

		public void setMouseHover(Tile this) {
			this.setOnMouseEntered(e -> {
				if (this.isOpen == false) {
				}
			});
			this.setOnMouseExited(e -> {
				if (this.isOpen == false) {
				}
			});

		}

		public void setLabel(String text) {
			this.setText(text);
		}

		public void setSumB(int sumB) {
			this.sumB = sumB;
		}

		public int getSumB() {
			return sumB;
		}
		
		//animation to reveal the tiles
		public void scaleDown() {
			FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), this);
			fadeTransition.setFromValue(1.0f);
			fadeTransition.setToValue(0f);
			fadeTransition.setCycleCount(1);
			fadeTransition.setAutoReverse(false);
			ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), this);
			scaleTransition.setToX(0.1f);
			scaleTransition.setToY(0.1f);
			scaleTransition.setCycleCount(1);
			scaleTransition.setAutoReverse(false);

			ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
			parallelTransition.play();
		}

		public void open() {
			if (isOpen)
				return;

			if (hasBomb) {
				System.out.println("Game Over");
				this.setVisible(false);
				return;
			}

			isOpen = true;
			this.scaleDown();

			if (this.getSumB() == 0) {
				//this makes the empty neighbours open as well
				getNeighbours(this).forEach(Tile::open);
			}
		}

	}
	//The class for ground which we place tiles on them
	public class Ground extends StackPane {
		public int x, y;
		public boolean hasBomb;
		public String sumB = new String("");

		public Text text = new Text();

		public Ground(int x, int y, boolean hasBomb) {
			this.x = x;
			this.y = y;
			this.getChildren().add(text);
		}

		boolean hasBomb() {
			return hasBomb;
		}

		{
			this.setMinHeight(40);
			this.setMinWidth(40);
			this.getStyleClass().add("ground");
		}
	}

}
