package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(final User user) {
        System.out.printf("Creating User %s", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByNameSurname(final String firstName, String lastName) {
        return (Optional<User>) userRepository.findByNameSurname(firstName,lastName);
    }

    @Override
    public List<User> findUsersByAgeGreaterThan(int age) {
        LocalDate now = LocalDate.now();
        LocalDate birthdate = now.minusYears(age);
        return userRepository.findUsersOlderThan(birthdate);
    }

    @Override
    public List<User> findUsersOlderThanDate(LocalDate date) {
        return userRepository.findUsersOlderThan(date);
    }


    @Override
    public Optional<User> getUserByEmail(final String email) {
        return (Optional<User>) userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setBirthdate(user.getBirthdate());
        existingUser.setEmail(user.getEmail());

        return userRepository.save(existingUser);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

//    public User saveUser(UserDto userDto) {
//        User user = new User(userDto.firstName(), userDto.lastName(), userDto.birthdate(), userDto.email());
//
//        User savedUser = userRepository.save(user);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser).getBody();
//    }

}