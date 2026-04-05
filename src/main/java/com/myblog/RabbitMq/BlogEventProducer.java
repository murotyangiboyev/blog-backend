package com.myblog.RabbitMq;

import com.myblog.model.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishBlogCreated(Blog blog){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                blog
        );
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(">>> Event published for blog:" + blog.getId());
    }

}
