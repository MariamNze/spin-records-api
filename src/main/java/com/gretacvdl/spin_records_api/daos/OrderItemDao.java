package com.gretacvdl.spin_records_api.daos;

import com.gretacvdl.spin_records_api.entities.OrderItem;
import com.gretacvdl.spin_records_api.exceptions.OrderItemNotFoundException;
import com.gretacvdl.spin_records_api.exceptions.TechnicalDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OrderItemDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String tableName = "order_item";

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> new OrderItem(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity"),
            rs.getBigDecimal("unit_price")
    );

    public List<OrderItem> findAll() {
        try {
            String sql = "SELECT * FROM " + tableName;
            return jdbcTemplate.query(sql, orderItemRowMapper);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public OrderItem findById(Long id) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            return jdbcTemplate.query(sql, orderItemRowMapper, id)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new OrderItemNotFoundException("L'article de commande avec l'ID " + id + " n'existe pas"));
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE order_id = ?";
            return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public OrderItem create(OrderItem orderItem) {
        try {
            String sql = "INSERT INTO " + tableName + " (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connexion -> {
                PreparedStatement ps = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, orderItem.getOrderId());
                ps.setLong(2, orderItem.getProductId());
                ps.setInt(3, orderItem.getQuantity());
                ps.setBigDecimal(4, orderItem.getUnitPrice());
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKey().longValue();
            orderItem.setId(id);
            return orderItem;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la création de l'article de commande, pour la commande avec l'ID " + orderItem.getOrderId(), e);
        }
    }

    public OrderItem update(Long id, OrderItem orderItem) {
        try {
            if (!orderItemExists(id)) {
                throw new OrderItemNotFoundException("Article de commande avec l'ID " + id + " n'existe pas");
            }
            String sql = "UPDATE " + tableName + " SET order_id = ?, product_id = ?, quantity = ?, unit_price = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql,
                    orderItem.getOrderId(),
                    orderItem.getProductId(),
                    orderItem.getQuantity(),
                    orderItem.getUnitPrice(),
                    id);
            if (rowsAffected <= 0) {
                throw new TechnicalDatabaseException("Aucune ligne mise à jour pour l'article de commande avec l'ID " + id);
            }
            return this.findById(id);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la mise à jour de l'article de commande avec l'ID " + id, e);
        }
    }

    public boolean delete(Long id) {
        try {
            String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la suppression de l'article de commande avec l'ID " + id, e);
        }
    }

    private boolean orderItemExists(Long id) {
        try {
            String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la vérification de l'existence de l'article de commande avec l'ID " + id, e);
        }
    }
}