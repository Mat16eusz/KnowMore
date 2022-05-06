package com.mateusz.jasiak.knowmore;

import java.util.ArrayList;

public class CurrentQuestionsAPI {

    private String myIdSocialMedia;
    private String friendIdSocialMedia;
    private boolean myActiveTurn;
    private boolean friendActiveTurn;
    private ArrayList<Integer> selectedQuestions = new ArrayList<Integer>();
    private int myIdQuestionOne;
    private String myQuestionOneEN;
    private ArrayList<String> myAnswerOneEN = new ArrayList<String>();
    private String myQuestionOnePL;
    private ArrayList<String> myAnswerOnePL = new ArrayList<String>();
    private int myMarkedAnswerOne;
    private int myIdQuestionTwo;
    private String myQuestionTwoEN;
    private ArrayList<String> myAnswerTwoEN = new ArrayList<String>();
    private String myQuestionTwoPL;
    private ArrayList<String> myAnswerTwoPL = new ArrayList<String>();
    private int myMarkedAnswerTwo;
    private int myIdQuestionThree;
    private String myQuestionThreeEN;
    private ArrayList<String> myAnswerThreeEN = new ArrayList<String>();
    private String myQuestionThreePL;
    private ArrayList<String> myAnswerThreePL = new ArrayList<String>();
    private int myMarkedAnswerThree;
    private int friendIdQuestionOne;
    private String friendQuestionOneEN;
    private ArrayList<String> friendAnswerOneEN = new ArrayList<String>();
    private String friendQuestionOnePL;
    private ArrayList<String> friendAnswerOnePL = new ArrayList<String>();
    private int friendMarkedAnswerOne;
    private int friendIdQuestionTwo;
    private String friendQuestionTwoEN;
    private ArrayList<String> friendAnswerTwoEN = new ArrayList<String>();
    private String friendQuestionTwoPL;
    private ArrayList<String> friendAnswerTwoPL = new ArrayList<String>();
    private int friendMarkedAnswerTwo;
    private int friendIdQuestionThree;
    private String friendQuestionThreeEN;
    private ArrayList<String> friendAnswerThreeEN = new ArrayList<String>();
    private String friendQuestionThreePL;
    private ArrayList<String> friendAnswerThreePL = new ArrayList<String>();
    private int friendMarkedAnswerThree;

    public CurrentQuestionsAPI(String myIdSocialMedia, String friendIdSocialMedia, boolean myActiveTurn, boolean friendActiveTurn, ArrayList<Integer> selectedQuestions, int myIdQuestionOne, String myQuestionOneEN, ArrayList<String> myAnswerOneEN, String myQuestionOnePL, ArrayList<String> myAnswerOnePL, int myMarkedAnswerOne, int myIdQuestionTwo, String myQuestionTwoEN, ArrayList<String> myAnswerTwoEN, String myQuestionTwoPL, ArrayList<String> myAnswerTwoPL, int myMarkedAnswerTwo, int myIdQuestionThree, String myQuestionThreeEN, ArrayList<String> myAnswerThreeEN, String myQuestionThreePL, ArrayList<String> myAnswerThreePL, int myMarkedAnswerThree, int friendIdQuestionOne, String friendQuestionOneEN, ArrayList<String> friendAnswerOneEN, String friendQuestionOnePL, ArrayList<String> friendAnswerOnePL, int friendMarkedAnswerOne, int friendIdQuestionTwo, String friendQuestionTwoEN, ArrayList<String> friendAnswerTwoEN, String friendQuestionTwoPL, ArrayList<String> friendAnswerTwoPL, int friendMarkedAnswerTwo, int friendIdQuestionThree, String friendQuestionThreeEN, ArrayList<String> friendAnswerThreeEN, String friendQuestionThreePL, ArrayList<String> friendAnswerThreePL, int friendMarkedAnswerThree) {
        this.myIdSocialMedia = myIdSocialMedia;
        this.friendIdSocialMedia = friendIdSocialMedia;
        this.myActiveTurn = myActiveTurn;
        this.friendActiveTurn = friendActiveTurn;
        this.selectedQuestions = selectedQuestions;
        this.myIdQuestionOne = myIdQuestionOne;
        this.myQuestionOneEN = myQuestionOneEN;
        this.myAnswerOneEN = myAnswerOneEN;
        this.myQuestionOnePL = myQuestionOnePL;
        this.myAnswerOnePL = myAnswerOnePL;
        this.myMarkedAnswerOne = myMarkedAnswerOne;
        this.myIdQuestionTwo = myIdQuestionTwo;
        this.myQuestionTwoEN = myQuestionTwoEN;
        this.myAnswerTwoEN = myAnswerTwoEN;
        this.myQuestionTwoPL = myQuestionTwoPL;
        this.myAnswerTwoPL = myAnswerTwoPL;
        this.myMarkedAnswerTwo = myMarkedAnswerTwo;
        this.myIdQuestionThree = myIdQuestionThree;
        this.myQuestionThreeEN = myQuestionThreeEN;
        this.myAnswerThreeEN = myAnswerThreeEN;
        this.myQuestionThreePL = myQuestionThreePL;
        this.myAnswerThreePL = myAnswerThreePL;
        this.myMarkedAnswerThree = myMarkedAnswerThree;
        this.friendIdQuestionOne = friendIdQuestionOne;
        this.friendQuestionOneEN = friendQuestionOneEN;
        this.friendAnswerOneEN = friendAnswerOneEN;
        this.friendQuestionOnePL = friendQuestionOnePL;
        this.friendAnswerOnePL = friendAnswerOnePL;
        this.friendMarkedAnswerOne = friendMarkedAnswerOne;
        this.friendIdQuestionTwo = friendIdQuestionTwo;
        this.friendQuestionTwoEN = friendQuestionTwoEN;
        this.friendAnswerTwoEN = friendAnswerTwoEN;
        this.friendQuestionTwoPL = friendQuestionTwoPL;
        this.friendAnswerTwoPL = friendAnswerTwoPL;
        this.friendMarkedAnswerTwo = friendMarkedAnswerTwo;
        this.friendIdQuestionThree = friendIdQuestionThree;
        this.friendQuestionThreeEN = friendQuestionThreeEN;
        this.friendAnswerThreeEN = friendAnswerThreeEN;
        this.friendQuestionThreePL = friendQuestionThreePL;
        this.friendAnswerThreePL = friendAnswerThreePL;
        this.friendMarkedAnswerThree = friendMarkedAnswerThree;
    }

    public String getMyIdSocialMedia() {
        return myIdSocialMedia;
    }

    public String getFriendIdSocialMedia() {
        return friendIdSocialMedia;
    }

    public boolean isMyActiveTurn() {
        return myActiveTurn;
    }

    public boolean isFriendActiveTurn() {
        return friendActiveTurn;
    }

    public ArrayList<Integer> getSelectedQuestions() {
        return selectedQuestions;
    }

    public int getMyIdQuestionOne() {
        return myIdQuestionOne;
    }

    public String getMyQuestionOneEN() {
        return myQuestionOneEN;
    }

    public ArrayList<String> getMyAnswerOneEN() {
        return myAnswerOneEN;
    }

    public String getMyQuestionOnePL() {
        return myQuestionOnePL;
    }

    public ArrayList<String> getMyAnswerOnePL() {
        return myAnswerOnePL;
    }

    public int getMyMarkedAnswerOne() {
        return myMarkedAnswerOne;
    }

    public int getMyIdQuestionTwo() {
        return myIdQuestionTwo;
    }

    public String getMyQuestionTwoEN() {
        return myQuestionTwoEN;
    }

    public ArrayList<String> getMyAnswerTwoEN() {
        return myAnswerTwoEN;
    }

    public String getMyQuestionTwoPL() {
        return myQuestionTwoPL;
    }

    public ArrayList<String> getMyAnswerTwoPL() {
        return myAnswerTwoPL;
    }

    public int getMyMarkedAnswerTwo() {
        return myMarkedAnswerTwo;
    }

    public int getMyIdQuestionThree() {
        return myIdQuestionThree;
    }

    public String getMyQuestionThreeEN() {
        return myQuestionThreeEN;
    }

    public ArrayList<String> getMyAnswerThreeEN() {
        return myAnswerThreeEN;
    }

    public String getMyQuestionThreePL() {
        return myQuestionThreePL;
    }

    public ArrayList<String> getMyAnswerThreePL() {
        return myAnswerThreePL;
    }

    public int getMyMarkedAnswerThree() {
        return myMarkedAnswerThree;
    }

    public int getFriendIdQuestionOne() {
        return friendIdQuestionOne;
    }

    public String getFriendQuestionOneEN() {
        return friendQuestionOneEN;
    }

    public ArrayList<String> getFriendAnswerOneEN() {
        return friendAnswerOneEN;
    }

    public String getFriendQuestionOnePL() {
        return friendQuestionOnePL;
    }

    public ArrayList<String> getFriendAnswerOnePL() {
        return friendAnswerOnePL;
    }

    public int getFriendMarkedAnswerOne() {
        return friendMarkedAnswerOne;
    }

    public int getFriendIdQuestionTwo() {
        return friendIdQuestionTwo;
    }

    public String getFriendQuestionTwoEN() {
        return friendQuestionTwoEN;
    }

    public ArrayList<String> getFriendAnswerTwoEN() {
        return friendAnswerTwoEN;
    }

    public String getFriendQuestionTwoPL() {
        return friendQuestionTwoPL;
    }

    public ArrayList<String> getFriendAnswerTwoPL() {
        return friendAnswerTwoPL;
    }

    public int getFriendMarkedAnswerTwo() {
        return friendMarkedAnswerTwo;
    }

    public int getFriendIdQuestionThree() {
        return friendIdQuestionThree;
    }

    public String getFriendQuestionThreeEN() {
        return friendQuestionThreeEN;
    }

    public ArrayList<String> getFriendAnswerThreeEN() {
        return friendAnswerThreeEN;
    }

    public String getFriendQuestionThreePL() {
        return friendQuestionThreePL;
    }

    public ArrayList<String> getFriendAnswerThreePL() {
        return friendAnswerThreePL;
    }

    public int getFriendMarkedAnswerThree() {
        return friendMarkedAnswerThree;
    }
}
