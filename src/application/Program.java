package application;

import java.util.ArrayList;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Departament;
import model.entities.Seller;

public class Program {

  public static void main(String[] args) {

    System.out.println("=== TEST: FindById Seller ===");
    SellerDao sellerDao = DaoFactory.createSellerDao();
    System.out.println(sellerDao.findById(3));

    System.out.println("\n=== TEST: FindByDepartament ===");
    Departament dep = new Departament(2, null);
    List<Seller> listSeller = new ArrayList<>();

    listSeller = sellerDao.findByDepartament(dep);
    for (Seller sel : listSeller) {
      System.out.println(sel);
    }

  }

}
