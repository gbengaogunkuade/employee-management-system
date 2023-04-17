package com.ogunkuade.address.repository;

import com.ogunkuade.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findAddressById(Long id);


}
