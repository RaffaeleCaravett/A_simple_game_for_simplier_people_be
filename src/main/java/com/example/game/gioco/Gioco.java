package com.example.game.gioco;

import com.example.game.categoria.Categoria;
import com.example.game.classifica.Classifica;
import com.example.game.entityInfos.EntityInfos;
import com.example.game.partita.Partita;
import com.example.game.preferito.Preferito;
import com.example.game.recensione.Recensione;
import com.example.game.richiesta.Richiesta;
import com.example.game.tournament.Tournament;
import com.example.game.trofeo.Trofeo;
import com.example.game.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "giochi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Gioco extends EntityInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nomeGioco;
    @Max(value = 5)
    @Min(value = 1)
    private int difficolta;
    private byte[] image;
    private String descrizione;
    @OneToOne(mappedBy = "gioco", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Classifica classifica;
    @OneToMany(mappedBy = "gioco", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Trofeo> trofeo;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "giochi_utenti",
            joinColumns= @JoinColumn(name = "giochi_id"),
            inverseJoinColumns = @JoinColumn(name = "utenti_id"))
    @JsonIgnore
    private List<User> users;
    @OneToMany(mappedBy = "gioco", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Recensione> recensione;
    private int totalRecensioniNumber;
    @OneToMany(mappedBy = "gioco", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Partita> partite;
    @OneToMany(mappedBy = "gioco", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Preferito> preferiti;
    @OneToMany(mappedBy = "gioco", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Tournament> tournaments;
    @ManyToMany(mappedBy = "giochi", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Categoria> categorie;
    public List<Recensione> getRecensione(){
        if(this.recensione.size()>=2) return this.recensione.stream().filter(Recensione::isActive).toList().subList(0,2);
        return this.recensione.stream().filter(Recensione::isActive).toList();
    }

    public int getTotalRecensioniNumber(){
        if (null!=this.recensione) return this.recensione.stream().filter(Recensione::isActive).toList().size();
        return 0;
    }
    public void addUser(User user) {
        if(users.contains(user)) return;
        users.add(user);
    }

    public void cleanCategorias(){
        this.categorie.clear();
    }
}
