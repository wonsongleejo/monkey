package com.monkey.storereservationservice.application.context;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@Component
@RequestScope
public class UserContext {
    private Long userId;
    private String userName;
    private String userRole;

    public Long getUserIdOrThrow() {
        if (userId == null) {
            throw new CustomException(ResponseCode.NEED_LOGIN);
        }
        return userId;
    }
}