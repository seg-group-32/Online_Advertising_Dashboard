package Backend;


import java.util.*;

public class Chart {
    private ImpressionData impressionEntries;
    private ServerData serverEntries;
    private ClickData clickEntries;
    private Date startDate;
    private MetricType metricType;
    private HashMap<Date, Double> metrics;
    private BounceType bounceType;
    private Integer bounceInterval;




    public Chart(ImpressionData impressionEntries, ServerData serverEntries, ClickData clickEntries, Date startDate,
    BounceType bounceType, Integer bounceInterval, MetricType metricType){
        this.clickEntries = clickEntries;
        this.serverEntries = serverEntries;
        this.impressionEntries = impressionEntries;
        this.startDate = startDate;
        this.bounceType = bounceType;
        this.bounceInterval = bounceInterval;
        this.metricType = metricType;
    }


    public void splitByHour (){

        metrics = new HashMap<Date, Double>();

        for (int i = 0; i < 24; i++) {
            ImpressionData releventImpressions = new ImpressionData();
            ClickData releventClicks = new ClickData();
            ServerData releventServers = new ServerData();

            for (ImpressionEntry im : impressionEntries) {
                if (im.getDateAndTime().getDate() == startDate.getDate() &&
                        im.getDateAndTime().getMonth() == startDate.getMonth() &&
                        im.getDateAndTime().getYear() == startDate.getYear() &&
                        im.getDateAndTime().getHours() == i) {
                    releventImpressions.add(im);
                }
            }
            for (ClickEntry c : clickEntries) {
                if (c.getDateAndTime().getDate() == startDate.getDate() &&
                        c.getDateAndTime().getMonth() == startDate.getMonth() &&
                        c.getDateAndTime().getYear() == startDate.getYear() &&
                c.getDateAndTime().getHours() == i) {
                    releventClicks.add(c);
                }
            }

            for (ServerEntry s : serverEntries) {
                if (s.getEntryDate().getDate() == startDate.getDate() &&
                        s.getEntryDate().getMonth() == startDate.getMonth() &&
                        s.getEntryDate().getYear() == startDate.getYear() &&
                s.getEntryDate().getHours() == i) {
                    releventServers.add(s);
                }
            }

            CampaignMetrics cMetrics = new CampaignMetrics();
            cMetrics.calculateMetrics(releventImpressions, releventClicks, releventServers);

            if (bounceType != null) {
                if (bounceType.equals(BounceType.Time)) {
                    cMetrics.calcBounceTime(releventServers, bounceInterval);
                } else if (bounceType.equals(BounceType.Pages)) {
                    cMetrics.calcBounceTime(releventServers, bounceInterval);
                }

                cMetrics.calcBounceRate();
            }

            double metric = 0;

            if (metricType.equals(MetricType.NOCLICK)) metric = cMetrics.getNoClicks();
            if (metricType.equals(MetricType.NOIMP)) metric = cMetrics.getNoImpression();
            if (metricType.equals(MetricType.NOUNI)) metric = cMetrics.getNoUniques();
            if (metricType.equals(MetricType.NOBOUNCE)) metric = cMetrics.getNoBounces();
            if (metricType.equals(MetricType.NOCONVO)) metric = cMetrics.getNoConversions();
            if (metricType.equals(MetricType.TOTALCOST)) metric = cMetrics.getTotalCost();
            if (metricType.equals(MetricType.CTR)) metric = cMetrics.getCTR();
            if (metricType.equals(MetricType.CPA)) metric = cMetrics.getCPA();
            if (metricType.equals(MetricType.CPC)) metric = cMetrics.getCPC();
            if (metricType.equals(MetricType.CPM)) metric = cMetrics.getCPM();
            if (metricType.equals(MetricType.BOUNCERATE)) metric = cMetrics.getBounceRate();

            metrics.put(new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), i, 0, 0), metric);

        }





    }

    public void splitByDay() {

        metrics = new HashMap<Date,Double>();

        int noDays = 0;

        if (startDate.getMonth() == 0 ||
                startDate.getMonth() == 2 ||
                startDate.getMonth() == 4 ||
                startDate.getMonth() == 6 ||
                startDate.getMonth() == 7 ||
                startDate.getMonth() == 9 ||
                startDate.getMonth() == 11) {
            noDays = 31;
        } else if (startDate.getMonth() == 3 ||
                startDate.getMonth() == 5 ||
                startDate.getMonth() == 8 ||
                startDate.getMonth() == 10) {
            noDays = 30;
        } else if (startDate.getYear() % 4 == 0) {
            noDays = 29;
        } else {
            noDays = 28;
        }


        for (int i = 1; i <= noDays; i++) {

            ImpressionData releventImpressions = new ImpressionData();
            ClickData releventClicks = new ClickData();
            ServerData releventServers = new ServerData();

            for (ImpressionEntry im : impressionEntries) {
                if (im.getDateAndTime().getYear() == startDate.getYear() &&
                    im.getDateAndTime().getMonth() == startDate.getMonth() &&
                    im.getDateAndTime().getDate() == i) {
                    releventImpressions.add(im);
                }
            }

            for (ClickEntry c : clickEntries) {
                if (c.getDateAndTime().getYear() == startDate.getYear() &&
                        c.getDateAndTime().getMonth() == startDate.getMonth() &&
                        c.getDateAndTime().getDate() == i) {
                    releventClicks.add(c);
                }
            }

            for (ServerEntry s : serverEntries) {
                if (s.getEntryDate().getYear() == startDate.getYear() &&
                        s.getEntryDate().getMonth() == startDate.getMonth() &&
                        s.getEntryDate().getDate() == i) {
                    releventServers.add(s);
                }
            }

            CampaignMetrics cMetrics = new CampaignMetrics();
            cMetrics.calculateMetrics(releventImpressions, releventClicks, releventServers);

            if (bounceType != null) {
                if (bounceType.equals(BounceType.Time)) {
                    cMetrics.calcBounceTime(releventServers, bounceInterval);
                } else if (bounceType.equals(BounceType.Pages)) {
                    cMetrics.calcBounceTime(releventServers, bounceInterval);
                }

                cMetrics.calcBounceRate();
            }

            double metric = 0;

            if (metricType.equals(MetricType.NOCLICK)) metric = cMetrics.getNoClicks();
            if (metricType.equals(MetricType.NOIMP)) metric = cMetrics.getNoImpression();
            if (metricType.equals(MetricType.NOUNI)) metric = cMetrics.getNoUniques();
            if (metricType.equals(MetricType.NOBOUNCE)) metric = cMetrics.getNoBounces();
            if (metricType.equals(MetricType.NOCONVO)) metric = cMetrics.getNoConversions();
            if (metricType.equals(MetricType.TOTALCOST)) metric = cMetrics.getTotalCost();
            if (metricType.equals(MetricType.CTR)) metric = cMetrics.getCTR();
            if (metricType.equals(MetricType.CPA)) metric = cMetrics.getCPA();
            if (metricType.equals(MetricType.CPC)) metric = cMetrics.getCPC();
            if (metricType.equals(MetricType.CPM)) metric = cMetrics.getCPM();
            if (metricType.equals(MetricType.BOUNCERATE)) metric = cMetrics.getBounceRate();

            metrics.put(new Date(startDate.getYear(), startDate.getMonth(),i), metric);


        }
    }

    public void splitByWeek() {
        metrics = new HashMap<Date, Double>();

        long startTime = startDate.getTime();
        Date start = new Date(startTime);
        long endTime = startTime + 604800000;
        Date endDate = new Date(endTime);

        for (int i = 1; i <= 26; i++) {
            ImpressionData releventImpressions = new ImpressionData();
            ClickData releventClicks = new ClickData();
            ServerData releventServers = new ServerData();

            for (ImpressionEntry im : impressionEntries) {
                if (im.getDateAndTime().after(start) &&
                    im.getDateAndTime().before(endDate)) {
                    releventImpressions.add(im);
                }
            }

            for (ClickEntry c : clickEntries) {
                if (c.getDateAndTime().after(start) &&
                        c.getDateAndTime().before(endDate)) {
                    releventClicks.add(c);
                }
            }

            for (ServerEntry s : serverEntries) {
                if (s.getEntryDate().after(start) &&
                        s.getEntryDate().before(endDate)) {
                    releventServers.add(s);
                }
            }

            CampaignMetrics cMetrics = new CampaignMetrics();
            cMetrics.calculateMetrics(releventImpressions, releventClicks, releventServers);

            if (bounceType != null) {
                if (bounceType.equals(BounceType.Time)) {
                    cMetrics.calcBounceTime(releventServers, bounceInterval);
                } else if (bounceType.equals(BounceType.Pages)) {
                    cMetrics.calcBounceTime(releventServers, bounceInterval);
                }

                cMetrics.calcBounceRate();
            }

            double metric = 0;

            if (metricType.equals(MetricType.NOCLICK)) metric = cMetrics.getNoClicks();
            if (metricType.equals(MetricType.NOIMP)) metric = cMetrics.getNoImpression();
            if (metricType.equals(MetricType.NOUNI)) metric = cMetrics.getNoUniques();
            if (metricType.equals(MetricType.NOBOUNCE)) metric = cMetrics.getNoBounces();
            if (metricType.equals(MetricType.NOCONVO)) metric = cMetrics.getNoConversions();
            if (metricType.equals(MetricType.TOTALCOST)) metric = cMetrics.getTotalCost();
            if (metricType.equals(MetricType.CTR)) metric = cMetrics.getCTR();
            if (metricType.equals(MetricType.CPA)) metric = cMetrics.getCPA();
            if (metricType.equals(MetricType.CPC)) metric = cMetrics.getCPC();
            if (metricType.equals(MetricType.CPM)) metric = cMetrics.getCPM();
            if (metricType.equals(MetricType.BOUNCERATE)) metric = cMetrics.getBounceRate();

            metrics.put(start, metric);

            startTime = endTime;
            endTime = endTime + 604800000;

            start = new Date(startTime);
            endDate = new Date(endTime);


        }
    }



    public static void main (String[] args) throws Exception{
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
        chart.splitByWeek();


        System.out.println(" ");
//        serverData.add(serverEntry);


    }

    public HashMap<Date, Double> getMetrics() {
        return metrics;
    }
}
