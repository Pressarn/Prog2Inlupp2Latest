package Location;

import GraphicalUI.LocationInfo;
import GraphicalUI.Position;
import Marker.Marker;


public class Described extends Location {

    public final String description;

    public Described(String name, Position position, Marker marker, Category typ, String description, LocationInfo locationInfo) {
        super(name, position, marker, typ, locationInfo);
        this.description = description;
    }

    public String toString(){
        return name + " " + position + " " + category + " " + description;
    }
}