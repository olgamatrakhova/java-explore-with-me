package ru.practicum.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.users.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.id IN ?1 ")
    List<User> getAllById(List<Long> id, Pageable pageable);

    @Query("select u from User u")
    List<User> getAllUser(Pageable pageable);

    Boolean getByEmail(String email);
}