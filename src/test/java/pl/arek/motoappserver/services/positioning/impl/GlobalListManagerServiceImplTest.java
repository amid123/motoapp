/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.positioning.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.arek.motoappserver.configuration.app.OtherComponentsConfiguration;
import pl.arek.motoappserver.configuration.app.RestConfiguration;
import pl.arek.motoappserver.configuration.app.ServicesConfiguration;
import pl.arek.motoappserver.configuration.app.TestConfiguration;
import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.services.positioning.GlobalListManagerService;
import pl.arek.motoappserver.services.positioning.GlobalMemberList;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(classes = {
    TestConfiguration.class,
    RestConfiguration.class,
    OtherComponentsConfiguration.class,
    ServicesConfiguration.class
})

@WebAppConfiguration
@ActiveProfiles(profiles = {"test", "main"})
public class GlobalListManagerServiceImplTest {

    @Autowired
    GlobalListManagerService globalListManagerService;

    @Autowired
    GlobalMemberList globalMemberList;

    private UserPosition position;

    @Before
    public void setUp() {

        position = new UserPosition();
        position.setLatitude(122.31);
        position.setLongitude(3252.31);

    }

    /**
     * Test of insertPositionToList method, of class GlobalListManagerService.
     */
    @Test
    public void testInsertPositionToList() {

        //checking whether global list is bigger then 0
        this.globalListManagerService.insertPositionToList(position);

        Assert.assertTrue(globalMemberList.getUsersPositions().size() > 0);
        Assert.assertTrue(globalMemberList.getUsersPositions().contains(position));
        this.globalListManagerService.insertPositionToList(position);
        Assert.assertTrue(globalMemberList.getUsersPositions().size() == 1);

    }

}
