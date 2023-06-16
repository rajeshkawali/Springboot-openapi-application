package com.rajeshkawali.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rajeshkawali.entity.Customer;

/**
 * @author Rajesh_Kawali
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query(value = "SELECT * FROM customer_inventory c WHERE c.surname = ?1", nativeQuery = true)
	Optional<Customer> findBySurname(String surname);

}
