package com.cs442.project.team13;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.List;

public class CategorySearch extends AppCompatActivity {
    private String selectState="";
    private String selectCampus="";
    private String selectDepartment="";
    private String selectClass="";
    private String selectProfessor="";
    private Spinner spState;
    private Spinner spCampus;
    private Spinner spDepartment;
    private Spinner spClass;
    private Spinner spProfessor;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  " + getSupportActionBar().getTitle()));
        }
        spState = (Spinner) findViewById(R.id.spinState);
        spCampus = (Spinner) findViewById(R.id.spinUniversity);
        spDepartment= (Spinner) findViewById(R.id.spinDepartment);
        spClass= (Spinner) findViewById(R.id.spinClass);
        spProfessor= (Spinner) findViewById(R.id.spinProfessor);
        listView = (ListView) findViewById(R.id.bookListView);

        stateQuery(spState, "state");

    }
    public String stateQuery(final Spinner spinner, final String queryStr){
        //ParseQuery query = ParseQuery.getQuery("abook");
        ParseQuery query = ParseQuery.getQuery("Category");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ArrayList<String> stateList = new ArrayList<>();
                    for (ParseObject o : list) {
                        int count = 0;
                        if (!stateList.isEmpty()) {
                            for (String a : stateList) {
                                String b = o.getString(queryStr);
                                boolean stt = b.equals(a);
                                if (stt) {
                                    count++;
                                }
                            }
                            if (count == 0) {
                                stateList.add(o.getString(queryStr));
                            }
                        } else {
                            stateList.add(o.getString(queryStr));
                        }
                    }
                    stateList.add(0, "State");
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stateList);
                    Log.d("STState", " " + stateList.toString());
                    if (!stateAdapter.isEmpty()) {
                        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner.setAdapter(stateAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));

                                //if(position==0){
                                  //  selectState = "";
                                //}else {
                                    selectState = item.toString();
                                //}
                                Log.d("SelectingState", selectState);
                                universityQuery(spCampus, "university");
                                Log.d("Selected", selectState);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectState);
                            }
                        });
                    }

                } else {
                    Log.d("State", "Error: " + e.getMessage());
                }
            }
        });
        return selectState;

    }
    public String universityQuery(final Spinner spinner, final String queryStr){
        //ParseQuery query = ParseQuery.getQuery("abook");
        ParseQuery query = ParseQuery.getQuery("Category");
        query.whereEqualTo("state", selectState);
        Log.d("UniversityPrequerie", selectState);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ArrayList<String> stateList = new ArrayList<>();
                    for (ParseObject o : list) {
                        int count = 0;
                        if (!stateList.isEmpty()) {
                            for (String a : stateList) {
                                String b = o.getString(queryStr);
                                boolean stt = b.equals(a);
                                if (stt) {
                                    count++;
                                }
                            }
                            if (count == 0) {
                                stateList.add(o.getString(queryStr));
                            }
                        } else {
                            stateList.add(o.getString(queryStr));
                        }
                    }
                    stateList.add(0, "University");
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stateList);
                    Log.d("STTUNIVERSITY", " " + stateList.toString());
                    if (!stateAdapter.isEmpty()) {
                        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner.setAdapter(stateAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                selectCampus = item.toString();
                                departmentQuery(spDepartment, "department");
                                Log.d("Selected", selectCampus);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectCampus);
                            }
                        });
                    }

                } else {
                    Log.d("State", "Error: " + e.getMessage());
                }
            }
        });
        return selectCampus;
    }
    public String departmentQuery(final Spinner spinner, final String queryStr){
        //ParseQuery query = ParseQuery.getQuery("abook");
        ParseQuery query = ParseQuery.getQuery("Category");
        Log.d("PrequeryDepartment",selectCampus);
        query.whereEqualTo("state", selectState);
        query.whereEqualTo("university", selectCampus);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.d("DepartmentCount",""+list.size());
                    ArrayList<String> stateList = new ArrayList<>();
                    for (ParseObject o : list) {
                        int count =0;
                        if(!stateList.isEmpty()) {
                            for (String a : stateList) {
                                String b = o.getString(queryStr);
                                boolean stt =  b.equals(a);
                                if (stt) {
                                    count++;
                                }
                            }
                            if(count==0){
                                stateList.add(o.getString(queryStr));
                            }
                        }
                        else{
                            stateList.add(o.getString(queryStr));
                        }
                    }
                    stateList.add(0, "Department");
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stateList);
                    Log.d("STTDEPARTMENT", " " + stateList.toString());
                    if(!stateAdapter.isEmpty()){
                        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner.setAdapter(stateAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                selectDepartment = item.toString();
                                classQuery(spClass, "class");
                                Log.d("Selected", selectDepartment);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectDepartment);
                            }
                        });
                    }

                } else {Log.d("State", "Error: " + e.getMessage());}
            }
        });
        return selectDepartment;
    }
    public String classQuery(final Spinner spinner, final String queryStr){
        //ParseQuery query = ParseQuery.getQuery("abook");
        ParseQuery query = ParseQuery.getQuery("Category");
        query.whereEqualTo("state", selectState);
        query.whereEqualTo("university",selectCampus);
        query.whereEqualTo("department",selectDepartment);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ArrayList<String> stateList = new ArrayList<>();
                    for (ParseObject o : list) {
                        int count =0;
                        if(!stateList.isEmpty()) {
                            for (String a : stateList) {
                                String b = o.getString(queryStr);
                                boolean stt =  b.equals(a);
                                if (stt) {
                                    count++;
                                }
                            }
                            if(count==0){
                                stateList.add(o.getString(queryStr));
                            }
                        }
                        else{
                            stateList.add(o.getString(queryStr));
                        }
                    }
                    stateList.add(0, "Class");
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stateList);
                    Log.d("STTCLASS", " " + stateList.toString());
                    if(!stateAdapter.isEmpty()){
                        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner.setAdapter(stateAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                selectClass = item.toString();
                                professorQuery(spProfessor, "professor");
                                Log.d("Selected", selectClass);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectClass);
                            }
                        });
                    }

                } else {Log.d("State", "Error: " + e.getMessage());}
            }
        });
        return selectClass;
    }
    public String professorQuery(final Spinner spinner, final String queryStr) {
        //ParseQuery query = ParseQuery.getQuery("abook");
        ParseQuery query = ParseQuery.getQuery("Category");
        Log.d("PrequeryProfessor",selectClass);
        query.whereEqualTo("state", selectState);
        query.whereEqualTo("university",selectCampus);
        query.whereEqualTo("department", selectDepartment);
        query.whereEqualTo("class",selectClass);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ArrayList<String> stateList = new ArrayList<>();
                    for (ParseObject o : list) {
                        int count =0;
                        if(!stateList.isEmpty()) {
                            for (String a : stateList) {
                                String b = o.getString(queryStr);
                                boolean stt =  b.equals(a);
                                if (stt) {
                                    count++;
                                }
                            }
                            if(count==0){
                                stateList.add(o.getString(queryStr));
                            }
                        }
                        else{
                            stateList.add(o.getString(queryStr));
                        }
                    }
                    stateList.add(0, "Professor");
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stateList);
                    Log.d("STTPROFESSOR", " " + stateList.toString());
                    if(!stateAdapter.isEmpty()){
                        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner.setAdapter(stateAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);
                                if(parent!=null && parent.getChildAt(0) != null)
                                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                selectProfessor = item.toString();
                                finalQuery("owner");
                                Log.d("Selected", selectProfessor);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("Nothing Selected", selectProfessor);
                            }
                        });
                    }

                } else {Log.d("State", "Error: " + e.getMessage());}
            }
        });
        return selectProfessor;
    }
    public void finalQuery( final String queryStr) {
        ParseQuery query = ParseQuery.getQuery("Book");
        Log.d("PrequeryProfessor", selectClass);
       // query.whereEqualTo("state", selectState);
        //query.whereEqualTo("university",selectCampus);
        //query.whereEqualTo("department", selectDepartment);
        if(!"State".equalsIgnoreCase(selectState) || !selectState.isEmpty()){
            query.whereEqualTo("state", selectState);
        }
        if(!"University".equalsIgnoreCase(selectCampus)) {
            query.whereEqualTo("university", selectCampus);
        }
        if(!"Department".equalsIgnoreCase(selectDepartment)) {
            query.whereEqualTo("department", selectDepartment);
        }

        if(!"Class".equalsIgnoreCase(selectClass)) {
            query.whereEqualTo("class", selectClass);
        }
        if(!"Professor".equalsIgnoreCase(selectProfessor)) {
            query.whereEqualTo("professor", selectProfessor);
        }
        query.findInBackground(new FindCallback<Book>() {
            @Override
            public void done(List<Book> list, ParseException e) {
                if (e == null) {
                    SearchBookListAdapter searchBookListAdapter = new SearchBookListAdapter(CategorySearch.this, list);
                    searchBookListAdapter.setNotifyOnChange(true);
                    listView.setAdapter(searchBookListAdapter);
                } else {
                    Log.d("State", "Error: " + e.getMessage());
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                Book abook = (Book) parent.getItemAtPosition(pos);
                Intent intent = new Intent();
                intent.setClass(CategorySearch.this, DisplayBookInfoActivity.class);
                intent.putExtra("id", abook.getObjectId());
                startActivity(intent);
            }
        });
    }
}
