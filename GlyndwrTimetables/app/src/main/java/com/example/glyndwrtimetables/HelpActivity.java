package com.example.glyndwrtimetables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity
{
    // Interface
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setupControls();
    }   //  protected void onCreate(Bundle savedInstanceState)

    protected void setupControls()
    {
        // Back Button
        backButton = findViewById(R.id.help_back_button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        }); //  backButton.setOnClickListener(new View.OnClickListener()

    }   //  protected void setupControls()

}   //  public class HelpActivity extends AppCompatActivity
