package com.gretacvdl.spin_records_api.daos;

import com.gretacvdl.spin_records_api.entities.Order;
import com.gretacvdl.spin_records_api.exceptions.OrderNotFoundException;
import com.gretacvdl.spin_records_api.exceptions.TechnicalDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String tableName = "orders";

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> new Order(
            rs.getLong("id"),
            rs.getLong("customer_id"),
            rs.getBigDecimal("total"),
            rs.getTimestamp("created_at")
    );

    public List<Order> findAll() {
        try {
            String sql = "SELECT * FROM " + tableName;
            return jdbcTemplate.query(sql, orderRowMapper);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique lors de la récupération des commandes : " + e.getMessage(), e);
        }
    }

    public Order findById(Long id) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            return jdbcTemplate.query(sql, orderRowMapper, id)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new OrderNotFoundException("La commande avec l'ID " + id + " n'existe pas"));
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public List<Order> findByCustomerId(Long customerId) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE customer_id = ? ORDER BY created_at DESC";
            return jdbcTemplate.query(sql, orderRowMapper, customerId);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public Order create(Order order) {
        try {
            String sql = "INSERT INTO " + tableName + " (customer_id, total) VALUES (?, ?)";
            jdbcTemplate.update(sql,
                    order.getCustomerId(),
                    order.getTotal()
            );
            String sqlGetId = "SELECT LAST_INSERT_ID()";
            Long id = jdbcTemplate.queryForObject(sqlGetId, Long.class);
            order.setId(id);
            return order;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la création de la commande pour le client avec l'ID : " + order.getCustomerId(), e);
        }
    }

    public Order update(Long id, Order order) {
        try {
            if (!orderExists(id)) {
                throw new OrderNotFoundException("Commande avec l'ID " + id + " n'existe pas");
            }
            String sql = "UPDATE " + tableName + " SET customer_id = ?, total = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql,
                    order.getCustomerId(),
                    order.getTotal(),
                    id);
            if (rowsAffected <= 0) {
                throw new TechnicalDatabaseException("Aucune ligne mise à jour pour la commande avec l'ID " + id);
            }
            return this.findById(id);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la mise à jour de la commande avec l'ID " + id, e);
        }
    }

    public boolean delete(Long id) {
        try {
            String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la suppression de la commande avec l'ID : " + id, e);
        }
    }

    private boolean orderExists(Long id) {
        try {
            String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la vérification de l'existence de la commande avec l'ID " + id, e);
        }
    }
}