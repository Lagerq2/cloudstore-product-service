package se.jensen.elias.cloudstoreproductservice.exceptions;

public record ErrorResponse(int status, String message, long timestamp) {
}
