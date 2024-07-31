package net.anax.appServerClient.client.http;

import android.util.Base64;
import android.util.Log;

import net.anax.appServerClient.client.cryptography.AESKey;
import net.anax.appServerClient.client.cryptography.RSAPublicKey;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.util.JsonUtilities;
import net.anax.appServerClient.client.util.StringUtilities;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Wrapper;
import java.util.*;

public class HttpWrapperRequest {

    private static final char CR = 13; // Carriage return
    private static final char LF = 10; // Line Feed
    private static final char SP = 32; // Space
    private static final char CLN = ':';
    private static final byte[] CRLF = new byte[]{CR, LF};

    HttpRequest underlyingRequest;
    AESKey aesKey;
    RSAPublicKey rsaKey;

    public HttpWrapperRequest(HttpRequest underlyingRequest, AESKey aesKey, RSAPublicKey rsaKey){
        this.underlyingRequest = underlyingRequest;
        this.aesKey = aesKey;
        this.rsaKey = rsaKey;
    }

    public HttpResponse send() throws IOException, RequestFailedException, HttpErrorStatusException {
        try {
            underlyingRequest.addHeader(HttpHeader.ContentLength, String.valueOf(underlyingRequest.payload.length()));

            ByteArrayOutputStream data = new ByteArrayOutputStream();

            data.write(underlyingRequest.method.name().getBytes(StandardCharsets.US_ASCII));
            data.write(SP);
            data.write(underlyingRequest.url.getPath().getBytes(StandardCharsets.US_ASCII));
            data.write(SP);
            data.write("HTTP/1.1".getBytes(StandardCharsets.US_ASCII));
            data.write(CRLF);

            for (HttpHeader header : underlyingRequest.headers.keySet()) {
                data.write(header.key.getBytes(StandardCharsets.US_ASCII));
                data.write(": ".getBytes(StandardCharsets.US_ASCII));
                data.write(underlyingRequest.headers.get(header).getBytes(StandardCharsets.US_ASCII));
                data.write(CRLF);
            }

            data.write(CRLF);
            data.write(underlyingRequest.payload.getBytes(StandardCharsets.US_ASCII));

            byte[] requestData = data.toByteArray();
            String requestDataBase64 = android.util.Base64.encodeToString(requestData, Base64.NO_WRAP);
            String aesKeyBase64 = android.util.Base64.encodeToString(aesKey.getKeyData(), Base64.NO_WRAP);
            String aesIvBase64 = Base64.encodeToString(aesKey.getIv(), Base64.NO_WRAP);

            JSONObject requestJson = new JSONObject();


            requestJson.put("request", requestDataBase64);
            requestJson.put("key", aesKeyBase64);
            requestJson.put("iv", aesIvBase64);

            byte[] wrapperData = requestJson.toString().getBytes(StandardCharsets.US_ASCII);



            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, rsaKey.getKey());
            byte[] encryptedData = cipher.doFinal(wrapperData);


            String encryptedDataBase64 = android.util.Base64.encodeToString(encryptedData, Base64.NO_WRAP).replace("\n", "");

            URL oldUrl = underlyingRequest.url;
            URL url = new URL(oldUrl.getProtocol(), oldUrl.getHost(), oldUrl.getPort(), "/rsaRelay");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");

            connection.setRequestProperty("User-Agent", "redacted");
            connection.setRequestProperty("Host", "redacted"); // does not work
            connection.setRequestProperty("Accept", "redacted");
            connection.setRequestProperty("Content-Type", "redacted");
            connection.setRequestProperty("Connection", "redacted"); // does not work either

            OutputStream ostream = connection.getOutputStream();

            System.out.println("beginning to write data: " + Arrays.toString(encryptedDataBase64.getBytes(StandardCharsets.US_ASCII)));

            ostream.write(encryptedDataBase64.getBytes(StandardCharsets.US_ASCII));

            connection.connect();
            HttpResponse response = new HttpResponse(connection.getResponseMessage(), connection.getResponseCode());

            System.out.println("response code: " + response.responseCode + ", response message: " + response.message);

            InputStream connectionInputStream;
            if(response.responseCode == 200){
                connectionInputStream = connection.getInputStream();
            }else{
                throw new HttpErrorStatusException("did not receive a 200 OK status code", response.responseCode, response.message);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionInputStream));

            String line;
            StringBuilder builder = new StringBuilder();

            while((line = reader.readLine()) != null){
                builder.append(line);
            }

            response.setPayload(builder.toString());
            byte[] underlyingResponse = getResponseFromRSARelayPayload(response.getPayload(), aesKey);
            return parseHttpResponse(underlyingResponse);

        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getResponseFromRSARelayPayload(String encryptedPayload, AESKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ParseException, RequestFailedException, InvalidAlgorithmParameterException {
        byte[] encryptedData = android.util.Base64.decode(encryptedPayload, Base64.NO_WRAP);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(key.getIv());

        cipher.init(Cipher.DECRYPT_MODE, key.getKey(), ivSpec);
        byte[] decryptedData = cipher.doFinal(encryptedData);

        String payload = new String(decryptedData, StandardCharsets.US_ASCII);

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(payload);
        return android.util.Base64.decode(JsonUtilities.extractString(responseJson, "response", new RequestFailedException("response data not found in response json")), Base64.NO_WRAP);
    }

    public static HttpResponse parseHttpResponse(byte[] response) throws RequestFailedException {

        int stage = 0;
        StringBuilder responseCode = new StringBuilder();
        StringBuilder responseMessage = new StringBuilder();
        StringBuilder tempHeader = new StringBuilder();
        String tempHeaderKey = null;
        StringBuilder payload = new StringBuilder();

        HashMap<String, List<String>> headers = new HashMap<>();

        for(int i = 0; i < response.length; i++){
            char b = (char) response[i];
            switch (stage) {
                case 0 : {if(b == SP){stage++;} break;} //skip over HTTP/1.1
                case 1 : {if(b == SP){stage++;}else{responseCode.append(b);} break;} //read status code, move on after encountering SP
                case 2 : {if(b == CR){stage++;}else{responseMessage.append(b);} break;} //read response message, skip if linebreak detected
                case 3 : {if(b != LF){throw new RequestFailedException("found no LF after CR in response after main line");}else{stage++;} break;} //only support for CRLF
                case 4 : {if(b == CLN){
                    stage++;
                    tempHeaderKey = tempHeader.toString();
                    tempHeader.setLength(0);
                }else{tempHeader.append(b);}break;} //read header key, move over to the value after encountering colon
                case 5 : {if(b == CR){
                    stage++;
                    if(headers.get(tempHeaderKey) == null){headers.put(tempHeaderKey, new ArrayList<>());}
                    headers.get(tempHeaderKey).add(tempHeader.toString());
                    tempHeader.setLength(0);

                }else{tempHeader.append(b);}break;} //read header value;
                case 6 : {if(b != LF){throw new RequestFailedException("found no LF after CR in response after header");}else{stage++;}break;}
                case 7 : {if(b != CR){tempHeader.append(b); stage = 4;}else{stage++;}break;}//move onto another header if not blank line. otherwise move onto payload
                case 8 : {if(b != LF){throw new RequestFailedException("found no LF after CR in response before payload");}else{stage++;}break;}
                case 9 : {payload.append(b);break;}
            }
        }

        if(!StringUtilities.isInteger(responseCode.toString())){throw new RequestFailedException("response code must be an integer");}
        HttpResponse r = new HttpResponse(responseMessage.toString(), Integer.parseInt(responseCode.toString()));

        tempHeader.setLength(0);
        for(String headerKey : headers.keySet()){
            for(String headerValue : headers.get(headerKey)){
                tempHeader.append(headerValue).append("; ");
            }
            tempHeader.delete(tempHeader.length()-2, tempHeader.length());
            r.addHeader(headerKey, tempHeader.toString());
            tempHeader.setLength(0);
        }

        r.setPayload(payload.toString());
        return r;
    }


}
