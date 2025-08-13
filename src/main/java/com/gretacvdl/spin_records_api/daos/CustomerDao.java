package com.gretacvdl.spin_records_api.daos;

import com.gretacvdl.spin_records_api.entities.Customer;
import com.gretacvdl.spin_records_api.exceptions.CustomerNotFoundException;
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
public class CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String tableName = "customer";

    private final RowMapper<Customer> customerRowMapper = (rs, rowNum) -> new Customer(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("name"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public List<Customer> findAll() {
        try {
            String sql = "SELECT * FROM " + tableName;
            return jdbcTemplate.query(sql, customerRowMapper);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public Customer findById(Long id) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            return jdbcTemplate.query(sql, customerRowMapper, id)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new CustomerNotFoundException("Le client avec l'ID " + id + " n'existe pas"));
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public Customer findByEmail(String email) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE email = ?";
            return jdbcTemplate.query(sql, customerRowMapper, email)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new CustomerNotFoundException("Le client avec l'email " + email + " n'existe pas"));
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public Customer create(Customer customer) {
        try {
            String sql = "INSERT INTO " + tableName + " (email, name) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connexion -> {
                PreparedStatement ps = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, customer.getEmail());
                ps.setString(2, customer.getName());
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKey().longValue();
            customer.setId(id);
            return customer;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la création du client : " + customer.getEmail(), e);
        }
    }

    public Customer update(Long id, Customer customer) {
        try {
            if (!customerExists(id)) {
                throw new CustomerNotFoundException("Client avec l'ID " + id + " n'existe pas");
            }
            String sql = "UPDATE " + tableName + " SET email = ?, name = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql,
                    customer.getEmail(),
                    customer.getName(),
                    id);
            if (rowsAffected <= 0) {
                throw new TechnicalDatabaseException("Aucune ligne mise à jour pour le client avec l'ID " + id);
            }
            return this.findById(id);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la mise à jour du client avec l'ID " + id, e);
        }
    }

    public boolean delete(Long id) {
        try {
            String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la suppression du client avec l'ID " + id, e);
        }
    }

    private boolean customerExists(Long id) {
        try {
            String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la vérification de l'existence du client avec l'ID " + id, e);
        }
    }
}