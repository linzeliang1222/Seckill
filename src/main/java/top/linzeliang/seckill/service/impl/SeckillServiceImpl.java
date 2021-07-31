package top.linzeliang.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import top.linzeliang.seckill.dao.SeckillDao;
import top.linzeliang.seckill.dao.SuccessKilledDao;
import top.linzeliang.seckill.dao.cache.RedisDao;
import top.linzeliang.seckill.dto.Exposer;
import top.linzeliang.seckill.dto.SeckillExecution;
import top.linzeliang.seckill.entity.Seckill;
import top.linzeliang.seckill.entity.SuccessKilled;
import top.linzeliang.seckill.enums.SeckillStatEnum;
import top.linzeliang.seckill.exception.RepeatKillException;
import top.linzeliang.seckill.exception.SeckillCloseException;
import top.linzeliang.seckill.exception.SeckillException;
import top.linzeliang.seckill.service.SeckillService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LinZeLiang
 * @Date: 2021-07-28
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    /**
     * md5盐值，用于混淆字符串
     */
    private final String salt = "8h$@*i3#%4f846?.73}8fh>;<>?,./)&#78:#c76t23dbq2yv1hi";


    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        // 缓存优化，超时的基础上维护一致性
        Seckill seckill;
        // 先查询redis
        seckill = redisDao.getSeckill(seckillId);
        if (null == seckill) {
            // redis中不存在再访问数据库
            seckill = seckillDao.queryById(seckillId);
            // 数据库中也不存在就抛出异常
            if (null == seckill) {
                return new Exposer(false, seckillId);
            } else {
                // 数据库中查询到了就放入redis
                redisDao.putSeckill(seckill);
            }
        }


        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();
        if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
        if (null == md5 || !this.getMD5(seckillId).equals(md5)) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑
        Date nowTime = new Date();

        try {
            // 记录购买逻辑
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            // 唯一，seckillId和userPhone
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新到记录，秒杀结束
                    throw new SeckillCloseException("seckill is close");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            // 所有检查异常
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (null == md5 || !this.getMD5(seckillId).equals(md5)) {
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }

        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);

        // 执行存储过程，result被复制
        try {
            seckillDao.killByProcedure(map);
            // 获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
