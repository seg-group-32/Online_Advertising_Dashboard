import java.util.ArrayList;

public class Campaign {
	
	private ClickData clickData;
	private ImpressionData impressionData;
	private ServerData serverData;
    private ArrayList<String> clickEntries;
    private ArrayList<String> serverEntries;
    private ArrayList<String> impressionEntries;
	
	public Campaign() {
		clickData = null;
		impressionData = null;
		serverData = null;
	}
	
	public void loadClickData()throws Exception{
        clickData = new ClickData();
        Intepreter intepreter = new Intepreter();
		for(String s:clickEntries){
         clickData.add(intepreter.interpretClickLog(s));
        }
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
	}
	
	public void loadImpressionData(ImpressionData impressionData) {
		this.impressionData = impressionData;
	}

	public void loadServerData(ServerData serverData) {
		this.serverData = serverData;
	}


}
