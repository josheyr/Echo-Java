package ac.echo;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import lombok.Getter;

public class Scan {
    @Getter String uuid;

    private API api;
    private String key;



    Scan(String uuid, API api, String key) {
        this.uuid = uuid;
        this.api = api;
        this.key = key;
    }

    public Detected getDetected(){
        return api.getScanDetected(key, uuid);
    }

    public ArrayList<String> getAlts() {
        return api.getScanAlts(key, uuid);
    }

    public JSONObject getResultObject() {
        return (JSONObject)api.getScanResult(key, uuid);
    }

    public String getResultString() {
        return getResultObject().toJSONString();
    }
    
}
