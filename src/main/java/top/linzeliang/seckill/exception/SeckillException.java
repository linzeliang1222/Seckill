package top.linzeliang.seckill.exception;

/**
 * @Description: TODO
 * @Author: LinZeLiang
 * @Date: 2021-07-28
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
