package h3.com.happyhog.Dropbox;

import org.json.JSONException;
import org.json.JSONObject;

import h3.com.happyhog.Define;

/**
 * Created by sloth on 2016-03-02.
 */
public class RelaySetting implements Serializable {
  private int temperatureNum;
  private int humidityNum;

  public RelaySetting() {
    temperatureNum = 0;
    humidityNum = 1;
  }
  @Override
  public boolean deserialize(String json) {
    try {
      JSONObject jsonRoot = new JSONObject(json);

      temperatureNum = jsonRoot.getInt(Define.TEMPERATURE_KEY);
      humidityNum = jsonRoot.getInt(Define.HUMIDITY_KEY);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return true;
  }

  @Override
  public String serialize() {
    JSONObject jsonRoot = new JSONObject();
    try {
      jsonRoot.put(Define.TEMPERATURE_KEY, temperatureNum);

      jsonRoot.put(Define.HUMIDITY_KEY, humidityNum);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonRoot.toString();
  }

  public void setTemperatureNum(int number) {
    temperatureNum = number;
  }

  public void setHumidityNum(int number) {
    humidityNum = number;
  }

  public int getTemperatureNum() {
    return temperatureNum;
  }

  public int getHumidityNum() {
    return humidityNum;
  }
}
