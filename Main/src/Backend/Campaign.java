package Backend;

import java.io.File;
import java.util.ArrayList;

public class Campaign {

    private BounceType bounceType = null;
    private Integer bounceInterval = null;

    private boolean isImpressionLoaded = false;
    private boolean isClickLoaded = false;
    private boolean isServerLoaded = false;

    private File impressionFile = null;
    private File serverFile = null;
    private File clickFile = null;

    private String name = null;
	
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
		isClickLoaded = true;
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
		isImpressionLoaded = true;
	}

	public void loadServerData() throws Exception {
        serverData = new ServerData();
        serverEntries.remove(0);
        for (String s: serverEntries){
            serverData.add(intepreter.interpretServerLog(s));
        }
        isServerLoaded = true;
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

    private void setClickEntries(ArrayList<String> clickEntries) {
        this.clickEntries = clickEntries;
    }

    private void setImpressionEntries(ArrayList<String> impressionEntries) {
        this.impressionEntries = impressionEntries;
    }

    private void setServerEntries(ArrayList<String> serverEntries) {
        this.serverEntries = serverEntries;
    }

    public CampaignMetrics getMetrics() {
        return metrics;
    }

    public void setName(String name) {
	    this.name = name;
    }

    public String getName() {
	    return this.name;
    }

    /** I made the following six methods to connect nicely with the multiple campaign data and controller.
     * @Author Jacob
     */
    public void setImpressionFile(File f) {
	    impressionFile = f;
        String path = f.getPath();
        CSVReader reader = new CSVReader();
        this.setImpressionEntries(reader.fetchInfo(path));
        try {
            this.loadImpressionData();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setClickFile(File f) {
	    clickFile = f;
        String path = f.getPath();
        CSVReader reader = new CSVReader();
        this.setClickEntries(reader.fetchInfo(path));
        try {
            this.loadClickData();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setServerFile(File f) {
	    serverFile = f;
        String path = f.getPath();
        CSVReader reader = new CSVReader();
        this.setServerEntries(reader.fetchInfo(path));
        try {
            this.loadServerData();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public File getImpressionFile() {
	    return impressionFile;
    }

    public File getClickFile() {
	    return clickFile;
    }

    public File getServerFile() {
	    return serverFile;
    }

    public void loadBounceData() {
        //All methods in Campaign check if the valid files are loaded.
        if(bounceType == BounceType.Time) {
            loadBounceTime(bounceInterval);
            loadBounceRate();
        } else if(bounceType == BounceType.Pages) {
            loadBouncePages(bounceInterval);
            loadBounceRate();
        }
    }

    private void loadBounceTime(Integer time){
        if(serverData != null)
            metrics.calcBounceTime(serverData,time);
    }

    private void loadBouncePages(Integer noPages) {
        if(serverData != null)
            metrics.calcBouncePages(serverData, noPages);
    }

    private void loadBounceRate() {
        if(clickData != null && serverData != null)
            metrics.calcBounceRate();
    }

    public boolean isClickLoaded() {
        return isClickLoaded;
    }

    public boolean isImpressionLoaded() {
        return isImpressionLoaded;
    }

    public boolean isServerLoaded() {
        return isServerLoaded;
    }

    public void setBounceType(BounceType b) {
        bounceType = b;
    }

    public void setBounceInterval(Integer i) {
        bounceInterval = i;
    }

    public boolean isBounceSet() {
        if(bounceType != null) {
            return true;
        } else {
            return false;
        }
    }
}
