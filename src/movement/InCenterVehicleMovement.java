/* 
 * Copyright 2007 TKK/Netlab
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package movement;

import core.Coord;
import java.util.List;

import core.Settings;
import movement.map.DijkstraPathFinder;
import movement.map.MapNode;
import movement.map.SimMap;

/**
 * Map based movement model that uses Dijkstra's algorithm to find shortest
 * paths between two random map nodes and Points Of Interest
 */
public class InCenterVehicleMovement extends MapBasedMovement{
    
    /** the Dijkstra shortest path finder */
    private DijkstraPathFinder pathFinder;    
    private SimMap map;    
    private String targets[];       
    private String homeCenterType;       
    private MapNode home;       
    private boolean isReturn;
    
    private static int nextTarget = 0;
    
    private MapNode targetCenter;
	/**
	 * Creates a new movement model based on a Settings object's settings.
	 * @param settings The Settings object where the settings are read from
	 */
	public InCenterVehicleMovement(Settings settings) {
            super(settings);
            
            this.homeCenterType = settings.getSetting("homeCenterType");            
            this.targets = settings.getCsvSetting("targetCenters");
            
            this.map = getMap();
            this.pathFinder = new DijkstraPathFinder(getOkMapNodeTypes());
            this.isReturn = false;     
	}
	
	/**
	 * Copyconstructor.
	 * @param mbm The ShortestPathMapBasedMovement prototype to base 
	 * the new object to 
	 */
	protected InCenterVehicleMovement(InCenterVehicleMovement rem){
            
            super(rem);
            this.pathFinder = rem.pathFinder;	
            this.map = rem.map;
            this.homeCenterType = rem.homeCenterType;
            this.targets = rem.targets;
            this.home = getHome();            
            this.isReturn = rem.isReturn;
            
            nextTarget = 0;
	}
        
    private MapNode getHome()
    {        
        if(DisasterMovement.getCenters().get(homeCenterType) == null)
            return null;
        MapNode h;

        List<MapNode> centers = DisasterMovement.getCenters().get(homeCenterType);
        h = centers.get(rng.nextInt(centers.size()));
        
        if (homeCenterType.compareTo("mainreliefcenter") == 0)
        {            
            List<MapNode> trgs = DisasterMovement.getCenters().get(targets[0]);            
            //targetCenter = trgs.get(rng.nextInt(trgs.size()));            
            targetCenter = trgs.get(nextTarget++);
        }
        
        return h;
    }
      
	@Override
	public Path getPath() {
            Path p = new Path(generateSpeed());
            MapNode to = null;
            
            //System.out.println("home " + homePoints);
            if (isReturn)
            {   
                to = home;
                isReturn = false;
            }
            else
            {   
                String target = targets[rng.nextInt(targets.length)];
                List<MapNode> targetCenters = DisasterMovement.getCenters().get(target);
                
                if(targetCenters == null)
                    to = home;
                else
                do{
                    //if (homeCenterType.compareTo("mainreliefcenter") == 0)
                    //{
                    //    to = targetCenter;
                    //    break;
                    //}
                    //else
                        to = targetCenters.get(rng.nextInt(targetCenters.size()));
                }while(to.equals(home));
                
                isReturn = true;                        
            }
		
            //System.out.println("Moving from " + lastMapNode + " to " + to + " " + isReturn);
        
            List<MapNode> nodePath = pathFinder.getShortestPath(lastMapNode, to);
            
            if (nodePath == null)
                return null;
            
            // this assertion should never fire if the map is checked in read phase
            assert nodePath.size() > 0 : "No path from " + lastMapNode + " to " +
                    to + ". The simulation map isn't fully connected";

            for (MapNode node : nodePath) { // create a Path from the shortest path
                    p.addWaypoint(node.getLocation());
            }

            lastMapNode = to;

            return p;
	}	
    
       
    @Override
    public Coord getInitialLocation() 
    {        
        lastMapNode = home;       
        return home.getLocation().clone();
    }
    
	@Override
	public InCenterVehicleMovement replicate() {
		return new InCenterVehicleMovement(this);
	}
}
