package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isPlayer1Turn;
    private boolean isPlayerTwo;
    private boolean isSoundOn;
    private int wrongSound, gameStartSound, gameOverSound, boxFillSound;
    private SoundPool soundPool; // will handle the sound of the game

    private String player1Name, player2Name;
    private String player1FillText, player2FillText;
    private DataHelper dataHelper;
    private int totalTurnCount;

    private TextView[] ticTacTextViews;
    private TextView turnTextView;
    private Button restartButton;

    private boolean isGameCompleted;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ticTacTextViews = new TextView[9];
        dataHelper = new DataHelper(this);
        loadData();
        if(isSoundOn) loadSound();

        TextView titleTextView = findViewById(R.id.titleTextViewId);
        turnTextView = findViewById(R.id.turnTextViewId);

        ticTacTextViews[0] = findViewById(R.id.ticTacTextViewId1);
        ticTacTextViews[1] = findViewById(R.id.ticTacTextViewId2);
        ticTacTextViews[2] = findViewById(R.id.ticTacTextViewId3);
        ticTacTextViews[3] = findViewById(R.id.ticTacTextViewId4);
        ticTacTextViews[4] = findViewById(R.id.ticTacTextViewId5);
        ticTacTextViews[5] = findViewById(R.id.ticTacTextViewId6);
        ticTacTextViews[6] = findViewById(R.id.ticTacTextViewId7);
        ticTacTextViews[7] = findViewById(R.id.ticTacTextViewId8);
        ticTacTextViews[8] = findViewById(R.id.ticTacTextViewId9);

        restartButton = findViewById(R.id.restartButtonId);

        // set the title textView
        titleTextView.setText(player1Name + " and " + player2Name + " are playing!");
        restartButton.setOnClickListener(this);

        init(); // initialize the game.
    }

    @SuppressLint("SetTextI18n")
    void init() {
        isPlayer1Turn = true; // first time it will be true.
        totalTurnCount = 0;

        turnTextView.setText(player1Name + "'s turn");

        for(int i = 0; i < 9; i++)
            ticTacTextViews[i].setOnClickListener(this);

        // play the game start sound
        if(isSoundOn) soundPool.play(gameStartSound, 1, 1, 0, 2, 1);

        isGameCompleted = false;
    }

    // load all the sounds
    void loadSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME).build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(10).build();

        try {
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("game_start_sound.mp3");
            gameStartSound = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("box_sound.ogg");
            boxFillSound = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("wrong_sound.ogg");
            wrongSound = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("game_over_sound.mp3");
            gameOverSound = soundPool.load(descriptor, 0);


        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadData() {
        player1Name = dataHelper.getNamePlayer1();
        if(player1Name.equals("")) player1Name = "X-man";

        player2Name = dataHelper.getNamePlayer2();
        if(player2Name.equals("")) player2Name = "O-man";

        isPlayerTwo = dataHelper.getIsPlayerTwo();
        isSoundOn = dataHelper.getSoundMode();

        // set the fill text
        player1FillText = player1Name.substring(0, 1).toUpperCase();
        if(isPlayerTwo) {
            player2FillText = player2Name.substring(0, 1).toUpperCase();

            if(player2FillText.equals(player1FillText))
                player2FillText += "!";
        }
        else {
            if(player1FillText.equals("C")) player2FillText = "C!";
            else player2FillText = "C";

            // if the game is in one player mode, then set the
            // player2's name to computer
            player2Name = "Computer";
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticTacTextViewId1 :
                fillTextViewBox(ticTacTextViews[0]);
                break;
            case R.id.ticTacTextViewId2 :
                fillTextViewBox(ticTacTextViews[1]);
                break;
            case R.id.ticTacTextViewId3 :
                fillTextViewBox(ticTacTextViews[2]);
                break;
            case R.id.ticTacTextViewId4 :
                fillTextViewBox(ticTacTextViews[3]);
                break;
            case R.id.ticTacTextViewId5 :
                fillTextViewBox(ticTacTextViews[4]);
                break;
            case R.id.ticTacTextViewId6 :
                fillTextViewBox(ticTacTextViews[5]);
                break;
            case R.id.ticTacTextViewId7 :
                fillTextViewBox(ticTacTextViews[6]);
                break;
            case R.id.ticTacTextViewId8 :
                fillTextViewBox(ticTacTextViews[7]);
                break;
            case R.id.ticTacTextViewId9 :
                fillTextViewBox(ticTacTextViews[8]);
                break;
            case R.id.restartButtonId:
                restartGame();
                restartButton.setVisibility(View.GONE);

        }
    }

    // This method will fill the box
    @SuppressLint("SetTextI18n")
    void fillTextViewBox(TextView textView) {
        if (textView.getText().equals("")) {
            if(isPlayer1Turn) {
                textView.setText(player1FillText);
                textView.setBackgroundResource(R.color.pink);
                totalTurnCount++;
                turnTextView.setText(player2Name + "'s turn");

                if(!isPlayerTwo && totalTurnCount < 8) { // if not is player two, give a computer's turn
                    checkIfWon(); // first check if won
                    if(!isGameCompleted) getComputerTurn();
                }
            }
            else {
                textView.setText(player2FillText);
                textView.setBackgroundResource(R.color.green);
                totalTurnCount++;
                turnTextView.setText(player1Name + "'s turn");

            }
            // play the box fill sound
            if(isSoundOn) soundPool.play(boxFillSound, 1, 1, 0, 0, 1);

            isPlayer1Turn = !isPlayer1Turn;
            checkIfWon(); // check if won

            if(totalTurnCount == 8 && !isGameCompleted)  // if count number is 8, then automatically 9th turn will happen
                giveAutomaticTurn();
        }
        else {
            Snackbar.make(textView, "The box was already filled.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            // play the wrong sound here
            if(isSoundOn) soundPool.play(wrongSound, 1, 1, 0, 0, 1);
        }
    }

    private void giveAutomaticTurn() {
        // Find the empty box
        for(TextView textView : ticTacTextViews)
            if(textView.getText().equals("")) {
                textView.setText(player1FillText);
                textView.setBackgroundResource(R.color.pink);
                break;
            }

        totalTurnCount++;
        // now check if won
        checkIfWon();
    }

    // This method will check if a player won
    private void checkIfWon() {
        // Check the rows.
        for(int i = 0; i < 3; i++){
            if(ticTacTextViews[i].getText().equals(ticTacTextViews[i + 3].getText()) && !ticTacTextViews[i].getText().equals("")
                    && ticTacTextViews[i].getText().equals(ticTacTextViews[i + 6].getText()))
            {
                gameOver(ticTacTextViews[i].getText().toString());
                return;
            }
        }

        // check the columns.
        for(int i = 0; i < 3; i++) {
            int col = i * 3;
            if(ticTacTextViews[col].getText().equals(ticTacTextViews[col + 1].getText()) && !ticTacTextViews[col].getText().equals("")
                    && ticTacTextViews[col].getText().equals(ticTacTextViews[col + 2].getText()))
            {
                gameOver(ticTacTextViews[col].getText().toString());
                return;
            }
        }

        // check for corners
        if(ticTacTextViews[0].getText().equals(ticTacTextViews[4].getText()) && !ticTacTextViews[0].getText().equals("")
                && ticTacTextViews[0].getText().equals(ticTacTextViews[8].getText())) {
            gameOver(ticTacTextViews[0].getText().toString());
            return;
        }

        if(ticTacTextViews[2].getText().equals(ticTacTextViews[4].getText()) && !ticTacTextViews[4].getText().equals("")
                && ticTacTextViews[2].getText().equals(ticTacTextViews[6].getText())) {
            gameOver(ticTacTextViews[2].getText().toString());
            return;
        }

        if(totalTurnCount == 9) gameOver("34212");
    }

    private void gameOver(String name) {
        if(isGameCompleted) return;

        isGameCompleted = true;

        // set all listener to null
        for(TextView textView : ticTacTextViews)
            textView.setOnClickListener(null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.game_over, null);

        // play the game over sound
        if(isSoundOn) soundPool.play(gameOverSound, 1, 1, 0, 0, 1);

        builder.setView(view);
        builder.setCancelable(false);

        TextView titleTextView = view.findViewById(R.id.titleTextViewId);
        TextView gameTextView = view.findViewById(R.id.gameTextViewId);
        ImageView imageView = view.findViewById(R.id.imageViewId);

        String title, game;
        if (name.equals("34212")) {
            title = "Wow!!!";
            game = "The match was draw";

            imageView.setImageResource(R.drawable.wow);

        }
        else {
            title = "Congratulations!!!";

            if(name.equals(player1FillText)) game = player1Name;
            else game = player2Name;

            game += " has won!!";

            imageView.setImageResource(R.drawable.flowers);
        }

        titleTextView.setText(title);
        gameTextView.setText(game);

        builder.setPositiveButton("Restart", (dialog, which) -> {
            // restart the game
            restartGame();
            dialog.dismiss();
        });

        String finalGame = game;
        builder.setNeutralButton("OK", (dialog, which) -> {
            restartButton.setVisibility(View.VISIBLE);

            turnTextView.setText(finalGame);

            dialog.dismiss();
        });

        builder.setNegativeButton("Back", (dialog, which) -> {
            // back to main activity
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        });


        builder.show();
    }

    void restartGame() {
        // reset the boxes
        for (TextView ticTacTextView : ticTacTextViews) {
            ticTacTextView.setText("");
            ticTacTextView.setBackgroundResource(R.color.box);
        }
        init(); // initialize
    }

    @SuppressLint("SetTextI18n")
    private void getComputerTurn() {

        int n;

        // generate random number until we get an empty box
        do {
            n = getRandomNumber();
        } while (!ticTacTextViews[n].getText().equals(""));

        ticTacTextViews[n].setText(player2FillText);
        ticTacTextViews[n].setBackgroundResource(R.color.green);

        isPlayer1Turn = !isPlayer1Turn; // get the player turn again

        totalTurnCount++;
        turnTextView.setText(player1Name + "'s turn");

        // play the box fill sounds
        if(isSoundOn) soundPool.play(boxFillSound, 1, 1, 0, 0, 1);
    }

    int getRandomNumber() {
        return new Random().nextInt(8);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isSoundOn)
            soundPool.release();
    }
}