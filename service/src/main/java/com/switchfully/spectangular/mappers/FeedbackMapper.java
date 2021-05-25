package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.session.FeedbackForCoach;
import com.switchfully.spectangular.domain.session.FeedbackForCoachee;
import com.switchfully.spectangular.dtos.AddFeedbackForCoachDto;
import com.switchfully.spectangular.dtos.AddFeedbackForCoacheeDto;
import com.switchfully.spectangular.dtos.FeedbackForCoachDto;
import com.switchfully.spectangular.dtos.FeedbackForCoacheeDto;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {
    public FeedbackForCoach toEntity(AddFeedbackForCoachDto dto) {
        return new FeedbackForCoach()
                .setExplanation(dto.getExplanation())
                .setUsefulness(dto.getUsefulness())
                .setPositive(dto.getPositive())
                .setNegative(dto.getNegative());
    }

    public FeedbackForCoachee toEntity(AddFeedbackForCoacheeDto dto) {
        return new FeedbackForCoachee()
                .setPreparedness(dto.getPreparedness())
                .setWillingness(dto.getWillingness())
                .setPositive(dto.getPositive())
                .setNegative(dto.getNegative());
    }

    public FeedbackForCoachDto toDto(FeedbackForCoach entity) {
        if (entity == null) {
            return null;
        }
        return new FeedbackForCoachDto()
                .setExplanation(entity.getExplanation())
                .setUsefulness(entity.getUsefulness())
                .setPositive(entity.getPositive())
                .setNegative(entity.getNegative());
    }

    public FeedbackForCoacheeDto toDto(FeedbackForCoachee entity) {
        if (entity == null) {
            return null;
        }
        return new FeedbackForCoacheeDto()
                .setPreparedness(entity.getPreparedness())
                .setWillingness(entity.getWillingness())
                .setPositive(entity.getPositive())
                .setNegative(entity.getNegative());
    }
}
