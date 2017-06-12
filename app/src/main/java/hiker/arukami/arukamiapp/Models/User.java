package hiker.arukami.arukamiapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User {

    @SerializedName("IdCard")
    @Expose
    private float idCard;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("MiddleName")
    @Expose
    private String middleName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("SecondLastName")
    @Expose
    private String secondLastName;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("BirthDate")
    @Expose
    private String birthDate;
    @SerializedName("Nationality")
    @Expose
    private int nationality;
    @SerializedName("AccountNumber")
    @Expose
    private float accountNumber;

    @SerializedName("PhotoURL")
    @Expose
    private String photo;


    public float getIdCard() {
        return idCard;
    }

    public void setIdCard(float idCard) {
        this.idCard = idCard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getGender() {
        return gender.equals("M") ? "Male" : "Female";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBirthDateFormat(String formatString) {
        SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-dd-MM'T'hh:mm:ss", Locale.US);
        Date date = null;
        try {
            date = formatInput.parse(birthDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatOutput = new SimpleDateFormat(formatString, Locale.US);
        String output = formatOutput.format(date);
        return output;
    }


    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getNationality() {
        return nationality;
    }

    public void setNationality(int nationality) {
        this.nationality = nationality;
    }

    public float getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(float accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString(){
        String name = firstName;
        if(middleName!= null && !middleName.isEmpty()){
            name += " " + middleName;
        }
        name += " " + lastName;
        if(secondLastName!= null && !secondLastName.isEmpty()){
            name += " " + secondLastName;
        }
        return name;
    }

}
