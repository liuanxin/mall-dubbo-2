package com.github.subscribe;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.common.Const;
import com.github.common.service.CommonService;
import com.github.common.util.LogUtil;
import com.github.message.constant.QueueConst;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class SimpleConsume {

    @Reference(version = Const.DUBBO_VERSION, lazy = true, check = false, timeout = Const.DUBBO_TIMEOUT)
    private CommonService commonService;

    @JmsListener(destination = QueueConst.SIMPLE_MQ_NAME)
    public void receiveMessage(final String message) throws JMSException {
        LogUtil.recordTime();
        try {
            handlerBusiness(message);
        } finally {
            if (LogUtil.ROOT_LOG.isInfoEnabled()) {
                LogUtil.ROOT_LOG.info("接收队列({})的数据({})并处理完成", QueueConst.SIMPLE_MQ_NAME, message);
            }
            LogUtil.unbind();
        }
    }

    /** 操作具体的业务 */
    private void handlerBusiness(String message) {
        // int count = commonService.xxx(message);
        // if (LogUtil.ROOT_LOG.isInfoEnabled()) {
        //     LogUtil.ROOT_LOG.info("消费 mq 时操作了 {} 笔订单", count);
        // }
    }
}
