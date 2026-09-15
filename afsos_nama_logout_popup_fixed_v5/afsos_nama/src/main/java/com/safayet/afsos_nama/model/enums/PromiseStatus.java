package com.safayet.afsos_nama.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PromiseStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    BROKEN("Broken");

    private final String label;

}
