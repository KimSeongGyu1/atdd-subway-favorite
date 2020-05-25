package wooteco.subway.web.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.exception.InvalidPasswordException;
import wooteco.subway.exception.NoResourceExistException;
import wooteco.subway.exception.SourceEqualsTargetException;
import wooteco.subway.web.member.InvalidAuthenticationException;

@RestControllerAdvice
public class SubwayControllerAdvice {
	private static final Logger LOGGER = LogManager.getLogger("SubwayControllerAdvice");

	@ExceptionHandler(NoResourceExistException.class)
	public ResponseEntity<ExceptionResponse> handleNoResourceExistException(RuntimeException e) {
		LOGGER.error(e);
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({
			SourceEqualsTargetException.class,
			InvalidPasswordException.class
	})
	public ResponseEntity<ExceptionResponse> handleBadRequestException(RuntimeException e) {
		LOGGER.error(e);
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidAuthenticationException.class)
	public ResponseEntity<ExceptionResponse> handleAuthenticationException(Exception e) {
		LOGGER.error(e);
		return new ResponseEntity<>(ExceptionResponse.of(e.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleUnexpectedException(Exception e) {
		LOGGER.error(e);
		return new ResponseEntity<>(ExceptionResponse.of("서버 오류가 발생했어요."), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
