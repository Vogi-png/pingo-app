package com.example.taskapp;

public class Tarefa {
    private String id;
    private String fk_id_usuarios;
    private String titulo;
    private String descricao;
    private String status;
    private Long dtcriacao;
    private Long dtconclusao;
    private String favoritos = "nao";

    public Tarefa() {}

    public Tarefa(String id, String fk_id_usuarios, String titulo, String descricao, String status, Long dtcriacao, Long dtconclusao) {
        this.id = id;
        this.fk_id_usuarios = fk_id_usuarios;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dtcriacao = dtcriacao;
        this.dtconclusao = dtconclusao;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFk_id_usuarios() { return fk_id_usuarios; }
    public void setFk_id_usuarios(String fk_id_usuarios) { this.fk_id_usuarios = fk_id_usuarios; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getDtcriacao() { return dtcriacao; }
    public void setDtcriacao(Long dtcriacao) { this.dtcriacao = dtcriacao; }

    public Long getDtconclusao() { return dtconclusao; }
    public void setDtconclusao(Long dtconclusao) { this.dtconclusao = dtconclusao; }

    public String getFavoritos() { return favoritos; }
    public void setFavoritos(String favorito) { this.favoritos = favoritos; }

}
