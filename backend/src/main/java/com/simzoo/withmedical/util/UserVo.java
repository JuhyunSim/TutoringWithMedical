package com.simzoo.withmedical.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Long id;
    private String nickname;
    private String role;
}
