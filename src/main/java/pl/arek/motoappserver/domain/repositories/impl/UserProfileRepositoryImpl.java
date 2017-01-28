/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.repositories.impl;

import pl.arek.motoappserver.domain.entities.User;
import pl.arek.motoappserver.domain.entities.UserProfile;
import pl.arek.motoappserver.domain.repositories.UserProfileRepository;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

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

        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }

        Query q = session.getNamedQuery("getProfileByUserName");
        q.setParameter("login", userName);
        q.setMaxResults(1);

        Transaction tx = session.getTransaction();

        try {
            tx.begin();

            UserProfile userProfile = (UserProfile) q.uniqueResult();

            tx.commit();

            return userProfile;

        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;

        } finally {
            session.close();
        }
    }

    @Override
    public boolean addNewProfileProfile(UserProfile profile) {
        Session session;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }

        Transaction tx = session.getTransaction();

        try {
            tx.begin();

            session.save(profile);

            tx.commit();
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean isUserProfileExist(String login) {
        Session session;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }

        Transaction tx = session.getTransaction();

        try {
            tx.begin();

            Query q = session.getNamedQuery("getProfileByUserName");
            q.setParameter("login", login);
            q.setMaxResults(1);

            UserProfile profile = (UserProfile) q.uniqueResult();
            tx.commit();

            if (profile != null) {
                return true;
            } else {
                return false;
            }

        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

//    @Override
//    public boolean deleteUserProfile(User user) {
//
//        Session session;
//
//        try {
//            session = sessionFactory.getCurrentSession();
//        } catch (HibernateException e) {
//            session = sessionFactory.openSession();
//        }
//
//        Transaction tx = session.getTransaction();
//
//        try {
//
//            tx.begin();
//
//            // aby usunac kaskadowo z relacjami musimy pobrać całą encje z relacjami do sesji i dopiero usuwac
//            UserProfile profile = this.getProfileByUserName(user.getLogin());
//            if (profile != null) {
//                session.delete(profile);
//            }
//
//            tx.commit();
//
//            return true;
//        } catch (HibernateException ex) {
//            ex.printStackTrace();
//            return false;
//        } finally {
//            session.close();
//        }
//    }
}
