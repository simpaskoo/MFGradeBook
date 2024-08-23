package com.example.javaapk;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.javaapk.data.SkolaOnlineHandler;

import net.anax.skolaOnlineScraper.data.assessment.SubjectAssessments;
import net.anax.skolaOnlineScraper.data.timetable.DateOfDay;
import net.anax.skolaOnlineScraper.scraper.RequestFailedException;
import net.anax.skolaOnlineScraper.webpage.SkolaOnlineLoginPage;

import java.io.IOException;
import java.util.Calendar;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void debug() throws IOException, RequestFailedException {
        SubjectAssessments[] subjectAssessments = SkolaOnlineLoginPage.loadNew()
                .login("", "")
                .goToAssessments()
                .getAssessmentList().getBySubjects();

        for(SubjectAssessments assessments : subjectAssessments){
            System.out.println(assessments.toString());
        }


    }
}