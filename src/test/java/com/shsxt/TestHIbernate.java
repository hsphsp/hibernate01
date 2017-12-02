package com.shsxt;

import com.shsxt.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;

public class TestHIbernate {

    @Test
    public void test01() {
        // 先创建一个SessionFactory
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        SessionFactory sessionFactory = new MetadataSources(serviceRegistry)
                .buildMetadata().buildSessionFactory();

        // 获取Session
        Session session = sessionFactory.openSession();

        // 利用Session的API进行数据的操作
        Transaction tx = session.beginTransaction();
        User user = new User();
        user.setUserName("ha111");
        user.setPassword("123");
        session.save(user);

        tx.commit();

        session.close();

    }
}
