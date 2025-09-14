package com.example.game.user;

import com.example.game.citta.Citta;
import com.example.game.classifica.Classifica;
import com.example.game.connectionRequest.ConnectionRequest;
import com.example.game.descrizione.Descrizione;
import com.example.game.entityInfos.EntityInfos;
import com.example.game.enums.Role;
import com.example.game.gioco.Gioco;
import com.example.game.invito.Invito;
import com.example.game.partita.Partita;
import com.example.game.preferito.Preferito;
import com.example.game.recensione.Recensione;
import com.example.game.richiesta.Richiesta;
import com.example.game.socket.chat.Chat;
import com.example.game.tournament.Tournament;
import com.example.game.trofeo.Trofeo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class User extends EntityInfos implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String immagineProfilo;
    private String changePasswordCode;
    private boolean isValidated;
    private String fullName;
    private Boolean isConnected;
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Descrizione descrizione;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isCompleted;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "citta_id")
    private Citta citta;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @JsonIgnore
    private List<Gioco> giochi;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @JsonIgnore
    private List<Classifica> classificas;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Recensione> recensione;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Partita> partite;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Preferito> preferiti;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Trofeo> trofeo;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Richiesta> richieste;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @JsonIgnore
    private List<Tournament> tournaments;
    private boolean isOpen;
    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Invito> inviti;
    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<ConnectionRequest> connectionRequestsReceived;
    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<ConnectionRequest> connectionRequestsSent;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "administrators")
    @JsonIgnore
    private List<Chat> chatAdministrated;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "utenti")
    @JsonIgnore
    private List<Chat> chats;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_id")
    )
    @JsonIgnore
    private List<User> blocked;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setFullName(String nome, String cognome) {
        this.fullName = String.valueOf(nome.charAt(0)).toUpperCase() + nome.substring(1) + " " +
                String.valueOf(cognome.charAt(0)).toUpperCase() + cognome.substring(1);
    }

    public void setNome(String nome) {
        this.nome = String.valueOf(nome.charAt(0)).toUpperCase() + nome.substring(1).toLowerCase();
    }

    public void setCognome(String cognome) {
        this.cognome = String.valueOf(cognome.charAt(0)).toUpperCase() + cognome.substring(1).toLowerCase();
    }

    public void addGioco(Gioco gioco) {
        if (giochi.contains(gioco)) return;
        giochi.add(gioco);
    }

    public void addClassifica(Classifica classifica) {
        if (classificas.contains(classifica)) return;
        classificas.add(classifica);
    }

    public boolean checkIfPreferitiExists(Gioco gioco) {
        List<Gioco> giochi = preferiti.stream().map(Preferito::getGioco).toList();
        return giochi.contains(gioco);
    }

    public List<Long> getBlocked() {
        if (this.blocked == null || this.blocked.isEmpty()) return new ArrayList<>();
        return this.blocked.stream().map(User::getId).toList();
    }
    public List<User> getBlockedUsers() {
        if (this.blocked == null || this.blocked.isEmpty()) return new ArrayList<>();
        return this.blocked;
    }
}
