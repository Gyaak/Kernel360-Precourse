# Kernel360-Precourse
---
## Ch 02. REST API
### REST API는 자원(URI)과 그 자원에 대한 행위(HTTP Method)로 이루어짐
### HTTP Method
|  | GET | POST | PUT | DELETE |
| --- | --- | --- | --- | --- |
| 의미 | 리소스 취득 | 리소스 생성, 추가 | 리소스 갱신, 생성 | 리소스 삭제 |
| CRUD | R | C | C / U | D |
| 멱등성 | O | X | O | O |
| 안정성 | O | X | X | X |
* 멱등성 : 동일한 요청을 여러번 보내도 그 결과가 동일한지
* 안정성 : 요청에 의해 리소스가 변경되는지
---
## Ch 03. Spring Boot Web
### Spring Boot에서의 예외처리
기존 try-catch문을 이용해 예외를 처리할 수 있지만, 코드가 중복되고 일관성 있는 예외처리가 힘듬<br>
Spring Boot에서는 @RestControllerAdvice와 @ExceptionHandler를 통해 좀더 편하게 예외처리가 가능
```
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 컨트롤러들에서 발생한 에러를 처리
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Api> exception(Exception e){

        var response = Api.builder()
                .resultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .resultMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
```

@ExceptionHandler 어노테이션은 컨트롤러의 메소드에 적용할 수 있으며, 이 경우 해당 컨트롤러에서 발생하는 예외만을 처리함

