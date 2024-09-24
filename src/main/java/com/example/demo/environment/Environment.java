package com.example.demo.environment;

import com.example.demo.round.Round;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Environment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long environmentId;

    private String fightingPlace;
    private String weather;

    @OneToMany(mappedBy = "environment")
    private List<Round> rounds;


}
