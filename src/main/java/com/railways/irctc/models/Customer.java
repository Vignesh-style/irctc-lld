package com.railways.irctc.models;

public class Customer {
    private String name;
    private int age;
    private String gender;
    private SeatType preference;

    public Customer(String name, int age, String gender, SeatType preference) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.preference = preference;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public SeatType getPreference() { return preference; }

    @Override
    public String toString() {
        return name + " (" + age + ", " + gender + ", prefers " + preference + ")";
    }
}
