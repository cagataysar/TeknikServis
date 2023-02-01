package com.garanti.TeknikServis.service;

import com.garanti.TeknikServis.excepton.EntityNoContentException;
import com.garanti.TeknikServis.excepton.UnexpectedException;
import com.garanti.TeknikServis.model.Sale;
import com.garanti.TeknikServis.model.SaleDto;
import com.garanti.TeknikServis.repo.SaleRepository;
import com.garanti.TeknikServis.repo.UserRepo;
import com.garanti.TeknikServis.security.TokenParser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SaleService {

    private SaleRepository repository;
    private UserRepo userRepo;

    public List< Sale > getAll() {
        return repository.getAll();
    }

    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }

    public boolean save ( Sale sale)
    {
        try
        {
            return repository.save(sale);
        }
        catch (Exception e) {
            return false;
        }
    }

    // User Servis Kısmı:
    public List <SaleDto> getListofSales(){
        List<SaleDto> list = repository.getListofSales();
        if(!list.isEmpty())
            return list;
        throw new EntityNoContentException("Sistemde satışa uygun ürün bulunmamaktadır.");
    }
    public List <SaleDto> getListofSalesByProduct(String productType){
        List<SaleDto> list = repository.getListofSalesByProduct(productType);
        if(!list.isEmpty())
            return list;
        throw new EntityNoContentException("Sistemde satışa uygun ürün bulunmamaktadır.");
    }

    public List <SaleDto> getListofSalesByProductId(Integer productID){
        List<SaleDto> list = repository.getListofSalesByProductId(productID);
        if(!list.isEmpty() && productID != null)
            return list;
        throw new EntityNoContentException("Sistemde satışa uygun ürün bulunmamaktadır.");
    }
    @Transactional
    public String buyTheProductInAd(Integer id, String creditcard, HttpHeaders headers){
        if(id != null && creditcard != null && creditcard.length() == 16){
            String username = TokenParser.jwt(headers.get("Authorization").get(0).substring(7));
            int userid = userRepo.getUserId(username);
            if(repository.buyTheProductInAd(id)){
                if(repository.insertSaleToSaleLog(id,userid,creditcard))
                    return "Başarılı bir şekilde ürünü satın aldınız.";
                throw new UnexpectedException("Ürün satın alınırken bir hata ile karşılaşıldı");
            }
            throw new IllegalArgumentException("Satın almaya çalıştığınız ürün sistemde bulunamadı.");

        }
        throw new IllegalArgumentException("İşlem gerçekleştirilirken bir hata ile karşılaşıldı.");


    }
}
