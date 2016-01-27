package h3.com.happyhog;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by ngh1 on 2016-01-23.
 */

// sqlite needed...
public class AnimalDatabase {
    private static final String ANIMAL_IMG = "_img";

    private static final String ANIMAL_TEMPERATURE = "_temperature";
    private static final String ANIMAL_HUMIDITY = "_humidity";

    private static final String ANIMAL_NAME = "_name";
    private static final String ANIMAL_MEMO = "_memo";
    private static final String ANIMAL_DEVICE = "_device";

    private static final String ANIMALS_NAME_LIST = "name_list";
    private static final String ANIMALS_NAME_DELIM = "---";

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;


    private static AnimalDatabase instance;

    public static AnimalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AnimalDatabase(context);
        }
        return instance;
    }

    private AnimalDatabase(Context context) {
        sharedPreferences = context.getSharedPreferences("HappyHog", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isAnimalEmpty() {
        return sharedPreferences.getString(ANIMALS_NAME_LIST, null) == null;
    }

    public boolean containAnimal(String animalNameWanted) {
        ArrayList<String> animalsNameList = getAnimalsNameList();

        if (animalsNameList == null) {
            return false;
        }

        for (String animalName : animalsNameList) {
            if (animalNameWanted.equals(animalName)) {
                return true;
            }
        }

        return false;
    }

    // getter and setter : animal name
    public ArrayList<String> getAnimalsNameList() {
        String animalsName = sharedPreferences.getString(ANIMALS_NAME_LIST, null);

        if (animalsName == null) {
            return null;
        }

        String[] separateAnimalsName = animalsName.split(ANIMALS_NAME_DELIM);
        ArrayList<String> animalsNameList = new ArrayList<String>();

        for (String animalName : separateAnimalsName) {
            animalsNameList.add(animalName);
        }

        if (animalsNameList.get(0).equals("null")) {
            animalsNameList.remove(0);
        }

        return animalsNameList;
    }

    public void setAnimalsNameList(ArrayList<String> animalsNameList) {
        String animalsName = "";

        for (String animalName : animalsNameList) {
            animalsName += ANIMALS_NAME_DELIM + animalName;
        }

        editor.putString(ANIMALS_NAME_LIST, animalsName).apply();
    }

    public void setAnimalsName (String animalName) {
        String animalsName = sharedPreferences.getString(ANIMALS_NAME_LIST, null);

        animalsName += ANIMALS_NAME_DELIM + animalName;
        editor.putString(ANIMALS_NAME_LIST, animalsName).apply();
    }

    // animal list : getter
    public ArrayList<Animal> getAnimalsList() {
        ArrayList<String> animalNameList = getAnimalsNameList();
        ArrayList<Animal> animals = new ArrayList<Animal>();

        for (String animalName : animalNameList) {
            animals.add(getAnimal(animalName));
        }

        return animals;
    }

    public boolean removeAnimal(String animalName) {
        String animalsName = sharedPreferences.getString(ANIMALS_NAME_LIST, null);

        if (animalsName == null || !containAnimal(animalName)) {
            return false;
        }

        ArrayList<String> animalsNameList = getAnimalsNameList();
        animalsNameList.remove(animalName);

        setAnimalsNameList(animalsNameList);

        return true;
    }

    // throw exception if getAnimal() == default Animal
    public Animal getAnimal(String animalName) {
        Animal animal = new Animal();

        animal.setImg(
                sharedPreferences.getInt(animalName + ANIMAL_IMG, animal.getImg()));
        animal.setTemperature(
                sharedPreferences.getString(animalName + ANIMAL_TEMPERATURE, animal.getTemperature()));
        animal.setHumidity(
                sharedPreferences.getString(animalName + ANIMAL_HUMIDITY, animal.getHumidity()));
        animal.setName(
                sharedPreferences.getString(animalName + ANIMAL_NAME, animal.getName()));
        animal.setMemo(
                sharedPreferences.getString(animalName + ANIMAL_MEMO, animal.getMemo()));
        animal.setDevice(
                sharedPreferences.getString(animalName + ANIMAL_DEVICE, animal.getDevice()));

        return animal;
    }

    public void putAnimal(Animal animal) {
        String animalName = animal.getName();

        editor.putInt(animalName + ANIMAL_IMG, animal.getImg())
                .putString(animalName + ANIMAL_TEMPERATURE, animal.getTemperature())
                .putString(animalName + ANIMAL_HUMIDITY, animal.getHumidity())
                .putString(animalName + ANIMAL_NAME, animal.getName())
                .putString(animalName + ANIMAL_MEMO, animal.getMemo())
                .putString(animalName + ANIMAL_DEVICE, animal.getDevice())
                .apply();

        setAnimalsName(animalName);
    }

    public void clear() {
        editor.clear().apply();
    }

    @Override
    public String toString() {
        return sharedPreferences.getString(ANIMALS_NAME_LIST, "NOT EXIST");
    }
}
