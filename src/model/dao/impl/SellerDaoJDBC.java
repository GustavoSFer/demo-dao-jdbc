package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Departament;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

  private Connection conn;

  public SellerDaoJDBC(Connection conn) {
    this.conn = conn;
  }

  @Override
  public void insert(Seller obj) {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(Seller obj) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteById(Integer id) {
    // TODO Auto-generated method stub

  }

  @Override
  public Seller findById(Integer id) {
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = conn.prepareStatement("SELECT seller.*, departament.nome as DepName " + "FROM seller "
          + "INNER JOIN departament " + "ON seller.DepartamentId = departament.id "
          + "WHERE seller.id = ?");

      st.setInt(1, id);

      rs = st.executeQuery();
      // o retono da nossa busca sempre vem na posição 0 mas se estiver info. esta na posição 0
      if (rs.next()) { // Se existir informação na proxima linha (1)
        Departament departament = new Departament();
        departament.setId(rs.getInt("departamentId"));
        departament.setName(rs.getString("DepName"));

        Seller seller = new Seller();
        seller.setId(rs.getInt("id"));
        seller.setName(rs.getString("nome"));
        seller.setEmail(rs.getString("email"));
        seller.setBirthDate(rs.getDate("birthDate"));
        seller.setBaseSalary(rs.getDouble("baseSalary"));
        seller.setDepartament(departament);

        return seller;
      }
      return null;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeResultSet(rs);
      DB.closeStatement(st);
    }
  }

  @Override
  public List<Seller> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

}
