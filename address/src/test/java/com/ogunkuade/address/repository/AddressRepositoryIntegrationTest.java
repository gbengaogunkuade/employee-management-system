package com.ogunkuade.address.repository;


import com.ogunkuade.address.entity.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class AddressRepositoryIntegrationTest {


    @Autowired
    private AddressRepository addressRepository;

    private Address address1;
    private Address address2;


    @BeforeEach
    void setUp() {
        address1 = new Address();
        address1.setId(1L);
        address1.setLane1("Adeola Odeku Street");
        address1.setLane2("Victoria Island");
        address1.setZip(112233L);
        address1.setState("Lagos");
        addressRepository.save(address1);

        address2 = new Address();
        address2.setId(2L);
        address2.setLane1("Idowu Taylor Street");
        address2.setLane2("Abeokuta");
        address2.setZip(445566L);
        address2.setState("Ogun");
        addressRepository.save(address2);
    }


    @AfterEach
    void tearDown(){
        addressRepository.deleteAll();
    }


    @Test
    void test_findAddressById(){
        Address addressFound;
        addressFound = addressRepository.findAddressById(address1.getId());
        assertThat(addressFound.getLane1()).isEqualTo("Adeola Odeku Street");
        assertThat(addressFound.getZip()).isEqualTo(112233L);
        assertThat(addressFound.getState()).isEqualTo("Lagos");
    }





}
