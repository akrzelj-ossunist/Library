package com.maurer.library.repositories;

import com.maurer.library.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findByFullName(String s);

    List<User> findByAddress(String s);

    @Query("SELECT u FROM User u " +
            "WHERE (:fullName IS NULL OR u.fullName = :fullName) " +
            "AND (:address IS NULL OR u.address = :address) " +
            "AND (:email IS NULL OR u.email = :email)")
    Page<User> findByFullNameAndAddressAndEmail(String fullName, String address, String email, Pageable pageable);


}
