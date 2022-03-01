package maze;
import java.util.*;
import java.io.*;

/**
 * Class for main maze logic, provides operations for creating Mazes from txt files, 
 * for getting tiles at given coords in the maze, and getting adjacent tiles 
 * @author MohamedBenRahuma 
 * 
 */

public class Maze implements Serializable{

    /**
     * Enum representing the directions that can be traversed in the Maze
     * @author MohamedBenRahuma
     */
    public enum Direction{
        NORTH, SOUTH, EAST, WEST;
    }

    /**
     * Nested Class providing details of where tiles are located within the Mazw
     * @author MohamedBenRahuma
     */

    public class Coordinate {
        private int x;
        private int y;

        public Coordinate(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }

        public String toString(){
            return "(" + x + ", " + y + ")";
        }
    }

    private Tile entrance;
    private Tile exit;
    private List<List<Tile>> tiles = new ArrayList<>();

    private Maze(){

    }

    /**
     * Maze objects created from reading txt files 
     * Also validity of text file is checked (ie whether it is representing a valid Maze)
     * @param path
     * @return
     * @throws InvalidMazeException
     * @throws RaggedMazeException
     * @throws IllegalArgumentException
     * @author MohamedBenRahuma
     */
    public static Maze fromTxt(String path)throws InvalidMazeException, RaggedMazeException,IllegalArgumentException{
        if(!checkForValidMaze(path))throw new InvalidMazeException(); 
        if(!checkForEntrance(path))throw new NoEntranceException();
        if(!checkForExit(path))throw new NoExitException(); 
        Maze m = new Maze();
        Tile entrance = null; 
        Tile exit = null; 
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = "";
            while(line != null){
                List<Tile> l = new ArrayList<>();
                line = reader.readLine();
                if(line != null){
                    for(int i=0; i<line.length(); i++){
                        Tile t = Tile.fromChar(line.charAt(i));
                        if(t.getType() == Tile.Type.ENTRANCE){
                            entrance = t;
                        }
                        if(t.getType() == Tile.Type.EXIT){
                            exit = t;
                        }
                        l.add(t);
                    }
                    m.tiles.add(l);
                }
            }
            m.setEntrance(entrance);
            m.setExit(exit);
            if(isRagged(m.tiles)){
                throw new RaggedMazeException(); 
            }
            return m;
            
        }
        catch(FileNotFoundException f){
            System.out.println(f.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    
    }
    /**
     * Check to see if Maze to read is not ragged
     * @param tiles
     * @return
     * @author MohamedBenRahuma
     */
    public static boolean isRagged(List<List<Tile>> tiles){
        int len = tiles.get(0).size(); 
        for(List<Tile> l: tiles){
            if(l.size() != len)return true; 
        }
        return false; 
    }


    /**
     * Checks if Maze to be read only contains the legal Maze Characters, any 
     * character outside of this list cause function to return false 
     * @param path
     * @return
     * @author MohamedBenRahuma
     */
    public static boolean checkForValidMaze(String path){
        List<Character> validSymbols = Arrays.asList('e', '#', '.', 'x','\n');
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = ""; 
            while(line != null){
                line = reader.readLine(); 
                if(line != null){
                    for(int i=0; i<line.length(); i++){
                        if(!validSymbols.contains(Character.valueOf(line.charAt(i))))return false; 
                    }
                }
                    
            }
        }
        catch (FileNotFoundException f){
            System.out.println(f.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return true; 
    }
    
    /**
     * checks that an entrance is present inside the Maze to be read
     * @param path
     * @return
     * @author MohamedBenRahuma
     */
    public static boolean checkForEntrance(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = ""; 
            while(line != null){
                line = reader.readLine(); 
                if(line!=null && line.contains("e"))return true; 
            }
        }
        catch (FileNotFoundException f){
            System.out.println(f.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false; 
    }
    /**
     * Checks that an exit is present inside the maze to be read
     * @param path
     * @return
     * @author MohamedBenRahuma
     */
    public static boolean checkForExit(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = "";
            while(line != null){
                line = reader.readLine();
                if(line!=null && line.contains("x"))return true;
            }
        }
        catch (FileNotFoundException f){
            System.out.println(f.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Returns an adjacent tile in the given direction 'd'
     * @param t
     * @param d
     * @return
     * @author MohamedBenRahuma
     */
    public Tile getAdjacentTile(Tile t, Direction d){
        Coordinate c = getTileLocation(t);
        switch(d){
            case EAST : return getTileAtLocation(new Coordinate(c.getX() + 1, c.getY()));
            case WEST: return getTileAtLocation(new Coordinate(c.getX() - 1, c.getY()));
            case NORTH: return getTileAtLocation(new Coordinate(c.getX(), c.getY()+1));
            case SOUTH: return getTileAtLocation(new Coordinate(c.getX(), c.getY() - 1));
        }
        return null;
    }

    /**
     * Returns entrance
     * @return
     * @author MohamedBenRahuma
     */
    public Tile getEntrance(){
        return entrance;
    }
    /**
     * Returns exit
     * @return
     * @author MohamedBenRahuma
     */
    public Tile getExit(){
        return exit;
    }
    /**
     * Returns tile at a given Coordinate in the Maze 
     * @param c
     * @return
     * @author MohamedBenRahuma
     */
    public Tile getTileAtLocation(Coordinate c){
        int x = c.getX();
        int y = (tiles.size()-1) - c.getY();

        for(int i=0; i<tiles.size(); i++){
            for(int j=0; j<tiles.get(i).size(); j++){
                if(i == y && j == x){
                    return tiles.get(i).get(j);
                }
            }
        }
        return null;
    }
    /**
     * Returns the Coordinate of a given tile 't' in the Maze 
     * @param t
     * @return
     * @author MohamedBenRahuma
     */
    public Coordinate getTileLocation(Tile t){
        for(int i=0; i<tiles.size(); i++){
            for(int j=0; j<tiles.get(i).size(); j++){
                if(tiles.get(i).get(j) == t){
                    int x = j;
                    int y = (tiles.size()-1) - i;
                    Coordinate c = new Coordinate(x, y);
                    return c;
                }
            }
        }
        return null;

    }
    /**
     * Return all the tiles in the Maze 
     * @return
     * @author MohamedBenRahuma
     */
    public List<List<Tile>> getTiles(){
        return tiles;
    }

    /**
     * Checks to see if given tile is in the Maze 
     * @param tile
     * @return
     * @author MohamedBenRahuma
     */
    public boolean tileInMaze(Tile tile){
        for(List<Tile> l: tiles){
            if(l.contains(tile))return true; 
        }
        return false; 
    }

    /**
     * Sets the entrance of the Maze 
     * @param t
     * @throws MultipleEntranceException
     * @throws IllegalArgumentException
     * @author MohamedBenRahuma
     */
    private void setEntrance(Tile t) throws MultipleEntranceException, IllegalArgumentException{
        if(entrance == null && tileInMaze(t)){
            entrance = t;
        }
        else if(!tileInMaze(t))throw new IllegalArgumentException(); 
        else{
            throw new MultipleEntranceException();
        }
    }

    /**
     * Sets the exit of the Maze
     * @param t
     * @throws MultipleExitException
     * @throws IllegalArgumentException
     * @author MohamedBenRahuma
     */
    private void setExit(Tile t) throws MultipleExitException, IllegalArgumentException{
        if(exit == null && tileInMaze(t)){
            exit = t;
        }
        else if(!tileInMaze(t))throw new IllegalArgumentException(); 
        else{
            throw new MultipleExitException();
        }
        
    }
    /**
     * Returns string representation of the Maze
     */
    public String toString(){
        String res = "";
        for(int i=0; i<tiles.size(); i++){
            for(int j=0; j<tiles.get(i).size(); j++){
                if(j==0)res += (tiles.size()-1) - i + "\t";
                res += tiles.get(i).get(j).toString() + " ";
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
