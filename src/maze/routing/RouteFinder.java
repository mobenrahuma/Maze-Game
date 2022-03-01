package maze.routing;
import maze.*;

import java.io.*;
import java.util.*;

/**
 * Class providing maze solving functionality, maze loading and saving functionality 
 * @author MohamedBenRahuma
 */
public class RouteFinder implements Serializable{
    private Maze maze;
    private Stack<Tile> route = new Stack<>();
    private boolean finished;

    /**
     * Constructor, takes maze object as an arg
     * @param m
     * @author MohamedBenRahuma
     */
    public RouteFinder(Maze m){
        maze = m;
    }
    /**
     * returns maze 
     * @return
     * @author MohamedBenRahuma
     */
    public Maze getMaze(){
        return maze;
    }
    /**
     * return current route that has been traversed in the Maze
     * @return
     * @author MohamedBenRahuma
     */
    public List<Tile> getRoute(){
        return new ArrayList<>(route);
    }

    /**
     * checks to see if Maze has been successfully traversed 
     * @return
     * @author MohamedBenRahuma
     */
    public boolean isFinished(){
        return finished;
    }

    /**
     * loads a RoutFinder from a file at the location of the arg path
     * @param path
     * @return
     * @author MohamedBenRahuma
     */
    public static RouteFinder load(String path){
        try(FileInputStream f = new FileInputStream(path); ObjectInputStream obj = new ObjectInputStream(f)){
            try{
                RouteFinder r = (RouteFinder)obj.readObject();
                return r;
            }
            catch(ClassNotFoundException c){
                System.out.println(c.getMessage());
            }
        }
        catch (FileNotFoundException f){
            System.out.println(f.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    /**
     * Saves route finder to file at given path
     * @param path
     * @author MohamedBenRahuma
     */
    public void save(String path){
        try(FileOutputStream f = new FileOutputStream(path); ObjectOutputStream obj = new ObjectOutputStream(f)){
            obj.writeObject(this);
        }
        catch(FileNotFoundException f){
            System.out.println(f.getMessage());
        }
        catch (IOException i){
            System.out.println(i.getMessage());
        }
    }

    public Stack<Tile> nonRoute = new Stack<>();
    boolean initialized = false;
    Set<Tile> visited = new HashSet<>();

    /**
     * Steps through the maze until either no rout is found at which exception is thrown 
     * or the maze has been successfully traversed 
     * @return
     * @throws NoRouteFoundException
     * @author MohamedBenRahuma
     */
    public boolean step() throws NoRouteFoundException{
        Maze.Direction[] directions = {Maze.Direction.EAST, Maze.Direction.WEST, Maze.Direction.NORTH, Maze.Direction.SOUTH};
        if(!finished) {
            if (route.isEmpty() && !initialized) {
                route.add(maze.getEntrance());
                initialized = true;
            }
            if(route.isEmpty() && initialized){
                throw new NoRouteFoundException();
            }
            if (!route.isEmpty()) {
                Tile currentTile = route.peek();
                Maze.Direction directionToGo = null;

                for (Maze.Direction d : directions) {
                    if (maze.getAdjacentTile(currentTile, d) != null && !visited.contains(maze.getAdjacentTile(currentTile, d)) && maze.getAdjacentTile(currentTile, d).getType() != Tile.Type.WALL) {
                        directionToGo = d;
                        break;
                    }
                }
                if (directionToGo == null) {
                    nonRoute.add(route.pop());
                } else {
                    if (maze.getAdjacentTile(currentTile, directionToGo).toString().equals("x")) {
                        route.add(maze.getAdjacentTile(currentTile, directionToGo));
                        finished = true;
                        return true;
                    }
                    route.add(maze.getAdjacentTile(currentTile, directionToGo));
                    visited.add(maze.getAdjacentTile(currentTile, directionToGo));
                }
            }
        }
        return false;
    }
    /**
     * return string representation of route finder
     * @author MohamedBenRahuma
     */
    public String toString(){
        String res = "";
        List<List<Tile>> tiles = maze.getTiles();
        for(int i=0; i<tiles.size(); i++){
            for(int j=0; j<tiles.get(i).size(); j++){
                if(j==0)res += (tiles.size()-1) - i + "\t";
                if(route.contains(tiles.get(i).get(j))){
                    res += "*" + " ";
                }
                else if(nonRoute.contains(tiles.get(i).get(j))){
                    res += "-" + " ";
                }
                else res += tiles.get(i).get(j).toString() + " ";
                if(j == tiles.get(i).size()-1){
                    res += "\n";
                }
            }
        }
        res += "\n\t";
        for(int i=0; i<tiles.get(0).size(); i++){
            res += i + " ";
        }
        return res;
    }


}
