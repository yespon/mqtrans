package com.yespon.mqtrans.services;

import java.util.Map;

/**
 * <p>
 *     利用MQ消息完成断点下载的实现类。
 * </p>
 * project: test
 * package: com.yespon.mqtrans.services
 * Author: yespon
 * Time: 2017/4/9 22:43
 */
public interface MQTranRecvService {
    /**
     * <p>
     *
     * </p>
     * @param msgId 要进行接收的文件对应的唯一标示符
     * @param transInfo 传输信息对象，用于存储具体的传输接收信息
     * @param hRecvFileControl 用户存储文件接收控制结构（RecvFileControl）的MAP对象
     * @param synObjectTrans 用于在多线程情况下对hRecvFileControl这个结构操作的同步控制
     */
    MQTranRecvService(byte[] msgId, TransInfo transInfo, Map hRecvFileControl, Object synObjectTrans);

    /**
     * <p>
     *     从文件传输控制消息队列（如FILETRANS.CONTROL）和文件传输完成控制队列（FILETRANS.CONTROL.FINISH）队列中
     *     获取相应的信息，来填充RecvFileControl对象对应的值，从而得到如何进行下载。
     * </p>
     * @param connection
     * @param receiveName
     * @param recvFileControl
     */
    getControlInfoFromQueue(MQConnection connection, String receiveName, RecvFileControl recvFileControl);

    /**
     * <p>
     *     删除文件传输控制队列（如FILETRANS.CONTROL）和文件传输完成控制队列（FILETRANS.CONTROL.FINISH）队列对应的信息，
     *     从而标识文件已经下载完成。
     * </p>
     * @param connection
     * @param recvFileControl
     */
    deleteInvalidControlInfoFromQueue(MQConnection connection, RecvFileControl recvFileControl);
    
}
