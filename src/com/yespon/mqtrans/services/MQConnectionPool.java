package com.yespon.mqtrans.services;

/**
 * project: test
 * package: com.yespon.mqtrans.services
 * Author: yespon
 * Time: 2017/4/9 9:23
 */
public interface MQConnectionPool {

    /**
     * 根据连接对象信息建立一个与MQ的新连接
     * @param connectionInfo
     */
    public void addMQConnection(MQConnectionInfo connectionInfo);

    /**
     * <p>
     *     取得一个可用连接
     *     <p>
     *         Step1：判断连接池中是否存在一个可用的空闲连接，<br>
     *             如果存在，则把此连接标志置为“忙标志”并置上相应的时间，返回连接给调用者；<br>
     *             否则，转Step2；
     *         Step2：如果连接池中没有满则调用addMQConnection方法，创建一个新连接，<br>
     *             并把此连接置为“忙标志”，置上相应时间，返回此连接给调用者；<br>
     *                 否则，返回NULL给调用者并调用LOG4J写错误日志。
     *     </p>
     * </p>
     * @param connectionInfo
     * @return
     */
    public MQConnection getMQConnection(MQConnectionInfo connectionInfo);

    /**
     * <p>
     *     把连接重新置为空闲标志
     * </p>
     * @param connection
     */
    public boolean freeConnection(MQConnection connection);

    /**
     * <p>
     *     断开并清除connectionInfo这个连接对应的的MQ连接池中所有的连接
     *     <p>
     *         这个方法主要适用于当网络中断时，导致connectionInfo对象对应的连接池中所有连接失效，<br>
     *             所以需要清除。
     *     </p>
     * </p>
     * @param connectionInfo
     */
    public void destoryAllConnection(MQConnectionInfo connectionInfo);

    /**
     * <p>
     *     清除连接池中的所有空闲时间过长的连接
     * </p>
     */
    public void destoryTimeOutConnection();
}
