package com.cs442.project.team13;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookCategoryActivity extends AppCompatActivity {
    private static final int POST_BOOK = 112;
    private static final String NONE ="None";

    Spinner spinnerState;
    Spinner spinnerUniversity;
    Spinner spinnerDeparment;
    Spinner spinnerClass;
    Spinner spinnerProfessor;
    ListView listView;

    String selectedState;
    String selectedUniversity;
    String selectedDepartment;
    String selectedClass;
    String selectedProfessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  " + getSupportActionBar().getTitle()));
        }
        setContentView(R.layout.activity_book_category);

        spinnerState = (Spinner) findViewById(R.id.state);
        spinnerUniversity = (Spinner) findViewById(R.id.university);
        spinnerDeparment = (Spinner) findViewById(R.id.department);
        spinnerClass = (Spinner) findViewById(R.id.class1);
        spinnerProfessor = (Spinner) findViewById(R.id.professor);


        populateState();

    }


    public void populateState(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> states, ParseException error) {
                if (states != null && !states.isEmpty()) {

                    Set<String> statesSet = new HashSet<String>();
                    List<String> statesList = new ArrayList<String>();

                    for (ParseObject state : states) {
                        String state1 = state.getString("state");
                        if (state1 != null && !state1.isEmpty())
                            statesSet.add(state1);
                    }

                    statesList.addAll(statesSet);
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, statesList);

                    if (!stateAdapter.isEmpty()) {
                        //stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerState.setAdapter(stateAdapter);
                        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));

                                selectedState = item.toString();
                                Log.d("Selecting", selectedState);
                                if (selectedState != null) {
                                    populateUniversity();
                                }
                                Log.d("Selected", selectedState);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectedState);
                            }
                        });
                    }


                }
            }
        });

    }


    public void populateUniversity(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category").whereEqualTo("state", selectedState);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException error) {
                if (parseObjects != null && !parseObjects.isEmpty()) {

                    Set<String> universitySet = new HashSet<String>();
                    List<String> universityList = new ArrayList<String>();

                    for (ParseObject parseObject:parseObjects) {
                        String university = parseObject.getString("university");
                        if(university!=null && !university.isEmpty())
                            universitySet.add(university);
                    }
                    universityList.addAll(universitySet);
                    ArrayAdapter<String> univeristyAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, universityList);


                    if (!univeristyAdapter.isEmpty()) {
                        univeristyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerUniversity.setAdapter(univeristyAdapter);
                        spinnerUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                selectedUniversity = item.toString();
                                Log.d("Selecting", selectedUniversity);
                                if(selectedUniversity!=null)
                                        populateDepartment();
                                Log.d("Selected", selectedUniversity);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectedUniversity);
                            }
                        });
                    }

                }
            }
        });


    }


    public void populateDepartment() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category").whereEqualTo("university", selectedUniversity);


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException error) {
                if (parseObjects != null) {

                    Set<String> departmentSet = new HashSet<String>();
                    List<String> departmentList = new ArrayList<String>();

                    for (ParseObject parseObject : parseObjects) {
                        String department = parseObject.getString("department");
                        if (department != null && !department.isEmpty())
                            departmentSet.add(department);
                    }
                    departmentList.addAll(departmentSet);
                    ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, departmentList);


                    if (!departmentAdapter.isEmpty()) {
                        departmentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerDeparment.setAdapter(departmentAdapter);
                        spinnerDeparment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));

                                selectedDepartment = item.toString();
                                Log.d("Selecting", selectedDepartment);
                                if (selectedDepartment != null)
                                    populateClass();
                                Log.d("Selected", selectedDepartment);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectedDepartment);
                            }
                        });
                    }

                }
            }
        });

    }

    public void populateClass(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category").whereEqualTo("department",selectedDepartment);


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException error) {
                if (parseObjects != null) {

                    Set<String> classSet = new HashSet<String>();
                    List<String> classList = new ArrayList<String>();

                    classList.add(NONE);


                    for (ParseObject parseObject:parseObjects) {
                        String department = parseObject.getString("class");
                        if(department!=null && !department.isEmpty())
                            classSet.add(department);
                    }

                    classList.addAll(classSet);
                    ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, classList);


                    if (!classAdapter.isEmpty()) {
                        classAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerClass.setAdapter(classAdapter);
                        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                selectedClass = item.toString();
                                Log.d("Selecting", selectedClass);
                                if(selectedClass!=null)
                                {
                                    populateProfessor();
                                    Log.d("Selected", selectedClass);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectedClass);
                            }
                        });
                    }

                }
            }
        });

    }



    public void populateProfessor(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category").whereEqualTo("class",selectedClass);


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException error) {
                if (parseObjects != null) {

                    Set<String> professorSet = new HashSet<String>();
                    List<String> professorList = new ArrayList<String>();

                    professorList.add(NONE);


                    for (ParseObject parseObject:parseObjects) {
                        String professor = parseObject.getString("professor");
                        if(professor!=null && !professor.isEmpty())
                            professorSet.add(professor);
                    }

                    professorList.addAll(professorSet);
                    ArrayAdapter<String> professorAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, professorList);


                    if (!professorAdapter.isEmpty()) {
                        professorAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerProfessor.setAdapter(professorAdapter);
                        spinnerProfessor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                selectedProfessor = item.toString();
                                Log.d("Selecting", selectedProfessor);
                                if(selectedClass!=null)
                                    Log.d("Selected", selectedProfessor);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectedProfessor);
                            }
                        });
                    }

                }
            }
        });

    }
    public void postBook(View view){
        Intent intent = new Intent(this, PostBookActivity.class);
        intent.putExtra("state",selectedState);
        intent.putExtra("university", selectedUniversity);
        intent.putExtra("department",selectedDepartment);
        intent.putExtra("class",selectedClass);
        intent.putExtra("professor",selectedProfessor);
        startActivityForResult(intent, POST_BOOK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case POST_BOOK:

                if (resultCode == RESULT_OK && null != data && data.getStringExtra("bookId")!=null) {
                    getIntent().putExtra("bookId",data.getStringExtra("bookId"));
                    setResult(RESULT_OK, getIntent());
                    BookCategoryActivity.this.finish();
                }
                break;
        }
    }
}
