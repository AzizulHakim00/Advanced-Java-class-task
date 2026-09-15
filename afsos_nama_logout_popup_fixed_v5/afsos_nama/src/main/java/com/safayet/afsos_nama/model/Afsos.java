package com.safayet.afsos_nama.model;

import com.safayet.afsos_nama.model.enums.AfsosCategory;
import com.safayet.afsos_nama.model.enums.AfsosLevel;
import com.safayet.afsos_nama.model.enums.AfsosStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "afsos")
public class Afsos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AfsosCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AfsosLevel level;

    @Column(nullable = false)
    private LocalDate regretDate;

    @Column(length = 100)
    private String relatedCourse;

    @Column(length = 50)
    private String semester;

    @Column(length = 500)
    private String consequence;

    @Column(length = 500)
    private String lessonLearned;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AfsosStatus status = AfsosStatus.NEW;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private boolean sharedAnonymously;

    @Column(nullable = false)
    private boolean approvedForFeed;

    @Column(nullable = false)
    private int sameBhaiCount;

    @Column(nullable = false)
    private int ripCgpaCount;

    @Column(nullable = false)
    private int nextSemesterCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
