package Location;

import GraphicalUI.Position;
import Marker.Marker;
import GraphicalUI.LocationInfo;

public abstract class Location {

    private final String name;
    private final Position position;
    private final Marker marker;
    private final Category category;
    private final LocationInfo locationInfo;
    private boolean marked = false;
    private boolean unfolded = false;


    public Location (String name, Position position, Category category, LocationInfo locationInfo, boolean marked, boolean unfolded){
        this.name = name;
        this.position = position;
        this.marked = marked;
        this.category = category;
        this.locationInfo = locationInfo;
        this.marker = createMarker();
        this.unfolded = unfolded;

    }

    public Category getCategory(){
        return category;
    }

    private Marker createMarker(){
        return new Marker(position.getX()- 25, position.getY() - 50, category, locationInfo, this);
    }

    public boolean getMarked(){
        return marked;
    }

    public boolean getUnfolded(){
        return unfolded;
    }

    public void setMarked(boolean marked){
        this.marked = marked;
    }
    public void setUnfolded(boolean unfolded){
        this.unfolded = unfolded;
    }

    public Position getPosition() {
        return position;
    }

   public Marker getMarker() {
        return marker;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return name +" " + position + " " + category;
    }
}