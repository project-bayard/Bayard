package edu.usm.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a query, which is a list of predicates.
 */
public class QueryDto implements Serializable {

    private List<PredicateDto> predicateDtos;

    public List<PredicateDto> getPredicateDtos() {
        return predicateDtos;
    }

    public void setPredicateDtos(List<PredicateDto> predicateDtos) {
        this.predicateDtos = predicateDtos;
    }
}
