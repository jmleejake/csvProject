package com.thekary.mbs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CommonDto", description = "공통 응답")
public class CommonDto<T> {

	/**
	 * 성공 여부
	 */
	@Schema(description = "S:성공(200), F:실패(400-599), U:토큰만료(401)", required = true)
	private String status;

	@Schema(description = "상태 메시지 (SUCCESS:성공, 그 외 에러 메시지)", required = true)
	private List<String> messages;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Schema(description = "리턴값", required = false)
	private T body;

	public CommonDto(String status, String message) {
		super();
		this.status = status;
		this.messages = Arrays.asList(message);
	}

	public CommonDto(String status, String message, T body) {
		super();
		this.status = status;
		this.messages = Arrays.asList(message);
		this.body = body;
	}

	public CommonDto(String status, List<String> messages) {
		super();
		this.status = status;
		this.messages = messages;
	}
}
