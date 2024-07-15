package com.rocketseat.planner.trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketseat.planner.activity.ActivityData;
import com.rocketseat.planner.activity.ActivityRequestPayload;
import com.rocketseat.planner.activity.ActivityResponse;
import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.link.LinkData;
import com.rocketseat.planner.link.LinkRequestPayload;
import com.rocketseat.planner.link.LinkResponse;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.participant.ParticipantCreateResponse;
import com.rocketseat.planner.participant.ParticipantData;
import com.rocketseat.planner.participant.ParticipantRequestPayload;
import com.rocketseat.planner.participant.ParticipantService;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired 
    private ActivityService activityService;

    @Autowired 
    private LinkService linkService;

    @Autowired
    private TripRepository repository;

    @PostMapping //Enviar dados novos
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload); 

        this.repository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));

    }

    @GetMapping("/{id}")//Recuperar dados existentes de uma viagem
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){ //Estamos falando de pegar os dados a partir do id 
        Optional<Trip> trip = this.repository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") //Método para alterar dados da viagem
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload){//Estamos falando de pegar os dados a partir do id 
        Optional<Trip> trip = this.repository.findById(id); //Ele é opcional pode ou não existir
        if(trip.isPresent()){// Se essa trip existe
            Trip rawTrip = trip.get();// Declaramos um novo objeto do tipo Trip que pega a trip
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination((payload.destination()));

            this.repository.save(rawTrip); //Salvar alterações no banco de dados

            return ResponseEntity.ok(rawTrip); // Retorna as informações atualizadas
        }

        return ResponseEntity.notFound().build(); // Retorna esse caso não encontre a viagem

    }
    
    @GetMapping("/{id}/confirm") //Confirma a viagem
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){//Estamos falando de pegar os dados a partir do id 
        Optional<Trip> trip = this.repository.findById(id); // A busca é feita pelo Id que extrai para um option pois não sabe se realmente vai estar lá 
        if(trip.isPresent()){// Verifica se a trip existe
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true); // Mudando o valor de false(adicionado por padrão ao cadastrar uma viagem) para True

            this.repository.save(rawTrip); //Salvar alterações no banco de dados
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();

    }
    
    @PostMapping("/{id}/invite") //Enviar dados novos
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id); // A busca é feita pelo Id que extrai para um option pois não sabe se realmente vai estar lá 
        if(trip.isPresent()){// Verifica se a trip existe
            Trip rawTrip = trip.get(); //Pega a viagem

            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip); //Cria um novo participante

            if (rawTrip.getIsConfirmed())  this.participantService.triggerConfirmationEmailToParticipant(payload.email());{
                
            }

            return ResponseEntity.ok(participantResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants") //Listar participantes da viagem
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvent(id);
        
        return ResponseEntity.ok(participantList);
    }

    @PostMapping("/{id}/activities") //Enviar dados novos
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id); // A busca é feita pelo Id que extrai para um option pois não sabe se realmente vai estar lá 
        if(trip.isPresent()){// Verifica se a trip existe
            Trip rawTrip = trip.get(); //Pega a viagem

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/activities")//Listar atividades da viagem
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromEvent(id);
        
        return ResponseEntity.ok(activityDataList);
    }

    @PostMapping("/{id}/links") //Enviar dados novos
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id); // A busca é feita pelo Id que extrai para um option pois não sabe se realmente vai estar lá 
        if(trip.isPresent()){// Verifica se a trip existe
            Trip rawTrip = trip.get(); //Pega a viagem

            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

            return ResponseEntity.ok(linkResponse);
        }

        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id){
        List<LinkData> linkDataList = this.linkService.getAllLinksFromEvent(id);
        
        return ResponseEntity.ok(linkDataList);
    }

}