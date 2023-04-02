package com.ogunkuade.address.service;


import com.ogunkuade.address.dto.AddressResponse;
import com.ogunkuade.address.entity.Address;
import com.ogunkuade.address.exception.AddressNotFoundException;
import com.ogunkuade.address.repository.AddressRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;



@Service
public class AddressService {


    private AddressRepository addressRepository;


    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }



    public Boolean checkForId(Long id){
        return addressRepository.existsById(id);
    }



    //  SAVE ADDRESS
    public AddressResponse saveAddress(Address address){
        Address savedAddress = addressRepository.save(address);
        AddressResponse addressResponse = new AddressResponse(
                savedAddress.getId(),
                savedAddress.getLane1(),
                savedAddress.getLane2(),
                savedAddress.getZip(),
                savedAddress.getState()
        );
        return addressResponse;
    }





    //  GET ADDRESS BY ID
    public AddressResponse getAddressById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(String.format("an address with the id %d does not exist", id)));
        AddressResponse addressResponse = new AddressResponse(
                address.getId(),
                address.getLane1(),
                address.getLane2(),
                address.getZip(),
                address.getState()
        );
        return addressResponse;
    }




    //  GET ALL ADDRESSES
    public List<AddressResponse> getAllAddresses(){
        List<Address> addresses = addressRepository.findAll();
        List<AddressResponse> addressesFound = new ArrayList<>();

        for(Address address : addresses){
            addressesFound.add(
                    new AddressResponse(
                            address.getId(),
                            address.getLane1(),
                            address.getLane2(),
                            address.getZip(),
                            address.getState()
                    )
            );
        }
        return addressesFound;
    }





    //  UPDATE ADDRESS BY ID
    public AddressResponse updateAddressById(Address address, Long id) throws AddressNotFoundException {
        Address addressToBeUpdated = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(String.format("an address with the id %d does not exist", id)));
        addressToBeUpdated.setLane1(address.getLane1());
        addressToBeUpdated.setLane2(address.getLane2());
        addressToBeUpdated.setZip(address.getZip());
        addressToBeUpdated.setState(address.getState());
        Address updatedAddress = addressRepository.save(addressToBeUpdated);
        //                                                                  /
        AddressResponse addressResponseNew = new AddressResponse();
        addressResponseNew.setId(updatedAddress.getId());
        addressResponseNew.setLane1(updatedAddress.getLane1());
        addressResponseNew.setLane2(updatedAddress.getLane2());
        addressResponseNew.setZip(updatedAddress.getZip());
        addressResponseNew.setState(updatedAddress.getState());
        return addressResponseNew;
    }




    //  DELETE ADDRESS BY ID
    public String deletingAddressById(Long id) {
        Address addressToBeDeleted = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(String.format("an address with the id %d does not exist", id)));
        addressRepository.delete(addressToBeDeleted);
        return "Address with the requested ID has been deleted";
    }





}
