// TODO: Auto-generated Javadoc
// ListIterater can be used to look at the contents of the floor queues for 
// debug/display purposes...
 // Owned by Jimin Park
// TODO: Auto-generated Javadoc

/**
 * The Class Floor.
 */
public class Floor {
	/** The Constant UP. */
	private final static int UP =1;
	
	/** The Constant DOWN. */
	private final static int DOWN = -1;
	
	/** The down. */
	// queues to track the up requests and down requests on each floor
	private GenericQueue<Passengers> down;
	
	/** The up. */
	private GenericQueue<Passengers> up;

	/**
	 * Instantiates a new floor.
	 *
	 * @param qSize the q size
	 */
	public Floor(int qSize) {
		down = new GenericQueue<Passengers>(qSize);
		up = new GenericQueue<Passengers>(qSize);
	}
	
	
	
	
	
	/**
	 * Adds the que.
	 *
	 * @param direction the direction
	 * @param p the p
	 */
	public void addQue(int direction, Passengers p) {
		if (direction == UP) {
			this.addUpQueue(p);
		}
		else {
			this.addDownQueue(p);
		}
	}
	
	/**
	 * Gets the que size.
	 *
	 * @param direction the direction
	 * @return the que size
	 */
	public int getQueSize(int direction) {
		if (direction == UP) {
			return getUpSize();
		}
		else {
			return getDownSize();
		}
		
	}
	// TODO: Write the helper methods for this class. 
	//       The building will need to be able to manipulate the
	//       up and down queues for each floor.... 
	//       This includes accessing all of the lower level queue
	//       methods as well as possibly accessing the contents of each
	/**
	 * Gets the up size.
	 *
	 * @return the up size
	 */
	//       queue
	public int getUpSize() {
		return up.size();
	}
	
	/**
	 * Gets the down size.
	 *
	 * @return the down size
	 */
	public int getDownSize() {
		return down.size();
	}
	
	//give head remove head, add to end of queue
	/**
	 * Gets the head.
	 *
	 * @param direction — positive (or 0) number gets "up" queue head, negative number gets "down" queue head
	 * @return the respective head
	 */
	public Passengers getHead(int direction) {
		return direction > 0 ? up.peek() : down.peek();
	}
	
	/**
	 * Removes the head.
	 *
	 * @param direction the direction
	 * @return the passengers
	 */
	public Passengers removeHead(int direction) {
		if(direction > 0) {
			return up.remove();
		}
		else {
			return down.remove();
		}
	}

	/**
	 * Gets the down contents.
	 *
	 * @return the down contents
	 */
	public String getDownContents() {
		return down.toString();
	}
	
	/**
	 * Gets the down contents.
	 *
	 * @return the down contents
	 */
	public String getUpContents() {
		return up.toString();
	}
	
	/**
	 * Adds passengers to the up queue.
	 *
	 * @param p the passengers
	 */
	public void addUpQueue(Passengers p) {
		up.add(p);
	}
	
	/**
	 * Adds passengers to the down queue.
	 *
	 * @param p the passengers
	 */
	public void addDownQueue(Passengers p) {
		down.add(p);
	}
}
