package com.rocketseat.planner.participant;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired
    private ParticipantRepository repository;

    @PostMapping("/{id}/confirm") //Precisamos receber o e-mail do participante para confirmar
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload){//A busca será feita pelo id, e no body receberemos as informações já existentes. 
        Optional<Participant> participant = this.repository.findById(id);

        if(participant.isPresent()){ //Se o participante existe 
            Participant rawParticipant = participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());

            this.repository.save(rawParticipant); //Salva a confirmação e o nome 

            return ResponseEntity.ok(rawParticipant); //Deu tudo certo, retorna as informações
        }
        return ResponseEntity.notFound().build(); //Caso não encontre o participante
    }
}
