package com.ogunkuade.employeemanagementsystem.dto;


import jakarta.validation.constraints.*;

public class EmsUpdateRequest {

    @NotBlank(message = "firstname must not be blank")
    @NotEmpty(message = "firstname must not be empty")
    @NotNull(message = "firstname must not be null")
    @Size(min = 4, max = 20, message = "firstname must be atleast 4 characters and not more than 20 characters")
    private String firstname;

    @NotBlank(message = "lastname must not be blank")
    @NotEmpty(message = "lastname must not be empty")
    @NotNull(message = "lastname must not be null")
    @Size(min = 4, max = 20, message = "lastname must be atleast 4 characters and not more than 20 characters")
    private String lastname;

    @NotBlank(message = "gender must not be blank")
    @NotEmpty(message = "gender must not be empty")
    @NotNull(message = "gender must not be null")
    private String gender;

    @NotBlank(message = "email must not be blank")
    @NotEmpty(message = "email must not be empty")
    @NotNull(message = "email must not be null")
    @Email
    private String email;

    @NotBlank(message = "lane1 must not be blank")
    @NotEmpty(message = "lane1 must not be empty")
    @NotNull(message = "lane1 must not be null")
    @Size(min = 4, max = 20, message = "lane1 must be atleast 4 characters and not more than 20 characters")
    private String lane1;

    @NotBlank(message = "lane2 must not be blank")
    @NotEmpty(message = "lane2 must not be empty")
    @NotNull(message = "lane2 must not be null")
    @Size(min = 4, max = 20, message = "lane2 must be atleast 4 characters and not more than 20 characters")
    private String lane2;

    @NotNull(message = "zip must not be null")
    private Long zip;

    @NotBlank(message = "state must not be blank")
    @NotEmpty(message = "state must not be empty")
    @NotNull(message = "state must not be null")
    @Size(min = 2, max = 15, message = "state must be atleast 2 characters and not more than 15 characters")
    private String state;


    public EmsUpdateRequest() {
    }

    public EmsUpdateRequest(String firstname, String lastname, String gender, String email, String lane1, String lane2, Long zip, String state) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.email = email;
        this.lane1 = lane1;
        this.lane2 = lane2;
        this.zip = zip;
        this.state = state;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLane1() {
        return lane1;
    }

    public void setLane1(String lane1) {
        this.lane1 = lane1;
    }

    public String getLane2() {
        return lane2;
    }

    public void setLane2(String lane2) {
        this.lane2 = lane2;
    }

    public Long getZip() {
        return zip;
    }

    public void setZip(Long zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "EmsUpdateRequest{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", lane1='" + lane1 + '\'' +
                ", lane2='" + lane2 + '\'' +
                ", zip=" + zip +
                ", state='" + state + '\'' +
                '}';
    }


}
