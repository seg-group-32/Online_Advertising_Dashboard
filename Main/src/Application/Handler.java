package Application;

import GUI.*;
import Backend.*;

import java.io.*;
import java.util.ArrayList;

/** Method gets all metrics and returns a String (for now) if everything is loaded.
 * Handler contains the bulk of useful methods used to interact with the backend to interpret and be able to display data to the user
 * @Author Jacob
 */
public class Handler {

    private CampaignManager manager;

    private static Campaign currentCampaign;
    private static CSVReader reader;

    Handler(String campaignName) {
        reader = new CSVReader();
        manager = new CampaignManager();
        currentCampaign = new Campaign();
        currentCampaign.setName(campaignName);
        manager.addCampaign(currentCampaign);
    }

    public void addCampaign(String name) {
        Campaign newCamp = new Campaign();
        newCamp.setName(name);
        manager.addCampaign(newCamp);
        currentCampaign = newCamp;
    }

    //WILL NEED TO CHANGE!! WE DONT WANT TO REQUIRE ALL FILES TO VIEW DATA - JACOB
    public void calculateMetrics() { //If all is loaded, calculate!!
        currentCampaign.calculateMetrics();
    }

    public Campaign getCurrentCampaign() {
        return currentCampaign;
    }

    public void setCurrentCampaign(Campaign c) {
        currentCampaign = c;
    }

    public ArrayList<Campaign> getAllCampaigns() {
        return manager.getCampaigns();
    }

}
