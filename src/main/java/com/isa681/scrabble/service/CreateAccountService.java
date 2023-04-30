package com.isa681.scrabble.service;

import com.isa681.scrabble.entity.Player;
import org.springframework.stereotype.Service;

@Service
public interface CreateAccountService {
    void createAccount(Player player);
}
