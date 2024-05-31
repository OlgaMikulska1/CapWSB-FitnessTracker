package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
//import lombok.RequiredArgsConstructor;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/trainings")
//@RequiredArgsConstructor
public class TrainingController {

    private final TrainingProvider trainingProvider;
    private final TrainingServiceImpl trainingService;

    private final UserService userService;


    public TrainingController(TrainingProvider trainingProvider, TrainingServiceImpl trainingService, UserService userService) {
        this.trainingProvider = trainingProvider;
        this.trainingService = trainingService;
        this.userService = userService;
    }

//    @PostMapping
//    public ResponseEntity<Training> createTraining(@RequestBody Training training) {
////        User user = training.setUser(user);
////        Training createdTraining = trainingProvider.createTraining(training);
////        return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
//        // Pobierz użytkownika na podstawie ID z treningu
//        Optional<User> userId = userService.getUserId(); // Załóżmy, że masz metodę getUserId() w klasie Training
//        Optional<User> user = userService.getUser(userId); // Przykładowe wywołanie serwisu userService do pobrania użytkownika
//
//        if (user == null) {
//            // Jeśli użytkownik o danym ID nie został znaleziony, zwróć odpowiedź 404 Not Found
//            return ResponseEntity.notFound().build();
//        }
//
//        // Przypisz użytkownika do treningu
//        training.setUser(user);
//
//        // Zapisz trening z przypisanym użytkownikiem
//        Training createdTraining = trainingProvider.createTraining(training);
//
//        // Zwróć odpowiedź 201 Created z zapisanym treningiem
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
//    }

//    @PostMapping
//    public ResponseEntity<Training> createTraining(@RequestBody Training training) {
//        Training createdTraining = trainingProvider.createTraining(training);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
//    }

    @PostMapping
    public ResponseEntity<Training> createTraining(@RequestBody TrainingRequest trainingRequest) {
        User user = userService.getUserById(trainingRequest.getUserId());
        Training training = new Training(
                user,
                trainingRequest.getStartTime(),
                trainingRequest.getEndTime(),
                trainingRequest.getActivityType(),
                trainingRequest.getDistance(),
                trainingRequest.getAverageSpeed()
        );
        Training createdTraining = trainingProvider.createTraining(training);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
    }


    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> trainings = trainingProvider.getAllTrainings();
        return ResponseEntity.ok(trainings);
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Training>> getTrainings(@PathVariable Long Id) {
////        Training training = trainingProvider.getTraining(Id)
////                .orElseThrow(() -> new TrainingNotFoundException(userId));
////        return ResponseEntity.ok(training);
//        return trainingService.getTrainingsForUser(userId);
//    }

    @GetMapping("/{userId}")
    public List<Training> getTrainings(@PathVariable Long userId) {
        return trainingService.getTrainingsForUser(userId);
    }

    @GetMapping("/finished/{afterTime}")
    public ResponseEntity<List<Training>> getFinishedTrainingsAfterTime(@PathVariable String afterTime) throws ParseException {
        List<Training> trainings = trainingService.getFinishedTrainingsAfterTime(new SimpleDateFormat("yyyy-MM-dd").parse(afterTime));
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/activityType")
    public ResponseEntity<List<Training>> getTrainingsByActivityType(@RequestParam ActivityType activityType) {
        List<Training> trainings = trainingService.getTrainingsByActivityType(activityType);
        return ResponseEntity.ok(trainings);
    }


}
