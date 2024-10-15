package com.burgetjoel.c196;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateTermActivity extends AppCompatActivity {

    EditText name_update, start_date_update, end_date_update;
    Button update_button, delete_button;
    String id, name, startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_update);

        name_update = findViewById(R.id.name_update);
        start_date_update = findViewById(R.id.start_date_update);
        end_date_update = findViewById(R.id.end_date_update);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        getAndSetIntentData();

        //Set Actionbar
        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle(name);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateTermActivity.this);

                String newName = name_update.getText().toString();
                String newStart = start_date_update.getText().toString();
                String newEnd = end_date_update.getText().toString();
                myDB.updateTermData(id, newName, newStart, newEnd);
                setResult(RESULT_OK, null);
                Intent intent = new Intent(UpdateTermActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("term_id") && getIntent().hasExtra("termName") && getIntent().hasExtra("termStartDate") && getIntent().hasExtra("termEndDate")){

            //getting ID from database
            id = getIntent().getStringExtra("term_id");

            //getting Data from intent
            name = getIntent().getStringExtra("termName");
            startDate = getIntent().getStringExtra("termStartDate");
            endDate = getIntent().getStringExtra("termEndDate");

            //setting data from intent
            name_update.setText(name);
            start_date_update.setText(startDate);
            end_date_update.setText(endDate);

        }else{
            Toast.makeText(this, "No Data from intent", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateTermActivity.this);
                myDB.deleteOneTermRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}