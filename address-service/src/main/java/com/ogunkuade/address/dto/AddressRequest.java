package com.ogunkuade.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressRequest {

    private Long id;
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
    @Size(min = 2, max = 15, message = "state must be atleast 4 characters and not more than 15 characters")
    private String state;

    public AddressRequest() {
    }

    public AddressRequest(String lane1, String lane2, Long zip, String state) {
        this.lane1 = lane1;
        this.lane2 = lane2;
        this.zip = zip;
        this.state = state;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "AddressRequest{" +
                "id=" + id +
                ", lane1='" + lane1 + '\'' +
                ", lane2='" + lane2 + '\'' +
                ", zip=" + zip +
                ", state='" + state + '\'' +
                '}';
    }

}
