package top.linzeliang.seckill.service;

import top.linzeliang.seckill.dto.Exposer;
import top.linzeliang.seckill.dto.SeckillExecution;
import top.linzeliang.seckill.entity.Seckill;
import top.linzeliang.seckill.exception.RepeatKillException;
import top.linzeliang.seckill.exception.SeckillCloseException;
import top.linzeliang.seckill.exception.SeckillException;

import java.util.List;

/**
 * @Description: 业务接口
 * @Author: LinZeLiang
 * @Date: 2021-07-28
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     *
     * @return java.util.List<top.linzeliang.seckill.entity.Seckill>
     * @date 2021-07-28
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return top.linzeliang.seckill.entity.Seckill
     * @date 2021-07-28
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启就输出秒杀接口地址
     *
     * @param seckillId
     * @return top.linzeliang.seckill.dto.Exposer
     * @date 2021-07-28
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return top.linzeliang.seckill.dto.SeckillExecution
     * @date 2021-07-28
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException;

    /**
     * 执行秒杀操作通过存储过程
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return top.linzeliang.seckill.dto.SeckillExecution
     * @date 2021-07-28
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
