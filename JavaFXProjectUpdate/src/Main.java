import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * @author 16842
 * @version 1.0.0
 * @description Game Main
 * @date 2023/1/14 0:30
 */
public class Main extends Application {
    /**
     * game map
     */
    private static int[][] map;
    private static int[][] map_root_2;
    private static int[][] map_root_3;
    private static int[][] map_root_4;
    /**
     * game reference map
     */
    private static int[][] map2;
    private static int[][] map2_root_2;
    private static int[][] map2_root_3;
    private static int[][] map2_root_4;
    /**
     * init player direction before game start
     */
    private static String direction = "bottom";//init direction
    /**
     * current player position
     */
    private static int x;
    private static int xLevel2;
    private static int xLevel3;
    private static int xLevel4;
    private static int y;
    private static int yLevel2;
    private static int yLevel3;
    private static int yLevel4;
    /**
     * background width
     */
    private static final int BACKGROUND_WIDTH = 700;
    /**
     * background height
     */
    private static final int BACKGROUND_HEIGHT = 340;
    /**
     * canvas width
     */
    private static final int CANVAS_WIDTH = 500;
    /**
     * canvas height
     */
    private static final int CANVAS_HEIGHT = 350;
    /**
     * text tip position x
     */
    private static final int TEXT_TIP_X = 530;
    /**
     * text tip position y
     */
    private static final int TEXT_TIP_Y = 200;
    /**
     * time text x position
     */
    private static final int TIME_TEXT_X = 550;
    /**
     * time text y position
     */
    private static final int TIME_TEXT_Y = 50;
    /**
     * player animation x position
     */
    private static final int PLAYER_ANIMATION_POSITION_X = 600;
    /**
     * player animation y position
     */
    private static final int PLAYER_ANIMATION_POSITION_Y = 100;
    /**
     * based 120 frame
     */
    private static final int PLAYER_MOTION = 120;
    /**
     * map equals element
     */
    private static final int MAP_REFERENCE_EQUALS = 4;
    /**
     * map not equals element
     */
    private static final int MAP_REFERENCE_NOT_EQUALS = 3;
    /**
     * map 0 ---land
     */
    private static final int MAP_LAND = 0;
    /**
     * map 1 --way
     */
    private static final int MAP_WAY = 1;
    /**
     * map 2 -- wall
     */
    private static final int MAP_WALL = 2;
    /**
     * map 3 --- box
     */
    private static final int MAP_BOX = 3;
    /**
     * map 4 --target
     */
    private static final int MAP_TARGET = 4;
    /**
     * map 5 --player
     */
    private static final int MAP_PLAYER = 5;
    /**
     * draw Image element
     */
    private static final int DRAW_IMAGE_ELEMENT = 50;
    /**
     * box end element
     */
    private static final int BOX_END_ELEMENT = 30;
    /**
     * draw box end interval
     */
    private static final int BOX_END_INTERVAL = 10;
    //create canvas
    private Canvas canvas = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);
    private Canvas canvasLevel2 = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);
    private Canvas canvasLevel3 = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);
    private Canvas canvasLevel4 = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);
    //GraphicsContext get info
    private GraphicsContext g2d = canvas.getGraphicsContext2D();
    private GraphicsContext g2dLevel2 = canvasLevel2.getGraphicsContext2D();
    private GraphicsContext g2dLevel3 = canvasLevel3.getGraphicsContext2D();
    private GraphicsContext g2dLevel4 = canvasLevel4.getGraphicsContext2D();
    //setting AnchorPane as root
    private AnchorPane root;
    private AnchorPane root_2;
    private AnchorPane root_3;
    private AnchorPane root_4;
    //game level stage
    private Stage stage_2;
    private Stage stage_3;
    private Stage stage_4;

    //check is or not game over
    private boolean gameOverTime = false;
    private boolean gameOverTimeLevel2 = false;
    private boolean gameOverTimeLevel3 = false;
    private boolean gameOverTimeLevel4 = false;
    //time info by animation
    private Text timeText;
    private Text timeTextLevel2;
    private Text timeTextLevel3;
    private Text timeTextLevel4;
    //count down
    private static final int START_TIME_TEXT = 60;
    //Unit of time
    private static final long TIME_MODEL = 1000000000;

    //Timer operation
    private AnimationTimer timer;
    private AnimationTimer timerCostLevel2;
    private AnimationTimer timerCostLevel3;
    private AnimationTimer timerCostLevel4;
    //Player About Animation
    private AnimationTimer timerPlayerAnimation;
    private AnimationTimer timerPlayerAnimationLevel2;
    private AnimationTimer timerPlayerAnimationLevel3;
    private AnimationTimer timerPlayerAnimationLevel4;
    //player Animation
    //setting player position animation
    private int playerX, playerY;
    private int playerLevel2X,playerLevel2Y;
    private int playerLevel3X,playerLevel3Y;
    private int playerLevel4X,playerLevel4Y;
    //Image animation
    private Image playerImageBottom, playerImageTop , playerImageLeft ,playerImageRight;
    private ImageView playerView,playerViewLevel2,playerViewLevel3,playerViewLevel4;
    /**
     * Image path
     */
    private static final String LAND_PATH = "land.png";
    private static final String WAY_PATH = "way.png";
    private static final String WALL_PATH = "wall.png";
    private static final String BOX_PATH = "box.png";
    private static final String BOX_END = "box_end.png";
    /**
     * setting player motion
     */
    private int playerMotion;
    /**
     * init game map
     */
    static {
        /**
         * int[][] success init game map
         * 0--land
         * 1--way
         * 2-wall
         * 3-box
         * 4--target position
         * 5--player
         */
        map = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,4,3,1,1,2,4,3,1,2},
                {2,1,1,1,1,2,2,2,1,2},
                {2,2,2,1,5,1,1,1,1,2},
                {2,1,1,1,1,1,2,2,2,2},
                {2,4,3,1,1,1,1,3,4,2},
                {2,2,2,2,2,2,2,2,2,2}
        };
        //reference map
        map2 = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,4,3,1,1,2,4,3,1,2},
                {2,1,1,1,1,2,2,2,1,2},
                {2,2,2,1,1,1,1,1,1,2},
                {2,1,1,1,1,1,2,2,2,2},
                {2,4,3,1,1,1,1,3,4,2},
                {2,2,2,2,2,2,2,2,2,2}
        };

        map_root_2 = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,1,1,1,1,2,1,3,4,2},
                {2,4,3,1,1,2,1,2,1,2},
                {2,2,2,2,5,1,1,1,1,2},
                {2,4,3,1,1,1,1,1,2,2},
                {2,1,1,1,1,1,3,4,2,2},
                {2,2,2,2,2,2,2,2,2,2}
        };

        map2_root_2 = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,1,1,1,1,2,1,3,4,2},
                {2,4,3,1,1,2,1,2,1,2},
                {2,2,2,2,1,1,1,1,1,2},
                {2,4,3,1,1,1,1,1,2,2},
                {2,1,1,1,1,1,3,4,2,2},
                {2,2,2,2,2,2,2,2,2,2}
        };

        map_root_3 = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,1,4,3,1,2,2,2,1,2},
                {2,1,4,3,1,2,4,3,1,2},
                {2,2,2,2,5,1,1,1,1,2},
                {2,2,2,1,1,1,2,2,2,2},
                {2,2,2,1,1,1,3,4,2,2},
                {2,2,2,2,2,2,2,2,2,2}
        };

        map2_root_3 = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,1,4,3,1,2,2,2,1,2},
                {2,1,4,3,1,2,4,3,1,2},
                {2,2,2,2,1,1,1,1,1,2},
                {2,2,2,1,1,1,2,2,2,2},
                {2,2,2,1,1,1,3,4,2,2},
                {2,2,2,2,2,2,2,2,2,2}
        };
        map_root_4 = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,1,1,1,2,4,3,1,1,2},
                {2,2,4,3,1,2,2,2,1,2},
                {2,2,2,2,5,1,1,1,1,2},
                {2,2,4,3,1,2,2,2,2,2},
                {2,2,2,1,1,3,4,2,2,2},
                {2,2,2,2,2,2,2,2,2,2}
        };
        map2_root_4 = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,1,1,1,2,4,3,1,1,2},
                {2,2,4,3,1,2,2,2,1,2},
                {2,2,2,2,1,1,1,1,1,2},
                {2,2,4,3,1,2,2,2,2,2},
                {2,2,2,1,1,3,4,2,2,2},
                {2,2,2,2,2,2,2,2,2,2}
        };
    }

    /**
     * Main start function
     * @param stage
     */
    public void start(Stage stage) {
        try {
            //create AnchorPane
            root = new AnchorPane();
            //create background
            Scene scene = new Scene(root,BACKGROUND_WIDTH,BACKGROUND_HEIGHT);
            //add UI element
            game(root,scene);
            //setting scene
            stage.setScene(scene);
            //setting title
            stage.setTitle("Level-1");
            //setting resizable
            stage.setResizable(false);
            //show canvas
            stage.show();
            //create timer
            //cost time success
            timer = new AnimationTimer() {
                //create game start init time
                long gameStartTime = 0;
                @Override
                public void handle(long time) {
                    //init start time
                    if(gameStartTime == 0){
                        gameStartTime = time;
                    }
                    //compute cost time
                    int costTimeInfo = START_TIME_TEXT - (int) ((time - gameStartTime) / TIME_MODEL);
                    //show count down information
                    try {
                        drawTime(costTimeInfo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //start timer
            timer.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * level-2 stage
     */
    public void startLevel2(){
        try {
            //stop player level 1 animation
            playerAnimationStop();
            //create stage level 2
            stage_2 = new Stage();
            //create AnchorPane
            root_2 = new AnchorPane();
            //create background level2
            Scene scene2 = new Scene(root_2,BACKGROUND_WIDTH,BACKGROUND_HEIGHT);
            //add UI element
            gameLevel2(root_2,scene2);
            //setting scene
            stage_2.setScene(scene2);
            //setting title
            stage_2.setTitle("Level-2");
            //setting resizable
            stage_2.setResizable(false);
            //show canvas
            stage_2.show();
            //create timer
            //create timer
            timerCostLevel2 = new AnimationTimer() {
                //create game start init time
                long gameStartTimeLevel2 = 0;
                @Override
                public void handle(long time) {
                    //init start time
                    if(gameStartTimeLevel2 == 0){
                        gameStartTimeLevel2 = time;
                    }
                    //compute cost time
                    int costTimeInfoLevel2 = START_TIME_TEXT - (int) ((time - gameStartTimeLevel2) / TIME_MODEL);
                    //show count down information
                    try {
                        drawTimeLevel2(costTimeInfoLevel2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //start timer
            timerCostLevel2.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create stage level 3
     */
    public void startLevel3(){
        try {
            //stop level 2 player animation
            playerAnimationStopLevel2();
            //stage 3
            stage_3 = new Stage();
            //create AnchorPane
            root_3 = new AnchorPane();
            //create background
            Scene scene3 = new Scene(root_3,BACKGROUND_WIDTH,BACKGROUND_HEIGHT);
            //add UI element
            gameLevel3(root_3,scene3);
            //setting scene
            stage_3.setScene(scene3);
            //setting title
            stage_3.setTitle("Level-3");
            //setting resizable
            stage_3.setResizable(false);
            //show canvas
            stage_3.show();
            //create timer
            timerCostLevel3 = new AnimationTimer() {
                //create game start init time
                long gameStartTimeLevel3 = 0;
                @Override
                public void handle(long time) {
                    //init start time
                    if(gameStartTimeLevel3 == 0){
                        gameStartTimeLevel3 = time;
                    }
                    //compute cost time level 3
                    int costTimeInfoLevel3 = START_TIME_TEXT - (int) ((time - gameStartTimeLevel3) / TIME_MODEL);
                    //show count down information
                    try {
                        drawTimeLevel3(costTimeInfoLevel3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //start timer
            timerCostLevel3.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * startLevel 4
     */
    public void startLevel4(){
        try {
            //stop animation level 3
            playerAnimationStopLevel3();
            stage_4 = new Stage();
            //create AnchorPane
            root_4 = new AnchorPane();
            //create background
            Scene scene4 = new Scene(root_4,BACKGROUND_WIDTH,BACKGROUND_HEIGHT);
            //add UI element
            gameLevel4(root_4,scene4);
            //setting scene
            stage_4.setScene(scene4);
            //setting title
            stage_4.setTitle("Level-4");
            //setting resizable
            stage_4.setResizable(false);
            //show canvas
            stage_4.show();
            //create timer
            //setting cost time
            timerCostLevel4 = new AnimationTimer() {
                //create game start init time
                long gameStartTimeLevel4 = 0;
                @Override
                public void handle(long time) {
                    //init start time
                    if(gameStartTimeLevel4 == 0){
                        gameStartTimeLevel4 = time;
                    }
                    //compute cost time
                    int costTimeInfoLevel4 = START_TIME_TEXT - (int) ((time - gameStartTimeLevel4) / TIME_MODEL);
                    //show count down information
                    try {
                        drawTimeLevel4(costTimeInfoLevel4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //start timer
            timerCostLevel4.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * gameInfo
     * @param root
     * @param scene
     */
    private void game(AnchorPane root, Scene scene) {
        //create canvas
        //draw map by Image
        drawMap(g2d);
        //tip start animation
        timeText = new Text(TIME_TEXT_X, TIME_TEXT_Y, "Cost Time:  "+START_TIME_TEXT + "  s  ");
        timeText.setFont(new Font("TimeRoman", 18));
        //setting color
        timeText.setFill(Color.ORANGE);
        // add root element
        root.getChildren().add(canvas);
        root.getChildren().add(timeText);
        //keyboard input
        play(scene,g2d);
    }

    /**
     * game level 2
     * @param root_2
     * @param scene2
     */
    private void gameLevel2(AnchorPane root_2, Scene scene2){
        drawMapLevel2(g2dLevel2);
        //setting cost time
        timeTextLevel2 = new Text(TIME_TEXT_X, TIME_TEXT_Y, "Cost Time:  "+START_TIME_TEXT + "  s  ");
        timeTextLevel2.setFont(new Font("TimeRoman", 18));
        //setting color
        timeTextLevel2.setFill(Color.ORANGE);
        // add root element
        root_2.getChildren().add(canvasLevel2);
        root_2.getChildren().add(timeTextLevel2);
        //keyboard input
        playLevel2(scene2,g2dLevel2);
    }

    /**
     * gameLevel3
     * @param root_3
     * @param scene3
     */
    private void gameLevel3(AnchorPane root_3, Scene scene3){
        drawMapLevel3(g2dLevel3);
        timeTextLevel3 = new Text(TIME_TEXT_X, TIME_TEXT_Y, "Cost Time:  "+START_TIME_TEXT + "  s  ");
        timeTextLevel3.setFont(new Font("TimeRoman", 18));
        //setting color
        timeTextLevel3.setFill(Color.ORANGE);
        // add root element
        root_3.getChildren().add(canvasLevel3);
        root_3.getChildren().add(timeTextLevel3);
        //keyboard input
        playLevel3(scene3,g2dLevel3);
    }

    /**
     * gameLevel 4
     * @param root_4
     * @param scene4
     */
    private void gameLevel4(AnchorPane root_4, Scene scene4){
        drawMapLevel4(g2dLevel4);
        timeTextLevel4 = new Text(TIME_TEXT_X, TIME_TEXT_Y, "Cost Time:  "+START_TIME_TEXT + "  s  ");
        timeTextLevel4.setFont(new Font("TimeRoman", 18));
        //setting color
        timeTextLevel4.setFill(Color.ORANGE);
        // add root element
        root_4.getChildren().add(canvasLevel4);
        root_4.getChildren().add(timeTextLevel4);
        //keyboard input
        playLevel4(scene4,g2dLevel4);
    }

    /**
     * drawTime animation
     * @param timeInfo
     */
    public void drawTime(int timeInfo) throws InterruptedException{
        if(gameOverTime == true){
            //game over
            timeText.setFill(Color.RED);
            //setting alert tip
            FailedGame();
        }else{
            //game starting
            timeText.setFill(Color.ORANGE);
            timeText.setText("Cost Time: " + timeInfo);
            //time out
            if(timeInfo == 0){
                //count down end
                gameOverTime = true;
            }
        }
    }

    /**
     * drawTimeLevel -2
     * @param timeInfoLevel2
     */
    public void drawTimeLevel2(int timeInfoLevel2) throws InterruptedException {
        if(gameOverTimeLevel2 == true){
            //game over
            timeTextLevel2.setFill(Color.RED);
            //setting alert tip
            FailedGameLevel2();
        }else{
            //game starting
            timeTextLevel2.setFill(Color.ORANGE);
            timeTextLevel2.setText("Cost Time: " + timeInfoLevel2);
            //time out
            if(timeInfoLevel2 == 0){
                //count down end
                gameOverTimeLevel2 = true;
            }
        }
    }

    /**
     * drawTimeLevel -3
     * @param timeInfoLevel3
     */
    public void drawTimeLevel3 (int timeInfoLevel3)  throws InterruptedException{
        if(gameOverTimeLevel3 == true){
            //game over
            timeTextLevel3.setFill(Color.RED);
            //setting alert tip failed
            FailedGameLevel3();
        }else{
            //game starting
            timeTextLevel3.setFill(Color.ORANGE);
            timeTextLevel3.setText("Cost Time: " + timeInfoLevel3);
            //time out
            if(timeInfoLevel3 == 0){
                //count down end
                gameOverTimeLevel3 = true;
            }
        }
    }

    /**
     * drawTimeLevel -4
     * @param timeInfoLevel4
     */
    public void drawTimeLevel4(int timeInfoLevel4) throws InterruptedException {
        if(gameOverTimeLevel4 == true){
            //game over
            timeTextLevel4.setFill(Color.RED);
            //setting alert tip
            FailedGameLevel4();
        }else{
            //game starting
            timeTextLevel4.setFill(Color.ORANGE);
            timeTextLevel4.setText("Cost Time: " + timeInfoLevel4);
            //time out
            if(timeInfoLevel4 == 0){
                //count down end
                gameOverTimeLevel4 = true;
            }
        }
    }

    /**
     * game win level -1
     */
    private void win() {
        //setting win element
        boolean win = true;
        //
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                //success compare between map and reference map
                if (map2[i][j]==MAP_REFERENCE_EQUALS  && map[i][j]!=MAP_REFERENCE_NOT_EQUALS) {
                    //not success
                    win = false;
                }
            }
        }
        //win == true game win
        if (win) {
            //stop cost time
            timer.stop();
            //In situ rotation
            playerAnimationStart();
            //success next level 2
            successGame();
        }
    }

    /**
     * winLevel -2
     */
    private void winLevel2(){
        //setting win element
        boolean winLevel2 = true;
        //
        for (int i = 0; i < map_root_2.length; i++) {
            for (int j = 0; j < map_root_2[i].length; j++) {
                //success compare between map and reference map
                if (map2_root_2[i][j]==MAP_REFERENCE_EQUALS  && map_root_2[i][j]!=MAP_REFERENCE_NOT_EQUALS) {
                    //not success
                    winLevel2 = false;
                }
            }
        }
        //win == true game win
        if (winLevel2) {
            //stop cost time
            timerCostLevel2.stop();
            playerAnimationStartLevel2();
            //success game
            successGameLevel2();
        }
    }

    /**
     * winLevel -3
     */
    private void winLevel3(){
        //setting win element
        boolean winLevel3 = true;
        //
        for (int i = 0; i < map_root_3.length; i++) {
            for (int j = 0; j < map_root_3[i].length; j++) {
                //success compare between map and reference map
                if (map2_root_3[i][j]==MAP_REFERENCE_EQUALS  && map_root_3[i][j]!=MAP_REFERENCE_NOT_EQUALS) {
                    //not success
                    winLevel3 = false;
                }
            }
        }
        //win == true game win
        if (winLevel3) {
            //stop cost time level3
            timerCostLevel3.stop();
            //player animation level 3
            playerAnimationStartLevel3();
            //success game level -3
            successGameLevel3();
        }
    }

    /**
     * winLevel -4
     */
    private void winLevel4(){
        //setting win element
        boolean winLevel4 = true;
        //
        for (int i = 0; i < map_root_4.length; i++) {
            for (int j = 0; j < map_root_4[i].length; j++) {
                //success compare between map and reference map
                if (map2_root_4[i][j]==MAP_REFERENCE_EQUALS  && map_root_4[i][j]!=MAP_REFERENCE_NOT_EQUALS) {
                    //not success
                    winLevel4 = false;
                }
            }
        }
        //win == true game win
        if (winLevel4) {
            //stop cost time level4
            timerCostLevel4.stop();
            playerAnimationStartLevel4();
            //success game level -4
            successGameLevel4();
        }
    }

    /**
     * successGame
     */
    public void successGame(){
        //create alert
        Alert alertSuccess = new Alert(Alert.AlertType.CONFIRMATION);
        //setting title
        alertSuccess.setTitle("Win Game");
        //null Title
        alertSuccess.setHeaderText(null);
        //setting info
        alertSuccess.setContentText("Victory Level -1 !  !  ! \n");

        //Button sure exit
        ButtonType next = new ButtonType("Next", ButtonBar.ButtonData.CANCEL_CLOSE);
        //add dialog
        alertSuccess.getButtonTypes().setAll(next);
        //compute result
        Optional<ButtonType> result = alertSuccess.showAndWait();
        //create position operation
        if(result.get() == next){//sure operation
            //go to level -2
            startLevel2();
        }
    }

    /**
     * successGameLevel -2
     */
    public void successGameLevel2(){
        //create alert level 2
        Alert alertSuccessLevel2 = new Alert(Alert.AlertType.CONFIRMATION);
        //setting title
        alertSuccessLevel2.setTitle("Win Game");
        //null Title
        alertSuccessLevel2.setHeaderText(null);
        //setting info
        alertSuccessLevel2.setContentText("Victory Level -2 !  !  ! \n");

        //Button sure next
        ButtonType next = new ButtonType("Next", ButtonBar.ButtonData.CANCEL_CLOSE);
        //add dialog
        alertSuccessLevel2.getButtonTypes().setAll(next);
        //compute result
        Optional<ButtonType> resultLevel2 = alertSuccessLevel2.showAndWait();
        //create position operation
        if(resultLevel2.get() == next){//sure operation
            //go to level 3
            startLevel3();
        }
    }

    /**
     * successGameLevel -3
     */
    public void successGameLevel3(){
        //create alert level 3
        Alert alertSuccessLevel3 = new Alert(Alert.AlertType.CONFIRMATION);
        //setting title
        alertSuccessLevel3.setTitle("Win Game");
        //null Title level 3
        alertSuccessLevel3.setHeaderText(null);
        //setting info
        alertSuccessLevel3.setContentText("Victory Level -3 !  !  ! \n");

        //Button sure next
        ButtonType next = new ButtonType("Next", ButtonBar.ButtonData.CANCEL_CLOSE);
        //add dialog
        alertSuccessLevel3.getButtonTypes().setAll(next);
        //compute result
        Optional<ButtonType> resultLevel3 = alertSuccessLevel3.showAndWait();
        //create position operation
        if(resultLevel3.get() == next){//sure operation
            //go to level -4
            startLevel4();
        }
    }

    /**
     * successGameLevel -4
     */
    public void successGameLevel4(){
        //create alert level 4
        Alert alertSuccessLevel4 = new Alert(Alert.AlertType.CONFIRMATION);
        //setting title
        alertSuccessLevel4.setTitle("Win Game");
        //null Title
        alertSuccessLevel4.setHeaderText(null);
        //setting info
        alertSuccessLevel4.setContentText("Victory Level -4 !  !  ! \n");

        //Button sure exit
        ButtonType exit = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        //add dialog
        alertSuccessLevel4.getButtonTypes().setAll(exit);
        //compute result
        Optional<ButtonType> resultLevel4 = alertSuccessLevel4.showAndWait();
        //create position operation
        if(resultLevel4.get() == exit){//sure operation
            //stop player in level 4 animation
            playerAnimationStopLevel4();
            //exit
            System.exit(-1);
        }
    }

    /**
     * as game exit
     */
    public void FailedGameExit(){
        System.exit(-1);
    }

    /**
     * FailedGame function
     */
    public void FailedGame() throws InterruptedException {
        //stop Timer
        timer.stop();
        //create alert failure
        Alert alertFailed = new Alert(Alert.AlertType.INFORMATION);
        //setting title
        alertFailed.setTitle("Failure Game");
        alertFailed.setHeaderText(null);
        //setting info
        alertFailed.setContentText("Timeout Failure Game! Please start again !\n");
        alertFailed.show();
        Thread.sleep(1000);
        FailedGameExit();
    }

    /**
     * FailedGameLevel -2
     */
    public void FailedGameLevel2() throws InterruptedException {
        //stop Timer
        timerCostLevel2.stop();
        //create alert failure
        Alert alertFailedLevel2 = new Alert(Alert.AlertType.INFORMATION);
        //setting title
        alertFailedLevel2.setTitle("Failure Game");
        alertFailedLevel2.setHeaderText(null);
        //setting info
        alertFailedLevel2.setContentText("Timeout Failure Game Level-2 !  !  ! \n");
        alertFailedLevel2.show();
        Thread.sleep(1000);
        FailedGameExit();
    }

    /**
     * FailedGameLevel -3
     */
    public void FailedGameLevel3() throws InterruptedException {
        //stop Timer
        timerCostLevel3.stop();
        //create alert failure
        Alert alertFailedLevel3 = new Alert(Alert.AlertType.INFORMATION);
        //setting title
        alertFailedLevel3.setTitle("Failure Game");
        alertFailedLevel3.setHeaderText(null);
        //setting info
        alertFailedLevel3.setContentText("Timeout Failure Game Level-3 !  !  ! \n");
        alertFailedLevel3.show();
        Thread.sleep(1000);
        FailedGameExit();
    }

    /**
     * FailedGameLevel -4
     */
    public void  FailedGameLevel4() throws InterruptedException {
        //stop Timer
        timerCostLevel4.stop();
        //create alert failure
        Alert alertFailedLevel4 = new Alert(Alert.AlertType.INFORMATION);
        //setting title
        alertFailedLevel4.setTitle("Failure Game");
        alertFailedLevel4.setHeaderText(null);
        //setting info
        alertFailedLevel4.setContentText("Timeout Failure Game Level-4 !  !  ! \n");
        alertFailedLevel4.show();
        Thread.sleep(1000);
        FailedGameExit();
    }

    /**
     * player move up
     */
    public void UpMove() throws InterruptedException {
        //current direction is top
        direction  = "top";
        //setting way and target
        if (map[x-1][y]==MAP_WAY || map[x-1][y]==MAP_TARGET) {
            //Restores the player's current position
            if (map2[x][y]==MAP_TARGET) {
                map[x][y] = MAP_TARGET;
            }else {
                map[x][y] = MAP_WAY;
            }
            //move player
            map[x-1][y] = MAP_PLAYER;
            //Record the player's current coordinates
            x = x - 1;//position -1
            //draw map continue
            drawMap(g2d);
        }
        //box compute
        if (map[x-1][y]==MAP_BOX) {
            //Continue judging the top of the box
            //If it's a way or a target
            if (map[x-1-1][y]==MAP_WAY  ||  map[x-1-1][y]==MAP_TARGET) {
                //move player
                if (map2[x][y] == MAP_TARGET) {
                    map[x][y] = MAP_TARGET;
                }else {
                    //current way
                    map[x][y] = MAP_WAY;
                }
                //move player to way
                map[x-1][y] = MAP_PLAYER;
                //Record the player's current position
                x = x - 1;
                //move box
                //The current seat of the bin does not need to be restored
                //move box
                map[x-1-1][y] = MAP_BOX;
                //check game over
                win();
            }
        }
    }

    /**
     * UpMoveLevel -2
     */
    public void UpMoveLevel2(){
        direction  = "top";
        //setting way and target
        if (map_root_2[xLevel2-1][yLevel2]==MAP_WAY || map_root_2[xLevel2-1][yLevel2]==MAP_TARGET) {
            //Restores the player's current position
            if (map2_root_2[xLevel2][yLevel2]==MAP_TARGET) {
                map_root_2[xLevel2][yLevel2] = MAP_TARGET;
            }else {
                map_root_2[xLevel2][yLevel2] = MAP_WAY;
            }
            //move player
            map_root_2[xLevel2-1][yLevel2] = MAP_PLAYER;
            //Record the player's current coordinates
            xLevel2 = xLevel2 - 1;//position -1
            //draw map continue
            drawMapLevel2(g2dLevel2);
        }
        //box compute
        if (map_root_2[xLevel2-1][yLevel2]==MAP_BOX) {
            //Continue judging the top of the box
            if (map_root_2[xLevel2-1-1][yLevel2]==MAP_WAY  ||  map_root_2[xLevel2-1-1][yLevel2]==MAP_TARGET) {
                //move player
                if (map2_root_2[xLevel2][yLevel2] == MAP_TARGET) {
                    map_root_2[xLevel2][yLevel2] = MAP_TARGET;
                }else {
                    //current way
                    map_root_2[xLevel2][yLevel2] = MAP_WAY;
                }
                //move player to way
                map_root_2[xLevel2-1][yLevel2] = MAP_PLAYER;
                //Record the player's current position
                xLevel2 = xLevel2 - 1;
                //move box
                map_root_2[xLevel2-1-1][yLevel2] = MAP_BOX;
                //check game over level 2
                winLevel2();
            }
        }
    }

    /**
     * UpMoveLevel -3
     */
    public void UpMoveLevel3(){
        //current direction is top
        direction  = "top";
        if (map_root_3[xLevel3-1][yLevel3]==MAP_WAY || map_root_3[xLevel3-1][yLevel3]==MAP_TARGET) {
            //Restores the player's current position
            if (map2_root_3[xLevel3][yLevel3]==MAP_TARGET) {
                map_root_3[xLevel3][yLevel3] = MAP_TARGET;
            }else {
                map_root_3[xLevel3][yLevel3] = MAP_WAY;
            }
            //move player
            map_root_3[xLevel3-1][yLevel3] = MAP_PLAYER;
            //Record the player's current coordinates
            xLevel3 = xLevel3 - 1;//position -1
            //draw map continue
            drawMapLevel3(g2dLevel3);
        }
        //box compute
        if (map_root_3[xLevel3-1][yLevel3]==MAP_BOX) {
            //Continue judging the top of the box
            //If it's a way or a target
            if (map_root_3[xLevel3-1-1][yLevel3]==MAP_WAY  ||  map_root_3[xLevel3-1-1][yLevel3]==MAP_TARGET) {
                //move player
                if (map2_root_3[xLevel3][yLevel3] == MAP_TARGET) {
                    map_root_3[xLevel3][yLevel3] = MAP_TARGET;
                }else {
                    //current way
                    map_root_3[xLevel3][yLevel3] = MAP_WAY;
                }
                //move player to way
                map_root_3[xLevel3-1][yLevel3] = MAP_PLAYER;
                //Record the player's current position
                xLevel3 = xLevel3 - 1;
                //move box
                map_root_3[xLevel3-1-1][yLevel3] = MAP_BOX;
                //check game over
                winLevel3();
            }
        }
    }

    /**
     * UpMoveLevel -4
     */
    public void UpMoveLevel4(){
        //current direction is top
        direction  = "top";
        //setting way and target
        if (map_root_4[xLevel4-1][yLevel4]==MAP_WAY || map_root_4[xLevel4-1][yLevel4]==MAP_TARGET) {
            //Restores the player's current position
            if (map2_root_4[xLevel4][yLevel4]==MAP_TARGET) {
                map_root_4[xLevel4][yLevel4] = MAP_TARGET;
            }else {
                map_root_4[xLevel4][yLevel4] = MAP_WAY;
            }
            //move player
            map_root_4[xLevel4-1][yLevel4] = MAP_PLAYER;
            //Record the player's current coordinates
            xLevel4 = xLevel4 - 1;//position -1
            //draw map continue
            drawMapLevel4(g2dLevel4);
        }
        //box compute
        if (map_root_4[xLevel4-1][yLevel4]==MAP_BOX) {
            //Continue judging the top of the box
            //If it's a way or a target
            if (map_root_4[xLevel4-1-1][yLevel4]==MAP_WAY  ||  map_root_4[xLevel4-1-1][yLevel4]==MAP_TARGET) {
                //move player
                if (map2_root_4[xLevel4][yLevel4] == MAP_TARGET) {
                    map_root_4[xLevel4][yLevel4] = MAP_TARGET;
                }else {
                    //current way
                    map_root_4[xLevel4][yLevel4] = MAP_WAY;
                }
                //move player to way
                map_root_4[xLevel4-1][yLevel4] = MAP_PLAYER;
                //Record the player's current position
                xLevel4 = xLevel4 - 1;
                //move box
                //The current seat of the bin does not need to be restored
                //move box
                map_root_4[xLevel4-1-1][yLevel4] = MAP_BOX;
                //check game over
                winLevel4();
            }
        }
    }

    /**
     * player move bottom
     */
    public void BottomMove() throws InterruptedException {
        //current direction down
        direction  = "bottom";
        //way and target check
        if (map[x+1][y]==MAP_WAY   ||   map[x+1][y]==MAP_TARGET) {
            if (map2[x][y]==MAP_TARGET) {
                map[x][y] = MAP_TARGET;
            }else {
                map[x][y] = MAP_WAY;
            }
            //move player
            map[x+1][y] = MAP_PLAYER;
            //current player
            x = x + 1;
            //draw map info
            drawMap(g2d);
        }
        //box compute
        if (map[x+1][y]==MAP_BOX) {
            //if box top
            //way or target
            if (map[x+1+1][y]==MAP_WAY   ||  map[x+1+1][y]==MAP_TARGET) {
                //move
                if (map2[x][y]==MAP_TARGET) {
                    map[x][y] = MAP_TARGET;
                }else {
                    map[x][y] = MAP_WAY;
                }
                //move
                map[x+1][y] = MAP_PLAYER;
                //current position
                x = x + 1;
                //move box
                map[x+1+1][y] = MAP_BOX;
                //win game
                win();

            }
        }
    }

    /**
     * BottomMoveLevel -2
     */
    public void BottomMoveLevel2(){
        //current direction down
        direction  = "bottom";
        //way and target check
        if (map_root_2[xLevel2+1][yLevel2]==MAP_WAY   ||   map_root_2[xLevel2+1][yLevel2]==MAP_TARGET) {
            if (map2_root_2[xLevel2][yLevel2]==MAP_TARGET) {
                map_root_2[xLevel2][yLevel2] = MAP_TARGET;
            }else {
                map_root_2[xLevel2][yLevel2] = MAP_WAY;
            }
            //move player
            map_root_2[xLevel2+1][yLevel2] = MAP_PLAYER;
            //current player
            xLevel2 = xLevel2 + 1;
            //draw map info
            drawMapLevel2(g2dLevel2);
        }
        //box compute
        if (map_root_2[xLevel2+1][yLevel2]==MAP_BOX) {
            //if box top
            //way or target
            if (map_root_2[xLevel2+1+1][yLevel2]==MAP_WAY   ||  map_root_2[xLevel2+1+1][yLevel2]==MAP_TARGET) {
                //move
                if (map2_root_2[xLevel2][yLevel2]==MAP_TARGET) {
                    map_root_2[xLevel2][yLevel2] = MAP_TARGET;
                }else {
                    map_root_2[xLevel2][yLevel2] = MAP_WAY;
                }
                //move
                map_root_2[xLevel2+1][yLevel2] = MAP_PLAYER;
                //current position
                xLevel2 = xLevel2 + 1;
                //move box
                map_root_2[xLevel2+1+1][yLevel2] = MAP_BOX;
                //win game
                winLevel2();
            }
        }
    }

    /**
     * BottomMoveLevel -3
     */
    public void BottomMoveLevel3(){
        //current direction down
        direction  = "bottom";
        //way and target check
        if (map_root_3[xLevel3+1][yLevel3]==MAP_WAY   ||   map_root_3[xLevel3+1][yLevel3]==MAP_TARGET) {
            if (map2_root_3[xLevel3][yLevel3]==MAP_TARGET) {
                map_root_3[xLevel3][yLevel3] = MAP_TARGET;
            }else {
                map_root_3[xLevel3][yLevel3] = MAP_WAY;
            }
            //move player
            map_root_3[xLevel3+1][yLevel3] = MAP_PLAYER;
            //current player
            xLevel3 = xLevel3 + 1;
            //draw map info
            drawMapLevel3(g2dLevel3);
        }
        //box compute
        if (map_root_3[xLevel3+1][yLevel3]==MAP_BOX) {
            //if box top
            //way or target
            if (map_root_3[xLevel3+1+1][yLevel3]==MAP_WAY   ||  map_root_3[xLevel3+1+1][yLevel3]==MAP_TARGET) {
                //move
                if (map2_root_3[xLevel3][yLevel3]==MAP_TARGET) {
                    map_root_3[xLevel3][yLevel3] = MAP_TARGET;
                }else {
                    map_root_3[xLevel3][yLevel3] = MAP_WAY;
                }
                //move
                map_root_3[xLevel3+1][yLevel3] = MAP_PLAYER;
                //current position
                xLevel3 = xLevel3 + 1;
                //move box
                map_root_3[xLevel3+1+1][yLevel3] = MAP_BOX;
                //win game
                winLevel3();

            }
        }
    }

    /**
     * BottomMoveLevel -4
     */
    public void BottomMoveLevel4(){
        //current direction down
        direction  = "bottom";
        //way and target check
        if (map_root_4[xLevel4+1][yLevel4]==MAP_WAY   ||   map_root_4[xLevel4+1][yLevel4]==MAP_TARGET) {
            if (map2_root_4[xLevel4][yLevel4]==MAP_TARGET) {
                map_root_4[xLevel4][yLevel4] = MAP_TARGET;
            }else {
                map_root_4[xLevel4][yLevel4] = MAP_WAY;
            }
            //move player
            map_root_4[xLevel4+1][yLevel4] = MAP_PLAYER;
            //current player
            xLevel4 = xLevel4 + 1;
            //draw map info
            drawMapLevel4(g2dLevel4);
        }
        //box compute
        if (map_root_4[xLevel4+1][yLevel4]==MAP_BOX) {
            //if box top
            if (map_root_4[xLevel4+1+1][yLevel4]==MAP_WAY   ||  map_root_4[xLevel4+1+1][yLevel4]==MAP_TARGET) {
                //move
                if (map2_root_4[xLevel4][yLevel4]==MAP_TARGET) {
                    map_root_4[xLevel4][yLevel4] = MAP_TARGET;
                }else {
                    map_root_4[xLevel4][yLevel4] = MAP_WAY;
                }
                map_root_4[xLevel4+1][yLevel4] = MAP_PLAYER;
                //current position
                xLevel4 = xLevel4 + 1;
                //move box
                map_root_4[xLevel4+1+1][yLevel4] = MAP_BOX;
                //win game level 4
                winLevel4();
            }
        }
    }

    /**
     * playerAnimationData
     */
    public void playerAnimationData(){
        playerImageBottom = new Image("bottom.png");
        playerImageTop = new Image("top.png");
        playerImageLeft = new Image("left.png");
        playerImageRight = new Image("right.png");
    }

    /**
     * player animation  start
     */
    public void playerAnimationStart(){
        playerAnimationData();
        playerView = new ImageView( playerImageBottom );
        playerView.setX(playerX);
        playerView.setY(playerY);
        playerView.toFront();
        root.getChildren().add(playerView);
        //create timerPlayerAnimation
        timerPlayerAnimation = new AnimationTimer() {
            @Override
            public void handle(long timeAnimation) {
                playerMoveAnimation();
            }
        };
        timerPlayerAnimation.start();
    }

    //animation 2
    public void playerAnimationStartLevel2(){
        playerAnimationData();
        playerViewLevel2 = new ImageView( playerImageBottom );
        playerViewLevel2.setX(playerLevel2X);
        playerViewLevel2.setY(playerLevel2Y);
        playerViewLevel2.toFront();
        root_2.getChildren().add(playerViewLevel2);
        //create timerPlayerAnimation
        timerPlayerAnimationLevel2 = new AnimationTimer() {
            @Override
            public void handle(long timeAnimationLevel2) {
                playerMoveAnimationLevel2();
            }
        };
        timerPlayerAnimationLevel2.start();
    }

    /**
     * playerAnimationStartLevel -3
     */
    public void playerAnimationStartLevel3(){
        playerAnimationData();
        playerViewLevel3 = new ImageView( playerImageBottom );
        playerViewLevel3.setX(playerLevel3X);
        playerViewLevel3.setY(playerLevel3Y);
        playerViewLevel3.toFront();
        root_3.getChildren().add(playerViewLevel3);
        //create timerPlayerAnimation
        timerPlayerAnimationLevel3 = new AnimationTimer() {
            @Override
            public void handle(long timeAnimationLevel2) {
                playerMoveAnimationLevel3();
            }
        };
        timerPlayerAnimationLevel3.start();
    }

    /**
     * playerAnimationStartLevel -4
     */
    public void playerAnimationStartLevel4(){
        playerAnimationData();
        playerViewLevel4 = new ImageView( playerImageBottom );
        //setting position
        playerViewLevel4.setX(playerLevel4X);
        playerViewLevel4.setY(playerLevel4Y);
        playerViewLevel4.toFront();
        root_4.getChildren().add(playerViewLevel4);
        //create timerPlayerAnimation
        timerPlayerAnimationLevel4 = new AnimationTimer() {
            @Override
            public void handle(long timeAnimationLevel2) {
                //start animation
                playerMoveAnimationLevel4();
            }
        };
        timerPlayerAnimationLevel4.start();
    }


    /**
     * player animation stop
     */
    public void playerAnimationStop(){
        timerPlayerAnimation.stop();
    }

    /**
     * player animation stop level 2
     */
    public void playerAnimationStopLevel2(){
        timerPlayerAnimationLevel2.stop();
    }

    /**
     * player animation stop level 3
     */
    public void playerAnimationStopLevel3(){
        timerPlayerAnimationLevel3.stop();
    }

    /**
     * player animation stop level 4
     */
    public void playerAnimationStopLevel4(){
        timerPlayerAnimationLevel4.stop();
    }

    /**
     * play game
     * important
     * 1.add keyboard
     * 	↑ describe player up
     * 	↓ describe player down
     * 	← describe player left
     * 	→ describe player right
     * 	G describe player animation start
     * 	H describe player animation stop
     * @param scene
     * @param g2d
     */
    private void play(Scene scene, GraphicsContext g2d) {
        //scene add function
        scene.setOnKeyPressed(new EventHandler<Event>() {
            public void handle(Event event) {
                //get KeyEvent
                KeyEvent ke = (KeyEvent)event;
                //get Code by Key
                KeyCode code = ke.getCode();
                switch (code) {
                    case UP:
                        try {
                            UpMove();
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    case DOWN:
                        try {
                            BottomMove();
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    case LEFT:
                        //current direction is left
                        direction  = "left";
                        //way and target
                        if (map[x][y-1]==MAP_WAY  ||  map[x][y-1]==MAP_TARGET) {
                            if (map2[x][y]==MAP_TARGET) {
                                map[x][y] = MAP_TARGET;
                            }else {
                                map[x][y] = MAP_WAY;
                            }
                            //move player
                            map[x][y-1] = MAP_PLAYER;
                            //current position
                            //because left so y -1
                            y = y-1;
                            //draw map
                            drawMap(g2d);
                            break;
                        }
                        //box
                        if (map[x][y-1]==MAP_BOX) {
                            if (map[x][y-1-1]==MAP_WAY   ||  map[x][y-1-1]==MAP_TARGET) {
                                //move
                                if (map2[x][y]==MAP_TARGET) {
                                    map[x][y] = MAP_TARGET;
                                }else {
                                    map[x][y] = MAP_WAY;
                                }
                                //move player
                                map[x][y-1] = MAP_PLAYER;
                                //move box
                                map[x][y-1-1] = MAP_BOX;
                                //current position
                                y = y - 1;
                                //continue beginning draw map
                                drawMap(g2d);
                                //check game over
                                win();
                            }
                        }
                        break;
                    case RIGHT:
                        //current direction right
                        direction  = "right";
                        //way or target
                        if (map[x][y+1]==MAP_WAY   ||   map[x][y+1]==MAP_TARGET) {
                            if (map2[x][y]==MAP_TARGET) {
                                map[x][y] = MAP_TARGET;
                            }else {
                                map[x][y] = MAP_WAY;
                            }
                            //move
                            map[x][y+1] = MAP_PLAYER;
                            //current player
                            y = y + 1;
                            //draw map
                            drawMap(g2d);
                            break;
                        }
                        //box
                        if (map[x][y+1]==MAP_BOX) {
                            if (map[x][y+1+1]==MAP_WAY  ||  map[x][y+1+1]==MAP_TARGET) {
                                //MOVE
                                if (map2[x][y]==MAP_TARGET) {
                                    map[x][y] = MAP_TARGET;
                                }else {
                                    map[x][y] = MAP_WAY;
                                }
                                //move player
                                map[x][y+1] = MAP_PLAYER;
                                //move player
                                map[x][y+1+1] = MAP_BOX;
                                //CURRENT player
                                y = y + 1;
                                //draw map
                                drawMap(g2d);
                                //win game
                                win();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * playLevel -2
     * @param scene2
     * @param g2dLevel2
     */
    private void playLevel2(Scene scene2, GraphicsContext g2dLevel2){
        //scene add function
        scene2.setOnKeyPressed(new EventHandler<Event>() {
            public void handle(Event event) {
                //get KeyEvent
                KeyEvent keLevel = (KeyEvent)event;
                //get Code by Key
                KeyCode code = keLevel.getCode();
                switch (code) {
                    case UP:
                        UpMoveLevel2();
                        break;
                    case DOWN:
                        BottomMoveLevel2();
                        break;
                    case LEFT:
                        //current direction is left
                        direction  = "left";
                        //way and target
                        if (map_root_2[xLevel2][yLevel2-1]==MAP_WAY  ||  map_root_2[xLevel2][yLevel2-1]==MAP_TARGET) {
                            if (map2_root_2[xLevel2][yLevel2]==MAP_TARGET) {
                                map_root_2[xLevel2][yLevel2] = MAP_TARGET;
                            }else {
                                map_root_2[xLevel2][yLevel2] = MAP_WAY;
                            }
                            //move player
                            map_root_2[xLevel2][yLevel2-1] = MAP_PLAYER;
                            //current position
                            //because left so y -1
                            yLevel2 = yLevel2-1;
                            //draw map
                            drawMapLevel2(g2dLevel2);
                            break;
                        }
                        //box
                        if (map_root_2[xLevel2][yLevel2-1]==MAP_BOX) {
                            if (map_root_2[xLevel2][yLevel2-1-1]==MAP_WAY   ||  map_root_2[xLevel2][yLevel2-1-1]==MAP_TARGET) {
                                //move
                                if (map2_root_2[xLevel2][yLevel2]==MAP_TARGET) {
                                    map_root_2[xLevel2][yLevel2] = MAP_TARGET;
                                }else {
                                    map_root_2[xLevel2][yLevel2] = MAP_WAY;
                                }
                                //move player
                                map_root_2[xLevel2][yLevel2-1] = MAP_PLAYER;
                                //move box
                                map_root_2[xLevel2][yLevel2-1-1] = MAP_BOX;
                                //current position
                                yLevel2 = yLevel2 - 1;
                                //continue beginning draw map
                                drawMapLevel2(g2dLevel2);
                                //check game over
                                winLevel2();
                            }
                        }
                        break;
                    case RIGHT:
                        //current direction right
                        direction  = "right";
                        //way or target
                        if (map_root_2[xLevel2][yLevel2+1]==MAP_WAY   ||   map_root_2[xLevel2][yLevel2+1]==MAP_TARGET) {
                            if (map2_root_2[xLevel2][yLevel2]==MAP_TARGET) {
                                map_root_2[xLevel2][yLevel2] = MAP_TARGET;
                            }else {
                                map_root_2[xLevel2][yLevel2] = MAP_WAY;
                            }
                            //move
                            map_root_2[xLevel2][yLevel2+1] = MAP_PLAYER;
                            //current player
                            yLevel2 = yLevel2 + 1;
                            //draw map
                            drawMapLevel2(g2dLevel2);
                            break;
                        }
                        //box
                        if (map_root_2[xLevel2][yLevel2+1]==MAP_BOX) {
                            if (map_root_2[xLevel2][yLevel2+1+1]==MAP_WAY  ||  map_root_2[xLevel2][yLevel2+1+1]==MAP_TARGET) {
                                //MOVE
                                if (map2_root_2[xLevel2][yLevel2]==MAP_TARGET) {
                                    map_root_2[xLevel2][yLevel2] = MAP_TARGET;
                                }else {
                                    map_root_2[xLevel2][yLevel2] = MAP_WAY;
                                }
                                //move player
                                map_root_2[xLevel2][yLevel2+1] = MAP_PLAYER;
                                //move player
                                map_root_2[xLevel2][yLevel2+1+1] = MAP_BOX;
                                //CURRENT player
                                yLevel2 = yLevel2 + 1;
                                //draw map
                                drawMapLevel2(g2dLevel2);
                                //win game
                                winLevel2();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * playLevel -3
     * @param scene3
     * @param g2dLevel3
     */
    private void playLevel3(Scene scene3, GraphicsContext g2dLevel3){
        //scene add function
        scene3.setOnKeyPressed(new EventHandler<Event>() {
            public void handle(Event event) {
                //get KeyEvent
                KeyEvent keLevel3 = (KeyEvent)event;
                //get Code by Key
                KeyCode code = keLevel3.getCode();
                switch (code) {
                    case UP:
                        UpMoveLevel3();
                        break;
                    case DOWN:
                        BottomMoveLevel3();
                        break;
                    case LEFT:
                        //current direction is left
                        direction  = "left";
                        //way and target
                        if (map_root_3[xLevel3][yLevel3-1]==MAP_WAY  ||  map_root_3[xLevel3][yLevel3-1]==MAP_TARGET) {
                            if (map2_root_3[xLevel3][yLevel3]==MAP_TARGET) {
                                map_root_3[xLevel3][yLevel3] = MAP_TARGET;
                            }else {
                                map_root_3[xLevel3][yLevel3] = MAP_WAY;
                            }
                            //move player
                            map_root_3[xLevel3][yLevel3-1] = MAP_PLAYER;
                            //current position
                            //because left so y -1
                            yLevel3 = yLevel3-1;
                            //draw map
                            drawMapLevel3(g2dLevel3);
                            break;
                        }
                        //box
                        if (map_root_3[xLevel3][yLevel3-1]==MAP_BOX) {
                            if (map_root_3[xLevel3][yLevel3-1-1]==MAP_WAY   ||  map_root_3[xLevel3][yLevel3-1-1]==MAP_TARGET) {
                                //move
                                if (map2_root_3[xLevel3][yLevel3]==MAP_TARGET) {
                                    map_root_3[xLevel3][yLevel3] = MAP_TARGET;
                                }else {
                                    map_root_3[xLevel3][yLevel3] = MAP_WAY;
                                }
                                //move player
                                map_root_3[xLevel3][yLevel3-1] = MAP_PLAYER;
                                //move box
                                map_root_3[xLevel3][yLevel3-1-1] = MAP_BOX;
                                //current position
                                yLevel3 = yLevel3 - 1;
                                //continue beginning draw map
                                drawMapLevel3(g2dLevel3);
                                //check game over
                                winLevel3();
                            }
                        }
                        break;
                    case RIGHT:
                        //current direction right
                        direction  = "right";
                        //way or target
                        if (map_root_3[xLevel3][yLevel3+1]==MAP_WAY   ||   map_root_3[xLevel3][yLevel3+1]==MAP_TARGET) {
                            if (map2_root_3[xLevel3][yLevel3]==MAP_TARGET) {
                                map_root_3[xLevel3][yLevel3] = MAP_TARGET;
                            }else {
                                map_root_3[xLevel3][yLevel3] = MAP_WAY;
                            }
                            //move
                            map_root_3[xLevel3][yLevel3+1] = MAP_PLAYER;
                            //current player
                            yLevel3 = yLevel3 + 1;
                            //draw map
                            drawMapLevel3(g2dLevel3);
                            break;
                        }
                        //box
                        if (map_root_3[xLevel3][yLevel3+1]==MAP_BOX) {
                            if (map_root_3[xLevel3][yLevel3+1+1]==MAP_WAY  ||  map_root_3[xLevel3][yLevel3+1+1]==MAP_TARGET) {
                                //MOVE
                                if (map2_root_3[xLevel3][yLevel3]==MAP_TARGET) {
                                    map_root_3[xLevel3][yLevel3] = MAP_TARGET;
                                }else {
                                    map_root_3[xLevel3][yLevel3] = MAP_WAY;
                                }
                                //move player
                                map_root_3[xLevel3][yLevel3+1] = MAP_PLAYER;
                                //move player
                                map_root_3[xLevel3][yLevel3+1+1] = MAP_BOX;
                                //CURRENT player
                                yLevel3 = yLevel3 + 1;
                                //draw map
                                drawMapLevel3(g2dLevel3);
                                //win game
                                winLevel3();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * playLevel -4
     * @param scene4
     * @param g2dLevel4
     */
    private void playLevel4(Scene scene4, GraphicsContext g2dLevel4){
        //scene add function
        scene4.setOnKeyPressed(new EventHandler<Event>() {
            public void handle(Event event) {
                //get KeyEvent
                KeyEvent keLevel4 = (KeyEvent)event;
                //get Code by Key
                KeyCode code = keLevel4.getCode();
                switch (code) {
                    case UP:
                        UpMoveLevel4();
                        break;
                    case DOWN:
                        BottomMoveLevel4();
                        break;
                    case LEFT:
                        //current direction is left
                        direction  = "left";
                        //way and target
                        if (map_root_4[xLevel4][yLevel4-1]==MAP_WAY  ||  map_root_4[xLevel4][yLevel4-1]==MAP_TARGET) {
                            if (map2_root_4[xLevel4][yLevel4]==MAP_TARGET) {
                                map_root_4[xLevel4][yLevel4] = MAP_TARGET;
                            }else {
                                map_root_4[xLevel4][yLevel4] = MAP_WAY;
                            }
                            //move player
                            map_root_4[xLevel4][yLevel4-1] = MAP_PLAYER;
                            //current position
                            //because left so y -1
                            yLevel4 = yLevel4-1;
                            //draw map
                            drawMapLevel4(g2dLevel4);
                            break;
                        }
                        //box
                        if (map_root_4[xLevel4][yLevel4-1]==MAP_BOX) {
                            if (map_root_4[xLevel4][yLevel4-1-1]==MAP_WAY   ||  map_root_4[xLevel4][yLevel4-1-1]==MAP_TARGET) {
                                //move
                                if (map2_root_4[xLevel4][yLevel4]==MAP_TARGET) {
                                    map_root_4[xLevel4][yLevel4] = MAP_TARGET;
                                }else {
                                    map_root_4[xLevel4][yLevel4] = MAP_WAY;
                                }
                                //move player
                                map_root_4[xLevel4][yLevel4-1] = MAP_PLAYER;
                                //move box
                                map_root_4[xLevel4][yLevel4-1-1] = MAP_BOX;
                                //current position
                                yLevel4 = yLevel4 - 1;
                                //continue beginning draw map
                                drawMapLevel4(g2dLevel4);
                                //check game over
                                winLevel4();
                            }
                        }
                        break;
                    case RIGHT:
                        //current direction right
                        direction  = "right";
                        //way or target
                        if (map_root_4[xLevel4][yLevel4+1]==MAP_WAY   ||   map_root_4[xLevel4][yLevel4+1]==MAP_TARGET) {
                            if (map2_root_4[xLevel4][yLevel4]==MAP_TARGET) {
                                map_root_4[xLevel4][yLevel4] = MAP_TARGET;
                            }else {
                                map_root_4[xLevel4][yLevel4] = MAP_WAY;
                            }
                            //move
                            map_root_4[xLevel4][yLevel4+1] = MAP_PLAYER;
                            //current player
                            yLevel4 = yLevel4 + 1;
                            //draw map
                            drawMapLevel4(g2dLevel4);
                            break;
                        }
                        //box
                        if (map_root_4[xLevel4][yLevel4+1]==MAP_BOX) {
                            if (map_root_4[xLevel4][yLevel4+1+1]==MAP_WAY  ||  map_root_4[xLevel4][yLevel4+1+1]==MAP_TARGET) {
                                //MOVE
                                if (map2_root_4[xLevel4][yLevel4]==MAP_TARGET) {
                                    map_root_4[xLevel4][yLevel4] = MAP_TARGET;
                                }else {
                                    map_root_4[xLevel4][yLevel4] = MAP_WAY;
                                }
                                //move player
                                map_root_4[xLevel4][yLevel4+1] = MAP_PLAYER;
                                //move player
                                map_root_4[xLevel4][yLevel4+1+1] = MAP_BOX;
                                //CURRENT player
                                yLevel4 = yLevel4 + 1;
                                //draw map
                                drawMapLevel4(g2dLevel4);
                                //win game
                                winLevel4();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }


    /**
     * AnimationBottom setting level 1-4
     */
    public void AnimationBottom(){
        playerView.setImage(playerImageBottom);
    }
    public void AnimationBottomLevel2(){
        playerViewLevel2.setImage(playerImageBottom);
    }
    public void AnimationBottomLevel3(){
        playerViewLevel3.setImage(playerImageBottom);
    }
    public void AnimationBottomLevel4(){
        playerViewLevel4.setImage(playerImageBottom);
    }

    /**
     * AnimationTop setting level 1-4
     */
    public void AnimationTop(){
        playerView.setImage(playerImageTop);
    }
    public void AnimationTopLevel2(){
        playerViewLevel2.setImage(playerImageTop);
    }
    public void AnimationTopLevel3(){
        playerViewLevel3.setImage(playerImageTop);
    }
    public void AnimationTopLevel4(){
        playerViewLevel4.setImage(playerImageTop);
    }
    /**
     * AnimationLeft setting level 1-4
     */
    public void AnimationLeft(){
        playerView.setImage(playerImageLeft);
    }
    public void AnimationLeftLevel2(){
        playerViewLevel2.setImage(playerImageLeft);
    }
    public void AnimationLeftLevel3(){
        playerViewLevel3.setImage(playerImageLeft);
    }
    public void AnimationLeftLevel4(){
        playerViewLevel4.setImage(playerImageLeft);
    }

    /**
     * AnimationRight setting level 1-4
     */
    public void AnimationRight(){
        playerView.setImage(playerImageRight);
    }
    public void AnimationRightLevel2(){
        playerViewLevel2.setImage(playerImageRight);
    }
    public void AnimationRightLevel3(){
        playerViewLevel3.setImage(playerImageRight);
    }
    public void AnimationRightLevel4(){
        playerViewLevel4.setImage(playerImageRight);
    }

    /**
     * player move animation
     */
    public void playerMoveAnimation(){
        playerMotion = (playerMotion+1) % PLAYER_MOTION; // based 120 frame
        switch (playerMotion) {
            case 0:
                AnimationBottom();
                break;
            case 18:
                AnimationTop();
                break;
            case 60:
                AnimationLeft();
                break;
            case 66:
                AnimationRight();
                break;
            case 72:
                AnimationBottom();
                break;
            case 78:
                AnimationTop();
                break;
            case 84:
                AnimationLeft();
                break;
            case 102:
                AnimationRight();
                break;
            default:
                break;
        }
    }

    /**
     * playerMoveAnimationLevel -2
     */
    public void playerMoveAnimationLevel2(){
        playerMotion = (playerMotion+1) % PLAYER_MOTION; // based 120 frame
        switch (playerMotion) {
            case 0:
                AnimationBottomLevel2();
                break;
            case 18:
                AnimationTopLevel2();
                break;
            case 60:
                AnimationLeftLevel2();
                break;
            case 66:
                AnimationRightLevel2();
                break;
            case 72:
                AnimationBottomLevel2();
                break;
            case 78:
                AnimationTopLevel2();
                break;
            case 84:
                AnimationLeftLevel2();
                break;
            case 102:
                AnimationRightLevel2();
                break;
            default:
                break;
        }
    }

    /**
     * playerMoveAnimationLevel -3
     */
    public void playerMoveAnimationLevel3(){
        playerMotion = (playerMotion+1) % PLAYER_MOTION; // based 120 frame
        switch (playerMotion) {
            case 0:
                AnimationBottomLevel3();
                break;
            case 18:
                AnimationTopLevel3();
                break;
            case 60:
                AnimationLeftLevel3();
                break;
            case 66:
                AnimationRightLevel3();
                break;
            case 72:
                AnimationBottomLevel3();
                break;
            case 78:
                AnimationTopLevel3();
                break;
            case 84:
                AnimationLeftLevel3();
                break;
            case 102:
                AnimationRightLevel3();
                break;
            default:
                break;
        }
    }

    /**
     * playerMoveAnimationLevel -4
     */
    public void playerMoveAnimationLevel4(){
        playerMotion = (playerMotion+1) % PLAYER_MOTION; // based 120 frame
        switch (playerMotion) {
            case 0:
                AnimationBottomLevel4();
                break;
            case 18:
                AnimationTopLevel4();
                break;
            case 60:
                AnimationLeftLevel4();
                break;
            case 66:
                AnimationRightLevel4();
                break;
            case 72:
                AnimationBottomLevel4();
                break;
            case 78:
                AnimationTopLevel4();
                break;
            case 84:
                AnimationLeftLevel4();
                break;
            case 102:
                AnimationRightLevel4();
                break;
            default:
                break;
        }
    }

    /**
     * draw map
     * @param
     */
    private void drawMap(GraphicsContext g2d) {
        //draw map traverse
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                /**
                 * Image img
                 * x - position
                 * y - position
                 * w - width
                 * h - height
                 * 0--land  1--way   2-wall 3-box  4--target 5--player
                 */
                switch (map[i][j]) {
                    case 0:
                        Image landImage =new Image(LAND_PATH);
                        g2d.drawImage(landImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 1:
                        Image wayImage =new Image(WAY_PATH);
                        g2d.drawImage(wayImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 2:
                        Image wallImage =new Image(WALL_PATH);
                        g2d.drawImage(wallImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 3:
                        Image box_way_image =new Image(WALL_PATH);
                        g2d.drawImage(box_way_image,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image box =new Image(BOX_PATH);
                        g2d.drawImage(box,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 4:
                        Image wayImage2 =new Image(WAY_PATH);
                        g2d.drawImage(wayImage2,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image box_end =new Image(BOX_END);
                        g2d.drawImage(box_end,j*DRAW_IMAGE_ELEMENT+BOX_END_INTERVAL,i*DRAW_IMAGE_ELEMENT+BOX_END_INTERVAL,BOX_END_ELEMENT,BOX_END_ELEMENT);
                        break;
                    case 5:
                        //init position
                        x = i;
                        y = j;
                        Image wayImage3 =new Image(WAY_PATH);
                        g2d.drawImage(wayImage3,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image playerImage = new Image(direction+".png");
                        g2d.drawImage(playerImage,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        playerX = j*DRAW_IMAGE_ELEMENT;
                        playerY = i*DRAW_IMAGE_ELEMENT;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * drawMapLevel -2
     * @param g2dLevel2
     */
    private void drawMapLevel2(GraphicsContext g2dLevel2){
        //draw map traverse
        for (int i = 0; i < map_root_2.length; i++) {
            for (int j = 0; j < map_root_2[i].length; j++) {
                switch (map_root_2[i][j]) {
                    case 0:
                        Image landImage =new Image(LAND_PATH);
                        g2dLevel2.drawImage(landImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 1:
                        Image wayImage =new Image(WAY_PATH);
                        g2dLevel2.drawImage(wayImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 2:
                        Image wallImage =new Image(WALL_PATH);
                        g2dLevel2.drawImage(wallImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 3:
                        Image box_way_image =new Image(WALL_PATH);
                        g2dLevel2.drawImage(box_way_image,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image box =new Image(BOX_PATH);
                        g2dLevel2.drawImage(box,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 4:
                        Image wayImage2 =new Image(WAY_PATH);
                        g2dLevel2.drawImage(wayImage2,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image box_end =new Image(BOX_END);
                        g2dLevel2.drawImage(box_end,j*DRAW_IMAGE_ELEMENT+BOX_END_INTERVAL,i*DRAW_IMAGE_ELEMENT+BOX_END_INTERVAL,BOX_END_ELEMENT,BOX_END_ELEMENT);
                        break;
                    case 5:
                        //setting position level 2
                        //init position
                        xLevel2 = i;
                        yLevel2 = j;
                        Image wayImage3 =new Image(WAY_PATH);
                        g2dLevel2.drawImage(wayImage3,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image playerImage = new Image(direction+".png");
                        g2dLevel2.drawImage(playerImage,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        playerLevel2X = j*DRAW_IMAGE_ELEMENT;
                        playerLevel2Y = i*DRAW_IMAGE_ELEMENT;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * drawMapLevel -3
     * @param g2dLevel3
     */
    private void drawMapLevel3(GraphicsContext g2dLevel3){
        //draw map traverse
        for (int i = 0; i < map_root_3.length; i++) {
            for (int j = 0; j < map_root_3[i].length; j++) {
                switch (map_root_3[i][j]) {
                    case 0:
                        Image landImage =new Image(LAND_PATH);
                        g2dLevel3.drawImage(landImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 1:
                        Image wayImage =new Image(WAY_PATH);
                        g2dLevel3.drawImage(wayImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 2:
                        Image wallImage =new Image(WALL_PATH);
                        g2dLevel3.drawImage(wallImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 3:
                        Image box_way_image =new Image(WALL_PATH);
                        g2dLevel3.drawImage(box_way_image,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image box =new Image(BOX_PATH);
                        g2dLevel3.drawImage(box,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 4:
                        Image wayImage2 =new Image(WAY_PATH);
                        g2dLevel3.drawImage(wayImage2,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image box_end =new Image(BOX_END);
                        g2dLevel3.drawImage(box_end,j*DRAW_IMAGE_ELEMENT+BOX_END_INTERVAL,i*DRAW_IMAGE_ELEMENT+BOX_END_INTERVAL,BOX_END_ELEMENT,BOX_END_ELEMENT);
                        break;
                    case 5:
                        //init position
                        xLevel3 = i;
                        yLevel3 = j;
                        Image wayImage3 =new Image(WAY_PATH);
                        g2dLevel3.drawImage(wayImage3,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image playerImage = new Image(direction+".png");
                        g2dLevel3.drawImage(playerImage,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        playerLevel3X = j*DRAW_IMAGE_ELEMENT;
                        playerLevel3Y = i*DRAW_IMAGE_ELEMENT;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * drawMapLevel -4
     * @param g2dLevel4
     */
    private void drawMapLevel4(GraphicsContext g2dLevel4){
        //draw map traverse
        for (int i = 0; i < map_root_4.length; i++) {
            for (int j = 0; j < map_root_4[i].length; j++) {
                switch (map_root_4[i][j]) {
                    case 0:
                        Image landImage =new Image(LAND_PATH);
                        g2dLevel4.drawImage(landImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 1:
                        Image wayImage =new Image(WAY_PATH);
                        g2dLevel4.drawImage(wayImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 2:
                        Image wallImage =new Image(WALL_PATH);
                        g2dLevel4.drawImage(wallImage,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 3:
                        Image box_way_image =new Image(WALL_PATH);
                        g2dLevel4.drawImage(box_way_image,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image box =new Image(BOX_PATH);
                        g2dLevel4.drawImage(box,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        break;
                    case 4:
                        Image wayImage2 =new Image(WAY_PATH);
                        g2dLevel4.drawImage(wayImage2,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image box_end =new Image(BOX_END);
                        g2dLevel4.drawImage(box_end,j*DRAW_IMAGE_ELEMENT+BOX_END_INTERVAL,i*DRAW_IMAGE_ELEMENT+BOX_END_INTERVAL,BOX_END_ELEMENT,BOX_END_ELEMENT);
                        break;
                    case 5:
                        //init position
                        xLevel4 = i;
                        yLevel4 = j;
                        Image wayImage3 =new Image(WAY_PATH);
                        g2dLevel4.drawImage(wayImage3,j * DRAW_IMAGE_ELEMENT,i * DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        Image playerImage = new Image(direction+".png");
                        g2dLevel4.drawImage(playerImage,j*DRAW_IMAGE_ELEMENT,i*DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT,DRAW_IMAGE_ELEMENT);
                        playerLevel4X = j*DRAW_IMAGE_ELEMENT;
                        playerLevel4Y = i*DRAW_IMAGE_ELEMENT;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Run Game
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}