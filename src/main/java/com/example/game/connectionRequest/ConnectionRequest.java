package com.example.game.connectionRequest;

import com.example.game.entityInfos.EntityInfos;
import com.example.game.enums.Esito;
import com.example.game.enums.EsitoRichiesta;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "connection_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ConnectionRequest extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    @JoinTable(name = "richieste_connessioni_ricevute_utenti",
            joinColumns = @JoinColumn(name = "richieste_id"),
            inverseJoinColumns = @JoinColumn(name = "utenti_id"))
    private User receiver;
    @ManyToOne
    @JoinTable(name = "richieste_connessioni_inviate_utenti",
            joinColumns = @JoinColumn(name = "richieste_id"),
            inverseJoinColumns = @JoinColumn(name = "utenti_id"))
    private User sender;
    private EsitoRichiesta esitoRichiesta;
}
