package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Departament implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;
  private String name;

  /**
   * Construtor padrão
   */
  public Departament() {}

  /**
   * Construtor como argumentos
   */
  public Departament(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * getters and setters
   */
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * HashCode e Equals para comparar os objetos e não somente os ponteiros
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Departament other = (Departament) obj;
    return Objects.equals(id, other.id);
  }


  /**
   * toString
   */
  @Override
  public String toString() {
    return "Departament [id=" + id + ", name=" + name + "]";
  };


}
