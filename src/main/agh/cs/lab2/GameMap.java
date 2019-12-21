package agh.cs.lab2;

import java.lang.Math;
import java.util.*;


public class GameMap implements IWorldMap, IPositionChangeObserver {
    private int day;
    public int height;
    public int width;
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private int jungleHeight;
    private int jungleWidth;
    private Vector2d lowerLeftJungle;
    private Vector2d upperRightJungle;
    private int jungleBigness;           //how many tiles in jungle
    private int notJungleBigness;        //how many tiles not in jungle
    private int potatoJuice;             //energy from food
    private int juiceCost;               //move cost
    private int initialJuice;            //start energy
    private Map<Vector2d, Grass> weed = new HashMap<>();                     //grass map
    public ArrayList<Animal> animals = new ArrayList<>();                   //list of animals
    public ArrayList<Grass> weeds = new ArrayList<>();                      //list of grass
    public ArrayList<Animal> deadAnimals = new ArrayList<>();               //list of dead animals
    public HashMap<Vector2d, ArrayList<Animal>> map = new HashMap<>();      //map of tiles containing animals
    public MapVisualizer printer = new MapVisualizer(this);

    public GameMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio) {
        this.day = 0;
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.jungleWidth = (int) (((double) width) * jungleRatio);
        this.jungleHeight = (int) (((double) height) * jungleRatio);
        this.lowerLeftJungle = new Vector2d((width - jungleWidth + 1) / 2, (height - jungleHeight + 1) / 2);
        this.upperRightJungle = this.lowerLeftJungle.add(new Vector2d(jungleWidth, jungleHeight));
        this.potatoJuice = plantEnergy;
        this.juiceCost = moveEnergy;
        this.initialJuice = startEnergy;
        this.jungleBigness = this.jungleHeight * this.jungleWidth;
        this.notJungleBigness = this.height * this.width - jungleBigness;
        for (int i = 0; i < 35; i++) arise();
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public Vector2d getLowerLeftJungle() {
        return lowerLeftJungle;
    }

    public Vector2d getUpperRightJungle() {
        return upperRightJungle;
    }

    public int getDay() {
        return this.day;
    }

    public int plantsAlive() {
        return this.weeds.size();
    }

    public int animalsAlive() {
        return this.animals.size();
    }

    public int avgEnergy() {
        int allEnergy = 0;
        for (int i = 0; i < animals.size(); i++)
            allEnergy += animals.get(i).getChaddery();
        return allEnergy / animals.size();
    }

    public int avgLifeLength() {
        int allLife = 0;
        for (int i = 0; i < deadAnimals.size(); i++)
            allLife += deadAnimals.get(i).getAge();
        return allLife / deadAnimals.size();
    }

    public int avgKidsAmount()
    {
        int allKids =0;
        for(int i=0;i<animals.size();i++)
            allKids += animals.get(i).kids.size();
        return allKids/animals.size();
    }


    @Override
    public String toString() {
        return printer.draw(getLowerLeft(), getUpperRight());
    }

    @Override
    public Object objectAt(Vector2d position) {
        for(int i=0;i<animals.size();i++)
        {
            if(animals.get(i).getPosition().equals(position)) return animals.get(i);
        }
        for(int i=0;i<weeds.size();i++)
        {
            if(weeds.get(i).getPosition().equals(position)) return weeds.get(i);
        }
        return null;
    }

    public boolean isObjectAt(Vector2d position) {
        for(int i=0;i<animals.size();i++)
        {
            if(animals.get(i).getPosition().equals(position)) return true;
        }
        for(int i=0;i<weeds.size();i++)
        {
            if(weeds.get(i).getPosition().equals(position)) return true;
        }
        return false;
    }

    //Make One Day(time period) Cycle
    public void liveToSeeAnotherDay()
    {
        arise();
        invade();
        devour();
        spread();
        day++;
    }



    //JUNGLE RELATED
    //---------------------------------------------------------------------
    public boolean inTheJungle(Vector2d theLion)   //check if position is in the jungle area
    {
        if(theLion.precedes(upperRightJungle) && theLion.follows(lowerLeftJungle)) return true;
        return false;
    }

    public boolean isJungleFull()                            //check if jungle is full
    {
        boolean checker = true;
        for(int i=0;i<width-1;i++)
        {
            for(int j=0;j<height-1;j++)
            {
                Vector2d current = new Vector2d(i,j);
                if(inTheJungle(current) && !isThatWeed(current)) checker = false;
            }
        }
        return checker;
    }

    public boolean isNotJungleFull()                         //check if outside of jungle is full
    {
        boolean checker = true;
        for(int i=0;i<width-1;i++)
        {
            for(int j=0;j<height-1;j++)
            {
                Vector2d current = new Vector2d(i,j);
                if(!inTheJungle(current) && !isThatWeed(current)) checker = false;
            }
        }
        return checker;
    }
    //---------------------------------------------------------------------



    //WEED RELATED
    //---------------------------------------------------------------------
    public Vector2d placeGreens(Grass grass)        //put grass
    {
        weed.put(grass.getPosition(), grass);
        weeds.add(grass);
        return grass.getPosition();
    }

    private boolean isThatWeed(Vector2d where)      //check for grass o position
    {
        if (weed.get(where) != null) return true;
        return false;
    }

    private void arise(){                           //grow new grass on the map
        //grow grass outside the jungle
        int counter;
        if(!isNotJungleFull()) {
            for(counter = 0; counter < notJungleBigness; counter++) {
                Vector2d position = new Vector2d((int) (Math.random() * 10000 % (width-1)), (int) (Math.random() * 10000 % (height-1)));
                if (!inTheJungle(position) && !isObjectAt(position)) {
                    placeGreens(new Grass(position));
                    break;
                }
            }
            if(counter == notJungleBigness){
                boolean spawned;
                if(width > height){
                    do{
                        int row = (int) (Math.random() * height);
                        spawned = false;
                        for(int i = 0; i < width; i++){
                            Vector2d position = new Vector2d(i, row);
                            if(!inTheJungle(position) && !isObjectAt(position)){
                                placeGreens(new Grass(position));
                                spawned = true;
                                break;
                            }
                        }
                    }while(!spawned);

                } else{
                    do{
                        int column = (int) (Math.random() * width);
                        spawned = false;
                        for(int j = 0; j < height; j++){
                            Vector2d position = new Vector2d(column, j);
                            if(!inTheJungle(position) && !isObjectAt(position)){
                                placeGreens(new Grass(position));
                                spawned = true;
                                break;
                            }
                        }
                    }while(!spawned);
                }
            }
        }

        //grow grass in the jungle
        if(!isJungleFull()){
            for(counter = 0; counter < jungleBigness; counter++) {
                Vector2d position = lowerLeftJungle.add(new Vector2d((int) (Math.random() * 10000 % (jungleWidth)), (int) (Math.random() * 10000 % (jungleHeight))));
                if (!isObjectAt(position)) {
                    placeGreens(new Grass(position));
                    break;
                }
            }
            if(counter == jungleBigness){
                boolean spawned;
                if(width > height){
                    do{
                        int row = lowerLeftJungle.y + (int) (Math.random() * jungleHeight);
                        spawned = false;
                        for(int i = lowerLeftJungle.x; i <= upperRightJungle.x; i++){
                            Vector2d position = new Vector2d(i, row);
                            if(!isObjectAt(position)){
                                placeGreens(new Grass(position));
                                spawned = true;
                                break;
                            }
                        }
                    }while(!spawned);

                } else{
                    do{
                        int column = lowerLeftJungle.x + (int) (Math.random() * jungleWidth);
                        spawned = false;
                        for(int j = lowerLeftJungle.y; j <= upperRightJungle.y; j++){
                            Vector2d position = new Vector2d(column, j);
                            if(!isObjectAt(position)){
                                placeGreens(new Grass(position));
                                spawned = true;
                                break;
                            }
                        }
                    }while(!spawned);
                }
            }
        }

    }
    //---------------------------------------------------------------------



    //ANIMAL RELATED
    //---------------------------------------------------------------------
    public boolean animalOn(Vector2d position)
    {
        if(map.get(position)!=null)
        {
        List<Animal> checkPosition = map.get(position);
        if(checkPosition.size() == 0) return false;
        return true;
        }
        return false;
    }


    public boolean place(Animal animal)
    {
        if(map.get(animal.getPosition())==null)
            map.put(animal.getPosition(), new ArrayList<Animal>());
        if(!animalOn(animal.getPosition()))
        {
            animals.add(animal);;
            map.get(animal.getPosition()).add(animal);
            animal.addObserver(this);
            Vector2d tester = new Vector2d(0,0);
            tester.x = animal.getPosition().x;
            tester.y = animal.getPosition().y;
            if(!animal.getPosition().equals(getBackThereCoward(tester)))
                animal.blink(getBackThereCoward(animal.getPosition()));
            sorter(animal.getPosition());
            return true;
        }
        return false;
    }

    public Vector2d getBackThereCoward(Vector2d position){                  //get opposite position for leaving map
        Vector2d opposite = new Vector2d(position.x, position.y);
        if(opposite.x < 0){
            opposite.x += this.width;
        }
        if(opposite.y < 0){
            opposite.y += this.height;
        }
        if(opposite.x >= this.width){
            opposite.x -= this.width;
        }
        if(opposite.y >= this.height){
            opposite.y -= this.height;
        }
        return opposite;
    }

    //All Animals Move
    public void invade()
    {
        for(int i=0;i<animals.size();i++)
        {
            Animal fox = animals.get(i);
            fox.move();
            Vector2d tester = new Vector2d(0,0);
            tester.x = fox.getPosition().x;
            tester.y = fox.getPosition().y;
            if(!fox.getPosition().equals(getBackThereCoward(tester))) {
                fox.blink(getBackThereCoward(fox.getPosition()));    //if left map...
            }
            fox.loseTheJuice(juiceCost);
        }
    }

    //All Animals Eat
    public void devour()
    {
        for(int i=0;i<animals.size();i++) {
            Animal goat = animals.get(i);
            if (isThatWeed(goat.getPosition()))
            {
                ArrayList<Animal> animalTile = map.get(goat.getPosition());
                if(animalTile == null) return;
                if(animalTile.size() == 0) return;
                //if alone - eat
                if(animalTile.size()==1)
                {
                    weeds.remove(weed.get(goat.getPosition()));
                    weed.remove(goat.getPosition());
                    goat.expandJuice(potatoJuice);
                }
                //if not alone and top 1 the only one - top 1 eats
                else if(animalTile.get(0).getChaddery() > animalTile.get(1).getChaddery())
                {
                    weeds.remove(weed.get(goat.getPosition()));
                    weed.remove(goat.getPosition());
                    animalTile.get(0).expandJuice(potatoJuice);
                }
                //if not alone and more than one top 1 - they share
                else
                {
                    int howManyChads = 0;
                    weeds.remove(weed.get(goat.getPosition()));
                    weed.remove(goat.getPosition());
                    while((howManyChads < animalTile.size()) && (animalTile.get(howManyChads).getChaddery() == animalTile.get(0).getChaddery())) howManyChads++;
                    for(int j=0;j<howManyChads;j++)
                    {
                        animalTile.get(j).expandJuice(potatoJuice/howManyChads);
                    }
                }
            }
        }
        for(int i=0;i<animals.size();i++)
        {
            Animal fox = animals.get(i);
            if(fox.shouldDie()) fox.die();
        }
    }

    //All Animals Make Bebes
    public void spread()
    {
        ArrayList<Animal> bebes = new ArrayList<>();
        Iterator hmIterator = map.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            ArrayList<Animal> currentTile = map.get(mapElement.getKey());

            //If a tile has more than one animal and two top animals have enough energy and both animals are not newborns
            if(currentTile.size()>1 && currentTile.get(1).getChaddery() > initialJuice/2 && currentTile.get(0).getAge()>0 && currentTile.get(1).getAge()>0) {
                Animal bebe = new Animal(this, currentTile.get(0).getPosition().randomAdj(), currentTile.get(0), currentTile.get(1));
                bebes.add(bebe);
                currentTile.get(0).descendants.add(bebe);
                for (int i = 0; i < currentTile.get(0).ancestors.size(); i++)
                {
                    currentTile.get(0).ancestors.get(i).descendants.add(bebe);             //add to children of first parent and all their ancestors
                }
                currentTile.get(1).descendants.add(bebe);
                for (int i = 0; i < currentTile.get(1).ancestors.size(); i++)
                {
                    currentTile.get(1).ancestors.get(i).descendants.add(bebe);             //add to children of second parent and all their ancestors
                }
            }

        }
        for(int i=0;i<bebes.size();i++)
        {
            animals.add(bebes.get(i));
            place(bebes.get(i));
            sorter(bebes.get(i).getPosition());
        }
    }
    //---------------------------------------------------------------------



    //OBSERVER RELATED
    //---------------------------------------------------------------------
    public void positionChanged(Vector2d oldPosition, Animal seal)
    {
        map.get(oldPosition).remove(seal);
        if(map.get(seal.getPosition()) == null) map.put(seal.getPosition(), new ArrayList<Animal>());
        map.get(seal.getPosition()).add(seal);
        sorter(seal.getPosition());
    }

    public void ohWhatAHorribleTragedyDeathIs(Vector2d position, Animal walrus)
    {
        deadAnimals.add(walrus);
        animals.remove(walrus);
        map.get(position).remove(walrus);
        sorter(walrus.getPosition());
    }
    //---------------------------------------------------------------------

    public void sorter(Vector2d position)
    {
        if(map.get(position).size()>1)
        Collections.sort(map.get(position), new Comparator<Animal>()
                {
                    public int compare(Animal animal1, Animal animal2)
                    {
                        return Integer.valueOf(animal2.getChaddery()).compareTo(animal1.getChaddery());
                    }
                }
        );
    }
}