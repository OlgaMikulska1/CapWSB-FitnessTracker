package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
//import lombok.RequiredArgsConstructor;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/trainings")
public class TrainingController {

    @Autowired
    private final TrainingProvider trainingProvider;
    @Autowired
    private final TrainingServiceImpl trainingService;
   @Autowired
    private final TrainingRepository trainingRepository;

   @Autowired
    private final UserService userService;


    public TrainingController(TrainingProvider trainingProvider, TrainingServiceImpl trainingService, TrainingRepository trainingRepository, UserService userService) {
        this.trainingProvider = trainingProvider;
        this.trainingService = trainingService;
        this.trainingRepository = trainingRepository;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Training> createTraining(@RequestBody TrainingRequest trainingRequest) {
        User user = userService.getUserById(trainingRequest.getUserId());
        Training training = buildTraining(trainingRequest, user);
        Training createdTraining = trainingProvider.createTraining(training);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
    }

    private Training buildTraining(TrainingRequest trainingRequest, User user) {
        return new Training(
                user,
                trainingRequest.getStartTime(),
                trainingRequest.getEndTime(),
                trainingRequest.getActivityType(),
                trainingRequest.getDistance(),
                trainingRequest.getAverageSpeed()
        );
    }



    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> trainings = trainingProvider.getAllTrainings();
        return ResponseEntity.ok(trainings);
    }

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


    @PutMapping("/{trainingId}")
    public ResponseEntity<Training> updateTraining(@PathVariable Long trainingId, @RequestBody Training updatedTraining) {
        Optional<Training> optionalTraining = trainingRepository.findById(trainingId);

        if (optionalTraining.isPresent()) {
            updatedTraining.setUser(optionalTraining.get().getUser());
            updatedTraining.setTrainingId(trainingId);
            return ResponseEntity.ok(trainingRepository.save(updatedTraining));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
