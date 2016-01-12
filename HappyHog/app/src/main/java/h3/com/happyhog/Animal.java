package h3.com.happyhog;

/**
 * Created by ngh1 on 2015-12-29.
 */
public class Animal {
    public int img;

    public String temperature;
    public String humidity;

    public String name;
    public String memo;
    public String device;

    Animal(int img) {
        this.img = img;
        this.temperature = "0";
        this.humidity = "0";
        this.name = "Default name";
        this.memo = "Default memo";
        this.device = "Default device";
    }

    Animal(int img, String temperature, String humidity) {
        this.img = img;
        this.temperature = temperature;
        this.humidity = humidity;
    }
}
