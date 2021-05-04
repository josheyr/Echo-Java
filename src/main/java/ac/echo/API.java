package ac.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class API {

    List<ProgressHandler> progressHandlers = new ArrayList<ProgressHandler>();

    public void registerProgressListener(String pin, ProgressListener progressListener) {
        progressHandlers.add(new ProgressHandler(pin, progressListener));
    }

    public String get(String url_string) {
        URL url;
        try {
            url = new URL(url_string);

            HttpURLConnection con;
            try {
                con = (HttpURLConnection) url.openConnection();

                try {
                    con.setRequestMethod("GET");

                    con.connect();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    return content.toString();

                } catch (ProtocolException e) {
                    return "";
                }
            } catch (IOException e) {
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }
    }

    public String getCurrentPin(String key) {
        String meResponse = get("https://api.echo.ac/query/pin?key=" + key);

        if (meResponse == "") {
            return null;
        }

        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject) response;

            if ((Boolean) json_response.get("success")) {
                return (String) ((JSONObject) json_response.get("result")).get("pin");
            } else {
                return null;
            }

        } catch (ParseException e) {
            return null;
        }
    }

    public Object getKeyObject(String key) {
        String meResponse = get("https://api.echo.ac/query/me?key=" + key);

        if (meResponse == "") {
            return null;
        }

        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject) response;

            if ((Boolean) json_response.get("success")) {

                return ((JSONObject) json_response.get("result"));
                
            } else {
                return null;
            }

        } catch (ParseException e) {
            return null;
        }
    }

    public Scan getMostRecentScan(String key) {
        ArrayList<String> scans = getScans(key, 1);
        if (scans != null && scans.size() > 0) {
            return new Scan(getScans(key, 1).get(0), this, key);
        } else {
            return null;
        }
    }

    public Detected getScanDetected(String key, String scan_uuid){
        String meResponse = get("https://api.echo.ac/query/scan?key=" + key + "&uuid=" + scan_uuid);

        if (meResponse == "") {
            return null;
        }

        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject) response;

            if ((Boolean) json_response.get("success")) {

                int detected = (int)((JSONObject) json_response.get("result")).get("detected");
                switch(detected) {
                    case 0:
                        return Detected.CLEAN;
                    case 1:
                        return Detected.DETECTED;
                    case 2:
                        return Detected.UNUSUAL;
                    default:
                        return null;
                }
            } else {
                return null;
            }

        } catch (ParseException e) {
            return null;
        }
    }

    public Object getScanResult(String key, String scan_uuid){
        String meResponse = get("https://api.echo.ac/query/scan?key=" + key + "&uuid=" + scan_uuid);

        if (meResponse == "") {
            return null;
        }

        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject) response;

            if ((Boolean) json_response.get("success")) {

                return ((JSONObject) json_response.get("result")).get("scan_results");
                
            } else {
                return null;
            }

        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<String> getScanAlts(String key, String scan_uuid) {
        System.out.println(scan_uuid);

        String meResponse = get("https://api.echo.ac/query/scan?key=" + key + "&uuid=" + scan_uuid);

        if (meResponse == "") {
            return null;
        }

        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject) response;

            if ((Boolean) json_response.get("success")) {

                return (JSONArray) ((JSONObject) json_response.get("result")).get("alts");

            } else {
                return null;
            }

        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<String> getScans(String key, int length) {
        String meResponse = get("https://api.echo.ac/query/scans?key=" + key + "&length=" + length);

        if (meResponse == "") {
            return null;
        }

        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject) response;

            if ((Boolean) json_response.get("success")) {
                if (length == 1) {

                    ArrayList<String> list = new ArrayList<String>();
                    list.add((String) ((JSONObject) json_response.get("result")).get("scans"));
                    return list;
                } else {

                    return (JSONArray) ((JSONObject) json_response.get("result")).get("scans");
                }
            } else {
                return null;
            }

        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<String> getAlts(String key, String username) {



        String meResponse = get("https://api.echo.ac/query/player?key=" + key + "&player=" + username);

        if (meResponse == "") {
            return null;
        }

        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject) response;

            if ((Boolean) json_response.get("success")) {
                return (JSONArray) ((JSONObject) json_response.get("result")).get("alts");
            } else {
                return null;
            }

        } catch (ParseException e) {
            return null;
        }
    }
}
