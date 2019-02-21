import java.util.Date;

public class ImpressionEntry {
	private Date dateAndTime;
	private String ID;
	private Gender gender;
	private AgeGroup ageGroup;
	private Income income;
	private Context context;
	private Float impressionCost;

	public ImpressionEntry(Date dateAndTime,String ID,Gender gender,AgeGroup ageGroup, Income income, Context context, Float impressionCost) {
        this.dateAndTime = dateAndTime;
        this.ID = ID;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.income = income;
        this.context = context;
        this.impressionCost = impressionCost;
    }

    public String getID() {
        return ID;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public Context getContext() {
        return context;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public Float getImpressionCost() {
        return impressionCost;
    }

    public Gender getGender() {
        return gender;
    }

    public Income getIncome() {
        return income;
    }

}
