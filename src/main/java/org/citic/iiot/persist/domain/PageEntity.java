package org.citic.iiot.persist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageEntity {

	@JsonIgnore
	private int startLine;

	@JsonIgnore
	private int limitLine;

	@JsonIgnore
	private String orderString;

	@JsonIgnore
	private int totalPage;

	@JsonIgnore
	private int totalLine;

	@JsonIgnore
	private int curPage;

	@JsonIgnore
	private int linePerPage;

	@JsonIgnore
	private String sequence;
}
