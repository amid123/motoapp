/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.positioning;

import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.domain.SystemResponse;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public interface GlobalListManagerService {

    SystemResponse insertPositionToList(UserPosition position);
    
}
