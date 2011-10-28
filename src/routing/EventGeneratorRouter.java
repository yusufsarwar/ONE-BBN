/* 
 * Copyright 2008 TKK/ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Connection;
import core.Coord;
import core.Message;
import core.Settings;
import core.SimClock;

/**
 * Passive router that doesn't send anything unless commanded. This is useful
 * for external event -controlled routing or dummy nodes.
 * For implementation specifics, see MessageRouter class.
 */
public class EventGeneratorRouter extends ActiveRouter {

	private Random rng = new Random(1000);
	private static final int eventSeparator = 100; 
	private static final int eventRadius = 500;
	private static final int noOfEvents = 5;
	private static final int packetsPerEvent = 5;
	
	public EventGeneratorRouter(Settings s) {
		super(s);
	}

	/**
	 * Copy-constructor.
	 * @param r Router to copy the settings from.
	 */
	protected EventGeneratorRouter(EventGeneratorRouter r) {
		super(r);
	}

	@Override
	public void update() {
		// nothing to update..
		
		if (true)
			return;
		if (SimClock.getIntTime() == 0)
		{
			System.out.println("$node(" + this.getHost().getAddress() + ") set X_ " + this.getHost().getLocation().getX());
			System.out.println("$node(" + this.getHost().getAddress() + ") set Y_ " + this.getHost().getLocation().getY());
		}
		else{
			System.out.println("$ns_ at " + SimClock.getTime() + " $node(" + this.getHost().getAddress() + ") set X_ " + this.getHost().getLocation().getX());
			System.out.println("$ns_ at " + SimClock.getTime() + " $node(" + this.getHost().getAddress() + ") set Y_ " + this.getHost().getLocation().getY());
		}
	}

	@Override
	public void changedConnection(Connection con) {
		// -"-
	}
	
	@Override
	public MessageRouter replicate() {
		return new EventGeneratorRouter(this);
	}

	@Override
	public boolean createNewMessage(Message m) {
		// TODO Auto-generated method stub
		//return super.createNewMessage(m);
		
		// MgcUI AddTfc    <node_id>   <time> <end_time> <interval> <dest> <size> [-meta <tag> <data>]
		// MgcUI   AddTfc          0       0.0 0.0 0.0 0 5 -meta x 0.0 -meta y 0.0";
		
		Coord loc = this.getHost().getLocation();
		List<Coord> eventList = new ArrayList<Coord>();
		
		double startTime = SimClock.getTime();
		double endTime = SimClock.getTime() + 100.0;
		double interval = 10.0;
		int size = 1500;
		
		for (int eventNo = 0; eventNo < noOfEvents; eventNo++)
		{
			boolean again = false;
			Coord eventLoc = null;
			
			do{
				eventLoc = new Coord(loc.getX() + rng.nextInt(eventRadius), loc.getY() + rng.nextInt(eventRadius));
				
				again = false;
				for (Coord c : eventList)
				{
					if (eventLoc.distance(c) < eventSeparator)
						again = true;
				}
			} while (again);
			
			eventList.add(eventLoc);
			
			for (int k = 0 ;k < packetsPerEvent; k++)
			{
				System.out.println("MgcUI AddTfc " + this.getHost().getAddress() + " " + startTime + " " + endTime + " " + interval +
						" " + size + " -meta x " + 	eventLoc.getX() + " -meta y " + eventLoc.getY() + " -meta g " + this.getHost().getAddress() +":" + eventNo);
			}
		}
		
		return true;
	}	
}
