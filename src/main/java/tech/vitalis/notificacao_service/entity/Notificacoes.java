package tech.vitalis.notificacao_service.entity;

import tech.vitalis.notificacao_service.entity.valueObject.TipoNotificacaoEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
public class Notificacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // ✅ Integer como no CaringU

    // ✅ IMPORTANTE: Mapear para o nome correto na tabela
    @Column(name = "pessoas_id", nullable = false)
    private Integer pessoaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoNotificacaoEnum tipo;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "visualizada")
    private Boolean visualizada = false;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    // Construtores
    public Notificacoes() {}

    public Notificacoes(Integer pessoaId, TipoNotificacaoEnum tipo, String titulo) {
        this.pessoaId = pessoaId;
        this.tipo = tipo;
        this.titulo = titulo;
        this.visualizada = false;
        this.dataCriacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Integer pessoaId) {
        this.pessoaId = pessoaId;
    }

    public TipoNotificacaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacaoEnum tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getVisualizada() {
        return visualizada;
    }

    public void setVisualizada(Boolean visualizada) {
        this.visualizada = visualizada;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}