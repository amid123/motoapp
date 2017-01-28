/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.rest.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import pl.arek.motoappserver.domain.entities.User;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;
import pl.arek.motoappserver.services.account.AccountService;
import pl.arek.motoappserver.services.account.AccountActivatorService;
import java.io.IOException;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.rest.dtos.json.EmailDto;
import pl.arek.motoappserver.rest.dtos.json.PasswordDto;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@RestController
@Scope("request")
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController implements Serializable {

    @Autowired
    AccountService userService;

    @Autowired
    AccountActivatorService accountActivatorService;

    @Autowired
    Gson gson;

    @Autowired
    Environment enviroment;

    /**
     * app/account : GET - just get current logeg user here
     *
     * @return
     */
    @RequestMapping(method = GET)
    ResponseEntity<User> getCurrentUser() {

        User u = userService.getCurrentUser();
         u.setPassword(null); // nie zwracamy hasla
         u.setActivationHash(null);
        return ok(u);
    }

    /**
     * app/account : DELETE - delete user account if user with want to delete is
     * currently loged.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<SystemResponse> deleteUser(HttpServletRequest request) {
        SystemResponse systemResponse = userService.dropUser(userService.getCurrentUser());

        request.getSession().invalidate();
   
        this.logout();
        return ok(systemResponse);
    }

    private void logout() throws IllegalArgumentException {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }
    
    /**
     * app/account/register : POST - register new account
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SystemResponse> register(HttpServletRequest request) throws IOException {
        User user = new User();

        user = gson.fromJson(request.getReader().readLine(), User.class);

        SystemResponse status = this.userService.registerNewAccount(user);
        return ok(status);

    }

    /**
     * app/account/activation/{uuid} - uuid is kind of activation hash this
     * resource is a entry point for account activation procves;
     *
     * @param request
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/activation/{uuid}", method = GET)
    ResponseEntity<SystemResponse> activate(HttpServletRequest request, @PathVariable("uuid") String uuid) {

        return new ResponseEntity<>(this.accountActivatorService.activateAccount(uuid), OK);

    }

    /**
     * app/account/password : PUT
     *
     * @param passwordJson
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/password")
    public ResponseEntity<SystemResponse> updateUserPassword(@RequestBody String passwordJson) throws IOException {

        PasswordDto passwordDto = getPasswordDtoFromRequest(passwordJson);

        User currentLoggedUser = this.userService.getCurrentUser();
        SystemResponse systemResponse = this.userService.changeUserPassword(currentLoggedUser, passwordDto.getOldPassword(), passwordDto.getNewPassword());

        return new ResponseEntity<>(systemResponse, HttpStatus.OK);
    }

    private PasswordDto getPasswordDtoFromRequest(String passwordJson) throws JsonSyntaxException {
        PasswordDto passwordDto = gson.fromJson(passwordJson, PasswordDto.class);
        return passwordDto;
    }

    /**
     * app/account/email : PUT
     *
     * @param emailJson
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/email")
    public ResponseEntity<SystemResponse> updateUserEmail(@RequestBody(required = false) String emailJson) {

        EmailDto emailDto = getEmailDtoFromRequest(emailJson);

        User currentLoggedUser = this.userService.getCurrentUser();
        SystemResponse systemResponse = this.userService.changeUserEmail(currentLoggedUser, emailDto.getEmail());

        return new ResponseEntity<>(systemResponse, HttpStatus.OK);
    }

    private EmailDto getEmailDtoFromRequest(String emailJson) throws JsonSyntaxException {
        EmailDto emailDto = gson.fromJson(emailJson, EmailDto.class);
        return emailDto;
    }
}
