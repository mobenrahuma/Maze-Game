import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import maze.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import maze.routing.NoRouteFoundException;
import maze.routing.RouteFinder;
import javafx.stage.FileChooser;

import java.io.*;
import maze.visualisation.MazeGraphics;

/**
 * Main Class that addes Maze Graphics and all components
 * @author MohamedBenRahuma
 */
public class MazeApplication extends Application{
    static Group g;
    static String path = "../resources/mazes/maze2.txt";
    static RouteFinder r;
    static Maze m;
    /**
     * creates default Maze and RouteFinder
     * @throws InvalidMazeException
     * @throws IllegalArgumentException
     * @author MohamedBenRahuma
     */
    public static void initialise() throws InvalidMazeException, IllegalArgumentException{
        m = Maze.fromTxt(path);
        r = new RouteFinder(m);
    }
    /**
     * Starts GUI and adds all Scene, Nodes to Stage
     * @param stage
     * @throws InvalidMazeException
     * @author MohamedBenRahuma
     */
    public void start(Stage stage) throws InvalidMazeException {
        int windowXSize = (m.getTiles().get(0).size() * 20) + 150;
        int windowYSize = (m.getTiles().size() * 20) + 150;

        g = MazeGraphics.mazeGraphic(m, r);
        VBox root = new VBox();

        Button b = new Button("Step");

        FileChooser fileChooser = new FileChooser();
        Button loadMaze = new Button("Load Maze");

        HBox h = new HBox();
        TextField t = new TextField("Enter file name here");
        Button saveRoute =  new Button("Save Route");

        h.getChildren().addAll(t, saveRoute);

        Button loadRoute = new Button("Load Route");

        root.getChildren().addAll(loadMaze, g, b, h, loadRoute);
        Scene s = new Scene(root, windowXSize, windowYSize, Color.WHITE);
        stage.setScene(s);
        stage.show();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        loadMaze.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                String p = file.getPath();
                path = p;
                try {
                    initialise();
                    start(stage);
                } catch (InvalidMazeException invalidMazeException) {
                    new Alert(Alert.AlertType.ERROR, "Invalid Maze").showAndWait();
                } catch(IllegalArgumentException i){
                    System.out.println(i.getMessage());
                }
            }
        });

        b.setOnAction(e -> {
            try {
                r.step();
                g = MazeGraphics.mazeGraphic(m,r);
                Group grp = (Group)root.getChildren().get(1);
                grp.getChildren().removeAll();
                grp.getChildren().addAll(g.getChildren());

            }
            catch (NoRouteFoundException noRouteFoundException) {
                new Alert(Alert.AlertType.ERROR, "No Route Found").showAndWait();
            }

        });

        DirectoryChooser directoryChooser = new DirectoryChooser();
        saveRoute.setOnAction(e -> {
            String text = t.getText() + ".obj";
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null){
                r.save(selectedDirectory.getAbsoluteFile() + "/" + text);
            }
        });

        FileChooser fileChooser2 = new FileChooser();
        fileChooser2.getExtensionFilters().add(new FileChooser.ExtensionFilter("obj files", "*.obj"));

        loadRoute.setOnAction(e -> {
            File file = fileChooser2.showOpenDialog(stage);
            if(file != null){
                String p = file.getPath();
                RouteFinder route = RouteFinder.load(p);
                m = route.getMaze();
                r = route;
                try {
                    start(stage);
                } catch (InvalidMazeException invalidMazeException) {
                    invalidMazeException.printStackTrace();
                }
            }
        });

    }
    /**
     * main function to launch Application
     * @param args
     * @throws InvalidMazeException
     * @throws IllegalArgumentException
     * @author MohamedBenRahuma
     */
    public static void main(String[] args) throws InvalidMazeException, IllegalArgumentException {
        initialise();
        launch(args);
    }


}
