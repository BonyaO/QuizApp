package com.bonya.quizapp;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is a model class for the questions, each question has four options with one of the them
 * the correct answer
 * The last member variable (field) is the number corresponding to the correct answer
 * the answer number for option 1 is 1, option 2 is 2, and so on
 * The class implements Parcelable to enable its references to be passed between activities and so on
 */

public class Question implements Parcelable {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private Category mCategory;

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        mCategory = category;
    }

    private int answerNumber;

    public enum Category{
        SINGLE_ANSWER,
        MULTIPLE_ANSWER,
        FREEFORM
    }

    public Question(Category category, String question, String option1){
        this.mCategory = category;
        this.question = question;
        this.option1 = option1;

    }

    public Question(Category category, String question, String option1, String option2, String option3, String option4) {
        this.mCategory = category;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
    }

    public Question() {
    }

    public Question(Category category, String question, String option1, String option2, String option3, String option4, int answerNumber) {
        this.mCategory = category;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answerNumber = answerNumber;
    }

    protected Question(Parcel in) {
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        option4 = in.readString();
        answerNumber = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(String.valueOf(mCategory));
        parcel.writeString(question);
        parcel.writeString(option1);
        parcel.writeString(option2);
        parcel.writeString(option3);
        parcel.writeString(option4);
        parcel.writeInt(answerNumber);
    }
}
