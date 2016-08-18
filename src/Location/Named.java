package Location;

import GraphicalUI.LocationInfo;
import GraphicalUI.Position;
import Marker.Marker;


public class Named extends Location {

    public Named(String name, Position position, Category typ, LocationInfo locationInfo, boolean marked, boolean folded) {
        super(name, position,  typ, locationInfo, marked, folded);

    }
}