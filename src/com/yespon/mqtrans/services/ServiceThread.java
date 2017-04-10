package com.yespon.mqtrans.services;

import java.util.Map;

/**
 * project: test
 * package: com.yespon.mqtrans.services
 * Author: yespon
 * Time: 2017/4/9 10:10
 */
public interface ServiceThread {
    /**
     * <p>
     *     服务线程的实现，完成上传、下载等工作
     * </p>
     */

    /**
     * <p>
     *     激发服务线程开始接收
     *
     * </p>
     * @param hRecvFileControl 用户存储文件接收控制结构（RecvFileControl）的MAP对象
     * @param synObjectRecv 用于在多线程情况下对hRecvFileControl这个结构操作的同步控制
     * @param transInfo 传输信息对象，用于存储具体的传输接收信息
     * @param complexEvent 用于进行多线程同步的时间对象，主要是当分块并发接收的时候，调用者需要等待
     *                     所有的进行接线程都返回，然后才能进行下一步的操作。
     * @param msgId 要进行接收的文件对应的唯一标志符
     * @param receiveName 接收者
     */
    void prepareRecv(Map hRecvFileControl, Object synObjectRecv, TransInfo transInfo, ComplexEvent complexEvent,
                     byte[] msgId, String receiveName);

    /**
     * <p>
     *     激发服务线程开始发送
     *
     * </p>
     * @param hSendFileControl 用户存储文件发送控制结构（SendFileControl）的MAP对象
     * @param synObject 用于在多线程情况下对hSendFileControl这个结构操作的同步控制
     * @param fileTransControlMsg 文件传输控制消息定义
     * @param transInfo 传输信息对象，用于存储具体的传输接收信息
     * @param complexEvent 用于进行多线程同步的事件对象，主要是当分块并发发送的时候，
     *                     调用者需要等待所有的进行发送线程都返回，然后才能进行下一步的操作
     * @param msgId 要进行发送的文件对应的唯一标识符
     */
    void prepareSend(Map hSendFileControl, Object synObject, FileTransControlMsg fileTransControlMsg,
                     TransInfo transInfo, ComplexEvent complexEvent, byte[] msgId);

    void run();
}
