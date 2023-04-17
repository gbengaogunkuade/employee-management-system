package com.ogunkuade.employeemanagementsystem.dto;


public class EmsResponse {

    private UserResponse userResponse;
    private AddressResponse addressResponse;

    public EmsResponse() {
    }

    public EmsResponse(UserResponse userResponse, AddressResponse addressResponse) {
        this.userResponse = userResponse;
        this.addressResponse = addressResponse;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public AddressResponse getAddressResponse() {
        return addressResponse;
    }

    public void setAddressResponse(AddressResponse addressResponse) {
        this.addressResponse = addressResponse;
    }

    @Override
    public String toString() {
        return "EmsResponse{" +
                "userResponse=" + userResponse +
                ", addressResponse=" + addressResponse +
                '}';
    }


}
