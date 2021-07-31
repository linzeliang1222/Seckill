package top.linzeliang.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.linzeliang.seckill.entity.SuccessKilled;

/**
 * 配置spring和junit的整合，junit加载时启动springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit的spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        int i = successKilledDao.insertSuccessKilled(1001, 15359764545L);
        System.out.println(i);
    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1001, 15359764545L);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}