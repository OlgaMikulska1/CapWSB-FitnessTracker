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
    private TrainingServiceImpl trainingService;

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

        User user1 = new User("Jan", "Kowalski", LocalDate.of(1990, 5, 15), "jan.kowalski@gmail.com");
        User user2 = new User("Anna", "Nowak", LocalDate.of(1985, 9, 20), "anna.nowak@gmail.com");

        Date startTime = new Date();

        Date endTime1 = new Date(startTime.getTime() + 10000);
        Date endTime2 = new Date(startTime.getTime() + 20000);
        Date endTime3 = new Date(startTime.getTime() + 30000);
        Date endTime4 = new Date(startTime.getTime() + 50000);

        training1 = new Training(user1, startTime, endTime1, ActivityType.RUNNING, 5.0, 10.0);
        training2 = new Training(user1, startTime, endTime2, ActivityType.CYCLING, 45.0, 20.0);
        training3 = new Training(user2, startTime, endTime3, ActivityType.WALKING, 3.0, 5.0);
        training4 = new Training(user1, startTime, endTime4, ActivityType.RUNNING, 15.0, 8.0);

    }

    @Test
    void testCreateTraining() {

        when(trainingRepository.save(training1)).thenReturn(training1);

        Training createdTraining = trainingService.createTraining(training1);

        assertEquals(training1, createdTraining);

    }

    @Test
    void findAllTrainings() {

        List<Training> trainings = Arrays.asList(training1, training2, training3);

        when(trainingRepository.findAll()).thenReturn(trainings);

        List<Training> allTrainings = trainingProvider.getAllTrainings();

        assertEquals(trainings, allTrainings);

    }

    @Test
    void findAllTrainingsByUserId() {

        Long userId = 1L;

        when(trainingRepository.findByUserId(userId)).thenReturn(Arrays.asList(training1, training2, training4));

        Iterable<Training> user1Trainings = trainingProvider.getTrainingsForUser(userId);

        assertEquals(3, ((List<Training>) user1Trainings).size());
        assertTrue(((List<Training>) user1Trainings).contains(training1));
        assertTrue(((List<Training>) user1Trainings).contains(training2));
        assertTrue(((List<Training>) user1Trainings).contains(training4));

    }

    @Test
    void findAllCompletedTrainingsAfterDate() {

        Date date = new Date();

        when(trainingRepository.findByEndTimeAfter(date)).thenReturn(Arrays.asList(training1, training2, training4));
        Iterable<Training> user1Trainings = trainingProvider.getFinishedTrainingsAfterTime(date);

        assertEquals(3, ((List<Training>) user1Trainings).size());
        assertTrue(((List<Training>) user1Trainings).contains(training1));
        assertTrue(((List<Training>) user1Trainings).contains(training2));
        assertTrue(((List<Training>) user1Trainings).contains(training4));

    }

    @Test
    void findAllTrainingsByActivityType() {

        ActivityType activityType = ActivityType.RUNNING;
        when(trainingRepository.findByActivityType(activityType)).thenReturn(Arrays.asList(training1, training4));

        List<Training> trainings = trainingProvider.getTrainingsByActivityType(activityType);

        assertEquals(2, trainings.size());
        assertTrue(trainings.contains(training1));
        assertTrue(trainings.contains(training4));
    }
}
