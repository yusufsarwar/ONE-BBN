/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movement;

import core.Coord;
import core.Settings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import movement.map.MapNode;

/**
 *
 * @author sarwar
 */

public class DisasterMovement extends MovementModel{
    private static Map<String, List<MapNode>>centers;
    public static Coord disasterCenter;
    public static double criticalRadius;    
    public static double repairRate;
    private static boolean instantiated = false;
    public static double startTime;
    public static double intensity;
    public static double constant;
    
    public DisasterMovement(Settings settings)
    {        
        criticalRadius = Double.parseDouble(settings.getSetting("criticalRadius"));
        repairRate = Double.parseDouble(settings.getSetting("repairRate"));
        instantiate();
    }
    
    public static void instantiate()
    {
        if (instantiated) return;        
        centers = new HashMap<String, List<MapNode>>();
        disasterCenter = new Coord(2000,2000);        
        startTime = 3600.0;
        intensity = 5;
        constant = criticalRadius * criticalRadius / 10.0;        
        instantiated = true;
    }
    
    public static Map<String, List<MapNode>> getCenters()
    {
        instantiate();
        return centers;
    }
    
    public static void reset()
    {
        if (instantiated)
            centers.clear();
    }

    @Override
    public Path getPath() {
        return null;
    }

    @Override
    public Coord getInitialLocation() {
        return null;
    }

    @Override
    public MovementModel replicate() {
        return null;
    }
}
