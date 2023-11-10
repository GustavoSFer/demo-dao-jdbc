package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    PreparedStatement st = null;
    try {
      st = conn.prepareStatement(
          "INSERT INTO seller (nome, email, birthDate, baseSalary, departamentId) VALUES (?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS);
      st.setString(1, obj.getName());
      st.setString(2, obj.getEmail());
      st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
      st.setDouble(4, obj.getBaseSalary());
      st.setInt(5, obj.getDepartament().getId());

      int linhasAfetadas = st.executeUpdate();

      if (linhasAfetadas > 0) {
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
          int id = rs.getInt(1); // Pega o valor da Keys que gerou na linha de cima;
          obj.setId(id);
        }
        DB.closeResultSet(rs);
      } else {
        throw new DbException("Algo deu errado! Nenhuma linha afetada");
      }
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(st);
    }
  }

  @Override
  public void update(Seller obj) {
    PreparedStatement st = null;
    try {
      st = conn.prepareStatement(
          "UPDATE seller SET nome = ?, email = ?, birthDate = ?, baseSalary = ?, departamentId = ? WHERE seller.id = ?");
      st.setString(1, obj.getName());
      st.setString(2, obj.getEmail());
      st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
      st.setDouble(4, obj.getBaseSalary());
      st.setInt(5, obj.getDepartament().getId());
      st.setInt(6, obj.getId());

      int row = st.executeUpdate();

      if (row > 0) {
        System.out.println("Update realizado com sucesso!");
      }

    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(st);
    }

  }

  @Override
  public void deleteById(Integer id) {
    PreparedStatement st = null;
    try {
      st = conn.prepareStatement("DELETE FROM seller WHERE seller.id = ?",
          Statement.RETURN_GENERATED_KEYS);

      st.setInt(1, id);

      int linhasAlteradas = st.executeUpdate();

      if (linhasAlteradas > 0) {
        System.out.println("foram alterado(s) " + linhasAlteradas + " linhas no banco!");
      } else {
        throw new DbException("Não existe o id informado!");
      }
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(st);
    }

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
        Departament departament = instantiateDepartamento(rs);

        Seller seller = instantiateSeller(rs, departament);

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

  private Seller instantiateSeller(ResultSet rs, Departament departament) throws SQLException {
    Seller seller = new Seller();
    seller.setId(rs.getInt("id"));
    seller.setName(rs.getString("nome"));
    seller.setEmail(rs.getString("email"));
    seller.setBirthDate(rs.getDate("birthDate"));
    seller.setBaseSalary(rs.getDouble("baseSalary"));
    seller.setDepartament(departament);

    return seller;
  }

  private Departament instantiateDepartamento(ResultSet rs) throws SQLException {
    Departament departament = new Departament();
    departament.setId(rs.getInt("departamentId"));
    departament.setName(rs.getString("DepName"));

    return departament;
  }

  @Override
  public List<Seller> findAll() {
    PreparedStatement st = null;
    ResultSet rs = null;

    try {
      st = conn.prepareStatement(
          "SELECT seller.*, departament.nome as DepName FROM seller INNER JOIN departament ON seller.departamentId = departament.id ORDER BY id");
      rs = st.executeQuery();

      List<Seller> listSeller = new ArrayList<>();
      Map<Integer, Departament> map = new HashMap<>();

      while (rs.next()) {
        Departament dep = map.get(rs.getInt("departamentId"));

        if (dep == null) {
          dep = instantiateDepartamento(rs);
          map.put(rs.getInt("departamentId"), dep);
        }

        Seller seller = instantiateSeller(rs, dep);
        listSeller.add(seller);
      }
      return listSeller;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeResultSet(rs);
      DB.closeStatement(st);
    }
  }

  @Override
  public List<Seller> findByDepartament(Departament dep) {
    PreparedStatement st = null;
    ResultSet rs = null;

    try {
      st = conn.prepareStatement("SELECT seller.*,departament.Nome as DepName "
          + "FROM seller INNER JOIN departament " + "ON seller.DepartamentId = departament.id "
          + "WHERE DepartamentId = ? " + "ORDER BY Nome");

      // Colocando o valor na nossa primeira '?'
      st.setInt(1, dep.getId());

      rs = st.executeQuery();

      // O resultado pode ser mais de um valor. Sendo assim, vamos salvar em uma lista
      List<Seller> list = new ArrayList<>();

      // Vamos criar um Map para salvar o departamento e assim verificar se ele ja existe. Para não
      // criar novos na memória.
      Map<Integer, Departament> map = new HashMap<>();

      while (rs.next()) {
        Departament departament = map.get(rs.getInt("DepartamentId"));

        if (departament == null) {
          departament = instantiateDepartamento(rs);
          map.put(rs.getInt("departamentId"), departament);
        }

        Seller seller = instantiateSeller(rs, departament);
        list.add(seller);
      }
      return list;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeResultSet(rs);
      DB.closeStatement(st);
    }
  }

}
