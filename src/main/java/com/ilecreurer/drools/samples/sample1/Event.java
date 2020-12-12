package com.ilecreurer.drools.samples.sample1;

import java.io.Serializable;
import java.util.Date;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Role.Type;
import org.kie.api.definition.type.Timestamp;

//@Role(Type.EVENT)
//@Timestamp("timestamp")
//@Expires(value="1s")
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date timestamp;
	private String msg;
	private int status;
	public Event() {
		super();
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String toString() {
		return "{\"msg\":\"" + msg + "\","
				+ "\"timestamp\":" + ((timestamp == null)?"":String.valueOf(timestamp.getTime()))
				+ "\"status\":" + status
				+ "}";
	}
}
