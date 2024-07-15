package com.rocketseat.planner.activity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.rocketseat.planner.trip.Trip;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activities") //Faz referência a tabela activities
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    //Aqui declaramos os campos, sendo eles da tabela criada
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //Id gerado de forma automática
    private UUID id;

    @Column(name = "occurs_at", nullable = false) //Aqui é o nome exato que está na tabela do banco de dados
    private LocalDateTime occursAt; //Aqui é forma como ele será utilizado na aplicação

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Activity(String title, String occursAt, Trip trip){
        this.title = title;
        this.occursAt = LocalDateTime.parse(occursAt, DateTimeFormatter.ISO_DATE_TIME);
        this.trip = trip;
    }
}
