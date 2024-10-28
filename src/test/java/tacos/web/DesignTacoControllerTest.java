package tacos.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tacos.data.IngredientRepository;
import tacos.model.Ingredient;
import tacos.model.Ingredient.Type;
import tacos.model.Taco;
import tacos.model.TacoOrder;
import tacos.service.IngredientService;
import tacos.service.OrderService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DesignTacoController.class)
public class DesignTacoControllerTest {
    @MockBean
    private IngredientRepository ingredientRepo;

    @MockBean
    private OrderService orderService;

    @MockBean
    private IngredientService ingredientService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testShowDesignForm() throws Exception {

        mockMvc.perform(get("/design"))
                .andExpect(status().isOk())
                .andExpect(view().name("design"))
                .andExpect(content().string(containsString("Design your taco")));
    }

    @Test
    void testProcessTaco_Valid() throws Exception {
        Taco taco = new Taco();
        taco.setName("Test Taco");
        taco.setIngredients(List.of(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP)));

        TacoOrder tacoOrder = new TacoOrder();

        mockMvc.perform(post("/design")
                        .flashAttr("taco", taco)
                        .flashAttr("tacoOrder", tacoOrder))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/current"));

        verify(orderService).addTacoToOrder(any(Taco.class), any(TacoOrder.class));
    }

    @Test
    void testProcessTaco_Invalid() throws Exception {
        Taco taco = new Taco();
        TacoOrder tacoOrder = new TacoOrder();

        mockMvc.perform(post("/design")
                        .flashAttr("taco", taco)
                        .flashAttr("tacoOrder", tacoOrder))
                .andExpect(status().isOk())
                .andExpect(view().name("design"))
                .andExpect(content().string(containsString("Design your taco")));
    }

    @Test
    void testAddIngredientsToModel() throws Exception {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("L1", "Lettuce", Type.WRAP),
                new Ingredient("C1", "Cheese", Type.CHEESE)
        );

        when(ingredientRepo.findAll()).thenReturn(ingredients);
        when(ingredientService.filterIngredientsByType(ingredients, Type.WRAP)).thenReturn(new ArrayList<>(List.of(ingredients.get(0))));
        when(ingredientService.filterIngredientsByType(ingredients, Type.CHEESE)).thenReturn(new ArrayList<>(List.of(ingredients.get(1))));

        mockMvc.perform(get("/design"))
                .andExpect(status().isOk())
                .andExpect(view().name("design"))
                .andExpect(content().string(containsString("Design your taco")))

                .andExpect(model().attribute("wrap", hasItem(ingredients.get(0))))
                .andExpect(model().attribute("cheese", hasItem(ingredients.get(1))));
    }

    @Test
    void testModelAttributes() throws Exception {
        mockMvc.perform(get("/design"))
                .andExpect(status().isOk())
                .andExpect(view().name("design"))
                .andExpect(model().attributeExists("tacoOrder"))
                .andExpect(model().attributeExists("taco"));
    }
}
