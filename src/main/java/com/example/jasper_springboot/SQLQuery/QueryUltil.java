package com.example.jasper_springboot.SQLQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

@Component
public class QueryUltil {
    @PersistenceContext
    EntityManager entityManager;
    public int deleteQuery(String hql){
        Query query = entityManager.createQuery(hql);
        return query.executeUpdate();
    }
}
