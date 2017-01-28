/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.services.systemresponse;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public enum ResponseCode {

    SUCCESS("response.msg.operation.success"),
    NOT_SUCCESS("response.msg.operation.failure"),
    WRONG_USERNAME_OR_PASSWORD("response.msg.operation.badcredentials"),
    ACCOUNT_ALREADY_EXIST("response.msg.operation.accountexist"),
    EMAIL_ALREADY_EXIST("response.msg.operation.emailexist"),
    INTERNAL_ERROR("response.msg.operation.internalerror"),
    ACCOUNT_ALREADY_ACTIVATED("response.msg.operation.alreadyactivated"),
    ACCOUNT_NOT_EXIST("response.msg.operation.accountnotexist"),
    ACCESS_DENIED("response.msg.operation.accessdenied");

    private final String message;

    ResponseCode(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
