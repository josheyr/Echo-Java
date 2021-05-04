package ac.echo;


import org.json.simple.JSONObject;

import lombok.Getter;

public class Key {
    private API api;
    @Getter String key;

    JSONObject keyObject;

    Key(String key, API api) {
        this.api = api;
        this.key = key;

        keyObject = (JSONObject)api.getKeyObject(key);
    }

    public String getUsername() {
        return (String)keyObject.get("username");
    }
    
    public boolean hasEcho() {
        return (boolean)keyObject.get("has_echo");
    }
    
    public void refreshKey() {
        keyObject = (JSONObject)api.getKeyObject(key);
    }

    public JSONObject get() {
        return keyObject;
    }
}
