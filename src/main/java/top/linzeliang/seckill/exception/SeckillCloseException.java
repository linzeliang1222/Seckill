package top.linzeliang.seckill.exception;

/**
 * @Description: 秒杀关闭异常
 * @Author: LinZeLiang
 * @Date: 2021-07-28
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
