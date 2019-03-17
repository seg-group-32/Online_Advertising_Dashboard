package Backend;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.File;
import java.util.*;

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


    public LineChart <String,Number> calculateChart(MetricType metricType, DateType dateType, Date startDate){
        Chart chart = new Chart(impressionData, serverData, clickData, startDate, bounceType, bounceInterval, metricType);
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis,yAxis);

        xAxis.setLabel(decideXaxis(dateType));
        yAxis.setLabel(decideYaxis(metricType));

        XYChart.Series series = new XYChart.Series();
        series.setName(this.getName());

        switch (dateType) {
            case Hour:
                chart.splitByHour();
                break;
            case Day:
                chart.splitByDay();
                break;
            case Week:
                chart.splitByWeek();
                break;
        }

        series = this.generateSeries(chart);
        lineChart.getData().add(series);
        return lineChart;
    }

    private String decideYaxis(MetricType metricType){
        switch (metricType){
            case CPA:
                return "Cost per acquisition";
            case CPC:
                return "Cost per click";
            case NOCLICK:
                return "Number of clicks";
            case CPM:
                return "Cost per thousand impressions";
            case CTR:
                return "Click through rate";
            case NOIMP:
                return "Number of impressions";
            case NOUNI:
                return "Number of Uniques";
            case NOCONVO:
                return "Number of conversions";
            case NOBOUNCE:
                return "Number of bounces";
            case TOTALCOST:
                return "Total Cost";
            case BOUNCERATE:
                return "Bounce rate";
        }
        return  null;
    }

    private String decideXaxis(DateType dateType){
        switch (dateType){
            case Day:
                return "Days";
            case Hour:
                return "Hour";
            case Week:
                return "Week";
        }
        return null;
    }

    private XYChart.Series generateSeries (Chart chart){
        XYChart.Series dataSeries1 = new XYChart.Series();
       while (!chart.getMetrics().isEmpty()){
           Date date = chart.getMetrics().keySet().iterator().next();
           for (Date key : chart.getMetrics().keySet()){
                if (key.before(date)){
                    date = key;
                }
           }
            dataSeries1.getData().add(new XYChart.Data(date.toString(),chart.getMetrics().get(date)));
            chart.getMetrics().remove(date);
       }

       return dataSeries1;
    }

    public static void main (String[] args) throws Exception{
        Campaign campaign = new Campaign();
        Date startDate = new Date(115,0,1,0,0,0);
        Date endDate = new Date();
        ImpressionData impressionEntries = new ImpressionData();
        ClickData clickData = new ClickData();
        ServerData serverData = new ServerData();
        Intepreter intepreter = new Intepreter();
        ImpressionEntry impressionEntry = intepreter.interpretImpressionLog("2015-01-01 12:01:18,8895519749317550080,Female,25-34,High,Shopping,0.000000");
        ServerEntry serverEntry = intepreter.interpretServerLog("2015-01-01 12:01:21,8895519749317550080,2015-01-01 12:05:13,7,No");
        ClickEntry clickEntry = intepreter.interpretClickLog("2015-01-01 12:01:21,8895519749317550080,11.794442");
        impressionEntries.add(impressionEntry);

        impressionEntry = intepreter.interpretImpressionLog("2015-01-04 13:21:30,2655802266942648320,Male,25-34,Low,Social Media,0.000000");
        impressionEntries.add(impressionEntry);

        impressionEntry = intepreter.interpretImpressionLog("2015-01-06 13:21:33,8408589459372142592,Female,45-54,Low,Blog,0.000000");

        impressionEntries.add(impressionEntry);

        serverData.add(serverEntry);

        serverEntry = intepreter.interpretServerLog("2015-01-04 13:11:57,7145773486672768000,2015-01-01 13:14:00,7,No");
        serverData.add(serverEntry);

        serverEntry = intepreter.interpretServerLog("2015-01-06 13:13:30,964547922247517184,2015-01-01 13:15:16,4,No");
        serverData.add(serverEntry);


        clickData.add(clickEntry);
        clickEntry = intepreter.interpretClickLog("2015-01-04 13:13:29,964547922247517184,0.000000");
        clickData.add(clickEntry);
        clickEntry = intepreter.interpretClickLog("2015-01-06 13:17:29,792431365104724992,0.000000");
        clickData.add(clickEntry);

        Chart chart = new Chart(impressionEntries,serverData,clickData,startDate,BounceType.Pages, 5, MetricType.NOCLICK);
        chart.splitByHour();
        XYChart.Series series = campaign.generateSeries(chart);
        System.out.println(series.toString());
    }



//    public CampaignMetrics makeFilter(boolean[] cases, Date startDate, Date endDate) {
//        Filter filter = new Filter(impressionData, clickData, serverData);
//
//        ImpressionData newImpressionData = filter.calculateFilters(cases, startDate, endDate);
//        ClickData newClickData = filter.filterClicks(newImpressionData,startDate, endDate);
//        ServerData newServerData = filter.filterServer(newClickData, startDate, endDate);
//
//        CampaignMetrics filteredMetrics = new CampaignMetrics();
//        filteredMetrics.calculateMetrics(newImpressionData, newClickData, newServerData);
//
//        if (bounceType != null) {
//            if (bounceType.equals(Time)) {
//                filteredMetrics.calcBounceTime(newServerData, bounceInterval);
//            }
//
//            if (bounceType.equals(Pages)) {
//                filteredMetrics.calcBouncePages(newServerData, bounceInterval);
//            }
//        }
//
//
//
//        return filteredMetrics;
//
//
//    }
}
