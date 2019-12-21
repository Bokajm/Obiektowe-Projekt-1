package agh.cs.lab2;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST,
    ERROR;

    public String toString(){
        switch(this)
        {
            case NORTH:
                return "Północ";
            case SOUTH:
                return "Południe";
            case WEST:
                return "Zachód";
            case EAST:
                return "Wschód";
            case NORTHEAST:
                return "Północny wschód";
            case SOUTHWEST:
                return "Południowy zachód";
            case NORTHWEST:
                return "Północny zachód";
            case SOUTHEAST:
                return "Południowy wschód";
            default:
            {
                System.out.print("Wat");
                break;
            }
        }
        return "Wat";
    }

    public Vector2d toUnitVector(){
        switch(this)
        {
            case NORTH:
                return (new Vector2d(0,1));
            case SOUTH:
                return (new Vector2d(0,-1));
            case WEST:
                return (new Vector2d(-1,0));
            case EAST:
                return (new Vector2d(1,0));
            case NORTHEAST:
                return (new Vector2d(1,1));
            case SOUTHWEST:
                return (new Vector2d(-1,-1));
            case NORTHWEST:
                return (new Vector2d(-1,1));
            case SOUTHEAST:
                return (new Vector2d(1,-1));
            default:
            {
                System.out.print("Czekaj co");
                break;
            }
        }
        return new Vector2d(0,0);
    }

    public MapDirection next()
    {
        switch(this)
        {
            case NORTH:
                return NORTHEAST;
            case NORTHEAST:
                return EAST;
            case EAST:
                return SOUTHEAST;
            case SOUTHEAST:
                return SOUTH;
            case SOUTH:
                return SOUTHWEST;
            case SOUTHWEST:
                return WEST;
            case WEST:
                return NORTHWEST;
            case NORTHWEST:
                return NORTH;
            default: return ERROR;
        }
    }
}
