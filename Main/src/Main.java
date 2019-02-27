public class Main
{

    public static void main(String[] args) throws Exception {
        Campaign campaign = new Campaign();
        CSVReader csvReader = new CSVReader();

        campaign.setClickEntries(csvReader.fetchInfo());
        campaign.setImpressionEntries(csvReader.fetchInfo());
        campaign.setServerEntries(csvReader.fetchInfo());

        campaign.loadServerData();
        campaign.loadClickData();
        campaign.loadImpressionData();


        campaign.calculateMetrics();

        System.out.println(campaign.getMetrics().getCPA());
        System.out.println(campaign.getMetrics().getNoClicks());
    }
}
