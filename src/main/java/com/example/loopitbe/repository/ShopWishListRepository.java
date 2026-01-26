package com.example.loopitbe.repository;

import com.example.loopitbe.entity.ShopWishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShopWishListRepository extends JpaRepository<ShopWishList, Long> {
    List<ShopWishList> findAllByUser_Id(Long userId);
    Optional<ShopWishList> findByUser_IdAndShopName(Long userId, String shopName);
    @Query("SELECT s.shopName FROM ShopWishList s WHERE s.user.userId = :userId AND s.shopName IN :shopNames")
    List<String> findWishedShopNames(Long userId, List<String> shopNames);
}
