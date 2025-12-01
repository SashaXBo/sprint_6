package ru.praktikum.qa_scooter.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.praktikum.qa_scooter.page.MainPage;
import ru.praktikum.qa_scooter.page.OrderPage;

import java.time.Duration;
import java.util.stream.Stream;

public class OrderTest {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;
    private WebDriverWait wait;

    // Пример данных заказа
    static Stream<OrderData> orderDataProvider() {
        return Stream.of(
                new OrderData("Иван", "Иванов", "Москва, ул. Ленина, 1", "Черкизовская",
                        "+79001000000", "31.12.2025", "двое суток", "чёрный жемчуг",
                        "Комментарий для курьера"),
                new OrderData("Петр", "Петров", "Санкт-Петербург, пр. Мира, 5", "Сокольники",
                        "+79002000000", "01.01.2026", "трое суток", "серая безысходность",
                        "Позвонить за час")
        );
    }

    @BeforeEach
    void init() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://qa-scooter.praktikum-services.ru/");

        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    void shouldOrderScooterFromTopButton(ru.praktikum.qa_scooter.model.OrderData orderData) {
        mainPage.clickTopOrderButton();
        orderPage.fillFirstForm(orderData);
        orderPage.fillSecondForm(orderData);
        orderPage.confirmOrder();
        orderPage.assertOrderCreated();
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    void shouldOrderScooterFromBottomButton(ru.praktikum.qa_scooter.model.OrderData orderData) {
        mainPage.scrollToBottomOrderButton();
        mainPage.clickBottomOrderButton();
        orderPage.fillFirstForm(orderData);
        orderPage.fillSecondForm(orderData);
        orderPage.confirmOrder();
        orderPage.assertOrderCreated();
    }

    static class OrderData {
        String firstName, lastName, address, station, phone, date, rentalPeriod, color, comment;

        OrderData(String firstName, String lastName, String address, String station,
                  String phone, String date, String rentalPeriod, String color, String comment) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.station = station;
            this.phone = phone;
            this.date = date;
            this.rentalPeriod = rentalPeriod;
            this.color = color;
            this.comment = comment;
        }
    }

    @AfterEach
    void clean() {
        if (driver != null) {
            driver.quit();
        }
    }
}
