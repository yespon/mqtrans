package com.yespon.mqtrans.services;

import java.util.Map;

/**
 * project: test
 * package: com.yespon.mqtrans.services
 * Author: yespon
 * Time: 2017/4/9 10:34
 */
public class ServiceThreadImpl implements ServiceThread {
    @Override
    public void prepareRecv(Map hRecvFileControl, Object synObjectRecv, TransInfo transInfo, ComplexEvent complexEvent, byte[] msgId, String receiveName) {

    }

    @Override
    public void prepareSend(Map hSendFileControl, Object synObject, FileTransControlMsg fileTransControlMsg, TransInfo transInfo, ComplexEvent complexEvent, byte[] msgId) {

    }

    @Override
    public void run() {
        while(GlobalVar.isRun) {
            try {
                synchronized (synObject) {
                    //等待被激发
                    synObject.wait();
                }
            } catch (InterruptedException e) {
                logger.error(this.getClass().getName() + " occur exception in waiting modify, " +
                        "exception info: ", e);
                //清除中断状态
                //synObject.interrupted()
                continue;
            }

            logger.info(this.getClass().getName() + " has been awakened to service");
            try {
                //有请求需要处理
                switch (tradeCode) {
                    case 1: {//发送
                        MQTranSendService tranSendService = new MQTranSendService(masgId, fileTransControlMsg,
                                transInfo, hSendFileControl, synObjectSend);
                        int iRetVal = tranSendService.sendFile();
                        synchronized (synObjectSend) {
                            SendFileControl sendFileControl = (SendFileControl)hSendFileControl.get(msgId);
                            if (sendFileControl != null) {
                                sendFileControl.retVal = iRetVal;
                            }
                        }
                        try {
                            complexSendEvent.setEvent();
                        } catch (Exception e) {
                            logger.error("Thread: " + this.getClass().getName() + " occur exception in " +
                                    "notify event, exception info: ", e);
                        }
                    }
                    break;
                    case 2: {//接收
                        MQTranRecvService tranRecvService = new MQTranRecvService(msgId, transInfo, hRecvFileControl,
                                synObjectRecv);
                        int iRetVal = tranRecvService.recvFile(receiverName);
                        synchronized (synObjectRecv) {
                            RecvFileControl recvFileControl = (RecvFileControl)hRecvFileControl.get(msgId);
                            if (recvFileControl != null) {
                                recvFileControl.retVal = iRetVal;
                            }
                        }
                        try {
                            complexSendEvent.setEvent();
                        }catch (Exception e) {
                            logger.error("Thread: " + this.getClass().getName() + " occur exception in " +
                                    "notify event, exception info: ", e);
                        }
                    }
                    break;
                    default:
                        break;
                }
            } catch (Exception e) {

            }
            pool.returnServiceThread(this);
        }
    }
}
