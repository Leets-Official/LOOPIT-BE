package com.example.loopitbe.repository;

import com.example.loopitbe.dto.request.SellPostSearchCondition;
import com.example.loopitbe.entity.Device;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.enums.PostStatus;
import com.example.loopitbe.enums.PriceRange;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SellPostSpecification {

    public static Specification<SellPost> search(SellPostSearchCondition condition) {
        return (Root<SellPost> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 판매중 필터링
            if (Boolean.TRUE.equals(condition.getOnlySale())) {
                predicates.add(cb.equal(root.get("status"), PostStatus.SALE));
            }

            // 2. 제조사 필터링
            if (condition.getManufacturer() != null && !condition.getManufacturer().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("manufacturer"), condition.getManufacturer()));
            }

            // 3. 가격 범위 필터링
            if (condition.getPriceRange() != null) {
                PriceRange range = condition.getPriceRange();
                predicates.add(cb.between(root.get("price"), condition.getPriceRange().getMin(), condition.getPriceRange().getMax()));
            }

            // 4. 시리즈 필터링
            if (condition.getSeries() != null && !condition.getSeries().isEmpty()) {
                Subquery<String> subquery = query.subquery(String.class);
                Root<Device> deviceRoot = subquery.from(Device.class);

                subquery.select(deviceRoot.get("model"))
                        .where(deviceRoot.get("series").in(condition.getSeries()));

                predicates.add(root.get("model").in(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
