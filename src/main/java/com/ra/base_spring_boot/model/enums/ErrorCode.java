package com.ra.base_spring_boot.model.enums;

public enum ErrorCode {
    // 9xxx: Hệ thống
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống không xác định"),

    // 1xxx: Auth
    UNAUTHENTICATED(1001, "Bạn cần đăng nhập để thực hiện hành động này"),
    UNAUTHORIZED(1002, "Bạn không có quyền truy cập vào chức năng này"),

    // 2xxx: Validation (Dùng cho Regex, NotBlank...)
    INVALID_KEY(2001, "Mã lỗi không hợp lệ"),
    INVALID_EMAIL(2002, "Định dạng email không hợp lệ"),
    INVALID_PHONE(2003, "Số điện thoại phải có 10 chữ số"),
    INVALID_PASSWORD(2004, "Mật khẩu phải ít nhất 8 ký tự, bao gồm chữ hoa và số"),
    INVALID_PASSWORD_OR_EMAIL(2005, "Email hoặc mật khẩu không đúng!"),

    // 3xxx: Logic Nghiệp vụ (Check trùng lặp, logic DB)
    USER_EXISTED(3001, "Tên người dùng đã tồn tại"),
    EMAIL_EXISTED(3002, "Email này đã được sử dụng"),
    USER_NOT_EXISTED(3003, "Người dùng không tồn tại"),

    // 4xxx: Tài nguyên
    DATA_NOT_FOUND(4001, "Dữ liệu yêu cầu không tìm thấy"),
    EMAIL_NOT_FOUND(4002, "Không tìm thấy email!");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}