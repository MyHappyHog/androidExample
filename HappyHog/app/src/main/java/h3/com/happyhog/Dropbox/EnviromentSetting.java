package h3.com.happyhog.Dropbox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import h3.com.happyhog.Define;

/**
 * Created by sloth on 2016-03-02.
 */
public class EnviromentSetting implements Serializable {
  private double maxTemperature;
  private double minTemperature;
  private double maxHumidity;
  private double minHumidity;

  @Override
  public boolean deserialize(String json) {
    try {
      JSONObject jsonRoot = new JSONObject(json);
      JSONArray ja = new JSONArray();

      ja.put(maxTemperature).put(minTemperature);
      jsonRoot.put(Define.TEMPERATURE_KEY, ja);

      ja = new JSONArray();
      ja.put(maxHumidity).put(minHumidity);
      jsonRoot.put(Define.HUMIDITY_KEY, ja);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return true;
  }

  @Override
  public String serialize() {
    JSONObject jsonRoot = new JSONObject();
    try {
      JSONArray tempArray = jsonRoot.getJSONArray(Define.TEMPERATURE_KEY);
      maxTemperature = tempArray.getInt(0);
      minTemperature = tempArray.getInt(1);

      tempArray = jsonRoot.getJSONArray(Define.HUMIDITY_KEY);
      maxHumidity = tempArray.getInt(0);
      minHumidity = tempArray.getInt(1);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonRoot.toString();
  }
}
