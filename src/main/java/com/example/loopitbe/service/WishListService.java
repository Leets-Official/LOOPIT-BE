package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.IsShopInWishListRequest;
import com.example.loopitbe.dto.request.PostWishlistToggleRequest;
import com.example.loopitbe.dto.request.ShopWishlistToggleRequest;
import com.example.loopitbe.dto.response.IsShopInWishListResponse;
import com.example.loopitbe.dto.response.SellPostWishListResponse;
import com.example.loopitbe.dto.response.ShopWishListResponse;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.ShopWishList;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.entity.PostWishList;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.ShopWishListRepository;
import com.example.loopitbe.repository.UserRepository;
import com.example.loopitbe.repository.PostWishListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListService {
    private final PostWishListRepository postWishListRepository;
    private final ShopWishListRepository shopWishListRepository;
    private final UserRepository userRepository;
    private final SellPostRepository sellPostRepository;

    public WishListService(
            PostWishListRepository postWishListRepository,
            ShopWishListRepository shopWishListRepository,
            UserRepository userRepository,
            SellPostRepository sellPostRepository) {
        this.postWishListRepository = postWishListRepository;
        this.shopWishListRepository = shopWishListRepository;
        this.userRepository = userRepository;
        this.sellPostRepository = sellPostRepository;
    }

    public String togglePostWishList(PostWishlistToggleRequest dto){
        Optional<PostWishList> wishlist = postWishListRepository.findByUser_UserIdAndSellPost_Id(dto.getUserId(), dto.getPostId());

        if (wishlist.isPresent()) {
            // 이미 존재하면 삭제
            postWishListRepository.delete(wishlist.get());
            return "Disabled";
        } else {
            // 존재하지 않으면 추가
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            SellPost post = sellPostRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new CustomException(ErrorCode.SELL_POST_NOT_FOUND));
            postWishListRepository.save(new PostWishList(user, post));
            return "Enabled";
        }
    }

    @Transactional
    public List<SellPostWishListResponse> getMyPostWishLists(Long userId) {
        List<PostWishList> postWishLists = postWishListRepository.findAllByUserId(userId);

        return postWishLists.stream()
                .map(postWishList -> SellPostWishListResponse.from(postWishList.getSellPost()))
                .toList();
    }

    public String toggleShopWishList(ShopWishlistToggleRequest dto){
        Optional<ShopWishList> wishlist = shopWishListRepository.findByUser_IdAndShopName(dto.getUserId(), dto.getShopName());

        if (wishlist.isPresent()) {
            // 이미 존재하면 삭제
            shopWishListRepository.delete(wishlist.get());
            return "Disabled";
        } else {
            // 존재하지 않으면 추가
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            shopWishListRepository.save(new ShopWishList(user, dto));
            return "Enabled";
        }
    }

    @Transactional
    public List<ShopWishListResponse> getMyShopWishLists(Long userId) {
        List<ShopWishList> shopWishLists = shopWishListRepository.findAllByUser_Id(userId);

        return shopWishLists.stream()
                .map(ShopWishListResponse::from)
                .toList();
    }

    @Transactional
    public List<IsShopInWishListResponse> checkShopWishList(IsShopInWishListRequest dto) {
        List<String> existingWishedNames = shopWishListRepository.findWishedShopNames(dto.getUserId(), dto.getShopNames());

        return dto.getShopNames().stream()
                .map(name -> new IsShopInWishListResponse(
                        name,
                        existingWishedNames.contains(name) // 리스트에 있으면 true
                ))
                .toList();
    }
}