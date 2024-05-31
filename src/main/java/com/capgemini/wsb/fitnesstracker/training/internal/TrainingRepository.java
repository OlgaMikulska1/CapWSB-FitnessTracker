package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTrainingId(Long trainingId);

    List<Training> findByUserId(Long userId);

    Optional<Training> findById(Long trainingId);

    List<Training> findByEndTimeAfter(Date date);

    List<Training> findByActivityType(ActivityType activityType);

}
