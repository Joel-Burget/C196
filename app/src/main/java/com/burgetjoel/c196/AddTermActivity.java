package com.burgetjoel.c196;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTermActivity extends AppCompatActivity {

    EditText name_input, start_date_input, end_date_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        name_input = findViewById(R.id.name_input);
        start_date_input = findViewById(R.id.start_date_input);
        end_date_input = findViewById(R.id.end_date_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDB = new DatabaseHelper(AddTermActivity.this);
                myDB.addTerm(name_input.getText().toString().trim(), start_date_input.getText().toString().trim(), end_date_input.getText().toString().trim());
                finish();
            }
        });
    }
}