package com.github.message.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.common.Const;
import com.github.message.constant.QueueConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

@Service(version = Const.DUBBO_VERSION, timeout = Const.DUBBO_TIMEOUT, filter = Const.DUBBO_FILTER, interfaceClass = QueueService.class)
public class QueueServiceImpl implements QueueService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier(QueueConst.SIMPLE_MQ_NAME)
    private Queue simpleQueue;

    @Override
    public void submitSimple(String simpleInfo) {
        jmsTemplate.convertAndSend(simpleQueue, simpleInfo);
    }
}
