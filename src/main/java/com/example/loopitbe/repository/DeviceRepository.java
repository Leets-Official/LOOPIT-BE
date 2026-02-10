package com.example.loopitbe.repository;

import com.example.loopitbe.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findTop10ByModelContainingOrderByModelIdAsc(String keyword);
    List<Device> findTop5ByModelContainingOrderByModelIdAsc(String keyword);

    // 모델명으로 기기 정보 조회 (시리즈 찾기용)
    Optional<Device> findByModel(String model);

    // 같은 시리즈에 속하는 모든 기기 조회 (비슷한 상품 검색용)
    List<Device> findAllBySeries(String series);
}
