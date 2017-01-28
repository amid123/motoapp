/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.repositories.impl;

import pl.arek.motoappserver.domain.repositories.UserRepository;
import pl.arek.motoappserver.domain.entities.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.arek.motoappserver.domain.entities.PersonalSettings;
import pl.arek.motoappserver.domain.entities.UserProfile;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Repository

public class UserRepositoryImpl implements UserRepository {
    
    org.slf4j.Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    
    private SessionFactory sessionFactory;
    
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public boolean addNewUser(User u) {
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        UserProfile profile = new UserProfile();
        try {
            
            tx.begin();
            session.save(u);
            
            profile.setUser(u);
            profile.setPersonalSettings(new PersonalSettings());
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

    /**
     *
     * @param user
     * @return
     */
    @Override
    public boolean deleteUser(User user) {
        
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx;
        tx = session.getTransaction();
        
        if (user != null) {
            try {
                
                tx.begin();
                
                user = session.get(User.class, user.getId());
                
                session.delete(user.getUserProfile());
                session.delete(user);
                tx.commit();
                
                return true;
            } catch (HibernateException ex) {
                ex.printStackTrace();
                return false;
            } finally {
                session.close();
            }
        } else {
            return false;
        }
    }

    /**
     *
     * Updatuje usera
     *
     * @param u
     * @return
     */
    @Override
    public boolean updateUser(User u) {
        
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        
        try {
            tx.begin();
            
            if (session.contains(u)) {
                session.merge(u);
            } else {
                session.update(u);
            }
            session.saveOrUpdate(u);
            
            tx.commit();
            
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
            
        } finally {
            session.close();
        }
    }

    /**
     * pobiera usera po loginie
     *
     * @param login
     * @return
     */
    @Override
    public User getUser(String login) {
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        
        try {
            tx.begin();
            
            Query query = session.getNamedQuery("findUserByLogin");
            query.setParameter("login", login);
            
            User u = (User) query.uniqueResult();
            tx.commit();
            
            return u;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
            
        } finally {
            session.close();
        }
    }

    /**
     * pobiera usera po emailu
     *
     * @param email
     * @return
     */
    @Override
    public User findByEmail(String email) {
        
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        
        try {
            tx.begin();
            
            Query query = session.getNamedQuery("findUserByEmail");
            query.setParameter("email", email);
            
            User u = (User) query.uniqueResult();
            tx.commit();
            
            return u;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
            
        } finally {
            session.close();
        }
    }

    /**
     * Pobiera userow z bazy i zwraca ich loginy w formie listy
     *
     * @return
     */
    @Override
    public List<String> getAllUsersNames() {
        List<String> unames = new ArrayList();
        
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        
        try {
            tx.begin();
            
            List<User> users = session.getNamedQuery("getAllUsers").list();
            
            if (!users.isEmpty()) {
                for (User u : users) {
                    unames.add(u.getLogin());
                }
            }
            tx.commit();
            
            return unames;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
            
        } finally {
            session.close();
        }
        
    }
    
    @Override
    public User findByUuid(String uuid) {
        
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        
        try {
            tx.begin();
            
            Query query = session.getNamedQuery("findUserByActivationHash");
            query.setParameter("activationHash_param", uuid);
            query.setMaxResults(1);
            
            User u = (User) query.uniqueResult();
            
            tx.commit();
            
            return u;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
            
        } finally {
            session.close();
        }
    }

    /**
     *
     * @param days accouts that are older then count of days in this param.
     * @param active activity status
     * @return accounts list
     */
    @Override
    public List<User> findUsersOlderThenAndActivated(int days, boolean active) {

        // tworzymy egzemplarz kalendarza
        Calendar calendar = Calendar.getInstance();
        // inicjujemy aktualna data
        calendar.setTime(new Date());
        // odejmujemy ilosc dni podanych w parametrze
        calendar.add(Calendar.DATE, days * (-1));
        // konwertujemy do date
        Date date = calendar.getTime();
        
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        
        try {
            tx.begin();
            
            Query query = session.getNamedQuery("findUserByActivationHashExpired");
            query.setParameter("register_date", date);
            query.setParameter("activated", active);
            
            List<User> users = query.list();
            
            tx.commit();
            
            return users;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
            
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean updateUserPassword(User user, String password) {
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        
        try {
            tx.begin();
            
            Query query = session.getNamedQuery("updateUserPasswordByLogin");
            query.setParameter("password", password);
            query.setParameter("login", user.getLogin());
            
            int modified = query.executeUpdate();
            
            tx.commit();
            
            return modified > 0;
        } catch (HibernateException ex) {
            
            ex.printStackTrace();
            return false;
            
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean updateUserEmail(User user, String email) {
        Session session;
        
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        
        Transaction tx = session.getTransaction();
        
        try {
            tx.begin();
            
            Query query = session.getNamedQuery("updateUserEmailByLogin");
            query.setParameter("email", email);
            query.setParameter("login", user.getLogin());
            
            int modified = query.executeUpdate();
            
            tx.commit();
            
            return modified > 0;
        } catch (HibernateException ex) {
            
            ex.printStackTrace();
            return false;
            
        } finally {
            session.close();
        }
    }
}
