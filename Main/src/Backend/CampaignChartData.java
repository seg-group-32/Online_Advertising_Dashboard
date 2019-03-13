//package Backend;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class CampaignChartData {
//        private ClickData clickData;
//        private ImpressionData impressionData;
//        private  ServerData serverData;
//
//    public CampaignChartData (ClickData clickData,ImpressionData impressionData, ServerData serverData){
//        this.clickData = clickData;
//        this.impressionData = impressionData;
//        this.serverData = serverData;
//    }
//
//    public ArrayList<Integer> divideClickMetricsByTime (String timeInterval){
//        ArrayList<Integer> clickPerTimeInterval=
//        Date minDate = clickData.get(0).getDateAndTime();
//        if (timeInterval.equals("HOUR")){
//            for (ClickEntry clickEntry : clickData ){
//                if ((clickEntry.getDateAndTime().getHours() == minDate.getHours())){
//
//                }
//            }
//        }
//    }
//}
