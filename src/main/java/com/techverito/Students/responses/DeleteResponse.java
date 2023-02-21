package com.techverito.Students.responses;

public class DeleteResponse {
    public static final String successMessage = "Student with ID %d has been successfully deleted.";
    public static final String failureMessage = "Student with ID %d could not be deleted.";
    private final String message;
    private final Boolean isSuccess;

    public DeleteResponse(Long id, Boolean isSuccess) {
        this.message = formattedMessage(id, isSuccess);
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    private static String formattedMessage(Long id, Boolean isSuccess) {
        return isSuccess ? String.format(successMessage, id) : String.format(failureMessage, id);
    }
}
