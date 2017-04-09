package com.yespon.mqtrans.services;

/**
 * project: test
 * package: com.yespon.mqtrans.services
 * Author: yespon
 * Time: 2017/4/9 9:17
 */
public interface InitEnvironment {

    /**
     * 读取sysparamconfig.xml配置完成相应对GlobalVar中变量的赋值
     * @param configFileName
     */
    public void initFromConfigFile(String configFileName);


}
