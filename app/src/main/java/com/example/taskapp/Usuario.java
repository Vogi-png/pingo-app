package com.example.taskapp;

import java.util.Date;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private GeneroPingo fk_genero_pingo;
    private String fk_nome_pingo;
    private Plano plano;
    private int moedas;
    private Date dtcriacao;

    public enum GeneroPingo {
        PINGO, PINGA
    }

    public enum Plano {
        FREE, PRO
    }

    public Usuario() {}

    public Usuario(int id, String nome, String email, String senha,
                   GeneroPingo fk_genero_pingo, String fk_nome_pingo,
                   Plano plano, int moedas, Date dtcriacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fk_genero_pingo = fk_genero_pingo;
        this.fk_nome_pingo = fk_nome_pingo;
        this.plano = plano;
        this.moedas = moedas;
        this.dtcriacao = dtcriacao;
    }

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public GeneroPingo getFk_genero_pingo() { return fk_genero_pingo; }
    public void setFk_genero_pingo(GeneroPingo fk_genero_pingo) { this.fk_genero_pingo = fk_genero_pingo; }

    public String getFk_nome_pingo() { return fk_nome_pingo; }
    public void setFk_nome_pingo(String fk_nome_pingo) { this.fk_nome_pingo = fk_nome_pingo; }

    public Plano getPlano() { return plano; }
    public void setPlano(Plano plano) { this.plano = plano; }

    public int getMoedas() { return moedas; }
    public void setMoedas(int moedas) { this.moedas = moedas; }

    public Date getDtcriacao() { return dtcriacao; }
    public void setDtcriacao(Date dtcriacao) { this.dtcriacao = dtcriacao; }
}
