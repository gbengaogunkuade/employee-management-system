package com.ogunkuade.employeemanagementsystem.dto;

public class AddressRequest {

    private Long id;
    private String lane1;
    private String lane2;
    private Long zip;
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
        return "Address{" +
                "id=" + id +
                ", lane1='" + lane1 + '\'' +
                ", lane2='" + lane2 + '\'' +
                ", zip=" + zip +
                ", state='" + state + '\'' +
                '}';
    }

}
