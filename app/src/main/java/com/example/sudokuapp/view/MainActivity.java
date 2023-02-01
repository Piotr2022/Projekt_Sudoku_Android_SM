package com.example.sudokuapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sudokuapp.R;

public class MainActivity extends AppCompatActivity {

    private Button newGameButton;
    private Button continueGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewGame();
            }
        });

        continueGameButton = findViewById(R.id.continueGameButton);
        continueGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewGame();
            }
        });
    }

    private void openNewGame(){
        Log.d("OK","OK");
        Intent intent = new Intent(this,ChooseDifficultyLevelActivity.class);
        startActivity(intent);
    }


}