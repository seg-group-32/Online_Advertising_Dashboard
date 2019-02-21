import java.util.Date;

public class ClickEntry {
	private Date dateAndTime;
	private String ID;
	private float clickCost;
	
	public ClickEntry(Date dateAndTime, String ID, float clickCost) {
		this.dateAndTime = dateAndTime;
		this.ID = ID;
		this.clickCost = clickCost;
	}

    public float getClickCost() {
        return clickCost;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public String getID() {
        return ID;
    }
}
