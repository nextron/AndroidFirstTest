package com.example.androidfirsttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//  Declaring the variables
    EditText edtName;
    Button btnAdd;
    TextView txvFees,txvHours,txvTotalFees,txvTotalHours;
    Spinner spCourses;
    CheckBox chkAccomodation, chkMedical;
    RadioButton rdbGrad, rdbUGrad;

    //Array list to store the courses
    ArrayList<Course> courseList = new ArrayList<>();

    //Index of selected item in spinner
    static int selectedCourseIndex = 0;

    //Array list to store the added courses
    ArrayList<Course> addedCourses = new ArrayList<Course>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the variables
        edtName = findViewById(R.id.edtName);
        btnAdd = findViewById(R.id.btnAdd);
        txvFees = findViewById(R.id.txvFees);
        txvHours = findViewById(R.id.txvHours);
        txvTotalFees = findViewById(R.id.txvTotalFees);
        txvTotalHours = findViewById(R.id.txvTotalHours);
        spCourses = findViewById(R.id.spCourses);
        chkAccomodation = findViewById(R.id.chkAccomodation);
        chkMedical = findViewById(R.id.chkMedical);
        rdbGrad = findViewById(R.id.rdbGrad);
        rdbUGrad = findViewById(R.id.rdbUGrad);

        //calling fill data
        fillData();
        updateTotalFeesAndHours();

        //calling getCourseNames() to get the course name in form of array
        String[] courseNames = getCourseNames();

        //Array Adaptor to display the course names
        ArrayAdapter aaCourseNames = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, courseNames);
        spCourses.setAdapter(aaCourseNames);

        //Handling course selection and displaying course gees and hours/week
        spCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourseIndex = position;
                String fees = courseList.get(position).getFees() + " $";
                txvFees.setText(fees);
                String hours = courseList.get(position).getHours() + " Hours";
                txvHours.setText(hours);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Handling CheckBox Events
        chkAccomodation.setOnCheckedChangeListener(new CheckBoxEvents());
        chkMedical.setOnCheckedChangeListener(new CheckBoxEvents());

        //Handling Add Button
        btnAdd.setOnClickListener(v -> {
            if(!checkCourse(courseList.get(selectedCourseIndex).getName())){
                int resHours = checkHours();
                if(resHours == 0){
                    addedCourses.add(courseList.get(selectedCourseIndex));
                    updateTotalFeesAndHours();
                }else{
                    Toast.makeText(this, "Sorry, You cannot add more than " + resHours +" Hours.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(MainActivity.this, "Sorry, This course has been already added.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Handling CheckBox Events
    private class CheckBoxEvents implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            updateTotalFeesAndHours();
        }
    }

    //Function that will update the total fees and hours
    private void updateTotalFeesAndHours(){
        int totalHours = 0;
        double totalFees = 0;
        for(Course cr: addedCourses){
            totalFees += cr.getFees();
            totalHours += cr.getHours();
        }
        if(chkMedical.isChecked()){
            totalFees += 700;
        }
        if(chkAccomodation.isChecked()){
            totalFees += 1000;
        }
        String totalFeesString = totalFees + " $";
        String totalHoursString = totalHours + " Hours";
        txvTotalHours.setText(totalHoursString);
        txvTotalFees.setText(totalFeesString);
    }

    //mehtod to check if the total hours are exceeding the limit or not
    private int checkHours(){
        int totalHours = 0;
        for(Course cr: addedCourses){
            totalHours += cr.getHours();
        }
        totalHours += courseList.get(selectedCourseIndex).getHours();
        if(rdbGrad.isChecked()){
            if(totalHours >= 21){
                return 21;
            }
        }
        if(rdbUGrad.isChecked()){
            if(totalHours >= 19){
                return 19;
            }
        }
        return 0;
    }

    //method to check if the course is already added or not
    //return true if it's already added
    private boolean checkCourse(String courseName){
        for(Course cr: addedCourses){
            if(cr.getName().equals(courseName))
                return true;
        }
        return false;
    }

    //method to get the course names
    private String[] getCourseNames(){
        String[] courseNames = new String[courseList.size()];
        for(int i = 0; i < courseList.size(); i++){
            courseNames[i] = courseList.get(i).getName();
        }
        return courseNames;
    }

    //method to fill data
    private void fillData(){
        courseList.add(new Course("Java",1300,6));
        courseList.add(new Course("Swift",1500,5));
        courseList.add(new Course("iOS",1350,5));
        courseList.add(new Course("Android",1400,7));
        courseList.add(new Course("Database",1000,4));
    }
}