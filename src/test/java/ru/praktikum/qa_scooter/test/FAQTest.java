package ru.praktikum.qa_scooter.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.praktikum.qa_scooter.page.MainPage;

import java.util.stream.Stream;

public class FAQTest {
    public WebDriver driver;
    public MainPage mainPage;

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        mainPage = new MainPage(driver);
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    // Источник данных для параметризации
    static Stream<QuestionAnswer> faqQuestions() {
        return Stream.of(
                new QuestionAnswer("Сколько это стоит? И как оплатить?", "Сутки — 400 рублей. Оплата курьеру — наличными или картой."),
                new QuestionAnswer("Хочу сразу несколько самокатов! Так можно?", "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."),
                new QuestionAnswer("Как рассчитывается время аренды?", "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."),
                new QuestionAnswer("Можно ли заказать самокат прямо на сегодня?", "Только начиная с завтрашнего дня. Но скоро станем расторопнее."),
                new QuestionAnswer("Можно ли продлить заказ или вернуть самокат раньше?", "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."),
                new QuestionAnswer("Вы привозите зарядку вместе с самокатом?", "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."),
                new QuestionAnswer("Можно ли отменить заказ?", "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."),
                new QuestionAnswer("Я жизу за МКАДом, привезёте?", "Да, обязательно. Всем самокатов! И Москве, и Московской области.")
                );
    }

    @ParameterizedTest(name = "Проверка ответов FAQ для вопроса: {0}")
    @MethodSource("faqQuestions")
    void faqAnswerOpensCorrectly(QuestionAnswer qa) {
        mainPage.openAnswerByQuestionText(qa.getQuestionText());
        Assertions.assertTrue(mainPage.isAnswerVisible(qa.getQuestionText()), "Ответ не отображается");
    }


    @AfterEach
    void teardown() {
        driver.quit();
    }

    static class QuestionAnswer {
        private String question;
        private String answer;

        QuestionAnswer(String questionText, String expectedAnswer) {
            this.question = questionText;
        }

        public String getQuestionText() {
            return question;
        }

        }
    }

