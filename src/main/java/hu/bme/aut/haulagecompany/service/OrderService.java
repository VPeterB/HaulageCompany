package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Order;
import hu.bme.aut.haulagecompany.model.TransportOperation;
import hu.bme.aut.haulagecompany.model.dto.GetOrderDTO;
import hu.bme.aut.haulagecompany.model.dto.OrderDTO;
import hu.bme.aut.haulagecompany.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public GetOrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        order.setShop(shopService.getShopById(orderDTO.getShopID()));
        if(orderDTO.getGoodIDs() == null){
            order.setGoods(new ArrayList<>());
        }else if (orderDTO.getGoodIDs().isEmpty()){
            order.setGoods(new ArrayList<>());
        }else {
            order.setGoods(goodService.getGoodsByIds(orderDTO.getGoodIDs()));
        }
        Order createdOrder = orderRepository.save(order);
        return convertToGetDTO(createdOrder);
    }

    private GetOrderDTO convertToGetDTO(Order createdOrder) {
        GetOrderDTO newDTO = modelMapper.map(createdOrder, GetOrderDTO.class);
        newDTO.setShopDTO(shopService.convertToDTO(createdOrder.getShop()));
        return newDTO;
    }

    public List<GetOrderDTO> getAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(this::convertToGetDTO)
                .toList();
    }

    public GetOrderDTO getOrderDTOById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(this::convertToGetDTO).orElse(null);
    }

    public GetOrderDTO updateOrder(Long id, OrderDTO updatedOrderDTO) {
        Optional<Order> existingOrder = orderRepository.findById(id);

        if (existingOrder.isPresent()) {
            Order updatedOrder = existingOrder.get();
            updatedOrder.setId(id);
            updatedOrder.setShop(shopService.getShopById(updatedOrderDTO.getShopID()));
            updatedOrder.setGoods(goodService.getGoodsByIds(updatedOrderDTO.getGoodIDs()));

            Order savedOrder = orderRepository.save(updatedOrder);
            return convertToGetDTO(savedOrder);
        } else {
            return null;
        }
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public Order getOrderById(Long orderID) {
        Optional<Order> order = orderRepository.findById(orderID);
        return order.orElse(null);
    }
}
