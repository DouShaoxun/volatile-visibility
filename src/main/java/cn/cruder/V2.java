package cn.cruder;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 用volatile修饰FLAG,VM options中不添加 -Xint 参数
 *
 * @Author: cruder
 * @Date: 2021/11/20/10:34
 */
@Slf4j
public class V2 {
    private volatile static Boolean FLAG = false;


    private static Long sleepMillis = 1000L;

    public static void main(String[] args) {


        Thread t1 = new Thread("t1") {

            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(sleepMillis);
                FLAG = true;
                log.info("flag:{}", FLAG);
            }
        };


        Thread t2 = new Thread("t2") {

            @SneakyThrows
            @Override
            public void run() {
                log.info("flag:{}", FLAG);
                Thread.sleep(sleepMillis * 5);
                log.info("flag:{}", FLAG);
            }
        };


        Thread t3 = new Thread("t3") {

            @SneakyThrows
            @Override
            public void run() {
                Long i = 0L;
                while (!FLAG) {
                    i++;
                }
                log.info("i:{}", i);
            }
        };


        t1.start();
        t2.start();
        t3.start();
    }
}
