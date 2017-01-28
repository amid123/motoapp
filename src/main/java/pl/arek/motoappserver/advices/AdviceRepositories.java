/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.advices;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Aspect
public class AdviceRepositories {

    private static final Logger logger = Logger.getLogger(AdviceRepositories.class.getName());

    @Around("execution(* pl.arek.motoappserver.domain.repositories.impl.UserRepositoryImpl.*(..))")
    public Object logAroundRepoOperations(ProceedingJoinPoint pjp) throws Throwable {
        
       String methodName = pjp.getSignature().getName();

       logger.log(Level.SEVERE,"Calling method in UserRepository!!! method name: " + methodName);
        
       
        Object retVal = pjp.proceed();
        
        return retVal;
    }

}
