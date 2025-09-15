package tech.vitalis.notificacao_service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.vitalis.notificacao_service.service.NotificacaoService;

import java.util.Map;

@Component
public class NotificacaoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoConsumer.class);

    private final NotificacaoService notificacaoService;

    public NotificacaoConsumer(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    /*
    @RabbitListener(queues = "queue.plano.vencimento", containerFactory = "rabbitListenerContainerFactory")
    public void receberPlanoVencimento(Map<String, Object> payload) {
        logger.info("📨 Microserviço recebeu evento PLANO_VENCIMENTO: {}", payload);
        notificacaoService.processarMensagemTeste(payload);
    }
     */

    @RabbitListener(queues = "queue.plano.vencimento.aluno", containerFactory = "rabbitListenerContainerFactory")
    public void receberPlanoVencimentoAluno(Map<String, Object> payload) {
        logger.info("📨 Microserviço recebeu evento PLANO_VENCIMENTO_ALUNO: {}", payload);
        notificacaoService.processarPlanoVencimentoAluno(payload);
    }

    @RabbitListener(queues = "queue.plano.vencimento.personal", containerFactory = "rabbitListenerContainerFactory")
    public void receberPlanoVencimentoPersonal(Map<String, Object> payload) {
        logger.info("📨 Microserviço recebeu evento PLANO_VENCIMENTO_PERSONAL: {}", payload);
        notificacaoService.processarPlanoVencimentoPersonal(payload);
    }
}