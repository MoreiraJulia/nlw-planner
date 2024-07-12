package com.rocketseat.planner.participant;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity //Fala que é uma entidade
@Table(name = "participants") // Fala que o possui uma tabela com o nome participants
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    @Id // Indica que é a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.AUTO) //Indica que é gerada automaticamente utilizando o generationType
    private UUID id; //Pegamos o id da tabela
    
    @Column(name = "is_confirmed", nullable = false)//Indica que é uma coluna na tabela e que seu nome é is_confirmed
    private Boolean isConfirmed;//Pega se está confirmado ou não

    @Column(nullable = false)
    private String name;//Pega o nome 

    @Column(nullable = false)
    private String email;//Pega o e-mail 

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Participant(String email, Trip trip){
        this.email = email;
        this.trip = trip;
        this.isConfirmed = false;
        this.name = "";
    }
}
