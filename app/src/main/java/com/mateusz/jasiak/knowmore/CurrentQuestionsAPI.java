package com.mateusz.jasiak.knowmore;

import java.util.ArrayList;

public class CurrentQuestionsAPI {

    private String _id;
    private String myIdSocialMedia;
    private String friendIdSocialMedia;
    private String whoseTurn;
    private boolean gameProper;
    private ArrayList<String> selectedQuestions = new ArrayList<>();
    private Integer myIdQuestionOne;
    private String myQuestionOneEN;
    private ArrayList<String> myAnswerOneEN = new ArrayList<>();
    private String myQuestionOnePL;
    private ArrayList<String> myAnswerOnePL = new ArrayList<>();
    private Integer myMarkedAnswerOne;
    private Integer myFriendMarkedAnswerOne;
    private Integer myIdQuestionTwo;
    private String myQuestionTwoEN;
    private ArrayList<String> myAnswerTwoEN = new ArrayList<>();
    private String myQuestionTwoPL;
    private ArrayList<String> myAnswerTwoPL = new ArrayList<>();
    private Integer myMarkedAnswerTwo;
    private Integer myFriendMarkedAnswerTwo;
    private Integer myIdQuestionThree;
    private String myQuestionThreeEN;
    private ArrayList<String> myAnswerThreeEN = new ArrayList<>();
    private String myQuestionThreePL;
    private ArrayList<String> myAnswerThreePL = new ArrayList<>();
    private Integer myMarkedAnswerThree;
    private Integer myFriendMarkedAnswerThree;
    private Integer friendIdQuestionOne;
    private String friendQuestionOneEN;
    private ArrayList<String> friendAnswerOneEN = new ArrayList<>();
    private String friendQuestionOnePL;
    private ArrayList<String> friendAnswerOnePL = new ArrayList<>();
    private Integer friendMarkedAnswerOne;
    private Integer friendIdQuestionTwo;
    private String friendQuestionTwoEN;
    private ArrayList<String> friendAnswerTwoEN = new ArrayList<>();
    private String friendQuestionTwoPL;
    private ArrayList<String> friendAnswerTwoPL = new ArrayList<>();
    private Integer friendMarkedAnswerTwo;
    private Integer friendIdQuestionThree;
    private String friendQuestionThreeEN;
    private ArrayList<String> friendAnswerThreeEN = new ArrayList<>();
    private String friendQuestionThreePL;
    private ArrayList<String> friendAnswerThreePL = new ArrayList<>();
    private Integer friendMarkedAnswerThree;

    //All
    public CurrentQuestionsAPI(String myIdSocialMedia, String friendIdSocialMedia, String whoseTurn, boolean gameProper, ArrayList<String> selectedQuestions, Integer myIdQuestionOne, String myQuestionOneEN, ArrayList<String> myAnswerOneEN, String myQuestionOnePL, ArrayList<String> myAnswerOnePL, Integer myMarkedAnswerOne, Integer myFriendMarkedAnswerOne, Integer myIdQuestionTwo, String myQuestionTwoEN, ArrayList<String> myAnswerTwoEN, String myQuestionTwoPL, ArrayList<String> myAnswerTwoPL, Integer myMarkedAnswerTwo, Integer myFriendMarkedAnswerTwo, Integer myIdQuestionThree, String myQuestionThreeEN, ArrayList<String> myAnswerThreeEN, String myQuestionThreePL, ArrayList<String> myAnswerThreePL, Integer myMarkedAnswerThree, Integer myFriendMarkedAnswerThree, Integer friendIdQuestionOne, String friendQuestionOneEN, ArrayList<String> friendAnswerOneEN, String friendQuestionOnePL, ArrayList<String> friendAnswerOnePL, Integer friendMarkedAnswerOne, Integer friendIdQuestionTwo, String friendQuestionTwoEN, ArrayList<String> friendAnswerTwoEN, String friendQuestionTwoPL, ArrayList<String> friendAnswerTwoPL, Integer friendMarkedAnswerTwo, Integer friendIdQuestionThree, String friendQuestionThreeEN, ArrayList<String> friendAnswerThreeEN, String friendQuestionThreePL, ArrayList<String> friendAnswerThreePL, Integer friendMarkedAnswerThree) {
        this.myIdSocialMedia = myIdSocialMedia;
        this.friendIdSocialMedia = friendIdSocialMedia;
        this.whoseTurn = whoseTurn;
        this.gameProper = gameProper;
        this.selectedQuestions = selectedQuestions;
        this.myIdQuestionOne = myIdQuestionOne;
        this.myQuestionOneEN = myQuestionOneEN;
        this.myAnswerOneEN = myAnswerOneEN;
        this.myQuestionOnePL = myQuestionOnePL;
        this.myAnswerOnePL = myAnswerOnePL;
        this.myMarkedAnswerOne = myMarkedAnswerOne;
        this.myFriendMarkedAnswerOne = myFriendMarkedAnswerOne;
        this.myIdQuestionTwo = myIdQuestionTwo;
        this.myQuestionTwoEN = myQuestionTwoEN;
        this.myAnswerTwoEN = myAnswerTwoEN;
        this.myQuestionTwoPL = myQuestionTwoPL;
        this.myAnswerTwoPL = myAnswerTwoPL;
        this.myMarkedAnswerTwo = myMarkedAnswerTwo;
        this.myFriendMarkedAnswerTwo = myFriendMarkedAnswerTwo;
        this.myIdQuestionThree = myIdQuestionThree;
        this.myQuestionThreeEN = myQuestionThreeEN;
        this.myAnswerThreeEN = myAnswerThreeEN;
        this.myQuestionThreePL = myQuestionThreePL;
        this.myAnswerThreePL = myAnswerThreePL;
        this.myMarkedAnswerThree = myMarkedAnswerThree;
        this.myFriendMarkedAnswerThree = myFriendMarkedAnswerThree;
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

    //First post
    public CurrentQuestionsAPI(String myIdSocialMedia, String friendIdSocialMedia, String whoseTurn, boolean gameProper, ArrayList<String> selectedQuestions, Integer myIdQuestionOne, String myQuestionOneEN, ArrayList<String> myAnswerOneEN, String myQuestionOnePL, ArrayList<String> myAnswerOnePL, Integer myMarkedAnswerOne, Integer myIdQuestionTwo, String myQuestionTwoEN, ArrayList<String> myAnswerTwoEN, String myQuestionTwoPL, ArrayList<String> myAnswerTwoPL, Integer myMarkedAnswerTwo, Integer myIdQuestionThree, String myQuestionThreeEN, ArrayList<String> myAnswerThreeEN, String myQuestionThreePL, ArrayList<String> myAnswerThreePL, Integer myMarkedAnswerThree) {
        this.myIdSocialMedia = myIdSocialMedia;
        this.friendIdSocialMedia = friendIdSocialMedia;
        this.whoseTurn = whoseTurn;
        this.gameProper = gameProper;
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
    }

    //Update 1
    public CurrentQuestionsAPI(String whoseTurn, boolean gameProper, ArrayList<String> selectedQuestions, Integer myFriendMarkedAnswerOne, Integer myFriendMarkedAnswerTwo, Integer myFriendMarkedAnswerThree, Integer friendIdQuestionOne, String friendQuestionOneEN, ArrayList<String> friendAnswerOneEN, String friendQuestionOnePL, ArrayList<String> friendAnswerOnePL, Integer friendMarkedAnswerOne, Integer friendIdQuestionTwo, String friendQuestionTwoEN, ArrayList<String> friendAnswerTwoEN, String friendQuestionTwoPL, ArrayList<String> friendAnswerTwoPL, Integer friendMarkedAnswerTwo, Integer friendIdQuestionThree, String friendQuestionThreeEN, ArrayList<String> friendAnswerThreeEN, String friendQuestionThreePL, ArrayList<String> friendAnswerThreePL, Integer friendMarkedAnswerThree) {
        this.whoseTurn = whoseTurn;
        this.gameProper = gameProper;
        this.selectedQuestions = selectedQuestions;
        this.myFriendMarkedAnswerOne = myFriendMarkedAnswerOne;
        this.myFriendMarkedAnswerTwo = myFriendMarkedAnswerTwo;
        this.myFriendMarkedAnswerThree = myFriendMarkedAnswerThree;
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

    public String getId() {
        return _id;
    }

    public String getMyIdSocialMedia() {
        return myIdSocialMedia;
    }

    public String getFriendIdSocialMedia() {
        return friendIdSocialMedia;
    }

    public String getWhoseTurn() {
        return whoseTurn;
    }

    public boolean getGameProper() {
        return gameProper;
    }

    public ArrayList<String> getSelectedQuestions() {
        return selectedQuestions;
    }

    public Integer getMyIdQuestionOne() {
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

    public Integer getMyMarkedAnswerOne() {
        return myMarkedAnswerOne;
    }

    public Integer getMyFriendMarkedAnswerOne() {
        return myFriendMarkedAnswerOne;
    }

    public Integer getMyIdQuestionTwo() {
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

    public Integer getMyMarkedAnswerTwo() {
        return myMarkedAnswerTwo;
    }

    public Integer getMyFriendMarkedAnswerTwo() {
        return myFriendMarkedAnswerTwo;
    }

    public Integer getMyIdQuestionThree() {
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

    public Integer getMyMarkedAnswerThree() {
        return myMarkedAnswerThree;
    }

    public Integer getMyFriendMarkedAnswerThree() {
        return myFriendMarkedAnswerThree;
    }

    public Integer getFriendIdQuestionOne() {
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

    public Integer getFriendMarkedAnswerOne() {
        return friendMarkedAnswerOne;
    }

    public Integer getFriendIdQuestionTwo() {
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

    public Integer getFriendMarkedAnswerTwo() {
        return friendMarkedAnswerTwo;
    }

    public Integer getFriendIdQuestionThree() {
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

    public Integer getFriendMarkedAnswerThree() {
        return friendMarkedAnswerThree;
    }
}
