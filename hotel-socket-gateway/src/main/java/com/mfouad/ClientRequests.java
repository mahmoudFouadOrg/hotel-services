package com.mfouad;

import java.util.Date;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ClientRequests {
	
	private String status;
	private String RequestMessage;
	private Date requestDate;
	private Date responseDate;
	private String responseMessage;

}
