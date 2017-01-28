/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.account.impl;

import pl.arek.motoappserver.configuration.security.LogedInChecker;
import pl.arek.motoappserver.domain.repositories.UserRepository;
import pl.arek.motoappserver.domain.entities.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import pl.arek.motoappserver.services.account.AccountService;
import pl.arek.motoappserver.services.account.AccountActivatorService;
import pl.arek.motoappserver.domain.repositories.UserProfileRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.arek.motoappserver.services.systemresponse.ResponseCode;
import pl.arek.motoappserver.domain.SystemResponse;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class AccountServiceImpl implements AccountService {

    @Autowired
    SystemResponseFactory responseFactory;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository profileRepository;

    @Autowired
    AccountActivatorService accountActivator;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final LogedInChecker loggedInChecker;

    @Autowired
    public AccountServiceImpl(LogedInChecker loggedInChecker) {
        this.loggedInChecker = loggedInChecker;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = this.userRepository.getUser(username);
        user.setUserProfile(null);
        return user;
    }

    @Override
    public List<String> getPermissions(String username) {
        return new ArrayList<>();
    }

    @Override
    public User getCurrentUser() {
        return loggedInChecker.getLoggedUser();
    }

    @Override
    public boolean isCurrentUserLoggedIn() {
        return loggedInChecker.getLoggedUser() != null;
    }

    @Override
    public SystemResponse registerNewAccount(User user) {

        if (isUserDoesNotExist(user.getLogin())) {
            if (isEmailAvilable(user)) {

                try {
                    this.bindActivationHash(user);
                } catch (NoSuchAlgorithmException ex) {
                    return response(ResponseCode.INTERNAL_ERROR);
                }

                buildUser(user);
                this.userRepository.addNewUser(user);
                startActivation(user);

                return response(ResponseCode.SUCCESS);

            } else {
                return response(ResponseCode.EMAIL_ALREADY_EXIST);
            }
        } else {
            return response(ResponseCode.ACCOUNT_ALREADY_EXIST);
        }
    }

    @Override
    public boolean isUserRegistered(String username) {

        if (this.userRepository.getUser(username) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SystemResponse dropUser(User user) {
        if (this.userRepository.deleteUser(user)) {
            return response(ResponseCode.SUCCESS);
        } else {
            return response(ResponseCode.NOT_SUCCESS);
        }
    }

    @Override
    public SystemResponse updateUser(String username, User user) {
        this.encodeUserPassword(user);
        if (this.userRepository.updateUser(user)) {
            return response(ResponseCode.SUCCESS);
        } else {
            return response(ResponseCode.NOT_SUCCESS);
        }
    }

    @Override
    public SystemResponse changeUserPassword(User user, String oldPassword, String newPassword) {

        String hashedPassword = this.passwordEncoder.encode(newPassword);

        if (user.getPassword().equals(oldPassword)) {
            this.userRepository.updateUserPassword(user, hashedPassword);
            return response(ResponseCode.SUCCESS);

        } else {
            return response(ResponseCode.WRONG_USERNAME_OR_PASSWORD);
        }
    }

    @Override
    public SystemResponse changeUserEmail(User user, String newEmail) {

        if (!this.isGivenEmailExist(newEmail)) {
            this.userRepository.updateUserEmail(user, newEmail);
            return response(ResponseCode.SUCCESS);

        } else {
            return response(ResponseCode.EMAIL_ALREADY_EXIST);
        }
    }

    private void buildUser(User user) {
        user.setActivated(false);
        user.setRegisterDate(new Date());
        this.encodeUserPassword(user);
    }

    private SystemResponse response(ResponseCode code) {
        return this.responseFactory.createSystemResponse(code);
    }

    private void startActivation(User user) {
        this.accountActivator.sendActiovationEmail(user);
    }

    private void encodeUserPassword(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    }

    private void bindActivationHash(User user) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String login = user.getLogin();
        UUID hash = buildHash(md, login);
        user.setActivationHash(hash.toString());
    }

    private UUID buildHash(MessageDigest md, String login) {
        UUID hash = UUID.nameUUIDFromBytes(md.digest(login.getBytes()));
        return hash;
    }

    private boolean isEmailAvilable(User user) {
        return !this.isGivenEmailExist(user.getEmail());
    }

    private boolean isUserDoesNotExist(String login) {
        return !this.isUserRegistered(login);
    }

    private boolean isGivenEmailExist(String email) {

        return this.userRepository.findByEmail(email) != null;
    }

}
