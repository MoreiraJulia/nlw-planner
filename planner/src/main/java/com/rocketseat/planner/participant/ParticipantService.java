package com.rocketseat.planner.participant;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketseat.planner.trip.Trip;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository repository;

    //Recebe como parêmtros uma lista que seria de e-mails dos participantes, e o id da viagem/viagem
    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.repository.saveAll(participants);

        System.out.println(participants.get(0).getId());
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email,  Trip trip){
        Participant newParticipant = new Participant(email, trip);
        this.repository.save(newParticipant);

        return new ParticipantCreateResponse(newParticipant.getId());
    }
    // Assim que o evento for confirmado pelo dono, esse evento é ativado, recebe o id da viagem
    public void triggerConfirmationEmailToParticipants(UUID tripId){};

    public void triggerConfirmationEmailToParticipant(String email){};
    
    public List<ParticipantData> getAllParticipantsFromEvent(UUID tripId){
        return this.repository.findByTripId(tripId).stream().map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(),participant.getIsConfirmed())).toList();
    }
}
