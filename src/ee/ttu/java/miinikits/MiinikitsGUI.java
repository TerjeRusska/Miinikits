package ee.ttu.java.miinikits;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by Terje on 06.04.2017.
 */
public class MiinikitsGUI extends Application {

    /**
     * Screen Width.
     */
    public static final int WIDTH = 1366;
    /**
     * Screen height.
     */
    public static final int HEIGTH = 768;
    /**
     * Default health of the goat.
     */
    public static final int HEALTH = 1400;
    /**
     * Width and Heigth of the bomb.
     */
    public static final int BOMBSIZE = 65;
    /**
     * Width and Heigth of the explosion.
     */
    public static final int EXPLSIZE = 130;
    /**
     * Object size 350.
     */
    public static final int SIZE350 = 350;
    /**
     * Constant 10.
     */
    public static final int TEN = 10;
    /**
     * 90 degrees to rotate.
     */
    public static final int ROTATE = 90;
    /**
     * 2/3 of the health bar.
     */
    public static final int HEALTHMED = 934;
    /**
     * 1/3 of the health bar.
     */
    public static final int HEALTHLOW = 467;
    /**
     * Object size 508.
     */
    public static final int SIZE508 = 508;
    /**
     * Object size 280.
     */
    public static final int SIZE280 = 280;
    /**
     * Bomb timeline rate increase.
     */
    public static final double RATE = 1.2;
    /**
     * Constant number 3.
     */
    public static final int THREE = 3;
    /**
     * Object size 32.
     */
    public static final int SIZE32 = 32;
    /**
     * Object size 96.
     */
    public static final int SIZE96 = 96;
    /**
     * Object size 200.
     */
    public static final int SIZE200 = 200;
    /**
     * Game score.
     */
    private int score = 0;
    /**
     * Game health bar.
     */
    private int health = HEALTH;
    /**
     * Game timeline for bombs.
     */
    private Timeline bombs;
    /**
     * Music player for background.
     */
    private MediaPlayer musicplayer;
    /**
     * Maximum points the goat can get when picking up the bomb.
     */
    private int bombsPicked = 0;

    /**
     * Miinikits main function, starts the game.
     *
     * @param args Javafx objects.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Miinikits Miina");

        Image background = image("Images/Background.png");
        ImagePattern pattern = new ImagePattern(background);

        AnchorPane gameField = new AnchorPane();
        gameField.setStyle("-fx-background-color: transparent");
        gameField.setPrefSize(WIDTH, HEIGTH);

        Scene scene = new Scene(gameField, WIDTH, HEIGTH, pattern);

        Media media = new Media(getClass().getClassLoader().getResource("Images/Music.mp3").toString());
        this.musicplayer = new MediaPlayer(media);
        musicplayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicplayer.play();

        Image goatImg = image("Images/Goat.gif");
        ImageView goat = new ImageView(goatImg);
        goat.setX(gameField.getWidth() / 2 - SIZE96);
        goat.setY(gameField.getHeight() / 2 - (SIZE32 * 2));

        Rectangle healthBar = new Rectangle();
        healthBar.setHeight(2 * TEN);
        healthBar.setWidth(this.health);
        healthBar.setFill(Color.LIMEGREEN);

        Label score = new Label("Score: " + this.score);
        score.setStyle("-fx-background-color: transparent;" + "-fx-font-size: 30 px;"
                + "-fx-font-family: Impact;" + "-fx-text-alignment: center;"
                + "-fx-text-fill: #77ff29;");
        score.setLayoutX(TEN);
        score.setLayoutY(2 * TEN);

        Button playAgain = new Button("Play again");
        playAgain.setStyle("-fx-background-color: rgba(46,42,41,0.9);" + "-fx-border-color: #ff2627;"
                + "-fx-border-width: 10;" + "-fx-font-size: 30 px;"
                + "-fx-font-family: Impact;" + "-fx-text-alignment: center;"
                + "-fx-text-fill: ghostwhite;");
        playAgain.setAlignment(Pos.CENTER);
        playAgain.setPrefWidth(SIZE350);
        playAgain.setLayoutX(SIZE508);
        playAgain.setLayoutY(SIZE508);

        Label gameOver = new Label("Game over!\nScore: ");
        gameOver.setStyle("-fx-background-color: rgba(46,42,41,0.9);" + "-fx-border-color: #ff2627;"
                + "-fx-border-width: 10;" + "-fx-font-size: 60 px;"
                + "-fx-font-family: Impact;" + "-fx-text-alignment: center;"
                + "-fx-text-fill: ghostwhite;");
        gameOver.setAlignment(Pos.CENTER);
        gameOver.setPrefSize(SIZE350, SIZE200);
        gameOver.setLayoutX(SIZE508);
        gameOver.setLayoutY(SIZE280);

        playAgain.setOnMouseClicked(event -> {
            this.score = 0;
            this.bombsPicked = 0;
            this.health = HEALTH;
            score.setText("Score: " + this.score);
            healthBar.setFill(Color.LIMEGREEN);
            gameField.getChildren().remove(gameOver);
            gameField.getChildren().remove(playAgain);
            this.bombs = bombs(gameField, goat);
            bombs.play();
        });
        playAgain.setOnMouseEntered(event -> {
            playAgain.setStyle("-fx-background-color: rgba(46,42,41,1);" + "-fx-border-color: #ff2627;"
                    + "-fx-border-width: 10;" + "-fx-font-size: 30 px;"
                    + "-fx-font-family: Impact;" + "-fx-text-alignment: center;"
                    + "-fx-text-fill: ghostwhite;");
        });
        playAgain.setOnMouseExited(event -> {
            playAgain.setStyle("-fx-background-color: rgba(46,42,41,0.9);" + "-fx-border-color: #ff2627;"
                    + "-fx-border-width: 10;" + "-fx-font-size: 30 px;"
                    + "-fx-font-family: Impact;" + "-fx-text-alignment: center;"
                    + "-fx-text-fill: ghostwhite;");
        });

        Timeline up = movement(goat, 0, -1);
        Timeline down = movement(goat, 0, 1);
        Timeline right = movement(goat, 1, 0);
        Timeline left = movement(goat, -1, 0);

        goatMovement(scene, right, left, up, down, goat);
        this.bombs = bombs(gameField, goat);
        this.bombs.play();

        Timeline scoreCheck = scoreCheck(score, healthBar, gameField, gameOver, playAgain);
        scoreCheck.play();

        gameField.getChildren().addAll(goat, score, healthBar);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Creates timelines for the movement of the goat.
     *
     * @param scene Game window where the movement takes place.
     * @param right Timeline moving the goat to the right.
     * @param left  Timeline moving the goat to the left.
     * @param up    Timeline moving the goat to up.
     * @param down  Timeline moving the goat down.
     * @param goat  Image of the goat.
     */
    private void goatMovement(Scene scene, Timeline right, Timeline left, Timeline up, Timeline down, ImageView goat) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                goat.setScaleX(1);
                goat.setScaleY(1);
                goat.setRotate(0);
                right.play();
                left.stop();
                up.stop();
                down.stop();
            } else if (event.getCode() == KeyCode.LEFT) {
                goat.setScaleX(-1);
                goat.setScaleY(1);
                goat.setRotate(0);
                left.play();
                right.stop();
                up.stop();
                down.stop();
            } else if (event.getCode() == KeyCode.UP) {
                goat.setScaleY(1);
                goat.setScaleX(1);
                goat.setRotate(-ROTATE);
                up.play();
                right.stop();
                left.stop();
                down.stop();
            } else if (event.getCode() == KeyCode.DOWN) {
                goat.setScaleY(-1);
                goat.setScaleX(1);
                goat.setRotate(ROTATE);
                down.play();
                right.stop();
                left.stop();
                up.stop();
            }
            event.consume();
        });
        scene.setOnKeyReleased(event -> {
            right.stop();
            left.stop();
            up.stop();
            down.stop();
        });
    }

    /**
     * Creates an image object.
     *
     * @param pathname location of the image.
     * @return image object
     */
    private Image image(String pathname) {
        javafx.scene.image.Image image = new Image(getClass().getClassLoader().getResource(pathname).toString());
        return image;
    }

    /**
     * Checks the score and health bar.
     *
     * @param score     label that shows the current score.
     * @param healthBar current healthbar lenght.
     * @param gamefield contains all game objects
     * @param gameOver  game over notification.
     * @param playAgain button to restart the game.
     * @return score check timeline.
     */
    private Timeline scoreCheck(Label score, Rectangle healthBar,
                                AnchorPane gamefield, Label gameOver, Button playAgain) {
        Timeline scoreCheck = new Timeline(new KeyFrame(
                Duration.millis(TEN),
                event -> {
                    score.setText("Score: " + this.score);
                    healthBar.setWidth(this.health);
                    if (this.health < HEALTHMED) {
                        healthBar.setFill(Color.YELLOW);
                    }
                    if (this.health < HEALTHLOW) {
                        healthBar.setFill(Color.RED);
                    }
                    if (this.health <= 0) {
                        if (!gamefield.getChildren().contains(gameOver)) {
                            this.bombs.stop();
                            gameOver.setText("Game over!\nScore: " + this.score);
                            gamefield.getChildren().add(gameOver);
                            gamefield.getChildren().addAll(playAgain);
                        }
                    }
                }));
        scoreCheck.setCycleCount(Timeline.INDEFINITE);
        return scoreCheck;
    }

    /**
     * Creates a timeline for the bomb creation and explosion.
     *
     * @param gamefield anchorpane containing game objects.
     * @param goat      goat object.
     * @return bomb timeline
     */
    private Timeline bombs(AnchorPane gamefield, ImageView goat) {
        Timeline bombs = new Timeline(new KeyFrame(
                Duration.seconds(TEN / 2),
                event -> {
                    Image bombImg = image("Images/Bomb.png");
                    ImageView bomb = new ImageView(bombImg);
                    bomb.setFitWidth(BOMBSIZE);
                    bomb.setFitHeight(BOMBSIZE);
                    Random random = new Random();
                    int randX = random.nextInt(WIDTH - BOMBSIZE);
                    int randY = random.nextInt(HEIGTH - BOMBSIZE);
                    bomb.setX(randX);
                    bomb.setY(randY);
                    gamefield.getChildren().add(bomb);
                    bomb.toBack();
                    Timeline explosion = explosion(gamefield, bomb, goat);
                    explosion.play();
                    Timeline pickBomb = pickBomb(goat, bomb, explosion, gamefield);
                    pickBomb.play();
                }));
        bombs.setCycleCount(Timeline.INDEFINITE);
        return bombs;
    }

    /**
     * Creates timeline for bomb explosion.
     *
     * @param gamefield anchorpane containing game objects.
     * @param bomb      bomb element
     * @param goat      goat element
     * @return explosion timeline.
     */
    private Timeline explosion(AnchorPane gamefield, ImageView bomb, ImageView goat) {
        Timeline explosion = new Timeline(new KeyFrame(
                Duration.seconds(THREE),
                event -> {
                    Image explImg = image("Images/Explosion.png");
                    ImageView expl = new ImageView(explImg);
                    expl.setFitWidth(EXPLSIZE);
                    expl.setFitHeight(EXPLSIZE);
                    expl.setX(bomb.getX() - SIZE32);
                    expl.setY(bomb.getY() - SIZE32);
                    gamefield.getChildren().remove(bomb);
                    gamefield.getChildren().add(expl);
                    expl.toFront();
                    if (this.health > 0) {
                        this.score -= THREE;
                    }
                    Timeline exploded = exploded(goat, expl, gamefield);
                    exploded.play();
                    Timeline fade = new Timeline(new KeyFrame(
                            Duration.seconds(1),
                            ev -> gamefield.getChildren().remove(expl)));
                    fade.play();
                }));
        return explosion;
    }

    /**
     * Creates timeline for the movement of the goat.
     *
     * @param goat goat objects
     * @param x    movement to the left/right
     * @param y    movement up/down
     * @return movement timeline
     */
    private Timeline movement(ImageView goat, double x, double y) {
        Timeline movement = new Timeline(new KeyFrame(
                Duration.millis(THREE),
                event -> {
                    goat.setX(goat.getX() + x);
                    goat.setY(goat.getY() + y);
                }));
        movement.setCycleCount(Timeline.INDEFINITE);
        return movement;
    }

    /**
     * Detects collision between goat and bomb.
     *
     * @param goat      goat object
     * @param bomb      bomb object
     * @param explosion explosion object
     * @param gamefield anchorpane containing all game objects
     * @return collision detection timeline
     */
    private Timeline pickBomb(ImageView goat, ImageView bomb, Timeline explosion, AnchorPane gamefield) {
        Timeline pickBomb = new Timeline(new KeyFrame(
                Duration.millis(1),
                event -> {
                    Bounds bombObj = bomb.localToScene(bomb.getBoundsInLocal());
                    Bounds goatObj = goat.localToScene(goat.getBoundsInLocal());
                    if (goatObj.intersects(bombObj) && gamefield.getChildren().contains(bomb)
                            && this.health > 0) {
                        double newScore = explosion.getCurrentTime().toSeconds();
                        newScore = Math.round(newScore);
                        this.score += (TEN / 2 + 1) - newScore;
                        this.bombsPicked += 1;
                        if (this.bombsPicked % (TEN / 2) == 0 && this.bombsPicked != 0) {
                            this.bombs.setRate(this.bombs.getRate() * RATE);
                        }
                        explosion.stop();
                        gamefield.getChildren().remove(bomb);
                    }
                }));
        pickBomb.setCycleCount(Timeline.INDEFINITE);
        return pickBomb;
    }

    /**
     * Detects collision between goat and explosion.
     *
     * @param goat      goat object
     * @param expl      explosion object
     * @param gamefield anchorpane containing all game objects.
     * @return collision detection timeline.
     */
    private Timeline exploded(ImageView goat, ImageView expl, AnchorPane gamefield) {
        Timeline exploded = new Timeline(new KeyFrame(
                Duration.millis(1),
                event -> {
                    Bounds bombObj = expl.localToScene(expl.getBoundsInLocal());
                    Bounds goatObj = goat.localToScene(goat.getBoundsInLocal());
                    if (goatObj.intersects(bombObj) && gamefield.getChildren().contains(expl)) {
                        this.health -= 1;
                    }
                }));
        exploded.setCycleCount(Timeline.INDEFINITE);
        return exploded;
    }
}
