package com.example.demo.card;

import com.example.demo.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public ApiResponse<Card> createCard(@RequestBody Card card) {
        Card createdCard = cardService.createCard(card);
        return ApiResponse.<Card>builder()
                .content(createdCard)
                .status(HttpStatus.CREATED)
                .message("Card created successfully")
                .build();
    }

    @GetMapping("/{cardId}")
    public ApiResponse<Card> getCardById(@PathVariable Long cardId) {
        return cardService.getCardById(cardId)
                .map(card -> ApiResponse.<Card>builder()
                        .content(card)
                        .status(HttpStatus.OK)
                        .message("Card found")
                        .build())
                .orElseGet(() -> ApiResponse.<Card>builder()
                        .content(null)
                        .status(HttpStatus.NOT_FOUND)
                        .message("Card not found")
                        .build());
    }

    @GetMapping
    public ApiResponse<List<Card>> getAllCards() {
        List<Card> cards = cardService.getAllCards();
        return ApiResponse.<List<Card>>builder()
                .content(cards)
                .status(HttpStatus.OK)
                .message("Cards retrieved successfully")
                .build();
    }

    @DeleteMapping("/{cardId}")
    public ApiResponse<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ApiResponse.<Void>builder()
                .content(null)
                .status(HttpStatus.NO_CONTENT)
                .message("Card deleted successfully")
                .build();
    }
}
