package agh.cs.lab2;

import java.util.ArrayList;
import java.util.List;

public class Animal{ //, Comparable<Animal>
    public boolean alive;
    private int dayBorn;
    private int currentDay;
    private MapDirection direction;
    private Vector2d position;
    private GameMap map;
    private Genes race;
    private int juicesLeft;
    private List<IPositionChangeObserver> observers = new ArrayList<>();
    public ArrayList<Animal> ancestors = new ArrayList<>();
    public ArrayList<Animal> descendants = new ArrayList<>();
    public ArrayList<Animal> kids = new ArrayList<>();

    //Create Brand New Specimen
    public Animal(GameMap map, int juices)
    {
        this.alive = true;
        this.map = map;
        this.dayBorn = map.getDay();
        this.currentDay = this.dayBorn;
        int initX = (int) (Math.random() * 10000 % map.width );
        int initY = (int) (Math.random() * 10000 % map.height );
        while(map.animalOn(new Vector2d(initX, initY)))
        {
            initX = (int) (Math.random() * 10000 % map.width );
            initY = (int) (Math.random() * 10000 % map.height );
        }
        this.position = new Vector2d(initX, initY);
        this.juicesLeft = juices;
        this.race = new Genes();
        this.direction = MapDirection.NORTH;
        youSpinMeRightRound(race.getRandomGene());

    }

    //Create a Fruit of Love
    public Animal(GameMap map, Vector2d InitialPosition, Animal mommy, Animal daddy) {
        this.alive = true;
        this.map = map;
        this.dayBorn = map.getDay();
        this.currentDay = this.dayBorn;
        this.direction = MapDirection.NORTH;
        this.position = InitialPosition;
        this.race = new Genes(mommy.race, daddy.race);
        this.juicesLeft = mommy.juicesLeft/4 + daddy.juicesLeft/4;
        this.ancestors.add(mommy);                 //add first parent and their ancestors as ancestors
        for(int i=0;i<mommy.ancestors.size();i++)
        {
            mommy.ancestors.get(i).descendants.add(this);
        }
        this.ancestors.add(daddy);                 //add second parent and their ancestors as ancestors
        for(int i=0;i<daddy.ancestors.size();i++)
        {
            daddy.ancestors.get(i).descendants.add(this);
        }
        mommy.kids.add(this);
        daddy.kids.add(this);
        mommy.loseTheJuice(mommy.juicesLeft/4);
        daddy.loseTheJuice(daddy.juicesLeft/4);
    }

    public int howManyKids() { return this.kids.size(); }
    public int howManyDescendants() { return this.descendants.size(); }
    public String whenDied()
    {
        if(this.alive == false)
        {
            return "I have lived many beautiful springs, saw so much, but at last I rested, at the age of " + getAge() + ".";
        }
        return "What";
    }

    //Lose Energy
    public void loseTheJuice(int juicesLost)      //return true if dies
    {
        this.juicesLeft -= juicesLost;
    }

    //Receive Energy (sometimes called eating)
    public int expandJuice(int juicesGottition)
    {
        this.juicesLeft += juicesGottition;
        currentDay++;
        return this.juicesLeft;
    }

    public Vector2d getPosition() { return this.position; }

    public int getChaddery() { return this.juicesLeft; }

    public int getAge() { return this.currentDay-this.dayBorn; }

    public String badToString()
    {
        return "(" + this.position.x + "," +  this.position.y + ")";
    }

    public String toString() {
        switch (this.direction) {
            case NORTH:
                return "N";
            case NORTHEAST:
                return "Y";
                //return "NE";
            case EAST:
                return "E";
            case SOUTHEAST:
                return "M";
                //return "SE";
            case SOUTH:
                return "S";
            case SOUTHWEST:
                return "C";
                //return "SW";
            case WEST:
                return "W";
            case NORTHWEST:
                return "A";
                //return "NW";
            case ERROR:
                return "What";
        }
        return "How did that happen";
    }

    public MapDirection facing() {
        return direction;
    }

    //Randomize Direction Using Genes
    public void youSpinMeRightRound(int spinnery)
    {
        for(int i=0; i<spinnery; i++)
        {
            this.direction = this.direction.next();
        }
    }

    //Rotate and Move
    public void move()
    {
        youSpinMeRightRound(this.race.getRandomGene());
        Vector2d oldPosition = this.position;
        this.position = this.position.add(this.direction.toUnitVector());
        positionChanged(oldPosition);
        this.currentDay++;
    }

    public void blink(Vector2d position)
    {
        Vector2d oldPosition = this.position;
        this.position = position;
        positionChanged(oldPosition);
    }

    //THE CYCLE OF LIFE AND DEATH CONTINUES
    //---------------------------------------------------------------------
    public boolean shouldDie()
    {
        if(juicesLeft<=0) return true;
        return false;
    }

    public void die()
    {
        this.alive = false;
        ohWhatAHorribleTragedyDeathIs();
    }
    //---------------------------------------------------------------------


    //OBSERVER RELATED
    //---------------------------------------------------------------------
    public void addObserver(IPositionChangeObserver observer)
    {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer)
    {
        observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition)
    {
        for(IPositionChangeObserver observer : observers)
        {
            observer.positionChanged(oldPosition,this);
        }
    }

    public void ohWhatAHorribleTragedyDeathIs() {
        for (IPositionChangeObserver observer : observers) {
            observer.ohWhatAHorribleTragedyDeathIs(this.position, this);
        }
    }
    //---------------------------------------------------------------------

}