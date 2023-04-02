package com.ogunkuade.address.controller;


import com.ogunkuade.address.dto.AddressResponse;
import com.ogunkuade.address.entity.Address;
import com.ogunkuade.address.exception.AddressNotFoundException;
import com.ogunkuade.address.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class AddressController {


    private AddressService addressService;


    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }



    @GetMapping("/{id}/check")
    public Boolean checkForId(@PathVariable Long id){
        return addressService.checkForId(id);
    }




    //SAVING ADDRESS
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponse savingAddress(@Valid @RequestBody Address address){
        return addressService.saveAddress(address);
    }



    //GETTING ADDRESS BY ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponse gettingAddressById(@PathVariable Long id) {
        System.out.println("Getting address with id " + id);
        return addressService.getAddressById(id);
    }



    //GETTING ALL ADDRESSES
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressResponse> gettingAllAddresses(){
        return addressService.getAllAddresses();
    }



    //  UPDATING ADDRESS BY ID
    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponse updatingAddressById(@Valid @RequestBody Address address, @PathVariable Long id) throws AddressNotFoundException {
        return addressService.updateAddressById(address, id);
    }


    // DELETING ADDRESS BY ID
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public String deletingAddressById(@PathVariable Long id) {
        return addressService.deletingAddressById(id);
    }





}
