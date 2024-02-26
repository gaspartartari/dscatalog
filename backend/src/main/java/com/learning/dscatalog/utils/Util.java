package com.learning.dscatalog.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.learning.dscatalog.projections.IdProjection;

public class Util {

    public static <ID> List<? extends IdProjection<ID>> sortList(List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unordered) {
        
        List<IdProjection<ID>> result = new ArrayList<>();

        Map<ID, IdProjection<ID>> map = new HashMap<>();
        for(IdProjection<ID> p : unordered){
            map.put(p.getId(), p);
        }

        for(IdProjection<ID> projection : ordered){
            result.add(map.get(projection.getId()));
        }

        return result;
    }
    
}
