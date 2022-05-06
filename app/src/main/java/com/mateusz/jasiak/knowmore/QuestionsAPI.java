package com.mateusz.jasiak.knowmore;

public class QuestionsAPI {

    private Integer idQuestions;
    private String questionsEN;
    private String answerOneEN;
    private String answerTwoEN;
    private String answerThreeEN;
    private String answerFourEN;
    private String questionsPL;
    private String answerOnePL;
    private String answerTwoPL;
    private String answerThreePL;
    private String answerFourPL;

    public QuestionsAPI(Integer idQuestions, String questionsEN, String answerOneEN, String answerTwoEN, String answerThreeEN, String answerFourEN, String questionsPL, String answerOnePL, String answerTwoPL, String answerThreePL, String answerFourPL) {
        this.idQuestions = idQuestions;
        this.questionsEN = questionsEN;
        this.answerOneEN = answerOneEN;
        this.answerTwoEN = answerTwoEN;
        this.answerThreeEN = answerThreeEN;
        this.answerFourEN = answerFourEN;
        this.questionsPL = questionsPL;
        this.answerOnePL = answerOnePL;
        this.answerTwoPL = answerTwoPL;
        this.answerThreePL = answerThreePL;
        this.answerFourPL = answerFourPL;
    }

    public Integer getIdQuestions() {
        return idQuestions;
    }

    public String getQuestionsEN() {
        return questionsEN;
    }

    public String getAnswerOneEN() {
        return answerOneEN;
    }

    public String getAnswerTwoEN() {
        return answerTwoEN;
    }

    public String getAnswerThreeEN() {
        return answerThreeEN;
    }

    public String getAnswerFourEN() {
        return answerFourEN;
    }

    public String getQuestionsPL() {
        return questionsPL;
    }

    public String getAnswerOnePL() {
        return answerOnePL;
    }

    public String getAnswerTwoPL() {
        return answerTwoPL;
    }

    public String getAnswerThreePL() {
        return answerThreePL;
    }

    public String getAnswerFourPL() {
        return answerFourPL;
    }
}
