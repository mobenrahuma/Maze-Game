package maze.visualisation;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import maze.Maze;
import maze.Tile;
import maze.routing.RouteFinder;

import java.util.ArrayList;
import java.util.List;
/**
 * Class providing mazeGraphics functionality, allows maze object to represented graphically 
 * @author MohamedBenRahuma
 */
public class MazeGraphics {
    /**
     * function returns a group representing a graphical representation of the maze 
     * @param m
     * @param rf
     * @return
     * @author MohamedBenRahuma
     */
    public static Group mazeGraphic(Maze m, RouteFinder rf){
        List<Rectangle> walls = new ArrayList<>();
        List<List<Tile>> tiles = m.getTiles();
        int x = 0;
        int y = 50;
        for(int i=0; i<tiles.size(); i++){
            for(int j=0; j<tiles.get(i).size(); j++){
                if(tiles.get(i).get(j).getType() == Tile.Type.WALL){
                    Rectangle r = new Rectangle(x, y, 20, 20);
                    r.setFill(Color.YELLOW);
                    r.setStroke(Color.BLACK);
                    walls.add(r);
                }
                if(tiles.get(i).get(j).getType() == Tile.Type.ENTRANCE){
                    Rectangle r = new Rectangle(x, y, 20, 20);
                    r.setFill(Color.GREEN);
                    r.setStroke(Color.BLACK);
                    walls.add(r);
                }
                if(tiles.get(i).get(j).getType() == Tile.Type.EXIT){
                    Rectangle r = new Rectangle(x, y, 20, 20);
                    r.setFill(Color.BLUE);
                    r.setStroke(Color.BLACK);
                    walls.add(r);
                }
                if(tiles.get(i).get(j).getType() == Tile.Type.CORRIDOR){
                    Rectangle r = new Rectangle(x, y, 20, 20);
                    r.setFill(Color.LIGHTGREY);
                    r.setStroke(Color.BLACK);
                    walls.add(r);

                }
                if(rf.nonRoute.contains(tiles.get(i).get(j))){
                    Rectangle r = new Rectangle(x, y, 20, 20);
                    r.setFill(Color.RED);
                    r.setStroke(Color.BLACK);
                    walls.add(r);
                }
                if(rf.getRoute().contains(tiles.get(i).get(j))){
                    Rectangle r = new Rectangle(x, y, 20, 20);
                    r.setFill(Color.GREEN);
                    r.setStroke(Color.BLACK);
                    walls.add(r);
                }
                x+=20;
            }
            x=0;
            y+=20;
        }
        Group g = new Group();
        g.getChildren().addAll(walls);
        return g;
    }
}
