package com.capgemini.wsb.fitnesstracker.user.api;

import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User createUser(User user);

    List<User> findUsersByAgeGreaterThan(int age);

    Optional<User> getUser(final Long userId);

    List<User> findUsersOlderThanDate(LocalDate date);

    void deleteUser(Long userId);

    User updateUser(Long userId, User user);

    User getUserById(Long userId);
}
