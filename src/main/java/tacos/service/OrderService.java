package tacos.service;

import org.springframework.stereotype.Service;
import tacos.model.Taco;
import tacos.model.TacoOrder;

@Service
public class OrderService {

    public void addTacoToOrder(Taco taco, TacoOrder tacoOrder) {
        taco.setTacoOrder(tacoOrder);
        tacoOrder.addTaco(taco);
    }
}