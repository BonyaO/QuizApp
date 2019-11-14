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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bonya.quizapp.Question.*;
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
    ArrayList<Question> mQuestions = new ArrayList<>();
    private ColorStateList defaultRadioButtonColor;
    LinearLayout mLinearLayout;
    CheckBox optionOneCB;
    CheckBox optionTwoCB;
    CheckBox optionThreeCB;
    CheckBox optionFourCB;
    EditText answerEditText;
    int totalQuestions;
    int questionCounter = 0;
    boolean answered;
    private Question mCurrentQuestion;
    private int score;

    //Because of the categories of questions, adding correct answers to questions containing multiple
    //answers was pretty difficult, So I put some of the answers here
    ArrayList<String> questionOneAnswers = new ArrayList<>();
    ArrayList<String> questionTwoAnswers = new ArrayList<>();
    ArrayList<String> questionThreeAnswers = new ArrayList<>();

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
        mLinearLayout = findViewById(R.id.checkbox_holder);
        optionOneCB = findViewById(R.id.option1_cb);
        optionTwoCB = findViewById(R.id.option2_cb);
        optionThreeCB = findViewById(R.id.option3_cb);
        optionFourCB = findViewById(R.id.option4_cb);
        answerEditText = findViewById(R.id.answer_textview);
        defaultRadioButtonColor = optionFourRadioButton.getTextColors();

        questionOneAnswers.add("Power failure");
        questionOneAnswers.add("Lack of paper in printer");
        questionOneAnswers.add("Connection failure in the network");
        questionTwoAnswers.add("Executing");
        questionThreeAnswers.add("Ensure Data integrity");
        questionThreeAnswers.add("Store and organize data");
        if(savedInstanceState == null){
            //Populating mQuestions List with 10 questions
            mQuestions.add(new Question(Category.SINGLE_ANSWER,"What is the full meaning of BCD?",
                    "Binary Coded Drinks",
                    "Binary Coded Decimal",
                    "Binomial Coded Decimal",
                    "Basic Computer Data", 2));
            mQuestions.add(new Question(Category.SINGLE_ANSWER,"All of the following are input devices except:",
                    "Memory Card",
                    "Microphone",
                    "Keyboard",
                    "Mouse", 1));
            mQuestions.add(new Question(Category.SINGLE_ANSWER, "1MB is equal to ",
                    "1024 Bytes",
                    "1000KB",
                    "1024KB",
                    "100KB", 3));
            mQuestions.add(new Question(Category.SINGLE_ANSWER, "Who was the first programmer?",
                    "Charles Babbage",
                    "Steve Jobs",
                    "Ada Augusta",
                    "Bill Gates", 3));
            mQuestions.add(new Question(Category.SINGLE_ANSWER,"To access the services of Operating System, the interface is provided by _______",
                    "System Calls",
                    "API",
                    "Library",
                    "Assembly instructions", 1));
           mQuestions.add(new Question(Category.MULTIPLE_ANSWER, "Which of the following error will be handled by the Operating System?",
                    "Power failure",
                    "Lack of paper in printer",
                    "Connection failure in the network",
                    "None Of The Above"));
            mQuestions.add(new Question(Category.MULTIPLE_ANSWER, "The process of carrying out a command is called:",
                    "Fetching",
                    "Controlling",
                    "Storing",
                    "Executing"));
            mQuestions.add(new Question(Category.MULTIPLE_ANSWER, "A Database is used to:",
                    "Store and organize data",
                    "Count all the people on earth",
                    "Ensure Data integrity",
                    "Play Music"));
            mQuestions.add(new Question(Category.FREEFORM,"What is the full meaning of D.O.S","Denial Of Service"));
            mQuestions.add(new Question(Category.FREEFORM, "What does the \"R\" in RAM stand for?","Random"));
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
            bindTextToViews();
            if(answered){
                questionTextView.setText(mCurrentQuestion.getQuestion());
                switch (mCurrentQuestion.getCategory()){
                    case SINGLE_ANSWER:
                        showAnswer();
                        break;
                    case MULTIPLE_ANSWER:
                        checkAnswerForCheckBoxes();
                        break;
                    case FREEFORM:
                        showAnswerForEditTex();
                        break;
                }

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
            answered = true;
            switch (mCurrentQuestion.getCategory()){
                case SINGLE_ANSWER:
                    checkAnswerForRadioButtons();
                    break;
                case MULTIPLE_ANSWER:
                    bindTextToViews();
                    break;
                case FREEFORM:
                    checkAnswerForEditText();
                    break;
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
        scoreTextView.setText("Score: " + score);
        int i = questionCounter + 1;
        if(questionCounter < totalQuestions){
            questionNumberTextView.setText("Q" + i + ")" );
            mCurrentQuestion = mQuestions.get(questionCounter);
            questionTextView.setText(mCurrentQuestion.getQuestion());
            bindTextToViews();
            questionCounter++;
            answered = false;
            confirmButton.setText("Confirm");
        }
        else{
            //Display Final result in A toast
            String alert_message = "You have completed the quiz, You Scored: " + score * 10 + "%";
            Toast.makeText(this, alert_message, Toast.LENGTH_SHORT).show();
            showResultsDialogue();
        }
    }

    private void bindTextToViews() {
        questionTextView.setText(mCurrentQuestion.getQuestion());
        switch (mCurrentQuestion.getCategory()){
            case SINGLE_ANSWER:
                answerEditText.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.GONE);
                mRadioGroup.setVisibility(View.VISIBLE);
                optionOneRadioButton.setTextColor(defaultRadioButtonColor);
                optionTwoRadioButton.setTextColor(defaultRadioButtonColor);
                optionThreeRadioButton.setTextColor(defaultRadioButtonColor);
                optionFourRadioButton.setTextColor(defaultRadioButtonColor);
                mRadioGroup.clearCheck();
                bindQuestionToButtonGroup();
                break;
            case MULTIPLE_ANSWER:
                mRadioGroup.setVisibility(View.GONE);
                answerEditText.setVisibility(View.GONE);
                optionOneCB.setTextColor(defaultRadioButtonColor);
                optionTwoCB.setTextColor(defaultRadioButtonColor);
                optionThreeCB.setTextColor(defaultRadioButtonColor);
                optionFourCB.setTextColor(defaultRadioButtonColor);
                optionOneCB.setChecked(false);
                optionTwoCB.setChecked(false);
                optionThreeCB.setChecked(false);
                optionFourCB.setChecked(false);
                mLinearLayout.setVisibility(View.VISIBLE);
                bindDataToCheckBoxes();
                break;
            case FREEFORM:
                mRadioGroup.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.GONE);
                answerEditText.setVisibility(View.VISIBLE);
                answerEditText.setText("");
                answerEditText.setActivated(false);
                break;
        }
    }

    private void bindDataToCheckBoxes() {
        optionOneCB.setText(mCurrentQuestion.getOption1());
        optionTwoCB.setText(mCurrentQuestion.getOption2());
        optionThreeCB.setText(mCurrentQuestion.getOption3());
        optionFourCB.setText(mCurrentQuestion.getOption4());
    }

    private void bindQuestionToButtonGroup() {
        questionTextView.setText(mCurrentQuestion.getQuestion());
        optionOneRadioButton.setText(mCurrentQuestion.getOption1());
        optionTwoRadioButton.setText(mCurrentQuestion.getOption2());
        optionThreeRadioButton.setText(mCurrentQuestion.getOption3());
        optionFourRadioButton.setText(mCurrentQuestion.getOption4());
    }

    private void checkAnswerForCheckBoxes() {
    String question1 = "Which of the following error will be handled by the Operating System?";
    String question2 = "The process of carrying out a command is called:";
    String question3 = "A Database is used to:";
    if(optionOneCB.isChecked() || optionTwoCB.isChecked() ||optionThreeCB.isChecked() ||optionFourCB.isChecked()){
        if(mCurrentQuestion.getQuestion().equals(question1)){
            checkSpecificQuestion(questionOneAnswers);
        }else if(mCurrentQuestion.getQuestion().equals(question2)){
            checkSpecificQuestion(questionTwoAnswers);
        }else if (mCurrentQuestion.getQuestion().equals(question3)){
            checkSpecificQuestion(questionThreeAnswers);
        }
    }else{
        answered = false;
        Toast.makeText(this, "Please Select at least one answer", Toast.LENGTH_SHORT).show();
    }
    }

    /**
     * Iterates through a list of check boxes to find those check boxes that corresponding to the right answer
     * @param list of correct answers (String) to compare with the Texts of the CheckBoxes
     */
    public void checkSpecificQuestion(ArrayList<String> list){
        List<CheckBox> checkBoxList = new ArrayList<>();
        checkBoxList.add(optionOneCB);
        checkBoxList.add(optionTwoCB);
        checkBoxList.add(optionThreeCB);
        checkBoxList.add(optionFourCB);
        int iScore = 0;
        for(CheckBox checkBox: checkBoxList){
            if(checkBox.isChecked() && list.contains(checkBox.getText().toString())){
                iScore +=1;
                checkBox.setTextColor(Color.GREEN);
            }else if(!checkBox.isChecked() && list.contains(checkBox.getText().toString())){
                checkBox.setTextColor(Color.GREEN);
                iScore -= 1;
            }else if(checkBox.isChecked() && !list.contains(checkBox.getText().toString())){
                iScore -=1;
                checkBox.setTextColor(Color.RED);
            }else{
                checkBox.setTextColor(Color.RED);
            }
        }
        if(iScore == list.size()){
            updateScore();
            iScore = 0;
        }
        questionCompletionProgressBar.incrementProgressBy(10);
        setConfirmButtonTextToNextOrFinish();
    }

    private void checkAnswerForEditText() {//Verifies if the typed answer is correct
        if(answerEditText.getText().toString().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.toast_message_two), Toast.LENGTH_SHORT).show();
            answered = false;
        }else{
            if (answerEditText.getText().toString().equalsIgnoreCase(mCurrentQuestion.getOption1())){
                updateScore();
            }else {
                Toast.makeText(this, "Opps!!, The Correct answer is: " + mCurrentQuestion.getOption1(), Toast.LENGTH_SHORT).show();
            }
            showAnswerForEditTex();
        }
    }
    private void showAnswerForEditTex() { //showing the answer after the confirrm button is clicked
        questionCompletionProgressBar.incrementProgressBy(10);
        totalQuestionTextView.setText("Percentage Completed: " + questionCounter*10 + "%");
        answerEditText.setText(mCurrentQuestion.getOption1());
        answerEditText.setTextColor(Color.GREEN);
        setConfirmButtonTextToNextOrFinish();
    }

    /**
     * Function Checks if the selected radio button is the correct answer
     */
    private void checkAnswerForRadioButtons() {
        RadioButton selectedButton = findViewById(mRadioGroup.getCheckedRadioButtonId());
        int answerNumber = mRadioGroup.indexOfChild(selectedButton) + 1;
        //Verify if any of the RadioButton is checked
        if(optionOneRadioButton.isChecked() || optionTwoRadioButton.isChecked() ||
                optionThreeRadioButton.isChecked() || optionFourRadioButton.isChecked()){
            //Update the Score if he answer is correct
            if(answerNumber == mCurrentQuestion.getAnswerNumber()){
                updateScore();
                Toast.makeText(this, "Hurray, your are correct", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Opps!!, you checked the wrong  answer", Toast.LENGTH_SHORT).show();
            }
            showAnswer();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
            answered = false;
        }
    }

    private void updateScore() {//Update the score and the various views holding information
        score++;
        scoreTextView.setText("Score: " + score);
        totalQuestionTextView.setText("Percentage Completed: " + questionCounter*10 + "%");
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
        setConfirmButtonTextToNextOrFinish();
    }
//Changes the the text of the Confirm Button appropriately depending on the question number
    private void setConfirmButtonTextToNextOrFinish() {
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
