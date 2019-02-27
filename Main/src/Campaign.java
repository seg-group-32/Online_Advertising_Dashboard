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
        clickEntries.remove(0);
		for(String s:clickEntries){
         clickData.add(intepreter.interpretClickLog(s));
        }
	}
	
	/** This method calculates the key metrics of the campaign, by instantiating all the class variables of the CampaignMetrics
	 * instance owned by this CampaignClass. Those class variables represent the metrics of the Campaign. 
	 */
	public void calculateMetrics() {
		metrics.calculateMetrics(this.impressionData, this.clickData, this.serverData);
	}


	
	public void loadImpressionData () throws Exception {
		impressionData = new ImpressionData();
		impressionEntries.remove(0);
		for (String s: impressionEntries){
		    impressionData.add(intepreter.interpretImpressionLog(s));
        }
	}

	public void loadServerData() throws Exception {
        serverData = new ServerData();
        serverEntries.remove(0);
        for (String s: serverEntries){
            serverData.add(intepreter.interpretServerLog(s));
        }
	}

    public ArrayList<String> getClickEntries() {
        return clickEntries;
    }

    public ArrayList<String> getImpressionEntries() {
        return impressionEntries;
    }

    public ArrayList<String> getServerEntries() {
        return serverEntries;
    }

    public void setClickEntries(ArrayList<String> clickEntries) {
        this.clickEntries = clickEntries;
    }

    public void setImpressionEntries(ArrayList<String> impressionEntries) {
        this.impressionEntries = impressionEntries;
    }

    public void setServerEntries(ArrayList<String> serverEntries) {
        this.serverEntries = serverEntries;
    }

    public CampaignMetrics getMetrics() {
        return metrics;
    }
}
