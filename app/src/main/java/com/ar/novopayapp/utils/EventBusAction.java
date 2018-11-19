package com.ar.novopayapp.utils;

public interface EventBusAction {
    String SYNC_SUCCESS = "sync_success";
    String PERMISSION_GRANTED = "permission_granted";
    String PERMISSION_DENIED = "permission_denied";
    String FORCE_PERMISSION = "force_permission";
}
