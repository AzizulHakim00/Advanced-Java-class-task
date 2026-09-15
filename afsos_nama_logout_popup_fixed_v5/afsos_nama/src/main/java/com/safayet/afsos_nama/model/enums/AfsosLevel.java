package com.safayet.afsos_nama.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AfsosLevel {

    LOW("Choto Afsos", 1),
    MEDIUM("Medium Afsos", 2),
    HIGH("Boro Afsos", 3),
    EXTREME("Jibon Shesh Afsos", 5);

    private final String label;
    private final int score;
}