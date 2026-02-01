package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.Transaction;
import com.example.loopitbe.entity.User;

import java.util.List;

public class MyPageResponse{
    String nickname;
    String email;
    String profileImageUrl;
    List<TransactionHistoryResponse> buyList;

    public static MyPageResponse from(User user, List<Transaction> buyList){
        MyPageResponse response = new MyPageResponse();

        response.nickname = user.getNickname();
        response.email = user.getEmail();
        response.profileImageUrl = user.getProfileImage();
        response.buyList = buyList.stream()
                .map(TransactionHistoryResponse::from)
                .toList();

        return response;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public List<TransactionHistoryResponse> getBuyList() {
        return buyList;
    }
}