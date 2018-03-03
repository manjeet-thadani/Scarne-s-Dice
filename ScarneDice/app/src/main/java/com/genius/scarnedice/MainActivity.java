package com.genius.scarnedice;

import android.os.Handler;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView yourScoreTextView, yourAddedScoreTextView, computerScoreTextView, computerAddedScoreTextView;
    private ImageView diceImageView;
    private Button rollButton, holdButton, resetButton;

    private int userOverallScore, userCurrentScore, computerOverallScore, computerCurrentScore;
    private boolean userTurn, ifAnyWon;
    private Random random;

    private int[] drawables = new int[]{R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        yourScoreTextView = (TextView) findViewById(R.id.your_total_score_textview);
        yourAddedScoreTextView = (TextView) findViewById(R.id.your_current_score_textview);
        computerScoreTextView = (TextView) findViewById(R.id.computer_total_score_textview);
        computerAddedScoreTextView = (TextView) findViewById(R.id.computer_current_score_textview);

        diceImageView = (ImageView) findViewById(R.id.dice_imageview);

        rollButton = (Button) findViewById(R.id.roll_button);
        holdButton = (Button) findViewById(R.id.hold_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        rollButton.setOnClickListener(this);
        holdButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        random = new Random();

        startGame();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roll_button:
                if (userTurn)
                    rollHandler();
                break;

            case R.id.hold_button:
                if (userTurn)
                    holdHandler();
                break;

            case R.id.reset_button:
                if (userTurn)
                    resetHandler();
                break;
        }
    }

    private void startGame() {
        userOverallScore = 0;
        userCurrentScore = 0;
        computerOverallScore = 0;
        computerCurrentScore = 0;

        yourScoreTextView.setText("");
        yourAddedScoreTextView.setText("");
        computerScoreTextView.setText("");
        computerAddedScoreTextView.setText("");

        ifAnyWon = false;

        userTurn();
    }

    private void userTurn() {
        Toast.makeText(this, "Your Turn!", Toast.LENGTH_SHORT).show();
        userTurn = true;
    }

    private void computerTurn() {
        Toast.makeText(this, "Computer Turn!", Toast.LENGTH_SHORT).show();
        userTurn = false;

        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (!ifAnyWon && !userTurn) {
                    if (computerCurrentScore <= 20) {
                        rollHandler();
                        handler.postDelayed(this, 2000);
                    } else {
                        holdHandler();
                    }
                }
            }
        };
        handler.post(runnableCode);
    }

    private void rollHandler() {
        if (userTurn) {
            int dicePosition = Math.abs(random.nextInt() % drawables.length);

            diceImageView.setImageResource(drawables[dicePosition]);
            userCurrentScore += dicePosition + 1;

            if (dicePosition == 0) {
                userTurn = false;
                userCurrentScore = 0;
                yourAddedScoreTextView.setText("+" + String.valueOf(userCurrentScore));

                computerTurn();
            } else {
                yourAddedScoreTextView.setText("+" + String.valueOf(userCurrentScore));
                checkIfAnyWon();
            }
        } else {
            int dicePosition = Math.abs(random.nextInt() % drawables.length);

            diceImageView.setImageResource(drawables[dicePosition]);
            computerCurrentScore += dicePosition + 1;

            if (dicePosition == 0) {
                userTurn = true;
                computerCurrentScore = 0;
                computerAddedScoreTextView.setText("+" + String.valueOf(computerCurrentScore));

                userTurn();
            } else {
                computerAddedScoreTextView.setText("+" + String.valueOf(computerCurrentScore));
                checkIfAnyWon();
            }
        }
    }

    private void holdHandler() {
        if (userTurn) {
            userOverallScore += userCurrentScore;
            userCurrentScore = 0;

            yourScoreTextView.setText(String.valueOf(userOverallScore));
            yourAddedScoreTextView.setText("+" + String.valueOf(userCurrentScore));

            userTurn = false;
            computerTurn();
        } else {
            computerOverallScore += computerCurrentScore;
            computerCurrentScore = 0;

            computerScoreTextView.setText(String.valueOf(computerOverallScore));
            computerAddedScoreTextView.setText("+" + String.valueOf(computerCurrentScore));

            userTurn = true;
            userTurn();
        }
    }

    private void resetHandler() {
        startGame();
    }

    private void checkIfAnyWon() {
        if (userOverallScore + userCurrentScore >= 100){
            Toast.makeText(this, "You Won!!", Toast.LENGTH_SHORT).show();
            ifAnyWon = true;
            userTurn = true;
        } else if (computerOverallScore + computerCurrentScore >= 100){
            Toast.makeText(this, "Computer Won!!", Toast.LENGTH_SHORT).show();
            ifAnyWon = true;
            userTurn = true;
        }
    }
}
