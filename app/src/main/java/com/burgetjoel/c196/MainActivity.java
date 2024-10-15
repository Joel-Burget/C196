package com.burgetjoel.c196;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgetjoel.c196.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    DatabaseHelper termDB;
    ArrayList<String> term_id, term_name, term_start_date, term_end_date;
    TermAdapter customAdapter;
    RecyclerView recyclerView;
    FloatingActionButton add_term_button;
    ImageView empty_imageview;
    TextView no_data;
    public static final String CHANNEL_ID = "general_notifications_channel";
    Button termTrackerButton, schedulerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout1 = inflater.inflate(R.layout.activity_main, null);
        View layout2 = inflater.inflate(R.layout.activity_startup, null);

        setContentView(layout2);
        termTrackerButton = findViewById(R.id.termTrackerButton);
        schedulerButton = findViewById(R.id.schedulerButton);

        createNotificationChannel();
        NotificationHelper.createNotificationChannel(this);

        termTrackerButton.setOnClickListener(v -> {
            setContentView(layout1);
            setLayoutOne();
        });

        schedulerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SchedulerActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    void storeTermsInArrays(){
        Cursor cursor = termDB.readAllTerms();

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            while(cursor.moveToNext()){
                term_id.add(cursor.getString(0));
                term_name.add(cursor.getString(1));
                term_start_date.add(cursor.getString(2));
                term_end_date.add(cursor.getString(3));

                empty_imageview.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all");
        builder.setMessage("Are you sure you want to delete all data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper myDB = new DatabaseHelper(MainActivity.this);
                myDB.deleteAllTermData();
                //refresh Activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {

        });
        builder.create().show();
    }

    private void createNotificationChannel(){
            CharSequence name = "Term Tracker";
            String description = "Alerts for activity start or end";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("general_notifications_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // Check if the notification permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationPermissionHelper.checkNotificationPermission(this);
                NotificationHelper.createNotificationChannel(this);
                NotificationHelper.showNotification(this, "test", "Fred is a kitty.");

            } else {
                // Permission denied, handle this accordingly
                Log.i("Error: ", "Cannot send notification without permission");
            }
        }
    }

    public void setLayoutOne(){
        recyclerView = findViewById(R.id.termRecycler);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        termDB = new DatabaseHelper(MainActivity.this);
        term_id = new ArrayList<>();
        term_name = new ArrayList<>();
        term_start_date = new ArrayList<>();
        term_end_date = new ArrayList<>();
        storeTermsInArrays();
        add_term_button = findViewById(R.id.add_term_button);
        add_term_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTermActivity.class);
                startActivity(intent);
            }
        });

        customAdapter = new TermAdapter(MainActivity.this, this, term_id, term_name, term_start_date, term_end_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
}