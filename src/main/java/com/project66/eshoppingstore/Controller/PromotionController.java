package com.project66.eshoppingstore.Controller;

import com.project66.eshoppingstore.Service.PromotionService;
import com.project66.eshoppingstore.entity.Promotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@CrossOrigin(origins = "*")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping
    public List<Promotion> getAllPromotions() {
        return promotionService.getAllPromotions();
    }

    @GetMapping("/{id}")
    public Promotion getPromotion(@PathVariable Long id) {
        return promotionService.getPromotionById(id);
    }

    @PostMapping
    public Promotion createPromotion(@RequestBody Promotion promotion) {
        return promotionService.savePromotion(promotion);
    }

    @PutMapping("/{id}")
    public Promotion updatePromotion(@PathVariable Long id, @RequestBody Promotion updated) {
        Promotion existing = promotionService.getPromotionById(id);
        if (existing != null) {
            existing.setTitle(updated.getTitle());
            existing.setDescription(updated.getDescription());
            existing.setDiscountPercent(updated.getDiscountPercent());
            existing.setStartDate(updated.getStartDate());
            existing.setEndDate(updated.getEndDate());
            return promotionService.savePromotion(existing);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
    }
}
