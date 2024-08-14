package com.example.javaapk.data;

import net.anax.appServerClient.client.data.MissingDataException;
import net.anax.appServerClient.client.util.JsonUtilities;

import net.anax.skolaOnlineScraper.data.InvalidDataInJsonException;
import net.anax.skolaOnlineScraper.data.assessment.AssessmentList;
import net.anax.skolaOnlineScraper.data.timetable.DateOfDay;
import net.anax.skolaOnlineScraper.data.timetable.TimetableWeek;
import net.anax.skolaOnlineScraper.scraper.RequestFailedException;
import net.anax.skolaOnlineScraper.webpage.SkolaOnlineAssessmentsPage;
import net.anax.skolaOnlineScraper.webpage.SkolaOnlineLoginPage;
import net.anax.skolaOnlineScraper.webpage.SkolaOnlineModulePage;
import net.anax.skolaOnlineScraper.webpage.SkolaOnlineTimetablePage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SkolaOnlineHandler {
    public String username;
    public String password;

    HashMap<DateOfDay, TimetableWeek> timetableWeeks = new HashMap<>();
    AssessmentList assessmentList = new AssessmentList(0);


    public SkolaOnlineHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public JSONObject toJson(){
        JSONObject data = new JSONObject();

        JSONArray timetableDates = new JSONArray();
        JSONArray timetables = new JSONArray();

        for(DateOfDay date : timetableWeeks.keySet()){
            timetableDates.add(date.toJson());
            timetables.add(timetableWeeks.get(date).getJsonObject());
        }

        data.put("timetableDates", timetableDates);
        data.put("timetables", timetables);
        data.put("assessmentList", assessmentList.getJSONObject());
        data.put("username", username);
        data.put("password", password);

        return data;
    }

    public AssessmentList getAssessments(){
        try{
            SkolaOnlineLoginPage loginPage = SkolaOnlineLoginPage.loadNew();
            SkolaOnlineModulePage modulePage = loginPage.login(username, password);
            SkolaOnlineAssessmentsPage assessmentsPage = modulePage.goToAssessments();
            assessmentList = assessmentsPage.getAssessmentList();
        } catch (IOException | RequestFailedException ignored) {}
        return assessmentList;
    }

    public TimetableWeek getTimetableWeekForDate(DateOfDay date){
        DateOfDay lastMonday = getLastMonday(date);
        System.out.println("last monday" + lastMonday.toJson().toJSONString());
        DateOfDay dateWeeksWednesday = getWeeksWednesday(date);
        try{
            SkolaOnlineLoginPage loginPage = SkolaOnlineLoginPage.loadNew();
            SkolaOnlineModulePage modulePage = loginPage.login(username, password);
            SkolaOnlineTimetablePage timetablePage = modulePage.goToTimetable().changeDateTo(dateWeeksWednesday.year, dateWeeksWednesday.monthOfYear, dateWeeksWednesday.dayOfMonth);
            TimetableWeek timetableWeek = timetablePage.getTimetable();
            timetableWeeks.put(lastMonday, timetableWeek);
        } catch (IOException | RequestFailedException ignored) {}
        return timetableWeeks.getOrDefault(lastMonday, null);
    }

    public static DateOfDay getWeeksWednesday(DateOfDay date){
        DateOfDay monday = getLastMonday(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(monday.year, monday.monthOfYear, monday.dayOfMonth);
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        return new DateOfDay(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    public static DateOfDay getLastMonday(DateOfDay dateOfDay){
        Calendar date = Calendar.getInstance();
        date.set(dateOfDay.year, dateOfDay.monthOfYear, dateOfDay.dayOfMonth);

        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        int daysToLastMonday = (dayOfWeek - Calendar.MONDAY + 7) % 7;
        date.add(Calendar.DAY_OF_MONTH, -daysToLastMonday);

        return new DateOfDay(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH), date.get(Calendar.YEAR));
    }

    public static SkolaOnlineHandler fromJson(JSONObject data) throws DataReadException, InvalidDataInJsonException, ParseException, MissingDataException {

        DataReadException e = new DataReadException("unable to reconstruct Handler, missing fields.", DataReadException.DataReadExceptionType.DataNotPresent);

        HashMap<DateOfDay, TimetableWeek> timetableWeeks = new HashMap<>();

        AssessmentList assessmentList = AssessmentList.parseFromJson(JsonUtilities.extractJSONObject(data, "assessmentList",e));

        JSONArray timetableDates = JsonUtilities.extractJSONArray(data, "timetableDates", e);
        JSONArray timetables = JsonUtilities.extractJSONArray(data, "timetables", e);

        for(int i = 0; i < timetableDates.size(); i++){
            DateOfDay date = DateOfDay.fromJson((JSONObject) timetableDates.get(i));
            TimetableWeek week = TimetableWeek.parseFromJsonString((JSONObject) timetables.get(i));

            timetableWeeks.put(date, week);
        }

        SkolaOnlineHandler handler = new SkolaOnlineHandler(JsonUtilities.extractString(data, "username", e), JsonUtilities.extractString(data, "password", e));
        handler.assessmentList = assessmentList;
        handler.timetableWeeks = timetableWeeks;

        return handler;
    }
}
