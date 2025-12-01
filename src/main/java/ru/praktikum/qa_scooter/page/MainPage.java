package ru.praktikum.qa_scooter.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы блоков FAQ
    // FAQ секция
    private final By faqSection = By.id("accordion"); // Секция FAQ с id='accordion'
    // Каждый вопрос
    private final By faqQuestion = By.xpath("//div[@id='accordion']/div[contains(@class, 'accordion__item')]"); // Вопрос
    // Стрелочка раскрытия вопроса
    private final By faqArrowButton = By.xpath(".//div[contains(@class, 'accordion__button')]"); // Внутри каждого блока вопроса, стрелка для раскрытия
    // Ответ на вопрос
    private final By faqAnswerPanel = By.xpath(".//div[contains(@class, 'accordion__panel')]"); // Блок с текстом ответа
    // Верхняя кнопка открытия окна заказа
    private final By topOrderButton = By.className("Button_Button__ra12g");
    // Нижняя кнопка открытия окна заказа
    private final By bottomOrderButton = By.xpath("//div[@class='Home_FinishButton__1_cWm']//button");

    public MainPage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Найти FAQ секцию
    public WebElement getFAQSection() {
        return driver.findElement(faqSection);
    } // [локатор: faqSection]

    // Найти все вопросы
    public List<WebElement> getQuestions() {
        return driver.findElements(faqQuestion);
    } // [локатор: faqQuestion]

    // Найти стрелку внутри вопроса
    public WebElement getArrowBtn(WebElement question) {
        return question.findElement(faqArrowButton);
    } // [локатор: faqArrowButton]

    // Получить блок ответа в вопросе
    public WebElement getAnswerPanel(WebElement question) {
        return question.findElement(faqAnswerPanel);
    } // [локатор: faqAnswerPanel]

    public WebElement findQuestionByText(String text) {
        String xpath = String.format(
                "//div[@id='accordion']//div[contains(@class, 'accordion__item') and .//div[text()=\"%s\"]]", text);
        return driver.findElement(By.xpath(xpath));
    }

    //открыть ответ кликом на вопрос
    public void openAnswerByQuestionText(String questionText) {
        WebElement question = findQuestionByText(questionText);
        WebElement arrow = getArrowBtn(question);
        arrow.click();
    }

    //проверить, что ответ отображается
    public boolean isAnswerVisible(String questionText) {
        WebElement question = findQuestionByText(questionText);
        WebElement answer = getAnswerPanel(question);
        return answer.isDisplayed();
    }
    public void clickTopOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(topOrderButton)).click();
    }

    public void scrollToBottomOrderButton() {
        WebElement bottomButton = wait.until(ExpectedConditions.visibilityOfElementLocated(bottomOrderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", bottomButton);
    }

    public void clickBottomOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(bottomOrderButton)).click();
    }
}



