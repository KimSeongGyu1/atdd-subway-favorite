package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.favorite.dto.AddFavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.favorite.dto.FavoritesResponse;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {
    public static final String STATION_NAME_KANGNAM = "강남역";
    public static final String STATION_NAME_YEOKSAM = "역삼역";
    public static final String STATION_NAME_SEOLLEUNG = "선릉역";
    public static final String STATION_NAME_HANTI = "한티역";
    public static final String STATION_NAME_DOGOK = "도곡역";
    public static final String STATION_NAME_MAEBONG = "매봉역";
    public static final String STATION_NAME_YANGJAE = "양재역";

    public static final String LINE_NAME_2 = "2호선";
    public static final String LINE_NAME_3 = "3호선";
    public static final String LINE_NAME_BUNDANG = "분당선";
    public static final String LINE_NAME_SINBUNDANG = "신분당선";

    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    public static final String TEST_USER_EMAIL2 = "phobi@email.com";
    public static final String TEST_USER_NAME2 = "포비";
    public static final String TEST_USER_PASSWORD2 = "phobi";

    @LocalServerPort
    public int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    public StationResponse createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return
                given().
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        post("/stations").
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(StationResponse.class);
    }

    public List<StationResponse> getStations() {
        return
                given().when().
                        get("/stations").
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", StationResponse.class);
    }

    public void deleteStation(Long id) {
        given().when().
                delete("/stations/" + id).
                then().
                log().all();
    }

    public LineResponse createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        return
                given().
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        post("/lines").
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(LineResponse.class);
    }

    public LineDetailResponse getLine(Long id) {
        return
                given().when().
                        get("/lines/" + id).
                        then().
                        log().all().
                        extract().as(LineDetailResponse.class);
    }

    public void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
        Map<String, String> params = new HashMap<>();
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                put("/lines/" + id).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    public List<LineResponse> getLines() {
        return
                given().when().
                        get("/lines").
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", LineResponse.class);
    }

    public void deleteLine(Long id) {
        given().when().
                delete("/lines/" + id).
                then().
                log().all();
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId) {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId == null ? "" : preStationId.toString());
        params.put("stationId", stationId.toString());
        params.put("distance", distance.toString());
        params.put("duration", duration.toString());

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines/" + lineId + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/lines/" + lineId + "/stations/" + stationId).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    public WholeSubwayResponse retrieveWholeSubway() {
        return
                given().
                        when().
                        get("/lines/detail").
                        then().
                        log().all().
                        extract().as(WholeSubwayResponse.class);
    }

    public PathResponse findPath(String source, String target, String type) {
        return
                given().
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        get("/paths?source=" + source + "&target=" + target + "&type=" + type).
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(PathResponse.class);
    }

    /**
     * 강남 - 역삼 - 선릉
     * |           |
     * |          한티
     * |           |
     * 양재 - 매봉 - 도곡
     */
    public void initStation() {
        // 역 등록
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
        StationResponse stationResponse4 = createStation(STATION_NAME_HANTI);
        StationResponse stationResponse5 = createStation(STATION_NAME_DOGOK);
        StationResponse stationResponse6 = createStation(STATION_NAME_MAEBONG);
        StationResponse stationResponse7 = createStation(STATION_NAME_YANGJAE);

        // 2호선
        LineResponse lineResponse1 = createLine("2호선");
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId(), 0, 0);
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId(), 5, 10);
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId(), 5, 10);

        // 분당선
        LineResponse lineResponse2 = createLine("분당선");
        addLineStation(lineResponse2.getId(), null, stationResponse3.getId(), 0, 0);
        addLineStation(lineResponse2.getId(), stationResponse3.getId(), stationResponse4.getId(), 5, 10);
        addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId(), 5, 10);

        // 3호선
        LineResponse lineResponse3 = createLine("3호선");
        addLineStation(lineResponse3.getId(), null, stationResponse5.getId(), 0, 0);
        addLineStation(lineResponse3.getId(), stationResponse5.getId(), stationResponse6.getId(), 5, 10);
        addLineStation(lineResponse3.getId(), stationResponse6.getId(), stationResponse7.getId(), 5, 10);

        // 신분당선
        LineResponse lineResponse4 = createLine("신분당선");
        addLineStation(lineResponse4.getId(), null, stationResponse1.getId(), 0, 0);
        addLineStation(lineResponse4.getId(), stationResponse1.getId(), stationResponse7.getId(), 40, 3);
    }

    public String createMember(String email, String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);

        return
                given().
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        post("/members").
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().header("Location");
    }

    public void failToCreateMember(String email, String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members").
                then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    protected RequestSpecification setAuthorization(Authentication authentication) {
        TokenResponse tokenResponse = authentication.getTokenResponse();

        return
                given().
                        cookie("JSESSIONID", authentication.getSessionId()).
                        header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public MemberResponse getMember(String email, Authentication authentication) {
        return
                setAuthorization(authentication).
                        when().
                        get("/members?email=" + email).
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(MemberResponse.class);
    }

    public ExceptionResponse failToGetMemberByAuthentication(String email, Authentication authentication) {
        return
                setAuthorization(authentication).
                        when().
                        get("/members?email=" + email).
                        then().
                        log().all().
                        statusCode(HttpStatus.UNAUTHORIZED.value()).
                        extract().as(ExceptionResponse.class);
    }

    public ExceptionResponse failToGetMemberByNotExisting(String email, Authentication authentication) {
        return
                setAuthorization(authentication).
                        when().
                        get("/members?email=" + email).
                        then().
                        log().all().
                        statusCode(HttpStatus.NOT_FOUND.value()).
                        extract().as(ExceptionResponse.class);
    }

    public MemberResponse getMemberById(String memberId, Authentication authentication) {
        return
                setAuthorization(authentication).
                        when().
                        get("/members/" + memberId).
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(MemberResponse.class);
    }

    public ExceptionResponse failToGetMemberByIdBecauseOfAuthentication(String memberId, Authentication authentication) {
        return
                setAuthorization(authentication).
                        when().
                        get("/members/" + memberId).
                        then().
                        log().all().
                        statusCode(HttpStatus.UNAUTHORIZED.value()).
                        extract().as(ExceptionResponse.class);
    }

    public void updateMember(String id, Authentication authentication, String newName, String newPassword) {
        Map<String, String> params = new HashMap<>();
        params.put("name", newName);
        params.put("password", newPassword);

        setAuthorization(authentication).
                body(params).
                when().
                put("/members/" + id).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    public void deleteMember(String id, Authentication authentication) {
        setAuthorization(authentication).
                when().
                delete("/members/" + id).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    public Response login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members/login");
    }

    public TokenResponse getTokenResponse(Response response) {
        return response.
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                and().
                extract().as(TokenResponse.class);
    }

    public FavoriteResponse addFavorite(Long memberId, AddFavoriteRequest addFavoriteRequest, Authentication authentication) {
        return
                setAuthorization(authentication).
                        body(addFavoriteRequest).
                        when().
                        post("/members/" + memberId + "/favorites").
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(FavoriteResponse.class);
    }

    public void failToAddFavorite(Long memberId, AddFavoriteRequest addFavoriteRequest, Authentication authentication) {
        setAuthorization(authentication).
                body(addFavoriteRequest).
                when().
                post("/members/" + memberId + "/favorites").
                then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    public FavoritesResponse readFavorite(Long memberId, Authentication authentication) {
        return
                setAuthorization(authentication).
                        when().
                        get("/members/" + memberId + "/favorites").
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().
                        as(FavoritesResponse.class);
    }

    public void removeFavorite(Long memberId, Long sourceId, Long targetId, Authentication authentication) {
        setAuthorization(authentication).
                when().
                delete("/members/" + memberId + "/favorites/source/" + sourceId + "/target/" + targetId).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    public ValidatableResponse failToRemoveFavorite(Long memberId, Long sourceId, Long targetId, Authentication authentication) {
        return
                setAuthorization(authentication).
                        when().
                        delete("/members/" + memberId + "/favorites/source/" + sourceId + "/target/" + targetId).
                        then().
                        log().all().
                        statusCode(HttpStatus.NOT_FOUND.value());
    }
}

