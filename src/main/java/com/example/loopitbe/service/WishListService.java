package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.WishlistToggleRequest;
import com.example.loopitbe.dto.response.WishListSellPostResponse;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.entity.WishList;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.loopitbe.repository.WishListRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final SellPostRepository sellPostRepository;

    public WishListService(WishListRepository wishListRepository, UserRepository userRepository, SellPostRepository sellPostRepository) {
        this.wishListRepository = wishListRepository;
        this.userRepository = userRepository;
        this.sellPostRepository = sellPostRepository;
    }

    public String toggleWishList(WishlistToggleRequest dto){
        Optional<WishList> wishlist = wishListRepository.findByUser_UserIdAndSellPost_Id(dto.getUserId(), dto.getPostId());

        if (wishlist.isPresent()) {
            // 이미 존재하면 삭제
            wishListRepository.delete(wishlist.get());
            return "Disabled";
        } else {
            // 존재하지 않으면 추가
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            SellPost post = sellPostRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new CustomException(ErrorCode.SELL_POST_NOT_FOUND));
            wishListRepository.save(new WishList(user, post));
            return "Enabled";
        }
    }

    @Transactional
    public List<WishListSellPostResponse> getMyWishLists(Long userId) {
        List<WishList> wishLists = wishListRepository.findAllByUserId(userId);

        return wishLists.stream()
                .map(wishList -> WishListSellPostResponse.from(wishList.getSellPost()))
                .toList();
    }
}