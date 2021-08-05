package com.divine.dy.lib_http.retrofit2;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Divine
 * CreateDate: 2020/11/2
 * Describe:
 */
public class GeneralException extends RuntimeException {
    private int Code;
    private String ErrorMessage;

    public GeneralException(int Code, String ErrorMessage) {
        this.Code = Code;
        try {
            JSONObject errorObj = new JSONObject(ErrorMessage);
            if (errorObj.has("msg")) {
                this.ErrorMessage = errorObj.optString("msg");
            } else if (errorObj.has("message")) {
                this.ErrorMessage = errorObj.optString("message");
            } else {
                this.ErrorMessage = ErrorMessage;
            }
        } catch (JSONException e) {
            this.ErrorMessage = ErrorMessage;
        }
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
