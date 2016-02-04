package h3.com.happyhog;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ngh1 on 2016-01-23.
 */

// sqlite needed...
public class AnimalDatabase {
    private static final String ANIMAL_KEY = "_key";

    private static final String ANIMAL_IMG = "_img";

    private static final String ANIMAL_TEMPERATURE = "_temperature";
    private static final String ANIMAL_HUMIDITY = "_humidity";

    private static final String ANIMAL_NAME = "_name";
    private static final String ANIMAL_MEMO = "_memo";
    private static final String ANIMAL_DEVICE = "_device";

    private static final String ANIMALS_NAME_LIST = "name_list";
    private static final String ANIMALS_NAME_DELIM = "\\|";

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    private static AnimalDatabase instance;

    private static ArrayList<Animal> animalsList;
    private static ArrayList<String> animalsKeyList;

    public static AnimalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AnimalDatabase(context);
        }
        return instance;
    }

    private AnimalDatabase(Context context) {
        sharedPreferences = context.getSharedPreferences("HappyHog", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        animalsKeyList = new ArrayList<String>();
        animalsList = new ArrayList<Animal>();

        if (!isAnimalEmpty()) {
            loadAnimalsKeyList();
            loadAnimalsList();
        }
    }

    private void loadAnimalsKeyList() {
        String[] animals = parseAnimal(sharedPreferences.getString(ANIMALS_NAME_LIST, null));
        // maybe occur classCastException...?

        Collections.addAll(animalsKeyList, animals);
    }

    private void loadAnimalsList() {
        for (String animalKey : animalsKeyList) {
            animalsList.add(getAnimal(animalKey));
        }
    }

    private Animal getAnimal(String animalKey) {
        Animal animal = new Animal();

        animal.setKey(
                sharedPreferences.getString(animalKey, "KEY_LOAD_FAILED"));
        animal.setImg(
                sharedPreferences.getInt(animalKey + ANIMAL_IMG, animal.getImg()));
        animal.setTemperature(
                sharedPreferences.getString(animalKey + ANIMAL_TEMPERATURE, animal.getTemperature()));
        animal.setHumidity(
                sharedPreferences.getString(animalKey + ANIMAL_HUMIDITY, animal.getHumidity()));
        animal.setName(
                sharedPreferences.getString(animalKey + ANIMAL_NAME, animal.getName()));
        animal.setMemo(
                sharedPreferences.getString(animalKey + ANIMAL_MEMO, animal.getMemo()));
        animal.setDevice(
                sharedPreferences.getString(animalKey + ANIMAL_DEVICE, animal.getDevice()));

        return animal;
    }

    private boolean setAnimal(Animal animal) {
        String animalKey = animal.getKey();

        editor.putInt(animalKey + ANIMAL_IMG, animal.getImg())
                .putString(animalKey + ANIMAL_TEMPERATURE, animal.getTemperature())
                .putString(animalKey + ANIMAL_HUMIDITY, animal.getHumidity())
                .putString(animalKey + ANIMAL_NAME, animal.getName())
                .putString(animalKey + ANIMAL_MEMO, animal.getMemo())
                .putString(animalKey + ANIMAL_DEVICE, animal.getDevice())
                .apply();

        return true;
    }

    private String[] parseAnimal(String animals) {
        String[] temp = animals.split(ANIMALS_NAME_DELIM);
        List<String> separateAnimals = new ArrayList<String>();
        Collections.addAll(separateAnimals, temp);
        separateAnimals.remove("null");

        return separateAnimals.toArray(new String[separateAnimals.size()]);
    }

    public boolean isAnimalEmpty() {
        return sharedPreferences.getString(ANIMALS_NAME_LIST, null) == null;
    }

    public boolean updateAnimal(Animal animal) {
        boolean found = false;

        if (animalsList.contains(animal)) {
            setAnimal(animal);

            int it = animalsList.indexOf(animal);
            animalsList.remove(it);
            animalsList.add(it, getAnimal(animal.getKey()));

            found = true;
        }

        return found;
    }

    public void addAnimal(Animal animal) {
        editor.putString(animal.getKey(), animal.getKey()).apply();
        editor.putString(ANIMALS_NAME_LIST,
                sharedPreferences.getString(ANIMALS_NAME_LIST, null)
                        + "|" + animal.getKey()).apply();
        setAnimal(animal);
        animalsList.add(animal);
    }

    public ArrayList<Animal> getAnimalsList() {
        return animalsList;
    }

    private ArrayList<String> getAnimalsKeyList() {
        return animalsKeyList;
    }

    public void clear() {
        editor.clear().apply();
        animalsList.clear();
        animalsKeyList.clear();
    }

    @Override
    public String toString() {
        return sharedPreferences.getString(ANIMALS_NAME_LIST, "NOT EXIST");
    }
}


