package com.ceng453.gameClient.dao;

public class RecordDAO {
    /**
     * The username of the record owner
     */
    private String username;

    /**
     * Score of the user
     */
    private Long score;

    /**
     * Time of the record creation
     */
    private Long date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String newName) {
        username = newName;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long newScore) {
        score = newScore;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long newDate) {
        date = newDate;
    }
}
