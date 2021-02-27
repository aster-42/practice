package flow;

public interface WebErrorInfo {
    void redirect(String link);
    void setErrorTitle(String title);
    void setErrorDetail(String detail);
}
