package com.rleon.springbatchP01txtFileToDataBase.processor;

import com.rleon.springbatchP01txtFileToDataBase.model.dto.Dishes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class DishesItemProcessor implements ItemProcessor<Dishes, Dishes> {


    @Override
    public Dishes process(Dishes dishes) throws Exception {
        String name = dishes.getName().toUpperCase();
        String origin = dishes.getOrigin().toUpperCase();
        String characteristics = dishes.getCharacteristics();

        Dishes transformDishes = new Dishes(name, origin, characteristics);
        log.info("Converting ( {} ) into ( {} )", dishes, transformDishes);
        return transformDishes;
    }
}
