package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Order;
import hu.bme.aut.haulagecompany.model.dto.OrderDTO;
import hu.bme.aut.haulagecompany.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ShopService shopService;
    private final GoodService goodService;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            ShopService shopService,
            GoodService goodService,
            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.shopService = shopService;
        this.goodService = goodService;
    }

    public OrderDTO createPurchase(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        order.setShop(shopService.getShopById(orderDTO.getShopID()));
        order.setGoods(goodService.getGoodsByIds(orderDTO.getGoodIDs()));

        Order createdOrder = orderRepository.save(order);
        return convertToDTO(createdOrder);
    }

    public List<OrderDTO> getAllPurchases() {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        return orders.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public OrderDTO getPurchaseById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(this::convertToDTO).orElse(null);
    }

    public OrderDTO updatePurchase(Long id, OrderDTO updatedOrderDTO) {
        Optional<Order> existingOrder = orderRepository.findById(id);

        if (existingOrder.isPresent()) {
            Order updatedOrder = existingOrder.get();
            updatedOrder.setId(id);
            updatedOrder.setShop(shopService.getShopById(updatedOrderDTO.getShopID()));
            updatedOrder.setGoods(goodService.getGoodsByIds(updatedOrderDTO.getGoodIDs()));

            Order savedOrder = orderRepository.save(updatedOrder);
            return convertToDTO(savedOrder);
        } else {
            return null;
        }
    }

    public void deletePurchase(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO convertToDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public Order getOrderById(Long orderID) {
        Optional<Order> order = orderRepository.findById(orderID);
        return order.orElse(null);
    }
}
