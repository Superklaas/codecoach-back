package com.switchfully.spectangular.repository;
import com.switchfully.spectangular.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findAccountByEmailAndEncryptedPassword(String email, String password);
}
