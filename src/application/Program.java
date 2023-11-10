package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Departament;
import model.entities.Seller;

public class Program {

  public static void main(String[] args) throws ParseException {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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

    System.out.println("\n=== TEST: FindAll ===");
    List<Seller> findAll = new ArrayList<>();
    findAll = sellerDao.findAll();

    for (Seller s : findAll) {
      System.out.println(s);
    }

    System.out.println("\n===TEST: INSERT ===");
    Seller newSeller = new Seller(null, "Marlene Silva", "marlene@hotmail.com",
        sdf.parse("25/11/1998"), 2800.00, dep);
    sellerDao.insert(newSeller);
    System.out.println("Id gerado: " + newSeller.getId());


    System.out.println("\n===TEST: DELETE ===");
    sellerDao.deleteById(6);
  }

}
