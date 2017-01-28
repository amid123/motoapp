/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.repositories;

import pl.arek.motoappserver.domain.entities.User;
import java.util.List;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public interface UserRepository {

    public boolean addNewUser(User u);

    public boolean deleteUser(User u);

    public boolean updateUser(User u);

    public User getUser(String login);

    public User findByEmail(String email);

    public User findByUuid(String uuid);

    // znajduje userow starszych niz ilosc dni i statusie aktywnosci
    public List<User> findUsersOlderThenAndActivated(int days, boolean active);

    public List<String> getAllUsersNames();

    public boolean updateUserPassword(User user, String password);

    public boolean updateUserEmail(User user, String email);

}
