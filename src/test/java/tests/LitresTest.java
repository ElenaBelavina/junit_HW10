package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class LitresTest {
    @BeforeEach
    void beforeEach(){
        open("https://www.litres.ru/");
    }

    @ValueSource(strings = {
            "Код  да Винчи", "Анна Каренина", "Пушкин"
         })

    @ParameterizedTest(name = "Для поискового запроса {0} должна выдаваться не пустая подборка книг")
    @DisplayName("Тест @ValueSource")
    @Tag("BLOCKER")
        void searchBookShouldNotBeEmpty(String searchBook){
            $("[data-testid=header__search-input--desktop]").setValue(searchBook).pressEnter();
            $("[data-testid=search-title__wrapper]").shouldHave(text("Результаты поиска"));
          }


    @CsvSource(value = {
            "[href='/chto-slushat/'],                 Мастер и Маргарита",
            "[href='/genre/komiksy-i-manga-274060/'], Убить пересмешника"
          })

    @ParameterizedTest (name = "При клике по пункту  {0} в результатах должна быть ссылка на книгу ~{1}~")
    @DisplayName("Тест @CsvSource")
    @Tag("BLOCKER")
    void searchResultShouldHaveExpectedLink(String clickLink, String expectedResult){
        $(clickLink).click();
        $("body").$(byText(expectedResult)).shouldHave(exactText(expectedResult));
       }


    @CsvFileSource(resources = "/test_data/searchResultsShouldContainExpectedResult.csv")
    @ParameterizedTest (name = "При клике по пункту  {0} в результатах должна быть страница с текстом ~{1}~")
    @DisplayName("Тест @CsvFileSource")
    @Tag("BLOCKER")
    void searchResultShouldHaveExpectedResult(String clickLink, String expectedResult){
        $(clickLink).click();
        $("body").$(byText(expectedResult)).shouldHave(exactText(expectedResult));
    }


    static Stream<Arguments> litresSiteShouldDisplayCorrectLabels() {
        return Stream.of(
                Arguments.of(
                        "[href='/new/'",
                        List.of("легкое чтение",
                                "серьезное чтение",
                                "история",
                                "бизнес-книги",
                                "знания и навыки",
                                "психология, мотивация",
                                "спорт, здоровье, красота",
                                "хобби, досуг",
                                "дом, дача",
                                "детские книги"  )
                            ),
                Arguments.of(
                        "[href='/popular/']",
                        List.of("легкое чтение",
                                "серьезное чтение",
                                "история",
                                "бизнес-книги",
                                "знания и навыки",
                                "психология, мотивация",
                                "спорт, здоровье, красота",
                                "хобби, досуг",
                                "дом, дача",
                                "детские книги" )
                             )
        );
    }

    @MethodSource
    @ParameterizedTest(name = "При клике по пункту  {0} должна открыться страница с меню в левой части ~{1}~")
    @DisplayName("Тест @MethodSource")
    void litresSiteShouldDisplayCorrectLabels (String link, List<String> expectedLabels) {
           $(link).click();
           $$(".GenreList-module__genresList__content_p4utW a").shouldHave(texts(expectedLabels));
    }
}

