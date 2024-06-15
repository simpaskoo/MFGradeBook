package net.anax.skolaOnlineScraper.webpage;

import net.anax.skolaOnlineScraper.browser.BrowserCookieCache;
import net.anax.skolaOnlineScraper.data.timetable.TimetableWeek;
import net.anax.skolaOnlineScraper.http.HttpCookie;
import net.anax.skolaOnlineScraper.http.EHttpMethod;
import net.anax.skolaOnlineScraper.http.HttpRequest;
import net.anax.skolaOnlineScraper.http.HttpResponse;
import net.anax.skolaOnlineScraper.logging.Logger;
import net.anax.skolaOnlineScraper.scraper.RequestFailedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;

public class SkolaOnlineTimetablePage extends AbstractSkolaOnlinePage{
    String html;
    TimetableWeek week = null;
    boolean doLogs;
    SkolaOnlineTimetablePage(){}
    SkolaOnlineTimetablePage(BrowserCookieCache cache, HashMap<String, HiddenFormInput> hiddenFromInputs){
        this.cookieCache = cache;
        this.hiddenFormInputs = hiddenFromInputs;
    }
    SkolaOnlineTimetablePage addHtml(String html){
        this.html = html;
        return this;
    }

    /**
     * extracts the timetable from the page.
     * @return returns a new TimetableWeek containing the data for the week contained in the page.
     * @exception RequestFailedException is thrown if the timetable is not contained in the page.
     * this is probably caused a change in the protocol SkolaOnline uses.
     */
    public TimetableWeek getTimetable() throws RequestFailedException {
        if(week == null){
            week = TimetableWeek.getTimeTableFromHTML(html);
        }
        return week;
    }

    /**
     * returns a new SkolaOnlineTimetablePage that has the timetable with the date provided.
     * @param year, month, dayOfMonth the date to which the timetable week contained will correspond.
     * @return returns a SkolaOnlineTimetablePage, the page can be broken,
     * which cannot be verified until an attempt to extract the timetable is made.
     * @exception IOException ios thrown in case of a failed connection.
     */
    public SkolaOnlineTimetablePage changeDateTo(int year, int month, int dayOfMonth) throws IOException {
        if(doLogs){
            System.out.println("==================START OF changeDateTo() LOG======================");
        }

        HttpRequest request = new HttpRequest(new URL("https://aplikace.skolaonline.cz/SOL/App/Kalendar/KZK001_KalendarTyden.aspx"), EHttpMethod.POST);
        addCommonHeadersToRequest(request);
        request.setHeader("Referer", "https://aplikace.skolaonline.cz/SOL/App/Kalendar/KZK001_KalendarTyden.aspx");
        request.setHeader("Origin", "https://aplikace.skolaonline.cz");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setBody(constructChangeDateRequestPayload(year, month, dayOfMonth));

        request.addCookie(cookieCache.getCookie("ASP.NET_SessionId"));
        request.addCookie(cookieCache.getCookie("SERVERID"));
        request.addCookie(cookieCache.getCookie("ZPUSOB_OVERENI"));
        request.addCookie(cookieCache.getCookie(".ASPXAUTH"));
        request.addCookie(cookieCache.getCookie("SESSION_EXPIRES"));
        request.addCookie(new HttpCookie("cookiesconsent_sol", "{\"level\":[\"necessary\"],\"revision\":0,\"data\":null,\"rfc_cookie\":false}"));

        if(doLogs){
            request.printSelf();
        }

        HttpResponse response = request.send();

        if(doLogs){
            response.printSelf();
        }

        Document doc = Jsoup.parse(response.getBody());
        updateHiddenFormInputs(doc);

        SkolaOnlineTimetablePage timetablePage = new SkolaOnlineTimetablePage(this.cookieCache, this.hiddenFormInputs).addHtml(response.getBody());
        timetablePage.doLogs = doLogs;

        response.addCookiesToCache(cookieCache);

        if(doLogs){
            System.out.println("-----------------------changeDateTo() hidden inputs start--------------------");
            for(String input : hiddenFormInputs.keySet()){
                System.out.println(input + ": " + hiddenFormInputs.get(input).value);
            }

            System.out.println("-----------------------changeDateTo() hidden inputs start--------------------");
            System.out.println("==================END OF changeDateTo() LOG======================");
        }
        response.disconnect();
        return timetablePage;
    }

    private String constructChangeDateRequestPayload(int yearVal, int monthOfYear, int dayOfMonth){

        String year = String.valueOf(yearVal);
        String month = String.valueOf(monthOfYear);
        String day = String.valueOf(dayOfMonth);

        String calendar_part = "%253Cx%2520PostData%253D%2522" + year + "x" + month + "x" + year + "x" + month + "x" + day + "x1%2522%253E%253C%2Fx%253E";

        UrlEncodedDataFormBuilder builder = new UrlEncodedDataFormBuilder();

        String __VIEWSTATE_SESSION_KEY = this.hiddenFormInputs.get("__VIEWSTATE_SESSION_KEY") == null ? "" : this.hiddenFormInputs.get("__VIEWSTATE_SESSION_KEY").value;
        String __EVENTVALIDATION = this.hiddenFormInputs.get("__EVENTVALIDATION") == null ? "" : this.hiddenFormInputs.get("__EVENTVALIDATION").value;

        builder.addField("__EVENTTARGET", "calendarPart%24kalendar");
        builder.addField("__EVENTARGUMENT", "");
        builder.addField("__LASTFOCUS", "");
        builder.addField("__VIEWSTATE_SESSION_KEY", __VIEWSTATE_SESSION_KEY);
        builder.addField("__VIEWSTATE", "");
        builder.addField("calendarPart_kalendar", calendar_part);
        builder.addField("calendarPart%24kalendar%24RBSelectionMode", "Week");
        builder.addField("CBZobrazitRozvrh", "on");
        builder.addField("CBZobrazitHodnoceni", "on");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl01%24txtTitulek", "");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl01%24txtNapoveda", "");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl01%24hiddenDruhOrganizaceID", "KS");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl02%24txtTitulek", "");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl02%24txtNapoveda", "");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl02%24hiddenDruhOrganizaceID", "VS");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl03%24txtTitulek", "");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl03%24txtNapoveda", "");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl03%24hiddenDruhOrganizaceID", "ZR");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl04%24txtTitulek", "");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl04%24txtNapoveda", "");
        builder.addField("ctl16%24instantniNapovedaEditor%24rptNapoveda%24ctl04%24hiddenDruhOrganizaceID", "NS");
        builder.addField("ctl16%24instantniNapovedaEditor%24hiddenInstantniNapovedaPoleID", "");
        try {
            builder.addField("__EVENTVALIDATION", URLEncoder.encode(__EVENTVALIDATION, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Logger.LOG("failed to encode __EVENTVALIDATION in constructChangeDateRequestPayload()");
        }

        return builder.getData();
    }

}
