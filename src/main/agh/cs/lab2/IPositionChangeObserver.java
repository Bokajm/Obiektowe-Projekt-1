package agh.cs.lab2;

public interface IPositionChangeObserver
{
    public void positionChanged(Vector2d oldPosition, Animal animal);
    public void ohWhatAHorribleTragedyDeathIs(Vector2d position, Animal animal);
}