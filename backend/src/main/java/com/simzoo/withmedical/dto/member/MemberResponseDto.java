package com.simzoo.withmedical.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.simzoo.withmedical.dto.tutee.TuteeProfileResponseDto;
import com.simzoo.withmedical.dto.tutor.TutorProfileResponseDto;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonInclude(Include.NON_NULL)
public class MemberResponseDto {
    private Long id;
    private String nickname;
    private Gender gender;
    private String phoneNumber;
    private List<Role> role;

    private TutorProfileResponseDto tutorProfile;

    private TuteeProfileResponseDto tuteeProfile;

    private List<TuteeProfileResponseDto> tuteeProfiles;
}
