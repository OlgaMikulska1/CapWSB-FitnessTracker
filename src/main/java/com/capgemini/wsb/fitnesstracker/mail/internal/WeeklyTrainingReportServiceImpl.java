package com.capgemini.wsb.fitnesstracker.mail.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

//@Service
@Component
public class WeeklyTrainingReportServiceImpl implements UserProvider {
    private final TrainingProvider trainingService;
    private final UserService userService;
    private final EmailSender emailSender;
    private UserProvider userProvider;

    private final UserRepository userRepository;

    public WeeklyTrainingReportServiceImpl(TrainingProvider trainingService, UserService userService, EmailSender emailSender, UserProvider userProvider, UserRepository userRepository) {
        this.trainingService = trainingService;
        this.userService = userService;
        this.emailSender = emailSender;
        this.userProvider = userProvider;
        this.userRepository = userRepository;
    }
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    @Scheduled(cron = "0 35 20 * * FRI") // co piątek o 20:35
    public void sendWeeklyTrainingReports() {
        List<User> users = userProvider.findAllUsers();

        for (User user : users) {
            List<Training> trainings = trainingService.getTrainingsForUser(user.getId());
            emailSender.send(trainings);
        }
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByNameSurname(String firstname, String lastName) {
        return Optional.empty();
    }
}
