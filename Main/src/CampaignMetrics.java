import java.util.ArrayList;
import java.util.HashSet;

/** This class represents the key metrics of a campaign, as specified in the project specification
 * Each instance of Campaign will have an instance of CampaignMetrics
 * @author Sanjeevan
 *
 */
public class CampaignMetrics {
	private int noImpressions;
	private int noClicks;
	private int noUniques;
	private int noBounces;
	private int noConversions;
	private Float totalCost;
	private Float ctr;
	private Float cpa;
	private Float cpc;
	private Float cpm;
	private Float bounceRate;
	
	/** Public method used to calculate the metrics needed, passing in the relevent click, impression and server data
	 * 
	 * @param impressionData
	 * @param clickData
	 * @param serverData
	 */
	public void calculateMetrics(ImpressionData impressionData, ClickData clickData, ServerData serverData) {
		calculateNoImpressions(impressionData);
		calculateNoClicks(clickData);
		calculateUniques(clickData);
		// calculateNoBounces -- implement later
		calculateNoConversions(serverData);
		calculateTotalCost(clickData, impressionData);
		calculateCTR();
		calculateCPA();
		calculateCPC();
		calculateCPM();
		// calculateBounceRate() -- implement later
	}
	
	/** Calculate no. of impressions given impression data. The value is saved as a private class variable of CampaignMetrics.
	 * 
	 * @param impressionData
	 */
	private void calculateNoImpressions(ImpressionData impressionData) {
		this.noImpressions = impressionData.size();
	}
	
	/** Calculate no. of clicks given click data. The value is saved as a private class variable of CampaignMetrics. 
	 * 
	 * @param clickData
	 */
	private void calculateNoClicks(ClickData clickData) {
		this.noClicks = clickData.size();
	}
	
	/** Calculate no. of uniques given click data. The value is saved as a private class variable of CampaignMetrics.
	 * 
	 * @param clickData
	 */
	private void calculateUniques(ClickData clickData) {
		HashSet<String> uniqueClickIDs = new HashSet<String>();
		
		for (ClickEntry c : clickData) {
			uniqueClickIDs.add(c.getID());
		}
		
		this.noUniques = uniqueClickIDs.size();
	}
	
	/** Calculate no. of conversions given server data. The value is saved as a private class variable of CampaignMetrics.
	 * 
	 * @param serverData
	 */
	private void calculateNoConversions(ServerData serverData) {
		int currentConversionCounter = 0;
		
		for (ServerEntry s : serverData) {
			if (s.getConversion()) currentConversionCounter++;
		}
		
		this.noConversions = currentConversionCounter;
	}
	
	/** Calculate total cost of campaign, given click and impression data. The value is saved as a private class variable of CampaignMetrics.
	 * 
	 * @param clickData
	 * @param impressionData
	 */
	private void calculateTotalCost(ClickData clickData, ImpressionData impressionData) {
		Float totalCost = (float) 0.00;
		
		for (ClickEntry c : clickData) {
			totalCost += c.getClickCost();
		}
		
		for (ImpressionEntry i : impressionData) {
			totalCost += i.getImpressionCost();
		}
		
		this.totalCost = totalCost;
	}
	
	/** Calculate click-through rate. The value is saved as a private class variable of CampaignMetrics.
	 * 
	 */
	private void calculateCTR() {
		this.ctr = ((float) this.noClicks / (float) this.noImpressions);
	}
	
	/** Calculate cost-per-acquisition. The value is saved as a private class variable of CampaignMetrics.
	 * 
	 */
	private void calculateCPA() {
		this.cpa = this.totalCost / ((float) this.noConversions);
	}
	
	/** Calculate cost-per-click. The value is saved as a private class variable of CampaignMetrics.
	 * 
	 */
	private void calculateCPC() {
		this.cpc = this.totalCost / ((float) this.noClicks);
	}
	
	/** calculate cost-per-thousand impressions. The value is saved as a private class variable of CampaignMetrics.
	 * 
	 */
	private void calculateCPM() {
		float i = ((float) this.noImpressions) / 1000;
		this.cpm = this.totalCost / i;
	}
	
	/** Getter method for no. of impressions
	 * 
	 * @return
	 */
	public int getNoImpression() {
		return this.noImpressions;
	}
	
	/** Getter method for no. of clicks
	 * 
	 * @return
	 */
	public int getNoClicks() {
		return this.noClicks;
	}
	
	/** Getter method for no. of uniques
	 * 
	 * @return
	 */
	public int getNoUniques() {
		return this.noUniques;
	}
	
	/** Getter method for no. of conversions
	 * 
	 * @return
	 */
	public int getNoConversions() {
		return this.noConversions;
	}
	
	/** Getter method for total cost
	 * 
	 * @return
	 */
	public Float getTotalCost() {
		return this.totalCost;
	}
	
	/** Getter method for CTR
	 * 
	 * @return
	 */
	public Float getCTR() {
		return this.ctr;
	}
	
	/** Getter method for CPA
	 * 
	 * @return
	 */
	public Float getCPA() {
		return this.cpa;
	}
	
	/** Getter method for CPC
	 * 
	 */
	public Float getCPC() {
		return this.cpc;
	}
	
	/** Getter method for cpm
	 * 
	 * @return
	 */
	public Float getCPM() {
		return this.cpm;
	}
	


}