package com.integral.model;

import com.integral.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
 private boolean isEnable= false;
 private VerificationType sendTo;
}
