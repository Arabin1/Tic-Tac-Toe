package com.example.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;

public class DataHelper {

    private final SharedPreferences sharedPreferences;

    public DataHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(Key.S_NAME, Context.MODE_PRIVATE);
    }

    void setNamePlayer1(String namePlayer1){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Key.PLAYER1_NAME, namePlayer1);
        editor.apply();
    }

    void setNamePlayer2(String namePlayer2) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Key.PLAYER2_NAME, namePlayer2);
        editor.apply();
    }

    void setIsPlayerTwo(boolean isPlayerTwo) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Key.IS_PLAYER_TWO, isPlayerTwo);
        editor.apply();
    }

    void setSoundMode(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Key.SOUND, value);
        editor.apply();
    }

    String getNamePlayer1() {
        return  sharedPreferences.getString(Key.PLAYER1_NAME, "X-man");
    }

    String getNamePlayer2() {
        return sharedPreferences.getString(Key.PLAYER2_NAME, "O-man");
    }

    boolean getIsPlayerTwo() {
        return sharedPreferences.getBoolean(Key.IS_PLAYER_TWO, true);
    }

    boolean getSoundMode() {
        return sharedPreferences.getBoolean(Key.SOUND, true);
    }
}
