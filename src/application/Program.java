package application;

import java.util.Date;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Departament;
import model.entities.Seller;

public class Program {

  public static void main(String[] args) {

    Departament depart = new Departament(1, "Eletronic");
    Seller seller = new Seller(12, "João Silva", "joao@gmail.com", new Date(), 3500.00, depart);

    System.out.println(seller);

    SellerDao sellerDao = DaoFactory.createSellerDao();


  }

}
