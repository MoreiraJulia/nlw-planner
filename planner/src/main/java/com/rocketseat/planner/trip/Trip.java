package com.rocketseat.planner.trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // Indica para o JPA que isso é uma entidade
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    
    @Id // Indica que é a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.AUTO) //Indica que é gerada automaticamente utilizando o generationType
    private UUID id; //Pegamos o id da tabela

    @Column(nullable = false) //Indica que é uma coluna na tabela e que não é nulo
    private String destination; //Pega o destino da viagem

    @Column(name = "starts_at", nullable = false) //Indica que é uma coluna na tabela e que seu nome é starts_at
    private LocalDateTime startsAt; //Pega a data de inicio

    @Column(name = "ends_at", nullable = false)//Indica que é uma coluna na tabela e que seu nome é ends_at
    private LocalDateTime endsAt;//Pega a data final

    @Column(name = "is_confirmed", nullable = false)//Indica que é uma coluna na tabela e que seu nome é is_confirmed
    private Boolean isConfirmed;//Pega se está confirmado ou não

    @Column(name = "owner_name", nullable = false)//Indica que é uma coluna na tabela e que seu nome é owner_name
    private String ownerName;//Pega o nome do dono

    @Column(name = "owner_email", nullable = false)//Indica que é uma coluna na tabela e que seu nome é owner_email
    private String ownerEmail;//Pega o e-mail do dono

    public Trip(TripRequestPayload data){
        this.destination = data.destination();
        this.isConfirmed = false;
        this.ownerEmail = data.owner_email();
        this.ownerName = data.owner_name();
        this.startsAt = LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
