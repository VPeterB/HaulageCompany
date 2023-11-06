package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Purchase;
import hu.bme.aut.haulagecompany.model.PurchasedGood;
import hu.bme.aut.haulagecompany.model.dto.OrderDTO;
import hu.bme.aut.haulagecompany.model.dto.OrderedGoodDTO;
import hu.bme.aut.haulagecompany.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase createPurchase(OrderDTO orderDTO) {
        Purchase purchase = new Purchase();
        purchase.setShopId(orderDTO.getShopId());

        List<OrderedGoodDTO> purchasedGoodsDTO = orderDTO.getGoods();
        List<PurchasedGood> purchasedGoods = new ArrayList<>();
        for (OrderedGoodDTO orderedGoodDTO : purchasedGoodsDTO) {
            PurchasedGood purchasedGood = new PurchasedGood();
            purchasedGood.setGoodId(orderedGoodDTO.getGoodId());
            purchasedGood.setQuantity(orderedGoodDTO.getQuantity());
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

    public Purchase updatePurchase(Long id, OrderDTO orderDTO) {
        Purchase existingPurchase = getPurchaseById(id);
        if (existingPurchase != null) {
            existingPurchase.setShopId(orderDTO.getShopId());

            List<OrderedGoodDTO> purchasedGoodsDTO = orderDTO.getGoods();
            List<PurchasedGood> purchasedGoods = new ArrayList<>();
            for (OrderedGoodDTO orderedGoodDTO : purchasedGoodsDTO) {
                PurchasedGood purchasedGood = new PurchasedGood();
                purchasedGood.setGoodId(orderedGoodDTO.getGoodId());
                purchasedGood.setQuantity(orderedGoodDTO.getQuantity());
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
