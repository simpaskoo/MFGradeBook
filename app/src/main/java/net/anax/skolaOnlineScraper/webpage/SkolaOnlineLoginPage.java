package net.anax.skolaOnlineScraper.webpage;

import net.anax.skolaOnlineScraper.browser.BrowserCookieCache;
import net.anax.skolaOnlineScraper.http.EHttpMethod;
import net.anax.skolaOnlineScraper.http.HttpRequest;
import net.anax.skolaOnlineScraper.http.HttpResponse;
import net.anax.skolaOnlineScraper.scraper.RequestFailedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class SkolaOnlineLoginPage extends AbstractSkolaOnlinePage{
    public static final String url = "https://www.skolaonline.cz/prihlaseni/";
    private boolean doLogs = false;
    private SkolaOnlineLoginPage(){
        this.cookieCache = new BrowserCookieCache();
        this.hiddenFormInputs = new HashMap<>();
    }

    /**
     * calls loadNew() with logs disabled
     */
    public static SkolaOnlineLoginPage loadNew() throws IOException {
        return loadNew(false);
    }

    /**
     * loads a new login page
     * @param doLogs specifies if the function should do logs. this argument is propagated to all subsequent pages.
     * @return returns a new login page from which you can than login into SkolaOnline
     * @exception IOException is thrown in case of a failed connection.
     */
    public static SkolaOnlineLoginPage loadNew(boolean doLogs) throws IOException {
        if(doLogs){System.out.println("===========================================START OF loadNew() LOG===========================================");}
        SkolaOnlineLoginPage loginPage = new SkolaOnlineLoginPage();
        loginPage.doLogs = doLogs;

        HttpRequest request = new HttpRequest(new URL(url), EHttpMethod.GET);

        addCommonHeadersToRequest(request);

        HttpResponse response = request.send();

        if(doLogs){
            request.printSelf();
        }

        if(doLogs){
            response.printSelf();
        }

        response.addCookiesToCache(loginPage.cookieCache);
        String body = response.getBody();
        Document doc = Jsoup.parse(body);

        loginPage.updateHiddenFormInputs(doc);

        if(doLogs){
            System.out.println("-----------------------loadNew() extracted hidden input values start-----------------------");
            for(String hiddenInput : loginPage.hiddenFormInputs.keySet()){
                System.out.println(hiddenInput + ": " + loginPage.hiddenFormInputs.get(hiddenInput).value);
            }
            System.out.println("-----------------------loadNew() extracted hidden form values end-----------------------");
        }
        response.disconnect();
        if(doLogs){System.out.println("===========================================END OF loadNew() LOG===========================================");}
        return loginPage;
    }

    /**
     * logs in to SkolaOnline
     * @param username the username with which to log in.
     * @param password the password with which to log in.
     * @return returns a new SkolaOnlineModulePage.
     * @exception IOException is thrown in case of failed connection.
     * @exception RequestFailedException is thrown if the response did not contain an authentication token.
     * thi can be caused by invalid credentials or by the protocol SkolaOnline uses changing.*
     *
     */
    public SkolaOnlineModulePage login(String username, String password) throws IOException, RequestFailedException {
        if(doLogs){System.out.println("=========================================START OF login() LOG=========================================");}
        SkolaOnlineModulePage modulePage = new SkolaOnlineModulePage(this.cookieCache, this.hiddenFormInputs);
        modulePage.doLogs = doLogs;

        HttpRequest request = new HttpRequest(new URL(SkolaOnlineModulePage.loginUrl), EHttpMethod.POST);

        String boundary = getBoundary();
        String payload = constructLoginRequestBody(boundary, username, password);

        request.setBody(payload);
        addCommonHeadersToRequest(request);
        request.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);

        request.setHeader("Origin", "https://skolaonline.cz");


        HttpResponse response = request.send();

        if(doLogs){
            request.printSelf();
        }


        response.addCookiesToCache(modulePage.cookieCache);

        if(modulePage.cookieCache.getCookie(".ASPXAUTH") == null){
            throw new RequestFailedException("failed to login");
        }

        String body = response.getBody();
        Document doc = Jsoup.parse(body);

        modulePage.updateHiddenFormInputs(doc);


        if(doLogs){
            response.printSelf();
            System.out.println("--------------------login() hidden inputs start--------------------");
            for(String hiddenInput : modulePage.hiddenFormInputs.keySet()){
                System.out.println(hiddenInput + ": " + modulePage.hiddenFormInputs.get(hiddenInput).value);
            }
            System.out.println("--------------------login() hidden inputs end--------------------");
        }
        response.disconnect();
        if(doLogs){System.out.println("=========================================END OF login() LOG=========================================");}
        return modulePage;
    }

    private String getBoundary(){
        return "---------------------------141200824129006017161243354094";
    }
    private String constructLoginRequestBody(String boundary, String username, String password){
        String __VIEWSTATE = hiddenFormInputs.get("__VIEWSTATE") == null ? "" : hiddenFormInputs.get("__VIEWSTATE").value;
        String __VIEWSTATEGENERATOR = hiddenFormInputs.get("__VIEWSTATEGENERATOR") == null ? "" : hiddenFormInputs.get("__VIEWSTATEGENERATOR").value;
        String __PREVIOUSPAGE = hiddenFormInputs.get("__PREVIOUSPAGE") == null ? "" : hiddenFormInputs.get("__PREVIOUSPAGE").value;
        String __EVENTVALIDATION = hiddenFormInputs.get("__EVENTVALIDATION") == null ? "" : hiddenFormInputs.get("__EVENTVALIDATION").value;
        String __dnnVariable = hiddenFormInputs.get("__dnnVariable") == null ? "" : hiddenFormInputs.get("__dnnVariable").value;
        String __RequestVerificationToken = hiddenFormInputs.get("__RequestVerificationToken") == null ? "" : hiddenFormInputs.get("__RequestVerificationToken").value;

        String boundary_delimiter = "--" + boundary;

        String body = new String(new byte[]{}) + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 69, 86, 69, 78, 84, 84, 65, 82, 71, 69, 84, 34, 10, 10, 100, 110, 110, 36, 99, 116, 114, 57, 57, 52, 36, 83, 79, 76, 76, 111, 103, 105, 110, 36, 98, 116, 110, 79, 68, 101, 115, 108, 97, 116, 10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 69, 86, 69, 78, 84, 65, 82, 71, 85, 77, 69, 78, 84, 34, 10, 10, 10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 86, 73, 69, 87, 83, 84, 65, 84, 69, 34, 10, 10})
                + __VIEWSTATE + new String(new byte[]{10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 86, 73, 69, 87, 83, 84, 65, 84, 69, 71, 69, 78, 69, 82, 65, 84, 79, 82, 34, 10, 10})
                + __VIEWSTATEGENERATOR + new String(new byte[]{10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 86, 73, 69, 87, 83, 84, 65, 84, 69, 69, 78, 67, 82, 89, 80, 84, 69, 68, 34, 10, 10, 10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 80, 82, 69, 86, 73, 79, 85, 83, 80, 65, 71, 69, 34, 10, 10})
                + __PREVIOUSPAGE + new String(new byte[]{10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 69, 86, 69, 78, 84, 86, 65, 76, 73, 68, 65, 84, 73, 79, 78, 34, 10, 10})
                + __EVENTVALIDATION + new String(new byte[]{10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 100, 110, 110, 36, 100, 110, 110, 83, 101, 97, 114, 99, 104, 36, 116, 120, 116, 83, 101, 97, 114, 99, 104, 34, 10, 10, 10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 74, 109, 101, 110, 111, 85, 122, 105, 118, 97, 116, 101, 108, 101, 34, 10, 10})
                + username + new String(new byte[]{10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 72, 101, 115, 108, 111, 85, 122, 105, 118, 97, 116, 101, 108, 101, 34, 10, 10})
                + password + new String(new byte[]{10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 83, 99, 114, 111, 108, 108, 84, 111, 112, 34, 10, 10, 49, 48, 54, 10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 100, 110, 110, 86, 97, 114, 105, 97, 98, 108, 101, 34, 10, 10})
                + __dnnVariable + new String(new byte[]{10})
                + boundary_delimiter + new String(new byte[]{10, 67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34, 95, 95, 82, 101, 113, 117, 101, 115, 116, 86, 101, 114, 105, 102, 105, 99, 97, 116, 105, 111, 110, 84, 111, 107, 101, 110, 34, 10, 10})
                + __RequestVerificationToken + new String(new byte[]{10})
                + boundary_delimiter + new String(new byte[]{45, 45, 10});
/*
        String body =
                """
                """+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__EVENTTARGET"

dnn$ctr994$SOLLogin$btnODeslat
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__EVENTARGUMENT"


"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__VIEWSTATE"

""" + __VIEWSTATE + "\n" + """
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__VIEWSTATEGENERATOR"
                
""" + __VIEWSTATEGENERATOR + "\n" + """
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__VIEWSTATEENCRYPTED"
                
                
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__PREVIOUSPAGE"
                
""" + __PREVIOUSPAGE + "\n" + """
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__EVENTVALIDATION"
                
""" + __EVENTVALIDATION + "\n" + """
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="dnn$dnnSearch$txtSearch"
                
                
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="JmenoUzivatele"
                
""" + username + "\n" + """
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="HesloUzivatele"
                
""" + password + "\n" +"""
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="ScrollTop"
                
106
""" +boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__dnnVariable"
                
""" + __dnnVariable + "\n" + """
"""+boundary_delimiter + "\n" +"""
Content-Disposition: form-data; name="__RequestVerificationToken"
                
""" + __RequestVerificationToken + "\n" + """
"""+boundary_delimiter + "--\n";
*/

        return body.replace("\n", "\r\n");
    }

}
