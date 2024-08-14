package com.example.javaapk;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.javaapk.data.SkolaOnlineHandler;

import net.anax.skolaOnlineScraper.data.timetable.DateOfDay;

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
    public void debug(){
        DateOfDay today = DateOfDay.fromCalendar(Calendar.getInstance());
        DateOfDay monday = SkolaOnlineHandler.getLastMonday(today);
        System.out.println("today: " + today.toJson().toJSONString());
        System.out.println("monday: " + monday.toJson().toJSONString());


    }
}