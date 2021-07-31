package top.linzeliang.seckill.dao;

import org.apache.ibatis.annotations.Param;
import top.linzeliang.seckill.entity.SuccessKilled;

/**
 * @Description: TODO
 * @Author: LinZeLiang
 * @Date: 2021-07-26
 */
public interface SuccessKilledDao {
    

    /**
     * 插入购买明细
     * @param seckillId
     * @param userPhone
     * @return int
     * @date 2021-07-26
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return top.linzeliang.seckill.dao.SuccessKilledDao
     * @date 2021-07-26
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
