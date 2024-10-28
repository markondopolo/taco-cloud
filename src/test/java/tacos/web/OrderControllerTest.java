package tacos.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tacos.model.Taco;
import tacos.model.TacoOrder;
import tacos.data.OrderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private OrderRepository orderRepo;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testOrderForm() throws Exception {
        mockMvc.perform(get("/orders/current")
                .flashAttr("tacoOrder", new TacoOrder()))
                .andExpect(status().isOk())
                .andExpect(view().name("orderForm"));
    }

    @Test
    void testProcessOrder_Valid() throws Exception {
        var tacoOrder = getTacoOrder();

        mockMvc.perform(post("/orders")
                        .flashAttr("tacoOrder", tacoOrder))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(orderRepo, times(1)).save(tacoOrder);
    }

    @Test
    void testProcessOrder_Invalid() throws Exception {
        mockMvc.perform(post("/orders")
                        .flashAttr("tacoOrder", new TacoOrder()))
                .andExpect(status().isOk())
                .andExpect(view().name("orderForm"))
                .andExpect(model().attributeHasErrors("tacoOrder"));
    }

    @Test
    void testSessionStatusComplete() throws Exception {
        TacoOrder tacoOrder = getTacoOrder();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("tacoOrder", tacoOrder);

        mockMvc.perform(post("/orders")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(orderRepo).save(tacoOrder);
        assertNull(session.getAttribute("tacoOrder"), "The session should not have the 'tacoOrder' attribute after completion.");
    }

    private static TacoOrder getTacoOrder() {
        TacoOrder tacoOrder = new TacoOrder();
        tacoOrder.setTacos(List.of(new Taco()));
        tacoOrder.setCcNumber("5105105105105100");
        tacoOrder.setCcCVV("333");
        tacoOrder.setCcExpiration("12/34");
        tacoOrder.setDeliveryCity("Test name");
        tacoOrder.setDeliveryStreet("Test name");
        tacoOrder.setDeliveryZip("0000");
        tacoOrder.setDeliveryName("Test name");
        tacoOrder.setDeliveryState("Test name");
        return tacoOrder;
    }
}
