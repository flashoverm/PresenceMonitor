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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.thral.presencemonitor.person.Person;

/**
 * Created by Markus Thral on 19.10.2017.
 */

public class StorageLayer {

    private static final String PERSONS_PATH = "persons.json";
    private static final Type PERSON_TYPE = new TypeToken<List<Person>>() {}.getType();

    private File folder;
    private Gson gson;

    private List<Person> persons;
    private boolean filter;


    public StorageLayer(Context context){
        this.folder = context.getFilesDir();
        this.gson = new Gson();
        this.persons = getAllPersons();
        this.filter = false;
    }

    public List<Person> getPersonList(){
        return persons;
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

    public boolean isFiltered(){
        return filter;
    }

    public boolean addPerson(Person person){
        if(persons.add(person) && savePersons()){
            return true;
        }
        return false;
    }

    public boolean removePerson(Person person){
        if(persons.remove(person) && savePersons()) {
            return true;
        }
        return false;
    }

    public void tooglePresence(Person person){
        person.tooglePresence();
        savePersons();
    }

    public void tooglePresentRemoveFilter(){
        for(Person person : persons){
            person.setPresent(false);
        }
        removeFilter();
    }

    public boolean filterForPresent(){
        boolean onePresent = false;
        for(Person person : persons){
            if(person.isPresent()){
                onePresent = true;
                break;
            }
        }

        if(onePresent){
            Iterator<Person> iterator = persons.iterator();
            Person person;
            while(iterator.hasNext()){
                person = iterator.next();
                if(!person.isPresent()){
                    iterator.remove();
                }
            }
            filter = true;
        }
        return filter;
    }

    public void removeFilter(){
        for(Person person : getAllPersons()){
            if(!persons.contains(person)){
                persons.add(person);
            }
        }
        filter = false;
    }


    private boolean savePersons(){
        return this.writeJson(folder.getPath()+"/"+PERSONS_PATH, persons);
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

    private List<Person> readJson(String path) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(path));
        return gson.fromJson(reader, PERSON_TYPE);
    }

}
