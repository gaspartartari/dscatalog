package com.learning.dscatalog.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.learning.dscatalog.entities.Product;
import com.learning.dscatalog.projections.ProductProjection;

public class Util {

    public static List<Product> sortList(List<ProductProjection> ordered, List<Product> unordered) {
        
        List<Product> result = new ArrayList<>();

        Map<Long, Product> map = new HashMap<>();
        for(Product p : unordered){
            map.put(p.getId(), p);
        }

        for(ProductProjection projection : ordered){
            result.add(map.get(projection.getId()));
        }

        return result;
    }
    
}
