package tacos.service;

import org.springframework.stereotype.Service;
import tacos.model.Ingredient;
import tacos.model.Ingredient.Type;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    public Iterable<Ingredient> filterIngredientsByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}