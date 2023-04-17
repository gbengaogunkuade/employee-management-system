package com.ogunkuade.address.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogunkuade.address.dto.AddressResponse;
import com.ogunkuade.address.entity.Address;
import com.ogunkuade.address.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(AddressController.class)
public class AddressControllerUnitTest {


    @MockBean
    private AddressService addressService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    private Address address1;
    private Address address2;
    private List<Address> addressList;

    private AddressResponse addressResponse1;
    private AddressResponse addressResponse2;
    private List<AddressResponse> addressResponseList;


    @BeforeEach
    void setUp() {
        address1 = new Address();
        address1.setId(1L);
        address1.setLane1("Adeola Odeku Street");
        address1.setLane2("Victoria Island");
        address1.setZip(112233L);
        address1.setState("Lagos");

        address2 = new Address();
        address2.setId(2L);
        address2.setLane1("Idowu Taylor Street");
        address2.setLane2("Abeokuta");
        address2.setZip(445566L);
        address2.setState("Ogun");

        addressList = new ArrayList<>();
        addressList.add(address1);
        addressList.add(address2);

        addressResponse1 = new AddressResponse();
        addressResponse1.setId(address1.getId());
        addressResponse1.setLane1(address1.getLane1());
        addressResponse1.setLane2(address1.getLane2());
        addressResponse1.setZip(address1.getZip());
        addressResponse1.setState(address1.getState());

        addressResponse2 = new AddressResponse();
        addressResponse2.setId(address2.getId());
        addressResponse2.setLane1(address2.getLane1());
        addressResponse2.setLane2(address2.getLane2());
        addressResponse2.setZip(address2.getZip());
        addressResponse2.setState(address2.getState());

        addressResponseList = new ArrayList<>();
        addressResponseList.add(addressResponse1);
        addressResponseList.add(addressResponse2);

    }




    //      TEST CREATE ADDRESS
    @Test
    void test_saveAddress() throws Exception {
        when(addressService.saveAddress(address1)).thenReturn(addressResponse1);
        String address1_String = objectMapper.writeValueAsString(address1);
        mockMvc.perform(
                        post("/api/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(address1_String)
                )
                .andDo(print())
                .andExpect(status().isOk());

        verify(addressService).saveAddress(any(Address.class));
    }




    //      TEST GETTING ADDRESS
    @Test
    void test_getAddressById() throws Exception{
        when(addressService.getAddressById(address1.getId())).thenReturn(addressResponse1);
        mockMvc.perform(
                        get("/api/{id}", address1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lane1").value("Adeola Odeku Street"))
                .andExpect(jsonPath("$.state").value("Lagos"));
        verify(addressService).getAddressById(eq(address1.getId()));
    }




    //      TEST GETTING ALL ADDRESSES
    @Test
    void test_getAllAddresses() throws Exception {
        when(addressService.getAllAddresses()).thenReturn(addressResponseList);
        mockMvc.perform(
                        get("/api/all")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lane1").value("Adeola Odeku Street"))
                .andExpect(jsonPath("$[0].state").value("Lagos"))
                .andExpect(jsonPath("$[1].lane2").value("Abeokuta"))
                .andExpect(jsonPath("$[1].zip").value(445566L));
        verify(addressService).getAllAddresses();
    }





    //      TEST UPDATING ADDRESS
    @Test
    void test_updateAddressById() throws Exception {
        when(addressService.updateAddressById(address1, address1.getId())).thenReturn(addressResponse1);
        String address1_String = objectMapper.writeValueAsString(address1);
        mockMvc.perform(
                        put("/api/{id}/update", address1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(address1_String)
                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(addressService).updateAddressById(any(Address.class), eq(address1.getId()));
    }







    //      TEST DELETE ADDRESS
    @Test
    void test_deletingAddressById() throws Exception {
        when(addressService.deletingAddressById(address1.getId())).thenReturn("user successfully deleted");
        mockMvc.perform(
                        delete("/api/{id}/delete", address1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(addressService).deletingAddressById(eq(address1.getId()));
    }





}
