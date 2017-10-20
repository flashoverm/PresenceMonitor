package de.thral.presencemonitor.person;

/**
 * Created by Markus Thral on 18.10.2017.
 */

public class Person implements Comparable<Person>{

    private final String firstName;
    private final String lastName;

    private int relevance;
    private boolean present;

    public Person (String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        this.relevance = 1;
        this.present = false;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getRelevance() {
        return relevance;
    }

    public boolean isPresent() {
        return present;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public void tooglePresence(){
        if(this.isPresent()){
            this.present = false;
        } else {
            this.present = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null)
            return false;
        return lastName != null ? lastName.equals(person.lastName) : person.lastName == null;

    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Person person) {
        return this.lastName.compareTo(person.getLastName());
    }
}
