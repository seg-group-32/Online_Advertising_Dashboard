
public class Campaign {
	
	private ClickData clickData;
	private ImpressionData impressionData;
	private ServerData serverData;
	
	public Campaign() {
		clickData = null;
		impressionData = null;
		serverData = null;
	}
	
	public void loadClickData(ClickData clickData) {
		this.clickData = clickData;
	}
	
	public void loadImpressionData(ImpressionData impressionData) {
		this.impressionData = impressionData;
	}

	public void loadServerData(ServerData serverData) {
		this.serverData = serverData;
	}
}
