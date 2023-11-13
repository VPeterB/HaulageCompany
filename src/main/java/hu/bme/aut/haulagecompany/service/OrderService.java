package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.dto.OrderDTO;
import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public OrderDTO createPurchase(OrderDTO orderDTO) {
        Purchase purchase = new Purchase();
        purchase.setShopId(orderDTO.getShopId());

        List<GoodDTO> purchasedGoodsDTO = orderDTO.getGoods();
        List<PurchasedGood> purchasedGoods = new ArrayList<>();
        for (GoodDTO goodDTO : purchasedGoodsDTO) {
            PurchasedGood purchasedGood = new PurchasedGood();
            purchasedGood.setGoodId(goodDTO.getGoodId());
            purchasedGood.setQuantity(goodDTO.getQuantity());
            purchasedGoods.add(purchasedGood);
        }
        purchase.setGoods(purchasedGoods);

        return purchaseRepository.save(purchase);
    }

    public List<OrderDTO> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public OrderDTO getPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElse(null);
    }

    public OrderDTO updatePurchase(Long id, OrderDTO orderDTO) {
        Purchase existingPurchase = getPurchaseById(id);
        if (existingPurchase != null) {
            existingPurchase.setShopId(orderDTO.getShopId());

            List<GoodDTO> purchasedGoodsDTO = orderDTO.getGoods();
            List<PurchasedGood> purchasedGoods = new ArrayList<>();
            for (GoodDTO goodDTO : purchasedGoodsDTO) {
                PurchasedGood purchasedGood = new PurchasedGood();
                purchasedGood.setGoodId(goodDTO.getGoodId());
                purchasedGood.setQuantity(goodDTO.getQuantity());
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
