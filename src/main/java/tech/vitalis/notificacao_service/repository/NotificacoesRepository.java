package tech.vitalis.notificacao_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.vitalis.notificacao_service.entity.Notificacoes;
import tech.vitalis.notificacao_service.entity.valueObject.TipoNotificacaoEnum;

import java.util.List;

@Repository
public interface NotificacoesRepository extends JpaRepository<Notificacoes, Integer> {

    boolean existsByPessoaIdAndTipoAndTituloAndVisualizadaFalse(
            Integer pessoaId,
            TipoNotificacaoEnum tipo,
            String titulo
    );

    List<Notificacoes> findByPessoaIdAndVisualizadaFalse(Integer pessoaId);

    List<Notificacoes> findByPessoaIdAndVisualizadaFalseOrderByDataCriacaoDesc(Integer pessoaId);

    List<Notificacoes> findByTipoAndVisualizadaFalse(TipoNotificacaoEnum tipo);

    List<Notificacoes> findByPessoaIdOrderByDataCriacaoDesc(Integer pessoaId);

    long countByPessoaIdAndVisualizadaFalse(Integer pessoaId);

}