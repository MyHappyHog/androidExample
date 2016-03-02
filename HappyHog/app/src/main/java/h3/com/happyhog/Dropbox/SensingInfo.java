package h3.com.happyhog.Dropbox;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import h3.com.happyhog.Define;

/**
 * Created by sloth on 2016-03-02.
 */
public class SensingInfo implements Serializable {

  private double temperature;
  private double humidity;
  private String rev;

  public SensingInfo() {
    temperature = humidity = 0;
    rev = "3e4404ddb9";
  }

  @Override
  public boolean deserialize(String json) {
    Log.i("delvelop", json);
    try {
      JSONObject jsonRoot = new JSONObject(json);

      temperature = jsonRoot.getDouble(Define.TEMPERATURE_KEY);
      humidity = jsonRoot.getDouble(Define.HUMIDITY_KEY);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    Log.i("delvelop", "temperature : " + temperature + ", humidity : " + humidity);

    return true;
  }

  @Override
  public String serialize() {
    JSONObject jsonRoot = new JSONObject();
    try {
      jsonRoot.put(Define.TEMPERATURE_KEY, temperature);

      jsonRoot.put(Define.HUMIDITY_KEY, humidity);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonRoot.toString();
  }

  public void setRev(String reversion) {
    rev = reversion;
  }

  public double getTemperature() {
    return temperature;
  }

  public double getHumidity() {
    return humidity;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public void setHumidity(double humidity) {
    this.humidity = humidity;
  }

  public String getRev() {
    return rev;
  }
}
