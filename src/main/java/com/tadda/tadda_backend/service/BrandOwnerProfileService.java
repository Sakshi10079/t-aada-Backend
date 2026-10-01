package com.tadda.tadda_backend.service;
import com.tadda.tadda_backend.dto.AdminBrandOwnerResponseDto;
import com.tadda.tadda_backend.entity.BrandOwnerProfile;
import com.tadda.tadda_backend.entity.User;
import com.tadda.tadda_backend.repository.BrandOwnerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandOwnerProfileService {

    private final BrandOwnerProfileRepository repository;

    public BrandOwnerProfile create(BrandOwnerProfile profile) {
        return repository.save(profile);
    }

    public List<BrandOwnerProfile> getAll() {
        return repository.findAll();
    }

    public BrandOwnerProfile getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public BrandOwnerProfile getByUser(User user) {
        return repository.findByUser(user).orElse(null);
    }

    public BrandOwnerProfile updateByUser(User user, BrandOwnerProfile updatedProfile) {

        BrandOwnerProfile existingProfile = repository.findByUser(user).orElse(null);

        if (existingProfile == null) {
            return null;
        }

        existingProfile.setBrandName(updatedProfile.getBrandName());
        existingProfile.setBusinessStage(updatedProfile.getBusinessStage());
        existingProfile.setProductCategories(updatedProfile.getProductCategories());
        existingProfile.setSellingPlatforms(updatedProfile.getSellingPlatforms());
        existingProfile.setSocialMediaHandles(updatedProfile.getSocialMediaHandles());
        existingProfile.setMockupExperience(updatedProfile.getMockupExperience());
        existingProfile.setMockupStyle(updatedProfile.getMockupStyle());

        return repository.save(existingProfile);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<AdminBrandOwnerResponseDto> getAllBrandOwnersForAdmin() {
        return repository.findAllBrandOwnersForAdmin();
    }
}