package com.simzoo.withmedical.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simzoo.withmedical.config.SecurityConfig;
import com.simzoo.withmedical.controller.SignupController;
import com.simzoo.withmedical.dto.auth.SignupRequestDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.service.MemberService;
import com.simzoo.withmedical.service.SignupService;
import com.simzoo.withmedical.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;


@WebMvcTest(value = SignupController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
@Import({GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignupService signupService;

    @MockBean
    private VerificationService verifyService;

    @MockBean
    private MemberService memberService;

    @Test
    void handleNotValidException_ShouldReturnBadRequest() throws Exception {
        // Given: FieldError 생성
        FieldError fieldError = new FieldError("objectName", "field", "Invalid field value");
        BindingResult bindingResult = new BindException(new Object(), "objectName");
        bindingResult.addError(fieldError);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null,
            bindingResult);

        SignupRequestDto invalidRequestDto = SignupRequestDto.builder()
            .nickname("nickname")
            .gender(null)
            .imageUrl("image")
            .build();

        MemberEntity member = new MemberEntity();

        // When: MockMvc 실행
        ResultActions result = mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDto))
        );

        System.out.println("result: " + result.andReturn().getResponse().getContentAsString());

        // Then: 응답 검증
        result.andExpect(status().isBadRequest());
    }

    @Test
    void handleCustomException_ShouldReturnCustomError() throws Exception {
        // Given: 예외를 발생시키는 동작 설정
        String receivePhone = "010";
        Integer verifyNumber = 1111;

        // verifyService에서 CustomException 발생 설정
        Mockito.doThrow(new CustomException(ErrorCode.NOT_FOUND_VERIFY_NUMBER))
            .when(verifyService).verifyNumber(receivePhone, verifyNumber);

        // When: MockMvc를 사용해 /signup/verify 엔드포인트 호출
        ResultActions result = mockMvc.perform(
            post("/signup/verify")
                .param("receivePhone", receivePhone)
                .param("verifyNumber", verifyNumber.toString())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Debug: 응답 내용 확인
        System.out.println("Response: " + result.andReturn().getResponse().getContentAsString());

        // Then: 응답 검증
        result.andExpect(status().isNotFound()) // 404 상태 확인
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_VERIFY_NUMBER"))
            .andExpect(jsonPath("$.message").value("인증번호가 존재하지 않거나 만료되었습니다."));
    }
}
