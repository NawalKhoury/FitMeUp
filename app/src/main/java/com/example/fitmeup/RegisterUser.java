package com.example.fitmeup;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "register_users")
public class RegisterUser {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String email;
    private String confirmEmail;
    private String password;
    private String confirmPassword;
    private String birthDate;
    private String gender;
    private String healthProblemStatus;
    private String securityQuestion;
    private String securityAnswer;
    private String weight;
    private String height;
    private int level;

    public RegisterUser(String username, String confirmEmail, String email, String password, String confirmPassword, String birthDate, String gender, String healthProblemStatus, String securityQuestion, String securityAnswer, String weight, String height, int level) {
        this.username = username;
        this.confirmEmail = confirmEmail;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.birthDate = birthDate;
        this.gender = gender;
        this.healthProblemStatus = healthProblemStatus;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.weight = weight;
        this.height = height;
        this.level = level;
    }

    // Getters and setters for each field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHealthProblemStatus() {
        return healthProblemStatus;
    }

    public void setHealthProblemStatus(String healthProblemStatus) {
        this.healthProblemStatus = healthProblemStatus;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
