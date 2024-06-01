package com.capgemini.wsb.fitnesstracker.training;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingRepository;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@Transactional
class TrainingServiceImplTest {

    private TrainingProvider trainingProvider;

    @Mock
    private TrainingRepository trainingRepository;

    private Training training1;
    private Training training2;
    private Training training3;
    private Training training4;

@BeforeEach
public void setUp() {
    MockitoAnnotations.initMocks(this);
    trainingProvider = new TrainingServiceImpl(trainingRepository);

    User user1 = new User("Marek", "Mostowiak", LocalDate.of(1975, 4, 1), "marek.mostowiak@example.com");
    User user2 = new User("Kinga", "Zduńska", LocalDate.of(1985, 3, 15), "kinga.zdunska@example.com");
    User user3 = new User("Grażyna", "Łagoda", LocalDate.of(1982, 11, 26), "grazyna.lagoda@example.com");

    Date startTime = new Date();

    Date endTime1 = new Date(startTime.getTime() + 15000);
    Date endTime2 = new Date(startTime.getTime() + 25000);
    Date endTime3 = new Date(startTime.getTime() + 35000);
    Date endTime4 = new Date(startTime.getTime() + 60000);

    training1 = new Training(user2, startTime, endTime1, ActivityType.SWIMMING, 1.0, 2.0);
    training2 = new Training(user1, startTime, endTime2, ActivityType.RUNNING, 10.0, 8.5);
    training3 = new Training(user3, startTime, endTime3, ActivityType.CYCLING, 20.0, 15.0);
    training4 = new Training(user1, startTime, endTime4, ActivityType.WALKING, 2.0, 3.5);
}


    @Test
    void shouldCreateTrainingSuccessfully() {
        // Przygotowanie
        when(trainingRepository.save(training1)).thenReturn(training1);

        // Działanie
        Training createdTraining = trainingProvider.createTraining(training1);

        // Asercja
        assertEquals(training1, createdTraining);
    }

    @Test
    void shouldFindAllTrainings() {
        // Przygotowanie
        List<Training> expectedTrainings = Arrays.asList(training1, training2, training3);
        when(trainingRepository.findAll()).thenReturn(expectedTrainings);

        // Działanie
        List<Training> foundTrainings = trainingProvider.getAllTrainings();

        // Asercja
        assertEquals(expectedTrainings, foundTrainings);
    }

    @Test
    void shouldFindAllTrainingsForUserId() {
        // Przygotowanie
        Long userId = 1L;
        when(trainingRepository.findByUserId(userId)).thenReturn(Arrays.asList(training1, training2, training4));

        // Działanie
        Iterable<Training> userTrainings = trainingProvider.getTrainingsForUser(userId);
        List<Training> userTrainingList = (List<Training>) userTrainings;

        // Asercja
        assertEquals(3, userTrainingList.size());
        assertTrue(userTrainingList.contains(training1));
        assertTrue(userTrainingList.contains(training2));
        assertTrue(userTrainingList.contains(training4));
    }

    @Test
    void findAllCompletedTrainingsAfterDate() {
        // Przygotowane
        Date date = new Date();
        when(trainingRepository.findByEndTimeAfter(date)).thenReturn(Arrays.asList(training1, training2, training4));

        // Działanie
        Iterable<Training> completedTrainings = trainingProvider.getFinishedTrainingsAfterTime(date);
        List<Training> completedTrainingList = (List<Training>) completedTrainings;

        // Asercja
        assertEquals(3, completedTrainingList.size());
        assertTrue(completedTrainingList.contains(training1));
        assertTrue(completedTrainingList.contains(training2));
        assertTrue(completedTrainingList.contains(training4));
    }

    @Test
    void shouldFindAllTrainingsForGivenActivityType() {
        // Przygotowanie
        ActivityType activityType = ActivityType.RUNNING;
        when(trainingRepository.findByActivityType(activityType)).thenReturn(Arrays.asList(training1, training4));

        // Działanie
        List<Training> foundTrainings = trainingProvider.getTrainingsByActivityType(activityType);

        // Asercja
        assertEquals(2, foundTrainings.size());
        assertTrue(foundTrainings.contains(training1));
        assertTrue(foundTrainings.contains(training4));
    }
}
