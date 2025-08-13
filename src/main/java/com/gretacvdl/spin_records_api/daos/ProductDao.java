package com.gretacvdl.spin_records_api.daos;

import com.gretacvdl.spin_records_api.entities.Product;
import com.gretacvdl.spin_records_api.exceptions.ProductNotFoundException;
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
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String tableName = "product";

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("artist"),
            rs.getString("genre"),
            rs.getInt("release_year"),
            rs.getString("label"),
            rs.getBigDecimal("price"),
            rs.getInt("stock"),
            rs.getString("cover_url"),
            rs.getString("description"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public List<Product> findAll() {
        try {
            String sql = "SELECT * FROM " + tableName;
            return jdbcTemplate.query(sql, productRowMapper);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public Product findById(Long id) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            return jdbcTemplate.query(sql, productRowMapper, id)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException("Le produit avec l'ID " + id + " n'existe pas"));
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public List<Product> searchByTitleOrArtist(String keyword) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE LOWER(title) LIKE ? OR LOWER(artist) LIKE ? ORDER BY title";
            String kw = "%" + keyword.toLowerCase() + "%";
            return jdbcTemplate.query(sql, productRowMapper, kw, kw);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur technique : " + e.getMessage());
        }
    }

    public Product create(Product product) {
        try {
            String sql = "INSERT INTO " + tableName + " (title, artist, genre, release_year, label, price, stock, cover_url, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connexion -> {
                PreparedStatement ps = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getTitle());
                ps.setString(2, product.getArtist());
                ps.setString(3, product.getGenre());
                ps.setInt(4, product.getReleaseYear());
                ps.setString(5, product.getLabel());
                ps.setBigDecimal(6, product.getPrice());
                ps.setInt(7, product.getStock());
                ps.setString(8, product.getCoverUrl());
                ps.setString(9, product.getDescription());
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKey().longValue();
            product.setId(id);
            return product;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la création du produit : " + product.getTitle(), e);
        }
    }

    public Product update(Long id, Product product) {
        try {
            if (!productExists(id)) {
                throw new ProductNotFoundException("Produit avec l'ID " + id + " n'existe pas");
            }
            String sql = "UPDATE " + tableName + " SET title = ?, artist = ?, genre = ?, release_year = ?, label = ?, price = ?, stock = ?, cover_url = ?, description = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql,
                    product.getTitle(),
                    product.getArtist(),
                    product.getGenre(),
                    product.getReleaseYear(),
                    product.getLabel(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCoverUrl(),
                    product.getDescription(),
                    id);
            if (rowsAffected <= 0) {
                throw new TechnicalDatabaseException("Aucune ligne mise à jour pour le produit avec l'ID " + id);
            }
            return this.findById(id);
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la mise à jour du produit avec l'ID " + id, e);
        }
    }

    public boolean delete(Long id) {
        try {
            String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la suppression du produit avec l'ID " + id, e);
        }
    }

    private boolean productExists(Long id) {
        try {
            String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            throw new TechnicalDatabaseException("Erreur lors de la vérification de l'existence du produit avec l'ID " + id, e);
        }
    }
}