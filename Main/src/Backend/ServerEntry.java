package Backend;

import java.util.Date;

public class ServerEntry {
	private Date entryDate;
	private String ID;
	private Date exitDate;
	private Integer pagesViewed;
	private Boolean conversion;

	public ServerEntry(Date entryDate, String ID, Date exitDate, Integer pagesViewed, Boolean conversion){
	    this.entryDate = entryDate;
	    this.exitDate = exitDate;
	    this.ID = ID;
	    this.pagesViewed = pagesViewed;
	    this.conversion = conversion;

    }

    public String getID() {
        return ID;
    }

    public Boolean getConversion() {
        return conversion;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public Date getExitDate() {
        return exitDate;
    }

    public Integer getPagesViewed() {
        return pagesViewed;
    }
}

