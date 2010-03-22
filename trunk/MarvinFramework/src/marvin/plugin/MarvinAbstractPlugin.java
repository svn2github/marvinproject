package marvin.plugin;

public class MarvinAbstractPlugin implements MarvinPlugin{

	private boolean valid;
	/**
	 * Ensures that this plug-in is working consistently to its attributes. 
	 */
	public void validate(){
		valid = true;
	}
	
	/**
	 * Invalidate this plug-in. It means that the attributes were changed and the plug-in needs to check whether
	 * or not change its behavior. 
	 */
	public void invalidate(){
		valid = false;
	}
	
	/**
	 * Determines whether this plug-in is valid. A plug-in is valid when it is correctly configured given a set
	 * of attributes. When an attribute is changed, the plug-in becomes invalid until the method validate() is
	 * called. 
	 * @return
	 */
	public boolean isValid(){
		return valid;
	}
}
