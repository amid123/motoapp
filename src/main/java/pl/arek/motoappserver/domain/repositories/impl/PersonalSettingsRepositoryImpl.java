/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.repositories.impl;

import pl.arek.motoappserver.domain.repositories.PersonalSettingsRepository;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.domain.entities.PersonalSettings;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class PersonalSettingsRepositoryImpl implements PersonalSettingsRepository {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public PersonalSettings getByUserName(String userName) {

        Session session;
        try {
            session = this.sessionFactory.getCurrentSession();
        } catch (HibernateException ex) {
            session = this.sessionFactory.openSession();
        }

        Query query = session.getNamedQuery("getSettingsByUserName");
        query.setParameter("login", userName);
        query.setMaxResults(1);

        Transaction tx = session.getTransaction();

        try {
            tx.begin();

            PersonalSettings personalSettings = (PersonalSettings) query.uniqueResult();
            tx.commit();

            return personalSettings;

        } catch (Exception ex) {
            tx.rollback();

        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public boolean updateMembersCount(int membersCount, String userName) {

        Session session;
        try {
            session = this.sessionFactory.getCurrentSession();
        } catch (HibernateException ex) {
            session = this.sessionFactory.openSession();
        }

        Query query = session.getNamedQuery("updateMembersCount");
        query.setParameter("login", userName);
        query.setParameter("members_count", membersCount);

        Transaction tx = session.getTransaction();

        try {
            tx.begin();

            int result = query.executeUpdate();

            tx.commit();

            return result > 0;

        } catch (Exception ex) {
            tx.rollback();

        } finally {
            session.close();
        }
        return false;
    }

}
