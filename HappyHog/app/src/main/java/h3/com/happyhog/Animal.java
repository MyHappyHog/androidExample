package h3.com.happyhog;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

import h3.com.happyhog.Dropbox.EnviromentSetting;
import h3.com.happyhog.Dropbox.RelaySetting;
import h3.com.happyhog.Dropbox.SensingInfo;

/**
 * animal class - it has animal information
 *
 * @version         1.00 (2016.01.23)
 * @author          ngh
 */
public class Animal implements Parcelable {
    private String key;

    private int img = R.drawable.default_animal_img;

    private SensingInfo sensingInfo = new SensingInfo();
    private EnviromentSetting envSetting = new EnviromentSetting();
    private RelaySetting relaySetting = new RelaySetting();

    private String name = "default name";
    private String memo = "default memo";
    private String device = "default device";

    public Animal() {
        key = UUID.randomUUID().toString().replace("-", "");
    }

    public String getState() {
        return "temperature : " + sensingInfo.getTemperature() + ", humidity : " + sensingInfo.getHumidity();
    }

    @Override
    public boolean equals(Object o) {
        return ((Animal)o).key.equals(this.key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // sending data method
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeInt(img);
        dest.writeString("" + sensingInfo.getTemperature());
        dest.writeString("" + sensingInfo.getHumidity());
        dest.writeString(name);
        dest.writeString(memo);
        dest.writeString(device);
    }

    // receiving data method
    public static final Parcelable.Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel source) {
            Animal animal = new Animal();
            animal.setKey(source.readString());
            animal.setImg(source.readInt());
            animal.setTemperature(source.readString());
            animal.setHumidity(source.readString());
            animal.setName(source.readString());
            animal.setMemo(source.readString());
            animal.setDevice(source.readString());

            return animal;
        }

        @Override
        public Animal[] newArray(int size) {
            return null;
        }
    };

    // Getter and Setter
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getImg() {
        return this.img;
    }

    public String getTemperature() {
        return "" + sensingInfo.getTemperature();
    }

    public String getHumidity() {
        return "" + sensingInfo.getHumidity();
    }

    public String getName() {
        return this.name;
    }

    public String getMemo() {
        return this.memo;
    }

    public String getDevice() {
        return this.device;
    }

    public SensingInfo getSensingInfo() {
        return sensingInfo;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setTemperature(String temperature) {
        sensingInfo.setTemperature(Double.parseDouble(temperature));
    }

    public void setHumidity(String humidity) {
        sensingInfo.setHumidity(Double.parseDouble(humidity));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
