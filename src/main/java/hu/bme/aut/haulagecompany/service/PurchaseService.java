package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Purchase;
import hu.bme.aut.haulagecompany.model.PurchasedGood;
import hu.bme.aut.haulagecompany.model.dto.PurchaseDTO;
import hu.bme.aut.haulagecompany.model.dto.PurchasedGoodDTO;
import hu.bme.aut.haulagecompany.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase createPurchase(PurchaseDTO purchaseDTO) {
        Purchase purchase = new Purchase();
        purchase.setShopId(purchaseDTO.getShopId());

        List<PurchasedGoodDTO> purchasedGoodsDTO = purchaseDTO.getGoods();
        List<PurchasedGood> purchasedGoods = new ArrayList<>();
        for (PurchasedGoodDTO purchasedGoodDTO : purchasedGoodsDTO) {
            PurchasedGood purchasedGood = new PurchasedGood();
            purchasedGood.setGoodId(purchasedGoodDTO.getGoodId());
            purchasedGood.setQuantity(purchasedGoodDTO.getQuantity());
            purchasedGoods.add(purchasedGood);
        }
        purchase.setGoods(purchasedGoods);

        return purchaseRepository.save(purchase);
    }

    public Iterable<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElse(null);
    }

    public Purchase updatePurchase(Long id, PurchaseDTO purchaseDTO) {
        Purchase existingPurchase = getPurchaseById(id);
        if (existingPurchase != null) {
            existingPurchase.setShopId(purchaseDTO.getShopId());

            List<PurchasedGoodDTO> purchasedGoodsDTO = purchaseDTO.getGoods();
            List<PurchasedGood> purchasedGoods = new ArrayList<>();
            for (PurchasedGoodDTO purchasedGoodDTO : purchasedGoodsDTO) {
                PurchasedGood purchasedGood = new PurchasedGood();
                purchasedGood.setGoodId(purchasedGoodDTO.getGoodId());
                purchasedGood.setQuantity(purchasedGoodDTO.getQuantity());
                purchasedGoods.add(purchasedGood);
            }
            existingPurchase.setGoods(purchasedGoods);

            return purchaseRepository.save(existingPurchase);
        }
        return null;
    }

    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }
}
