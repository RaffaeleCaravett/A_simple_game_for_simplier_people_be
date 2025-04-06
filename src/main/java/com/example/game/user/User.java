package com.example.game.user;

import com.example.game.citta.Citta;
import com.example.game.entityInfos.EntityInfos;
import com.example.game.enums.Role;
import com.example.game.gioco.Gioco;
import com.example.game.partita.Partita;
import com.example.game.recensione.Recensione;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends EntityInfos implements UserDetails{
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
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "citta_id")
    private Citta citta;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @JsonIgnore
    private List<Gioco> giochi;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Recensione> recensione;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Partita> partite;

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
    public void setFullName(String nome, String cognome){
        this.fullName=String.valueOf(nome.charAt(0)).toUpperCase()+nome.substring(1) +
                String.valueOf(cognome.charAt(0)).toUpperCase()+cognome.substring(1);
    }
    public void setNome(String nome){
        this.nome = String.valueOf(getNome().charAt(0)).toUpperCase()+getNome().substring(1).toLowerCase();
    }
    public void setCognome(String cognome){
        this.cognome = String.valueOf(getCognome().charAt(0)).toUpperCase()+getCognome().substring(1).toLowerCase();
    }
    public void addGioco(Gioco gioco) throws Exception {
        if(giochi.contains(gioco)) throw new Exception();
        giochi.add(gioco);
    }
}
