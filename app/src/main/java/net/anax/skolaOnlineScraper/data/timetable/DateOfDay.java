package net.anax.skolaOnlineScraper.data.timetable;
import androidx.annotation.Nullable;
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
