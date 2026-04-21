package com.example.scheduler;

import com.example.entity.Task;
import com.example.repository.TaskRepository;
import com.example.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskReminderScheduler {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private EmailService emailService;

 // ⏰ Runs every day at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendDeadlineReminders() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Task> tasks = taskRepo.findByDeadline(tomorrow);

        for (Task task : tasks) {

            String email = task.getAssignedTo().getEmail();

            System.out.println("Sending reminder to: " + email);

            emailService.sendEmail(
                email,
                "⏰ Task Deadline Reminder",
                "Reminder: Your task \"" + task.getTitle() + "\" is due tomorrow!"
            );
        }
    }
}