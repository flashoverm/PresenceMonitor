package de.thral.presencemonitor;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import de.thral.presencemonitor.person.Person;

/**
 * Created by Markus Thral on 18.10.2017.
 */

public class StorageLayer {

    private static final String PERSONS_PATH = "persons.json";
    private static final Type PERSON_TYPE = new TypeToken<List<Person>>() {}.getType();

    private File folder;
    private Gson gson;

    public StorageLayer(Context context){
        this.folder = context.getFilesDir();
        this.gson = new Gson();
    }

    public List<Person> getAllPersons(){
        try {
            return this.readJson(folder.getPath() + "/" + PERSONS_PATH);
        } catch (FileNotFoundException e){
            return new LinkedList<>();
        }
    }

    public List<Person> getPersonsPresent() {
        List<Person> present = new LinkedList<>();
        for(Person person : getAllPersons()){
            if(person.isPresent()){
                present.add(person);
            }
        }
        return present;
    }

    public boolean updatePersons(List<Person> updated){
        return this.writeJson(folder.getPath()+"/"+PERSONS_PATH, updated);
    }

    public boolean updatePerson(Person updated) {
        List<Person> list = getAllPersons();
        for(Person person : list){
            if(person.equals(updated)){
                person.setPresent(updated.isPresent());
            }
        }
        return this.writeJson(folder.getPath()+"/"+PERSONS_PATH, list);
    }

    private boolean writeJson(String path, List<Person> list){
        try (Writer writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(list, writer);
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private List<Person> readJson(String path) throws FileNotFoundException{
        JsonReader reader = new JsonReader(new FileReader(path));
        return gson.fromJson(reader, PERSON_TYPE);
    }


    /*
        Unused methods
     */
    public boolean addPerson(Person person){
        List<Person> newList = this.getAllPersons();
        newList.add(person);
        return this.writeJson(folder.getPath()+"/"+PERSONS_PATH, newList);
    }

    public boolean removePerson(Person person){
        List<Person> newList = this.getAllPersons();
        if(newList.remove(person)){
            return this.writeJson(folder.getPath()+"/"+PERSONS_PATH, newList);
        }
        return false;
    }
}
