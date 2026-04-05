package com.myblog.RabbitMq;

import com.myblog.model.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveBlogCreated(Blog blog) {
        System.out.println(">>> New blog received:" + blog.getId());
    }

}
