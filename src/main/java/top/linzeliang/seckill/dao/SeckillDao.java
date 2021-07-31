package top.linzeliang.seckill.dao;

import org.apache.ibatis.annotations.Param;
import top.linzeliang.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LinZeLiang
 * @Date: 2021-07-26
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return int
     * @date 2021-07-26
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return top.linzeliang.seckill.entity.Seckill
     * @date 2021-07-26
     */
    Seckill queryById(@Param("seckillId") long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return java.util.List<top.linzeliang.seckill.entity.Seckill>
     * @date 2021-07-26
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀
     * @param paramMap
     * @date 2021-07-30
     */
    void killByProcedure(Map<String, Object> paramMap);
}
