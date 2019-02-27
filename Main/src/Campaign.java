import java.util.ArrayList;

public class Campaign {
	
	private ClickData clickData;
	private ImpressionData impressionData;
	private ServerData serverData;
    private ArrayList<String> clickEntries;
    private ArrayList<String> serverEntries;
    private ArrayList<String> impressionEntries;
    private CampaignMetrics metrics;
	private Intepreter intepreter = new Intepreter();

	public Campaign() {
		clickData = null;
		impressionData = null;
		serverData = null;
		metrics = new CampaignMetrics();
	}
	
	public void loadClickData()throws Exception{
        clickData = new ClickData();
		for(String s:clickEntries){
         clickData.add(intepreter.interpretClickLog(s));
        }
	}
	
	/** This method calculates the key metrics of the campaign, by instantiating all the class variables of the CampaignMetrics
	 * instance owned by this CampaignClass. Those class variables represent the metrics of the Campaign. 
	 */
	private void calculateMetrics() {
		metrics.calculateMetrics(this.impressionData, this.clickData, this.serverData);
	}

    public static void main(String[] args) throws Exception{
        Campaign campaign = new Campaign();

        CSVReader csvReader = new CSVReader();
        campaign.clickEntries = csvReader.fetchInfo();
        campaign.clickEntries.remove(0);

        campaign.loadClickData();
        for (ClickEntry clickEntry : campaign.clickData){
            System.out.println("ID: "+clickEntry.getID()+" Click Cost: "+clickEntry.getClickCost()+" Date and Time: "+clickEntry.getDateAndTime());
        }

        campaign.loadImpressionData();
        campaign.loadServerData();
	}
	
	public void loadImpressionData () throws Exception {
		impressionData = new ImpressionData();
		for (String s: impressionEntries){
		    impressionData.add(intepreter.interpretImpressionLog(s));
        }
	}

	public void loadServerData() throws Exception {
        serverData = new ServerData();
        for (String s: serverEntries){
            serverData.add(intepreter.interpretServerLog(s));
        }
	}


}
