/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.domain.repositories;

import pl.arek.motoappserver.domain.entities.PersonalSettings;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public interface PersonalSettingsRepository {

    PersonalSettings getByUserName(String userName);

    boolean updateMembersCount(int membersCount, String userName);
    
}
