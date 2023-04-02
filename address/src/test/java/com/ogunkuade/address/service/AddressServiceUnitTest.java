package com.ogunkuade.address.service;



import com.ogunkuade.address.dto.AddressResponse;
import com.ogunkuade.address.entity.Address;
import com.ogunkuade.address.exception.AddressNotFoundException;
import com.ogunkuade.address.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AddressServiceUnitTest {


    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;


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
    void test_saveAddress() {
        when(addressRepository.save(address1)).thenReturn(address1);
        addressResponse1 = addressService.saveAddress(address1);
        assertThat(addressResponse1).isNotNull();
        assertThat(addressResponse1.getZip()).isEqualTo(112233L);
        assertThat(addressResponse1.getState()).isEqualTo("Lagos");
        verify(addressRepository).save(address1);
    }




    //      TEST GETTING ADDRESS
    @Test
    void test_getAddressById() {
        when(addressRepository.findById(address1.getId())).thenReturn(Optional.of(address1));
        addressResponse1 = addressService.getAddressById(address1.getId());
        assertThat(addressResponse1).isNotNull();
        assertThat(addressResponse1.getZip()).isEqualTo(112233L);
        assertThat(addressResponse1.getState()).isEqualTo("Lagos");
        verify(addressRepository).findById(address1.getId());
    }

    @Test
    void test_getAddressById_NotFound() {
        AddressNotFoundException addressNotFoundException = assertThrows(AddressNotFoundException.class, () -> addressService.getAddressById(address1.getId()));

        assertTrue(addressNotFoundException.getMessage().equals(String.format("an address with the id %d does not exist", address1.getId())));
    }




    //      TEST GETTING ALL ADDRESSES
    @Test
    void test_getAllAddresses() {
        when(addressRepository.findAll()).thenReturn(addressList);
        addressResponseList = addressService.getAllAddresses();
        assertThat(addressResponseList).isNotNull();
        assertThat(addressResponseList.size()).isEqualTo(addressList.size());
        assertThat(addressResponseList.get(1).getLane2()).isEqualTo("Abeokuta");
        verify(addressRepository).findAll();
    }





    //      TEST UPDATING ADDRESS
    @Test
    void test_updateAddressById() {
        AddressResponse updatedAddressResponse;
        when(addressRepository.findById(address1.getId())).thenReturn(Optional.of(address1));
        address1.setLane1("Allen Avenue");
        address1.setZip(999999L);
        when(addressRepository.save(address1)).thenReturn(address1);
        updatedAddressResponse = addressService.updateAddressById(address1, address1.getId());
        assertThat(updatedAddressResponse).isNotNull();
        assertThat(updatedAddressResponse.getLane1()).isEqualTo("Allen Avenue");
        assertThat(updatedAddressResponse.getZip()).isEqualTo(999999L);
        verify(addressRepository).findById(address1.getId());
        verify(addressRepository).save(address1);
    }


    @Test
    void test_updateAddressById_NotFound() {
        AddressNotFoundException addressNotFoundException = assertThrows(AddressNotFoundException.class, () -> addressService.updateAddressById(address1, address1.getId()));

        assertTrue(addressNotFoundException.getMessage().equals(String.format("an address with the id %d does not exist", address1.getId())));
    }






    //      TEST DELETE ADDRESS
    @Test
    void test_deletingAddressById() {
        String returnedMessage;
        when(addressRepository.findById(address1.getId())).thenReturn(Optional.of(address1));
        doNothing().when(addressRepository).delete(address1);
        returnedMessage = addressService.deletingAddressById(address1.getId());
        assertThat(returnedMessage).isEqualTo("Address with the requested ID has been deleted");
        verify(addressRepository).findById(address1.getId());
        verify(addressRepository).delete(address1);
    }


    @Test
    void test_deletingAddressById_NotFound() {
        AddressNotFoundException addressNotFoundException = assertThrows(AddressNotFoundException.class, () -> addressService.deletingAddressById(address1.getId()));

        assertTrue(addressNotFoundException.getMessage().equals(String.format("an address with the id %d does not exist", address1.getId())));
    }





}
