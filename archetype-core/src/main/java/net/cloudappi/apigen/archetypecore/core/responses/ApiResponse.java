package net.cloudappi.apigen.archetypecore.core.responses;

import lombok.Data;
import net.cloudappi.apigen.archetypecore.core.responses.metadata.Metadata;
import net.cloudappi.apigen.archetypecore.core.responses.metadata.Paging;
import net.cloudappi.apigen.archetypecore.core.responses.result.ApiError;
import net.cloudappi.apigen.archetypecore.core.responses.result.ApiResult;
import org.springframework.util.Assert;

import java.util.List;

@Data
public class ApiResponse<T> {

	private ApiResult result;
	private Metadata metadata;
	private T data;

	public ApiResponse() {
		this(null);
	}

	protected ApiResponse(T data) {
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	public ApiResponse<T> withMetadata(Metadata metadata) {
		Assert.isNull(this.metadata, "Metadata already declared");
		this.metadata = metadata;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <R extends ApiResponse<T>> R withMetadataPagination(Paging paging) {
		initMetadata();
		Assert.isNull(this.metadata.getPaging(), "Paging already declared");
		this.metadata.setPaging(paging);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public <R extends ApiResponse<T>> R withMetadataPagination(Integer init, Integer limit, Long total) {
		initMetadata();
		Assert.isNull(this.metadata.getPaging(), "Paging already declared");
		this.metadata.setPaging(Paging.from(init, limit, total));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public <R extends ApiResponse<T>> R withResult(ApiResult result) {
		Assert.isNull(this.result, "Result already declared");
		this.result = result;
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public <R extends ApiResponse<T>> R withResultStatus(Boolean status) {
		initResult();
		this.result.setStatus(status);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public <R extends ApiResponse<T>> R withResultHttpCode(Integer httpCode) {
		initResult();
		this.result.setHttpCode(httpCode);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public <R extends ApiResponse<T>> R withResultInfo(String info) {
		initResult();
		this.result.setInfo(info);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public <R extends ApiResponse<T>> R withResultUpdatedElements(Integer updatedElements) {
		initResult();
		this.result.setUpdatedElements(updatedElements);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public <R extends ApiResponse<T>> R withResultErrors(List<ApiError> errors) {
		initResult();
		this.result.setErrors(errors);
		return (R) this;
	}

	private void initMetadata() {
		if (this.metadata == null) this.metadata = new Metadata();
	}

	private void initResult() {
		if (this.result == null) this.result = new ApiResult();
	}
}