package tech.vitalis.notificacao_service.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tech.vitalis.notificacao_service.entity.Notificacoes;
import tech.vitalis.notificacao_service.entity.valueObject.TipoNotificacaoEnum;
import tech.vitalis.notificacao_service.repository.NotificacoesRepository;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class NotificacaoService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoService.class);
    private final NotificacoesRepository notificacaoRepository;

    public NotificacaoService(NotificacoesRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    /*
    public void processarMensagemTeste(Map<String, Object> payload) {
        try {
            logger.info("🧪 Processando mensagem de TESTE: {}", payload);

            Integer alunoId = Integer.valueOf(payload.get("alunoId").toString());
            String dataFim = payload.get("dataFim").toString();

            String titulo = "Seu plano vence em " + dataFim;
            criarEPersistirNotificacao(alunoId, titulo, TipoNotificacaoEnum.PLANO_PROXIMO_VENCIMENTO);

            logger.info("🔔 TESTE - Plano do aluno {} vence em {}", alunoId, dataFim);

        } catch (Exception e) {
            logger.error("❌ Erro ao processar mensagem de teste: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no processamento da mensagem de teste", e);
        }
    }
     */

    public void processarPlanoVencimentoAluno(Map<String, Object> evento) {
        try {
            logger.info("📨 Processando evento ALUNO: {}", evento);
            processarEventoPlanoVencimento(evento, "ALUNO");
        } catch (Exception e) {
            logger.error("❌ Erro ao processar evento aluno: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no processamento do evento do aluno", e);
        }
    }

    public void processarPlanoVencimentoPersonal(Map<String, Object> evento) {
        try {
            logger.info("📨 Processando evento PERSONAL: {}", evento);
            processarEventoPlanoVencimento(evento, "PERSONAL");
        } catch (Exception e) {
            logger.error("❌ Erro ao processar evento personal: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no processamento do evento do personal", e);
        }
    }

    private void processarEventoPlanoVencimento(Map<String, Object> evento, String tipoPessoa) {
        try {
            // Extrair TODOS os dados da mensagem
            Integer pessoaId = Integer.valueOf(evento.get("pessoaId").toString());
            String titulo = evento.get("titulo").toString();
            String nomePessoa = evento.get("nomePessoa").toString();

            logger.info("Processando notificação para {}: pessoaId={}, nome={}",
                    tipoPessoa, pessoaId, nomePessoa);

            boolean jaExiste = notificacaoRepository.existsByPessoaIdAndTipoAndTituloAndVisualizadaFalse(
                    pessoaId, TipoNotificacaoEnum.PLANO_PROXIMO_VENCIMENTO, titulo);

            if (!jaExiste) {
                criarEPersistirNotificacao(pessoaId, titulo, TipoNotificacaoEnum.PLANO_PROXIMO_VENCIMENTO);
                logger.info("Notificação de {} criada com sucesso para pessoa {}", tipoPessoa, pessoaId);
            } else {
                logger.info("Notificação já existe para {} {}- ignorando", tipoPessoa ,nomePessoa);
            }

        } catch (Exception e) {
            logger.error("Erro ao processar evento de plano vencimento: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no processamento da notificação", e);
        }
    }

    private void criarEPersistirNotificacao(Integer pessoaId, String titulo, TipoNotificacaoEnum tipo) {
        Notificacoes notificacao = new Notificacoes();
        notificacao.setPessoaId(pessoaId);
        notificacao.setTipo(tipo);
        notificacao.setTitulo(titulo);
        notificacao.setVisualizada(false);
        notificacao.setDataCriacao(LocalDateTime.now());

        try {
            notificacaoRepository.save(notificacao);
            logger.info("✅ Notificação salva com sucesso para pessoa {}", notificacao.getPessoaId());
        } catch (DataIntegrityViolationException e) {
            logger.warn("⚠️ Notificação duplicada detectada para pessoa {} - ignorando", notificacao.getPessoaId());
        } catch (Exception e) {
            logger.error("❌ Erro ao salvar notificação para pessoa {}: {}",
                    notificacao.getPessoaId(), e.getMessage(), e);
            throw e;
        }
    }
}