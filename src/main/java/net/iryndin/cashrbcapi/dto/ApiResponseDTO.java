package net.iryndin.cashrbcapi.dto;

/**
 * General API response with any payload
 * @param <T>
 */
public class ApiResponseDTO<T> {

    private boolean ok;
    private String msg;
    private T payload;

    public ApiResponseDTO() {
    }

    public ApiResponseDTO(boolean ok, String msg, T payload) {
        this.ok = ok;
        this.msg = msg;
        this.payload = payload;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
