package top.linzeliang.seckill.dao;

import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.linzeliang.seckill.entity.Seckill;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 配置spring和junit的整合，junit加载时启动springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit的spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() {
        int i = seckillDao.reduceNumber(1000, new Date());
        System.out.println(i);
    }

    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
    }

    @Test
    public void killByProcedure() {
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", 1001L);
        map.put("phone", 12367834581L);
        map.put("killTime", killTime);
        map.put("result", 0);

        seckillDao.killByProcedure(map);

        int result = MapUtils.getInteger(map, "result", -2);
        System.out.println(result);
    }
}