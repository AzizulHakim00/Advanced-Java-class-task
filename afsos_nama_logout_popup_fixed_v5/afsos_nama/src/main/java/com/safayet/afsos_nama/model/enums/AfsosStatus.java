package com.safayet.afsos_nama.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AfsosStatus {
    NEW("New"),
    STILL_SUFFERING("Still Suffering"),
    LESSON_LEARNED("Lesson Learned"),
    FIXED("Fixed"),
    REPEATED_AGAIN("Repeated Again");

    private final String label;
}
