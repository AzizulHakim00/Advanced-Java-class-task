package com.safayet.afsos_nama.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AfsosCategory {
    ATTENDANCE("Attendance"),
    ASSIGNMENT("Assignment"),
    EXAM("Exam"),
    PRESENTATION("Presentation"),
    GROUP_PROJECT("Group Project"),
    LATE_START("Late Start"),
    SLEEP("Sleep"),
    MONEY("Money"),
    COURSE_SELECTION("Course Selection"),
    TEACHER_ISSUE("Teacher Issue"),
    CAMPUS_LIFE("Campus Life"),
    OTHER("Other");

    private final String label;
}
