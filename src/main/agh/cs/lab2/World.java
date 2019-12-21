package agh.cs.lab2;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class World {

        public static void main(String[] args) throws InterruptedException
        {
                int width = 0, height = 0, startEnergy = 0, moveEnergy = 0, plantEnergy = 0;
                double jungleRatio = 0;

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();

                try {
                        JsonElement configElem = parser.parse(new FileReader("/C:/lab.proj/data.json")); //add file
                        JsonObject config = (JsonObject) configElem;
                        width = config.get("width").getAsInt();
                        height = config.get("height").getAsInt();
                        startEnergy = config.get("startEnergy").getAsInt();
                        moveEnergy = config.get("moveEnergy").getAsInt();
                        plantEnergy = config.get("plantEnergy").getAsInt();
                        jungleRatio = config.get("jungleRatio").getAsDouble();
                } catch (FileNotFoundException exc) {
                        System.out.println("Give me a proper file next time.");
                }

                GameMap map = new GameMap(width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio);
                for(int i=0;i<30;i++)
                        map.place(new Animal(map, startEnergy));

                for(int i=0;i<70;i++)
                {
                        System.out.println("Day " + map.getDay());
                        System.out.print(map.toString());
                        map.liveToSeeAnotherDay();
                }
        }
}

