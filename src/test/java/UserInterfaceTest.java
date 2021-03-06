import bookStore.bookStorePages.BookStorePage;
import bookStore.bookStorePages.LogInPage;
import bookStore.bookStorePages.ProfilePage;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Random;

import static bookStore.TestConst.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static uiTestHelper.statics.LogInPageStatic.*;

@Epic("UITest")
@Feature("BookStoreUITest")
@Tag("ui")
public class UserInterfaceTest {

    @BeforeAll
    @Step("SetUp")
    static void setUp() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.startMaximized = true;
        Configuration.timeout = 10000;
    }

    @AfterAll
    static void deleteBooksAndLogOutAfterAllTests(){
        open(PROFILE_URL, ProfilePage.class);
        ProfilePage.getDeleteAllBooksBtn().scrollIntoView(true);
        ProfilePage.clickDeleteAllBooksBtn();
        ProfilePage.clickOkDeleteBtn();
        switchTo().alert().accept();
        ProfilePage.clickLogOutBtn();
    }


  @DisplayName("LogIn")
    @Test
    void logInTest() {
        open(LOGIN_URL, LogInPage.class);
        LogInPage.inputUserName(TEST_USER_NAME);
        LogInPage.inputPassword(TEST_USER_PASSWORD);
        LogInPage.getLoginBtn().scrollIntoView(true);
        LogInPage.clickLogInBtn();
        webdriver().shouldHave(url(PROFILE_URL));
    }

    @DisplayName("LogInPageStatics")
    @Test
    public void loginPageStaticsTest(){
        open(LOGIN_URL, LogInPage.class);
        Assertions.assertEquals(LOGIN_HEADER,LogInPage.getHeader().getText());
        Assertions.assertEquals(TITLE_H2,LogInPage.getTitleH2().getText());
        Assertions.assertEquals(TITLE_H5,LogInPage.getTitleH5().getText());
        Assertions.assertEquals(USERNAME_LABEL,LogInPage.getUsernameLabel().getText());
        Assertions.assertEquals(PASSWORD_LABEL,LogInPage.getPasswordLabel().getText());
    }

    @DisplayName("goToNewUserPage")
    @Test
    public void goToNewUserPageTest(){
        open(LOGIN_URL, LogInPage.class);
        LogInPage.getNewUserBtn().scrollIntoView(true);
        LogInPage.clickNewUserBtn();
        webdriver().shouldHave(url(REGISTER_URL));
    }

    // BOOKSTORE_PAGE TESTS
    @DisplayName("SearchByValidTitle")
    @Test
    public void searchByValidTitleTest(){
        open(BOOKSTORE_URL, BookStorePage.class);
        List<String> titles = BookStorePage.getBookTitles();
        String title = titles.get(new Random().nextInt(titles.size()-1));
        BookStorePage.findBooks(title);
        String author = BookStorePage.getBookAuthor(0).getText();
        String publisher = BookStorePage.getBookPublisher(0).getText();
        Assertions.assertEquals(title, BookStorePage.getBookTitles().get(0));
        Assertions.assertEquals(author, BookStorePage.getBookAuthors().get(0));
        Assertions.assertEquals(publisher,BookStorePage.getBookPublishers().get(0));
        BookStorePage.clearSearchBox();
    }

    @DisplayName("Check5RowsSelector")
    @Test
    public void check5RowsSelectorTest() {
        open(BOOKSTORE_URL, BookStorePage.class);
        BookStorePage.getRowsSelector().scrollIntoView(true);
        BookStorePage.selectRow("5");
        Assertions.assertFalse(BookStorePage.checkEnabledPrevBtn());
        Assertions.assertTrue(BookStorePage.checkEnabledNextBtn());
        BookStorePage.clickNextButton();
        Assertions.assertNotEquals("1", BookStorePage.getPageFieldValue());
    }

    @DisplayName("LastNextBtnNotActive")
    @Test
    public void lastNextBtnNotActiveTest() {
        open(BOOKSTORE_URL, BookStorePage.class);
        String numberOfPagesAsText = BookStorePage.getNumberOfPages().getText();
        int numberOfPages = Integer.parseInt(numberOfPagesAsText);
        BookStorePage.getNextButton().scrollIntoView(true);
        for (int i = 0; i < numberOfPages-1; i++) {
            BookStorePage.clickNextButton();
        }
        Assertions.assertFalse(BookStorePage.checkEnabledNextBtn());

    }

}
