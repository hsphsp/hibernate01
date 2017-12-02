package com.shsxt;

import com.shsxt.model.User;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

public class TestHibernateCRUD {

    private Session session = null;

    @Before
    public void before() {
        // 先创建一个SessionFactory
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        SessionFactory sessionFactory = new MetadataSources(serviceRegistry)
                .buildMetadata().buildSessionFactory();
        // 获取Session
        session = sessionFactory.openSession();
    }

    /**
     * 添加
     */
    @Test
    public void testAdd() {
        Transaction tx = session.beginTransaction();
        User user = new User();
        user.setUserName("add2");
        user.setPassword("2");
        session.save(user);
        tx.commit();
        session.close();
    }

    @Test
    public void testDelete() {
        try {
            Transaction tx = session.beginTransaction();
            User user = new User();
            user.setId(15);
           // user.setUserName("add2");
            //user.setPassword("2");
            session.delete(user);
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() {
        try {
            Transaction tx = session.beginTransaction();
            User user = new User();
            user.setId(12);
            user.setUserName("ha1");
            user.setPassword("123");
            session.update(user);
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindGet() {
        try {
            User user = (User)session.get("User", 2);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindLoad() {
        try {
            User user = (User)session.load("User", 2);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveUpdate() {
        try {
            Transaction tx = session.beginTransaction();
            User user = new User();
           // user.setId(12);
            user.setUserName("ha2211");
            user.setPassword("123456");
            session.saveOrUpdate(user);
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //先根据对象查询数据, 如果有则更新, 如果没有就插入 --根据主键找
    @Test
    public void testMerge() {
        try {
            Transaction tx = session.beginTransaction();
            User user = new User();
            user.setId(16);
            user.setUserName("ha3");
            user.setPassword("123456");
            session.merge(user);
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLock() {

        // 3. 获取session操作数据库
        User user = new User();
        user.setId(2);
        session.lock(user, LockMode.NONE); // 不打印sql没有查询数据库
//		session.lock(restaurant, LockMode.READ); // 执行乐观锁
//		session.lock(restaurant, LockMode.UPGRADE_SKIPLOCKED); // 升级到悲观锁
        try {
            // 4. 查询数据
            user = session.get(User.class, user.getId());
            System.out.println(user.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Test
    public void testOptLock() {
        // 先创建一个SessionFactory
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        SessionFactory sessionFactory = new MetadataSources(serviceRegistry)
                .buildMetadata().buildSessionFactory();
        // 获取Session
        Session session1 = sessionFactory.openSession();
        Session session2 = sessionFactory.openSession();

        // 查询数据
        User user1 = session1.load(User.class, 16);
        User user2 = session2.load(User.class, 16);
        System.out.println(user1.getVersion() + "---" + user2.getVersion());

        user1.setUserName("haha111");
        Transaction tx1 = session1.beginTransaction();
        session1.update(user1);
        tx1.commit();
        System.out.println(user1.getVersion() + "---" + user2.getVersion());

        user2.setUserName("haha222");
        Transaction tx2 = session2.beginTransaction();
        session2.update(user2);
        tx2.commit();
    }

}
