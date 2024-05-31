package com.capgemini.wsb.fitnesstracker.mail.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public EmailSenderImpl(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    public void send(List<Training> email) {
        String toAddress = "sandbox.smtp.mailtrap.io";  // Replace with actual recipient logic
        String subject = "Weekly Training Report";
        String content = generateReportContent(email);

        EmailDto newemail = new EmailDto(toAddress, subject, content);
        sendEmail(newemail);
    }

    private void sendEmail(EmailDto emailDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailDto.toAddress());
            helper.setSubject(emailDto.subject());
            helper.setText(emailDto.content(), true);
            helper.setFrom(mailProperties.getFrom());

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle exception
        }
    }

    private String generateReportContent(List<Training> trainings) {
        StringBuilder content = new StringBuilder("Weekly Training Report\n\n");
        for (Training training : trainings) {
            content.append("Training ID: ").append(training.getTrainingId()).append("\n")
                    .append("Start Time: ").append(training.getStartTime()).append("\n")
                    .append("End Time: ").append(training.getEndTime()).append("\n")
                    .append("Activity Type: ").append(training.getActivityType()).append("\n")
                    .append("Distance: ").append(training.getDistance()).append("\n")
                    .append("Average Speed: ").append(training.getAverageSpeed()).append("\n\n");
        }
        return content.toString();
    }
}
