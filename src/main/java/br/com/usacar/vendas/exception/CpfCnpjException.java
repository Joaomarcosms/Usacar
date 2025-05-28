package br.com.usacar.vendas.exception;

public class CpfCnpjException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public CpfCnpjException(String msg) {
        super(msg);
    }
    public CpfCnpjException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
