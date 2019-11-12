package com.bonya.quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_SCORE = "KeyScore";
    private static final String KEY_ANSWERED = "KeyAnswered";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_QUESTIONS = "keyQuestions";
    private static final String KEY_PROGRESS_LEVEL = "keyProgress";




    TextView questionTextView;
    TextView questionNumberTextView;
    RadioGroup mRadioGroup;
    RadioButton optionOneRadioButton;
    RadioButton optionTwoRadioButton;
    RadioButton optionThreeRadioButton;
    RadioButton optionFourRadioButton;
    TextView scoreTextView;
    TextView totalQuestionTextView;
    Button confirmButton;
    ProgressBar questionCompletionProgressBar;


    ArrayList<Question> mQuestions;

    private ColorStateList defaultRadioButtonColor;


    int totalQuestions;
    int questionCounter = 0;
    boolean answered;
    private Question mCurrentQuestion;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.question_tv);
        questionNumberTextView = findViewById(R.id.question_number);
        scoreTextView = findViewById(R.id.score_textview);
        totalQuestionTextView = findViewById(R.id.total_questions_textview);
        mRadioGroup = findViewById(R.id.radio_group);
        optionOneRadioButton = findViewById(R.id.option_one);
        optionTwoRadioButton = findViewById(R.id.option_two);
        optionThreeRadioButton = findViewById(R.id.option_three);
        optionFourRadioButton = findViewById(R.id.option_four);
        confirmButton = findViewById(R.id.confirmButton);
        questionCompletionProgressBar = findViewById(R.id.question_completionBar);

        defaultRadioButtonColor = optionFourRadioButton.getTextColors();


        if(savedInstanceState == null){
            //Populating mQuestions List with 10 questions
            mQuestions = new ArrayList<>();
            mQuestions.add(new Question("What is the full meaning of BCD?",
                    "Binary Coded Drinks",
                    "Binary Coded Decimal",
                    "Binomial Coded Decimal",
                    "Basic Computer Data", 2));
            mQuestions.add(new Question("All of the following are input devices except:",
                    "Memory Card",
                    "Microphone",
                    "Keyboard",
                    "Mouse", 1));
            mQuestions.add(new Question("1MB is equal to ",
                    "1024 Bytes",
                    "1000KB",
                    "1024KB",
                    "100KB", 3));
            mQuestions.add(new Question("Who was the first programmer?",
                    "Charles Babbage",
                    "Steve Jobs",
                    "Ada Augusta",
                    "Bill Gates", 3));
            mQuestions.add(new Question("To access the services of Operating System, the interface is provided by _______",
                    "System Calls",
                    "API",
                    "Library",
                    "Assembly instructions", 1));
            mQuestions.add(new Question("Which of the following error will be handled by the Operating System?",
                    "Power failure",
                    "Lack of paper in printer",
                    "Connection failure in the network",
                    "All of the above", 4));
            mQuestions.add(new Question("The process of carrying out a command is called",
                    "Fetching",
                    "Controlling",
                    "Storing",
                    "Executing", 4));
            mQuestions.add(new Question("A Database is used to:",
                    "Store and organize data in records",
                    "Store and organize papers",
                    "Store and organize records in files",
                    "Store and organize records in fields", 1));
            mQuestions.add(new Question("The term Icon refers to",
                    "A picture or symbol that represents a command",
                    "A Photograph",
                    "A Leader",
                    "A symbol of Power", 1));
            mQuestions.add(new Question("What does the \"R\" in RAM stand for?",
                    "Rewrite",
                    "Read",
                    "Readable",
                    "Random", 4));

            totalQuestions = mQuestions.size();
            Collections.shuffle(mQuestions);
            showNextQuestion();
        }
        else{
            mQuestions = savedInstanceState.getParcelableArrayList(KEY_QUESTIONS);
            totalQuestions = mQuestions.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            mCurrentQuestion = mQuestions.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            questionCompletionProgressBar.setProgress(savedInstanceState.getInt(KEY_PROGRESS_LEVEL));

            scoreTextView.setText("Score: " + score);
            totalQuestionTextView.setText("Percentage Completed: " + (questionCounter-1)*10 + "%");
            questionNumberTextView.setText("Q" + questionCounter + ")" );
            bindQuestionToButtonGroup();

            if(answered){
                showAnswer();
                totalQuestionTextView.setText("Percentage Completed: " + questionCounter*10 + "%");
            }
        }





    }


    /**
     * Verifies if any of the RadioButtons are checked and carries out the appropriate action
     * @param view the confirm answer button
     */
    public void confirmAnswer(View view) {
        totalQuestionTextView.setText("Percentage Completed: " + questionCounter*10 + "%");

        if(!answered){
            if(optionOneRadioButton.isChecked() || optionTwoRadioButton.isChecked() ||
                    optionThreeRadioButton.isChecked() || optionFourRadioButton.isChecked()){
                checkAnswer();
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            showNextQuestion();
        }
    }

    /**
     * showNextQuestion simple replaces the text in the various views with the information of the subsequent Question
     * and resets the colour of the checkboxes
     */

    private void showNextQuestion() {
        optionOneRadioButton.setTextColor(defaultRadioButtonColor);
        optionTwoRadioButton.setTextColor(defaultRadioButtonColor);
        optionThreeRadioButton.setTextColor(defaultRadioButtonColor);
        optionFourRadioButton.setTextColor(defaultRadioButtonColor);
        mRadioGroup.clearCheck();
        scoreTextView.setText("Score: " + score);
        int i = questionCounter + 1;
        questionNumberTextView.setText("Q" + i + ")" );


        if(questionCounter < totalQuestions){
            mCurrentQuestion = mQuestions.get(questionCounter);
            bindQuestionToButtonGroup();

            questionCounter++;
            answered = false;

            confirmButton.setText("Confirm");
        }
        else{
            showResultsDialogue();
        }
    }

    private void bindQuestionToButtonGroup() {
        questionTextView.setText(mCurrentQuestion.getQuestion());
        optionOneRadioButton.setText(mCurrentQuestion.getOption1());
        optionTwoRadioButton.setText(mCurrentQuestion.getOption2());
        optionThreeRadioButton.setText(mCurrentQuestion.getOption3());
        optionFourRadioButton.setText(mCurrentQuestion.getOption4());
    }

    /*
    //Verifies if the checked RadioButton corresponds to the correct answer
     */
    private void checkAnswer() {
        answered = true;
        RadioButton selectedButton = findViewById(mRadioGroup.getCheckedRadioButtonId());
        int answerNumber = mRadioGroup.indexOfChild(selectedButton) + 1;

        if(answerNumber == mCurrentQuestion.getAnswerNumber()){
            score++;
            scoreTextView.setText("Score: " + score);
            Toast.makeText(this, "Hurray, your are correct", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Opps!!, you checked the wrong  answer", Toast.LENGTH_SHORT).show();
        }
        showAnswer();
    }

    /**
     * showAnswer() sets a green colour to the text of the RadioButton corresponding to the correct answer
     * and a red text colour to the others
     */
    private void showAnswer() {
        optionOneRadioButton.setTextColor(Color.RED);
        optionTwoRadioButton.setTextColor(Color.RED);
        optionThreeRadioButton.setTextColor(Color.RED);
        optionFourRadioButton.setTextColor(Color.RED);
        questionCompletionProgressBar.incrementProgressBy(10);

        switch (mCurrentQuestion.getAnswerNumber()){
            case 1:
                optionOneRadioButton.setTextColor(Color.GREEN);
                break;
            case 2:
                optionTwoRadioButton.setTextColor(Color.GREEN);
                break;
            case 3:
                optionThreeRadioButton.setTextColor(Color.GREEN);
                break;
            case 4:
                optionFourRadioButton.setTextColor(Color.GREEN);
                break;
            default:
                break;
        }
        if(questionCounter < totalQuestions){
            confirmButton.setText("Next");
        }else{
            confirmButton.setText("Finish");
        }

    }

    /**
     * This method presents an AlertDialog to the user containing the user's total score
     * and an option to either close the app or restart the quiz
     */
    private void showResultsDialogue() {
        String alert_message = "You have completed the quiz, You Scored: " + score * 10 + "%";
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Quiz Completed");
        alertDialog.setMessage(alert_message);
        alertDialog.setIcon(R.drawable.ic_check);

        alertDialog.setPositiveButton("Restart Quiz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Collections.shuffle(mQuestions);
                questionCounter = 0;
                score = 0;
                questionCompletionProgressBar.setProgress(0);
                totalQuestionTextView.setText("Percentage Completed: 0%");

                showNextQuestion();

            }
        });
        alertDialog.setNegativeButton("Close App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });


        //This disables the ability of the user to cancel the dialog by clicking out of the bounds
        //or pressing the back button
        Dialog dialog = alertDialog.create();
        dialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * This method ensures that data is preserved when the screen is rotated
     * The data is stored as key-value pairs in the outState Bundle and is retrieved in the
     * onCreate() method from the savedInstanceState Bundle
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putInt(KEY_PROGRESS_LEVEL, questionCompletionProgressBar.getProgress());
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTIONS, mQuestions);
    }
}
