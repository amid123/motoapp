/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.positioning.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.arek.motoappserver.configuration.app.OtherComponentsConfiguration;
import pl.arek.motoappserver.configuration.app.ServicesConfiguration;
import pl.arek.motoappserver.configuration.app.TestConfiguration;
import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.domain.entities.User;
import pl.arek.motoappserver.services.positioning.GlobalMemberList;
import pl.arek.motoappserver.services.positioning.PersonalizedListBuilder;
import pl.arek.motoappserver.services.positioning.PersonalizedMemberList;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    TestConfiguration.class,
    //RestConfiguration.class,
    OtherComponentsConfiguration.class,
    ServicesConfiguration.class
})

@ActiveProfiles(profiles = {"test", "main"})
public class PersonalizedListBuilderImplTest {

    @Autowired
    private PersonalizedListBuilder listBuilder;
    @Autowired
    private GlobalMemberList globalMemberList;

    public PersonalizedListBuilderImplTest() {

    }

    private UserPosition rondoMogilskie;

    private UserPosition stella;

    private UserPosition agh;

    private UserPosition m1;

    @Before
    public void setUp() {

        User u1 = new User("Arek", "tajne haslo arka", "arek@arek.pl");
        User u2 = new User("Krzys", "tajne haslo krzysia", "weg@geg.pl");
        User u3 = new User("Andrzej", "tajne haslo andrzeja", "efggeg@weg.pl");
        User u4 = new User("MArek", "tajne haslo marka", "fdsdf@sdf.pl");

        /**
         * this geographical coordinates was taken form goole maps. we just
         * checking here if our service work properly.
         *
         *
         * distances from rondo mogilskie to:
         *
         * m1: 2864 agh: 2623m stella: 3847m
         *
         *
         * So, for radius 4km we should get 3 result on list, for radius 3km we
         * got 2 result and for radius 2700 just one result. All other where
         * radius is less then 2623 there should be zero results on our
         * personalized list
         *
         */
        // here user arek in place rondo mogilskie in krakow
        rondoMogilskie = new UserPosition(u1, 50.065972, 19.959657);
        // here we have user Krzys and hi is on stella sawickiego
        stella = new UserPosition(u2, 50.087620, 20.001714);
        // here we have user andrzej and his on agh
        agh = new UserPosition(u3, 50.065118, 19.922921);
        // and at last we got marek his in m1 trade center
        m1 = new UserPosition(u4, 50.063906, 19.999654);

        globalMemberList.getUsersPositions().add(m1);
        globalMemberList.getUsersPositions().add(agh);
        globalMemberList.getUsersPositions().add(stella);
        globalMemberList.getUsersPositions().add(rondoMogilskie);

    }

    @After
    public void tearDown() {
    }

    //* Test of buildList method, of class PersonalizedListBuilderImpl.
    @Test
    public void testBuildList() {

        PersonalizedMemberList list;

        /**
         * for radius 3km we should have only one result on list
         */
        list = this.listBuilder.buildList(4000, rondoMogilskie);
        Assert.assertEquals(3, list.getUsersPositions().size());

        /**
         * now we should have about three results on list
         */
        list = this.listBuilder.buildList(3000, rondoMogilskie);
        Assert.assertEquals(2, list.getUsersPositions().size());

        /**
         * 1 result for 2700
         */
        list = this.listBuilder.buildList(2700, rondoMogilskie);
        Assert.assertEquals(1, list.getUsersPositions().size());
        
        /**
         * and 0 results fo less then 2623
         */
        list = this.listBuilder.buildList(2600, rondoMogilskie);
        Assert.assertEquals(0, list.getUsersPositions().size());
    }
}
