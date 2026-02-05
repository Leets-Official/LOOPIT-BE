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

            // 기본 조건 - 삭제되지 않은 판매글만 조회
            predicates.add(cb.equal(root.get("isDeleted"), false));

            // 기본 조건 - 판매중 상태인 판매글만 조회
            predicates.add(cb.equal(root.get("status"), PostStatus.SALE));

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

            // 5. 키워드 필터링
            // null 체크, 빈 문자열(""), 공백 문자(" ")를 모두 체크합니다.
            if (condition.getKeyword() != null && !condition.getKeyword().trim().isEmpty()) {
                String pattern = "%" + condition.getKeyword().trim() + "%";

                // 제목 혹은 모델명에 LIKE %keyword% << 이런식으로 적용
                Predicate titleLike = cb.like(root.get("title"), pattern);
                Predicate modelLike = cb.like(root.get("model"), pattern);

                predicates.add(cb.or(titleLike, modelLike));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
