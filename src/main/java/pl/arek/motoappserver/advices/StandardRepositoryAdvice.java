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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Aspect
public class StandardRepositoryAdvice {

    private static final Logger logger = Logger.getLogger(StandardRepositoryAdvice.class.getName());

    @Autowired
    SessionFactory sessionFactory;

  @Around("execution(* pl.arek.motoappserver.domain.repositories.*.*(..))")

    public Object aroundRepoOperations(ProceedingJoinPoint pjp) throws Throwable {

        /**
         * first logging invocation of repository method
         */
        String methodName = pjp.getSignature().getName();
        logger.log(Level.SEVERE, "Calling method in UserRepository!!! method name: " + methodName);

        /**
         * next step is to open session or get current if exist
         */
        Session session;
        logger.log(Level.SEVERE, "session comming up...");
        try {
            session = this.sessionFactory.getCurrentSession();
            logger.log(Level.SEVERE, "current session retrioved succesful retrived!");
        } catch (HibernateException ex) {
            logger.log(Level.SEVERE, "There is no current session! creating new one " + ex.getMessage());
            session = this.sessionFactory.openSession();
        }

        /**
         * and here we need to open transaction if it success just try to do
         * something in our repository.
         */
        try {

            logger.log(Level.SEVERE, "Transaction beginign...");
            session.getTransaction().begin();
            logger.log(Level.SEVERE, "Transaction has begin success!");

            logger.log(Level.SEVERE, "Its looks like all operations was successd");
        } catch (HibernateException ex) {
            logger.log(Level.SEVERE, "There is a problem with opening transaction! " + ex.getMessage());
        }

        Object retVal = pjp.proceed();

        
        try {

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "There was some error in repository operations all operations will be roledback" + ex.getMessage());
            session.getTransaction().rollback();
        } finally {

            try {
                logger.log(Level.SEVERE, "commiting transaction...");
                session.getTransaction().commit();
                logger.log(Level.SEVERE, "Transaction has commited successdful");
            } catch (HibernateException ex) {
                logger.log(Level.SEVERE, "There was some error in transaction commiting: " + ex.getMessage());
            }

            try {
                session.close();
            } catch (HibernateException ex) {
                logger.log(Level.SEVERE, "There is a problem with session cloasing, maby its allready closed? " + ex.getMessage());
            }
        }

        return retVal;
    }
}
