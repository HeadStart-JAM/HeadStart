package com.example.headstart;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database testUsers = new Database("Users");
        // Database testEmployers = new Database("Employers");
        // Database testJobs = new Database("Jobs");

//      testUsers.addDefaultUser("name1234", "user1234@gmail.com", "1234");
//      testUsers.updateUserProfile("user1234", "MA", "test city", "00000", "Junior", "test school", "test description.", "000-000-0000", "17");
//
//      testEmployers.addDefaultEmployer("employer5678", "company5678", "employer5678@gmail.com", "5678");
//      testEmployers.updateEmployerProfile("employer5678", "MA", "test city", "00000", "10 Street", "test description", "000-000-0000");
//
//      testJobs.createJob("job9101112", "job9101112", "full-time", "test description", "MA", "test city", "00000", "10 Street", "req 1, req 2", "skill 1, skill 2", "weekdays", "$1000", "test benefits", "16", "11th", "school", "jeffwilcoxhotmailcom");
//      testJobs.createJob("job13141516", "job13141516", "internship", "test description 2", "MA", "test town", "11111", "50 Way", "req 3, req 4", "skill 3, skill 4", "weekends", "$5000", "test benefits 2", "14", "9th", "school", "jeffwilcoxhotmailcom");
//
//      Reference specUser = new Reference("Users", "user1234");
//      specUser.readUser();
//
//      Reference specEmployer = new Reference("Employers", "employer5678");
//      specEmployer.readEmployer();
//
//      Reference specJob = new Reference("Jobs", "job9101112");
//      specJob.readJob();

    }

    public void clickApplicantButton(View v){
        EditText emailText = findViewById(R.id.emailConfirmText);
        String email = emailText.getText().toString();

        EditText passwordText = findViewById(R.id.passwordText);
        String password = passwordText.getText().toString();

        applicantSignIn(email, password, v);
    }

    public void applicantSignIn(String email, String password, View v){
        findUser(email);
        final Controller aController = (Controller) getApplicationContext();
        User currentUser = aController.getUser();

        if(currentUser.getEmail().equals("")){

        }else{
            if(currentUser.comparePassword(password)){
                Database jobs = new Database("Jobs");
                for(int i = 0; i < jobs.populateRandom().size(); i++){
                    aController.getFilteredJobs().set(i, jobs.populateRandom().get(i));
                }
                aController.setJobRefreshNumber(0);
                performOpenJobListings(v);
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void findUser(final String email){
        final Controller aController = (Controller) getApplicationContext();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    User extracted = ds.getValue(User.class);
                    if(extracted.getEmail().equals(email)){
                        final Controller aController = (Controller) getApplicationContext();
                        aController.setUser(extracted);
                        break;
                    }
                }
                if(aController.getUser().getEmail().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MainActivity", "Failed to read value.", error.toException());
            }
        });
    }

    public String emailReducer(String email){
        ArrayList<String> characters = new ArrayList<String>();
        for(int i = 0; i < email.length(); i ++){
            characters.add(email.substring(i, i + 1));
        }
        characters.remove("@");
        characters.remove(".");
        String reduced = "";
        for(int i = 0; i < characters.size(); i++){
            reduced += characters.get(i);
        }
        return reduced;
    }

    public boolean isEmail(String email){
        int atTimes = 0;
        int periodTimes = 0;
        for(int i = 0; i < email.length(); i ++){
            if(email.substring(i, i + 1).equals("@")){
                atTimes += 1;
            }else if(email.substring(i, i + 1).equals(".")){
                periodTimes += 1;
            }
        }
        if(atTimes == 1 && periodTimes == 1){
            return true;
        }
        return false;
    }

    public void performOpenJobListings(View v){
        Intent intent = new Intent(this, jobListingPage.class);
        startActivity(intent);
    }

    public void guestButton(){
        final Controller aController = (Controller) getApplicationContext();
        aController.setUser(new User());

        Intent intent = new Intent(this, jobListingPage.class);
        startActivity(intent);
    }

    public void clickEmployerButton(View v){
        EditText emailText = findViewById(R.id.emailConfirmText);
        String email = emailText.getText().toString();

        EditText passwordText = findViewById(R.id.passwordText);
        String password = passwordText.getText().toString();

        employerSignIn(email, password, v);
    }

    public void employerSignIn(String email, String password, View v){
        findEmployer(email);
        final Controller aController = (Controller) getApplicationContext();
        Employer currentEmployer = aController.getEmployer();

        if(currentEmployer.getEmail().equals("")){

        }else{
            if(currentEmployer.comparePassword(password)){
                performOpenEmployerMainPage(v);
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void findEmployer(final String email){
        final Controller aController = (Controller) getApplicationContext();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference employersRef = database.getReference("Employers");
        employersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Employer extracted = ds.getValue(Employer.class);
                    if(extracted.getEmail().equals(email)){
                        final Controller aController = (Controller) getApplicationContext();
                        aController.setEmployer(extracted);
                        break;
                    }
                }
                if(aController.getEmployer().getEmail().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MainActivity", "Failed to read value.", error.toException());
            }
        });
    }

    public void performOpenEmployerMainPage(View v){
        Intent intent = new Intent(this, employerMainPage.class);
        startActivity(intent);
    }

    public void performCreateProfilePage(View v){
        Intent intent = new Intent(this, createProfile.class);
        startActivity(intent);
    }
}
