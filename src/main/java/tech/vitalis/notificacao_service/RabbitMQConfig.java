package tech.vitalis.notificacao_service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.AcknowledgeMode;

@Configuration
public class RabbitMQConfig {

    // ====== Filas ======
    @Bean
    public Queue planoVencimentoQueue() {
        return new Queue("queue.plano.vencimento", true);
    }

    @Bean
    public Queue planoVencimentoAlunoQueue() {
        return new Queue("queue.plano.vencimento.aluno", true);
    }

    @Bean
    public Queue planoVencimentoPersonalQueue() {
        return new Queue("queue.plano.vencimento.personal", true);
    }

    // ====== Exchange ======
    @Bean
    public TopicExchange notificacoesExchange(@Value("${app.rabbitmq.exchange:notificacoes.exchange}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    // ====== Bindings ======
    @Bean
    public Binding bindingPlanoVencimento(Queue planoVencimentoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(planoVencimentoQueue).to(exchange).with("notificacao.plano.vencimento");
    }

    @Bean
    public Binding bindingPlanoVencimentoAluno(Queue planoVencimentoAlunoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(planoVencimentoAlunoQueue).to(exchange).with("notificacao.plano.vencimento.aluno");
    }

    @Bean
    public Binding bindingPlanoVencimentoPersonal(Queue planoVencimentoPersonalQueue, TopicExchange exchange) {
        return BindingBuilder.bind(planoVencimentoPersonalQueue).to(exchange).with("notificacao.plano.vencimento.personal");
    }

    // ====== Conversor ======
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ====== RabbitTemplate ======
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    // ====== Listener Container Factory ======
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
           MessageConverter messageConverter
    ){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter); // precisa ser Jackson2JsonMessageConverter
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }
}