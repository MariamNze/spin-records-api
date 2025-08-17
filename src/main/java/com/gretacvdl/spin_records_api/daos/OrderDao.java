package com.gretacvdl.spin_records_api.daos;

import com.gretacvdl.spin_records_api.entities.Order;
import com.gretacvdl.spin_records_api.entities.OrderItem;
import com.gretacvdl.spin_records_api.exceptions.OrderNotFoundException;
import com.gretacvdl.spin_records_api.exceptions.TechnicalDatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String tableName = "orders";

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> new Order(
            rs.getLong("id"),
            rs.getLong("customer_id"),
            rs.getBigDecimal("total"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    private final RowMapper<OrderItem> itemRowMapper = (rs, rowNum) -> new OrderItem(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity"),
            rs.getBigDecimal("unit_price")
    );

    public List<Order> findAll() {
        try {
            String sql = "SELECT * FROM " + tableName;
            return jdbcTemplate.query(sql, orderRowMapper);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
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

    public List<Order> findByCustomerEmail(String email) {
        try {
            String sql = """
                    SELECT o.* FROM orders o
                    JOIN customer c ON o.customer_id = c.id
                    WHERE c.email = ?
                    ORDER BY o.created_at DESC
                    """;
            return jdbcTemplate.query(sql, orderRowMapper, email);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public List<OrderItem> findItems(Long orderId) {
        try {
            String sql = """
                    SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price
                    FROM order_item oi
                    WHERE oi.order_id = ?
                    """;
            return jdbcTemplate.query(sql, itemRowMapper, orderId);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage(), e);
        }
    }

    public List<OrderItem> findItemsWithProductTitle(Long orderId) {
        try {
            String sql = """
                    SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price
                    FROM order_item oi
                    JOIN product p ON oi.product_id = p.id
                    WHERE oi.order_id = ?
                    """;
            return jdbcTemplate.query(sql, itemRowMapper, orderId);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage(), e);
        }
    }

    public Order create(Order order) {
        try {
            String sql = "INSERT INTO " + tableName + " (customer_id, total) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connexion -> {
                PreparedStatement ps = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, order.getCustomerId());
                ps.setBigDecimal(2, order.getTotal());
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKey().longValue();
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