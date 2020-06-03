package com.example.glyndwrtimetables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity
{
    // Interface
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupControls();
    }   //  protected void onCreate(Bundle savedInstanceState)

    protected void setupControls()
    {
        backButton = findViewById(R.id.about_back_button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        }); //  backButton.setOnClickListener(new View.OnClickListener()

    }   //  protected void setupControls()

}   //  public class AboutActivity extends AppCompatActivity
