package com.example.insurance.reward.controller.dto;

import com.example.insurance.reward.model.Reward;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardDto {
    private Long id;
    private String name;

    public RewardDto(Reward reward) {
        BeanUtils.copyProperties(reward, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
