package com.example.headstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Random;

public class NewJob extends AppCompatActivity {

    /**
     * opens the android studio page to make a new job
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);
    }

    /**
     * reduces an email into an ID that can be used to store data in firebase
     * @param email the email
     * @return the reduced email
     */
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

    /**
     * Stores the job locally and also pushes it into firebase
     * @param v the view
     */
    public void onClickSubmit(View v){
        final Controller aController = (Controller) getApplicationContext();
        Employer currentEmployer = aController.getEmployer();

        Job newJob = new Job();

        EditText jobNameText = findViewById(R.id.jobNameText);
        String jobTitle = jobNameText.getText().toString();

        boolean uniqueJob = true;
        for(int i = 0; i < currentEmployer.getJobs().size(); i ++){
            if(jobTitle.equals(currentEmployer.getJobs().get(i).getJobTitle())){
                uniqueJob = false;
            }
        }

        newJob.setJobTitle(jobTitle);

        EditText jobInformationText = findViewById(R.id.jobInformationText);
        String jobInformation = jobInformationText.getText().toString();

        newJob.setJobDescription(jobInformation);

        EditText locationText = findViewById(R.id.locationText);
        String location = locationText.getText().toString();

        EditText addressText = findViewById(R.id.addressText);
        String address = addressText.getText().toString();

        newJob.setAddress(address);

        EditText requirementsText = findViewById(R.id.requirementsText);
        String requirements = requirementsText.getText().toString();

        newJob.setRequirements(requirements);

        EditText preferredSkillsText = findViewById(R.id.preferredSkillsText);
        String preferredSkills = preferredSkillsText.getText().toString();

        newJob.setSkills(preferredSkills);

        EditText scheduleText = findViewById(R.id.scheduleText);
        String schedule = scheduleText.getText().toString();

        newJob.setSchedule(schedule);

        EditText salaryText = findViewById(R.id.salaryText);
        String salary = salaryText.getText().toString();

        newJob.setSalary(salary);

        EditText benefitsText = findViewById(R.id.benefitsText);
        String benefits = benefitsText.getText().toString();

        newJob.setBenefits(benefits);

        RadioButton fullTime = findViewById(R.id.FullTime);
        RadioButton partTime = findViewById(R.id.PartTime);
        RadioButton internship = findViewById(R.id.Internship);
        RadioButton coOp = findViewById(R.id.Coop);

        if(fullTime.isChecked()){
            newJob.setJobType("Full Time");
        }else if(partTime.isChecked()){
            newJob.setJobType("Part Time");
        }else if(internship.isChecked()){
            newJob.setJobType("Internship");
        }else{
            newJob.setJobType("Co-Op");
        }

        RadioButton highSchoolButton = findViewById(R.id.CurrentHS);
        RadioButton highSchoolGraduateButton = findViewById(R.id.HSGrad);
        RadioButton collegeButton = findViewById(R.id.CollegeStd);
        RadioButton collegeGraduateButton = findViewById(R.id.CollegeGrad);

        if(highSchoolButton.isChecked()){
            newJob.setSchool("High School");
        }else if(highSchoolGraduateButton.isChecked()){
            newJob.setSchool("High School Graduate");
        }else if(collegeButton.isChecked()){
            newJob.setSchool("College");
        }else{
            newJob.setSchool("College Graduate");
        }

        EditText ageText = findViewById(R.id.ageText);
        String minimumAge = ageText.getText().toString();

        newJob.setAgeMinimum(minimumAge);

        String[] locationArray = location.split(",");
        if(locationArray.length == 3){
            String state = locationArray[0];
            String city = locationArray[1];
            String zipCode = locationArray[2];
            newJob.setState(state);
            newJob.setZipcode(zipCode);
            newJob.setCity(city);
        }

        EditText URLtext = findViewById(R.id.URLText);
        String URL = URLtext.getText().toString();

        newJob.setApplicationURL(URL);

        EditText keywordText = findViewById(R.id.keywordText);
        String keywords = keywordText.getText().toString();

        String[] keywordsArray = keywords.split(",");
        if(!keywords.equals("")){
            for(int i = 0; i < keywordsArray.length; i ++){
                newJob.getKeywords().add(keywordsArray[i].replaceAll(" ", "").toLowerCase());
            }
        }

        newJob.setCompanyID(emailReducer(currentEmployer.getEmail()));

        if(jobTitle.equals("") || jobInformation.equals("") || address.equals("") || requirements.equals("") || preferredSkills.equals("") || schedule.equals("") || salary.equals("") || benefits.equals("") || minimumAge.equals("") || newJob.getState().equals("") || newJob.getCity().equals("") || newJob.getZipcode().equals("") || !(fullTime.isChecked() || partTime.isChecked() || internship.isChecked() || coOp.isChecked()) || !(highSchoolButton.isChecked() || highSchoolGraduateButton.isChecked() || collegeButton.isChecked()|| collegeGraduateButton.isChecked()) || keywords.equals("") || URL.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "All Fields Must Be Filled", Toast.LENGTH_LONG);
            toast.show();
        }else if (uniqueJob){
            currentEmployer.addJob(newJob);

            Database jobs = new Database("Jobs");
            jobs.createJob(newJob);
            currentEmployer.updateFireBaseJobs();

            Intent intent = new Intent(this, employerMainPage.class);
            startActivity(intent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Job Name Must Be Unique", Toast.LENGTH_LONG);
            toast.show();
        }


    }

    /**
     * Creates 50 randomized jobs. Was meant for populating our data set and testing.
     * @param v the view
     */
    public void addRandom50(View v){
        final Controller aController = (Controller) getApplicationContext();
        Employer currentEmployer = aController.getEmployer();
        for(int i = 0; i < 50; i ++){
            Random rand = new Random();
            Job newJob = new Job();

            String jobTitle = "This is the Job Title: Number " + Integer.toString(i + 1);
            newJob.setJobTitle(jobTitle);
            newJob.setJobDescription("This is a fake job");
            newJob.setAddress("10 parkway Ave");

            String[] states = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
            int randState = rand.nextInt(states.length);
            newJob.setState(states[randState]);
            newJob.setZipcode("00000");
            newJob.setCity("Boston");
            newJob.setRequirements("These should be the requirements");
            newJob.setSkills("These should be the preferred skills");

            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Weekends"};

            int day1 = rand.nextInt(days.length);
            int day2 = rand.nextInt(days.length);
            while(day1 == day2){
                day2 = rand.nextInt(days.length);
            }
            newJob.setSchedule(days[day1] + " and " + days[day2]);

            int intSalary = rand.nextInt(19) + 12;
            String salary = Integer.toString(intSalary);
            newJob.setSalary(salary);
            newJob.setBenefits("These should be the benefits");

            String[] jobTypes = {"Full Time", "Part Time", "Internship", "Co-Op"};
            int randJobType = rand.nextInt(jobTypes.length);
            newJob.setJobType(jobTypes[randJobType]);

            String[] schoolTypes = {"High School", "High School Graduate", "College", "College Graduate"};
            int randSchoolType = rand.nextInt(schoolTypes.length);
            newJob.setSchool(schoolTypes[randSchoolType]);

            int minAge = rand.nextInt(9) + 14;
            String minimumAge = Integer.toString(minAge);
            newJob.setAgeMinimum(minimumAge);
            newJob.setCompanyID(emailReducer(currentEmployer.getEmail()));

            String[] keywordsArray = {"Biology", "Chemistry", "Engineering", "Accounting", "Computer Science", "Manufacturing", "Finance", "Business", "Management", "Software", "Programming", "Lab", "Marketing", "Design", "Quality", "Teacher", "Grocery Store", "Communication", "Public Speaking", "Advertising", "Banking", "Robotics", "Civil Engineering", "Biotechnology", "Medical", "CAD", "Education", "Safety", "Consultant", "Inspection", "Research", "Developer", "Cybersecurity", "Epidemiology", "Security", "Information", "Data", "Analytics", "Cooking", "Baking", "Mathematics", "Astronomy", "Forensics"};
            int keyword1 = rand.nextInt(keywordsArray.length);
            int keyword2 = rand.nextInt(keywordsArray.length);
            while(keyword1 == keyword2) {
                keyword2 = rand.nextInt(keywordsArray.length);
            }
            newJob.getKeywords().add(keywordsArray[keyword1].replaceAll(" ", "").toLowerCase());
            newJob.getKeywords().add(keywordsArray[keyword2].replaceAll(" ","").toLowerCase());

            String[] urls = {"https://www.massacademy.org/admissions/", "https://youtu.be/dQw4w9WgXcQ", "http://www.trex-game.skipser.com"};
            int randomUrl = rand.nextInt(urls.length);
            newJob.setApplicationURL(urls[randomUrl]);

            Database jobs = new Database("Jobs");
            jobs.createJob(newJob);
            currentEmployer.addJob(newJob);
        }

        currentEmployer.updateFireBaseJobs();
    }
}
