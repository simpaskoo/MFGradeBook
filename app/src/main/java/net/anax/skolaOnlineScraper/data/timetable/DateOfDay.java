package net.anax.skolaOnlineScraper.data.timetable;
import androidx.annotation.Nullable;

import net.anax.appServerClient.client.data.MissingDataException;
import net.anax.appServerClient.client.util.JsonUtilities;

import org.json.simple.JSONObject;

import java.util.Calendar;
import java.util.Objects;

public class DateOfDay {
    public int dayOfMonth;
    public int monthOfYear;
    public int year;

    public DateOfDay(int dayOfMonth, int monthOfYear, int year) {
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.year = year;
    }

    public static DateOfDay fromCalendar(Calendar calendar){
        return new DateOfDay(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    public JSONObject toJson(){
        JSONObject data = new JSONObject();
        data.put("day", dayOfMonth);
        data.put("month", monthOfYear);
        data.put("year", year);
        return data;
    }

    public static DateOfDay fromJson(JSONObject data) throws MissingDataException {
        MissingDataException e = new MissingDataException("missing data, unable to extract DateOfDay");
        return new DateOfDay(
                JsonUtilities.extractInt(data, "day", e),
                JsonUtilities.extractInt(data, "month", e),
                JsonUtilities.extractInt(data, "year", e)
        );

    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfMonth, monthOfYear, year);
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof DateOfDay){
            DateOfDay other = (DateOfDay)obj;
            return other.dayOfMonth == this.dayOfMonth
                    && other.monthOfYear == this.monthOfYear
                    && other.year == this.year;
        }
        return false;
    }
}
