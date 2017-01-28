/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.advices;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Aspect
public class AdviceAccountCleaner {

    private static final Logger logger = Logger.getLogger(AdviceAccountCleaner.class.getName());

    @Before("execution(* pl.arek.motoappserver.services.account.impl.AccountCleanerServiceImpl.clean(..))")
    public void logBeforeAccountCleaning(JoinPoint joinPoint) {
        logger.log(Level.SEVERE, "Cleaning expired accounts!");
    }

}
