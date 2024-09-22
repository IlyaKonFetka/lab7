package org.example.model;

import org.example.interfaces.Validatable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Person extends Element implements Validatable, Serializable, Comparable<Person> {
    private Integer id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным,
                    // Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null,
                                                  // Значение этого поля должно генерироваться автоматически
    private Long height; //Поле не может быть null, Значение поля должно быть больше 0
    private Long weight; //Поле не может быть null, Значение поля должно быть больше 0
    private String passportID; //Длина строки должна быть не меньше 4, Поле не может быть null
    private Color hairColor; //Поле может быть null
    private Location location; //Поле может быть null
    private long ownerID;


    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy [HH:mm]");

    public Person() {
    }

    public Person(Integer id,
                  String name,
                  Coordinates coordinates,
                  Location location,
                  Long height,
                  Long weight,
                  String passportID,
                  Color hairColor,
                  Long ownerID) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.location = location;
        this.height = height;
        this.weight = weight;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.ownerID = ownerID;
    }



    @Override
    public int compareTo(Person o) {
        return CharSequence.compare(this.name, o.name);
    }

    @Override
    public boolean validate(){
        return validateName() &
               validateCoordinates() &
               validateCreationDate() &
               validateHeight() &
               validateWeight() &
               validatePassportID() &
               validateLocation();
    }

    public boolean validateName(){return name != null && !name.isEmpty();}
    public boolean validateCoordinates(){return coordinates != null && coordinates.validate();}
    public boolean validateCreationDate(){return creationDate != null;}
    public boolean validateHeight(){return height != null && height > 0;}
    public boolean validateWeight(){return weight != null && weight > 0;}
    public boolean validatePassportID(){return passportID != null && passportID.length() >= 4;}
    public boolean validateLocation(){return location == null || location.validate();}


    @Override
    public String toString() {
        String locationString = location==null?"null": location.toString();
        String hairColorString = hairColor==null?"null": hairColor.toString();
        return "Name: " + name + "\n" +
                "\tid: " + id + ", passportID: " + passportID + ", creationDate: " + creationDate.format(timeFormatter) +
                ", height: " + height + ", weight: " + weight + "\n" +
                "\thairColor: " + hairColorString + "\n" +
                "\tCoordinates: " + coordinates.toString() + "\n" +
                "\tLocation: " + locationString;
    }

    public String getPassportID() {
        return passportID;
    }
    public Long getWeight() {
        return weight;
    }
    public String getName() {
        return name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getID() {
        return id;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public Long getHeight() {
        return height;
    }
    public void setHeight(Long height) {
        this.height = height;
    }
    public void setWeight(Long weight) {
        this.weight = weight;
    }
    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }
    public Color getHairColor() {
        return hairColor;
    }
    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public static DateTimeFormatter getTimeFormatter() {
        return timeFormatter;
    }
    public static void setTimeFormatter(DateTimeFormatter timeFormatter) {
        Person.timeFormatter = timeFormatter;
    }
    public long getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }
}