package com.testfxlab.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Objects;

public class Human {

    private Integer humanId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final IntegerProperty age;
    private final ObjectProperty<LocalDate> birthdate;

    public Human() {
        this("");
    }

    public Human(String firstName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.birthdate = new SimpleObjectProperty<>();
    }

    public Human(Integer humanId, String firstName, String lastName, LocalDate birthdate) {
        this.humanId = humanId;
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.age = new SimpleIntegerProperty();
        this.birthdate = new SimpleObjectProperty<>();
        setBirthdate(birthdate);
    }

    public Human(Human human) {
        this(human.getHumanId(), human.getFirstName(), human.getLastName(), human.getBirthdate());
    }

    public void setHumanId(Integer humanId) {
        this.humanId = humanId;
    }

    public Integer getHumanId() {
        return humanId;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public LocalDate getBirthdate() {
        return birthdate.get();
    }

    public ObjectProperty<LocalDate> birthdateProperty() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate.set(birthdate);
        if (birthdate != null) {
            age.set(birthdate.until(LocalDate.now()).getYears());
        }
    }

    public boolean isNew() {
        return humanId == null;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Human human = (Human) o;
        return Objects.equals(humanId, human.humanId);
    }

    @Override
    public String toString() {
        return "Human{"
                + "humanId=" + humanId
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", age=" + age
                + ", birthdate=" + birthdate
                + "} " + super.toString();
    }
}
