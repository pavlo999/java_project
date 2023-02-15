package utils;

import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration config = new Configuration().configure("hibernate.cfg.xml");
                config.addAnnotatedClass(Role.class);
                config.addAnnotatedClass(Question.class);
                config.addAnnotatedClass(Answer.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(config.getProperties());
                sessionFactory = config.buildSessionFactory();
            } catch (Exception ex) {
                System.out.println("Error " + ex.getMessage());
            }
        }
        return sessionFactory;
    }
}