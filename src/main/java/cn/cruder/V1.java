package cn.cruder;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 不用volatile修饰FLAG
 * <p/>
 * <p>
 * (1) sleepMillis=100;VM options中不添加 -Xint 参数
 * <br/>
 * t1将FLAG值改变之后,t2可以取到新的值，t3不会停下来
 * <p/>
 * (2) sleepMillis=100;VM options中添加 -Xint 参数
 * <br/>
 * t1将FLAG值改变之后,t2可以取到新的值，t3会停下来
 * <p/>
 * (3) sleepMillis=1;  VM options中添加 -Xint 参数
 * <br/>
 * t1将FLAG值改变之后,t2可以取到新的值;t3会停下来
 * <p/>
 * (4) sleepMillis=1;  VM options中不添加 -Xint 参数
 * <br/>
 * t1将FLAG值改变之后,t2可以取到新的值;t3会停下来
 * <p>
 * <p>
 * <p>
 * <p/>
 * 原因分析：如果不加-Xint参数，jit对于会优化代码，t3代码经常从物理内存中取值，每次都是false，
 * while循环被优化
 * <pre class="code">
 *  while (!false) {
 *      i++;
 *  }
 *
 * </pre>
 * @Author: cruder
 * @Date: 2021/11/20/10:34
 */
@Slf4j
public class V1 {
    private static Boolean FLAG = false;


    private static Long sleepMillis = 100L;

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
