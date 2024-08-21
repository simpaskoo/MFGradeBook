package net.anax.skolaOnlineScraper.data.assessment;

import net.anax.skolaOnlineScraper.data.InvalidDataInJsonException;
import net.anax.skolaOnlineScraper.scraper.RequestFailedException;
import net.anax.skolaOnlineScraper.util.StringUtilities;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Assessment {
    public String date;
    public String subject;
    public String theme;
    public String weight;
    public String result;
    public String verbalAssessment;

    public Assessment(String date, String subject, String theme, String weight, String result, String verbal_assessment){
        this.date = date;
        this.subject = subject;
        this.theme = theme;
        this.weight = weight;
        this.result = result;
        this.verbalAssessment = verbal_assessment;
    }

    public static Assessment getEmptyAssessment(){
        return new Assessment("", "", "", "", "-", " ");
    }

    public static Assessment parseFromRowElement(Element assessmentElement) throws RequestFailedException {

        Assessment assessment = Assessment.getEmptyAssessment();
        Elements cells = assessmentElement.getElementsByTag("td");

        try {
            assessment.date = cells.get(3).getElementsByTag("nobr").first().text();
            assessment.subject = cells.get(4).getElementsByTag("nobr").first().text();
            assessment.theme = cells.get(5).getElementsByTag("nobr").first().text();
            assessment.weight = cells.get(6).getElementsByTag("nobr").first().text();
            assessment.result = cells.get(7).getElementsByTag("nobr").first().text();
            assessment.verbalAssessment = cells.get(8).getElementsByTag("nobr").first().text();
        }catch (NullPointerException | IndexOutOfBoundsException e){
            throw new RequestFailedException("data not found in html");
        }

        return assessment;
    }

    public JSONObject getJsonObject(){
        JSONObject data = new JSONObject();

        data.put("date", date);
        data.put("subject", subject);
        data.put("theme", theme);
        data.put("weight", weight);
        data.put("result", result);
        data.put("verbal_assessment", verbalAssessment);

        return data;
    }

    public static Assessment parseFromJson(JSONObject data) throws InvalidDataInJsonException {

        Assessment assessment = Assessment.getEmptyAssessment();
        if (!data.containsKey("date") || !data.containsKey("subject") || !data.containsKey("theme") || !data.containsKey("weight") || !data.containsKey("result") || !data.containsKey("verbal_assessment")) {
            throw new InvalidDataInJsonException("does not contain necessary data");
        }

        assessment.date = (String) data.get("date");
        assessment.subject = (String) data.get("subject");
        assessment.theme = (String) data.get("theme");
        assessment.weight = (String) data.get("weight");
        assessment.result = (String) data.get("result");
        assessment.verbalAssessment = (String) data.get("verbal_assessment");

        return assessment;
    }


    public void printSelf() {
        printSelf(20, 50, 80, 5, 6, 100);
    }

    public void printSelf(int maxDateLength, int maxSubjectLength, int maxThemeLength, int maxWeightLength, int maxResultLength, int maxVerbalAssessmentLength){
        System.out.print(date + StringUtilities.repeat(" ", Math.max(0, maxDateLength-date.length())) + " | ");
        System.out.print(subject + StringUtilities.repeat(" ", Math.max(0, maxSubjectLength-subject.length())) + " | ");
        System.out.print(theme + StringUtilities.repeat(" ",  Math.max(0, maxThemeLength-theme.length())) + " | ");
        System.out.print(weight+ StringUtilities.repeat(" ", Math.max(0, maxWeightLength-weight.length())) + " | ");
        System.out.print(result + StringUtilities.repeat(" ", Math.max(0, maxResultLength-result.length())) + " | ");
        System.out.println(verbalAssessment + StringUtilities.repeat(" ", Math.max(0, maxVerbalAssessmentLength-verbalAssessment.length())) + " | ");

    }

    @Override
    public String toString() {
        return "Assessment{" +
                "date='" + date + '\'' +
                ", subject='" + subject + '\'' +
                ", theme='" + theme + '\'' +
                ", weight='" + weight + '\'' +
                ", result='" + result + '\'' +
                ", verbalAssessment='" + verbalAssessment + '\'' +
                '}';
    }
}
