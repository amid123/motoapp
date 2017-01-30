/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.repositories.impl;

import java.util.List;
import org.hibernate.Hibernate;
import pl.arek.motoappserver.domain.entities.UserProfile;
import pl.arek.motoappserver.domain.repositories.UserProfileRepository;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.domain.entities.Contact;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class UserProfileRepositoryImpl implements UserProfileRepository {

    SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UserProfile getProfileByUserName(String userName) {
        Session session;

//        try {
            session = sessionFactory.getCurrentSession();
//        } catch (HibernateException e) {
//            session = sessionFactory.openSession();
//        }

        Query q = session.getNamedQuery("getProfileByUserName");
        q.setParameter("login", userName);
        q.setMaxResults(1);

//        Transaction tx = session.getTransaction();
//
//        try {
//            tx.begin();

            UserProfile userProfile = (UserProfile) q.uniqueResult();

            List<Contact> lazyCollection = userProfile.getPersonalDetails().getContacts();

            if (!Hibernate.isInitialized(lazyCollection)) {
                Hibernate.initialize(lazyCollection);
            }

//            tx.commit();

          //  session.evict(userProfile);
            return userProfile;

//        } catch (HibernateException ex) {
//            ex.printStackTrace();
//            return null;
//
//        } finally {
//            session.close();
//        }
    }

    @Override
    public boolean addNewProfileProfile(UserProfile profile) {
        Session session;

//        try {
            session = sessionFactory.getCurrentSession();
//        } catch (HibernateException e) {
//            session = sessionFactory.openSession();
//        }
//
//        Transaction tx = session.getTransaction();
//
//        try {
//            tx.begin();

            session.save(profile);

//            tx.commit();
            return true;
//        } catch (HibernateException ex) {
//            ex.printStackTrace();
//            return false;
//        } finally {
//            session.close();
//        }
    }

    @Override
    public boolean isUserProfileExist(String login) {
        Session session;

//        try {
            session = sessionFactory.getCurrentSession();
//        } catch (HibernateException e) {
//            session = sessionFactory.openSession();
//        }
//
//        Transaction tx = session.getTransaction();
//
//        try {
//            tx.begin();

            Query q = session.getNamedQuery("getProfileByUserName");
            q.setParameter("login", login);
            q.setMaxResults(1);

            UserProfile profile = (UserProfile) q.uniqueResult();
//            tx.commit();

            if (profile != null) {
                return true;
            } else {
                return false;
            }
//
//        } catch (HibernateException ex) {
//            ex.printStackTrace();
//            return false;
//        } finally {
//            session.close();
//        }
    }
    
    
}
