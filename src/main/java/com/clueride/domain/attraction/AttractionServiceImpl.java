package com.clueride.domain.attraction;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link AttractionService} interface.
 */
public class AttractionServiceImpl implements AttractionService {

    private final AttractionStore attractionStore;

    @Inject
    public AttractionServiceImpl(
            AttractionStore attractionStore
    ) {
        this.attractionStore = attractionStore;
    }

    @Override
    public Attraction getById(Integer attractionId) {
        return attractionStore.getById(attractionId).build();
    }

    @Override
    public List<Attraction> getByNameFragment(NameFragmentQuery nameFragmentQuery) {
        List<Attraction> attractions = new ArrayList<>();
        for (AttractionEntity entity : attractionStore.getByNameFragment(nameFragmentQuery.fragment)) {
            attractions.add(entity.build());
        }
        return attractions;
    }

}
