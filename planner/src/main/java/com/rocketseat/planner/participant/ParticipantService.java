package com.rocketseat.planner.participant;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ParticipantService {
    //Recebe como parêmtros uma lista que seria de e-mails dos participantes, e o id da viagem/viagem
    public void registerParticipantsToEvent(List<String> participantsToInvite,UUID tripId){
        
    }
    // Assim que o evento for confirmado pelo dono, esse evento é ativado, recebe o id da viagem
    public void triggerConfirmationEmailToParticipants(UUID tripId){}
}
