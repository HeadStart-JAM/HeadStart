package com.example.headstart;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {

    FirebaseDatabase database;
    DatabaseReference ref;

    public Database(String path) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference(path);
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return database;
    }

    public DatabaseReference getDatabaseReference() {
        return ref;
    }

    public void addDefaultUser(String ID, String name, String email, String password) {
        User user = new User();
        user.setDisplayName(name);
        user.setEmail(email);

        ref.child(ID).setValue(user);
        ref.child(ID).child("password").setValue(password);
    }

    public void updateUserProfile(String ID, String state, String city, String zipcode, String year, String school, String description, String phoneNumber, String age) {
        Profile profile = new Profile(state, city, zipcode, year, school, description, phoneNumber, age);

        ref.child(ID).child("profile").setValue(profile);
    }

    public void addDefaultEmployer(String ID, String name, String email, String password) {
        Employer employer = new Employer();
        employer.setDisplayName(name);
        employer.setEmail(email);

        ref.child(ID).setValue(employer);
        ref.child(ID).child("password").setValue(password);
    }

    public void updateEmployerProfile(String ID, String state, String city, String zipcode, String address, String description, String phoneNumber) {
        Profile profile = new Profile(ID, state, city, zipcode, description, phoneNumber);

        ref.child(ID).child("profile").setValue(profile);
    }

//    //EDIT FOR JOBS
//
//    public void addDefaultJob(String ID, String name, String email, String password) {
//        Employer employer = new Employer();
//        employer.setDisplayName(name);
//        employer.setEmail(email);
//
//        ref.child(ID).setValue(employer);
//        ref.child(ID).child("password").setValue(password);
//    }
//
//    public void updateJobInformation(String ID, String state, String city, String zipcode, String year, String school, String description, String phoneNumber, String age) {
//        Profile profile = new Profile(state, city, zipcode, year, school, description, phoneNumber, age);
//
//        ref.child(ID).child("profile").setValue(profile);
//    }
}