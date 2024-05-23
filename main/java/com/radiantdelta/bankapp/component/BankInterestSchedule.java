package com.radiantdelta.bankapp.component;

import com.radiantdelta.bankapp.entities.User;
import com.radiantdelta.bankapp.repositories.ScheduleRepository;
import com.radiantdelta.bankapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EnableScheduling
public class BankInterestSchedule {
    public final static float maxAddVal = 2.07f;
    public final static float percentGrowth = 1.05f;
    @Autowired
    ScheduleRepository scheduleRepository;


    @Scheduled(fixedDelay = 1, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void addPercent() {

            List<User> usersToAddPercent = scheduleRepository.findAllForAddInterest(maxAddVal);

            for (User u : usersToAddPercent) {
                u.setAmount(u.getAmount() * percentGrowth);
                scheduleRepository.saveAndFlush(u);
            }
        log.info("Если есть пользователи, которым можно начислить процент, проценты начислены ");
    }

}
