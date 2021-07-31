package top.linzeliang.seckill.exception;

/**
 * @Description: 重复秒杀异常，运行期异常
 * @Author: LinZeLiang
 * @Date: 2021-07-28
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
