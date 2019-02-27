
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a Intepreter Class, That convert raw data into useful data.
 * @author Zehao Xuew
 */
public class Intepreter  {


    private String [] splitStringByComma (String string){
        String [] splitString = string.split(",");
        return splitString;
    }

    private Date stringToDate(String stringDate){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(stringDate);
            return date;
        }
        catch (ParseException e){
            return null;
        }

    }

    private boolean isConversion (String s){
        if (s.equals("Yes")){
            return true;
        }
        else return false;
    }

    private Gender decideGender (String string){
        if (string.equals("Male")){
            return Gender.MALE;
        }
        else return Gender.FEMALE;
    }

    private Income decideIncome(String s){
        if (s.equals("High")){
            return Income.HIGH;
        }
        else if (s.equals("Low")){
            return Income.LOW;
        }
        else return Income.MEDIUM;
    }

    private AgeGroup decideAgeGroup(String s){
        if (s.equals(AgeGroup.GROUP1.getName())){
            return AgeGroup.GROUP1;
        }
        else if (s.equals(AgeGroup.GROUP2.getName()))
            return AgeGroup.GROUP2;
        else if (s.equals(AgeGroup.GROUP3.getName())){
            return AgeGroup.GROUP3;
        }
        else if (s.equals(AgeGroup.GROUP4.getName())){
            return AgeGroup.GROUP4;
        }
        else return AgeGroup.GROUP5;
    }

    private Context decideContext(String s){
       switch(s){
           case "News":
               return Context.NEWS;
           case "Blog":
               return Context.BLOG;
           case "Shopping":
               return Context.SHOPPING;
           case "Social Media":
               return Context.SOCIALMEDIA;
           case  "Hobbies":
               return Context.HOBBIES;
           case "Travel":
               return Context.TRAVEL;
       }
       return null;
    }

    public ClickEntry interpretClickLog(String string) throws Exception{
        String [] splitString = splitStringByComma(string);
        Date date = stringToDate(splitString[0]);
        String ID = splitString[1];
        Float clickCost = Float.parseFloat(splitString[2]);
        return new ClickEntry(date,ID,clickCost);
    }

    public ServerEntry interpretServerLog(String string) throws Exception{
        String [] splitString = splitStringByComma(string);
        Date entryDate = stringToDate(splitString[0]);
        String ID = splitString[1];
        Date exitDate = stringToDate(splitString[2]);
        Integer numPageViewed = Integer.parseInt(splitString[3]);
        Boolean conversion = isConversion(splitString[4]);

        return new ServerEntry(entryDate,ID,exitDate,numPageViewed,conversion);
    }

    public ImpressionEntry interpretImpressionLog (String string) throws Exception{
        String[] splitString = splitStringByComma(string);
        Date date = stringToDate(splitString[0]);
        String ID = splitString[1];
        Gender gender = decideGender(splitString[2]);
        AgeGroup ageGroup = decideAgeGroup(splitString[3]);
        Income income = decideIncome(splitString[4]);
        Context context = decideContext(splitString[5]);
        Float impressionCost =Float.parseFloat(splitString[6]);

        return new ImpressionEntry(date,ID,gender,ageGroup,income,context,impressionCost);
    }




}
