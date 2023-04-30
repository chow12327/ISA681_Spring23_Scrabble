package com.isa681.scrabble.service;
import com.isa681.scrabble.dao.PlayerRepository;
import com.isa681.scrabble.entity.Player;
import org.springframework.stereotype.Service;

@Service
public class CreateAccountServiceImpl implements CreateAccountService {
    private PlayerRepository playerRepository;

    public CreateAccountServiceImpl(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    @Override
    public void createAccount(Player player) {
        playerRepository.save(player);

    }

}
