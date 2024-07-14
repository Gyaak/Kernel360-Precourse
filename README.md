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
### Spring Boot Validation
컨트롤러에서 입력받은 데이터를 어노테이션 기반으로 검증 할 수 있음
-> 핵심로직과 검증로직을 분리하여 생산성이 높아짐

```
@PostMapping("")
    public Api<UserRegisterRequest> register(
        @Valid // 검증하고자 하는 입력값에 @Valid 어노테이션을 붙여 검증
        @RequestBody
        Api<UserRegisterRequest> userRegisterRequest
    ){
        var body = userRegisterRequest.getData();
        return Api.<UserRegisterRequest>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data(body)
                .build();
    }
```
```
@NoArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRegisterRequest {

    private String name;

    private String nickname;

    @Size(min = 1, max = 12)
    @NotBlank
    private String password;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer age;

    @Email
    private String email;

    ...

}
```
## Ch 04. Memory DataBase
실제 데이터 베이스와 연결하기 전에 메모리에 데이터를 저장하며 Repository에 대해 학습
```
abstract public class SimpleDataRepository<T extends Entity, ID extends Long> implements DataRepository<T, ID> {

    private List<T> dataList = new ArrayList<T>(); // 데이터를 리스트에 저장

    private static long index = 0; // id

    private Comparator<T> sort = new Comparator<T>() {
        @Override
        public int compare(T o1, T o2) {
            return Long.compare(o1.getId(), o2.getId());
        }
    };

    @Override
    public T save(T data) {
        if(Objects.isNull(data)){
            throw new RuntimeException("데이터가 존재하지 않습니다.");
        }

        var prevData = dataList.stream()
                .filter(it -> {
                    return it.getId().equals(data.getId());
                })
                .findFirst();
        if(prevData.isPresent()){
            dataList.remove(prevData.get());
            dataList.add(data);
        }else{
            index++;
            data.setId(index);
            dataList.add(data);
        }
        return data;
    }

    @Override
    public Optional<T> findById(ID id){
        return dataList.stream()
                .filter(it -> {
                    return ( it.getId().equals(id) );
                })
                .findFirst();
    }

    @Override
    public List<T> findAll() {
        return dataList
                .stream()
                .sorted(sort)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(ID id) {
        var deleteEntity = dataList.stream()
                .filter(it -> {
                    return ( it.getId().equals(id) );
                })
                .findFirst();
        if(deleteEntity.isPresent()){
            dataList.remove(deleteEntity.get());
        }
    }
}
```

## Ch 06. Spring Data JPA
기존 JDBC는 날것의 코드를 사용하기 때문에 번거로움
Spring Data JPA는 복잡한 데이터나 트랜잭션 관리는 알아서 해주고 사용자는 메소드 기반으로 db에 접근할 수 있다.

```
// JpaRepository를 상속받음으로서 기본적인 CRUD 기능을 사용할 수 있다.
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
```

## Ch 08. 간단한 게시판 프로젝트

### ERD
![simple-board](https://github.com/user-attachments/assets/f71695cb-3f68-45e0-a976-3ecccf3a3f15)

