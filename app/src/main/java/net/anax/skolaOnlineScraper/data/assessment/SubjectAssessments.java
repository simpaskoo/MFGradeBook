package net.anax.skolaOnlineScraper.data.assessment;

import net.anax.appServerClient.client.util.StringUtilities;

import java.util.ArrayList;

public class SubjectAssessments {
    String subject;
    ArrayList<Assessment> assessments = new ArrayList<>();
    int totalPoints;
    int maxPoints;
    double averageGrade;

    public SubjectAssessments(String subject ) {
        this.subject = subject;
    }

    void updateData(){
        int totalPoints = 0;
        int maxPoints = 0;
        double gradeCount = 0;
        double gradeSum = 0;

        for(Assessment assessment : assessments){

            if(assessment.result.contains("/")){
                String[] parts = assessment.result.split("/");
                if(parts.length != 2){continue;}
                if(!StringUtilities.isInteger(parts[0]) || ! StringUtilities.isInteger(parts[1])){continue;}

                totalPoints += Integer.parseInt(parts[0]);
                maxPoints += Integer.parseInt(parts[1]);


            }else{
                String rawGrade = assessment.result.replace("-", "");

                if(rawGrade.isEmpty() || !StringUtilities.isInteger(rawGrade)){continue;}

                String weightStr = assessment.weight.replace(",", ".");
                if(!StringUtilities.isFloat(weightStr)){continue;}

                double grade = Integer.parseInt(rawGrade);
                double weight = Float.parseFloat(weightStr);

                if(assessment.result.contains("-")){
                    grade += 0.5f;
                }

                gradeSum += grade * weight;
                gradeCount += weight;
            }
        }

        this.totalPoints = totalPoints;
        this.maxPoints = maxPoints;
        this.averageGrade = gradeSum / gradeCount;
    }

    @Override
    public String toString() {
        return "SubjectAssessments{" +
                "subject='" + subject + '\'' +
                ", totalPoints=" + totalPoints +
                ", maxPoints=" + maxPoints +
                ", averageGrade=" + averageGrade +
                ", assessments=" + assessments +
                '}';
    }
}
