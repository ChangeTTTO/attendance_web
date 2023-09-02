package com.example.easy.retry.customized;

import cn.hutool.json.JSONUtil;
import com.aizuda.easy.retry.client.core.callback.RetryCompleteCallback;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.easy.retry.dao.FailOrderBaseMapper;
import com.example.easy.retry.po.FailOrderPo;
import com.example.easy.retry.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCompleteCallback implements RetryCompleteCallback {

    @Autowired
    private FailOrderBaseMapper failOrderBaseMapper;

    /**
     * 重试成功后的回调函数
     * 参数1-场景名称
     * 参数2-执行器名称
     * 参数3-入参信息
     */
    @Override
    public void doSuccessCallback(String sceneName, String executorName, Object[] objects) {
        // 重试成功后删除失败表中的数据
        OrderVo orderVo = (OrderVo) objects[0];
        log.info("远程重试成功,场景{},执行器{},参数信息",sceneName,executorName, JSONUtil.toJsonStr(objects));
        failOrderBaseMapper.delete(
                new LambdaQueryChainWrapper<>(failOrderBaseMapper)
                        .eq(FailOrderPo::getOrderId,orderVo.getOrderId())
        );
    }

    /**
     * 重试达到最大次数后的回调函数
     * 参数1-场景名称
     * 参数2-执行器名称
     * 参数3-入参信息
     */
    @Override
    public void doMaxRetryCallback(String sceneName, String executorName, Object[] objects) {
        OrderVo orderVo = (OrderVo) objects[0];
        log.info("远程重试达到最大限度,场景{},执行器{},参数信息",sceneName,executorName, JSONUtil.toJsonStr(objects));
        // 重试失败后插入订单失败信息
        failOrderBaseMapper.insert(FailOrderPo.builder()
                        .orderId(orderVo.getOrderId())
                        .sourceId(orderVo.getSource())
                        .sceneName(sceneName)
                        .executorName(executorName)
                        .args(JSONUtil.toJsonStr(objects))
                        .build());
    }
}
