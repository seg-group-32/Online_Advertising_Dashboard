package Backend;

import com.sun.org.apache.xpath.internal.operations.Bool;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Filter {

    private ClickData clickData;
    private ImpressionData impressionData;
    private  ServerData serverData;

    private ArrayList<ImpressionData> filters;

    private ClickData tmpClickData = new ClickData();
    private ImpressionData tmpImpressionData = new ImpressionData();
    private ServerData tmpServerData = new ServerData();







    public Filter (ImpressionData impressionData, ClickData clickData, ServerData serverData) {
        this.clickData = clickData;
        this.impressionData = impressionData;
        this.serverData = serverData;
    }



    public void filter (Date startDate, Date endDate, Bool[] switches){



    }

    public ImpressionData calculateFilters(boolean[] cases, Date startDate, Date endDate){
        ImpressionData copy = new ImpressionData();

        boolean add;

        for (ImpressionEntry i : impressionData) {
            add = true;

            if (!(cases[0])) {
                if (i.getGender().equals(Gender.MALE)) add = false;
            }

            if (!(cases[1])) {
                if (i.getGender().equals(Gender.FEMALE)) add = false;
            }

            if (!(cases[2])) {
                if (i.getAgeGroup().equals(AgeGroup.GROUP1)) add = false;
            }

            if (!(cases[3])) {
                if (i.getAgeGroup().equals(AgeGroup.GROUP2)) add = false;
            }

            if (!(cases[4])) {
                if (i.getAgeGroup().equals(AgeGroup.GROUP3)) add = false;
            }
            if (!(cases[5])) {
                if (i.getAgeGroup().equals(AgeGroup.GROUP4)) add = false;
            }

            if (!(cases[6])) {
                if (i.getAgeGroup().equals(AgeGroup.GROUP5)) add = false;
            }

            if (!(cases[7])) {
                if (i.getIncome().equals(Income.LOW)) add = false;
            }

            if (!(cases[8])) {
                if (i.getIncome().equals(Income.MEDIUM)) add = false;
            }

            if (!(cases[9])) {
                if (i.getIncome().equals(Income.HIGH)) add = false;
            }

            if (!(cases[10])) {
                if (i.getContext().equals(Context.NEWS)) add = false;
            }

            if (!(cases[11])) {
                if (i.getContext().equals(Context.SHOPPING)) add = false;
            }

            if (!(cases[12])) {
                if (i.getContext().equals(Context.SOCIALMEDIA)) add = false;
            }

            if (!(cases[13])) {
                if (i.getContext().equals(Context.BLOG)) add = false;
            }

            if (!(cases[14])) {
                if (i.getContext().equals(Context.HOBBIES)) add = false;
            }

            if (!(cases[15])) {
                if (i.getContext().equals(Context.TRAVEL)) add = false;
            }

            if (startDate != null && endDate != null) {
                if (i.getDateAndTime().after(endDate) || i.getDateAndTime().before(startDate)) add = false;
            }

            if (add) copy.add(i);
        }
      return copy;
    }

    public ClickData filterClicks (ImpressionData filteredImpressions, Date startDate, Date endDate){
        ClickData clickCopy = new ClickData();
        HashSet<String> IDset = new HashSet<String>();

        for (ImpressionEntry i : filteredImpressions) {
            IDset.add(i.getID());
        }


        for (ClickEntry c : clickData) {
            if (IDset.contains(c.getID())) {
                if (c.getDateAndTime().before(endDate) && c.getDateAndTime().after(startDate)) clickCopy.add(c);
            }
        }

        return clickCopy;
    }

    public ServerData filterServer (ClickData filteredClicks, Date startDate, Date endDate){
        ServerData serverCopy = new ServerData();
        HashSet<String> IDset = new HashSet<String>();

        for (ClickEntry c : filteredClicks) {
            IDset.add(c.getID());
        }


        for (ServerEntry s : serverData) {
            if (IDset.contains(s.getID())) {
                if (s.getEntryDate().after(startDate) && s.getEntryDate().before(endDate)) serverCopy.add(s);
            }
        }

        return serverCopy;
    }



    public static void main (String[] args) throws Exception{

        ImpressionData impressionEntries = new ImpressionData();
        ClickData clickData = new ClickData();
        ServerData serverData = new ServerData();
        Intepreter intepreter = new Intepreter();
        ImpressionEntry impressionEntry = intepreter.interpretImpressionLog("2015-01-01 12:01:18,8895519749317550080,Female,25-34,High,Shopping,0.000000");
        ServerEntry serverEntry = intepreter.interpretServerLog("2015-01-01 12:01:21,8895519749317550080,2015-01-01 12:05:13,7,No");
        ClickEntry clickEntry = intepreter.interpretClickLog("2015-01-01 12:01:21,8895519749317550080,11.794442");
        impressionEntries.add(impressionEntry);
        clickData.add(clickEntry);
        serverData.add(serverEntry);
        Filter filter = new Filter(impressionEntries,clickData,serverData);

        boolean [] booleans = new boolean[16];
        for (int i = 0; i<16;i++){
            booleans[i] = true;
        }

        booleans[1] = false;

        Date startDate = new Date(113,12,31,1,1,1);
        Date endDate = new Date(118,11,31,1,1,1);

       ImpressionData filteredData = filter.calculateFilters(booleans,startDate,endDate);
       ClickData filteredClickData = filter.filterClicks(filteredData,startDate,endDate);
       ServerData filteredServerData = filter.filterServer(filteredClickData,startDate,endDate);
        System.out.println(filteredData.size());
        System.out.println(filteredClickData.size());
        System.out.println(filteredServerData.size());

    }
}
