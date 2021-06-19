package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText player1EditText, player2EditText;
    private TextView player1TextView, player2TextView;
    private RadioButton oneRadioButtonId, twoRadioButtonId;
    private DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataHelper = new DataHelper(this);

        Button startGameButton = findViewById(R.id.startGameButtonId);
        Button aboutButton = findViewById(R.id.aboutButtonId);
        Button helpButton = findViewById(R.id.helpButtonId);

        startGameButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startGameButtonId :
                startGame();
                break;
            case R.id.helpButtonId :
                helpMethod();
                break;
            case R.id.aboutButtonId :
                aboutMethod();
        }
    }

    private void aboutMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.about_layout, null );

        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void helpMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Help?");

        builder.setMessage(R.string.game_description);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void startGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.start_game, null );

        builder.setCancelable(false);

        builder.setView(view);
        ToggleButton toggleButton = view.findViewById(R.id.toggleButtonId);

        player1EditText = view.findViewById(R.id.player1EditTextId);
        player2EditText = view.findViewById(R.id.player2EditTextId);

        player1TextView = view.findViewById(R.id.player1TextViewId);
        player2TextView = view.findViewById(R.id.player2TextViewId);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroupId);
        oneRadioButtonId = view.findViewById(R.id.oneRadioButtonId);
        twoRadioButtonId = view.findViewById(R.id.twoRadioButtonId);

        toggleButton.setText(dataHelper.getSoundMode() ? "ON" : "OF");

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.oneRadioButtonId)
                setPlayerOneInterface();
            else setPlayerTwoInterface();
        });

        if(dataHelper.getIsPlayerTwo())
            setPlayerTwoInterface();
        else setPlayerOneInterface();

        builder.setPositiveButton("OK", (dialog, which) -> {
            // First save all the settings.
            dataHelper.setIsPlayerTwo(radioGroup.getCheckedRadioButtonId() == R.id.twoRadioButtonId);
            dataHelper.setNamePlayer1(player1EditText.getText().toString());
            dataHelper.setNamePlayer2(player2EditText.getText().toString());
            dataHelper.setSoundMode(toggleButton.getText().equals("ON"));

            // then start the game.
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    // This function will set player one interface
    void setPlayerOneInterface() {
        player1TextView.setText(R.string.your_name);

        player1TextView.setVisibility(View.VISIBLE);
        player1EditText.setVisibility(View.VISIBLE);

        player1EditText.setText(dataHelper.getNamePlayer1());
        player2EditText.setText(dataHelper.getNamePlayer2());

        player2EditText.setVisibility(View.GONE);
        player2TextView.setVisibility(View.GONE);

        oneRadioButtonId.setChecked(true);
    }

    // This function will set player two interface
    void setPlayerTwoInterface() {
        player1TextView.setText(R.string.player1_name);
        player2TextView.setText(R.string.player2_name);

        player1EditText.setVisibility(View.VISIBLE);
        player2EditText.setVisibility(View.VISIBLE);
        player1TextView.setVisibility(View.VISIBLE);
        player2TextView.setVisibility(View.VISIBLE);

        // then set saved text to edit text
        player1EditText.setText(dataHelper.getNamePlayer1());
        player2EditText.setText(dataHelper.getNamePlayer2());

        twoRadioButtonId.setChecked(true);
    }

}