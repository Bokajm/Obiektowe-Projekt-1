package agh.cs.lab2;

public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return "(" + x + "," +  y +")";
    }

    public boolean precedes(Vector2d other)
    {
        if(other.x >= this.x && other.y >= this.y) return true;
        else return false;
    }

    public boolean follows(Vector2d other)
    {
        if(other.x <= this.x && other.y <= this.y) return true;
        else return false;
    }

    public Vector2d upperRight(Vector2d other)
    {
        int nx, ny;
        if(other.x > this.x) nx = other.x;
        else nx = this.x;
        if(other.y > this.y) ny = other.y;
        else ny = this.y;

        Vector2d wynik = new Vector2d(nx, ny);
        return wynik;
    }

    public Vector2d lowerLeft(Vector2d other)
    {
        int nx, ny;
        if(other.x < this.x) nx = other.x;
        else nx = this.x;
        if(other.y < this.y) ny = other.y;
        else ny = this.y;

        Vector2d wynik = new Vector2d(nx, ny);
        return wynik;
    }

    public Vector2d add(Vector2d other)
    {
        int nx, ny;
        nx = other.x + this.x;
        ny = other.y + this.y;

        Vector2d wynik = new Vector2d(nx, ny);
        return wynik;
    }

    public Vector2d subtract(Vector2d other)
    {
        int nx, ny;
        nx = this.x - other.x;
        ny = this.y - other.y;

        Vector2d wynik = new Vector2d(nx, ny);
        return wynik;
    }

    public boolean equals(Object other)
    {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return (this.x == that.x && this.y == that.y);
    }

    public Vector2d opposite()
    {
        Vector2d wynik = new Vector2d(-this.x, -this.y);
        return wynik;
    }

    public Vector2d randomAdj()
    {
        int random = (int) (Math.random() * 10000 % 8 );
        switch(random)
        {
            case 0:
            {
                return this.add(new Vector2d(1,1));
            }
            case 1:
            {
                return this.add(new Vector2d(0,1));
            }
            case 2:
            {
                return this.add(new Vector2d(1,0));
            }
            case 3:
            {
                return this.add(new Vector2d(-1,1));
            }
            case 4:
            {
                return this.add(new Vector2d(-1,-1));
            }
            case 5:
            {
                return this.add(new Vector2d(0,-1));
            }
            case 6:
            {
                return this.add(new Vector2d(-1,0));
            }
            case 7:
            {
                return this.add(new Vector2d(1,-1));
            }
        }
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

}
