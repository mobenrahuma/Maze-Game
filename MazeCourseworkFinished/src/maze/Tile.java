package maze;

import java.io.Serializable;
/**
 * Class providing tile functionality 
 * @author MohamedBenRahuma
 */

public class Tile implements Serializable {

    /**
     * enum representing all the different types of tiles
     * @author MohamedBenRahuma
     */
    public enum Type{
        CORRIDOR, ENTRANCE, EXIT, WALL;
    }
    private Type type;

    private Tile(Type t){
        type = t;
    }
    /**
     * creates tile object from char arg
     * @param c
     * @return
     * @author MohamedBenRahuma
     */
    protected static Tile fromChar(char c){
        switch(c){
            case 'e' : return new Tile(Type.ENTRANCE);
            case '#' : return new Tile(Type.WALL);
            case 'x' : return new Tile(Type.EXIT);
            case '.' : return new Tile(Type.CORRIDOR);
        }
        return null;
    }
    /**
     * returns type of tile
     * @return
     * @author MohamedBenRahuma
     */
    public Type getType(){
        return type;
    }
    /**
     * Checks to see if tile type is navigable 
     * @author MohamedBenRahuma
     */
    public boolean isNavigable(){
        if(this.getType() != Type.WALL){
            return true; 
        }
        return false; 
    }
    /**
     * return string representation of tile 
     * @author MohamedBenRahuma
     */
    public String toString(){
        switch(this.getType()){
            case CORRIDOR: return ".";
            case WALL: return "#";
            case ENTRANCE: return "e";
            case EXIT: return "x";
        }

        return null;
    }


}
