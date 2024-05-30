package repository;

import domain.Car;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class CarRepositoryHibernate implements CarRepository{

    private EntityManager entityManager;

    public CarRepositoryHibernate() {
        entityManager = new Configuration()
                .configure("hibernate/postgres.cfg.xml")
                .buildSessionFactory()
                .createEntityManager();
    }

    @Override
    public Car save(Car car) {
        EntityTransaction transaction = entityManager.getTransaction();
        try { transaction.begin();
            entityManager.persist(car);
            transaction.commit();
            return car;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Transaction cancelled");

        }
    }

    @Override
    public Car getById(Long id) {
        return entityManager.find(Car.class, id);
    }

    @Override
    public List<Car> getAll() {
        //дз без транзакции

        return entityManager.createQuery("SELECT id,brand,price,year FROM Car")
                .getResultList();
    }

    @Override
    public Car update(Car car) {
        //дз с транзакц

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            entityManager.merge(car);

            transaction.commit();
            return car;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Transaction cancelled");

        }

    }

    @Override
    public void delete(Long id) {
        //дз с транзакц

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Car car = this.getById(id);

            transaction.begin();

            entityManager.remove(car);

            transaction.commit();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Transaction cancelled");

        }

    }
}
