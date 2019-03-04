package Backend;

import java.util.ArrayList;

//CHANGED - JACOB

/** This class represents a managing class to manage multiple campaigns which may be compared by the user
 * 
 * @author Sanjeevan
 *
 */
public class CampaignManager {
	private ArrayList<Campaign> campaigns;

	public CampaignManager() {
		campaigns = new ArrayList<Campaign>();
	}
	
	/** A method which takes a Campain instance and adds it to the list of Campaigns managed by the CampaignManager
	 * 
	 * @param campaign
	 */
	public void addCampaign(Campaign campaign) {
		this.campaigns.add(campaign);
	}

	//Added by Jacob
	public ArrayList<Campaign> getCampaigns() { return campaigns; }
}
