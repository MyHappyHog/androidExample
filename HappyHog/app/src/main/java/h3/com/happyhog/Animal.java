package h3.com.happyhog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * animal class - it has animal information
 *
 * @version         1.00 (2016.01.23)
 * @author          ngh
 */
public class Animal implements Parcelable {
    private int img = R.drawable.default_animal_img;

    private String temperature = "-128";
    private String humidity = "-128";

    private String name = "default name";
    private String memo = "default memo";
    private String device = "default device";

    public String getState() {
        return "temperature : " + temperature + ", humidity : " + humidity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // sending data method
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(img);
        dest.writeString(temperature);
        dest.writeString(humidity);
        dest.writeString(name);
        dest.writeString(memo);
        dest.writeString(device);
    }

    // receiving data method
    public static final Parcelable.Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel source) {
            Animal animal = new Animal();
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
    public int getImg() {
        return this.img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return this.humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
