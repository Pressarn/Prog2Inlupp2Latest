package Location;

import GraphicalUI.LocationInfo;
import GraphicalUI.Position;
import Marker.Marker;


public class Named extends Location {

    public Named(String name, Position position, Marker marker, Category typ, LocationInfo locationInfo) {
        super(name, position, marker, typ, locationInfo);

    }
}