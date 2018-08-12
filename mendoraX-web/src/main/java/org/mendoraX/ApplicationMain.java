package org.mendoraX;

import org.mendoraX.initData.vo.MicroService;

/**
 * @auther menfre
 * @date 2018/8/12
 * version: 1.0
 * desc:
 */
public class ApplicationMain {
    public static void main(String[] args) {
        MendoraX.launch(ApplicationMain.class.getClassLoader(), MicroService.WEB);
    }
}
