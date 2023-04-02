package com.ogunkuade.employeemanagementsystem.feignclient;


import com.ogunkuade.employeemanagementsystem.dto.AddressRequest;
import com.ogunkuade.employeemanagementsystem.dto.AddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "ADDRESS-SERVICE", path = "/address-app/api")
public interface AddressClient {


    @GetMapping("/{id}/check")
    Boolean checkForId(@PathVariable Long id);

    @PostMapping("/create")
    AddressResponse savingAddress(@RequestBody AddressRequest addressRequest);

    @GetMapping("/{id}")
    AddressResponse gettingAddressById(@PathVariable Long id);

    @GetMapping("/all")
    List<AddressResponse> gettingAllAddresses();

    @PutMapping("/{id}/update")
    AddressResponse updatingAddressById(@RequestBody AddressRequest addressRequest, @PathVariable Long id);

    @DeleteMapping("/{id}/delete")
    String deletingAddressById(@PathVariable Long id);


}




