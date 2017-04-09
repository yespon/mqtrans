package com.yespon.mqtrans.services;

import java.util.Map;

/**
 * <p>
 *     利用MQ消息完成断点续传的实现类。
 * </p>
 * project: test
 * package: com.yespon.mqtrans.services
 * Author: yespon
 * Time: 2017/4/9 10:58
 */
public interface MQTranSendService {
    /**
     * <p>
     *
     * </p>
     * @param msgId 要进行发送的文件对应的唯一标示符
     * @param fileTransControlMsg 文件传输控制消息定义
     * @param transInfo 传输信息对象，用于存储具体的传输接收信息
     * @param hSendFileControl 用户存储文件发送控制结构（SendFileControl）的MAP对象
     * @param synObjectTrans 用于在多线程情况下对hSendFileControl这个结构操作的同步控制
     */
    MQTranSendService(byte[] msgId, FileTransControlMsg fileTransControlMsg, TransInfo transInfo,
                      Map hSendFileControl, Object synObjectTrans);

    /**
     * <p>
     *     判断文件控制传输消息是否在FILETRANS.CONTROL队列中存在（当然，这个队列名可以通过修改sysparamconfig.xml
     *     中相应的节点值进行修改）。以标识文件是否已经开始传输了，如果在此队列中存在根据（HOSTNAME+IP+ABSOLUTEFILENAME）
     *     根据SHA-1算法所获取的MSGID对应的消息，则标识文件已经开始传输了。
     *     判断消息是否存在于队列中采用MQ消息浏览机制
     * </p>
     * @param connection
     * @return
     */
    boolean isExistFileTransControlMsg(MQConnection connection);

    /**
     * <p>
     *     判断在FILETRANS.CONTROL.FINISH（当然这个队列名也可以通过配置文件修改）队列中是否存在标识文件已经传输完毕的消息。
     *     判断此消息是否在队列中存在采用MQ消息浏览机制。
     * </p>
     * @param connection
     * @return
     */
    boolean isExistFileTransControlFinishedMsg(MQConnection connection);

    /**
     * <p>
     *     往FILETRANS.CONTROL队列中发送文件传输控制消息，标识文件已经开始传输了。
     * </p>
     * @param connection
     */
    void sendFileTransControlMsg(MQConnection connection);

    /**
     * <p>
     *     发送文件已经传输完毕的消息到MQ中（如FILETRANS.CONTROL.FINISH队列中），以触发接收接口以开始发送接收文件。
     * </p>
     * @param connection
     */
    void sendFileTransControlFinishedMsg(MQConnection connection);

    /**
     * <p>
     *     发送文件的具体实现。
     *     Step1：判断文件已经传输了多少
     *          1.1 如果启用了客户端持久化机制（即GlobalVal。isControlMsgPersist为True），
     *              则从GlobalVar.tempPath + "/send/" + ByteBuffer.ByteToHex(MsgId)对应的文件中读取相应的内容生成SendFileControl
     *              对应的对象，从而可以知道已经传输了多少。并调用hSendFileControl.put(msgId, sendFileControl)把此对象放入对应的
     *              hSendFileControl结构中
     *          1.2 否则，判断内存中的hSendFileControl结构是否存在msgId对应的SendFileControl对象，如果存在则知道传输了多少，
     *          如果不存在，则知道这个文件对应的数据需要从头开始传输并且需要创建一个SendFileControl对象并放入hSendFileControl
     *          结构中。
     *     Step2：如果已经传输完毕（即sendFileControl.isFinished为True）则删除GlobalVar.tempPath + "/send/"
     *          + ByteBuffer.ByteToHex(msgId)对应的持久化文件，并返回成功代码
     *     Step3：用SendFileControl结构的segSize更新FileTransControlMsg.segSize信息，以防止修改sysparamcnfig.xml相应的节点
     *          信息而导致传输的问题（因为不更新将导致针对这个文件传输拆分出来的一个消息大小不一致），这个将调用
     *          connectionPool.getConnection（connectionInfo）这个方法来从连接池中取得与MQ的连接，如果取连接失败的话，则
     *          返回相应的失败代码
     *     Step4：调用isExistFileTransControlMsg（connection）方法，来判断是否在传输控制队列存在要传输的文件对应的文件传输控制信息。
     *
     *
     * </p>
     */
    void sendFile();
}
