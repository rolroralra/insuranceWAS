package com.example.insurance.subscription.controller.dto;

import com.example.insurance.subscription.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private Long id;
    private String name;

    public SubscriptionDto(Subscription subscription) {
        BeanUtils.copyProperties(subscription, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
