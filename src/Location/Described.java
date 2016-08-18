package Location;

import GraphicalUI.LocationInfo;
import GraphicalUI.Position;
import Marker.Marker;


public class Described extends Location {

    private final String description;

    public Described(String name, Position position, Category typ, String description, LocationInfo locationInfo, boolean marked, boolean folded) {
        super(name, position,  typ, locationInfo, marked, folded);
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}