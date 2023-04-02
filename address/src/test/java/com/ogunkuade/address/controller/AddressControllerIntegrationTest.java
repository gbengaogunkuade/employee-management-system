package com.ogunkuade.address.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogunkuade.address.entity.Address;
import com.ogunkuade.address.repository.AddressRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AddressControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private Address address1;
    private Address address2;

    private Address address4;


    @BeforeEach
    void setUp() {

        address1 = new Address();
        address1.setId(1L);
        address1.setLane1("Adeola Odeku");
        address1.setLane2("Victoria Island");
        address1.setZip(112233L);
        address1.setState("Lagos");
        addressRepository.save(address1);

        address2 = new Address();
        address2.setId(2L);
        address2.setLane1("Idowu Taylor");
        address2.setLane2("Abeokuta");
        address2.setZip(445566L);
        address2.setState("Ogun");
        addressRepository.save(address2);

    }


    @AfterEach
    void tearDown(){
        addressRepository.deleteAll();
    }





    //      TEST GETTING ADDRESS
    @Test
    void test_getAddressById() throws Exception{
        mockMvc.perform(
                        get("/api/{id}", address1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lane1").value("Adeola Odeku"))
                .andExpect(jsonPath("$.state").value("Lagos"));
    }




    //      TEST GETTING ALL ADDRESSES
    @Test
    void test_getAllAddresses() throws Exception {
        mockMvc.perform(
                        get("/api/all")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lane1").value("Adeola Odeku"))
                .andExpect(jsonPath("$[0].state").value("Lagos"))
                .andExpect(jsonPath("$[1].lane2").value("Abeokuta"))
                .andExpect(jsonPath("$[1].zip").value(445566L));
    }





    //      TEST CREATE ADDRESS
    @Test
    void test_saveAddress() throws Exception {
        Address address3 = new Address();
        address3.setId(3L);
        address3.setLane1("Adeola Odeku");
        address3.setLane2("Victoria Island");
        address3.setZip(345522L);
        address3.setState("Lagos");

        String address1_String = objectMapper.writeValueAsString(address3);
        mockMvc.perform(
                        post("/api/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(address1_String)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }






    //      TEST UPDATING ADDRESS
    @Test
    void test_updateAddressById() throws Exception {
        address1.setLane1("Freeway Avenue");
        address1.setState("New York");
        String address1_String = objectMapper.writeValueAsString(address1);
        mockMvc.perform(
                        put("/api/{id}/update", address1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(address1_String)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lane1").value("Freeway Avenue"))
                .andExpect(jsonPath("$.state").value("New York"));
    }




    //      TEST DELETE ADDRESS
    @Test
    void test_deletingAddressById() throws Exception {
        mockMvc.perform(
                        delete("/api/{id}/delete", address1.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }





}
